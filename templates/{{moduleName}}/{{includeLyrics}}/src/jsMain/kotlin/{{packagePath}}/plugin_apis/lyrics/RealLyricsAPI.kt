package {{packageName}}.plugin_apis.lyrics

import dev.krtirtho.plugin_interfaces.plugin_apis.lyrics.LyricsAPI
import dev.krtirtho.plugin_interfaces.plugin_apis.lyrics.LyricsResponse
import dev.krtirtho.plugin_interfaces.plugin_apis.metadata.track.MetadataTrack

class RealLyricsAPI : LyricsAPI {
    override suspend fun getLyrics(track: MetadataTrack): LyricsResponse? =
        TODO("Fetch and map lyrics for the supplied track")
}
