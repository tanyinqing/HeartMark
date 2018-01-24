package com.yikang.heartmark.common.base;

import android.app.ActivityManager;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.yikang.heartmark.application.AppContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Created by chuanyhu on 2014/10/8.
 */
public class BaseFragmentActivity extends FragmentActivity {

    private AbsBaseFragment currentFragment;
    protected Logger logger = LoggerFactory.getLogger(BaseFragmentActivity.class);

    public BaseFragmentActivity() {
        ActivityManager activityManager = (ActivityManager) AppContext.getAppContext().getSystemService(Context.ACTIVITY_SERVICE);
        int memSize = activityManager.getMemoryClass();
        logger.debug("BaseFragmentActivity getMemoryClass", memSize);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewServer.get(this).addWindow(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // AppContext.getAppContext().setCurrentActivity(null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ViewServer.get(this).setFocusedWindow(this);
        // AppContext.getAppContext().setCurrentActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ViewServer.get(this).removeWindow(this);
    }

    public void setCurrentFragment(AbsBaseFragment currentFragment) {
        this.currentFragment = currentFragment;
    }

    public AbsBaseFragment getCurrentFragment() {
        return currentFragment;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        logger.debug("[Focus] onWindowFocusChanged: hasFocus=" + hasFocus + " getSimpleName:" + this.getClass().getSimpleName());

        //Dont care if CommonActivity lost focus!
        if (!hasFocus && this.getClass().getSimpleName().equals("CommonActivity")) return;

        // AppContext.getAppContext().setHasFocus(hasFocus);
        //AppContext.getAppContext().setHidden(!hasFocus);
        // logger.debug("[Focus] onWindowFocusChanged: Update  isHidden=" + AppContext.getAppContext().isHidden() + " getSimpleName:" + this.getClass().getSimpleName());
    }

    @Override
    //TODO Change foe API 14 or above
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        //logger.debug("[Performance] onTrimMemory:" + level);
        if (level == ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN) {
            logger.debug("[Focus] onTrimMemory: TRIM_MEMORY_UI_HIDDEN. hidden=true");
            //    AppContext.getAppContext().setHidden(true);
            //  logger.debug("[Focus] onTrimMemory: AppContext.isHidden=" + AppContext.getAppContext().isHidden());
//            if (currentFragment!=null){
//                logger.debug("[Performance] currentFragment: clearData");
//                currentFragment.clearData();
//            }
        }
        if (level == ComponentCallbacks2.TRIM_MEMORY_RUNNING_MODERATE)
            logger.debug("[Performance] onTrimMemory: TRIM_MEMORY_RUNNING_MODERATE");
        if (level == ComponentCallbacks2.TRIM_MEMORY_RUNNING_LOW)
            logger.debug("[Performance] onTrimMemory: TRIM_MEMORY_RUNNING_LOW ");
        if (level == ComponentCallbacks2.TRIM_MEMORY_RUNNING_CRITICAL)
            logger.debug("[Performance] onTrimMemory: TRIM_MEMORY_RUNNING_CRITICAL");
        if (level == ComponentCallbacks2.TRIM_MEMORY_BACKGROUND)
            logger.debug("[Performance] onTrimMemory: TRIM_MEMORY_BACKGROUND ");
        if (level == ComponentCallbacks2.TRIM_MEMORY_MODERATE)
            logger.debug("[Performance] onTrimMemory: TRIM_MEMORY_MODERATE");
        if (level == ComponentCallbacks2.TRIM_MEMORY_COMPLETE)
            logger.debug("[Performance] onTrimMemory: TRIM_MEMORY_COMPLETE");
        if (level == ComponentCallbacks2.TRIM_MEMORY_RUNNING_CRITICAL)
            logger.debug("[Performance] onTrimMemory: TRIM_MEMORY_RUNNING_CRITICAL");

    }

    @Override
    //TODO same as TRIM_MEMORY_COMPLETE
    public void onLowMemory() {
        super.onLowMemory();
        logger.debug("[Performance] onLowMemory");
    }
}
