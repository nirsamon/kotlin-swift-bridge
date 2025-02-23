import {NativeModules, NativeEventEmitter, Platform} from 'react-native';

import {
  requestPermissionsAndroid,
  requestPermissionsBluetoothAndroid,
} from '../src/permissions.android';

const LINKING_ERROR =
  `The package 'serino-pos-kotlin-bridge' doesn't seem to be linked. Make sure: \n\n` +
  Platform.select({ios: "- You have run 'pod install'\n", default: ''}) +
  '- You rebuilt the app after installing the package\n' +
  '- You are not using Expo Go\n';

const KotlinSwiftBridge: KotlinSwiftBridgeInterface =
  NativeModules.KotlinSwiftBridge
    ? NativeModules.KotlinSwiftBridge
    : new Proxy(
        {},
        {
          get() {
            throw new Error(LINKING_ERROR);
          },
        },
      );

interface KotlinSwiftBridgeInterface {
  getConstants(): any;
  run(json: string): Promise<string>;
  eventCallback(json: string): Promise<string>;
  registerEvents(): Promise<string>;
  unregisterEvents(): Promise<string>;
}

export const requestPermissions: () => Promise<boolean> = Platform.select({
  ios: async () => true,
  default: requestPermissionsAndroid,
});

export const requestBluetoothPermissions: () => Promise<boolean> =
  Platform.select({
    ios: async () => true,
    default: requestPermissionsBluetoothAndroid,
  });

export const {
  EVENT_ON_BLE_SCAN
} = KotlinSwiftBridge.getConstants();

export function run(json: string): Promise<string> {
  return KotlinSwiftBridge.run(json);
}

export function eventCallback(json: string): Promise<string> {
  return KotlinSwiftBridge.eventCallback(json);
}

export function registerEvents(): Promise<string> {
  return KotlinSwiftBridge.registerEvents();
}

export function unregisterEvents(): Promise<string> {
  return KotlinSwiftBridge.unregisterEvents();
}
