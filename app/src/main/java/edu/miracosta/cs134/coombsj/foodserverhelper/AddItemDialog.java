package edu.miracosta.cs134.coombsj.foodserverhelper;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * The AddItemDialog class is a controller for an AppDialogFragment.
 * This dialog allows the user to add a new Item object into the system.
 * This dialog asks the user to select the table number for the new item
 * in a spinner view. It then asks for the ItemType and the ItemName
 * @author Jamison Coombs
 */
public class AddItemDialog extends AppCompatDialogFragment {
    private Spinner tableNumberSpinner;
    private Spinner itemTypeSpinner;
    private EditText itemNameEditText;
    private AddItemDialogListener listener;

    /**
     * This method gets all the table numbers from the previous activity
     * @param tableNums
     * @return Array of table numbers
     */
    public static AddItemDialog getTables(int[] tableNums) {
        AddItemDialog getTables = new AddItemDialog();

        Bundle bundle = new Bundle();
        bundle.putIntArray("TableNums", tableNums);
        getTables.setArguments(bundle);

        return getTables;
    }

    /**
     * This method runs when the Activity is created.
     * @param savedInstanceState
     * @return
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.add_item_dialog, null);

        // View wire ups
        tableNumberSpinner = view.findViewById(R.id.tableNumberSpinner);
        itemTypeSpinner = view.findViewById(R.id.itemTypeSpinner);
        itemNameEditText = view.findViewById(R.id.itemNameEditText);

        // Sets the item types in the itemTypeSpinner
        String[] itemTypes = new String[] {"Food", "Drink"};
        ArrayAdapter<String> itemTypeAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, itemTypes);
        itemTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        itemTypeSpinner.setAdapter(itemTypeAdapter);

        // Retrieves all the open table numbers, and adds them to an array
        int[] tableNums = getArguments().getIntArray("TableNums");
        ArrayList<Integer> tableNumsArray = new ArrayList<>();
        for (int i = 0; i < tableNums.length; i++) {
            tableNumsArray.add(tableNums[i]);
        }

        // That array is then used to load up the selections in the tableNumberSpinner
        ArrayAdapter<Integer> tableNumsAdapter = new ArrayAdapter<Integer>(getContext(), android.R.layout.simple_spinner_item, tableNumsArray);
        tableNumsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tableNumberSpinner.setAdapter(tableNumsAdapter);

        // Disables the tableNumberSpinner if there is only one table in the array.
        // NOTE: If the dialog is started from within a TableDetails activity then
        //       only the table it was started from will show.
        if (tableNumsArray.size() == 1)
            tableNumberSpinner.setEnabled(false);

            builder.setView(view)
                .setTitle("Add Item") // Sets the title for the dialog box
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() { // Fires when the cancel button is clicked
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Nothing happens and we return back to the activity
                    }
                })
                .setPositiveButton("add item", new DialogInterface.OnClickListener() { // Fires when the add item button is clicked
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (itemNameEditText.getText().toString().isEmpty()) // Check to see if the item name field is empty
                        {
                            Toast.makeText(getContext(), "Fill out required fields", Toast.LENGTH_LONG).show(); // If empty display toast and return
                        }
                        else {  // If not empty retrieve the information from the views and store as variable
                            int tableNumber = Integer.parseInt(tableNumberSpinner.getSelectedItem().toString());
                            int itemType = itemTypeSpinner.getSelectedItemPosition();
                            String itemName = itemNameEditText.getText().toString();
                            listener.attachItem(tableNumber, itemType, itemName); // Attach the item to the listener and send back to the previous activity
                        }
                    }
                });


        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) { // Runs when the item is attached
        super.onAttach(context);

        try {
            listener = (AddItemDialogListener) context;
        } catch (Exception e) {
            Log.e("AddTableFragment", "Error attaching" + e.getMessage());
        }
    }

    public interface AddItemDialogListener
    {
        void attachItem(int tableNumber, int numberGuests, String specialNotes); // Attaches Item
    }

}
