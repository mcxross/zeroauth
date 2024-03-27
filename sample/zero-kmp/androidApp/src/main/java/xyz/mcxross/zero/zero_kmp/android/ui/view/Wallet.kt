package xyz.mcxross.zero.zero_kmp.android.ui.view

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.util.*
import kotlinx.coroutines.launch
import xyz.mcxross.ksui.client.EndPoint
import xyz.mcxross.ksui.client.suiHttpClient
import xyz.mcxross.ksui.model.SuiAddress
import xyz.mcxross.ksui.util.requestTestTokens
import xyz.mcxross.zero.model.ZKLoginResponse

@Composable
fun WalletView(context: Context, zkLoginResponse: ZKLoginResponse) {
  var balance = remember { mutableLongStateOf(0L) }
  val coroutineScope = rememberCoroutineScope()
  val suiHttpClient = remember { suiHttpClient { endpoint = EndPoint.DEVNET } }
  var myAddress by remember { mutableStateOf(zkLoginResponse.address) }

  // This state is used to trigger balance updates.
  val updateTrigger = remember { mutableIntStateOf(0) }

  LaunchedEffect(key1 = updateTrigger.intValue, key2 = myAddress) {
    if (myAddress?.isNotEmpty() == true) {

      balance.longValue = suiHttpClient.getBalance(SuiAddress(myAddress!!)).totalBalance
    }
  }

  // Periodic balance update logic
  DisposableEffect(Unit) {
    val timer = Timer()
    timer.schedule(
      object : TimerTask() {
        override fun run() {
          coroutineScope.launch {
            // Increment the trigger to refresh the balance
            updateTrigger.intValue += 1
          }
        }
      },
      0,
      5000, // Update every 5 seconds
    )

    onDispose { timer.cancel() }
  }

  Column {
    Text(
      "Account",
      modifier = Modifier.padding(4.dp).height(30.dp),
      fontWeight = FontWeight.Bold,
      fontSize = MaterialTheme.typography.h5.fontSize,
    )
    Divider()
    Column {
      Text(
        myAddress ?: "No Address",
        modifier = Modifier.padding(4.dp),
        fontWeight = FontWeight.Normal,
        fontSize = MaterialTheme.typography.body1.fontSize,
      )
      Text(
        "Balance: ${balance.longValue.div(1_000_000_000)} SUI",
        modifier = Modifier.padding(4.dp),
        fontWeight = FontWeight.Bold,
        fontSize = MaterialTheme.typography.h6.fontSize,
      )
    }
    Button(
      onClick = { Toast.makeText(context, "Coming Soon!", Toast.LENGTH_SHORT).show() },
      modifier = Modifier.padding(top = 20.dp).fillMaxWidth().padding(4.dp),
      shape = RoundedCornerShape(15.dp),
    ) {
      Text("Send")
    }
    Button(
      onClick = {
        coroutineScope.launch {
          if (myAddress?.isNotEmpty() == true) {
            suiHttpClient.requestTestTokens(SuiAddress(myAddress!!))
          }
        }
      },
      modifier = Modifier.fillMaxWidth().padding(4.dp),
      shape = RoundedCornerShape(15.dp),
    ) {
      Text("Request Test Tokens")
    }

    Spacer(modifier = Modifier.weight(1f))

    Button(
      onClick = {},
      modifier =
        Modifier.padding(top = 20.dp, bottom = 20.dp, end = 30.dp, start = 30.dp)
          .fillMaxWidth()
          .padding(4.dp),
      shape = RoundedCornerShape(15.dp),
    ) {
      Text("Log Out")
    }
  }
}
