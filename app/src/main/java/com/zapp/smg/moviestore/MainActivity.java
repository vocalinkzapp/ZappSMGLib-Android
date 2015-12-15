package com.zapp.smg.moviestore;

import com.msagi.flashbus.annotation.Subscribe;
import com.zapp.library.smg.core.util.AppUtils;
import com.zapp.library.smg.mcom.model.LiteMcomTransaction;
import com.zapp.smg.moviestore.model.Movie;
import com.zapp.smg.moviestore.model.PaymentMethod;
import com.zapp.smg.moviestore.model.backend.AddItemToShoppingCartRequest;
import com.zapp.smg.moviestore.model.backend.CreateOrderRequest;
import com.zapp.smg.moviestore.model.backend.GetCatalogueRequest;
import com.zapp.smg.moviestore.model.backend.GetCatalogueResponse;
import com.zapp.smg.moviestore.model.backend.GetPaymentStatusRequest;
import com.zapp.smg.moviestore.model.backend.GetPaymentStatusResponse;
import com.zapp.smg.moviestore.model.backend.CreateOrderResponse;
import com.zapp.smg.moviestore.service.BackendAPIError;
import com.zapp.smg.moviestore.service.Events;
import com.zapp.smg.moviestore.ui.MovieListAdapter;
import com.zapp.smg.moviestore.ui.PaymentConfirmationDialogFragment;
import com.zapp.smg.moviestore.ui.SelectPaymentMethodDialogFragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    /**
     * Tag for logging.
     */
    private static final String TAG = MainActivity.class.getSimpleName();

    private FlashBus mBus;

    private ProgressBar mProgressIndicator;

    private RecyclerView mRecyclerView;

    private MovieListAdapter mAdapter;

    /**
     * A handy handler on the main thread.
     */
    private Handler mHandler = new Handler();

    private final MovieListAdapter.OnPurchaseListener mOnPurchaseListener = new MovieListAdapter.OnPurchaseListener() {
        @Override
        public void onPurchase(@NonNull final UUID itemUuid) {
            Log.e(TAG, "onPurchase: " + itemUuid);
            FlashBus.getDefault().post(new AddItemToShoppingCartRequest(itemUuid));
            final SelectPaymentMethodDialogFragment selectPaymentMethodDialogFragment = new SelectPaymentMethodDialogFragment();
            selectPaymentMethodDialogFragment.show(getSupportFragmentManager(), SelectPaymentMethodDialogFragment.TAG);
        }
    };

    /**
     * Scroll listener to forward scroll events to adapter
     */
    private RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {

        @Override
        public void onScrolled(final RecyclerView recyclerView, final int dx, final int dy) {
            mAdapter.onScroll(dy);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mBus = FlashBus.getDefault();

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //this feature is not yet supported
                mBus.post(new Events.DisplayFeatureNotSupportedMessage());
            }
        });

        mProgressIndicator = (ProgressBar) findViewById(R.id.toolbar_progress_bar);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        mRecyclerView.addOnScrollListener(mOnScrollListener);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        final RecyclerView.LayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new MovieListAdapter(mOnPurchaseListener);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onNewIntent(final Intent intent) {
        Log.e(TAG, "onNewIntent");

        super.onNewIntent(intent);
        if (intent != null) {
            final Uri uri = intent.getData();

            if (uri != null && uri.isHierarchical()) {
                //this branch is hit by the Bank App (re)opening the Merchant App on Payment Finish or Cancel (using intent)

                final String merchantTransactionId = uri.getAuthority();
                final String aptrId = uri.getQueryParameter("aptrId");

                Log.e(TAG, "merchantTransactionId: " + merchantTransactionId + ", aptrId: " + aptrId);

                try {
                    final UUID transactionId = UUID.fromString(merchantTransactionId);
                    mBus.register(this);
                    mBus.post(new GetPaymentStatusRequest(transactionId));
                } catch (Exception e) {
                    Log.e(TAG, "Error parsing data from Pay by Bank app CFI application", e);
                }
            }
        }
    }

    @Override
    protected void onStart() {
        Log.e(TAG, "onStart");
        super.onStart();
        mBus.register(this);
    }

    @Override
    protected void onResume() {
        Log.e(TAG, "onResume");
        super.onResume();

        //signal a request for movies
        mBus.post(new GetCatalogueRequest());
    }

    @Override
    protected void onStop() {
        Log.e(TAG, "onStop");
        super.onStop();
        mBus.unregister(this);
    }

    @Subscribe
    public void onDisplayFeatureNotSupportedMessage(@NonNull final Events.DisplayFeatureNotSupportedMessage event) {
        Snackbar.make(mRecyclerView, getString(R.string.feature_not_supported), Snackbar.LENGTH_SHORT).show();
    }

    @Subscribe
    public void onGetCatalogueResponse(@NonNull final GetCatalogueResponse catalogueResponse) {
        final Movie[] movies = catalogueResponse.getMovies();
        if (movies.length != 0) {
            Log.i(TAG, "Movie catalogue loaded: number of movies: " + movies.length);
            mAdapter.setMovies(catalogueResponse.getMovies());
 //           mProgressIndicator.setVisibility(View.INVISIBLE);
            mRecyclerView.setVisibility(View.VISIBLE);
        } else {
            Log.e(TAG, "No movies found in catalogue");
        }
    }

    @Subscribe
    public void onBackendAPIError(@NonNull final BackendAPIError error) {
        Log.e(TAG, "Backend API error", error.getError());
    }

    @Subscribe
    public void onCreateOrderRequest(@NonNull final CreateOrderRequest event) {
        Log.e(TAG, "Creating order...");
    }

    @Subscribe
    public void onCreateOrderResponse(@NonNull final CreateOrderResponse createOrderResponse) {
        Log.e(TAG, "Payment submitted");
        final PaymentMethod paymentMethod = createOrderResponse.getPaymentMethod();

        if (paymentMethod == PaymentMethod.PAY_BY_BANK_APP) {
            final LiteMcomTransaction liteTransaction = createOrderResponse.getPayByBankAppTransaction();
            AppUtils.openZappCFIApp(this, liteTransaction.getAptId(), liteTransaction.getAptrId());
        } else {
            Log.e(TAG, "Payment submitted with unknown payment method: " + paymentMethod);
        }
    }

    @Subscribe
    public void onGetPaymentStatusResponse(@NonNull final GetPaymentStatusResponse paymentStatusResponse) {
        Log.e(TAG, "Payment status received: " + paymentStatusResponse.getStatus());

        final GetPaymentStatusResponse.Status paymentStatus = paymentStatusResponse.getStatus();
        if (paymentStatus == GetPaymentStatusResponse.Status.AUTHORISED || paymentStatus == GetPaymentStatusResponse.Status.DECLINED) {
            final PaymentConfirmationDialogFragment paymentConfirmationDialogFragment = PaymentConfirmationDialogFragment.newInstance(paymentStatusResponse);
            paymentConfirmationDialogFragment.show(getSupportFragmentManager(), PaymentConfirmationDialogFragment.TAG);
        } else {
            //check payment status again in 2 seconds.
            mHandler.removeCallbacks(null);
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mBus.post(new GetPaymentStatusRequest(paymentStatusResponse.getTransactionId()));
                }
            }, 2000);
        }
    }

}
