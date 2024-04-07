package com.example.flutter_kotlin_integration

import android.content.Intent
import android.nfc.NfcAdapter
import android.os.Bundle
import android.widget.Toast
import io.flutter.embedding.android.FlutterActivity
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import com.example.flutter_kotlin_integration.KHostApduService


class MainActivity : FlutterActivity() {
    private var mNfcAdapter: NfcAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this)

        MethodChannel(flutterEngine?.dartExecutor?.binaryMessenger!!, "nfc_channel").setMethodCallHandler { methodCall, result ->
            when (methodCall.method) {
                "startNFCService" -> {
                    val ndefMessage = methodCall.argument<String>("ndefMessage")
                    if (ndefMessage != null) {
                        startNFCService(ndefMessage)
                    } else {
                        result.error("MISSING_ARGUMENT", "NDEF message is missing", null)
                    }
                }
                "stopNFCService" -> stopNFCService()
                else -> result.notImplemented()
            }
        }
    }

    private fun startNFCService(ndefMessage: String) {
        if (checkNFCEnable()) {
            val intent = Intent(this, KHostApduService::class.java) // Replace KHostApduService with the correct class name
            intent.putExtra("ndefMessage", ndefMessage)
            startService(intent)
        } else {
            Toast.makeText(this, "NFC is not enabled", Toast.LENGTH_SHORT).show()
        }
    }

    private fun stopNFCService() {
        val intent = Intent(this, KHostApduService::class.java) // Replace KHostApduService with the correct class name
        stopService(intent)
    }

    private fun checkNFCEnable(): Boolean {
        return mNfcAdapter?.isEnabled == true
    }
}
