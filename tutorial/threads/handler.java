public class MainActivity extends Activity {
    
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        textView = (TextView)findViewById(R.id.hello);
                
        WorkingClass workingClass = new WorkingClass(true);
        Thread thread = new Thread(workingClass);
        thread.start();
    }
    
    class WorkingClass implements Runnable{
        
        public static final int SUCCESS = 1;
        public static final int FAIL = 2;
        
        private boolean dummyResult;
        
        public WorkingClass(boolean dummyResult){
            this.dummyResult = dummyResult;
        }       
        
        @Override
        public void run() {
            //Фоновая работа
            
            //Отправить в хэндлеру сообщение
            if (dummyResult){
                //Можно отправить пустое сообщение со статусом
                uiHandler.sendEmptyMessage(SUCCESS);
            } else {
                //Или передать в месте с сообщением данные
                Message msg = Message.obtain();
                msg.what = FAIL;
                msg.obj = "An error occurred";
                uiHandler.sendMessage(msg);
            }
        }
    } 

    Handler uiHandler = new Handler(new Handler.Callback() {
        
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
            case WorkingClass.SUCCESS:
                textView.setText("Success");
                return true;
            case WorkingClass.FAIL:
                textView.setText((String)msg.obj);
                return true;
            }
            return false;
        }
    });  
}
