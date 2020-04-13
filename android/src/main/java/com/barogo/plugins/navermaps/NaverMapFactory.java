package com.barogo.plugins.navermaps;

import android.app.Application;
import android.content.Context;
import androidx.lifecycle.Lifecycle;

import com.naver.maps.map.CameraPosition;

import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.PluginRegistry;
import io.flutter.plugin.common.StandardMessageCodec;
import io.flutter.plugin.platform.PlatformView;
import io.flutter.plugin.platform.PlatformViewFactory;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class NaverMapFactory extends PlatformViewFactory {

    private final AtomicInteger mActivityState;
    private final BinaryMessenger binaryMessenger;
    private final Application application;
    private final int activityHashCode;
    private final Lifecycle lifecycle;
    private final PluginRegistry.Registrar registrar; // V1 embedding only.

    NaverMapFactory(
            AtomicInteger state,
            BinaryMessenger binaryMessenger,
            Application application,
            Lifecycle lifecycle,
            PluginRegistry.Registrar registrar,
            int activityHashCode) {
        super(StandardMessageCodec.INSTANCE);
        mActivityState = state;
        this.binaryMessenger = binaryMessenger;
        this.application = application;
        this.activityHashCode = activityHashCode;
        this.lifecycle = lifecycle;
        this.registrar = registrar;
    }

    @SuppressWarnings("unchecked")
    @Override
    public PlatformView create(Context context, int id, Object args) {
        Map<String, Object> params = (Map<String, Object>) args;
        final NaverMapBuilder builder = new NaverMapBuilder();

        Convert.interpretNaverMapOptions(params.get("options"), builder);
        if (params.containsKey("initialCameraPosition")) {
            CameraPosition position = Convert.toCameraPosition(params.get("initialCameraPosition"));
            builder.setInitialCameraPosition(position);
        }
        return builder.build(
                id,
                context,
                mActivityState,
                binaryMessenger,
                application,
                lifecycle,
                registrar,
                activityHashCode);
    }
}
