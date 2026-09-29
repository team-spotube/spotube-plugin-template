package {{packageName}}.metadata

import dev.krtirtho.plugin_interfaces.plugin_apis.metadata.common.PaginationResult
import dev.krtirtho.plugin_interfaces.plugin_apis.metadata.common.PaginationStrategy
import dev.krtirtho.plugin_interfaces.plugin_apis.metadata.track.MetadataTrack
import dev.krtirtho.plugin_interfaces.plugin_apis.metadata.track.MetadataTrackAPI
import {{packageName}}.core.{{serviceClassName}}

class RealMetadataTrackAPI(private val service: {{serviceClassName}}) : MetadataTrackAPI {
    override suspend fun getTrack(id: String): MetadataTrack =
        TODO("Fetch and map a provider track")

    override suspend fun savedTracks(
        pagination: PaginationStrategy?,
    ): PaginationResult<MetadataTrack> = TODO("Fetch the user's saved tracks")

    override suspend fun isSavedTracks(ids: List<String>): List<Boolean> =
        TODO("Check which tracks are saved")

    override suspend fun saveTracks(ids: List<String>) {
        TODO("Save tracks to the user's library")
    }

    override suspend fun removeSavedTracks(ids: List<String>) {
        TODO("Remove tracks from the user's library")
    }

    override suspend fun recommendationsBasedOnTracks(
        seedTrackIds: List<String>,
        limit: Int,
    ): List<MetadataTrack> = TODO("Fetch recommendations based on seed tracks")
}
