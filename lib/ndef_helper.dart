import 'dart:convert';

class NdefRecord {
  final int tnf;
  final List<int> type;
  final List<int> id;
  final List<int> payload;

  NdefRecord({
    required this.tnf,
    required this.type,
    required this.id,
    required this.payload,
  });

  List<int> toBytes() {
    final tnfByte = tnf & 0x07;
    final flags = (tnf & 0x07) << 4;
    final header = flags | type.length;
    final List<int> bytes = [
      header,
      payload.length & 0xFF,
      ...(id.length.toRadixString(16).codeUnits), // ID length
      ...type,
      ...id,
      ...payload,
    ];
    return bytes;
  }
}

class NdefHelper {
  static NdefRecord createTextRecord(String text, String language) {
    final languageBytes = utf8.encode(language);
    final textBytes = utf8.encode(text);

    final payload = [
      languageBytes.length, // Language code length
      ...languageBytes,
      ...textBytes,
    ];

    return NdefRecord(
      tnf: 0x01, // TNF_WELL_KNOWN
      type: [0x54], // RTD_TEXT
      id: [],
      payload: payload,
    );
  }

  static NdefRecord createUriRecord(String uri) {
    final uriBytes = utf8.encode(uri);
    final payload = [0x01, ...uriBytes]; // Prefix byte (0x01) for URI type

    return NdefRecord(
      tnf: 0x01, // TNF_WELL_KNOWN
      type: [0x55], // RTD_URI
      id: [],
      payload: payload,
    );
  }
}
