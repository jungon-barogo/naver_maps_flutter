package com.barogo.plugins.navermaps;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;

import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.NaverMapOptions;
import com.naver.maps.map.OnMapReadyCallback;

import java.util.concurrent.atomic.AtomicInteger;

import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.PluginRegistry;
import io.flutter.plugin.platform.PlatformView;

import static com.barogo.plugins.navermaps.NaverMapsPlugin.CREATED;
import static com.barogo.plugins.navermaps.NaverMapsPlugin.DESTROYED;
import static com.barogo.plugins.navermaps.NaverMapsPlugin.PAUSED;
import static com.barogo.plugins.navermaps.NaverMapsPlugin.RESUMED;
import static com.barogo.plugins.navermaps.NaverMapsPlugin.STARTED;
import static com.barogo.plugins.navermaps.NaverMapsPlugin.STOPPED;

/**
 * Controller of a single GoogleMaps MapView instance.
 */
final class NaverMapController
        implements Application.ActivityLifecycleCallbacks,
        DefaultLifecycleObserver,
        ActivityPluginBinding.OnSaveInstanceStateListener,
        NaverMapOptionsSink,
        MethodChannel.MethodCallHandler,
        OnMapReadyCallback,
        PlatformView {

    private static final String TAG = "GoogleMapController";
    private final int id;
    private final AtomicInteger activityState;
    private final MethodChannel methodChannel;
    private final MapView mapView;
    private NaverMap naverMap;
    private boolean disposed = false;
    private final float density;
    private MethodChannel.Result mapReadyResult;
    private final int
            activityHashCode; // Do not use directly, use getActivityHashCode() instead to get correct hashCode for both v1 and v2 embedding.
    private final Lifecycle lifecycle;
    private final Context context;
    private final Application
            mApplication; // Do not use direclty, use getApplication() instead to get correct application object for both v1 and v2 embedding.
    private final PluginRegistry.Registrar registrar; // For v1 embedding only.

    NaverMapController(
            int id,
            Context context,
            AtomicInteger activityState,
            BinaryMessenger binaryMessenger,
            Application application,
            Lifecycle lifecycle,
            PluginRegistry.Registrar registrar,
            int registrarActivityHashCode,
            NaverMapOptions options) {
        this.id = id;
        this.context = context;
        this.activityState = activityState;
        this.mapView = new MapView(context, options);
        this.density = context.getResources().getDisplayMetrics().density;
        methodChannel = new MethodChannel(binaryMessenger, "plugins.barogo.com/naver_maps_" + id);
        methodChannel.setMethodCallHandler(this);
        mApplication = application;
        this.lifecycle = lifecycle;
        this.registrar = registrar;
        this.activityHashCode = registrarActivityHashCode;
    }

    @Override
    public View getView() {
        return mapView;
    }

    void init() {
        switch (activityState.get()) {
            case STOPPED:
                mapView.onCreate(null);
                mapView.onStart();
                mapView.onResume();
                mapView.onPause();
                mapView.onStop();
                break;
            case PAUSED:
                mapView.onCreate(null);
                mapView.onStart();
                mapView.onResume();
                mapView.onPause();
                break;
            case RESUMED:
                mapView.onCreate(null);
                mapView.onStart();
                mapView.onResume();
                break;
            case STARTED:
                mapView.onCreate(null);
                mapView.onStart();
                break;
            case CREATED:
                mapView.onCreate(null);
                break;
            case DESTROYED:
                // Nothing to do, the activity has been completely destroyed.
                break;
            default:
                throw new IllegalArgumentException(
                        "Cannot interpret " + activityState.get() + " as an activity state");
        }
        if (lifecycle != null) {
            lifecycle.addObserver(this);
        } else {
            getApplication().registerActivityLifecycleCallbacks(this);
        }
        mapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(NaverMap naverMap) {
        this.naverMap = naverMap;
        if (mapReadyResult != null) {
            mapReadyResult.success(null);
            mapReadyResult = null;
        }
    }

    @Override
    public void onMethodCall(MethodCall call, MethodChannel.Result result) {
        switch (call.method) {
            case "map#waitForMap":
                if (naverMap != null) {
                    result.success(null);
                    return;
                }
                mapReadyResult = result;
                break;
            default:
                result.notImplemented();
        }
    }

    @Override
    public void dispose() {
        if (disposed) {
            return;
        }
        disposed = true;
        methodChannel.setMethodCallHandler(null);
        mapView.onDestroy();
        getApplication().unregisterActivityLifecycleCallbacks(this);
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        if (disposed || activity.hashCode() != getActivityHashCode()) {
            return;
        }
        mapView.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityStarted(Activity activity) {
        if (disposed || activity.hashCode() != getActivityHashCode()) {
            return;
        }
        mapView.onStart();
    }

    @Override
    public void onActivityResumed(Activity activity) {
        if (disposed || activity.hashCode() != getActivityHashCode()) {
            return;
        }
        mapView.onResume();
    }

    @Override
    public void onActivityPaused(Activity activity) {
        if (disposed || activity.hashCode() != getActivityHashCode()) {
            return;
        }
        mapView.onPause();
    }

    @Override
    public void onActivityStopped(Activity activity) {
        if (disposed || activity.hashCode() != getActivityHashCode()) {
            return;
        }
        mapView.onStop();
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        if (disposed || activity.hashCode() != getActivityHashCode()) {
            return;
        }
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        if (disposed || activity.hashCode() != getActivityHashCode()) {
            return;
        }
        mapView.onDestroy();
    }

    // DefaultLifecycleObserver and OnSaveInstanceStateListener

    @Override
    public void onCreate(@NonNull LifecycleOwner owner) {
        if (disposed) {
            return;
        }
        mapView.onCreate(null);
    }

    @Override
    public void onStart(@NonNull LifecycleOwner owner) {
        if (disposed) {
            return;
        }
        mapView.onStart();
    }

    @Override
    public void onResume(@NonNull LifecycleOwner owner) {
        if (disposed) {
            return;
        }
        mapView.onResume();
    }

    @Override
    public void onPause(@NonNull LifecycleOwner owner) {
        if (disposed) {
            return;
        }
        mapView.onResume();
    }

    @Override
    public void onStop(@NonNull LifecycleOwner owner) {
        if (disposed) {
            return;
        }
        mapView.onStop();
    }

    @Override
    public void onDestroy(@NonNull LifecycleOwner owner) {
        if (disposed) {
            return;
        }
        mapView.onDestroy();
    }

    @Override
    public void onRestoreInstanceState(Bundle bundle) {
        if (disposed) {
            return;
        }
        mapView.onCreate(bundle);
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        if (disposed) {
            return;
        }
        mapView.onSaveInstanceState(bundle);
    }

    // GoogleMapOptionsSink methods


    @Override
    public void setMapType(int mapType) {
    }

    private int getActivityHashCode() {
        if (registrar != null && registrar.activity() != null) {
            return registrar.activity().hashCode();
        } else {
            return activityHashCode;
        }
    }

    private Application getApplication() {
        if (registrar != null && registrar.activity() != null) {
            return registrar.activity().getApplication();
        } else {
            return mApplication;
        }
    }
}
