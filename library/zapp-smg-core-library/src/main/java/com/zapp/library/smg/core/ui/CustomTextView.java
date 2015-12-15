package com.zapp.library.smg.core.ui;

import com.zapp.library.smg.core.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.text.Html;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

/**
 * A custom text view that allows to specify the custom Zapp to it.
 * <br>
 * Created by gdragan on 8/7/2014.
 */
public class CustomTextView extends TextView {

    /**
     * Tag for logging.
     */
    private static final String TAG = CustomTextView.class.getSimpleName();

    /**
     * Create new instance.
     *
     * @param context The context to use.
     */
    public CustomTextView(final Context context) {
        super(context);
        init(context, null);
    }

    /**
     * Create new instance.
     *
     * @param context The context to use.
     * @param attrs   The attribute set to use.
     */
    public CustomTextView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    /**
     * Create new instance.
     *
     * @param context  The context to use.
     * @param attrs    The attribute set to use.
     * @param defStyle The default stype id to use.
     */
    public CustomTextView(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    /**
     * Initialize custom view.
     *
     * @param context The context to use.
     * @param attrs   The attribute set to use.
     */
    private void init(final Context context, final AttributeSet attrs) {
        final TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomTextView, 0, 0);

        try {
            final String fontName = a.getString(R.styleable.CustomTextView_fontName);
            applyTypeface(context, fontName);
            final String htmlText = a.getString(R.styleable.CustomTextView_htmlText);
            if (!TextUtils.isEmpty(htmlText)) {
                setText(Html.fromHtml(htmlText));
            }
        } catch (Exception e) {
            Log.w(TAG, "Custom font hasn't been provided");
        } finally {
            a.recycle();
        }
    }

    /**
     * Apply type face to this custom text view.
     *
     * @param context  The context to use.
     * @param fontName The font name to use.
     */
    private void applyTypeface(final Context context, final String fontName) {
        Typeface customFont = Typeface.createFromAsset(context.getAssets(), fontName);
        setTypeface(customFont);
    }
}
