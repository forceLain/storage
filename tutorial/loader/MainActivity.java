public class MainActivity extends ListActivity implements LoaderCallbacks<Cursor> {
	
	//поля из базы данных контактов
	static final String[] CONTACTS_SUMMARY_PROJECTION = new String[] {
        Contacts._ID,
        Contacts.DISPLAY_NAME,
        Contacts.CONTACT_STATUS,
    };
	
	private static final int LOADER_ID = 1;	
	private SimpleCursorAdapter adapter;
	TextView textview;	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//Текст, видимый во время загрузки контактов
		textview = (TextView)findViewById(R.id.loading);
		
		//Скрываем список контактов, пока они не загрузятся
		getListView().setVisibility(View.GONE);
			    
		//Адаптер для ListView
	    adapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_2, null,
                new String[] { Contacts.DISPLAY_NAME, Contacts.CONTACT_STATUS },
                new int[] { android.R.id.text1, android.R.id.text2 }, 0);
	    
	    setListAdapter(adapter);
	    
	    //Инициализация Loader'а
	    //передаем мэнеджеру id Loader'а и callback
	    LoaderManager lm = getLoaderManager();
	    lm.initLoader(LOADER_ID, null, this);
	}

	//Здесь мы должны сконструировать Loader, который будет
	//использоваться для обращения к БД контактов
	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		Uri baseUri = Contacts.CONTENT_URI;

        String select = "((" + Contacts.DISPLAY_NAME + " NOTNULL) AND ("
                + Contacts.HAS_PHONE_NUMBER + "=1) AND ("
                + Contacts.DISPLAY_NAME + " != '' ))";
        return new CursorLoader(this, baseUri,
                CONTACTS_SUMMARY_PROJECTION, select, null,
                Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC");
	}

	//Метод будет вызван, когда загрузка будет завершена
	//Используем готовый курсор, что бы отобразить список контактов
	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		 switch (loader.getId()) {
	      case LOADER_ID:
	    	adapter.swapCursor(cursor);
	    	textview.setVisibility(View.GONE);
	    	getListView().setVisibility(View.VISIBLE);
	        break;
	    }
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		adapter.swapCursor(null);
	}	
}