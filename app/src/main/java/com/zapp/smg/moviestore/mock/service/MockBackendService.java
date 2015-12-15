package com.zapp.smg.moviestore.mock.service;

import com.google.gson.Gson;

import com.msagi.flashbus.annotation.Subscribe;
import com.msagi.flashbus.annotation.ThreadId;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import com.zapp.library.smg.core.model.LitePaymentStatus;
import com.zapp.library.smg.core.model.network.LitePaymentStatusResponse;
import com.zapp.library.smg.mcom.model.LiteMcomPaymentRequest;
import com.zapp.library.smg.mcom.model.LiteMcomTransaction;
import com.zapp.smg.moviestore.FlashBus;
import com.zapp.smg.moviestore.R;
import com.zapp.smg.moviestore.mock.service.zapp.smg.ZappSMGService;
import com.zapp.smg.moviestore.model.Movie;
import com.zapp.smg.moviestore.model.PaymentMethod;
import com.zapp.smg.moviestore.model.ShoppingCart;
import com.zapp.smg.moviestore.model.backend.AddItemToShoppingCartRequest;
import com.zapp.smg.moviestore.model.backend.CreateOrderRequest;
import com.zapp.smg.moviestore.model.backend.CreateOrderResponse;
import com.zapp.smg.moviestore.model.backend.GetCatalogueRequest;
import com.zapp.smg.moviestore.model.backend.GetCatalogueResponse;
import com.zapp.smg.moviestore.model.backend.GetPaymentStatusRequest;
import com.zapp.smg.moviestore.model.backend.GetPaymentStatusResponse;
import com.zapp.smg.moviestore.service.BackendService;

import android.content.Context;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Hashtable;
import java.util.UUID;

/**
 * Mock backend service which mocks every backend calls.
 */
public class MockBackendService extends BackendService {

    /**
     * Tag for logging.
     */
    private static final String TAG = MockBackendService.class.getSimpleName();

    /**
     * The FlashBus instance.
     */
    private FlashBus mBus;

    /**
     * The application context.
     */
    private final Context mAppContext;

    /**
     * The GSON (JSON format utility) instance.
     */
    private final Gson mGson = new Gson();

    /**
     * The mock web server to mock Merchant backend services.
     */
    private MockWebServer mMockWebServer;

    /**
     * The shopping cart (simulates the shopping cart related to the user session on the Merchant gateway).
     */
    private ShoppingCart mShoppingCart = new ShoppingCart();

    /**
     * Countdown timer to check Zapp payment status periodically (simulates the Merchant gateway service which polls Zapp Small Merchant Gateway for the
     * status of the Zapp payment).
     */
    private CountDownTimer mZappPaymentCountdownTimer;

    /**
     * The proxy for Zapp Small Merchant Gateway service.
     */
    private ZappSMGService mZappSMGService = new ZappSMGService();

    /**
     * Cache table to store the Zapp payment statuses (simulates payment status cache table on the Merchant gateway).
     */
    private final Hashtable<String, LitePaymentStatusResponse> mZappPaymentStatusCache = new Hashtable<>();

    /**
     * Cache table to store the Merchant payment statuses (simulates payment cache table on the Merchant gateway).
     */
    private final Hashtable<String, CreateOrderResponse> mPaymentCache = new Hashtable<>();

    /**
     * Create new instance.
     */
    public MockBackendService(@NonNull final Context appContext) {
        super();
        mAppContext = appContext;
        mBus = FlashBus.getDefault();
        mBus.register(this);
        mBus.post(new MockEvents.StartMockWebServerEvent());
    }

    @Override
    protected String getGatewayBaseUrl() {
        return "http://127.0.0.1:8080/";
    }

    @Subscribe(thread = ThreadId.BACKGROUND)
    public void onStartMockWebServer(@NonNull final MockEvents.StartMockWebServerEvent event) {
        try {
            mMockWebServer = new MockWebServer();
            mMockWebServer.start(8080);
            Log.i(TAG, "Mock web server is active on port 8080");
        } catch (Exception e) {
            Log.e(TAG, "Failed to start mock web server", e);
        }
    }

    @Subscribe(thread = ThreadId.BACKGROUND)
    @Override
    public void getCatalogue(@NonNull final GetCatalogueRequest event) {

        final String mockMovieListResponseBody = loadAssetAsString("mock/getMovieListResponse.json");
        if (mockMovieListResponseBody == null) {
            throw new IllegalStateException("No mock asset for 'get movie list' found");
        }

        //mock response
        mMockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(mockMovieListResponseBody));

        //call parent
        super.getCatalogue(event);
    }

    @Subscribe(thread = ThreadId.BACKGROUND)
    @Override
    public void addItemToShoppingCart(@NonNull final AddItemToShoppingCartRequest event) {

        final String mockMovieList = loadAssetAsString("mock/getMovieListResponse.json");
        final GetCatalogueResponse catalogueResponse = mGson.fromJson(mockMovieList, GetCatalogueResponse.class);
        final Movie[] movies = catalogueResponse.getMovies();

        final UUID itemUUD = event.getItemUUID();

        Movie movieToPurchase = null;
        for (final Movie movie : movies) {
            if (movie.getId().equals(itemUUD)) {
                movieToPurchase = movie;
                break;
            }
        }

        if (movieToPurchase == null) {
            //TODO movie selected with UUID cannot be found in the movie catalogue.
        }

        mShoppingCart.setMovie(movieToPurchase);

        final String mockAddItemToShoppingCartResponseBody = loadAssetAsString("mock/addItemToShoppingCartResponse.json");
        if (mockAddItemToShoppingCartResponseBody == null) {
            throw new IllegalStateException("No mock asset for 'add item to shopping cart' request found");
        }

        //mock response
        mMockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(mockAddItemToShoppingCartResponseBody));

        super.addItemToShoppingCart(event);
    }

    @Subscribe(thread = ThreadId.BACKGROUND)
    @Override
    public void createOrder(@NonNull final CreateOrderRequest event) {

        final PaymentMethod paymentMethod = event.getPaymentMethod();

        if (paymentMethod != PaymentMethod.PAY_BY_BANK_APP) {
            throw new IllegalStateException("This demo supports Pay by Bank app payment method only");
        }

        try {
            //create new id for the transaction
            final UUID merchantTransactionId = UUID.randomUUID();
            final String zappMerchantId = mAppContext.getString(R.string.pbba_merchant_id);
            final String zappMerchantCallbackUrlScheme = mAppContext.getString(R.string.pbba_callback_url_scheme);
            final String zappMerchantCallbackUrl = String.format("%s://%s", zappMerchantCallbackUrlScheme, merchantTransactionId.toString());

            //create new Zapp Small Merchant Gateway payment request
            final LiteMcomPaymentRequest zappSmgPaymentRequest = new LiteMcomPaymentRequest(mShoppingCart.getTotalAmount(), zappMerchantId, zappMerchantCallbackUrl,
                    merchantTransactionId.toString(), /* AdHoc */ null);

            //submit Zapp Small Merchant Gateway payment request to the Zapp Small Merchant Gateway
            final LiteMcomTransaction liteTransaction = mZappSMGService.submitPaymentRequest(zappSmgPaymentRequest);

            //if Zapp transaction is created successfully
            if (liteTransaction != null) {
                final long paymentStatusCheckInterval = 5000L;
                final String aptrId = liteTransaction.getAptrId();

                mZappPaymentCountdownTimer = new CountDownTimer(liteTransaction.getConfirmationExpiryInterval() * 1000, paymentStatusCheckInterval) {
                    @Override
                    public void onTick(final long millisUntilFinished) {
                        pollZappPaymentStatus(aptrId);
                    }

                    @Override
                    public void onFinish() {
                        pollZappPaymentStatus(aptrId);
                    }
                };
                mZappPaymentCountdownTimer.start();

                final CreateOrderResponse createOrderResponse = new CreateOrderResponse();
                createOrderResponse.setPaymentMethod(PaymentMethod.PAY_BY_BANK_APP); //this is the only supported method in this demo
                createOrderResponse.setTransactionId(merchantTransactionId);
                createOrderResponse.setPayByBankAppTransaction(liteTransaction);
                createOrderResponse.setMovie(mShoppingCart.getMovie());

                //store Merchant payment
                mPaymentCache.put(merchantTransactionId.toString(), createOrderResponse);

                // mock Merchant backend response
                final String mockSubmitPaymentResponseBody = mGson.toJson(createOrderResponse, CreateOrderResponse.class);
                mMockWebServer.enqueue(new MockResponse()
                        .setResponseCode(200)
                        .setBody(mockSubmitPaymentResponseBody));

                super.createOrder(event);
            } else {
                final String mockSubmitPaymentRequestErrorBody = loadAssetAsString("mock/submitPaymentRequestErrorResponse.json");
                if (mockSubmitPaymentRequestErrorBody == null) {
                    throw new IllegalStateException("No mock asset for 'submit payment request error' request found");
                }

                // mock Merchant backend response
                mMockWebServer.enqueue(new MockResponse()
                        .setResponseCode(500)
                        .setBody(mockSubmitPaymentRequestErrorBody));

            }
        } catch (Exception e) {
            Log.e(TAG, "Error submitting payment request", e);
        }
    }

    @Subscribe(thread = ThreadId.BACKGROUND)
    @Override
    public void getPaymentStatus(@NonNull final GetPaymentStatusRequest event) {

        final UUID transactionId = event.getTransactionId();
        final CreateOrderResponse merchantPayment = mPaymentCache.get(transactionId.toString());

        final GetPaymentStatusResponse paymentStatusResponse = new GetPaymentStatusResponse();
        paymentStatusResponse.setTransactionId(transactionId);

        if (merchantPayment == null) {

            Log.e(TAG, "Cannot get payment status for unknown transactionId: " + transactionId.toString());

        } else {

            final PaymentMethod paymentMethod = merchantPayment.getPaymentMethod();
            if (paymentMethod == PaymentMethod.PAY_BY_BANK_APP) {

                final LitePaymentStatusResponse litePaymentStatusResponse = mZappPaymentStatusCache.get(merchantPayment.getPayByBankAppTransaction().getAptrId());

                if (litePaymentStatusResponse != null) {
                    final boolean isZappPaymentAuthorised =
                            litePaymentStatusResponse.getPaymentStatus() == LitePaymentStatus.AUTHORISED ||
                                    litePaymentStatusResponse.getPaymentStatus() == LitePaymentStatus.AWAITING_SETTLEMENT;

                    final GetPaymentStatusResponse.Status paymentStatus = isZappPaymentAuthorised ? GetPaymentStatusResponse.Status.AUTHORISED
                            : GetPaymentStatusResponse.Status.DECLINED;
                    paymentStatusResponse.setStatus(paymentStatus);
                    paymentStatusResponse.setMovie(merchantPayment.getMovie());
                } else {
                    paymentStatusResponse.setStatus(GetPaymentStatusResponse.Status.IN_PROGRESS);
                }

            } else {
                Log.e(TAG, "Cannot get payment status for unsupported payment method: " + paymentMethod + ", transactionId: " + transactionId.toString());
            }
        }

        Log.e(TAG, "Payment status: transactionId: " + transactionId + ", status: " + paymentStatusResponse.getStatus());

        final String mockGetPaymentStatusBody = mGson.toJson(paymentStatusResponse, GetPaymentStatusResponse.class);
        mMockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(mockGetPaymentStatusBody));

        super.getPaymentStatus(event);
    }

    /**
     * Retrieve payment status from the Zapp Small Merchant Gateway.
     *
     * @param aptrId The id of the Zapp payment transaction.
     */
    private void pollZappPaymentStatus(@NonNull final String aptrId) {
        Log.e(TAG, "Poll Zapp Small Merchant Gateway for payment status: aptrId: " + aptrId);
        final LitePaymentStatusResponse zappPaymentStatus = mZappSMGService.checkPaymentStatus(aptrId);
        if (zappPaymentStatus != null) {
            final LitePaymentStatus paymentStatus = zappPaymentStatus.getPaymentStatus();

            //cache the Zapp Small Merchant Gateway response
            mZappPaymentStatusCache.put(aptrId, zappPaymentStatus);

            Log.e(TAG, "New Zapp payment status: " + paymentStatus);
            if (paymentStatus != LitePaymentStatus.IN_PROGRESS) {
                mZappPaymentCountdownTimer.cancel();
                mZappPaymentCountdownTimer = null;
            }
        } else {
            Log.e(TAG, "New Zapp payment status: unknown");
        }
    }

    /**
     * Load an asset file to string.
     *
     * @param assetPath The path of the asset file.
     * @return The string content of the asset file or null if error happened during loading.
     */
    private String loadAssetAsString(@NonNull final String assetPath) {

        final StringBuilder stringBuilder = new StringBuilder();
        try (final InputStream inputStream = mAppContext.getAssets().open(assetPath);
             final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"))) {
            String str;

            while ((str = bufferedReader.readLine()) != null) {
                stringBuilder.append(str);
            }
        } catch (IOException ioe) {
            Log.e(TAG, "Error loading asset: " + assetPath, ioe);
            return null;
        }
        return stringBuilder.toString();
    }


}
