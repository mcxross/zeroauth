package xyz.mcxross.zero.zero_kmp.android.ui.view

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import xyz.mcxross.zero.extension.zkLogin
import xyz.mcxross.zero.model.Google
import xyz.mcxross.zero.model.OpenIDServiceConfiguration
import xyz.mcxross.zero.model.ZKLoginRequest
import xyz.mcxross.zero.zero_kmp.android.R

@Composable
fun HomeView(context: Context, launcher: ActivityResultLauncher<Intent>) =
  Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
    Column(
      Modifier.fillMaxWidth(),
      verticalArrangement = Arrangement.Center,
      horizontalAlignment = Alignment.CenterHorizontally,
    ) {
      val coroutineScope = rememberCoroutineScope()
      Text(
        "Access Sui",
        modifier = Modifier.padding(bottom = 20.dp),
        fontWeight = FontWeight.Bold,
        fontSize = MaterialTheme.typography.h4.fontSize,
      )
      LoginButton(
        onClick = {
          coroutineScope.launch {
            val googleZKIntent =
              context zkLogin
                ZKLoginRequest(
                  OpenIDServiceConfiguration(
                    Google(),
                    System.getenv("GOOGLE_CLIENT_ID")!!, // Replace with your Google Client ID
                    System.getenv("GOOGLE_REDIRECT_URI")!!, // Replace with your Google Redirect URI,
                  )
                )
            launcher.launch(googleZKIntent)
          }
        },
        text = "Login with Google",
        painter = painterResource(id = R.drawable.ic_google_black_16dp),
        tint = Color(0xFFDB4437),
      )

      LoginButton(
        onClick = { /*TODO*/},
        text = "Login with Apple",
        painter = painterResource(id = R.drawable.ic_apple_black_16dp),
        tint = Color(0xFF000000),
      )

      LoginButton(
        onClick = { /*TODO*/},
        text = "Login with Facebook",
        painter = painterResource(id = R.drawable.ic_facebook_black_16dp),
        tint = Color(0xFF1877F2),
      )

      LoginButton(
        onClick = { /*TODO*/},
        text = "Login with Slack",
        painter = painterResource(id = R.drawable.ic_slack_black_16dp),
        tint = Color(0xFF4A154B),
      )
    }
  }
