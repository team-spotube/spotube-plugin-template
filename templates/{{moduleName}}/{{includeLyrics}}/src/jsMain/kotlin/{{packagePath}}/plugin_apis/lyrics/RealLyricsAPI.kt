package {{packageName}}.plugin_apis.lyrics

import org.spotube.plugin_interfaces.plugin_apis.lyrics.LyricsAPI
import org.spotube.plugin_interfaces.plugin_apis.lyrics.LyricsResponse
import org.spotube.plugin_interfaces.plugin_apis.metadata.track.MetadataTrack

class RealLyricsAPI : LyricsAPI {
    override suspend fun getLyrics(track: MetadataTrack): LyricsResponse? =
        TODO("Fetch and map lyrics for the supplied track")
}
