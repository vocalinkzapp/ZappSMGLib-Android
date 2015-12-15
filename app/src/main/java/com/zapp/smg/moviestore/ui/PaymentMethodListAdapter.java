package com.zapp.smg.moviestore.ui;

import com.zapp.smg.moviestore.R;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by msagi on 11/12/2015.
 */
public class PaymentMethodListAdapter extends ArrayAdapter<PaymentMethodListAdapter.PaymentMethod> {

    public static class PaymentMethod {
        private final int mImageResId;
        private final int mTextResId;

        public PaymentMethod(final int imageResId, final int textResId) {
            mImageResId = imageResId;
            mTextResId = textResId;
        }
    }

    public PaymentMethodListAdapter(@NonNull final Context context, @NonNull final List<PaymentMethod> paymentMethods) {
        super(context, R.layout.view_payment_method_list_item, paymentMethods);
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {

        final PaymentMethod paymentMethod = getItem(position);

        //this adapter is for the payment method list, there is no need for view holder pattern as
        //this list will not be scrollable

        final View view = View.inflate(getContext(), R.layout.view_payment_method_list_item, null);
        final ImageView paymentMethodImage = (ImageView) view.findViewById(R.id.payment_method_image);
        paymentMethodImage.setImageResource(paymentMethod.mImageResId);

        final TextView paymentMethodText = (TextView) view.findViewById(R.id.payment_method_text);
        paymentMethodText.setText(paymentMethod.mTextResId);

        return view;
    }
}
