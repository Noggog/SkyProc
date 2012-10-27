package skyproc;

import java.io.Serializable;
import skyproc.SubStringPointer.Files;

/**
 * An extended major record that has a name. (FULL record)
 * @author Justin Swanson
 */
public abstract class MajorRecordNamed extends MajorRecord implements Serializable {

    static final SubRecordsPrototype namedProto = new SubRecordsPrototype(MajorRecord.majorProto);
    static {
	namedProto.add(new SubStringPointer(Type.FULL, Files.STRINGS));
    }

    MajorRecordNamed() {
        super();
    }

    /**
     * Returns the in-game name of the Major Record.
     * @return
     */
    public String getName() {
        return subRecords.getSubString(Type.FULL).print();
    }

    /**
     * Sets the in-game name of the Major Record.
     * @param in The string to set the in-game name to.
     */
    public void setName(String in) {
        subRecords.setSubString(Type.FULL, in);
    }

}
