public class MainActivity extends Activity {
    
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        textView = (TextView)findViewById(R.id.hello);
                
        WorkingClass workingClass = new WorkingClass();
        Thread thread = new Thread(workingClass);
        thread.start();
    }
    
    class WorkingClass implements Runnable{
        @Override
        public void run() {
            //Фоновая работа
            
            //Отправить в UI поток новый Runnable
            textView.post(new Runnable() {
                @Override
                public void run() {
                    textView.setText("The job is done!");
                }
            });
        }
    }   
}