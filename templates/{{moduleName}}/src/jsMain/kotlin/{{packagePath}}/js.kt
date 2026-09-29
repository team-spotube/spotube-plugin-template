package {{packageName}}

import app.cash.zipline.Zipline
import dev.krtirtho.plugin_interfaces.core.runPluginInitialized
import dev.krtirtho.plugin_interfaces.host_apis.HttpClientAPI
import dev.krtirtho.plugin_interfaces.host_apis.HttpClientAPI_SERVICE_NAME
import dev.krtirtho.plugin_interfaces.host_apis.PersistedStorageAPI
import dev.krtirtho.plugin_interfaces.host_apis.PersistedStorageAPI_SERVICE_NAME
import dev.krtirtho.plugin_interfaces.host_apis.WebViewAPI
import dev.krtirtho.plugin_interfaces.host_apis.WebViewAPI_SERVICE_NAME
import dev.krtirtho.plugin_interfaces.plugin_apis.core.CoreAPI
import dev.krtirtho.plugin_interfaces.plugin_apis.core.CoreAPI_SERVICE_NAME
import {{packageName}}.core.{{serviceClassName}}
import {{packageName}}.core.RealCoreAPI
{{#includeMetadata}}
import dev.krtirtho.plugin_interfaces.plugin_apis.metadata.album.MetadataAlbumAPI
import dev.krtirtho.plugin_interfaces.plugin_apis.metadata.album.MetadataAlbumAPI_SERVICE_NAME
import dev.krtirtho.plugin_interfaces.plugin_apis.metadata.artist.MetadataArtistAPI
import dev.krtirtho.plugin_interfaces.plugin_apis.metadata.artist.MetadataArtistAPI_SERVICE_NAME
import dev.krtirtho.plugin_interfaces.plugin_apis.metadata.browse.MetadataBrowseAPI
import dev.krtirtho.plugin_interfaces.plugin_apis.metadata.browse.MetadataBrowseAPI_SERVICE_NAME
import dev.krtirtho.plugin_interfaces.plugin_apis.metadata.playlist.MetadataPlaylistAPI
import dev.krtirtho.plugin_interfaces.plugin_apis.metadata.playlist.MetadataPlaylistAPI_SERVICE_NAME
import dev.krtirtho.plugin_interfaces.plugin_apis.metadata.search.MetadataSearchAPI
import dev.krtirtho.plugin_interfaces.plugin_apis.metadata.search.MetadataSearchAPI_SERVICE_NAME
import dev.krtirtho.plugin_interfaces.plugin_apis.metadata.track.MetadataTrackAPI
import dev.krtirtho.plugin_interfaces.plugin_apis.metadata.track.MetadataTrackAPI_SERVICE_NAME
import dev.krtirtho.plugin_interfaces.plugin_apis.metadata.user.MetadataUserAPI
import dev.krtirtho.plugin_interfaces.plugin_apis.metadata.user.MetadataUserAPI_SERVICE_NAME
import {{packageName}}.metadata.RealMetadataAlbumAPI
import {{packageName}}.metadata.RealMetadataArtistAPI
import {{packageName}}.metadata.RealMetadataBrowseAPI
import {{packageName}}.metadata.RealMetadataPlaylistAPI
import {{packageName}}.metadata.RealMetadataSearchAPI
import {{packageName}}.metadata.RealMetadataTrackAPI
import {{packageName}}.metadata.RealMetadataUserAPI
{{/includeMetadata}}
{{#includeAudio}}
import dev.krtirtho.plugin_interfaces.host_apis.CryptoAPI
import dev.krtirtho.plugin_interfaces.host_apis.CryptoAPI_SERVICE_NAME
import dev.krtirtho.plugin_interfaces.plugin_apis.audio.AudioAPI
import dev.krtirtho.plugin_interfaces.plugin_apis.audio.AudioAPI_SERVICE_NAME
import {{packageName}}.audio.RealAudioAPI
{{/includeAudio}}
{{#includeLyrics}}
import dev.krtirtho.plugin_interfaces.plugin_apis.lyrics.LyricsAPI
import dev.krtirtho.plugin_interfaces.plugin_apis.lyrics.LyricsAPI_SERVICE_NAME
import {{packageName}}.plugin_apis.lyrics.RealLyricsAPI
{{/includeLyrics}}
{{#includeScrobble}}
import dev.krtirtho.plugin_interfaces.plugin_apis.scrobble.ScrobbleAPI
import dev.krtirtho.plugin_interfaces.plugin_apis.scrobble.ScrobbleAPI_SERVICE_NAME
import {{packageName}}.plugin_apis.scrobble.RealScrobbleAPI
{{/includeScrobble}}

private val zipline by lazy { Zipline.get() }

@OptIn(ExperimentalJsExport::class)
@JsExport
fun main() {
    runPluginInitialized {
        val httpClient = zipline.take<HttpClientAPI>(HttpClientAPI_SERVICE_NAME)
        val webview = zipline.take<WebViewAPI>(WebViewAPI_SERVICE_NAME)
        val storage = zipline.take<PersistedStorageAPI>(PersistedStorageAPI_SERVICE_NAME)

        val client = {{serviceClassName}}(httpClient)
        zipline.bind<CoreAPI>(CoreAPI_SERVICE_NAME, RealCoreAPI(webview, storage, client))

        {{#includeMetadata}}zipline.bind<MetadataAlbumAPI>(MetadataAlbumAPI_SERVICE_NAME, RealMetadataAlbumAPI(client))
        zipline.bind<MetadataArtistAPI>(MetadataArtistAPI_SERVICE_NAME, RealMetadataArtistAPI(client))
        zipline.bind<MetadataBrowseAPI>(MetadataBrowseAPI_SERVICE_NAME, RealMetadataBrowseAPI(client))
        zipline.bind<MetadataPlaylistAPI>(MetadataPlaylistAPI_SERVICE_NAME, RealMetadataPlaylistAPI(client))
        zipline.bind<MetadataSearchAPI>(MetadataSearchAPI_SERVICE_NAME, RealMetadataSearchAPI(client))
        zipline.bind<MetadataTrackAPI>(MetadataTrackAPI_SERVICE_NAME, RealMetadataTrackAPI(client))
        zipline.bind<MetadataUserAPI>(MetadataUserAPI_SERVICE_NAME, RealMetadataUserAPI(client)){{/includeMetadata}}
        {{#includeAudio}}
        val crypto = zipline.take<CryptoAPI>(CryptoAPI_SERVICE_NAME)
        zipline.bind<AudioAPI>(AudioAPI_SERVICE_NAME, RealAudioAPI(client, crypto))
        {{/includeAudio}}
        {{#includeLyrics}}zipline.bind<LyricsAPI>(LyricsAPI_SERVICE_NAME, RealLyricsAPI()){{/includeLyrics}}
        {{#includeScrobble}}zipline.bind<ScrobbleAPI>(ScrobbleAPI_SERVICE_NAME, RealScrobbleAPI()){{/includeScrobble}}
    }
}
