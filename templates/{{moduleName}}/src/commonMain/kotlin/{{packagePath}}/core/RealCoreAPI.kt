package {{packageName}}.core

import org.spotube.plugin_interfaces.host_apis.PersistedStorageAPI
import org.spotube.plugin_interfaces.host_apis.WebViewAPI
import org.spotube.plugin_interfaces.plugin_apis.core.CoreAPI
import org.spotube.plugin_interfaces.plugin_apis.core.PluginUpdateInfo
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
        // For WebView login, read cookies with webViewAPI.getCookies(url) only for hosts listed in
        // cookieDomains (spotubePlugin {} in build.gradle.kts); other hosts are refused.
        TODO("Implement provider login")
    }

    override suspend fun logout() {
        TODO("Implement provider logout")
    }
}
