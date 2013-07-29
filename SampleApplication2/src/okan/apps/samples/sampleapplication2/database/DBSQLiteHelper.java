package okan.apps.samples.sampleapplication2.database;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;

import okan.apps.samples.sampleapplication2.utils.Constants;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

/**
 * SQLite Helper Class
 * 
 * @author Okan SAYILGAN
 */
public class DBSQLiteHelper extends SQLiteOpenHelper {
	
	private static final String APP_DATABASE_NAME = "app.db";
	private static final int DATABASE_VERSION = 1;
	
	private static final String DB_PATH = "/data/data/" + Constants.APPLICATION_PACKAGE_NAME + "/databases/";
	
	private static DBSQLiteHelper dbHelper;
	
	private Context mContext;
	
	/**
	 * @param context			Activity Context
	 * @return DBSQLiteHelper 	SingleTon Class
	 */
	public static DBSQLiteHelper getDBHelperInstance(Context context) {

		if (dbHelper != null) {
			return dbHelper;
		} else {
			dbHelper = new DBSQLiteHelper(context);
			return dbHelper;
		}
	}

	/**
	 * Constructor
	 */
	private DBSQLiteHelper(Context context) {

		super(context, APP_DATABASE_NAME, null, DATABASE_VERSION);

		this.mContext = context;
	}
	
	public static final String COLUMN_ID = "id";
	
	/********************************** COMPANY *********************************/

	/* TABLE COMPANY */
	public static final String TABLE_COMPANY = "company";
	public static final String COLUMN_COMPANY_NAME = "name";
	public static final String COLUMN_COMPANY_DESCRIPTION = "description";
	public static final String COLUMN_COMPANY_EMAIL = "email";
	
	/********************************** COMPANY MEDIA *********************************/
	
	/* TABLE COMPANY MEDIA */
	public static final String TABLE_COMPANY_MEDIA = "company_media";
	public static final String COLUMN_COMPANY_MEDIA_TYPE = "type";
	public static final String COLUMN_COMPANY_MEDIA_URL = "url";
	public static final String COLUMN_COMPANY_MEDIA_PARENT_ID = "parent_id";

	/********************************** STUDENT *********************************/

	/* TABLE STUDENT */
	public static final String TABLE_STUDENT = "student";
	public static final String COLUMN_STUDENT_NAME = "name";
	public static final String COLUMN_STUDENT_DEPARTMENT = "department";
	
	/********************************** FAVORITES *********************************/
	
	/* TABLE FAVORITES */
	public static final String TABLE_FAVORITES = "favorites";
	public static final String COLUMN_FAVORITES_DATE = "date";
	public static final String COLUMN_FAVORITES_REFERENCE_TABLE = "reference_table";
	public static final String COLUMN_FAVORITES_REFERENCE_OBJECT_ID = "reference_object_id";

	/***************************************************************************************************************/
	/******************************** TABLE CREATION SQLITE STATEMENTS **************************************/
	/***************************************************************************************************************/
	
	/* COMPANY MEDIA TABLE, Database creation SQL statement */
	private static final String COMPANY_MEDIA_TABLE_CREATE = "create table " + TABLE_COMPANY_MEDIA + "(" + 
			COLUMN_ID + " integer primary key autoincrement, " +
			COLUMN_COMPANY_MEDIA_TYPE + " text , " +
			COLUMN_COMPANY_MEDIA_URL + " text , " +
			COLUMN_COMPANY_MEDIA_PARENT_ID + " integer  REFERENCES " +
			TABLE_COMPANY + "(" + COLUMN_ID + ") ON DELETE CASCADE " + ");";
	
	/* COMPANY TABLE, Database creation SQL statement */
	private static final String COMPANY_TABLE_CREATE = "create table " + TABLE_COMPANY + "(" + 
			COLUMN_ID + " integer primary key autoincrement, " + 
			COLUMN_COMPANY_NAME + " text , " + 
			COLUMN_COMPANY_DESCRIPTION + " text , " +
			COLUMN_COMPANY_EMAIL + " text );";
	
	/* STUDENT TABLE, Database creation SQL statement */
	private static final String STUDENT_TABLE_CREATE = "create table " + TABLE_STUDENT + "(" + 
			COLUMN_ID + " integer primary key autoincrement, " + 
			COLUMN_STUDENT_NAME + " text , " + 
			COLUMN_STUDENT_DEPARTMENT + " text );";
	
	/* FAVORITES TABLE, Database creation SQL statement */
	private static final String FAVORITES_TABLE_CREATE = "create table " + TABLE_FAVORITES + "(" + 
			COLUMN_ID + " integer primary key autoincrement, " + 
			COLUMN_FAVORITES_DATE + " text , " +
			COLUMN_FAVORITES_REFERENCE_TABLE + " text , " + 
			COLUMN_FAVORITES_REFERENCE_OBJECT_ID + " integer );";
	
	@Override
	public void onCreate(SQLiteDatabase database) {
		
		database.execSQL(COMPANY_TABLE_CREATE);
		database.execSQL(COMPANY_MEDIA_TABLE_CREATE);
		database.execSQL(STUDENT_TABLE_CREATE);
		database.execSQL(FAVORITES_TABLE_CREATE);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(DBSQLiteHelper.class.getName(), "Upgrading database from version " + oldVersion
				+ " to " + newVersion + ", which will destroy all old data");
		
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMPANY);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMPANY_MEDIA);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_STUDENT);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITES);
		
		onCreate(db);
	}
	
	@Override
	public void onOpen(SQLiteDatabase db) {
		super.onOpen(db);
		if (!db.isReadOnly()) {
			/* Enable foreign key constraints */
			db.execSQL("PRAGMA foreign_keys=ON;");
		}
	}
	
	/**
	 * Creates a empty database on the system and rewrites it with your own
	 * database.
	 * */
	public void createDataBase() throws IOException {

		boolean srcExists = false;

		String[] files = mContext.getAssets().list("");

		for (String string : files) {
			if (string.equals(APP_DATABASE_NAME)) {
				srcExists = true;
			}
		}

		if (!srcExists)
			return;

		boolean dbExist = checkDataBase();

		if (dbExist) {

			// do nothing - database already exist
			Log.d("DB", "DB already Exist, no need to copy database...");

		} else {

			// By calling this method and empty database will be created into
			// the default system path
			// of your application so we are gonna be able to overwrite that
			// database with our database.
			this.getReadableDatabase();

			try {

				Log.d("DB", "DB doesn't Exist, copying the database...");
				copyDataBase();

			} catch (IOException e) {
				throw new Error("Error copying database");
			}
		}
	}
	
	/**
	 * Copies your database from your local assets-folder to the just created
	 * empty database in the system folder, from where it can be accessed and
	 * handled. This is done by transferring byte stream.
	 * */
	private void copyDataBase() throws IOException {

		// Open your local DB as the input stream
		InputStream myInput = mContext.getAssets().open(APP_DATABASE_NAME);

		// Path to the just created empty DB
		String outFileName = DB_PATH + APP_DATABASE_NAME;

		// Open the empty DB as the output stream
		OutputStream myOutput = new FileOutputStream(outFileName);

		// transfer bytes from the Input File to the Output File
		byte[] buffer = new byte[1024];
		int length;
		while ((length = myInput.read(buffer)) > 0) {
			myOutput.write(buffer, 0, length);
		}

		// Close the streams
		myOutput.flush();
		myOutput.close();
		myInput.close();
	}

	/**
	 * Check if the database already exist to avoid re-copying the file each
	 * time you open the application.
	 * 
	 * @return True if it exists, False otherwise.
	 */
	private boolean checkDataBase() {

		String myPath = DB_PATH + APP_DATABASE_NAME;
		File dbfile = new File(myPath);
		boolean checkDB = dbfile.exists();

		return checkDB;
	}
	
	/**
	 * Exports Application Database to the External File System.
	 */
	public void dumpSQL() {

		try {

			File sd = Environment.getExternalStorageDirectory();
			File data = Environment.getDataDirectory();
			if (sd.canWrite()) {
				String currentDBPath = "//data//" + Constants.APPLICATION_PACKAGE_NAME + "//databases//" + APP_DATABASE_NAME;
				String backupDBPath = "db_backup.db";
				File currentDB = new File(data, currentDBPath);
				File backupDB = new File(sd, backupDBPath);
				if (currentDB.exists()) {
					FileChannel src = new FileInputStream(currentDB).getChannel();
					FileChannel dst = new FileOutputStream(backupDB).getChannel();
					dst.transferFrom(src, 0, src.size());
					src.close();
					dst.close();
				}
			}
		} catch (Exception e) {
		}
	}
}