public class DownloadService extends IntentService {
	
	public DownloadService() {
		super("DownloadService");
	}

	public static final String CHANNEL = DownloadService.class.getSimpleName()+".broadcast";

	private void sendResult() {
		Intent intent = new Intent(CHANNEL);
	    sendBroadcast(intent);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	//Этот метод вызвается автоматически и в отдельном потоке
	@Override
	protected void onHandleIntent(Intent intent) {
		//фоновая операция
		
		//отправка сообщения о завершении операции
		sendResult();
	}
}