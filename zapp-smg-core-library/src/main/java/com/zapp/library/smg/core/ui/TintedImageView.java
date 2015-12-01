package com.zapp.library.smg.core.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.zapp.library.smg.core.R;

/**
 * Tinted image view.
 *
 * @author msagi
 */
public class TintedImageView extends ImageView {

    /**
     * The tint color.
     */
    private int mTintColor = Color.TRANSPARENT;

    /**
     * {@inheritDoc}
     */
    public TintedImageView(final Context context) {
        this(context, null);
        initCustomAttributes(null);
    }

    /**
     * {@inheritDoc}
     */
    public TintedImageView(final Context context, final AttributeSet attrs) {
        this(context, attrs, 0);
        initCustomAttributes(attrs);
    }

    /**
     * {@inheritDoc}
     */
    public TintedImageView(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initCustomAttributes(attrs);
    }

    /**
     * Initialise custom attributes.
     *
     * @param attrs The attributes to initialise tinted image view from.
     */
    private void initCustomAttributes(final AttributeSet attrs) {
        final TypedArray a = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.TintedImageView, 0, 0);
        mTintColor = a.getColor(R.styleable.TintedImageView_tint, Color.TRANSPARENT);
        a.recycle();
        setColorFilter(mTintColor, PorterDuff.Mode.SRC_IN);
    }
}
