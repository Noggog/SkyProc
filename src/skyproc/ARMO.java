/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package skyproc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.zip.DataFormatException;
import lev.LExportParser;
import lev.LFlags;
import lev.LShrinkArray;
import skyproc.exceptions.BadParameter;
import skyproc.exceptions.BadRecord;

/**
 * Armor
 * @author Justin Swanson
 */
public class ARMO extends MajorRecordDescription {

    private static final Type[] type = {Type.ARMO};
    SubData OBND = new SubData(Type.OBND);
    /**
     * A script package containing scripts and their properties
     */
    public ScriptPackage scripts = new ScriptPackage();
    SubForm EITM = new SubForm(Type.EITM);
    SubString MOD2 = new SubString(Type.MOD2, true);
    SubData MO2T = new SubData(Type.MO2T);
    SubData MO2S = new SubData(Type.MO2S);
    SubString MOD4 = new SubString(Type.MOD4, true);
    SubData MO4T = new SubData(Type.MO4T);
    SubData MO4S = new SubData(Type.MO4S);
    BODT BODT = new BODT();
    SubForm YNAM = new SubForm(Type.YNAM);
    SubForm ZNAM = new SubForm(Type.ZNAM);
    SubForm ETYP = new SubForm(Type.ETYP);
    SubForm BIDS = new SubForm(Type.BIDS);
    SubForm BAMT = new SubForm(Type.BAMT);
    SubForm RNAM = new SubForm(Type.RNAM);
    Keywords keywords = new Keywords();
    SubList<SubForm> MODLs = new SubList<SubForm>(new SubForm(Type.MODL));
    DATA DATA = new DATA();
    SubData DNAM = new SubData(Type.DNAM);
    SubForm TNAM = new SubForm(Type.TNAM);

    /**
     * Armor Major Record
     */
    ARMO() {
        super();
        subRecords.remove(Type.FULL);
        subRecords.remove(Type.DESC);

        subRecords.add(scripts);
        subRecords.add(OBND);
        subRecords.add(FULL);
        subRecords.add(EITM);
        subRecords.add(MOD2);
        subRecords.add(MO2T);
        subRecords.add(MO2S);
        subRecords.add(MOD4);
        subRecords.add(MO4T);
        subRecords.add(MO4S);
        subRecords.add(BODT);
        subRecords.add(YNAM);
        subRecords.add(ZNAM);
        subRecords.add(ETYP);
        subRecords.add(BIDS);
        subRecords.add(BAMT);
        subRecords.add(RNAM);
        subRecords.add(keywords);
        subRecords.add(DESC);
        subRecords.add(MODLs);
        subRecords.add(DATA);
        subRecords.add(DNAM);
        subRecords.add(TNAM);
    }

    @Override
    Type[] getTypes() {
        return type;
    }

    @Override
    Record getNew() {
        return new ARMO();
    }

    class DATA extends SubRecord {

        int value;
        float weight;

        DATA() {
            super(Type.DATA);
        }

        @Override
        void export(LExportParser out, Mod srcMod) throws IOException {
            super.export(out, srcMod);
            out.write(value, 4);
            out.write(weight);
        }

        @Override
        void parseData(LShrinkArray in) throws BadRecord, DataFormatException, BadParameter {
            super.parseData(in);
            value = in.extractInt(4);
            weight = in.extractFloat();
            if (logging()) {
                logSync("", "Value: " + value + ", weight " + weight);
            }
        }

        @Override
        SubRecord getNew(Type type) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void clear() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        Boolean isValid() {
            return true;
        }

        @Override
        int getContentLength(Mod srcMod) {
            return 8;
        }
    }

    class BODT extends SubRecord {

        LFlags bodyParts;
        LFlags flags;
        int armorType;
        boolean old = false;

        BODT() {
            super(Type.BODT);
        }

        @Override
        void export(LExportParser out, Mod srcMod) throws IOException {
            super.export(out, srcMod);
            out.write(bodyParts.export(), 4);
            out.write(flags.export(), 4);
            if (!old) {
                out.write(armorType, 4);
            }
        }

        @Override
        void parseData(LShrinkArray in) throws BadRecord, DataFormatException, BadParameter {
            super.parseData(in);
            bodyParts = new LFlags(in.extract(4));
            flags = new LFlags(in.extract(4));
            if (!in.isEmpty()) {
                armorType = in.extractInt(4);
            } else {
                old = true;
            }
        }

        @Override
        SubRecord getNew(Type type) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void clear() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        Boolean isValid() {
            return true;
        }

        @Override
        int getContentLength(Mod srcMod) {
            return old ? 8 : 12;
        }
    }

    /**
     * Flags associated with the ARMO record.
     */
    public enum ARMOFlags {

	/**
	 * 
	 */
	PLAYABLE
    }

    // Get/Set
    /**
     *
     * @return Returns the list of ARMA records associated with the ARMO.
     */
    public ArrayList<FormID> getArmatures() {
	return SubList.subFormToPublic(MODLs);
    }

    /**
     *
     * @param id Adds an ARMA record to the MODL list.
     */
    public void addArmature(FormID id) {
        MODLs.add(new SubForm(Type.MODL, id));
    }

    /**
     *
     * @param id Removes an ARMA record from the MODL list if it exists.
     */
    public void removeArmature(FormID id) {
        MODLs.remove(new SubForm(Type.MODL, id));
    }

    /**
     * 
     * @param id
     */
    public void setEnchantment(FormID id) {
        EITM.setForm(id);
    }

    /**
     * 
     * @param part
     * @param on
     */
    public void set(BodyPart part, Boolean on) {
        BODT.bodyParts.set(part.ordinal(), on);
    }

    /**
     * 
     * @param part
     * @return
     */
    public boolean get(BodyPart part) {
        return BODT.bodyParts.is(part.ordinal());
    }

    /**
     * 
     * @param flag
     * @param on
     */
    public void set(ARMOFlags flag, Boolean on) {
        BODT.flags.set(4, on);
    }

    /**
     * 
     * @param flag
     * @return
     */
    public boolean get(ARMOFlags flag) {
        return BODT.flags.is(4);
    }

    /**
     * 
     * @param type
     */
    public void setArmorType(ArmorType type) {
        BODT.armorType = type.ordinal();
        BODT.old = false;
    }

    /**
     * 
     * @return
     */
    public ArmorType getArmorType() {
        if (!BODT.old) {
            return ArmorType.values()[BODT.armorType];
        } else {
            return ArmorType.CLOTHING;
        }
    }

    /**
     * 
     * @return
     */
    public FormID getEnchantment() {
        return EITM.getForm();
    }

    /**
     * 
     * @param path
     * @param perspective
     */
    public void setModel(String path, Perspective perspective) {
        switch (perspective) {
            case THIRD_PERSON:
                MOD2.setString(path);
                break;
            case FIRST_PERSON:
                MOD4.setString(path);
                break;
        }
    }

    /**
     * 
     * @param perspective
     * @return
     */
    public String getModel(Perspective perspective) {
        switch (perspective) {
            case THIRD_PERSON:
                return MOD2.print();
            default:
                return MOD4.print();
        }
    }

    /**
     * 
     * @param sound
     */
    public void setPickupSound(FormID sound) {
        YNAM.setForm(sound);
    }

    /**
     * 
     * @return
     */
    public FormID getPickupSound() {
        return YNAM.getForm();
    }

    /**
     * 
     * @param sound
     */
    public void setDropSound(FormID sound) {
        ZNAM.setForm(sound);
    }

    /**
     * 
     * @return
     */
    public FormID getDropSound() {
        return ZNAM.getForm();
    }

    /**
     * 
     * @param slot
     */
    public void setEquipSlot(FormID slot) {
        ETYP.setForm(slot);
    }

    /**
     * 
     * @return
     */
    public FormID getEquipSet() {
        return ETYP.getForm();
    }

    /**
     * 
     * @param set
     */
    public void setBashImpactData(FormID set) {
        BIDS.setForm(set);
    }

    /**
     * 
     * @return
     */
    public FormID getBashImpactData() {
        return BIDS.getForm();
    }

    /**
     * 
     * @param race
     */
    public void setRace(FormID race) {
        RNAM.setForm(race);
    }

    /**
     * 
     * @return
     */
    public FormID getRace() {
        return RNAM.getForm();
    }

    /**
     * 
     * @param value
     */
    public void setValue(int value) {
        DATA.value = value;
    }

    /**
     * 
     * @return
     */
    public int getValue() {
        return DATA.value;
    }

    /**
     * 
     * @param weight
     */
    public void setWeight(float weight) {
        DATA.weight = weight;
    }

    /**
     * 
     * @return
     */
    public float getWeight() {
        return DATA.weight;
    }

    /**
     * 
     * @param rating
     */
    public void setArmorRating(int rating) {
        DNAM.setData(rating * 100);
    }

    /**
     * 
     * @return
     */
    public int getArmorRating() {
        return DNAM.toInt() / 100;
    }

    /**
     * 
     * @param template
     */
    public void setTemplate(FormID template) {
        TNAM.setForm(template);
    }

    /**
     * 
     * @return
     */
    public FormID getTemplate() {
        return TNAM.getForm();
    }
//
//    public SubList<SubForm> getKeywordIDs () {
//	return keywords;
//    }
}
