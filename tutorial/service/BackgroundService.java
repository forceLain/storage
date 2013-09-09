public class BackgroundService extends Service {
    
    public static final String CHANNEL = BackgroundService.class.getSimpleName()+".broadcast";
    
    //Этот метод будет вызван всякий раз,
    //когда сервису будет передан новый Intent
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //здесь вы можете запустить новый поток или задачу
        
        sendResult();
        return Service.START_NOT_STICKY;
    }

    //После завершения работы информируйте об этом,
    //разослав Broadcast
    private void sendResult() {
        Intent intent = new Intent(CHANNEL);
        sendBroadcast(intent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}