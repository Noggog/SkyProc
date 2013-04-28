/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package skyproc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.zip.DataFormatException;
import lev.LImport;
import skyproc.exceptions.BadParameter;
import skyproc.exceptions.BadRecord;

/**
 *
 * @author Justin Swanson
 */
public class STAT extends MajorRecord {

    static final SubPrototype STATprototype = new SubPrototype(MajorRecord.majorProto) {

	@Override
	protected void addRecords() {
	    add(new SubData("OBND", new byte[12]));
	    add(new Model());
	    add(new DNAM());
	    add(new SubData("MNAM"));
	}
    };
    static class DNAM extends SubRecordTyped {

	float angle = 0;
	FormID id = new FormID();

	DNAM () {
	    super("DNAM");
	}

	@Override
	void export(ModExporter out) throws IOException {
	    super.export(out);
	    out.write(angle);
	    id.export(out);
	}

	@Override
	void parseData(LImport in, Mod srcMod) throws BadRecord, BadParameter, DataFormatException {
	    super.parseData(in, srcMod);
	    angle = in.extractFloat();
	    id.setInternal(in.extract(4));
	}

	@Override
	ArrayList allFormIDs() {
	    ArrayList<FormID> out = new ArrayList<>(1);
	    out.add(id);
	    return out;
	}

	@Override
	SubRecord getNew(String type) {
	    return new DNAM();
	}

	@Override
	int getContentLength(ModExporter out) {
	    return 8;
	}
    }

    // Common Functions
    STAT() {
	super();
	subRecords.setPrototype(STATprototype);
    }
    
    public STAT(String edid) {
	this();
	originateFromPatch(edid);
    }

    @Override
    ArrayList<String> getTypes() {
	return Record.getTypeList("STAT");
    }

    @Override
    Record getNew() {
	return new STAT();
    }

    public Model getModelData() {
	return subRecords.getModel();
    }
    
    DNAM getDNAM() {
	return (DNAM) subRecords.get("DNAM");
    }
    
    public float getMaxAngle() {
	return getDNAM().angle;
    }
    
    public void setMaxAngle(float angle) {
	getDNAM().angle = angle;
    }
    
    public void setMaterial(FormID id) {
	getDNAM().id = id;
    }
    
    public FormID getMaterial() {
	return getDNAM().id;
    }
}