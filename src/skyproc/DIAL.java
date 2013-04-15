/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package skyproc;

import java.io.IOException;
import java.util.ArrayList;
import lev.LExporter;
import lev.Ln;

/**
 *
 * @author Justin Swanson
 */
public class DIAL extends MajorRecord {

    static final INFO info = new INFO();
    static final SubPrototype DIALprototype = new SubPrototype(MajorRecord.majorProto) {

	@Override
	protected void addRecords() {
	    add(new SubStringPointer("FULL", SubStringPointer.Files.STRINGS));
	    add(new SubFloat("PNAM"));
	    add(new SubForm("BNAM"));
	    add(new SubForm("QNAM"));
	    add(new SubData("DATA"));
	    add(SubString.getNew("SNAM", false));
	    add(new SubInt("TIFC"));
	}
    };
    GRUP<INFO> grup = new GRUP<>(info);
    boolean gruped = false;

    DIAL() {
	super();
	subRecords.setPrototype(DIALprototype);
    }

    @Override
    ArrayList<String> getTypes() {
	return Record.getTypeList("DIAL");
    }

    @Override
    Record getNew() {
	return new DIAL();
    }

    @Override
    GRUP getGRUPAppend() {
	gruped = true;
	return grup;
    }

    @Override
    boolean shouldExportGRUP() {
	return gruped;//!grup.isEmpty() || version[2] != 1 || getEDID().equals("");
    }

    @Override
    void export(ModExporter out) throws IOException {
	subRecords.setSubInt("TIFC", grup.getRecords().size());
	super.export(out);
    }
    
    // Get/Set
    public void setDialog(String dialog) {
	subRecords.setSubStringPointer("FULL", dialog);
    }

    public String getDialog() {
	return subRecords.getSubStringPointer("FULL").print();
    }

    public void setPriority(Float f) {
	subRecords.setSubFloat("PNAM", f);
    }

    public float getPriority() {
	return subRecords.getSubFloat("PNAM").get();
    }

    public void setBranch(FormID branch) {
	subRecords.setSubForm("BNAM", branch);
    }

    public FormID getBranch() {
	return subRecords.getSubForm("BNAM").getForm();
    }

    public void setQuest(FormID quest) {
	subRecords.setSubForm("QNAM", quest);
    }

    public FormID getQuest() {
	return subRecords.getSubForm("QNAM").getForm();
    }

    public void setSubTypeName(String name) {
	if (name.length() < 4) {
	    name = Ln.spaceRight(4, '_', name);
	}
	subRecords.setSubString("SNAM", name);
    }
    
    public String getSubTypeName () {
	return subRecords.getSubString("SNAM").print();
    }
    
    public ArrayList<INFO> getDialogTopicInfos() {
	return grup.getRecords();
    }
}
