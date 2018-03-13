//
//  HCTNoDevSettings.m
//  HCTInstanceloader
//
//  Created by Henry on 2018/3/13.
//

#import "HCTNoDevSettings.h"
#import <React/RCTDevSettings.h>
#import <objc/runtime.h>

//BOOL defaultMethod(id self, SEL _cmd) {
//    return NO;
//}

@implementation HCTNoDevSettings

+ (NSString *)moduleName { return @"DevSettings"; }
+ (BOOL)requiresMainQueueSetup { return YES; }

//- (instancetype)init { return nil; }

- (instancetype)initWithDataSource:(id<RCTDevSettingsDataSource>)dataSource { return [super init]; }

//+ (BOOL)resolveInstanceMethod:(SEL)sel {
//    if (![super resolveInstanceMethod:sel]) {
//        class_addMethod([self class], sel, (IMP)defaultMethod, "c@:");
//    }
//    return YES;
//}

- (id)valueForUndefinedKey:(NSString *)key {
    return nil;
}

- (void)doesNotRecognizeSelector:(SEL)aSelector {
    NSLog(@"Call HCTNoDevSettings with selector:%@", NSStringFromSelector(aSelector));
}

- (BOOL)isHotLoadingAvailable { return NO; }
- (BOOL)isLiveReloadAvailable { return NO; }
- (BOOL)isRemoteDebuggingAvailable { return NO; }
- (id)settingForKey:(NSString *)key { return nil; }
- (void)reload {}
- (void)toggleElementInspector {}
- (void)toggleJSCSamplingProfiler {}

@end
