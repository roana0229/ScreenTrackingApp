package app.roana0229.org.screentrackingapp.tracking;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;

import app.roana0229.org.screentrackingapp.utility.SimpleLogger;


public class TrackingViewPager extends ViewPager {

    private ScreenTrackingCallBack callBack;
    private long fragmentStartedTime;
    private int prevPosition;

    public TrackingViewPager(Context context) {
        this(context, null);
    }

    public TrackingViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        prevPosition = getCurrentItem();
        getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                getViewTreeObserver().removeOnPreDrawListener(this);
                startTracking();
                return true;
            }
        });
    }

    public void setTrackingCallBack(ScreenTrackingCallBack callBack) {
        this.callBack = callBack;
    }

    private void startTracking() {
        addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                Fragment fragment = getFragment(prevPosition);
                if (!(fragment instanceof ViewPagerTrackingMarker)) {
                    return;
                }

                long exposureTime = System.currentTimeMillis() - fragmentStartedTime;
                if (callBack != null) {
                    TrackingMarker trackingMarker = (TrackingMarker) fragment;
                    callBack.track(trackingMarker.getScreenName(), trackingMarker.getScreenParameter(), exposureTime);
                }
                fragmentStartedTime = System.currentTimeMillis();
                prevPosition = position;
            }
        });
    }

    private Fragment getFragment(int index) {
        FragmentManager supportFragmentManager = ((AppCompatActivity) getContext()).getSupportFragmentManager();
        return supportFragmentManager.findFragmentByTag("android:switcher:" + getId() + ":" + index);
    }

}