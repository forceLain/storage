public class MainActivity extends Activity {
	
	private static final String IMAGE_URL = "http://eastbancgroup.com/images/ebtLogo.gif";

	ImageView imageView;
	DownloadManager downloadManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		imageView = (ImageView)findViewById(R.id.imageView);
		
		//Получаем ссылку на DownloadManager сервис
		downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        
		//Создаем новый запрос
		Request request = new Request(Uri.parse(IMAGE_URL));
        request.setTitle("Title"); //заголовок будущей нотификации
        request.setDescription("My description"); //описание будущей нотификации
        request.setMimeType("application/my-mime"); //mine type загружаемого файла
        
        //Установите следующий флаг, если хотите,
        //что-бы уведомление осталось по окончании загрузки
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        //Добавляем запрос в очередь
        downloadManager.enqueue(request);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		//Подписываемся на сообщения от сервиса
		registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
		registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_NOTIFICATION_CLICKED));
	};
	
	@Override
	protected void onPause() {
		super.onPause();
		//Отписываемся от сообщений сервиса
		unregisterReceiver(receiver);
	};
	
	BroadcastReceiver receiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			
			//Сообщение о том, что загрузка закончена
			if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)){
				long downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
				DownloadManager dm = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
				DownloadManager.Query query = new DownloadManager.Query();			
				query.setFilterById(downloadId);
				Cursor cursor = dm.query(query);
	            if (cursor.moveToFirst()){
            		int columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
                    if (DownloadManager.STATUS_SUCCESSFUL == cursor.getInt(columnIndex)) {                   	
                        String uriString = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                        imageView.setImageURI(Uri.parse(uriString));
                    }
	            }
	        
	        //Сообщение о клике по нотификации
			} else if (DownloadManager.ACTION_NOTIFICATION_CLICKED.equals(action)){
				DownloadManager dm = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
				//несколько параллельных загрузок могут быть объеденены в одну нотификацию,
				//по этому мы пытаемся получить список всех загрузок, связанных с 
				//выбранной нотификацией
				long[] ids = intent.getLongArrayExtra(DownloadManager.EXTRA_NOTIFICATION_CLICK_DOWNLOAD_IDS);
				DownloadManager.Query query = new DownloadManager.Query();
	            query.setFilterById(ids);
	            Cursor cursor = dm.query(query);
	            int idIndex = cursor.getColumnIndex(DownloadManager.COLUMN_ID);
	            if (cursor.moveToFirst()){
	            	do {
	            		//здесь вы можете получить id загрузки и
	            		//реализовать свое поведение
	            		long downloadId = cursor.getLong(idIndex);
	            		
	            	} while (cursor.moveToNext());
	            }
			}
		}	
	};
}