package com.example.flutter_kotlin_integration

import android.content.Intent
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.Ndef
import android.os.Bundle
import android.widget.Toast
import io.flutter.embedding.android.FlutterActivity
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import java.nio.charset.Charset
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

    private fun startNFCService(ndefMessageText: String) {
        if (checkNFCEnable()) {
            // Create NDEF record
            val ndefRecord = createTextRecord(ndefMessageText)

            // Encode NDEF message
            val ndefMessage = NdefMessage(ndefRecord)

            // Start NFC transmission
            val intent = Intent(this, javaClass).apply {
                action = NfcAdapter.ACTION_TAG_DISCOVERED
                putExtra(NfcAdapter.EXTRA_NDEF_MESSAGES, arrayOf(ndefMessage))
            }
            startActivity(intent)
        } else {
            Toast.makeText(this, "NFC is not enabled", Toast.LENGTH_SHORT).show()
        }
    }

    private fun stopNFCService() {
        // No action needed here, as NFC transmission is stopped automatically
    }

    private fun checkNFCEnable(): Boolean {
        return mNfcAdapter?.isEnabled == true
    }

    private fun createTextRecord(text: String): NdefRecord {
        val languageCode: ByteArray = "en".toByteArray(Charset.forName("US-ASCII"))
        val textBytes: ByteArray = text.toByteArray(Charset.forName("UTF-8"))
        val payload = ByteArray(textBytes.size + 1)
        payload[0] = 0x02.toByte() // UTF-8
        System.arraycopy(languageCode, 0, payload, 1, languageCode.size)
        System.arraycopy(textBytes, 0, payload, languageCode.size + 1, textBytes.size)
        return NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, byteArrayOf(), payload)
    }
}
