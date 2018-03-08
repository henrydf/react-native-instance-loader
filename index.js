
// import { NativeModules } from 'react-native';

// const { HCTInstanceloader } = NativeModules;

// export default HCTInstanceloader;


import React from 'react';
import { DeviceEventEmitter, Alert } from 'react-native';

class NavigationHelperComp extends React.Component {
  componentWillMount() {
    DeviceEventEmitter.addListener('RNInstanceFinished', this.onNavigate);
    DeviceEventEmitter.addListener('RNInstanceFailed', this.onFailed);
  }

  componentWillUnmount() {
    DeviceEventEmitter.removeListener('RNInstanceFinished', this.onNavigate);
    DeviceEventEmitter.removeListener('RNInstanceFailed', this.onFailed);
  }

  onNavigate = (navParams) => {
    const {
      navigation: {
        routeName,
        params,
        action,
      },
    } = navParams;
    this.props.navigation.navigate(routeName, params, action);
  }

  onFailed = (msg) => {
    Alert.alert('异常退出', `${msg.ModuleName}发生了异常...`);
  }

  render() {
    return null;
  }
}

export default NavigationHelperComp;
