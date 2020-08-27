package edu.miracosta.cs134.coombsj.foodserverhelper;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import edu.miracosta.cs134.coombsj.foodserverhelper.Model.Item;

/**
 * The ItemsListAdapter class is a custom list adapter for the TableDetails activity.
 * It sets the information for each list item.
 */
public class ItemsListAdapter extends ArrayAdapter<Item> {
    private Context mContext;
    private List<Item> mItemList;
    private int mResourceId;

    /**
     * Creates a new <code>OfferingListAdapter</code> given a mContext, resource id and list of offerings.
     *
     * @param c The mContext for which the adapter is being used (typically an activity)
     * @param rId The resource id (typically the layout file name)
     * @param items The list of offerings to display
     */
    public ItemsListAdapter(Context c, int rId, List<Item> items) {
        super(c, rId, items);
        mContext = c;
        mResourceId = rId;
        mItemList = items;

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
        final Item selectedItem = mItemList.get(pos);

        LayoutInflater inflater =
                (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(mResourceId, null);

        // Wire up the views
        LinearLayout itemListLinearLayout = view.findViewById(R.id.itemListLinearLayout);
        ImageView itemListImageView = view.findViewById(R.id.itemListImageView);
        TextView itemListNameTextView = view.findViewById(R.id.itemListItemNameTextView);
        TextView itemListItemTypeTextView = view.findViewById(R.id.itemListItemTypeTextView);
        TextView itemListTimeOrdered = view.findViewById(R.id.itemListTimeOrdered);
        TextView itemListItemIsDoneTextView = view.findViewById(R.id.itemListIsDoneTextView);

        Resources res = getContext().getResources();
        int resID = R.drawable.drink;

        String uriString = ContentResolver.SCHEME_ANDROID_RESOURCE +
                "://" + res.getResourcePackageName(resID) +
                "/" + res.getResourceTypeName(resID) +
                "/" + res.getResourceEntryName(resID);

        Uri drinkImage = Uri.parse(uriString);

        String itemType = "Food";

        // Get the time ordered from the Item and format it in proper form
        SimpleDateFormat formatter = new SimpleDateFormat("hh:mm aa");
        Date d = new Date(selectedItem.getTimeOrdered());
        String formattedTime = formatter.format(d);

        // Set the image view to the corresponding item type
        if (selectedItem.getItemType() == 1) {
            itemListImageView.setImageURI(drinkImage);
            itemType = "Drink";
        }

        // Set the TextViews
        itemListNameTextView.setText(selectedItem.getItemName());
        itemListItemTypeTextView.setText(itemType);
        itemListTimeOrdered.setText("Time Ordered: " + formattedTime);


        // Special instructions for if the item is done
        if (selectedItem.isDone())
        {
            itemListItemIsDoneTextView.setVisibility(View.VISIBLE);
            itemListLinearLayout.setBackgroundColor(getContext().getResources().getColor(R.color.colorDeliveredItem));
            itemListLinearLayout.setClickable(false);
        }

        // Sets the tag, so when user clicks a specific offering, we can respond to that object specifically.
        itemListLinearLayout.setTag(selectedItem);
        return view;
    }
}
