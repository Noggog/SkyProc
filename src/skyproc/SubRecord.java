/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package skyproc;

import java.util.zip.DataFormatException;
import skyproc.exceptions.BadRecord;
import java.io.IOException;
import lev.LExporter;
import skyproc.exceptions.BadParameter;
import lev.LShrinkArray;
import skyproc.MajorRecord.Mask;

/**
 * An abstract class outlining the functionality of subrecords, which are
 * records within Major Records.
 * @author Justin Swanson
 */
public abstract class SubRecord extends Record {

    Type[] type;

    SubRecord(Type[] type_) {
        // Don't call explicity aside from special subrecord constructors
        type = type_;
    }

    SubRecord(Type type_) {
        type = new Type[1];
        type[0] = type_;
    }

    @Override
    void parseData(LShrinkArray in) throws BadRecord, DataFormatException, BadParameter {
        super.parseData(in);
    }

    final void parseData(LShrinkArray in, Mask mask) throws BadRecord, DataFormatException, BadParameter {
        if (mask == null || mask.allowed.get(Record.getNextType(in)) == true) {
            parseData(in);
        }
    }

    @Override
    public String print() {
        return "No " + type[0].toString();
    }

    @Override
    public String toString() {
        return type[0].toString();
    }

    @Override
    int getSizeLength() {
        return 2;
    }

    @Override
    int getFluffLength() {
        return 0;
    }

    @Override
    Type[] getTypes() {
        return type;
    }

    @Override
    void export(LExporter out, Mod srcMod) throws IOException {
        if (isValid()) {
            out.write(getTypes()[0].toString());
            out.write(getContentLength(srcMod), 2);
        }
    }

    abstract SubRecord getNew(Type type);

    /**
     * Clears the record to "factory settings".  The actual changes depend on
     * each specific extension of the record.
     */
    public abstract void clear();

    boolean confirmLink() {
	return true;
    }

    void standardizeMasters(Mod srcMod) {
    }

    @Override
    void logSync(String header, String... log) {
        if (SPGlobal.debugSubrecordAll || SPGlobal.debugSubrecordsAllowed.contains(getTypes()[0])) {
            super.logSync(header, log);
        }
    }

    @Override
    Record getNew() {
        return getNew(Type.NULL);
    }
}
