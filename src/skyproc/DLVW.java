/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package skyproc;

import java.util.ArrayList;

/**
 *
 * @author Justin Swanson
 */
public class DLVW extends MajorRecord {

    static final SubPrototype DLVWprototype = new SubPrototype(MajorRecord.majorProto) {

	@Override
	protected void addRecords() {
	    add(new SubForm("QNAM"));
	    add(new SubList<>(new SubForm("BNAM")));
	    add(new SubList<>(new SubForm("TNAM")));
	    add(new SubData("ENAM"));
	    add(new SubData("DNAM"));
	}
    };

    DLVW() {
	super();
	subRecords.setPrototype(DLVWprototype);
    }

    @Override
    Record getNew() {
	return new DLVW();
    }

    @Override
    ArrayList<String> getTypes() {
	return Record.getTypeList("DLVW");
    }

    /**
     *
     * @param quest
     */
    public void setQuest(FormID quest) {
	subRecords.setSubForm("QNAM", quest);
    }

    /**
     *
     * @return
     */
    public FormID getQuest() {
	return subRecords.getSubForm("QNAM").getForm();
    }

    /**
     *
     * @return
     */
    public ArrayList<FormID> getBranches () {
	return subRecords.getSubList("BNAM").toPublic();
    }

    /**
     * @deprecated modifying the ArrayList will now directly affect the record.
     * @param dialogBranch
     */
    public void addBranch(FormID dialogBranch) {
	subRecords.getSubList("BNAM").add(new SubForm("BNAM", dialogBranch));
    }

    /**
     * @deprecated modifying the ArrayList will now directly affect the record.
     * @param dialogBranch
     */
    public void removeBranch(FormID dialogBranch) {
	subRecords.getSubList("BNAM").remove(new SubForm("BNAM", dialogBranch));
    }

    /**
     * @deprecated modifying the ArrayList will now directly affect the record.
     */
    public void clearBranches() {
	subRecords.getSubList("BNAM").clear();
    }
}
