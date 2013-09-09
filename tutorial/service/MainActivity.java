public class MainActivity extends Activity {
        
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        textView = (TextView)findViewById(R.id.hello);      
        
        //Подписываемся на события нашего сервиса
        registerReceiver(receiver, new IntentFilter(BackgroundService.CHANNEL));
        
        //Запускаем сервис, передавая ему новый Intent
        Intent intent = new Intent(this, BackgroundService.class);
        startService(intent);
    }
    
    @Override
    protected void onStop() {
        //Отписываемся от событий сервиса
        unregisterReceiver(receiver);
        super.onStop();
    }
    
    
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            textView.setText("Message from Service");
        }
    };
}