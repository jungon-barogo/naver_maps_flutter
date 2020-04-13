part of naver_maps_flutter;

class CameraPosition {
  const CameraPosition({
    this.bearing = 0.0,
    @required this.target,
    this.tilt = 0.0,
    this.zoom = 0.0,
  })  : assert(bearing != null),
        assert(target != null),
        assert(tilt != null),
        assert(zoom != null);

  final double bearing;

  final LatLng target;

  final double tilt;

  final double zoom;

  dynamic toMap() => <String, dynamic>{
        'bearing': bearing,
        'target': target._toJson(),
        'tilt': tilt,
        'zoom': zoom,
      };

  static CameraPosition fromMap(dynamic json) {
    if (json == null) {
      return null;
    }
    return CameraPosition(
      bearing: json['bearing'],
      target: LatLng._fromJson(json['target']),
      tilt: json['tilt'],
      zoom: json['zoom'],
    );
  }

  @override
  bool operator ==(dynamic other) {
    if (identical(this, other)) return true;
    if (runtimeType != other.runtimeType) return false;
    final CameraPosition typedOther = other;
    return bearing == typedOther.bearing &&
        target == typedOther.target &&
        tilt == typedOther.tilt &&
        zoom == typedOther.zoom;
  }

  @override
  int get hashCode => hashValues(bearing, target, tilt, zoom);

  @override
  String toString() =>
      'CameraPosition(bearing: $bearing, target: $target, tilt: $tilt, zoom: $zoom)';
}

class CameraUpdate {
  CameraUpdate._(this._json);

  static CameraUpdate newCameraPosition(CameraPosition cameraPosition) {
    return CameraUpdate._(
      <dynamic>['newCameraPosition', cameraPosition.toMap()],
    );
  }

  static CameraUpdate newLatLng(LatLng latLng) {
    return CameraUpdate._(<dynamic>['newLatLng', latLng._toJson()]);
  }

  static CameraUpdate newLatLngBounds(LatLngBounds bounds, double padding) {
    return CameraUpdate._(<dynamic>[
      'newLatLngBounds',
      bounds._toList(),
      padding,
    ]);
  }

  static CameraUpdate newLatLngZoom(LatLng latLng, double zoom) {
    return CameraUpdate._(
      <dynamic>['newLatLngZoom', latLng._toJson(), zoom],
    );
  }

  static CameraUpdate scrollBy(double dx, double dy) {
    return CameraUpdate._(
      <dynamic>['scrollBy', dx, dy],
    );
  }

  static CameraUpdate zoomBy(double amount, [Offset focus]) {
    if (focus == null) {
      return CameraUpdate._(<dynamic>['zoomBy', amount]);
    } else {
      return CameraUpdate._(<dynamic>[
        'zoomBy',
        amount,
        <double>[focus.dx, focus.dy],
      ]);
    }
  }

  static CameraUpdate zoomIn() {
    return CameraUpdate._(<dynamic>['zoomIn']);
  }

  static CameraUpdate zoomOut() {
    return CameraUpdate._(<dynamic>['zoomOut']);
  }

  static CameraUpdate zoomTo(double zoom) {
    return CameraUpdate._(<dynamic>['zoomTo', zoom]);
  }

  final dynamic _json;

  dynamic _toJson() => _json;
}
