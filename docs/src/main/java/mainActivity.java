public class MainActivity extends AppCompatActivity {

    private final ActivityResultLauncher<Intent> zkLoginGoogleResultLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult result) {
                            if (result.getResultCode() == Activity.RESULT_OK) {
                                Intent data = result.getData();
                                // Handle the login response
                            }
                        }
                    }
            );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ZKLoginRequest zkLoginRequest =
                new ZKLoginRequest(new OpenIDServiceConfiguration(new Google(), "YOUR_CLIENT_ID", "YOUR_REDIRECT_URI"));

        DefaultZKLoginService zkLoginService = new DefaultZKLoginService(this);
        Intent intent = zkLoginService.zkLogin(zkLoginRequest);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                zkLoginGoogleResultLauncher.launch(intent);
            }
        });
    }
}