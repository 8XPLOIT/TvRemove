package com.example.tvremote

import android.view.KeyEvent
import kotlinx.coroutines.*

/**
 * Remote via IP memakai Android TV Remote Protocol v2:
 *  - Pairing  : TLS port 6467 -> TV menampilkan kode 6 digit -> kirim secret (SHA-256 dari cert + kode)
 *  - Remote   : TLS port 6466 (client certificate hasil pairing), kirim RemoteKeyInject(keyCode, SHORT)
 *
 * Implementasi protokol (protobuf) ditempatkan di sini. Antarmuka sudah dipakai UI.
 */
class TvRemoteClient {
    var connected = false; private set

    suspend fun startPairing(ip: String) = withContext(Dispatchers.IO) {
        // TODO: buka SSLSocket ke ip:6467, kirim PairingRequest, tunggu kode dari TV
    }
    suspend fun finishPairing(code: String) = withContext(Dispatchers.IO) {
        // TODO: kirim PairingSecret, simpan sertifikat klien (KeyStore) lalu connected = true
    }
    suspend fun connect(ip: String) = withContext(Dispatchers.IO) {
        // TODO: SSLSocket ke ip:6466 dengan sertifikat tersimpan; balas ping dari TV
        connected = true
    }
    fun sendKey(keyCode: Int) {
        // TODO: encode RemoteMessage{ remote_key_inject{ key_code, direction=SHORT } } dan tulis ke socket
    }

    companion object {
        val UP = KeyEvent.KEYCODE_DPAD_UP; val DOWN = KeyEvent.KEYCODE_DPAD_DOWN
        val LEFT = KeyEvent.KEYCODE_DPAD_LEFT; val RIGHT = KeyEvent.KEYCODE_DPAD_RIGHT
        val OK = KeyEvent.KEYCODE_DPAD_CENTER; val BACK = KeyEvent.KEYCODE_BACK
        val HOME = KeyEvent.KEYCODE_HOME; val POWER = KeyEvent.KEYCODE_POWER
        val VOL_UP = KeyEvent.KEYCODE_VOLUME_UP; val VOL_DOWN = KeyEvent.KEYCODE_VOLUME_DOWN
        val MUTE = KeyEvent.KEYCODE_VOLUME_MUTE
    }
}
