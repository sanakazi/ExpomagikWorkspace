package com.conceptcandy.expomagik.navdrawer;


public class TypefaceUtil {

    /**
     * Using reflection to override default typeface
     * NOTICE: DO NOT FORGET TO SET TYPEFACE FOR APP THEME AS DEFAULT TYPEFACE WHICH WILL BE OVERRIDDEN
     * @param context to work with assets
     * @param defaultFontNameToOverride for example "monospace"
     * @param customFontFileNameInAssets file name of the font from assets
     */
//    public static void overrideFont(Context context) {
//        try {
//            final Typeface customFontTypeface = Typeface.createFromAsset(context.getAssets(), "abel.ttf");
//
//            final Field defaultFontTypefaceField = Typeface.class.getDeclaredField("SERIF");
//            defaultFontTypefaceField.setAccessible(true);
//            defaultFontTypefaceField.set(null, customFontTypeface);
//        } catch (Exception e) {
////            Log.e("Can not set custom font " + customFontFileNameInAssets + " instead of " + defaultFontNameToOverride);
//        }
//    }
//    
//    public static Typeface Font(Context context) {
//    	return Typeface.createFromAsset(context.getAssets(),"abel.ttf");
//    }
    
}