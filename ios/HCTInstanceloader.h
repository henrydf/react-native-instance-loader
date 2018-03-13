
#if __has_include("RCTBridgeModule.h")
#import "RCTBridgeModule.h"
#else
#import <React/RCTBridgeModule.h>
#endif

#import <React/RCTBridge.h>

#import <UIKit/UIKit.h>

@protocol HCTMainRNBridgeProvider

- (RCTBridge*)bridgeOfMainRNApp;

@end


@interface HCTInstanceloader : NSObject <RCTBridgeModule>

@property (atomic, assign) BOOL isLoadingBundle;

@end
  
