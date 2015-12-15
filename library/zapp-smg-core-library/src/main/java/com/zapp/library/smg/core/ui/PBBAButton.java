package com.zapp.library.smg.core.ui;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.zapp.library.smg.core.R;

/**
 * The Pay by Bank app button to be embedded in the merchant application to start the transaction.
 */
public class PBBAButton extends RelativeLayout implements View.OnClickListener {
    /**
     * The progress animation for the button.
     */
    private final TintedImageView mAnimatedIcon;

    /**
     * The Pay by Bank app icon on the button.
     */
    private final Drawable mIcon;

    /**
     * The Pay by Bank app icon animation on the button.
     */
    private final Drawable mIconAnimation;

    /**
     * The button view.
     */
    private final View mButtonContainer;

    /**
     * The buttons click listener.
     */
    private OnClickListener mClickListener;

    /**
     * Creates a new PBBAButton.
     *
     * @param context The view context.
     */
    public PBBAButton(final Context context) {
        this(context, null);
    }

    /**
     * Creates a new PBBAButton with AttributeSet.
     *
     * @param context The view context.
     * @param attrs   The styled attributes.
     */
    public PBBAButton(final Context context, final AttributeSet attrs) {
        super(context, attrs);

        final TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.PBBAButton, 0, 0);
        final int messageType = a.getInt(R.styleable.PBBAButton_message_type, 0);
        a.recycle();

        mIconAnimation = getDrawable(R.drawable.pbba_animation);
        mIcon = getDrawable(R.drawable.pbba_symbol_icon);

        final int layoutId = getLayoutId(messageType);
        final View view = inflate(getContext(), layoutId, this);
        mButtonContainer = view.findViewById(R.id.pbba_button_container);
        mAnimatedIcon = (TintedImageView) view.findViewById(R.id.progress);

        mAnimatedIcon.setImageDrawable(mIcon);
        mButtonContainer.setOnClickListener(this);
    }

    /**
     * Get the pay by bank app button layout id.
     * @param messageType The message type attribute.
     * @return The pay by bank app button layout id.
     */
    protected int getLayoutId(final int messageType) {
        return messageType == 0 ? R.layout.pbba_button_pay_by_bank_app : R.layout.pbba_button_get_code;
    }

    @Override
    public void onClick(final View v) {
        setEnabled(false);
        startAnimation();
        if (mClickListener != null) {
            mClickListener.onClick(this);
        }
    }

    @Override
    public void setEnabled(final boolean enabled) {
        super.setEnabled(enabled);
        mButtonContainer.setEnabled(enabled);
    }

    @Override
    public void setOnClickListener(final OnClickListener listener) {
        super.setOnClickListener(listener);
        this.mClickListener = listener;
    }

    public void startAnimation() {
        mAnimatedIcon.setImageDrawable(mIconAnimation);
        // Start the animation (looped playback by default).
        ((Animatable) mIconAnimation).start();
    }

    public void stopAnimation() {
        ((Animatable) mIconAnimation).stop();
        mAnimatedIcon.setImageDrawable(mIcon);
    }

    /**
     * Get drawable from resource id.
     *
     * @param drawableResId The resource id of the drawable.
     * @return The drawable instance.
     */
    @SuppressWarnings("deprecation")
    private Drawable getDrawable(@NonNull @DrawableRes final int drawableResId) {
        final Resources res = getResources();

        final Drawable drawable;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            drawable = res.getDrawable(drawableResId, null);
        } else {
            drawable = res.getDrawable(drawableResId);
        }
        return drawable;
    }
}
