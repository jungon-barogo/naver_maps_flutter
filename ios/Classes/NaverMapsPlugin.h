#import <Flutter/Flutter.h>

@interface NaverMapsPlugin : NSObject <FlutterPlugin>
@end

@interface NaverMapFactory : NSObject <FlutterPlatformViewFactory>
- (instancetype)initWithRegistrar:(NSObject<FlutterPluginRegistrar> *)registrar;
@end

@interface NaverMapView : NSObject <FlutterPlatformView>
- (instancetype)initWithFrame:(CGRect)frame
               viewIdentifier:(int64_t)viewId
                    arguments:(nullable id)args;
@end