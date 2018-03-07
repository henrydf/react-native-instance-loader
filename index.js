
// import { NativeModules } from 'react-native';

// const { HCTInstanceloader } = NativeModules;

// export default HCTInstanceloader;


import React from 'react';
import { DeviceEventEmitter } from 'react-native';

class NavigationHelperComp extends React.Component {
  componentWillMount() {
    DeviceEventEmitter.addListener('RNInstanceFinished', this.onNavigate);
  }

  componentWillUnmount() {
    DeviceEventEmitter.removeListener('RNInstanceFinished', this.onNavigate);
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

  render() {
    return null;
  }
}

export default NavigationHelperComp;
