/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package skyproc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.zip.DataFormatException;
import lev.LChannel;
import lev.LExporter;
import lev.LFlags;
import skyproc.exceptions.BadParameter;
import skyproc.exceptions.BadRecord;

/**
 *
 * @author Justin Swanson
 */
public class AMMO extends MajorRecordDescription {

    static final SubRecordsPrototype prototype = new SubRecordsPrototype(MajorRecordDescription.descProto);
    static {
	prototype.add(new SubData(Type.OBND));
	prototype.reposition(Type.FULL);
	prototype.add(new SubString(Type.MODL, true));
	prototype.add(new SubData(Type.MODT));
	prototype.add(new SubForm(Type.YNAM));
	prototype.add(new SubForm(Type.ZNAM));
	prototype.reposition(Type.DESC);
	prototype.add(new KeywordSet());
	prototype.add(new DATA());
    }
    static Type[] types = {Type.AMMO};

    AMMO() {
	super();
    }

    @Override
    Record getNew() {
	return new AMMO();
    }

    @Override
    Type[] getTypes() {
	return types;
    }

    static class DATA extends SubRecord {

	FormID projectile = new FormID();
	LFlags flags = new LFlags(4);
	float damage = 0;
	int value = 0;

	DATA() {
	    super(Type.DATA);
	}

	@Override
	void export(LExporter out, Mod srcMod) throws IOException {
	    super.export(out, srcMod);
	    projectile.export(out);
	    out.write(flags.export(), 4);
	    out.write(damage);
	    out.write(value);
	}

	@Override
	void parseData(LChannel in) throws BadRecord, DataFormatException, BadParameter {
	    super.parseData(in);
	    projectile.setInternal(in.extract(4));
	    flags.set(in.extract(4));
	    damage = in.extractFloat();
	    value = in.extractInt(4);
	}

	@Override
	SubRecord getNew(Type type) {
	    return new DATA();
	}

	@Override
	int getContentLength(Mod srcMod) {
	    return 16;
	}

	@Override
	ArrayList<FormID> allFormIDs() {
	    ArrayList<FormID> out = new ArrayList<FormID>(1);
	    out.add(projectile);
	    return out;
	}
    }

    /**
     *
     */
    public enum AMMOFlag {

	/**
	 *
	 */
	IgnoresWeaponResistance,
	/**
	 *
	 */
	VanishesWhenNotInFlight;
    }

    //Get/Set
    /**
     *
     * @param path
     */
    public void setModel(String path) {
	subRecords.setSubString(Type.MODL, path);
    }

    /**
     *
     * @return
     */
    public String getModel() {
	return subRecords.getSubString(Type.MODL).print();
    }

    /**
     *
     * @param sound
     */
    public void setPickupSound(FormID sound) {
	subRecords.setSubForm(Type.YNAM, sound);
    }

    /**
     *
     * @return
     */
    public FormID getPickupSound() {
	return subRecords.getSubForm(Type.YNAM).getForm();
    }

    /**
     *
     * @param sound
     */
    public void setDropSound(FormID sound) {
	subRecords.setSubForm(Type.ZNAM, sound);
    }

    /**
     *
     * @return
     */
    public FormID getDropSound() {
	return subRecords.getSubForm(Type.ZNAM).getForm();
    }

    DATA getData() {
	return (DATA) subRecords.get(Type.DATA);
    }

    /**
     *
     * @param projectile
     */
    public void setProjectile(FormID projectile) {
	getData().projectile = projectile;
    }

    /**
     *
     * @return
     */
    public FormID getProjectile() {
	return getData().projectile;
    }

    /**
     *
     * @param flag
     * @param on
     */
    public void set(AMMOFlag flag, boolean on) {
	getData().flags.set(flag.ordinal() + 1, on);
    }

    /**
     *
     * @param flag
     * @return
     */
    public boolean get(AMMOFlag flag) {
	return getData().flags.get(flag.ordinal() + 1);
    }

    /**
     *
     * @param damage
     */
    public void setDamage(float damage) {
	getData().damage = damage;
    }

    /**
     *
     * @return
     */
    public float getDamage() {
	return getData().damage;
    }

    /**
     *
     * @param gold
     */
    public void setValue(int gold) {
	getData().value = gold;
    }

    /**
     *
     * @return
     */
    public int getValue() {
	return getData().value;
    }
}
