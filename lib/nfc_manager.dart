//mnfc_manager.dart
import 'package:flutter/services.dart';

class NFCManager {
  static const platform = MethodChannel('nfc_channel');

  static Future<void> startNFCService(String ndefMessage) async {
    try {
      await platform.invokeMethod('startNFCService', {'ndefMessage': ndefMessage});
    } on PlatformException catch (e) {
      print("Failed to start NFC service: '${e.message}'.");
    }
  }

  static Future<void> stopNFCService() async {
    try {
      await platform.invokeMethod('stopNFCService');
    } on PlatformException catch (e) {
      print("Failed to stop NFC service: '${e.message}'.");
    }
  }
}
