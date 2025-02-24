#!/bin/sh
set -e

BRIDGE_VERSION=1.0.0
AAR_VERSION=1.0.0
BUILD_TYPE=release

echo ""
echo "==================== DETAILS ===================="
echo "Bridge version: $BRIDGE_VERSION"
echo "AAR version: $AAR_VERSION"
echo "Build type: $BUILD_TYPE"
echo ""

echo Copy controller module
cp -a "../android-ble/controller/build/outputs/aar/controller-${BUILD_TYPE}.aar" "./android/libs/controller-${BUILD_TYPE}-$BRIDGE_VERSION.aar"

echo Copy common module
cp -a "../android-ble/common/build/outputs/aar/common-${BUILD_TYPE}.aar" "./android/libs/common-${BUILD_TYPE}-$BRIDGE_VERSION.aar"

echo Copy blescanner module
cp -a "../android-ble/blescanner/build/outputs/aar/blescanner-${BUILD_TYPE}.aar" "./android/libs/blescanner-${BUILD_TYPE}-$BRIDGE_VERSION.aar"