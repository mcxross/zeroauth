class MainActivity : AppCompatActivity() {
  private val zkLoginGoogleResultLauncher =
    registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
      if (result.resultCode == Activity.RESULT_OK) {
        val data: Intent? = result.data
        // Handle the login response
      }
    }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      val zkLoginRequest = ZKLoginRequest(OpenIDServiceConfiguration(Google(), clientID = "YOUR_CLIENT_ID", redirectUri = "YOUR_REDIRECT_URI"))

      val intent = this@MainActivity zkLogin zkLoginRequest

      Button(onClick = {
        zkLoginGoogleResultLauncher.launch(intent)
      }) {
        Text("Login With Google")
      }
    }
  }
}
