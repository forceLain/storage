public class MainActivity extends Activity {
    
    private static final String IMAGE_URL = "http://eastbancgroup.com/images/ebtLogo.gif";
    
    TextView textView;
    ImageView imageView;
    ProgressDialog progressDialog;
    DownloadTask downloadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        textView = (TextView)findViewById(R.id.hello);
        imageView = (ImageView)findViewById(R.id.imageView);
                
        downloadTask = new DownloadTask();
        //Запускаем задачу, передавая ей ссылку на картинку
        downloadTask.execute(IMAGE_URL);
    }
    
    @Override
    protected void onStop() {
        //Завершить загрузку картинки сразу
        //как закроется Activity
        downloadTask.cancel(true);
        super.onStop();
    }
    
    /*
     * При расширении класса  AsyncTask<Params, Progress, Result>
     * необходимо указать, какими типами будут его generic-параметры.
     * Params - тип входных данных. В нашем случае будет String, т.к. 
     * передаваться будет url картинки
     * Progress - тип данных, которые будут переданы для обновления прогресса.
     * В нашем случае Integer. 
     * Result - тип результата. В нашем случае Drawable.
     */
    class DownloadTask extends AsyncTask<String, Integer, Drawable>{
        
        @Override
        protected void onPreExecute() {
            //Отображаем системный диалог загрузки
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setIndeterminate(false);
            progressDialog.setMax(100);
            progressDialog.setProgress(0);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setMessage("Downloading Image");
            progressDialog.show();
        }

        @Override
        protected Drawable doInBackground(String... params) {
            //В этом методе происходит загрузка картинки через
            //стандартный класс URLConnection
            int count;
            try {
                URL url = new URL(params[0]);
                URLConnection conection = url.openConnection();
                conection.connect();
                int lenghtOfFile = conection.getContentLength();
                InputStream input = new BufferedInputStream(url.openStream(), 8192);
                OutputStream output = new FileOutputStream("/sdcard/downloadedfile.jpg");
                byte data[] = new byte[256];     
                long total = 0;
     
                while ((count = input.read(data)) != -1) {
                    
                    //Проверяем, актуальна ли еще задача
                    if (isCancelled()){
                        return null;
                    }
                    total += count;  
                    output.write(data, 0, count);
                    
                    //Информирование о закачке.
                    //Передаем число, отражающее процент загрузки файла
                    //После вызова этого метода автоматически будет вызван
                    //onProgressUpdate в главном потоке
                    publishProgress((int)((total*100)/lenghtOfFile));
                }
                output.flush(); 
                output.close();
                input.close();
     
            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }
     
            String imagePath = Environment.getExternalStorageDirectory().toString() + "/downloadedfile.jpg";
            return Drawable.createFromPath(imagePath);
        }
        
        @Override
        protected void onProgressUpdate(Integer... progress) {
            progressDialog.setProgress(progress[0]);
        }
        
        //скроем диалог и покажем картинку
        @Override
        protected void onPostExecute(Drawable result) {
            imageView.setImageDrawable(result);
            progressDialog.dismiss();
        }
        
        //Этот метод будет вызван вместо onPostExecute,
        //если мы остановили выполнение задачи методом 
        //AsyncTask#cancel(boolean mayInterruptIfRunning)
        @Override
        protected void onCancelled() {

        }
    }
}