package {{packageName}}.audio

import dev.krtirtho.plugin_interfaces.host_apis.CryptoAPI
import dev.krtirtho.plugin_interfaces.plugin_apis.audio.AudioAPI
import dev.krtirtho.plugin_interfaces.plugin_apis.audio.AudioFormat
import dev.krtirtho.plugin_interfaces.plugin_apis.audio.AudioSource
import dev.krtirtho.plugin_interfaces.plugin_apis.metadata.track.MetadataTrack
import {{packageName}}.core.{{serviceClassName}}

class RealAudioAPI(
    private val service: {{serviceClassName}},
    private val crypto: CryptoAPI,
) : AudioAPI {
    override val supportedQualities: List<AudioFormat> =
        TODO("Declare the audio formats supported by this provider")

    override suspend fun getStreamsByTrack(track: MetadataTrack): List<AudioSource> =
        TODO("Resolve provider audio sources for a Spotube track")

    override suspend fun getStreamsOfAudioSource(source: AudioSource.Basic): List<AudioSource.Streamed> =
        TODO("Resolve playable streams for a provider audio source")
}
