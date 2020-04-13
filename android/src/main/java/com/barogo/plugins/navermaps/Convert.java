package com.barogo.plugins.navermaps;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraPosition;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

class Convert {
    static CameraPosition toCameraPosition(Object o) {
        final Map<?, ?> data = toMap(o);
        LatLng latLng = toLatLng(data.get("target"));
        float zoom = toFloat(data.get("zoom"));
        float tilt = toFloat(data.get("tilt"));
        float bearing = toFloat(data.get("bearing"));
        return new CameraPosition(latLng, zoom);
    }

    private static double toDouble(Object o) {
        return ((Number) o).doubleValue();
    }

    private static float toFloat(Object o) {
        return ((Number) o).floatValue();
    }

    private static Float toFloatWrapper(Object o) {
        return (o == null) ? null : toFloat(o);
    }

    private static int toInt(Object o) {
        return ((Number) o).intValue();
    }

    static Object latLngToJson(LatLng latLng) {
        return Arrays.asList(latLng.latitude, latLng.longitude);
    }

    static LatLng toLatLng(Object o) {
        final List<?> data = toList(o);
        return new LatLng(toDouble(data.get(0)), toDouble(data.get(1)));
    }

    private static List<?> toList(Object o) {
        return (List<?>) o;
    }

    private static Map<?, ?> toMap(Object o) {
        return (Map<?, ?>) o;
    }

    private static float toFractionalPixels(Object o, float density) {
        return toFloat(o) * density;
    }

    private static int toPixels(Object o, float density) {
        return (int) toFractionalPixels(o, density);
    }

    private static String toString(Object o) {
        return (String) o;
    }

    static void interpretNaverMapOptions(Object o, NaverMapOptionsSink sink) {
        final Map<?, ?> data = toMap(o);
        final Object mapType = data.get("mapType");
        if (mapType != null) {
            sink.setMapType(toInt(mapType));
        }
    }
}
