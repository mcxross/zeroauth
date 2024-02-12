package xyz.mcxross.zero.zero_kmp.android

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import xyz.mcxross.zero.model.ZKLoginResponse
import xyz.mcxross.zero.util.jsonToZKLoginResponse
import xyz.mcxross.zero.zero_kmp.android.ui.theme.ZeroTheme
import xyz.mcxross.zero.zero_kmp.android.ui.view.HomeView
import xyz.mcxross.zero.zero_kmp.android.ui.view.WalletView

class MainActivity : ComponentActivity() {

  private val zkLoginSuccess = mutableStateOf(false)

  private var activityResult: ActivityResult? = null

  private var zkLoginResponse: ZKLoginResponse? = null

  private val zkLoginResultLauncher =
    registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
      if (result.resultCode == Activity.RESULT_OK) {
        activityResult = result
        zkLoginResponse =
          result.data?.getStringExtra("zkLoginResponse")?.let { jsonToZKLoginResponse(it) }
        zkLoginSuccess.value = true
      } else {
        Toast.makeText(this, "Login failed", Toast.LENGTH_SHORT).show()
      }
    }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      ZeroTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
          if (!zkLoginSuccess.value) {
            HomeView(this@MainActivity, zkLoginResultLauncher)
          } else {
            activityResult?.data?.getStringExtra("zkLoginResponse")
            WalletView(this@MainActivity, zkLoginResponse!!)
          }
        }
      }
    }
  }
}
