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
public class ENCH extends MagicItem {

    static final SubRecordsPrototype ENCHproto = new SubRecordsPrototype(MagicItem.magicItemProto) {

	@Override
	protected void addRecords() {
	    reposition(Type.OBND);
	    reposition(Type.FULL);
	    remove(Type.DESC);
	    add(new ENIT());
	    reposition(Type.EFID);
	    reposition(Type.KWDA);
	}
    };
    static Type[] types = {Type.ENCH};

    ENCH() {
	super();
	subRecords.prototype = ENCHproto;
    }

    @Override
    Type[] getTypes() {
	return types;
    }

    @Override
    Record getNew() {
	return new ENCH();
    }

    static class ENIT extends SubRecord {

	int baseCost = 0;
	LFlags flags = new LFlags(4);
	CastType castType = CastType.ConstantEffect;
	int chargeAmount = 0;
	DeliveryType targetType = DeliveryType.Self;
	EnchantType enchantType = EnchantType.Enchantment;
	float chargeTime = 0;
	FormID baseEnchantment = new FormID();
	FormID wornRestrictions = new FormID();

	ENIT() {
	    super(Type.ENIT);
	}

	@Override
	void export(LExporter out, Mod srcMod) throws IOException {
	    super.export(out, srcMod);
	    out.write(baseCost, 4);
	    out.write(flags.export());
	    out.write(castType.ordinal(), 4);
	    out.write(chargeAmount, 4);
	    out.write(targetType.ordinal(), 4);
	    out.write(enchantType.value, 4);
	    out.write(chargeTime);
	    baseEnchantment.export(out);
	    wornRestrictions.export(out);
	}

	@Override
	void parseData(LChannel in) throws BadRecord, DataFormatException, BadParameter {
	    super.parseData(in);
	    baseCost = in.extractInt(4);
	    flags.set(in.extract(4));
	    castType = CastType.values()[in.extractInt(4)];
	    chargeAmount = in.extractInt(4);
	    targetType = DeliveryType.values()[in.extractInt(4)];
	    enchantType = EnchantType.value(in.extractInt(4));
	    chargeTime = in.extractFloat();
	    baseEnchantment.setInternal(in.extract(4));
	    wornRestrictions.setInternal(in.extract(4));
	}

	@Override
	SubRecord getNew(Type type) {
	    return new ENIT();
	}

	@Override
	int getContentLength(Mod srcMod) {
	    return 36;
	}

	@Override
	ArrayList<FormID> allFormIDs() {
	    ArrayList<FormID> out = new ArrayList<FormID>(2);
	    out.add(baseEnchantment);
	    out.add(wornRestrictions);
	    return out;
	}
    }

    /**
     *
     */
    public enum EnchantType {

	/**
	 *
	 */
	Enchantment(6),
	/**
	 *
	 */
	StaffEnchantment(12);
	int value;

	EnchantType(int value) {
	    this.value = value;
	}

	static EnchantType value(int in) {
	    switch (in) {
		case 12:
		    return StaffEnchantment;
		default:
		    return Enchantment;
	    }
	}
    }

    /**
     *
     */
    public enum ENCHFlag {

	/**
	 *
	 */
	ManualCalc(0),
	/**
	 *
	 */
	ExtendDurationOnRecast(3);
	int value;

	ENCHFlag(int in) {
	    value = in;
	}
    }

    ENIT getENIT() {
	return (ENIT) subRecords.get(Type.ENIT);
    }

    /**
     *
     * @param cost
     */
    public void setBaseCost(int cost) {
	getENIT().baseCost = cost;
    }

    /**
     *
     * @return
     */
    public int getBaseCost() {
	return getENIT().baseCost;
    }

    /**
     *
     * @param in
     * @param on
     */
    public void set(ENCHFlag in, boolean on) {
	getENIT().flags.set(in.value, on);
    }

    /**
     *
     * @param in
     * @return
     */
    public boolean get(ENCHFlag in) {
	return getENIT().flags.get(in.value);
    }

    /**
     *
     * @param type
     */
    public void setCastType(CastType type) {
	getENIT().castType = type;
    }

    /**
     *
     * @return
     */
    public CastType getCastType() {
	return getENIT().castType;
    }

    /**
     *
     * @param amount
     */
    public void setChargeAmount(int amount) {
	getENIT().chargeAmount = amount;
    }

    /**
     *
     * @return
     */
    public int getChargeAmount() {
	return getENIT().chargeAmount;
    }

    /**
     *
     * @param type
     */
    public void setDeliveryType(DeliveryType type) {
	getENIT().targetType = type;
    }

    /**
     *
     * @return
     */
    public DeliveryType getDeliveryType() {
	return getENIT().targetType;
    }

    /**
     *
     * @param type
     */
    public void setEnchantType(EnchantType type) {
	getENIT().enchantType = type;
    }

    /**
     *
     * @return
     */
    public EnchantType getEnchantType() {
	return getENIT().enchantType;
    }

    /**
     *
     * @param time
     */
    public void setChargeTime(float time) {
	getENIT().chargeTime = time;
    }

    /**
     *
     * @return
     */
    public float getChargeTime() {
	return getENIT().chargeTime;
    }

    /**
     *
     * @param id
     */
    public void setBaseEnchantment(FormID id) {
	getENIT().baseEnchantment = id;
    }

    /**
     *
     * @return
     */
    public FormID getBaseEnchantment() {
	return getENIT().baseEnchantment;
    }

    /**
     *
     * @param id
     */
    public void setWornRestrictions(FormID id) {
	getENIT().wornRestrictions = id;
    }

    /**
     *
     * @return
     */
    public FormID getWornRestrictions() {
	return getENIT().wornRestrictions;
    }
}
