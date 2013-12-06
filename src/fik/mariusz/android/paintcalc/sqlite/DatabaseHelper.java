package fik.mariusz.android.paintcalc.sqlite;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import fik.mariusz.android.paintcalc.model.Room;

public class DatabaseHelper extends SQLiteOpenHelper {

	/* Logcat tag */
	private static final String TAG = "DatabaseHandler";

	private static DatabaseHelper instance = null;

	/** Database version */
	private static final int DATABASE_VERSION = 1;

	/** Database Name */
	private static final String DATABASE_NAME = "PaintCalc.db";

	/** Table name for rooms */
	private static final String TABLE_ROOM = "Room";

	/* Columns names for rooms table */
	private static final String KEY_ID = "Id";
	private static final String ID_OPTIONS = "INTEGER PRIMARY KEY AUTOINCREMENT";
	private static final String KEY_LENGTH = "Length";
	private static final String LENGTH_OPTIONS = "REAL NOT NULL";
	private static final String KEY_WIDTH = "Width";
	private static final String WIDTH_OPTIONS = "REAL NOT NULL";
	private static final String KEY_HEIGHT = "Height";
	private static final String HEIGHT_OPTIONS = "REAL NOT NULL";

	/* Array wit all columns used in db.query() */
	private static final String COLUMNS[] = { KEY_ID, KEY_LENGTH, KEY_WIDTH, KEY_HEIGHT };

	private static final String DATABASE_CREATE = "CREATE TABLE " + TABLE_ROOM + " (" + KEY_ID + " " + ID_OPTIONS
			+ ", " + KEY_LENGTH + " " + LENGTH_OPTIONS + ", " + KEY_WIDTH + " " + WIDTH_OPTIONS + ", " + KEY_HEIGHT
			+ " " + HEIGHT_OPTIONS + ");";
	private static final String DROP_TABLE_ROOM = "DROP TABLE IF EXISTS " + TABLE_ROOM;

	public static DatabaseHelper getInstance(Context context) {
		if (instance == null) {
			instance = new DatabaseHelper(context.getApplicationContext());
		}
		return instance;
	}

	private DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.d(TAG, "Database creating...");
		Log.d(TAG, "Table " + TABLE_ROOM + " ver." + DATABASE_VERSION + " created");
		// Create tables
		db.execSQL(DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO: Implement proper db upgrade
		Log.d(TAG, "Database upgrading...");
		Log.d(TAG, "Table " + TABLE_ROOM + " upgraded from ver." + oldVersion + " to ver." + newVersion);
		// Drop older table if existed
		db.execSQL(DROP_TABLE_ROOM);
		// Create tables again
		onCreate(db);

	}

	// Adding new room
	public void addRoom(Room room) {
		SQLiteDatabase db = getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_LENGTH, room.getLenght());
		values.put(KEY_WIDTH, room.getWidth());
		values.put(KEY_HEIGHT, room.getHeight());

		db.insert(TABLE_ROOM, null, values);
	}

	// Getting single room
	public Room getRoom(int id) {
		SQLiteDatabase db = getReadableDatabase();

		String where = KEY_ID + "=?" + id;
		Cursor cursor = db.query(TABLE_ROOM, COLUMNS, where, null, null, null, null);

		if (cursor != null)
			cursor.moveToFirst();

		final int i = cursor.getInt(0);
		final double l = cursor.getDouble(1);
		final double w = cursor.getDouble(2);
		final double h = cursor.getDouble(3);
		cursor.close();

		// return contact
		return new Room(i, l, w, h);
	}

	// Getting all rooms
	public List<Room> getAllRooms() {
		final List<Room> roomList = new ArrayList<Room>();

		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.query(TABLE_ROOM, COLUMNS, null, null, null, null, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Room room = new Room();
				room.setId(cursor.getInt(0));
				room.setLenght(cursor.getDouble(1));
				room.setWidth(cursor.getDouble(2));
				room.setHeight(cursor.getDouble(3));
				// Adding contact to list
				roomList.add(room);
			} while (cursor.moveToNext());
		}
		cursor.close();

		// return contact list
		return roomList;
	}

	// Getting rooms count
	public int getRoomsCount() {
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.query(TABLE_ROOM, COLUMNS, null, null, null, null, null);

		final int count = cursor.getCount();
		cursor.close();

		return count;
	}

	// Updating single room
	public int updateRoom(Room room) {
		final String whereArgs[] = { String.valueOf(room.getId()) };

		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_LENGTH, room.getLenght());
		values.put(KEY_WIDTH, room.getWidth());
		values.put(KEY_HEIGHT, room.getHeight());

		// updating row
		final int rowsAffected = db.update(TABLE_ROOM, values, KEY_ID + " = ?", whereArgs);

		return rowsAffected;
	}

	// Deleting single room
	public void deleteRoom(Room room) {
		final String whereArgs[] = { String.valueOf(room.getId()) };

		SQLiteDatabase db = getWritableDatabase();
		db.delete(TABLE_ROOM, KEY_ID + " = ?", whereArgs);
	}

	// Deleting the most recent room (with highest id?)
	public void deleteLatestRoom() {
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.query(TABLE_ROOM, COLUMNS, null, null, null, null, "ID DESC LIMIT 1");

		if (cursor != null)
			cursor.moveToFirst();

		final int i = cursor.getInt(0);
		cursor.close();
		final String whereArgs[] = { String.valueOf(i) };
		// remove roome with latest id
		db.delete(TABLE_ROOM, KEY_ID + "=?", whereArgs);
	}

	// Deleting all records in Rooms table
	public int deleteAllRooms() {
		SQLiteDatabase db = getWritableDatabase();
		final int rowsDeleted = db.delete(TABLE_ROOM, null, null);
		return rowsDeleted;
	}

}
