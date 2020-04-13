package com.barogo.plugins.navermaps;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.Lifecycle;

import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.NaverMapOptions;

import java.util.concurrent.atomic.AtomicInteger;

import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.PluginRegistry;

class NaverMapBuilder implements NaverMapOptionsSink {
    private final NaverMapOptions options = new NaverMapOptions();

    NaverMapController build(
            int id,
            Context context,
            AtomicInteger state,
            BinaryMessenger binaryMessenger,
            Application application,
            Lifecycle lifecycle,
            PluginRegistry.Registrar registrar,
            int activityHashCode) {
        final NaverMapController controller =
                new NaverMapController(
                        id,
                        context,
                        state,
                        binaryMessenger,
                        application,
                        lifecycle,
                        registrar,
                        activityHashCode,
                        options);
        controller.init();
        return controller;
    }

    void setInitialCameraPosition(CameraPosition position) {
        options.camera(position);
    }

    @Override
    public void setMapType(int mapType) {
    }
}
