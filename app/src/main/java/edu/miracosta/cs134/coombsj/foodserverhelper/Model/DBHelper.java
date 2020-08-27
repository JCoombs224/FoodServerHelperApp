package edu.miracosta.cs134.coombsj.foodserverhelper.Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * The DBHelper method allows the app to use and access a unique
 * SQLite database.
 * @author Jamison Coombs
 */
public class DBHelper extends SQLiteOpenHelper {

    private Context mContext;

    //TASK: DEFINE THE DATABASE VERSION AND NAME  (DATABASE CONTAINS MULTIPLE TABLES)
    public static final String DATABASE_NAME = "AllOrders";
    private static final int DATABASE_VERSION = 1;

    //TASK: DEFINE THE FIELDS (COLUMN NAMES) FOR THE TABLES TABLE
    private static final String TABLES_TABLE = "Tables";
    private static final String TABLES_KEY_FIELD_ID = "_id";
    private static final String FIELD_TABLE_NUMBER = "tableNumber";
    private static final String FIELD_NUMBER_GUESTS = "numberGuests";
    private static final String FIELD_SPECIAL_NOTES = "specialNotes";

    //TASK: DEFINE THE FIELDS (COLUMN NAMES) FOR THE ITEMS TABLE
    private static final String ITEMS_TABLE = "Items";
    private static final String ITEMS_KEY_FIELD_ID = "_id";
    private static final String FIELD_ITEM_TABLE_NUMBER = "itemTableNumber";
    private static final String FIELD_ITEM_TYPE = "itemType";
    private static final String FIELD_ITEM_NAME = "itemName";
    private static final String FIELD_ITEM_TIME_ORDERED = "itemTime";
    private static final String FIELD_ITEM_IS_DONE = "itemIsDone";

    public DBHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createQuery = "CREATE TABLE " + TABLES_TABLE + "("
                + TABLES_KEY_FIELD_ID + " INTEGER PRIMARY KEY, "
                + FIELD_TABLE_NUMBER + " INTEGER, "
                + FIELD_NUMBER_GUESTS + " INTEGER ,"
                + FIELD_SPECIAL_NOTES + " TEXT " + ")";
        db.execSQL(createQuery);

        createQuery = "CREATE TABLE " + ITEMS_TABLE + "("
                + ITEMS_KEY_FIELD_ID + " INTEGER PRIMARY KEY, "
                + FIELD_ITEM_TABLE_NUMBER + " INTEGER, "
                + FIELD_ITEM_TYPE + " INTEGER, "
                + FIELD_ITEM_NAME + " TEXT, "
                + FIELD_ITEM_TIME_ORDERED + " DATE, "
                + FIELD_ITEM_IS_DONE + " INTEGER" + ")";
        db.execSQL(createQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database,
                          int oldVersion,
                          int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS " + TABLES_TABLE);
        database.execSQL("DROP TABLE IF EXISTS " + ITEMS_TABLE);
        onCreate(database);
    }

    // Methods below are all for the Tables table in the db
    /**
     * Adds a new table to the database
     * @param table
     */
    public void addTable(Table table) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(FIELD_TABLE_NUMBER, table.getTableNumber());
        values.put(FIELD_NUMBER_GUESTS, table.getNumberGuests());
        values.put(FIELD_SPECIAL_NOTES, table.getSpecialNotes());

        long id = db.insert(TABLES_TABLE, null, values);

        // CLOSE THE DATABASE CONNECTION
        db.close();
    }

    /**
     * Gets all the tables in the database
     * @return List<Table>allTables
     */
    public List<Table> getAllTables() {
        List<Table> tablesList = new ArrayList<>();
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.query(
                TABLES_TABLE,
                new String[]{TABLES_KEY_FIELD_ID, FIELD_TABLE_NUMBER, FIELD_NUMBER_GUESTS, FIELD_SPECIAL_NOTES},
                null,
                null,
                null, null, null, null);

        //COLLECT EACH ROW IN THE TABLE
        if (cursor.moveToFirst()) {
            do {
                Table table =
                        new Table(cursor.getLong(0),
                                cursor.getInt(1),
                                cursor.getInt(2),
                                cursor.getString(3));
                tablesList.add(table);
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return tablesList;
    }

    /**
     * Deletes a table from the database
     * @param table
     * @return
     */
    public int deleteTable(Table table) {
        SQLiteDatabase db = this.getWritableDatabase();

        // DELETE THE TABLE ROW
        int rowsDeleted = db.delete(TABLES_TABLE, TABLES_KEY_FIELD_ID + " = ?",
                new String[]{String.valueOf(table.getId())});
        db.close();
        return rowsDeleted;
    }

    // Methods below are for the Items table in the db
    /**
     * Adds a new item to the database
     * @param item
     */
    public void addItem(Item item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(FIELD_ITEM_TABLE_NUMBER, item.getTableNumber());
        values.put(FIELD_ITEM_TYPE, item.getItemType());
        values.put(FIELD_ITEM_NAME, item.getItemName());
        values.put(FIELD_ITEM_TIME_ORDERED, item.getTimeOrdered());
        values.put(FIELD_ITEM_IS_DONE, item.isDone());

        long id = db.insert(ITEMS_TABLE, null, values);

        // CLOSE THE DATABASE CONNECTION
        db.close();
    }

    /**
     * Gets all the tables in the database
     * @return List<Table>allTables
     */
    public List<Item> getAllItems() {
        List<Item> itemsList = new ArrayList<>();
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.query(
                ITEMS_TABLE,
                new String[]{ITEMS_KEY_FIELD_ID, FIELD_ITEM_TABLE_NUMBER, FIELD_ITEM_TYPE, FIELD_ITEM_NAME, FIELD_ITEM_TIME_ORDERED, FIELD_ITEM_IS_DONE},
                null,
                null,
                null, null, null, null);

        //COLLECT EACH ROW IN THE TABLE
        if (cursor.moveToFirst()) {
            do {
                Item item =
                        new Item(cursor.getLong(0),
                                cursor.getInt(1),
                                cursor.getInt(2),
                                cursor.getString(3),
                                cursor.getString(4),
                                cursor.getInt(5) > 0);
                itemsList.add(item);
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return itemsList;
    }

    /**
     * Deletes a table from the database
     * @param item
     * @return
     */
    public int deleteItem(Item item) {
        SQLiteDatabase db = this.getWritableDatabase();

        // DELETE THE TABLE ROW
        int rowsDeleted = db.delete(ITEMS_TABLE, ITEMS_KEY_FIELD_ID + " = ?",
                new String[]{String.valueOf(item.getId())});
        db.close();
        return rowsDeleted;
    }

    public void refreshDatabase()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + ITEMS_TABLE);
        String createQuery = "CREATE TABLE " + ITEMS_TABLE + "("
                + ITEMS_KEY_FIELD_ID + " INTEGER PRIMARY KEY, "
                + FIELD_ITEM_TABLE_NUMBER + " INTEGER, "
                + FIELD_ITEM_TYPE + " INTEGER, "
                + FIELD_ITEM_NAME + " TEXT, "
                + FIELD_ITEM_TIME_ORDERED + " DATE, "
                + FIELD_ITEM_IS_DONE + " INTEGER" + ")";
        db.execSQL(createQuery);

        db.execSQL("DROP TABLE IF EXISTS " + TABLES_TABLE);
        createQuery = "CREATE TABLE " + TABLES_TABLE + "("
                + TABLES_KEY_FIELD_ID + " INTEGER PRIMARY KEY, "
                + FIELD_TABLE_NUMBER + " INTEGER, "
                + FIELD_NUMBER_GUESTS + " INTEGER ,"
                + FIELD_SPECIAL_NOTES + " TEXT " + ")";
        db.execSQL(createQuery);
    }





}
