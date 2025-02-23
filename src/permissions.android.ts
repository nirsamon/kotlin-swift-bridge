import { PermissionsAndroid } from 'react-native';

export async function requestPermissionsAndroid() {
  const granted = await PermissionsAndroid.request(
    PermissionsAndroid.PERMISSIONS.READ_PHONE_STATE ??
      'android.permission.READ_PHONE_STATE'
  );

  if (granted !== PermissionsAndroid.RESULTS.GRANTED) {
    return false;
  }

  return true;
}

export async function requestPermissionsBluetoothAndroid() {
  const grantedReadPhoneState = await PermissionsAndroid.requestMultiple([
    PermissionsAndroid.PERMISSIONS.BLUETOOTH_CONNECT ??
    'android.permission.BLUETOOTH_CONNECT',
    PermissionsAndroid.PERMISSIONS.BLUETOOTH_SCAN ??
    'android.permission.BLUETOOTH_SCAN',
    PermissionsAndroid.PERMISSIONS.ACCESS_FINE_LOCATION ??
    'android.permission.ACCESS_FINE_LOCATION'
  ]

  );

  if (grantedReadPhoneState['android.permission.BLUETOOTH_CONNECT'] !== PermissionsAndroid.RESULTS.GRANTED &&
      grantedReadPhoneState['android.permission.BLUETOOTH_SCAN'] !== PermissionsAndroid.RESULTS.GRANTED &&
      grantedReadPhoneState['android.permission.ACCESS_FINE_LOCATION'] !== PermissionsAndroid.RESULTS.GRANTED) {
    return false;
  }

  return true;
}
