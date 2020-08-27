package edu.miracosta.cs134.coombsj.foodserverhelper;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import edu.miracosta.cs134.coombsj.foodserverhelper.Model.DBHelper;
import edu.miracosta.cs134.coombsj.foodserverhelper.Model.Item;
import edu.miracosta.cs134.coombsj.foodserverhelper.Model.Table;

/**
 * The TableDetails class is the controller for the activity.
 * This activity displays all the information about the selected table, and shows
 * a listView of all the Items that are associated with that table. Allows the
 * user to either add more items to the table, or close the table from the
 * system.
 * @author Jamison Coombs
 */
public class TableDetails extends AppCompatActivity implements AddItemDialog.AddItemDialogListener {

    // Private instance variables
    private TextView detailsTableNumberTextView;
    private TextView detailsNumberGuestsTextView;
    private TextView detailsSpecialNotesTextView;

    private ListView detailsItemListView;
    private ItemsListAdapter itemListAdapter;

    private DBHelper mDB;
    private List<Item> allItemsList;
    private List<Item> filteredItemList;

    private int tableNumberGlobal;

    /**
     * Runs when the activity is created.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table_details);

        // Wire up views from layout
        detailsTableNumberTextView = findViewById(R.id.detailsTableNumberTextView);
        detailsNumberGuestsTextView = findViewById(R.id.detailsNumberGuestsTextView);
        detailsSpecialNotesTextView = findViewById(R.id.detailsSpecialNotesTextView);

        detailsItemListView = findViewById(R.id.tableDetailsListView);

        // Link database to the all items list
        mDB = new DBHelper(this);
        allItemsList = mDB.getAllItems();

        // Get parameters form intent
        int tableNumber = getIntent().getExtras().getInt("TableNumber");
        int numberGuests = getIntent().getExtras().getInt("NumberGuests");
        String specialNotes = getIntent().getExtras().getString("SpecialNotes");

        tableNumberGlobal = tableNumber;


        filteredItemList = new ArrayList<>();

        // Set the filtered list
        // First filter through all the items and find which items correspond to the table
        for (int i = 0; i < allItemsList.size(); i++) {
            if (allItemsList.get(i).getTableNumber() == tableNumber)
                filteredItemList.add(allItemsList.get(i));
        }

        // Second filter sets the items that are marked as done to the bottom of the list
        filterDoneItems();

        // Set the text boxes
        detailsTableNumberTextView.setText("Table #" + tableNumber);
        detailsNumberGuestsTextView.setText(numberGuests + " Guests");
        detailsSpecialNotesTextView.setText(specialNotes);

        itemListAdapter = new ItemsListAdapter(this, R.layout.item_list_item, filteredItemList);
        detailsItemListView.setAdapter(itemListAdapter);

    }

    /**
     * Starts a new add itemItemDialog for an item to be added to the table.
     * @param v
     */
    public void addItem(View v)
    {
        int[] tableNums = {tableNumberGlobal};

        AddItemDialog addItemDialog = new AddItemDialog().getTables(tableNums);
        addItemDialog.show(getSupportFragmentManager(), "Add Item Dialog");
    }

    /**
     * Retrieves the attached item from the addItemDialog and adds that Item
     * to the list and DB.
     * @param tableNumber
     * @param itemType
     * @param itemName
     */
    @Override
    public void attachItem(int tableNumber, int itemType, String itemName) {
        Item newItem = new Item(tableNumber, itemType, itemName);
        filteredItemList.add(newItem);
        filterDoneItems();
        itemListAdapter.notifyDataSetChanged();
        mDB.addItem(newItem);
    }

    /**
     * This method starts when the user presses on one of the items on the list
     * and marks that item as done, sending it to the bottom of the list.
     * @param v
     */
    public void itemDone(View v)
    {
        Item selectedItem = (Item) v.getTag();
        filteredItemList.remove(selectedItem);
        mDB.deleteItem(selectedItem);
        selectedItem.setDone(true);
        filteredItemList.add(selectedItem);
        mDB.addItem(selectedItem);
        itemListAdapter.notifyDataSetChanged();
    }

    /**
     * Filters the items that have been done to the bottom of the list
     */
    public void filterDoneItems()
    {
        Collections.sort(filteredItemList, new Comparator<Item>() {
            @Override
            public int compare(Item abc1, Item abc2) {
                return Boolean.compare(!abc2.isDone(),!abc1.isDone());
            }
        });
    }

    /**
     * This method is ran when the user closes the table, deleting all the associated
     * items from the database with the table, and tells the MainActivity to delete
     * this table.
     * @param v
     */
    public void closeTable(View v)
    {
        // Remove all the items assigned to the table from the item database
        for (int i = 0; i < filteredItemList.size(); i++)
            mDB.deleteItem(filteredItemList.get(i));

        // Remove the table from the table database
        List<Table> tmpTableList = mDB.getAllTables();
        for (int i = 0; i < tmpTableList.size(); i++)
            if (tmpTableList.get(i).getTableNumber() == tableNumberGlobal)
                mDB.deleteTable(tmpTableList.get(i));

        final Intent intent = new Intent();
        intent.putExtra("DeletedTable",tableNumberGlobal);
        setResult(Activity.RESULT_OK, intent);

        finish();
    }

    /**
     * Cancels deleting the table from DB.
     */
    @Override
    public void onBackPressed() {
        setResult(Activity.RESULT_CANCELED);
        super.onBackPressed();
    }
}
