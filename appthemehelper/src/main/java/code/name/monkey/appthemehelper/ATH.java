package code.name.monkey.appthemehelper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;

import code.name.monkey.appthemehelper.util.ColorUtil;
import code.name.monkey.appthemehelper.util.TintHelper;
import code.name.monkey.appthemehelper.util.ToolbarContentTintHelper;

/**
 * @author Karim Abou Zeid (kabouzeid)
 */
public final class ATH {

    private ATH() {
    }

    @SuppressLint("CommitPrefEdits")
    static boolean didThemeValuesChange(@NonNull Context context, long since) {
        return ThemeStore.isConfigured(context) && ThemeStore.prefs(context).getLong(ThemeStore.VALUES_CHANGED, -1) > since;
    }

    public static void setStatusbarColorAuto(Activity activity) {
        setStatusbarColor(activity, ThemeStore.statusBarColor(activity));
    }

    public static void setStatusbarColor(Activity activity, int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().setStatusBarColor(color);
            setLightStatusbarAuto(activity, color);
        }
    }

    public static void setLightStatusbarAuto(Activity activity, int bgColor) {
        setLightStatusbar(activity, ColorUtil.isColorLight(bgColor));
    }

    public static void setLightStatusbar(Activity activity, boolean enabled) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            final View decorView = activity.getWindow().getDecorView();
            final int systemUiVisibility = decorView.getSystemUiVisibility();
            if (enabled) {
                decorView.setSystemUiVisibility(systemUiVisibility | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            } else {
                decorView.setSystemUiVisibility(systemUiVisibility & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        }
    }

    public static void setNavigationbarColorAuto(Activity activity) {
        setNavigationbarColor(activity, ThemeStore.navigationBarColor(activity));
    }

    public static void setNavigationbarColor(Activity activity, int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().setNavigationBarColor(color);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O &&
                ColorUtil.isColorLight(ThemeStore.navigationBarColor(activity))) {
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        }
    }

    public static void setActivityToolbarColorAuto(Activity activity, @Nullable Toolbar toolbar) {
        setActivityToolbarColor(activity, toolbar, ThemeStore.primaryColor(activity));
    }

    public static void setActivityToolbarColor(Activity activity, @Nullable Toolbar toolbar, int color) {
        if (toolbar == null) return;
        toolbar.setBackgroundColor(color);
        ToolbarContentTintHelper.setToolbarContentColorBasedOnToolbarColor(activity, toolbar, color);
    }

    public static void setTaskDescriptionColorAuto(@NonNull Activity activity) {
        setTaskDescriptionColor(activity, ThemeStore.primaryColor(activity));
    }

    public static void setTaskDescriptionColor(@NonNull Activity activity, @ColorInt int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Task description requires fully opaque color
            color = ColorUtil.stripAlpha(color);
            // Sets color of entry in the system recents page
            activity.setTaskDescription(new ActivityManager.TaskDescription((String) activity.getTitle(), null, color));
        }
    }

    public static void setTint(@NonNull View view, @ColorInt int color) {
        TintHelper.setTintAuto(view, color, false);
    }

    public static void setBackgroundTint(@NonNull View view, @ColorInt int color) {
        TintHelper.setTintAuto(view, color, true);
    }
}