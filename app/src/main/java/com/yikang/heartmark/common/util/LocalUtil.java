package com.yikang.heartmark.common.util;

import android.content.res.Configuration;

import com.yikang.heartmark.application.AppContext;

import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by chuanyhu on 2014/8/26.
 */
public class LocalUtil {

    public static String getCurLang() {
        AppContext appContext = AppContext.getAppContext();
        Configuration config = appContext.getResources().getConfiguration();
        String language = SharedPreferencesUtils.getString(appContext, "language", null);
        if (language == null || language.isEmpty()) {
            return Locale.getDefault().getLanguage();
        } else {
            return language;
        }
    }

    public static String getTimeZone() {
        TimeZone tz = TimeZone.getDefault();
        String str = String.format("%s (GMT%+d) offset %d", tz.getDisplayName(), tz.getRawOffset() / (3600 * 1000), tz.getRawOffset() / 1000);
        return str;
    }

}
