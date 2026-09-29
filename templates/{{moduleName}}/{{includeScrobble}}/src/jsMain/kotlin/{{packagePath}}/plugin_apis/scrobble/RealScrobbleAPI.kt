package {{packageName}}.plugin_apis.scrobble

import dev.krtirtho.plugin_interfaces.plugin_apis.scrobble.ScrobbleAPI
import dev.krtirtho.plugin_interfaces.plugin_apis.scrobble.ScrobbleTrack

class RealScrobbleAPI : ScrobbleAPI {
    override suspend fun scrobble(track: ScrobbleTrack) {
        TODO("Send playback progress to the provider")
    }
}
