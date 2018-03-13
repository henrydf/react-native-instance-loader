
# react-native-instanceloader

## Getting started

`$ npm install react-native-instanceloader --save`
or
`$ yarn add react-native-instanceloader`

### Mostly automatic installation

`$ react-native link react-native-instanceloader`

#### iOS - Cocoapods
1. Add `# Add new pods below this line` to your Podfile at right place, see more at <http://facebook.github.io/react-native/docs/linking-libraries-ios.html#step-2>.
2. install this library as a pod.
```shell
$ cd ios # which directory include your Podfile
$ pod install
```
3. Not sure below is required!
4. In XCode, in the project navigator, right click `Libraries` ➜ `Add Files to [your project's name]`
5. Go to `node_modules` ➜ `react-native-instanceloader` and add `HCTInstanceloader.xcodeproj`
#### iOS - w/o Cocoapods
not tested...

#### Android
1. Add a new activity description in `AndroidManifest.xml`
```xml
<activity
	android:name="com.hongchuangtech.enhancer.HCTReactActivity"
	android:label="@string/app_name"
	android:theme="@style/Theme.AppCompat.Light.NoActionBar" />
```
2. Make sure your RN code is in ReactActivity.
3. Extend your MainApplication from `com.hongchuangtech.enhancer.HCTReactApplication`

### Manual installation


#### iOS

1. In XCode, in the project navigator, right click `Libraries` ➜ `Add Files to [your project's name]`
2. Go to `node_modules` ➜ `react-native-instanceloader` and add `HCTInstanceloader.xcodeproj`
3. In XCode, in the project navigator, select your project. Add `libHCTInstanceloader.a` to your project's `Build Phases` ➜ `Link Binary With Libraries`
4. Run your project (`Cmd+R`)<

#### Android

1. Open up `android/app/src/main/java/[...]/MainActivity.java`
  - Add `import com.hongchuangtech.HCTInstanceloaderPackage;` to the imports at the top of the file
  - Add `new HCTInstanceloaderPackage()` to the list returned by the `getPackages()` method
2. Append the following lines to `android/settings.gradle`:
  	```
  	include ':react-native-instanceloader'
  	project(':react-native-instanceloader').projectDir = new File(rootProject.projectDir, 	'../node_modules/react-native-instanceloader/android')
  	```
3. Insert the following lines inside the dependencies block in `android/app/build.gradle`:
  	```
      compile project(':react-native-instanceloader')
  	```


## Usage

### Load Sub RN App
1. Main RN App
```javascript
import { NativeModules } from 'react-native';
NativeModules.HCTInstanceloader.startNewInstance({
	androidUrl: 'http://10.0.2.2:8080/water-site/water-site.android.zip', // bundle files w/o any unnecessary directories
	iosUrl: 'http://localhost:8080/water-site/water-site.ios.zip', // bundle files w/o any unnecessary directories
	moduleName: 'WaterSite', // root component name which registed in AppRegistry.registerComponent
	namespace: 'WaterSite', // store local bundle files under this, will use moduleName when not set this value
});
```
2. Sub RN App
```javascript
import { NativeModules } from 'react-native';
NativeModules.HCTInstanceloader.returnMainProcess({});
```
3. Example Zip Hierachy (iOS)
```
HenryMBP:questionnaire henry$ unzip -l questionnaire.ios.zip
Archive:  questionnaire.ios.zip
  Length      Date    Time    Name
---------  ---------- -----   ----
        0  03-13-2018 15:43   assets/
        0  03-13-2018 15:43   assets/js/
        0  03-13-2018 15:43   assets/js/commonView/
        0  03-13-2018 15:43   assets/js/commonView/echarts/
   540418  03-13-2018 15:43   assets/js/commonView/echarts/tpl.html
        0  03-13-2018 15:43   assets/js/questionnaire/
				...
  1465213  03-13-2018 15:43   main.jsbundle
       21  03-13-2018 15:43   main.jsbundle.meta
```
4. Example Zip Hierachy (Android)
```
HenryMBP:questionnaire henry$ unzip -l questionnaire.android.zip
Archive:  questionnaire.android.zip
  Length      Date    Time    Name
---------  ---------- -----   ----
        0  03-13-2018 15:44   drawable-hdpi/
      134  03-13-2018 15:44   drawable-hdpi/node_modules_reactnavigation_src_views_assets_backicon.png
				...
  1457649  03-13-2018 15:44   main.jsbundle
       21  03-13-2018 15:44   main.jsbundle.meta
---------                     -------
  2055977                     43 files
```
### Pass arguments
1. Pass arguments to Sub RN App
```javascript
import { NativeModules } from 'react-native';
NativeModules.HCTInstanceloader.startNewInstance({
	androidUrl: 'http://10.0.2.2:8080/water-site/water-site.android.zip', // bundle files w/o any unnecessary directories
	iosUrl: 'http://localhost:8080/water-site/water-site.ios.zip', // bundle files w/o any unnecessary directories
	moduleName: 'WaterSite', // root component name which registed in AppRegistry.registerComponent
	namespace: 'WaterSite', // store local bundle files under this, will use moduleName when not set this value
	initProps: {
			token: '11234123',
			screenProps: { // react-navigation will only take this arguments to its children.
					whatever: 'abcd',
			},
	},
});
```
2. Receive arguments from Main RN App (Stateful Comp)
```javascript
import React from 'react';
import { AppRegistry } from 'react-native';
import { StackNavigator } from 'react-navigation';
const router = StackNavigator({
	// ...
});
class ExampleComp extends React.Component {
	constructor(props) {
		super(props);
		console.log(props);
	}
	render() {
		return router;
	}
}
AppRegistry.registerComponent('WaterSite', () => (props) => <Example {...props} /> );
```
3. Receive arguments from Main RN App (Stateless Comp)
```javascript
import React from 'react';
import { AppRegistry } from 'react-native';
import { StackNavigator } from 'react-navigation';
const router = StackNavigator({
	// ...
});
AppRegistry.registerComponent('WaterSite', () => (props) => {
	console.log(props);
	return router;
});
```
4. Return data to Main RN App
```javascript
import { NativeModules } from 'react-native';
NativeModules.returnMainProcess({
	// anything you want to sendback but function
});
```
5. Recive data from Sub RN App
```javascript
import { NativeModules } from 'react-native';
NativeModules.DeviceEventEmitter.addListener('RNInstanceFinished', data => console.log('receive data from sub rn app!!!', data));
```
6. Catch Exception from Sub RN App (Android)
```javascript
DeviceEventEmitter.addListener('RNInstanceFailed', (msg) => {
	Alert.alert('异常退出', `${msg['PARAMS_MODULE_NAME']}发生了异常...`);
});
```
