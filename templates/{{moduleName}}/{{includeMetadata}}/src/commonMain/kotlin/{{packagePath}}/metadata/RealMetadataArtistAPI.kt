package {{packageName}}.metadata

import dev.krtirtho.plugin_interfaces.plugin_apis.metadata.album.MetadataAlbum
import dev.krtirtho.plugin_interfaces.plugin_apis.metadata.artist.MetadataArtist
import dev.krtirtho.plugin_interfaces.plugin_apis.metadata.artist.MetadataArtistAPI
import dev.krtirtho.plugin_interfaces.plugin_apis.metadata.artist.MetadataArtistOverview
import dev.krtirtho.plugin_interfaces.plugin_apis.metadata.common.PaginationResult
import dev.krtirtho.plugin_interfaces.plugin_apis.metadata.common.PaginationStrategy
import dev.krtirtho.plugin_interfaces.plugin_apis.metadata.playlist.MetadataPlaylist
import dev.krtirtho.plugin_interfaces.plugin_apis.metadata.track.MetadataTrack
import {{packageName}}.core.{{serviceClassName}}

class RealMetadataArtistAPI(private val service: {{serviceClassName}}) : MetadataArtistAPI {
    override suspend fun getArtist(id: String): MetadataArtist.Detailed =
        TODO("Fetch and map a provider artist")

    override suspend fun artistOverview(id: String): MetadataArtistOverview =
        TODO("Build an artist overview from provider data")

    override suspend fun getArtistTop10Tracks(id: String): List<MetadataTrack> =
        TODO("Fetch an artist's top tracks")

    override suspend fun relatedArtists(
        id: String,
        pagination: PaginationStrategy?,
    ): PaginationResult<MetadataArtist.Basic> = TODO("Fetch related artists")

    override suspend fun featuredPlaylists(
        id: String,
        pagination: PaginationStrategy?,
    ): PaginationResult<MetadataPlaylist> = TODO("Fetch artist-related playlists")

    override suspend fun getArtistAlbums(
        id: String,
        pagination: PaginationStrategy?,
    ): PaginationResult<MetadataAlbum.Detailed> = TODO("Fetch an artist's albums")

    override suspend fun savedArtists(
        pagination: PaginationStrategy?,
    ): PaginationResult<MetadataArtist.Detailed> = TODO("Fetch the user's saved artists")

    override suspend fun isSavedArtists(ids: List<String>): List<Boolean> =
        TODO("Check which artists are saved")

    override suspend fun saveArtists(ids: List<String>) {
        TODO("Save artists to the user's library")
    }

    override suspend fun removeSavedArtists(ids: List<String>) {
        TODO("Remove artists from the user's library")
    }
}
