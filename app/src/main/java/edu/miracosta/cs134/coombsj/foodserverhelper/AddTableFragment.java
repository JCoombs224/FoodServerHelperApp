package edu.miracosta.cs134.coombsj.foodserverhelper;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * The AddTableFragment is a DialogFragment that allows the user to add a new table into the system.
 * The dialog asks for the user to enter a table number, the number of guests at the table and any
 * special notes that the customer has.
 * @author Jamison Coombs
 */
public class AddTableFragment extends AppCompatDialogFragment {
    private EditText tableNumberEditText;
    private EditText numberOfGuestsEditText;
    private EditText specialNotesEditText;
    private AddTableDialogListener listener;

    /**
     * This method fires when the dialog is created.
     * @param savedInstanceState
     * @return
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.add_table_dialog, null);

        builder.setView(view)
                .setTitle("Add Table") // Sets the title for the dialog
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Runs when the cancel button is pressed
                    }
                })
                .setPositiveButton("add table", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) { // Runs when the Add Table button is pressed
                        // Check to ensure all required fields are entered
                        if (tableNumberEditText.getText().toString().isEmpty() || numberOfGuestsEditText.getText().toString().isEmpty())
                        {
                            Toast.makeText(getContext(), "Table Number & Number of guests are required fields", Toast.LENGTH_LONG).show();
                        }
                        else { // If all fields are entered attach the values to the listener
                            int tableNumber = Integer.parseInt(tableNumberEditText.getText().toString());
                            int numberGuests = Integer.parseInt(numberOfGuestsEditText.getText().toString());
                            String specialNotes = specialNotesEditText.getText().toString();
                            listener.attachTable(tableNumber, numberGuests, specialNotes);
                        }
                    }
                });

        // Wire up the views in the dialog
        tableNumberEditText = view.findViewById(R.id.tableNumberEditText);
        numberOfGuestsEditText = view.findViewById(R.id.numberOfGuestsEditText);
        specialNotesEditText = view.findViewById(R.id.notesEditText);

        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) { // Runs when the values are attached
        super.onAttach(context);

        try {
            listener = (AddTableDialogListener) context;
        } catch (Exception e) {
            Log.e("AddTableFragment", "Error attaching" + e.getMessage());
        }
    }

    /**
     * Attaches the values entered to the fragment
     * and ends the activity.
     */
    public interface AddTableDialogListener
    {
        void attachTable(int tableNumber, int itemType, String itemName);
    }

}
