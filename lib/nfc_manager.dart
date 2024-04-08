import 'dart:async';

import 'package:flutter/services.dart';

class MyNfcPlugin {
  static const MethodChannel _channel = const MethodChannel('nfc_channel');

  static Future<void> startNFCService(String ndefMessage) async {
    try {
      await _channel.invokeMethod('startNFCService', {'ndefMessage': ndefMessage});
    } on PlatformException catch (e) {
      print("Failed to start NFC service: '${e.message}'.");
    }
  }

  static Future<void> stopNFCService() async {
    try {
      await _channel.invokeMethod('stopNFCService');
    } on PlatformException catch (e) {
      print("Failed to stop NFC service: '${e.message}'.");
    }
  }
}
