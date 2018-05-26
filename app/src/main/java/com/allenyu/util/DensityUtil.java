package com.allenyu.util;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

/**
 * Created by magical.zhang on 2017/2/15.
 * Description : 屏幕信息 尺寸转换
 */
public class DensityUtil {

    private static float density = 1;

    static {
        density = Resources.getSystem().getDisplayMetrics().density;
    }

    public static int dp2px(float dpValue) {
        return (int) (dpValue * density + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    public static int sp2px(float spValue) {
        final float fontScale = Resources.getSystem().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    private static WindowManager getWindowManager(Context context) {
        Object service = context.getSystemService(Context.WINDOW_SERVICE);
        if (service == null) return null;

        return (WindowManager) service;
    }

    private static Display getDefaultDisplay(Context context) {
        WindowManager wm = getWindowManager(context);
        if (wm == null) return null;

        return wm.getDefaultDisplay();
    }


    private static DisplayMetrics getDisplayMetrics(Context context) {
        Display display = getDefaultDisplay(context);
        if (display != null) {
            DisplayMetrics result = new DisplayMetrics();
            display.getMetrics(result);
            return result;
        }
        return null;
    }

    /**
     * 计算 系统状态栏高度
     */
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId =
                context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 计算 有些手机底部导航键高度
     */
    public static int getNavigationBarHeight(Context context) {
        Resources resources = context.getResources();
        int result = resources.getDimensionPixelSize(
                resources.getIdentifier("navigation_bar_height", "dimen", "android"));
        return result;
    }
}
