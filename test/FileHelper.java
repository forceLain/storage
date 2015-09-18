public class FileHelper {

    private Hashtable<String, byte[]> cache = new Hashtable<>();

    public byte[] get(String name){
        byte[] content = cache.get(name);
        if (content == null){
            File file = new File("/sdcard/my_application/files/" + name);
            byte[] bFile = new byte[(int) file.length()];
            FileInputStream fileInputStream = null;
            try {
                fileInputStream = new FileInputStream(file);
                fileInputStream.read(bFile);
                fileInputStream.close();
            }catch(Exception e){
                e.printStackTrace();
            }
            cache.put(name, bFile);
            return bFile;
        } else {
            return content;
        }
    }

    public void getAsync(final String name, final Callback callback){
        Thread thread = new Thread(){
            @Override
            public void run() {
                byte[] bytes = get(name);
                callback.onFileLoaded(bytes);
            }
        };
        thread.start();
    }

    public interface Callback {
        void onFileLoaded(byte[] content);
    }
}
