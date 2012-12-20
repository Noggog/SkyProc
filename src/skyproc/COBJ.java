package skyproc;

import java.util.ArrayList;

/**
 *
 * @author Arkangel
 */
public class COBJ extends MajorRecord {

    // Static prototypes and definitions
    static final SubPrototype COBJproto = new SubPrototype(MajorRecord.majorProto) {

	@Override
	protected void addRecords() {
	    add(new SubListCounted<>("COCT", 4, new SubFormInt("CNTO")));
	    add(new SubData("COED"));
	    add(new SubList<>(new Condition()));
	    add(new SubForm("CNAM"));
	    add(new SubForm("BNAM"));
	    add(new SubInt("NAM1", 2));
	    add(new KeywordSet());
	}
    };

    // Common Functions
    /**
     * Creates a new COBJ record with CK default settings.
     * @param srcMod The mod to have the new COBJ originate from.
     * @param edid A unique EDID
     */
    public COBJ (Mod srcMod, String edid) {
	this();
	originateFrom(srcMod, edid);
	subRecords.getSubInt("NAM1").set(1);
    }

    COBJ() {
	super();
	subRecords.setPrototype(COBJproto);
    }

    @Override
    ArrayList<String> getTypes() {
	return Record.getTypeList("COBJ");
    }

    @Override
    Record getNew() {
	return new COBJ();
    }

    // Get/Set
    /**
     *
     * @return
     */
    public KeywordSet getKeywordSet() {
	return subRecords.getKeywords();
    }

    /**
     *
     * @return
     */
    public ArrayList<Condition> getConditions() {
	return subRecords.getSubList("CTDA").toPublic();
    }

    /**
     *
     * @param c
     */
    public void addCondition(Condition c) {
	subRecords.getSubList("CTDA").add(c);
    }

    /**
     *
     * @param c
     */
    public void removeCondition(Condition c) {
	subRecords.getSubList("CTDA").remove(c);
    }

    /**
     *
     * @param itemReference
     * @param count
     * @return
     */
    public boolean addIngredient(FormID itemReference, int count) {
	return subRecords.getSubList("CNTO").add(new SubFormInt("CNTO", itemReference, count));
    }

    /**
     *
     * @param itemReference
     * @return
     */
    public boolean removeIngredient(FormID itemReference) {
	return subRecords.getSubList("CNTO").remove(new SubFormInt("CNTO", itemReference, 1));
    }

    /**
     *
     */
    public void clearIngredients() {
	subRecords.getSubList("CNTO").clear();
    }

    /**
     *
     * @return
     */
    public ArrayList<SubFormInt> getIngredients() {
	return SubList.subFormIntToPublic(subRecords.getSubList("CNTO"));
    }

    /**
     *
     * @return
     */
    public FormID getResultFormID() {
	return subRecords.getSubForm("CNAM").getForm();
    }

    /**
     *
     * @param form
     */
    public void setResultFormID(FormID form) {
	subRecords.setSubForm("CNAM", form);
    }

    /**
     *
     * @return
     */
    public FormID getBenchKeywordFormID() {
	return subRecords.getSubForm("BNAM").getForm();
    }

    /**
     *
     * @param form
     */
    public void setBenchKeywordFormID(FormID form) {
	subRecords.setSubForm("BNAM", form);
    }

    /**
     *
     * @return
     */
    public int getOutputQuantity() {
	return subRecords.getSubInt("NAM1").get();
    }

    /**
     *
     * @param n
     */
    public void setOutputQuantity(int n) {
	subRecords.setSubInt("NAM1", n);
    }
}
