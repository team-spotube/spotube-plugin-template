package {{packageName}}.metadata

import dev.krtirtho.plugin_interfaces.plugin_apis.metadata.album.MetadataAlbum
import dev.krtirtho.plugin_interfaces.plugin_apis.metadata.album.MetadataAlbumAPI
import dev.krtirtho.plugin_interfaces.plugin_apis.metadata.common.PaginationResult
import dev.krtirtho.plugin_interfaces.plugin_apis.metadata.common.PaginationStrategy
import dev.krtirtho.plugin_interfaces.plugin_apis.metadata.track.MetadataTrack
import {{packageName}}.core.{{serviceClassName}}

class RealMetadataAlbumAPI(private val service: {{serviceClassName}}) : MetadataAlbumAPI {
    override suspend fun getAlbum(id: String): MetadataAlbum.Detailed =
        TODO("Fetch and map a provider album")

    override suspend fun getTrackAlbum(track: MetadataTrack): MetadataAlbum.Detailed =
        TODO("Fetch the album associated with a provider track")

    override suspend fun getAlbumTracks(
        id: String,
        pagination: PaginationStrategy?,
    ): PaginationResult<MetadataTrack> = TODO("Fetch paginated album tracks")

    override suspend fun savedAlbums(
        pagination: PaginationStrategy?,
    ): PaginationResult<MetadataAlbum.Detailed> = TODO("Fetch the user's saved albums")

    override suspend fun isSavedAlbums(ids: List<String>): List<Boolean> =
        TODO("Check which albums are saved")

    override suspend fun saveAlbums(ids: List<String>) {
        TODO("Save albums to the user's library")
    }

    override suspend fun removeSavedAlbums(ids: List<String>) {
        TODO("Remove albums from the user's library")
    }
}
