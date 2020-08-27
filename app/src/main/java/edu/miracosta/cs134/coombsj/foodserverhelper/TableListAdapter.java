package edu.miracosta.cs134.coombsj.foodserverhelper;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import edu.miracosta.cs134.coombsj.foodserverhelper.Model.Table;

/**
 * The TableListAdapter is a custom adapter to display all the tables in a listView
 * in the MainActivity.
 * @author Jamison Coombs
 */
public class TableListAdapter extends ArrayAdapter<Table> {
    private Context mContext;
    private List<Table> mTablesList;
    private int mResourceId;



    /**
     * Creates a new <code>OfferingListAdapter</code> given a mContext, resource id and list of offerings.
     *
     * @param c The mContext for which the adapter is being used (typically an activity)
     * @param rId The resource id (typically the layout file name)
     * @param tables The list of offerings to display
     */
    public TableListAdapter(Context c, int rId, List<Table> tables) {
        super(c, rId, tables);
        mContext = c;
        mResourceId = rId;
        mTablesList = tables;

    }

    /**
     * Gets the view associated with the layout.
     * @param pos The position of the Offering selected in the list.
     * @param convertView The converted view.
     * @param parent The parent - ArrayAdapter
     * @return The new view with all content set.
     */
    @Override
    public View getView(int pos, View convertView, ViewGroup parent)
    {
        final Table selectedTable = mTablesList.get(pos);

        LayoutInflater inflater =
                (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(mResourceId, null);

        LinearLayout tabelListLinearLayout = view.findViewById(R.id.tableListLinearLayout);
        TextView tableNumberTextView = view.findViewById(R.id.tableListNumber);
        TextView numberGuestsTextView = view.findViewById(R.id.tableListNumberGuests);

        // DONE (2): Set the text of each of the 3 views accordingly.
        tableNumberTextView.setText("Table #" + String.valueOf(selectedTable.getTableNumber()));
        numberGuestsTextView.setText(String.valueOf(selectedTable.getNumberGuests()) + " guests");


        // Sets the tag, so when user clicks a specific offering, we can respond to that object specifically.
        tabelListLinearLayout.setTag(selectedTable);
        return view;
    }
}
