public class MainActivity extends Activity {
        
    TextView textView;
    private int counter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        counter = 0;
        textView = (TextView)findViewById(R.id.hello);
        
        //старт таймера
        new Thread(new WorkingClass()).start();
    }
    
    class WorkingClass implements Runnable{
        
        public static final int RELAUNCH = 1;
        private static final int DELAY = 1000;

        @Override
        public void run() {
            //фоновая операция
            
            //отправим сообщение хендлеру с задержкой в 1000ms
            uiHandler.sendEmptyMessageDelayed(RELAUNCH, DELAY);
        }
        
    }
    
    Handler uiHandler = new Handler(new Handler.Callback() {
        
        @Override
        public boolean handleMessage(Message msg) {
            //перезапустим поток
            if (msg.what == WorkingClass.RELAUNCH){
                textView.setText("Times: "+counter);
                counter++;
                new Thread(new WorkingClass()).start();
                return true;
            }
            return false;
        }
    });
}