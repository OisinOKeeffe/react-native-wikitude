//
//  RNWikitude.h
//  RNWikitude
//
//  Created by Brave Digital Machine 7 on 2017/09/05.
//  Copyright © 2017 Brave Digital. All rights reserved.
//
@import UIKit;
// import RCTBridgeModule

#import <React/RCTBridgeModule.h>

#import <AVFoundation/AVFoundation.h>
#import "ARViewController.h"


@interface RNWikitude : NSObject <RCTBridgeModule>

//@property (nonatomic, strong) NSDictionary *defaultOptions;
//@property (nonatomic, retain) NSMutableDictionary *options;
//@property (nonatomic, strong) RCTPromiseResolveBlock resolve;
@property (nonatomic, strong) RCTPromiseRejectBlock reject;

@end
