package com.zapp.smg.moviestore.ui;

import com.zapp.library.smg.core.util.AppUtils;
import com.zapp.smg.moviestore.FlashBus;
import com.zapp.smg.moviestore.R;
import com.zapp.smg.moviestore.model.backend.CreateOrderRequest;
import com.zapp.smg.moviestore.service.Events;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.LinkedList;

import static com.zapp.smg.moviestore.ui.PaymentMethodListAdapter.PaymentMethod;

/**
 * Dialog fragment for purchase item feature.
 *
 * @author msagi
 */
public class SelectPaymentMethodDialogFragment extends DialogFragment implements AdapterView.OnItemClickListener {

    /**
     * Tag for tagging and logging.
     */
    public static final String TAG = SelectPaymentMethodDialogFragment.class.getSimpleName();

    private static final int PAY_BY_BANK_APP_PAYMENT_METHOD_POSITION = 1;

    private final LinkedList<PaymentMethod> mPaymentMethods = new LinkedList<>();

    private ListView mListView;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.content_select_payment_method_dialog, container, /* attachToRoot */ false);

        mListView = (ListView) view.findViewById(R.id.payment_method_list_view);
        mListView.setOnItemClickListener(this);

        return view;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        final Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setTitle(R.string.select_payment_method_dialog_title_text);
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mPaymentMethods.add(new PaymentMethod(R.drawable.payment_method_diners_club, R.string.payment_option_label_text_diners_club));
        mPaymentMethods.add(new PaymentMethod(R.drawable.payment_method_discover, R.string.payment_option_label_text_discover));
        mPaymentMethods.add(new PaymentMethod(R.drawable.payment_method_mastercard, R.string.payment_option_label_text_mastercard));
        mPaymentMethods.add(new PaymentMethod(R.drawable.payment_method_paypal, R.string.payment_option_label_text_paypal));
        mPaymentMethods.add(new PaymentMethod(R.drawable.payment_method_visa, R.string.payment_option_label_text_visa));

        //check for Pay by Bank app payment method availability
        if (AppUtils.isZappCFIAppAvailable(getContext())) {
            mPaymentMethods.add(PAY_BY_BANK_APP_PAYMENT_METHOD_POSITION, new PaymentMethod(R.drawable.payment_method_pbba, R.string.payment_option_label_text_pbba));
        }

        final PaymentMethodListAdapter paymentMethodListAdapter = new PaymentMethodListAdapter(getContext(), mPaymentMethods);
        mListView.setAdapter(paymentMethodListAdapter);
    }

    @Override
    public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {

        if (AppUtils.isZappCFIAppAvailable(getContext()) && position == PAY_BY_BANK_APP_PAYMENT_METHOD_POSITION) {
            Log.e(TAG, "PBBA payment option clicked");
            FlashBus.getDefault().post(new CreateOrderRequest(com.zapp.smg.moviestore.model.PaymentMethod.PAY_BY_BANK_APP));
        } else {
            //no payment methods are supported in this demo except Pay by Bank app
            FlashBus.getDefault().post(new Events.DisplayFeatureNotSupportedMessage());
        }

        dismiss();
    }
}