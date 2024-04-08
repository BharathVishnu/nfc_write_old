package com.example.flutter_kotlin_integration

import android.content.Intent
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.NfcAdapter
import android.os.Bundle
import android.widget.Toast
import io.flutter.embedding.android.FlutterActivity
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel

class MainActivity : FlutterActivity() {

    private lateinit var nfcAdapter: NfcAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        nfcAdapter = NfcAdapter.getDefaultAdapter(this)

        if (nfcAdapter == null || !nfcAdapter.isEnabled) {
            // NFC is not available or not enabled on this device
            // Handle the scenario accordingly
            return
        }

        MethodChannel(flutterEngine!!.dartExecutor.binaryMessenger, "nfc_channel").setMethodCallHandler { methodCall, result ->
            when (methodCall.method) {
                "startNFCService" -> {
                    val ndefMessage = methodCall.argument<String>("ndefMessage")
                    if (ndefMessage != null) {
                        startNFCService(ndefMessage)
                        result.success(null)
                    } else {
                        result.error("MISSING_ARGUMENT", "NDEF message is missing", null)
                    }
                }
                "stopNFCService" -> {
                    stopNFCService()
                    result.success(null)
                }
                else -> result.notImplemented()
            }
        }
    }

    private fun startNFCService(ndefMessage: String) {
        val message = ndefMessage.toByteArray()
        val record = NdefRecord.createMime("text/plain", message)
        val ndefMessage = NdefMessage(arrayOf(record))
        nfcAdapter.setNdefPushMessage(ndefMessage, this)
    }

    private fun stopNFCService() {
        nfcAdapter.setNdefPushMessage(null, this)
    }
}
