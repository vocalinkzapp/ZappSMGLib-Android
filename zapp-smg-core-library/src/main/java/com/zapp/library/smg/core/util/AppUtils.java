package com.zapp.library.smg.core.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.zapp.library.smg.core.R;

/**
 * App utility class.
 * @author msagi
 */
public final class AppUtils {

    public static final String ZAPP_SCHEME = "zapp://";

    /**
     * Hidden constructor for utility class.
     */
    private AppUtils() {
    }

    /**
     * Checks if there is any of the CFI apps installed on the device supports PBBA payment.
     *
     * @param context application context
     * @return true if available
     */
    public static boolean isZappCFIAppAvailable(@NonNull final Context context) {
        final Intent zappIntent = new Intent();
        zappIntent.setData(new Uri.Builder().scheme(ZAPP_SCHEME).build());
        zappIntent.setAction(Intent.ACTION_VIEW);
        final ResolveInfo resolveInfo = context.getPackageManager().resolveActivity(zappIntent, PackageManager.MATCH_DEFAULT_ONLY);
        return resolveInfo != null;
    }

    /**
     * Open PBBA enabled CFI application.
     *
     * @param activity The activity which starts the banking application.
     * @param aptId    used to build the intent
     * @param aptrId   used to build the intent
     */
    public static void openZappCFIApp(@NonNull Activity activity, @NonNull final String aptId, @NonNull final String aptrId) {
        if (!activity.isFinishing()) {
            final Uri appUri = Uri.parse(ZAPP_SCHEME + aptId + '/' + aptrId);
            final Intent bankingAppStartIntent = new Intent(Intent.ACTION_VIEW, appUri);
            activity.startActivity(bankingAppStartIntent);
        }
    }

}
