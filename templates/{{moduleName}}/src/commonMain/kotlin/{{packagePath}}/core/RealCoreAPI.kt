package {{packageName}}.core

import dev.krtirtho.plugin_interfaces.host_apis.PersistedStorageAPI
import dev.krtirtho.plugin_interfaces.host_apis.WebViewAPI
import dev.krtirtho.plugin_interfaces.plugin_apis.core.CoreAPI
import dev.krtirtho.plugin_interfaces.plugin_apis.core.PluginUpdateInfo
import kotlinx.coroutines.flow.StateFlow
import net.swiftzer.semver.SemVer

class RealCoreAPI(
    private val webViewAPI: WebViewAPI,
    private val storage: PersistedStorageAPI,
    private val service: {{serviceClassName}},
) : CoreAPI {
    override val requiresAuthentication: Boolean =
        TODO("Choose whether this provider requires user authentication")

    override val loggedInFlow: StateFlow<Boolean> =
        TODO("Expose the provider authentication state")

    override suspend fun checkPluginUpdates(currentVersion: SemVer): PluginUpdateInfo? =
        TODO("Implement provider plugin update checks, or return null")

    override fun supportMarkdownText(currentVersion: SemVer): String =
        TODO("Return the provider's support/help text")

    override suspend fun login() {
        TODO("Implement provider login")
    }

    override suspend fun logout() {
        TODO("Implement provider logout")
    }
}
