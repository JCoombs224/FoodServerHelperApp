package edu.miracosta.cs134.coombsj.foodserverhelper;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import java.util.List;

import edu.miracosta.cs134.coombsj.foodserverhelper.Model.DBHelper;
import edu.miracosta.cs134.coombsj.foodserverhelper.Model.Item;
import edu.miracosta.cs134.coombsj.foodserverhelper.Model.Table;

/**
 * The MainActivity class is a controller for the main launcher activity.
 * The MainActivity shows all the current active tables in the system in a
 * listView. The desired table can be selected in the list to show more details
 * such as all the items needed for that table. This activity allows users to add
 * new tables, new items, and go to the NextUpActivity.
 * @author Jamison Coombs
 */
public class MainActivity extends AppCompatActivity implements AddTableFragment.AddTableDialogListener, AddItemDialog.AddItemDialogListener {

    // Private instance variables
    private static final String TAG = "Food Server Helper";

    private List<Table> allTablesList;
    private DBHelper mDB;

    private ListView tableListView;
    private TableListAdapter tableListAdapter;

    private static final int TABLE_DELETED_REQUEST_CODE = 10101;

    /**
     * Runs when the activity is started.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDB = new DBHelper(this);
        //mDB.refreshDatabase();
        allTablesList = mDB.getAllTables();

        // Wire up views
        tableListView = findViewById(R.id.allTablesListView);
        // Instantiate list adapter
        tableListAdapter = new TableListAdapter(this, R.layout.table_list_item, allTablesList);
        tableListView.setAdapter(tableListAdapter);

    }

    /**
     * This method is fired when the add table button is pressed on the view.
     * It creates a new add table fragment.
     * @param v
     */
    public void addTable(View v)
    {
        AddTableFragment addTableFragment = new AddTableFragment();
        addTableFragment.show(getSupportFragmentManager(), "Add Table Dialog");
    }

    /**
     * This method is fired when the add item button is pressed on the view.
     * It creates a new add item fragment
     * @param v
     */
    public void addItem(View v)
    {
        int[] tableNums = new int[allTablesList.size()];

        for (int i = 0; i < allTablesList.size(); i++) {
            tableNums[i] = allTablesList.get(i).getTableNumber();
        }

        AddItemDialog addItemDialog = new AddItemDialog().getTables(tableNums);
        addItemDialog.show(getSupportFragmentManager(), "Add Item Dialog");
    }

    /**
     * This method is fired when a table in the listView is pressed.
     * It stores the table information and sends it and the view to a
     * TableDetails activity.
     * @param v
     */
    public void viewTableDetails(View v)
    {
        Table selectedTable = (Table) v.getTag();
        Intent detailsIntent = new Intent(this, TableDetails.class);
        detailsIntent.putExtra("TableNumber", selectedTable.getTableNumber());
        detailsIntent.putExtra("NumberGuests", selectedTable.getNumberGuests());
        detailsIntent.putExtra("SpecialNotes", selectedTable.getSpecialNotes());
        startActivityForResult(detailsIntent, TABLE_DELETED_REQUEST_CODE);
    }


    // Below are the methods that receive the information from the dialog boxes
    // and add the information needed to the lists

    /**
     * When a table is added from the add table fragment, this method is ran.
     * It retrieves the information from the fragment and stores it into the
     * a new table object.
     * @param tableNumber
     * @param numberGuests
     * @param specialNotes
     */
    @Override
    public void attachTable(int tableNumber, int numberGuests, String specialNotes) {
        Table newTable = new Table(tableNumber, numberGuests, specialNotes);
        mDB.addTable(newTable);
        allTablesList.add(newTable);
        tableListAdapter.notifyDataSetChanged();
    }


    /**
     * When an Item is added from the add item dialog, this method is run.
     * It retrieves the Item information and creates a new Item. It then stores
     * that item into the DB.
     * @param tableNumber
     * @param itemType
     * @param itemName
     */
    @Override
    public void attachItem(int tableNumber, int itemType, String itemName) {
        Item newItem = new Item(tableNumber, itemType, itemName);
        mDB.addItem(newItem);
    }

    /**
     * This method is ran when a table is closed from the TableDetails activity.
     * It retrieves what table was closed and removes it from the list as well as
     * the DB.
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TABLE_DELETED_REQUEST_CODE) // Checks to see if the request code is from the table details activity
            if (resultCode == Activity.RESULT_OK) { // If the result was ok (the table was closed) then this code will run
                {
                    for (int i = 0; i < allTablesList.size(); i++) {
                        if (allTablesList.get(i).getTableNumber() == data.getIntExtra("DeletedTable", -1))
                            allTablesList.remove(allTablesList.get(i));
                    }
                }
                tableListAdapter.notifyDataSetChanged();
            }
    }

    /**
     * This method is fired when the nextup button is pressed and starts
     * that activity.
     * @param v
     */
    public void viewNextUpScreen(View v)
    {
        Intent intent = new Intent(this, NextUpActivity.class);
        startActivity(intent);
    }
}
