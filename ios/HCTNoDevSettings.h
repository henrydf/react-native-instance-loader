//
//  HCTNoDevSettings.h
//  HCTInstanceloader
//
//  Created by Henry on 2018/3/13.
//

#import <Foundation/Foundation.h>
#import <React/RCTBridgeModule.h>

@interface HCTNoDevSettings : NSObject<RCTBridgeModule>
@property (nonatomic, readonly) BOOL isHotLoadingAvailable;
@property (nonatomic, readonly) BOOL isLiveReloadAvailable;
@property (nonatomic, readonly) BOOL isRemoteDebuggingAvailable;
@property (nonatomic, readonly) BOOL isJSCSamplingProfilerAvailable;

/**
 * Whether the bridge is connected to a remote JS executor.
 */
@property (nonatomic, assign) BOOL isDebuggingRemotely;

/*
 * Whether shaking will show RCTDevMenu. The menu is enabled by default if RCT_DEV=1, but
 * you may wish to disable it so that you can provide your own shake handler.
 */
@property (nonatomic, assign) BOOL isShakeToShowDevMenuEnabled;

/**
 * Whether performance profiling is enabled.
 */
@property (nonatomic, assign, setter=setProfilingEnabled:) BOOL isProfilingEnabled;

/**
 * Whether automatic polling for JS code changes is enabled. Only applicable when
 * running the app from a server.
 */
@property (nonatomic, assign, setter=setLiveReloadEnabled:) BOOL isLiveReloadEnabled;

/**
 * Whether hot loading is enabled.
 */
@property (nonatomic, assign, setter=setHotLoadingEnabled:) BOOL isHotLoadingEnabled;

/**
 * Toggle the element inspector.
 */
- (void)toggleElementInspector;

/**
 * Toggle JSC's sampling profiler.
 */
- (void)toggleJSCSamplingProfiler;

/**
 * Enables starting of profiling sampler on launch
 */
@property (nonatomic, assign) BOOL startSamplingProfilerOnLaunch;

/**
 * Whether the element inspector is visible.
 */
@property (nonatomic, readonly) BOOL isElementInspectorShown;

/**
 * Whether the performance monitor is visible.
 */
@property (nonatomic, assign) BOOL isPerfMonitorShown;
@end
