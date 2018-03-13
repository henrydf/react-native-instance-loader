//
//  HCTRNViewController.m
//  HCTInstanceloader
//
//  Created by Henry on 2018/3/9.
//  Copyright © 2018年 Facebook. All rights reserved.
//

#import "HCTRNViewController.h"
#import "HCTNoDevSettings.h"
#import <React/RCTRootView.h>

@interface HCTRNViewController () {
    NSURL* _url;
    NSString* _moduleName;
    NSDictionary* _initialProps;
}

@end

@implementation HCTRNViewController

- (instancetype)initWithBundleURL:(NSURL*)url moduleName:(NSString*)moduleName initialProps:(NSDictionary*)props {
    if (self = [super init]) {
        _url = url;
        _moduleName = moduleName;
        _initialProps = props;
    }
    return self;
}

//- (UIView *)view{
//    return [[RCTRootView alloc] initWithBridge:<#(RCTBridge *)#> moduleName:<#(NSString *)#> initialProperties:<#(NSDictionary *)#>]
//}

- (void)loadView{
    RCTBridge* bridge = [[RCTBridge alloc] initWithDelegate:self launchOptions:nil];
    self.view = [[RCTRootView alloc] initWithBridge:bridge moduleName:_moduleName initialProperties:_initialProps];
}

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (NSURL *)sourceURLForBridge:(RCTBridge *)bridge {
    return _url;
}

- (BOOL)shouldBridgeInitializeNativeModulesSynchronously:(RCTBridge *)bridge {
    return YES;
}

- (BOOL)shouldBridgeLoadJavaScriptSynchronously:(RCTBridge *)bridge {
    return YES;
}

- (NSArray<id<RCTBridgeModule>> *)extraModulesForBridge:(RCTBridge *)bridge {
    return @[[HCTNoDevSettings new]];
}

@end
