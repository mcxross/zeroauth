package xyz.mcxross.zero.zero_kmp.android

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import xyz.mcxross.zero.extension.zkLoginIntent
import xyz.mcxross.zero.model.Google
import xyz.mcxross.zero.model.ZKLoginRequest

class MainActivity : ComponentActivity() {

  private val zkLoginGoogleResultLauncher =
    registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
      if (result.resultCode == Activity.RESULT_OK) {
        Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
      }
      Toast.makeText(this, "Login failed", Toast.LENGTH_SHORT).show()
    }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      MyApplicationTheme {
        Surface(
          modifier = Modifier.fillMaxSize(),
          color = MaterialTheme.colors.background,
        ) {
          Box(
            modifier = Modifier
              .fillMaxSize(),
            contentAlignment = Alignment.Center,
          ) {
            Column(
              Modifier
                .fillMaxWidth(),
            ) {

              LoginButton(
                onClick = {
                  val googleZKIntent =
                    this@MainActivity zkLoginIntent ZKLoginRequest(
                      Google(),
                      "xyz.mcxross.zero",
                      "xyz.mcxross.zero.zero_kmp.android",
                      "some_nonce",
                    )
                  zkLoginGoogleResultLauncher.launch(googleZKIntent)
                },
                text = "Login with Google",
                painter = painterResource(id = R.drawable.ic_google_black_16dp),
                tint = Color(0xFFDB4437),
              )

              LoginButton(
                onClick = { /*TODO*/ },
                text = "Login with Apple",
                painter = painterResource(id = R.drawable.ic_apple_black_16dp),
                tint = Color(0xFF000000),
              )

              LoginButton(
                onClick = { /*TODO*/ },
                text = "Login with Facebook",
                painter = painterResource(id = R.drawable.ic_facebook_black_16dp),
                tint = Color(0xFF1877F2),
              )

              LoginButton(
                onClick = { /*TODO*/ },
                text = "Login with Slack",
                painter = painterResource(id = R.drawable.ic_slack_black_16dp),
                tint = Color(0xFF4A154B),
              )
            }

          }

        }
      }
    }
  }
}

@Composable
fun LoginButton(
  onClick: () -> Unit,
  text: String,
  painter: Painter,
  tint: Color,
  borderColor: Color = MaterialTheme.colors.primaryVariant
) {
  Button(
    onClick = onClick,
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 6.dp, vertical = 4.dp),
    shape = RoundedCornerShape(10.dp),
    colors = ButtonDefaults.buttonColors(
      backgroundColor = Color.Transparent,
      contentColor = tint,
    ),
    border = BorderStroke(1.dp, borderColor),
    elevation = ButtonDefaults.elevation(0.dp),
  ) {
    Row(
      horizontalArrangement = Arrangement.Center,
      verticalAlignment = Alignment.CenterVertically,
    ) {
      Icon(
        painter = painter,
        contentDescription = "Login button icon",
        tint = tint,
      )
      Spacer(modifier = Modifier.width(10.dp))
      Text(text = text)
    }
  }

}
