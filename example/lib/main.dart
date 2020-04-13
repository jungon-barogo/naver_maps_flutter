import 'dart:async';

import 'package:flutter/material.dart';
import 'package:naver_maps_flutter/naver_maps_flutter.dart';

void main() => runApp(MyApp());

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  @override
  Widget build(BuildContext context) {
    Completer<NaverMapController> _controller = Completer<NaverMapController>();

    final CameraPosition _kBarogoPlex =
        CameraPosition(target: LatLng(37.516885, 127.038238), zoom: 16.0);

    return MaterialApp(
      home: Scaffold(
          appBar: AppBar(
            title: const Text('Barogo Naver Maps Plugin example'),
          ),
          body: NaverMap(
            initialCameraPosition: _kBarogoPlex,
            onMapCreated: (NaverMapController controller) {
              _controller.complete(controller);
            },
          )),
    );
  }
}
