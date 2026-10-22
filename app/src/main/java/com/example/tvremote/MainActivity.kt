package com.example.tvremote

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.android.gms.cast.MediaInfo
import com.google.android.gms.cast.MediaLoadRequestData
import com.google.android.gms.cast.MediaMetadata
import com.google.android.gms.cast.framework.CastContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val client = TvRemoteClient()
    private val scope = CoroutineScope(Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val cast = CastContext.getSharedInstance(this)
        setContent { MaterialTheme { Screen(cast) } }
    }

    @Composable
    fun Screen(cast: CastContext) {
        var ip by remember { mutableStateOf("") }
        var code by remember { mutableStateOf("") }
        var url by remember { mutableStateOf("") }
        var status by remember { mutableStateOf("Belum terhubung") }

        Column(Modifier.padding(16.dp).fillMaxSize(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(ip, { ip = it }, label = { Text("IP Google TV (mis. 192.168.1.20)") }, modifier = Modifier.fillMaxWidth())
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = { scope.launch { client.startPairing(ip); status = "Masukkan kode di TV" } }) { Text("Pairing") }
                Button(onClick = { scope.launch { client.connect(ip); status = "Terhubung" } }) { Text("Connect") }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(code, { code = it }, label = { Text("Kode TV") }, modifier = Modifier.weight(1f))
                Button(onClick = { scope.launch { client.finishPairing(code); status = "Pairing selesai" } }) { Text("OK") }
            }
            Text(status)

            // D-pad
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) { K("▲", TvRemoteClient.UP) }
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                K("◀", TvRemoteClient.LEFT); K("OK", TvRemoteClient.OK); K("▶", TvRemoteClient.RIGHT)
            }
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) { K("▼", TvRemoteClient.DOWN) }
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                K("Back", TvRemoteClient.BACK); K("Home", TvRemoteClient.HOME); K("Power", TvRemoteClient.POWER)
            }
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                K("Vol-", TvRemoteClient.VOL_DOWN); K("Mute", TvRemoteClient.MUTE); K("Vol+", TvRemoteClient.VOL_UP)
            }

            HorizontalDivider()
            Text("Cast")
            OutlinedTextField(url, { url = it }, label = { Text("URL video (mp4/HLS)") }, modifier = Modifier.fillMaxWidth())
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = { castUrl(cast, url) }) { Text("Cast URL") }
                // Mirror layar HP lewat fitur bawaan Android (Cast screen)
                Button(onClick = { startActivity(Intent(Settings.ACTION_CAST_SETTINGS)) }) { Text("Mirror Layar") }
            }

            Spacer(Modifier.weight(1f))
            Text(
                "with love by Kapten Nemo ❤",
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.fillMaxWidth(),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }

    @Composable
    fun K(label: String, key: Int) = Button(onClick = { client.sendKey(key) }, modifier = Modifier.padding(2.dp)) { Text(label) }

    private fun castUrl(cast: CastContext, url: String) {
        val session = cast.sessionManager.currentCastSession ?: return
        val meta = MediaMetadata(MediaMetadata.MEDIA_TYPE_MOVIE).apply { putString(MediaMetadata.KEY_TITLE, "Cast dari HP") }
        val info = MediaInfo.Builder(url).setStreamType(MediaInfo.STREAM_TYPE_BUFFERED)
            .setContentType(if (url.endsWith(".m3u8")) "application/x-mpegURL" else "video/mp4")
            .setMetadata(meta).build()
        session.remoteMediaClient?.load(MediaLoadRequestData.Builder().setMediaInfo(info).setAutoplay(true).build())
    }
}
