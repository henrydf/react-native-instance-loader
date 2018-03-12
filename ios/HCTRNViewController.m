//
//  HCTRNViewController.m
//  HCTInstanceloader
//
//  Created by Henry on 2018/3/9.
//  Copyright © 2018年 Facebook. All rights reserved.
//

#import "HCTRNViewController.h"
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
    self.view = [[RCTRootView alloc] initWithBundleURL:_url
                                            moduleName:_moduleName
                                     initialProperties:_initialProps
                                         launchOptions:nil];
}

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

@end
