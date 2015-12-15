package com.zapp.smg.moviestore.ui;

import com.zapp.smg.moviestore.R;
import com.zapp.smg.moviestore.model.backend.GetPaymentStatusResponse;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

/**
 * Dialog fragment for payment confirmation feature.
 *
 * @author msagi
 */
public class PaymentConfirmationDialogFragment extends DialogFragment {

    /**
     * Tag for tagging and logging.
     */
    public static final String TAG = PaymentConfirmationDialogFragment.class.getSimpleName();

    private static final String ARG_PAYMENT_STATUS = "arg.paymentStatus";
    private static final String ARG_MOVIE_TITLE = "arg.movieTitle";

    public static PaymentConfirmationDialogFragment newInstance(@NonNull final GetPaymentStatusResponse paymentStatusResponse) {
        final Bundle arguments = new Bundle();

        final GetPaymentStatusResponse.Status paymentStatus = paymentStatusResponse.getStatus();
        if (paymentStatus != GetPaymentStatusResponse.Status.AUTHORISED && paymentStatus != GetPaymentStatusResponse.Status.DECLINED) {
            throw new IllegalArgumentException("payment status not in [AUTHORISED, DECLINED]");
        }
        arguments.putSerializable(ARG_PAYMENT_STATUS, paymentStatus);

        final String movieTitle = paymentStatusResponse.getMovie().getTitle();
        if (movieTitle == null) {
            throw new IllegalArgumentException("movieTitle == null");
        }
        arguments.putString(ARG_MOVIE_TITLE, movieTitle);

        final PaymentConfirmationDialogFragment fragment = new PaymentConfirmationDialogFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        final GetPaymentStatusResponse.Status paymentStatus = (GetPaymentStatusResponse.Status) getArguments().getSerializable(ARG_PAYMENT_STATUS);
        if (paymentStatus == null) {
            throw new IllegalStateException("Payment status argument is required");
        }

        final String dialogTitle = getDialogTitleText(paymentStatus);
        final String dialogMessage = getDialogMessageText(paymentStatus);

        return new AlertDialog.Builder(getContext())
                .setTitle(dialogTitle)
                .setMessage(dialogMessage)
                .setCancelable(false)
                .setPositiveButton(R.string.payment_confirmation_dialog_button_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, final int which) {
                        dismiss();
                    }
                })
                .create();
    }

    /**
     * Get the dialog title text based on the payment status.
     *
     * @param paymentStatus The payment status.
     * @return The text for the given payment status.
     */
    private String getDialogTitleText(@NonNull final GetPaymentStatusResponse.Status paymentStatus) {

        switch (paymentStatus) {

            case AUTHORISED:
                return getString(R.string.payment_status_authorised);
            case DECLINED:
                return getString(R.string.payment_status_declined);
        }

        return null;
    }


    /**
     * Get the dialog message text based on the payment status.
     *
     * @param paymentStatus The payment status.
     * @return The text for the given payment status.
     */
    private String getDialogMessageText(@NonNull final GetPaymentStatusResponse.Status paymentStatus) {

        final String movieTitle = getArguments().getString(ARG_MOVIE_TITLE);

        switch (paymentStatus) {

            case AUTHORISED:
                return getString(R.string.payment_status_message_authorised, movieTitle);
            case DECLINED:
                return getString(R.string.payment_status_message_declined, movieTitle);
        }

        return null;
    }
}

