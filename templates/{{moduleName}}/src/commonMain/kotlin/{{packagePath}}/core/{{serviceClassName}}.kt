package {{packageName}}.core

import dev.krtirtho.plugin_interfaces.host_apis.HttpClientAPI

/** Provider client boundary. Add endpoint methods and response mapping for the selected service. */
class {{serviceClassName}}(
    val httpClientAPI: HttpClientAPI,
)
