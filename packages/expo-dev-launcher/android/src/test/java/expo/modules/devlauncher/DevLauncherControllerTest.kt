package expo.modules.devlauncher

import android.content.Intent
import android.net.Uri
import androidx.test.core.app.ApplicationProvider
import com.facebook.react.ReactNativeHost
import com.google.common.truth.Truth
import expo.interfaces.devmenu.DevMenuManagerInterface
import expo.modules.devlauncher.koin.DevLauncherKoinContext
import expo.modules.devlauncher.tests.DevLauncherTestInterceptor
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class DevLauncherControllerTest {

  internal class DevLauncherTestInterceptorAllowReinitialization : DevLauncherTestInterceptor {
    override fun allowReinitialization() = true
  }

  @Before
  fun setup() {
    val reactNativeHost = mockk<ReactNativeHost>(relaxed = true)
    DevLauncherKoinContext.reinitialize()
    DevLauncherKoinContext.app.koin.declare(DevLauncherTestInterceptorAllowReinitialization())
    DevLauncherController.initialize(ApplicationProvider.getApplicationContext(), reactNativeHost)
  }

  @Test
  fun `sets shouldAutoLaunch on dev menu manager`() {
    val controller = DevLauncherController.instance as DevLauncherController
    val mockDevMenuManager = mockk<DevMenuManagerInterface>(relaxed = true)
    controller.devMenuManager = mockDevMenuManager
    Truth.assertThat(controller.canLaunchDevMenuOnStart).isTrue()

    controller.handleIntent(Intent().apply {
      data = Uri.parse("https://expo-development-client")
      putExtra("EXDevMenuDisableAutoLaunch", true)
    }, null)

    verify {
      mockDevMenuManager.setCanLaunchDevMenuOnStart(false)
    }
    Truth.assertThat(controller.canLaunchDevMenuOnStart).isFalse()
  }
}