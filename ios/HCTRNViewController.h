//
//  HCTRNViewController.h
//  HCTInstanceloader
//
//  Created by Henry on 2018/3/9.
//  Copyright © 2018年 Facebook. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <React/RCTBridgeDelegate.h>

@interface HCTRNViewController : UIViewController<RCTBridgeDelegate>

- (instancetype)initWithBundleURL:(NSURL*)url moduleName:(NSString*)moduleName initialProps:(NSDictionary*)props;

@end
