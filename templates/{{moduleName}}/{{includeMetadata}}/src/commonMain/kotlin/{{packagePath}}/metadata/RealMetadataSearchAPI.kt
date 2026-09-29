package {{packageName}}.metadata

import dev.krtirtho.plugin_interfaces.plugin_apis.metadata.common.PaginationResult
import dev.krtirtho.plugin_interfaces.plugin_apis.metadata.common.PaginationStrategy
import dev.krtirtho.plugin_interfaces.plugin_apis.metadata.search.MetadataSearchAPI
import dev.krtirtho.plugin_interfaces.plugin_apis.metadata.search.MetadataSearchResult
import dev.krtirtho.plugin_interfaces.plugin_apis.metadata.search.MetadataSupportedSearchType
import {{packageName}}.core.{{serviceClassName}}

class RealMetadataSearchAPI(private val service: {{serviceClassName}}) : MetadataSearchAPI {
    override val supportedSearchTypes: List<MetadataSupportedSearchType> =
        TODO("Declare the search types supported by this provider")

    override suspend fun search(query: String): List<MetadataSearchResult> =
        TODO("Search all supported provider result types")

    override suspend fun searchTracks(
        query: String,
        pagination: PaginationStrategy?,
    ): PaginationResult<MetadataSearchResult.Track> = TODO("Search provider tracks")

    override suspend fun searchArtists(
        query: String,
        pagination: PaginationStrategy?,
    ): PaginationResult<MetadataSearchResult.Artist> = TODO("Search provider artists")

    override suspend fun searchAlbums(
        query: String,
        pagination: PaginationStrategy?,
    ): PaginationResult<MetadataSearchResult.Album> = TODO("Search provider albums")

    override suspend fun searchPlaylists(
        query: String,
        pagination: PaginationStrategy?,
    ): PaginationResult<MetadataSearchResult.Playlist> = TODO("Search provider playlists")

    override suspend fun searchUsers(
        query: String,
        pagination: PaginationStrategy?,
    ): PaginationResult<MetadataSearchResult.User> = TODO("Search provider users")
}
