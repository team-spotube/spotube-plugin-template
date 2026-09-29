package {{packageName}}.metadata

import dev.krtirtho.plugin_interfaces.plugin_apis.metadata.browse.MetadataBrowseAPI
import dev.krtirtho.plugin_interfaces.plugin_apis.metadata.browse.MetadataBrowseGenre
import dev.krtirtho.plugin_interfaces.plugin_apis.metadata.browse.MetadataBrowseItem
import dev.krtirtho.plugin_interfaces.plugin_apis.metadata.browse.MetadataBrowseSection
import dev.krtirtho.plugin_interfaces.plugin_apis.metadata.common.PaginationResult
import dev.krtirtho.plugin_interfaces.plugin_apis.metadata.common.PaginationStrategy
import {{packageName}}.core.{{serviceClassName}}

class RealMetadataBrowseAPI(private val service: {{serviceClassName}}) : MetadataBrowseAPI {
    override suspend fun featured(): List<MetadataBrowseItem> =
        TODO("Fetch featured provider content")

    override suspend fun genres(): List<MetadataBrowseGenre> =
        TODO("Fetch browse genres/categories")

    override suspend fun list(
        genreId: String,
        pagination: PaginationStrategy?,
    ): PaginationResult<MetadataBrowseSection> = TODO("Fetch sections for a browse genre")

    override suspend fun sublist(
        genreId: String,
        sectionId: String,
        pagination: PaginationStrategy?,
    ): PaginationResult<MetadataBrowseItem> = TODO("Fetch items in a browse section")
}
