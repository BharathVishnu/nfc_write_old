//main.dart
import 'package:flutter/material.dart';
import 'nfc_manager.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'NFC Service Demo',
      theme: ThemeData(
        primarySwatch: Colors.blue,
      ),
      home: NFCServicePage(),
    );
  }
}

class NFCServicePage extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('NFC Service Demo'),
      ),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            ElevatedButton(
              onPressed: () {
                startNFCService(context);
              },
              child: Text('Start NFC Service'),
            ),
            SizedBox(height: 16),
            ElevatedButton(
              onPressed: () {
                stopNFCService(context);
              },
              child: Text('Stop NFC Service'),
            ),
          ],
        ),
      ),
    );
  }

  void startNFCService(BuildContext context) {
    try {
      NFCManager.startNFCService("Your NDEF Message");
      ScaffoldMessenger.of(context).showSnackBar(SnackBar(
        content: Text('NFC service started'),
      ));
    } catch (e) {
      print('Error starting NFC service: $e');
      ScaffoldMessenger.of(context).showSnackBar(SnackBar(
        content: Text('Failed to start NFC service'),
      ));
    }
  }

  void stopNFCService(BuildContext context) {
    try {
      NFCManager.stopNFCService();
      ScaffoldMessenger.of(context).showSnackBar(SnackBar(
        content: Text('NFC service stopped'),
      ));
    } catch (e) {
      print('Error stopping NFC service: $e');
      ScaffoldMessenger.of(context).showSnackBar(SnackBar(
        content: Text('Failed to stop NFC service'),
      ));
    }
  }
}
