
# react-native-install-referrer

Module for getting install referrer on android, using Google Play Install Referrer API.

## Getting started

`$ npm install react-native-install-referrer --save`

or

`$ yarn add react-native-install-referrer`

### Mostly automatic installation

`$ react-native link react-native-install-referrer`

### Manual installation

#### Android

1. Open up `android/app/src/main/java/[...]/MainAplication.java`
  - Add `import com.reactlibrary.RNInstallReferrerPackage;` to the imports at the top of the file
  - Add `new RNInstallReferrerPackage()` to the list returned by the `getPackages()` method
2. Append the following lines to `android/settings.gradle`:
  	```
  	include ':react-native-install-referrer'
  	project(':react-native-install-referrer').projectDir = new File(rootProject.projectDir, 	'../node_modules/react-native-install-referrer/android')
  	```
3. Insert the following lines inside the dependencies block in `android/app/build.gradle`:
  	```
      implementation project(':react-native-install-referrer')
  	```

## Usage
```javascript
import RNInstallReferrer from 'react-native-install-referrer';
...
const referrer = await RNInstallReferrer.getReferrer();
console.log(referrer);
//  or
RNInstallReferrer.getReferrer().then(referrer=>console.log(referrer));

//  response example:
//  {
//  	"installTimestamp": "1557907970",
//  	"installReferrer": "utm_source=google-play&utm_medium=organic",
//  	"clickTimestamp": "1557907970",
//  }
//
//  or
//
//  {
//  	"message": "some error message",
//  }
```

## Caution
The install referrer information will be available for 90 days and won't change unless the application is reinstalled. To avoid unecessary API calls in your app, you should invoke the API only once during the first execution after install. 