part of naver_maps_flutter;

typedef void MapCreatedCallback(NaverMapController controller);

typedef void CameraPositionCallback(CameraPosition position);

class NaverMap extends StatefulWidget {
  const NaverMap({
    Key key,
    @required this.initialCameraPosition,
    this.onMapCreated,
  })  : assert(initialCameraPosition != null),
        super(key: key);

  final MapCreatedCallback onMapCreated;

  final CameraPosition initialCameraPosition;

  @override
  State<NaverMap> createState() => _NaverMapState();
}

class _NaverMapState extends State<NaverMap> {
  final Completer<NaverMapController> _controller =
      Completer<NaverMapController>();

  _NaverMapOptions _naverMapOptions;

  @override
  Widget build(BuildContext context) {
    final Map<String, dynamic> creationParams = <String, dynamic>{
      'initialCameraPosition': widget.initialCameraPosition?.toMap(),
      'options': _naverMapOptions.toMap(),
    };
    if (defaultTargetPlatform == TargetPlatform.android) {
      return AndroidView(
        viewType: 'plugins.barogo.com/naver_maps',
        onPlatformViewCreated: onPlatformViewCreated,
        creationParams: creationParams,
        creationParamsCodec: const StandardMessageCodec(),
      );
    } else if (defaultTargetPlatform == TargetPlatform.iOS) {
      return UiKitView(
        viewType: 'plugins.barogo.com/naver_maps',
        onPlatformViewCreated: onPlatformViewCreated,
        creationParams: creationParams,
        creationParamsCodec: const StandardMessageCodec(),
      );
    }

    return Text(
        '$defaultTargetPlatform is not yet supported by the maps plugin');
  }

  @override
  void initState() {
    super.initState();
    _naverMapOptions = _NaverMapOptions.fromWidget(widget);
  }

  @override
  void didUpdateWidget(NaverMap oldWidget) {
    super.didUpdateWidget(oldWidget);
    _updateOptions();
  }

  void _updateOptions() async {
    final _NaverMapOptions newOptions = _NaverMapOptions.fromWidget(widget);
    final Map<String, dynamic> updates =
        _naverMapOptions.updatesMap(newOptions);
    if (updates.isEmpty) {
      return;
    }
    final NaverMapController controller = await _controller.future;
    // ignore: unawaited_futures
    controller._updateMapOptions(updates);
    _naverMapOptions = newOptions;
  }

  Future<void> onPlatformViewCreated(int id) async {
    final NaverMapController controller = await NaverMapController.init(
      id,
      widget.initialCameraPosition,
      this,
    );

    _controller.complete(controller);
    if (widget.onMapCreated != null) {
      widget.onMapCreated(controller);
    }
  }
}

class _NaverMapOptions {
  _NaverMapOptions();

  static _NaverMapOptions fromWidget(NaverMap map) {
    return _NaverMapOptions();
  }

  Map<String, dynamic> toMap() {
    final Map<String, dynamic> optionsMap = <String, dynamic>{};

    void addIfNonNull(String fieldName, dynamic value) {
      if (value != null) {
        optionsMap[fieldName] = value;
      }
    }

    return optionsMap;
  }

  Map<String, dynamic> updatesMap(_NaverMapOptions newOptions) {
    final Map<String, dynamic> prevOptionsMap = toMap();

    return newOptions.toMap()
      ..removeWhere(
          (String key, dynamic value) => prevOptionsMap[key] == value);
  }
}
