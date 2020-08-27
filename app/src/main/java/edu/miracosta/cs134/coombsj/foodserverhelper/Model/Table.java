package edu.miracosta.cs134.coombsj.foodserverhelper.Model;

/**
 * The Table class represents a table at a restaurant as a java object.
 * A table stores little information.
 * Only the number of the Table, the number of guests seated at the table
 * and any special notes the customer adds.
 * The table number is later associated with a matching table number field from
 * an Item object.
 * @author Jamison Coombs
 */
public class Table {
    private long mId;
    private int mTableNumber;
    private int mNumberGuests;
    private String mSpecialNotes;
    public int getTableNumber() {
        return mTableNumber;
    }

    public long getId() {
        return mId;
    }

    void setId(long id)
    {
        if (id < -1)
            throw new IllegalArgumentException();
        mId = id;
    }

    public int getNumberGuests() {
        return mNumberGuests;
    }

    public void setNumberGuests(int numberGuests) {
        mNumberGuests = numberGuests;
    }

    public String getSpecialNotes() {
        return mSpecialNotes;
    }

    public void setSpecialNotes(String specialNotes) {
        mSpecialNotes = specialNotes;
    }

    public Table(int tableNumber, int numberGuests, String specialNotes) {
        mTableNumber = tableNumber;
        mNumberGuests = numberGuests;
        mSpecialNotes = specialNotes;
    }

    public Table(long id, int tableNumber, int numberGuests, String specialNotes) {
        setId(id);
        mTableNumber = tableNumber;
        mNumberGuests = numberGuests;
        mSpecialNotes = specialNotes;
    }
}
