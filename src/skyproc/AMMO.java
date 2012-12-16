/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package skyproc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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

    // Static prototypes and definitions
    static final ArrayList<Type> type = new ArrayList<>(Arrays.asList(new Type[]{Type.AMMO}));
    static final SubPrototype AMMOprototype = new SubPrototype(MajorRecordDescription.descProto) {

	@Override
	protected void addRecords() {
	    add(new SubData(Type.OBND));
	    reposition(Type.FULL);
	    add(SubString.getNew(Type.MODL, true));
	    add(new SubData(Type.MODT));
	    add(new AltTextures(Type.MODS));
	    add(new SubForm(Type.YNAM));
	    add(new SubForm(Type.ZNAM));
	    reposition(Type.DESC);
	    add(new KeywordSet());
	    add(new DATA());
	    add(SubString.getNew(Type.ONAM, true));
	}
    };
    static final class DATA extends SubRecordTyped {

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

    // Enums
    /**
     *
     */
    public enum AMMOFlag {

	/**
	 *
	 */
	IgnoresWeaponResistance, // 0
	NonPlayable, //1
	/**
	 *
	 */
	VanishesWhenNotInFlight; //2
    }
    
    // Common Functions
    AMMO() {
	super();
	subRecords.setPrototype(AMMOprototype);
    }

    @Override
    Record getNew() {
	return new AMMO();
    }

    @Override
    ArrayList<Type> getTypes() {
	return type;
    }

    //Get/Set
    /**
     *
     * @return
     */
    public KeywordSet getKeywordSet() {
	return subRecords.getKeywords();
    }

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
	getData().flags.set(flag.ordinal(), on);
    }

    /**
     *
     * @param flag
     * @return
     */
    public boolean get(AMMOFlag flag) {
	return getData().flags.get(flag.ordinal());
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
