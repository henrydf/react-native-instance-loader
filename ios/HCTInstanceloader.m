
#import "HCTInstanceloader.h"
#import "HCTRNViewController.h"
#import <SSZipArchive/SSZipArchive.h>

void presentViewController(NSURL* url, NSString* moduleName, NSDictionary* initialProps) {
    UIViewController* rootVC = [UIApplication sharedApplication].keyWindow.rootViewController;
    HCTRNViewController* rnVC = [[HCTRNViewController alloc] initWithBundleURL:url
                                                                    moduleName:moduleName
                                                                  initialProps:initialProps];
    [rootVC presentViewController:rnVC animated:YES completion:^{
        
    }];
}


@implementation HCTInstanceloader

//- (dispatch_queue_t)methodQueue
//{
//    return dispatch_get_main_queue();
//}
RCT_EXPORT_MODULE();

RCT_EXPORT_METHOD(print)
{
    NSLog(@"Method is OK!!!!");
}

RCT_EXPORT_METHOD(startNewInstance:(id)rnInfo)
{
    NSLog(@"Wohahahahahhahaha@@!@#!@#!@#!@#!@");
    if (self.isLoadingBundle) {
        return;
    }
    self.isLoadingBundle = YES;

    
    NSURL* url = [NSURL URLWithString:rnInfo[@"iosUrl"]];
    NSString* moduleName = rnInfo[@"moduleName"];
    NSString* namespace = rnInfo[@"namespace"] ?: moduleName;
    NSArray* paths = NSSearchPathForDirectoriesInDomains(NSCachesDirectory, NSUserDomainMask, YES);
    NSString* cacheDir = rnInfo[@"cacheDir"] ?: paths.firstObject;
    NSString* destDir = [NSString stringWithFormat:@"%@/%@", cacheDir, namespace];
    NSString* bundleFile = [NSString stringWithFormat:@"%@/main.jsbundle", destDir];
    
    NSDictionary* initProps = rnInfo[@"initProps"];

    NSURLSession* session = [NSURLSession sessionWithConfiguration:[NSURLSessionConfiguration defaultSessionConfiguration]];
    [[session downloadTaskWithURL:url
                completionHandler:^(NSURL * _Nullable location, NSURLResponse * _Nullable response, NSError * _Nullable error) {
                    [SSZipArchive unzipFileAtPath:location.absoluteString
                                    toDestination:destDir
                                        overwrite:YES
                                         password:nil
                                  progressHandler:^(NSString * _Nonnull entry, unz_file_info zipInfo, long entryNumber, long total) {
                                      
                                  }
                                completionHandler:^(NSString * _Nonnull path, BOOL succeeded, NSError * _Nullable error) {
                                    self.isLoadingBundle = NO;
                                    dispatch_async(dispatch_get_main_queue(), ^{
                                        presentViewController([NSURL URLWithString:bundleFile], moduleName, initProps);
                                    });
                                }];
                }] resume];
}



@end
  
