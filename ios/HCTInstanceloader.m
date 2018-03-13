
#import "HCTInstanceloader.h"
#import "HCTRNViewController.h"
#import <SSZipArchive/SSZipArchive.h>
#import <React/RCTBridge.h>
#import <React/RCTRootView.h>

static id __previousBridge = nil;

void presentViewController(NSURL* url, NSString* moduleName, NSDictionary* initialProps) {
    if ([RCTBridge respondsToSelector:@selector(currentBridge)]) {
        __previousBridge = [RCTBridge performSelector:@selector(currentBridge)];
    }
    UIViewController* rootVC = [UIApplication sharedApplication].keyWindow.rootViewController;
    HCTRNViewController* rnVC = [[HCTRNViewController alloc] initWithBundleURL:url
                                                                    moduleName:moduleName
                                                                  initialProps:initialProps];
    [rootVC presentViewController:rnVC animated:YES completion:^{
        
    }];
}

@implementation HCTInstanceloader

@synthesize bridge = _bridge;

RCT_EXPORT_MODULE();

RCT_EXPORT_METHOD(returnMainProcess:(id)data)
{
    dispatch_async(dispatch_get_main_queue(), ^{
        UIViewController* rootViewController = [UIApplication sharedApplication].keyWindow.rootViewController;
        UIView* rootView = rootViewController.view;
        RCTBridge* bridge = nil;
        if ([rootView isKindOfClass:[RCTRootView class]]) {
            bridge = ((RCTRootView*)rootView).bridge;
        } else {
            id appDelegate = [UIApplication sharedApplication].delegate;
            if ([appDelegate respondsToSelector:@selector(bridgeOfMainRNApp)]) {
                bridge = [appDelegate bridgeOfMainRNApp];
            }
        }
        NSAssert(bridge, @"Can not get RCTBridge from main RN, give up...");
        [rootViewController dismissViewControllerAnimated:YES completion:^{
            [bridge enqueueJSCall:@"RCTDeviceEventEmitter.emit" args:@[@"RNInstanceFinished", data]];
            if ([RCTBridge respondsToSelector:@selector(setCurrentBridge:)]) {
                [RCTBridge performSelector:@selector(setCurrentBridge:) withObject:__previousBridge];
            }
//            [bridge invalidate];
//            [bridge reload];
        }];
    });
}

RCT_EXPORT_METHOD(startNewInstance:(id)rnInfo)
{
    if (self.isLoadingBundle) {
        return;
    }
    self.isLoadingBundle = YES;

    
    NSURL* url = [NSURL URLWithString:rnInfo[@"iosUrl"]];
    NSString* moduleName = rnInfo[@"moduleName"];
    NSString* namespace = rnInfo[@"namespace"] ?: moduleName;
    NSArray* paths = NSSearchPathForDirectoriesInDomains(NSCachesDirectory, NSUserDomainMask, YES);
    NSString* cacheDir = rnInfo[@"cacheDir"] ?: paths.firstObject;
    NSString* zipPath = [NSString stringWithFormat:@"%@/tmp.zip", cacheDir];
    NSString* destDir = [NSString stringWithFormat:@"%@/%@", cacheDir, namespace];
    [[NSFileManager defaultManager] createDirectoryAtPath:destDir
                              withIntermediateDirectories:YES
                                               attributes:nil
                                                    error:nil];
    NSString* bundleFile = [NSString stringWithFormat:@"file://%@/main.jsbundle", destDir];
    
    NSDictionary* initProps = rnInfo[@"initProps"];
    NSError* error;

    NSURLSession* session = [NSURLSession sessionWithConfiguration:[NSURLSessionConfiguration defaultSessionConfiguration]];
    [[session downloadTaskWithURL:url
                completionHandler:^(NSURL * _Nullable location, NSURLResponse * _Nullable response, NSError * _Nullable error) {
                    [[NSFileManager defaultManager] moveItemAtPath:location.path toPath:zipPath error:&error];
                    [SSZipArchive unzipFileAtPath:zipPath
                                    toDestination:destDir
                                        overwrite:YES
                                         password:nil
                                  progressHandler:^(NSString * _Nonnull entry, unz_file_info zipInfo, long entryNumber, long total) {
                                      
                                  }
                                completionHandler:^(NSString * _Nonnull path, BOOL succeeded, NSError * _Nullable error) {
                                    self.isLoadingBundle = NO;
                                    if (succeeded) {
                                        dispatch_async(dispatch_get_main_queue(), ^{
                                            presentViewController([NSURL URLWithString:bundleFile], moduleName, initProps);
                                        });
                                    }
                                    [[NSFileManager defaultManager] removeItemAtPath:zipPath error:&error];
                                }];
                }] resume];
}



@end
  
