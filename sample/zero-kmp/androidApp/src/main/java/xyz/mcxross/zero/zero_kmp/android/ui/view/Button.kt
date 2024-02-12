package xyz.mcxross.zero.zero_kmp.android.ui.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp

@Composable
fun LoginButton(
  onClick: () -> Unit,
  text: String,
  painter: Painter,
  tint: Color,
  borderColor: Color = MaterialTheme.colors.primaryVariant,
) =
  Button(
    onClick = onClick,
    modifier = Modifier.height(65.dp).fillMaxWidth().padding(horizontal = 6.dp, vertical = 4.dp),
    shape = RoundedCornerShape(15.dp),
    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent, contentColor = tint),
    border = BorderStroke(1.dp, borderColor),
    elevation = ButtonDefaults.elevation(0.dp),
  ) {
    Row(
      horizontalArrangement = Arrangement.Center,
      verticalAlignment = Alignment.CenterVertically,
    ) {
      Icon(painter = painter, contentDescription = "Login button icon", tint = tint)
      Spacer(modifier = Modifier.width(10.dp))
      Text(text = text)
    }
  }
