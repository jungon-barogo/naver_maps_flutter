part of naver_maps_flutter;

class NaverMapController {
  NaverMapController._(
    this.channel,
    CameraPosition initialCameraPosition,
    this._naverMapState,
  ) : assert(channel != null) {
    channel.setMethodCallHandler(_handleMethodCall);
  }

  static Future<NaverMapController> init(
    int id,
    CameraPosition initialCameraPosition,
    _NaverMapState naverMapState,
  ) async {
    assert(id != null);
    final MethodChannel channel =
        MethodChannel('plugins.barogo.com/naver_maps_$id');

    await channel.invokeMethod<void>('map#waitForMap');

    return NaverMapController._(
      channel,
      initialCameraPosition,
      naverMapState,
    );
  }

  @visibleForTesting
  final MethodChannel channel;

  final _NaverMapState _naverMapState;

  Future<dynamic> _handleMethodCall(MethodCall call) async {
    switch (call.method) {
      case 'camera#onMoveStarted':
        break;
      case 'camera#onMove':
        break;
      case 'camera#onIdle':
        break;
      case 'marker#onTap':
        break;
      case 'marker#onDragEnd':
        break;
      case 'infoWindow#onTap':
        break;
      case 'polyline#onTap':
        break;
      case 'polygon#onTap':
        break;
      case 'circle#onTap':
        break;
      case 'map#onTap':
        break;
      case 'map#onLongPress':
        break;
      default:
        throw MissingPluginException();
    }
  }

  Future<void> _updateMapOptions(Map<String, dynamic> optionsUpdate) async {
    assert(optionsUpdate != null);
    await channel.invokeMethod<void>(
      'map#update',
      <String, dynamic>{
        'options': optionsUpdate,
      },
    );
  }

  Future<void> animateCamera(CameraUpdate cameraUpdate) async {
    await channel.invokeMethod<void>('camera#animate', <String, dynamic>{
      'cameraUpdate': cameraUpdate._toJson(),
    });
  }

  Future<void> moveCamera(CameraUpdate cameraUpdate) async {
    await channel.invokeMethod<void>('camera#move', <String, dynamic>{
      'cameraUpdate': cameraUpdate._toJson(),
    });
  }

  Future<LatLngBounds> getVisibleRegion() async {
    final Map<String, dynamic> latLngBounds =
        await channel.invokeMapMethod<String, dynamic>('map#getVisibleRegion');
    final LatLng southwest = LatLng._fromJson(latLngBounds['southwest']);
    final LatLng northeast = LatLng._fromJson(latLngBounds['northeast']);

    return LatLngBounds(northeast: northeast, southwest: southwest);
  }
}
