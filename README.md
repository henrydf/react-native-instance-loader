
# react-native-instanceloader

## Getting started

`$ npm install react-native-instanceloader --save`

### Mostly automatic installation

`$ react-native link react-native-instanceloader`

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
```javascript
import HCTInstanceloader from 'react-native-instanceloader';

// TODO: What to do with the module?
HCTInstanceloader;
```
  