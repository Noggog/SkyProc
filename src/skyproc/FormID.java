/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package skyproc;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import lev.LExportParser;
import lev.Ln;

/**
 * This class represents a FormID that distinguishes one record from another.  The FormID is value unique
 * across all mods.  This class supports automatic mod index correction.
 * @author Justin Swanson
 */
public class FormID implements Serializable {

    boolean valid = false;
    byte[] form = new byte[4];
    private ModListing master = null;
    /**
     * An empty FormID for easy NULL checking.
     */
    public static final FormID NULL = new FormID();

    FormID() {
    }

    /**
     *
     * @param id String containing the last 6 digits of a FormID.  Acceptable forms: "000123", "00 01 23", "0x00 0x01 0x23".
     * @param master String containing the mod it originates from.  Eg. "Skyrim.esm"
     */
    public FormID(String id, String master) {
        this(id, new ModListing(master));
    }

    public FormID(String form) {
        this(form.substring(0, 6), form.substring(6));
    }

    /**
     *
     * @param id String containing the last 6 digits of a FormID.  Acceptable forms: "000123", "00 01 23", "0x00 0x01 0x23".
     * @param master The mod from which this formID originates.
     */
    public FormID(String id, ModListing master) {
        this(Ln.parseHexString(id, 4, false), master);
    }

    public FormID(byte[] id, ModListing master) {
        set(id);
        this.master = master;
    }

    /**
     *
     * @param id An int array containing the last 6 digits of the FormID.
     * @param master The modname from which this formID originates.
     */
    public FormID(int[] id, ModListing master) {
        this(Ln.toByteArray(id), master);
    }



    FormID(int id, ModListing master) {
        setInternal(Ln.toByteArray(id));
        this.master = master;
    }

    FormID(FormID in) {
        form = in.form;
        master = in.master;
    }

    /**
     *
     * @param id An int array containing the last 6 digits of the FormID.
     */
    final public void set(byte[] id) {
        setInternal(Ln.reverse(id));
    }

    final void setInternal(byte[] id) {
        form = id;
        if (id.length > 4) {
            form = Arrays.copyOfRange(form, 0, 3);
        } else if (id.length < 3) {
            form = new byte[4];
            System.arraycopy(id, 0, form, 0, id.length);
        }
        valid = true;
//	}
    }

    void export(LExportParser out) throws IOException {
        if (isValid()) {
            out.write(getInternal(true), 4);
        }
    }

    String getArrayStr(Boolean masterIndex) {
        if (isValid()) {
            return Ln.printHex(getInternal(masterIndex), false, true);
        } else {
            return "No FormID";
        }
    }

    /**
     *
     * @return An int array of length 8, including the current master index.
     */
    public byte[] get() {
        return Ln.reverse(getInternal(true));
    }

    byte[] getInternal(Boolean masterIndex) {
        if (isValid()) {
            if (!masterIndex || master == null) {
                return Arrays.copyOfRange(form, 0, 3);
            } else {
                return form;
            }
        } else {
            return null;
        }
    }

    /**
     *
     * @return The name of the mod from which this FormID originates.
     */
    public ModListing getMaster() {
        return master;
    }

    /**
     *
     * @return A string representation of this FormID, with the master name printed instead of numerical mod index.
     */
    public String getFormStr() {
        if (master == null) {
            return getArrayStr(false);
        } else {
            return getArrayStr(false) + getMaster().print();
        }
    }

    /**
     *
     * @return A string representing the FormID.
     */
    public String getTitle() {
        return toString();
    }

    /**
     *
     * @return A representation of the formID. (eg. "000123Skyrim.esm")
     */
    @Override
    public String toString() {
        if (isStandardized()) {
            return getFormStr();
        } else if (isValid()) {
            return getArrayStr(true);
        } else {
            return "NULL";
        }
    }

    void standardize(Mod srcMod) {
        if (isValid()) {
            if (master == null) {
                master = srcMod.getNthMaster(form[3]);
            }
            adjustMasterIndex(srcMod);
        }
    }

    private void adjustMasterIndex(Mod srcMod) {
        ArrayList<String> masters = srcMod.getMastersStrings();
        for (byte i = 0; i < masters.size(); i++) {
            if (masters.get(i).equals(master.print())) {
                form[3] = i;
                return;
            }
        }
        form[3] = (byte)masters.size();
    }

    Boolean isValid() {
        return valid;
    }

    Boolean isStandardized() {
        return (isValid() && master != null);
    }

    int getContentLength() {
        return 4;
    }

    /**
     *
     * @param obj Another FormID
     * @return True if the originating masters and the last 6 indices match.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final FormID other = (FormID) obj;
        for (int i = 0; i < 3; i++) {
            if (form[i] != other.form[i]) {
                return false;
            }
        }
        if ((this.master == null) ? (other.master != null) : !this.master.equals(other.master)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Arrays.hashCode(Arrays.copyOf(this.form, 3));
        hash = 97 * hash + (this.master != null ? this.master.hashCode() : 0);
        return hash;
    }

}
