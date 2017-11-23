package com.strategy.intecom.vtc.fixrepairer.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.strategy.intecom.vtc.fixrepairer.view.base.AppCore;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Pattern;

/**
 * Created by Mr. Ha on 5/16/16.
 */
public class Utils {

    private static final String TAG = "Utils";

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    /**
     * Checks if the device has Internet connection.
     *
     * @return <code>true</code> if the phone is connected to the Internet.
     */
    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(
                Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifiNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiNetwork != null && wifiNetwork.isConnected()) {
            return true;
        }

        NetworkInfo mobileNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (mobileNetwork != null && mobileNetwork.isConnected()) {
            return true;
        }

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected()) {
            return true;
        }

        return false;
    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    public static boolean checkPlayServices(final Activity context) {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(context);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(context, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                AppCore.showLog(TAG, "This device is not supported.");
            }
            return false;
        }
        return true;
    }

    public static void getHashKey(Context context) {

        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;

                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                AppCore.showLog("KeyHash ----- : ", something);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            AppCore.showLog(" ------ name not found", e1.toString());
        }

        catch (NoSuchAlgorithmException e) {
            AppCore.showLog(" --------- no such an algorithm " + e.toString());
        } catch (Exception e) {
            AppCore.showLog(" -------- exception" +  e.toString());
        }
    }

    /**
     * Convert dp to Pixels
     */
    public static float convertDpToPixels(float dp,Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi/160f);
        return px;
    }

    /**
     * Convert Pixel to dp
     */
    public static float convertPixelsToDp(float px,Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / (metrics.densityDpi / 160f);
        return dp;
    }

    /**
	 * Get Bitmap Resource
	 */
    public static Bitmap getBitmapResource(Context context, int id) {
        if(context == null)
            return null;
        return BitmapFactory.decodeResource(context.getResources(), id);
    }
    /**
     * Get Bitmap Option
     */
    public static BitmapFactory.Options getBitmapOption() {
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inDither = false;
        bitmapOptions.inJustDecodeBounds = false;
        bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;
        bitmapOptions.inSampleSize = 1;
        bitmapOptions.inPurgeable = true;
        bitmapOptions.inPreferQualityOverSpeed = true;
        bitmapOptions.inTempStorage=new byte[32 * 1024];
        return bitmapOptions;
    }

    /**
     * Check File
     */
    public static File isFile(String sPath, String sFileName) {
        File f = new File(sPath);
        for (File temp : f.listFiles()) {
            if (temp.getName().equals(sFileName)) {
                return temp;
            }
        }
        return f;
    }

    /**
     * Get Size Screen
     */
    public static int[] getSizeScreen(Activity context){
        Display display = context.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        int []screen = new int[2];

        screen[0] = width;
        screen[1] = height;

        int screenSize = context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;
        switch (screenSize) {
            case Configuration.SCREENLAYOUT_SIZE_LARGE:
                AppCore.showLog("Large screen");
                break;
            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                AppCore.showLog("Normal screen");
                break;
            case Configuration.SCREENLAYOUT_SIZE_SMALL:
                AppCore.showLog("Small screen");
                break;
            default:
                AppCore.showLog("Screen size is neither large, normal or small");
        }
        return screen;
    }

    public static void hideKeyboard(Activity activity){
        // Check if no view has focus:
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static String getIMEI(Context context){

        TelephonyManager mngr = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
        String imei = mngr.getDeviceId();
        return imei;

    }

    public static String initConvertTimeDisplayLong(String strTime) {

        String formattedDate = "";
        try {
            Locale.setDefault(new Locale("vi", "VN"));
            TimeZone tz = TimeZone.getTimeZone("GMT+14:00");
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
            DateFormat dateFormat2 = new SimpleDateFormat("EE HH:mm dd/MM/yyyy", Locale.getDefault());
            dateFormat2.setTimeZone(tz);
            Date date = dateFormat.parse(strTime);
            formattedDate = dateFormat2.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return formattedDate;
    }

    public static String initConvertTimeDisplayShort(String strTime) {

        String formattedDate = "";
        try {
            Locale.setDefault(new Locale("vi", "VN"));
            TimeZone tz = TimeZone.getTimeZone("GMT+14:00");
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
            DateFormat dateFormat2 = new SimpleDateFormat("HH:mm dd/MM", Locale.getDefault());
            dateFormat2.setTimeZone(tz);
            Date date = dateFormat.parse(strTime);
            formattedDate = dateFormat2.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return formattedDate;
    }

    public static boolean validateCMTND(String email) {
        if (email.matches("^*[0-9]{9,12}")) return true;
        return false;
    }

    public static boolean validateEmail(String email) {
        if (email.matches("^[_A-Za-z0-9-+]+(.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(.[A-Za-z0-9]+)*(.[A-Za-z]{2,})$")) return true;
        return false;
    }

    public static boolean validatePhoneNumber(String phoneNo) {
        if (phoneNo.matches("^0[1-9][0-9]{8,9}")) return true;
        return false;
    }

    public static int isNameValid(String name) {

        if (TextUtils.isEmpty(name)) {
            return Const.VALIDATE_RESULT_NAME_NULL;
        }

        if (name.length() < Const.VALIDATE_NAME_MIN_LENGTH || name.length() > Const.VALIDATE_NAME_MAX_LENGTH) {
            return Const.VALIDATE_RESULT_NAME_WRONG_LENGTH;
        }

        // TODO need to update logic to validate number, space and vietnamese character
        Pattern p =
//                Pattern.compile("[a-zA-Z ]+");
                Pattern.compile("[ a-zA-Z_ÀÁÂÃÈÉÊÌÍÒÓÔÕÙÚĂĐĨŨƠàáâãèéêìíòóôõùúăđĩũơƯĂẠẢẤẦẨẪẬẮẰẲẴẶẸẺẼỀỀỂưăạảấầẩẫậắằẳẵặẹẻẽềềểỄỆỈỊỌỎỐỒỔỖỘỚỜỞỠỢỤỦỨỪễệỉịọỏốồổỗộớờởỡợụủứừỬỮỰỲỴÝỶỸửữựỳỵỷỹ]+");

        if (!p.matcher(name).matches()) {
            return Const.VALIDATE_RESULT_NAME_NOT_CORRECT;
        }

        return Const.VALIDATE_RESULT_NAME_OK;
    }

    public static String formatDecimal(float number) {
        return String.format(Locale.getDefault(), "%,.0f", number);
    }

    public static String convertK(long count) {
        if (count < 1000) return "" + count;
        int exp = (int) (Math.log(count) / Math.log(1000));

        double result = (count / Math.pow(1000, exp));

        if(result <= ((double) exp)){
            return String.format(Locale.getDefault(), "%.0f %c", count / Math.pow(1000, exp), "kMGTPE".charAt((int) exp-1));
        }else {
            return String.format(Locale.getDefault(), "%.1f %c", count / Math.pow(1000, exp), "kMGTPE".charAt((int) exp-1));
        }
    }

    public static String initTextBold(String str) {
        return String.valueOf("<b>" + str + "</b>");
    }
}
