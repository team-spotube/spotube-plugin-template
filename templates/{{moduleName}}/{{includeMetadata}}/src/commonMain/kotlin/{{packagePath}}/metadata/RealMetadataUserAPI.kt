package {{packageName}}.metadata

import org.spotube.plugin_interfaces.plugin_apis.metadata.user.MetadataUser
import org.spotube.plugin_interfaces.plugin_apis.metadata.user.MetadataUserAPI
import {{packageName}}.core.{{serviceClassName}}

class RealMetadataUserAPI(private val service: {{serviceClassName}}) : MetadataUserAPI {
    override suspend fun getUser(id: String): MetadataUser? =
        TODO("Fetch and map a provider user")
}
