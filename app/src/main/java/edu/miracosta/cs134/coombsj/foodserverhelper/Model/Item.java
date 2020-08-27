package edu.miracosta.cs134.coombsj.foodserverhelper.Model;

import java.util.Date;
import java.util.Objects;

/**
 * The Item class represents a java object.
 * An Item is an item ordered from a unique table number,
 * and can either be a Food or Drink.
 * @author Jamison Coombs
 */
public class Item {
    private long mId;
    private int mTableNumber;
    private int mItemType;
    private String mItemName;
    private String mTimeOrdered;
    private boolean mIsDone;

    /**
     * Gets the unique Id of the current Item
     * @return Id
     */
    public long getId() {
        return mId;
    }

    /**
     * Sets the unique Id of the current Item
     * @param id
     */
    void setId(long id)
    {
        if (id < -1)
            throw new IllegalArgumentException();
        mId = id;
    }

    /**
     * Gets the table number associated with the item
     * (what table the item is for)
     * @return TableNumber
     */
    public int getTableNumber() {
        return mTableNumber;
    }

    /**
     * Sets the table number for the item
     * @param tableNumber
     */
    public void setTableNumber(int tableNumber) {
        mTableNumber = tableNumber;
    }

    /**
     * Gets the type of the current Item
     * (food = 0 / drink = 1)
     * @return Whether the item is a food or drink
     */
    public int getItemType() {
        return mItemType;
    }

    /**
     * Sets the type of item for the current Item
     * @param itemType
     */
    public void setItemType(int itemType) {
        mItemType = itemType;
    }

    /**
     * Gets a string representing the name of the item ordered
     * @return Name of the Item
     */
    public String getItemName() {
        return mItemName;
    }

    /**
     * Sets the name of the item ordered
     * @param itemName
     */
    public void setItemName(String itemName) {
        mItemName = itemName;
    }

    /**
     * Gets the time that the item was ordered in the form
     * of a string with the full date/time. This string can
     * later be formatted into a simple time format.
     * @return Date/Time of the order
     */
    public String getTimeOrdered() {
        return mTimeOrdered;
    }

    /**
     * Returns true if the item has been delivered to the table.
     * @return If the item is done or not
     */
    public boolean isDone() {
        return mIsDone;
    }

    /**
     * Sets the item to being done or not
     * @param done
     */
    public void setDone(boolean done) {
        mIsDone = done;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return mId == item.mId &&
                mTableNumber == item.mTableNumber &&
                mItemType == item.mItemType &&
                mIsDone == item.mIsDone &&
                mItemName.equals(item.mItemName) &&
                mTimeOrdered.equals(item.mTimeOrdered);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mId, mTableNumber, mItemType, mItemName, mTimeOrdered, mIsDone);
    }

    /**
     * Default Constructor for a new Item Object
     * @param tableNumber
     * @param itemType
     * @param itemName
     */
    public Item(int tableNumber, int itemType, String itemName) {
        mTableNumber = tableNumber;
        mItemType = itemType;
        mItemName = itemName;
        mTimeOrdered = String.valueOf(new Date());
        mIsDone = false;
    }

    /**
     * Constructer for an item that has already been instantiated in the app before.
     * This one is typically used when loading from the SQLite database.
     * @param id
     * @param tableNumber
     * @param itemType
     * @param itemName
     * @param timeOrdered
     * @param isDone
     */
    public Item(long id, int tableNumber, int itemType, String itemName, String timeOrdered, boolean isDone) {
        setId(id);
        mTableNumber = tableNumber;
        mItemType = itemType;
        mItemName = itemName;
        mTimeOrdered = timeOrdered;
        mIsDone = isDone;
    }
}
