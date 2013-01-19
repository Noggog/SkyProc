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
 * A internal structure found in many major records representing body setups.
 * The use depends on the context of the major record it is inside of.
 *
 * @author Justin Swanson
 */
public class BodyTemplate extends SubShell {

    static SubPrototype BODTproto = new SubPrototype() {
	@Override
	protected void addRecords() {
	    add(new BodyTemplateMain());
	    add(new SubData("BOD2"));
	}
    };

    static class BodyTemplateMain extends SubRecord {

	LFlags bodyParts = new LFlags(4);
	LFlags flags = new LFlags(4);
	ArmorType armorType = null;
	boolean valid = false;

	BodyTemplateMain() {
	    super();
	}

	@Override
	void export(LExporter out, Mod srcMod) throws IOException {
	    super.export(out, srcMod);
	    out.write(bodyParts.export(), 4);
	    out.write(flags.export(), 4);
	    if (armorType != null) {
		out.write(armorType.ordinal());
	    }
	}

	@Override
	void parseData(LChannel in) throws BadRecord, DataFormatException, BadParameter {
	    super.parseData(in);
	    bodyParts = new LFlags(in.extract(4));
	    flags = new LFlags(in.extract(4));
	    if (!in.isDone()) {
		armorType = ArmorType.values()[in.extractInt(4)];
	    }
	    valid = true;
	}

	@Override
	SubRecord getNew(String type) {
	    return new BodyTemplateMain();
	}

	@Override
	boolean isValid() {
	    return valid;
	}

	@Override
	int getContentLength(Mod srcMod) {
	    return armorType == null ? 8 : 12;
	}

	@Override
	ArrayList<String> getTypes() {
	    return Record.getTypeList("BODT");
	}
    }

    BodyTemplate() {
	super(BODTproto);
    }

    /**
     * An enum containg various body part slots.
     *
     * @author Justin Swanson
     */
    public enum FirstPersonFlags {

	/**
	 *
	 */
	HEAD,
	/**
	 *
	 */
	HAIR,
	/**
	 *
	 */
	BODY,
	/**
	 *
	 */
	HANDS,
	/**
	 *
	 */
	FOREARMS,
	/**
	 *
	 */
	AMULET,
	/**
	 *
	 */
	RING,
	/**
	 *
	 */
	FEET,
	/**
	 *
	 */
	CALVES,
	/**
	 *
	 */
	SHIELD,
	/**
	 *
	 */
	TAIL,
	/**
	 *
	 */
	LONG_HAIR,
	/**
	 *
	 */
	CIRCLET,
	/**
	 *
	 */
	EARS,
	/**
	 *
	 */
	BodyAddOn3,
	/**
	 *
	 */
	BodyAddOn4,
	/**
	 *
	 */
	BodyAddOn5,
	/**
	 *
	 */
	BodyAddOn6,
	/**
	 *
	 */
	BodyAddOn7,
	/**
	 *
	 */
	BodyAddOn8,
	/**
	 *
	 */
	DecapitateHead,
	/**
	 *
	 */
	Decapitate,
	/**
	 *
	 */
	BodyAddOn9,
	/**
	 *
	 */
	BodyAddOn10,
	/**
	 *
	 */
	BodyAddOn11,
	/**
	 *
	 */
	BodyAddOn12,
	/**
	 *
	 */
	BodyAddOn13,
	/**
	 *
	 */
	BodyAddOn14,
	/**
	 *
	 */
	BodyAddOn15,
	/**
	 *
	 */
	BodyAddOn16,
	/**
	 *
	 */
	BodyAddOn17,
	/**
	 *
	 */
	FX01,}

    /**
     *
     */
    public enum GeneralFlags {

	/**
	 *
	 */
	ModulatesVoice(0),
	/**
	 *
	 */
	NonPlayable(4);
	int value;

	GeneralFlags(int val) {
	    value = val;
	}
    }

    BodyTemplateMain getMain() {
	return (BodyTemplateMain) subRecords.get("BODT");
    }

    @Override
    SubRecord getNew(String type) {
	return new BodyTemplate();
    }

    /**
     *
     * @param flag
     * @param on
     */
    public void set(FirstPersonFlags flag, boolean on) {
	BodyTemplateMain main = getMain();
	main.bodyParts.set(flag.ordinal(), on);
	main.valid = true;

    }

    /**
     *
     * @param part
     * @return
     */
    public boolean get(FirstPersonFlags part) {
	BodyTemplateMain main = getMain();
	main.valid = true;
	return main.bodyParts.get(part.ordinal());
    }

    /**
     *
     * @param flag
     * @param on
     */
    public void set(GeneralFlags flag, boolean on) {
	BodyTemplateMain main = getMain();
	main.valid = true;
	main.flags.set(flag.value, on);
    }

    /**
     *
     * @param flag
     * @return
     */
    public boolean get(GeneralFlags flag) {
	BodyTemplateMain main = getMain();
	main.valid = true;
	return main.flags.get(flag.value);
    }

    /**
     *
     * @param type
     */
    public void setArmorType(ArmorType type) {
	BodyTemplateMain main = getMain();
	main.valid = true;
	main.armorType = type;
    }

    /**
     *
     * @return
     */
    public ArmorType getArmorType() {
	BodyTemplateMain main = getMain();
	main.valid = true;
	return main.armorType;
    }
}