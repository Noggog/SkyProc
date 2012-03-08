/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package skyproc;

import java.util.Iterator;
import skyproc.exceptions.BadParameter;
import lev.LShrinkArray;
import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeSet;
import java.util.zip.DataFormatException;
import lev.LExporter;
import lev.Ln;
import skyproc.exceptions.BadRecord;

/**
 * A specialized collection of subrecords.  Think of it as a special SkyProc
 * ArrayList used for subrecords.
 * @param <T>  The type of subrecord the group contains.
 * @author Justin Swanson
 */
class SubList<T extends SubRecord> extends SubRecord implements Iterable<T> {

    ArrayList<T> collection = new ArrayList<T>();
    T prototype;
    Type counterType = Type.NULL;
    boolean allowDups = true;
    private int counterLength = 4;

    SubList(T prototype_) {
        super(prototype_.type);
        prototype = prototype_;
    }

    SubList(Type counter, int counterLength, T prototype_) {
        this(prototype_);
        counterType = counter;
	this.counterLength = counterLength;
        type = new Type[prototype.type.length + 1];
        System.arraycopy(prototype.type, 0, type, 0, prototype.type.length);
        type[prototype_.type.length] = counterType;
    }

    @Override
    int getHeaderLength() {
        return 0;
    }

    @Override
     Boolean isValid() {
        return !collection.isEmpty();
    }

    /**
     *
     * @param s Record to check for containment.
     * @return True if an equal() record exists within the SubRecordList.
     */
    public boolean contains(T s) {
        return collection.contains(s);
    }

    /**
     *
     * @param i Index of the item to retrieve.
     * @return The ith item.
     */
    public T get(int i) {
        return collection.get(i);
    }

    /**
     * Adds an item to the list.  Some groups allow duplicate items, some do not,
     * depending on the internal specifications and context.  This function returns
     * true if the item was successfully added to list, or false if an equal one
     * already existed, and the group did not allow duplicates.
     * @param item Item to add to the list.
     * @return true if the item was added to the list. False if an equal item
     * already existed in the list, and duplicates were not allowed.
     */
    public boolean add(T item) {
        if (allowDups || !collection.contains(item)) {
            collection.add(item);
            return true;
        } else {
            return false;
        }
    }

    void allowDuplicates(boolean on) {
        allowDups = on;
    }

    /**
     *
     * @return True if the SubRecordList allows duplicate records.
     */
    public boolean allowsDuplicates () {
	return allowDups;
    }

    /**
     *
     * @param item Item to remove from the list.
     * @return True if an item was removed.
     */
    public boolean remove(T item) {
        if (collection.contains(item)) {
            collection.remove(item);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Removes an item based on its index in the list.
     * @param i Index of the item to remove.
     */
    public void remove(int i) {
        collection.remove(i);
    }

    /**
     *
     * @return The number of items currently in the list.
     */
    public int size() {
        return collection.size();
    }

    @Override
    public void clear() {
        collection.clear();
    }

    /**
     *
     * @return True if list is empty, and size == 0.
     */
    public boolean isEmpty() {
        return collection.isEmpty();
    }

    /**
     * This function will replace all records in the SubRecordList with the ones
     * given.<br><br>
     * WARNING: All existing records will be lost.
     * @param in ArrayList of records to replace the current ones.
     */
    public void setRecordsTo(ArrayList<T> in) {
        collection = in;
    }

    /**
     * This function will add all records given to the list using add().
     * @param in ArrayList of records to add in.
     */
    public void addRecordsTo(ArrayList<T> in) {
	for (T t : collection) {
	    collection.add(t);
	}
    }

    @Override
    void parseData(LShrinkArray in) throws BadRecord, DataFormatException, BadParameter {
        Type t = getNextType(in);
        if (counterType != t) {
            if (t.equals(type[0])) {
                T newRecord = (T) prototype.getNew(type[0]);
                newRecord.parseData(in);
                add(newRecord);
            } else {
                get(size() - 1).parseData(in);
            }
        }
    }

    @Override
    SubRecord getNew(Type type) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    int getContentLength(Mod srcMod) {
        int length = 0;
        if (counterType != Type.NULL) {
            length += counterLength + 6;
        }
        for (SubRecord r : collection) {
            length += r.getTotalLength(srcMod);
        }
        return length;
    }

    @Override
    void export(LExporter out, Mod srcMod) throws IOException {
        if (isValid()) {
            if (counterType != Type.NULL) {
                SubData counter = new SubData(counterType, Ln.toByteArray(collection.size(), counterLength));
                counter.export(out, srcMod);
            }
            for (T entry : collection) {
                entry.export(out, srcMod);
            }
        }
    }

    @Override
    void standardizeMasters(Mod srcMod) {
        for (T item : collection) {
            item.standardizeMasters(srcMod);
        }
    }

    static ArrayList<FormID> subFormToPublic (SubList<SubForm> in) {
	ArrayList<FormID> out = new ArrayList<FormID>(in.size());
	for (SubForm s : in) {
	    out.add(s.ID);
	}
	return out;
    }

    static ArrayList<SubFormInt> subFormIntToPublic (SubList<SubFormInt> in) {
	ArrayList<SubFormInt> out = new ArrayList<SubFormInt>(in.size());
	for (SubFormInt s : in) {
	    out.add(s);
	}
	return out;
    }

    /**
     *
     * @return An iterator of all records in the SubRecordList.
     */
    @Override
    public Iterator<T> iterator() {
        return collection.listIterator();
    }
}
