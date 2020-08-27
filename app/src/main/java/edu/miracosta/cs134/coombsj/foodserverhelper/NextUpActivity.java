package edu.miracosta.cs134.coombsj.foodserverhelper;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import edu.miracosta.cs134.coombsj.foodserverhelper.Model.DBHelper;
import edu.miracosta.cs134.coombsj.foodserverhelper.Model.Item;

/**
 * The NextUpActivity displays a list view of all the Items ordered in the system.
 * It decends in order from oldest to newest ordered and displays information about the
 * item.
 * @author Jamison Coombs
 */
public class NextUpActivity extends AppCompatActivity {

    private ListView nextUpListView;
    private DBHelper mDB;
    private List<Item> allItemsList;
    private ItemsNextUpListAdapter nextUpListAdapter;

    /**
     * Starts when the activity is created.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next_up);

        nextUpListView = findViewById(R.id.nextUpListView);
        mDB = new DBHelper(this);
        allItemsList = mDB.getAllItems();

        filterDoneItems();

        nextUpListAdapter = new ItemsNextUpListAdapter(this, R.layout.next_up_list_item, allItemsList);
        nextUpListView.setAdapter(nextUpListAdapter);
    }

    /**
     * This method starts when the user presses on one of the items on the list
     * and marks that item as done, sending it to the bottom of the list.
     * @param v
     */
    public void itemDone(View v)
    {
        Item selectedItem = (Item) v.getTag();
        allItemsList.remove(selectedItem);
        mDB.deleteItem(selectedItem);
        selectedItem.setDone(true);
        allItemsList.add(selectedItem);
        mDB.addItem(selectedItem);
        nextUpListAdapter.notifyDataSetChanged();
    }

    /**
     * This is the method filters the items that have been done already to the bottom
     * of the list.
     */
    public void filterDoneItems()
    {
        Collections.sort(allItemsList, new Comparator<Item>() {
            @Override
            public int compare(Item abc1, Item abc2) {
                return Boolean.compare(!abc2.isDone(),!abc1.isDone());
            }
        });
    }
}
