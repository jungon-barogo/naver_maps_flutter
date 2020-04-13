#import "NaverMapsPlugin.h"

@implementation NaverMapsPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  FlutterMethodChannel* channel = [FlutterMethodChannel
      methodChannelWithName:@"naver_maps_flutter"
            binaryMessenger:[registrar messenger]];
  NaverMapsPlugin* instance = [[NaverMapsPlugin alloc] init];
  [registrar addMethodCallDelegate:instance channel:channel];

  NaverMapFactory* viewFactory = [[NaverMapFactory alloc] init];
  [registrar registerViewFactory:viewFactory withId:@"plugins.barogo.com/naver_maps"];
}
@end

@implementation NaverMapFactory {
  NSObject<FlutterPluginRegistrar>* _registrar;
}

- (instancetype)initWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  self = [super init];
  if (self) {
    _registrar = registrar;
  }
  return self;
}

- (NSObject<FlutterPlatformView>*)createWithFrame:(CGRect)frame
                                   viewIdentifier:(int64_t)viewId
                                    arguments:(id _Nullable)args {
  return [[NaverMapView alloc] initWithFrame:frame
                                viewIdentifier:viewId
                                arguments:args];
}
@end

@implementation NaverMapView {
  CGRect _frame;
  int64_t _viewId;
}

- (instancetype)initWithFrame:(CGRect)frame
               viewIdentifier:(int64_t)viewId
                arguments:(id _Nullable)args {
  _frame = frame;
  _viewId = viewId;
  return self;
}

- (UIView*)view {
  UIView* testView = [[UIView alloc] initWithFrame:_frame];
  testView.backgroundColor = [UIColor greenColor];
  return testView;
}
@end