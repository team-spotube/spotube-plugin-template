package {{packageName}}.metadata

import dev.krtirtho.plugin_interfaces.plugin_apis.metadata.common.PaginationResult
import dev.krtirtho.plugin_interfaces.plugin_apis.metadata.common.PaginationStrategy
import dev.krtirtho.plugin_interfaces.plugin_apis.metadata.playlist.MetadataPlaylist
import dev.krtirtho.plugin_interfaces.plugin_apis.metadata.playlist.MetadataPlaylistAPI
import dev.krtirtho.plugin_interfaces.plugin_apis.metadata.track.MetadataTrack
import {{packageName}}.core.{{serviceClassName}}

class RealMetadataPlaylistAPI(private val service: {{serviceClassName}}) : MetadataPlaylistAPI {
    override suspend fun getPlaylist(id: String): MetadataPlaylist =
        TODO("Fetch and map a provider playlist")

    override suspend fun getPlaylistTracks(
        id: String,
        pagination: PaginationStrategy?,
    ): PaginationResult<MetadataTrack> = TODO("Fetch paginated playlist tracks")

    override suspend fun savedPlaylists(
        pagination: PaginationStrategy?,
    ): PaginationResult<MetadataPlaylist> = TODO("Fetch the user's saved playlists")

    override suspend fun isSavedPlaylists(ids: List<String>): List<Boolean> =
        TODO("Check which playlists are saved")

    override suspend fun savePlaylists(ids: List<String>) {
        TODO("Save playlists to the user's library")
    }

    override suspend fun removeSavedPlaylists(ids: List<String>) {
        TODO("Remove playlists from the user's library")
    }

    override suspend fun createPlaylist(
        name: String,
        description: String?,
        isPublic: Boolean,
        isCollaborating: Boolean,
        imageBase64: String,
        trackIds: List<String>,
    ): MetadataPlaylist = TODO("Create a provider playlist")

    override suspend fun updatePlaylist(
        id: String,
        name: String?,
        description: String?,
        isPublic: Boolean?,
        isCollaborating: Boolean?,
        imageBase64: String?,
        trackIds: List<String>?,
    ): MetadataPlaylist = TODO("Update a provider playlist")

    override suspend fun deletePlaylist(id: String) {
        TODO("Delete a provider playlist")
    }

    override suspend fun addTracksToPlaylist(playlistId: String, trackIds: List<String>) {
        TODO("Add tracks to a provider playlist")
    }

    override suspend fun removeTracksFromPlaylist(playlistId: String, trackIds: List<String>) {
        TODO("Remove tracks from a provider playlist")
    }
}
