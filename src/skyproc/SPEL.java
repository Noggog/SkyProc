/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package skyproc;

import java.util.ArrayList;
import skyproc.exceptions.NotFound;

/**
 *
 * @author Plutoman101
 */
public class SPEL extends MagicItem {

    static final Type[] type = {Type.SPEL};
    SubForm MDOB = new SubForm(Type.MDOB);
    SubForm ETYP = new SubForm(Type.ETYP);
    SPIT SPIT = new SPIT();

    @Override
    Type[] getTypes() {
	return type;
    }

    @Override
    Record getNew() {
	return new SPEL();
    }

    SPEL() {
	super();
	init();
    }

    public SPEL(Mod modToOriginateFrom, String edid) {
	super(modToOriginateFrom, edid);
	init();
	ETYP.getForm().setInternal(new byte[]{(byte) 0x44, (byte) 0x3F, (byte) 0x01, (byte) 0x00});
	SPIT.valid = true;
    }

    @Override
    final void init() {
	super.init();
	subRecords.add(MDOB);
	subRecords.add(ETYP);
	subRecords.add(description);
	subRecords.add(SPIT);
	subRecords.add(magicEffects);
    }

    @Override
    void standardizeMasters(Mod srcMod) {
	super.standardizeMasters(srcMod);
	magicEffects.standardizeMasters(srcMod);
    }

    public enum SPELFlag {

	ManualCostCalculation(0),
	PCStartSpell(17),
	AreaEffectIgnoresLOS(19),
	IgnoreResistance(20),
	NoAbsorbOrReflect(21),
	NoDualCastModification(23);
	int value;

	SPELFlag(int valuein) {
	    value = valuein;
	}
    }

    public enum SPELType {

	Spell(0),
	Disease(1),
	Power(2),
	LesserPower(3),
	Ability(4),
	Addition(10),
	Voice(11),
	UNKNOWN(-1);
	int value;

	SPELType(int valuein) {
	    value = valuein;
	}

	static SPELType value(int value) {
	    for (SPELType s : SPELType.values()) {
		if (s.value == value) {
		    return s;
		}
	    }
	    return UNKNOWN;
	}
    }

    // Get Set functions
    public void setInventoryModel(FormID invModel) {
	MDOB.setForm(invModel);
    }

    public FormID getInventoryModel() {
	return MDOB.getForm();
    }

    public void setEquipSlot(FormID equipType) {
	ETYP.setForm(equipType);
    }

    public FormID getEquipSlot() {
	return ETYP.getForm();
    }

    public void setBaseCost(int baseCost) {
	SPIT.baseCost = baseCost;
    }

    public int getBaseCost() {
	return SPIT.baseCost;
    }

    public void set(SPELFlag flag, boolean on) {
	SPIT.flags.set(flag.value, on);
    }

    public boolean get(SPELFlag flag) {
	return SPIT.flags.is(flag.value);
    }

    public void setSpellType(SPELType type) {
	SPIT.baseType = type.value;
    }

    public SPELType getSpellType() {
	return SPELType.value(SPIT.baseType);
    }

    public void setChargeTime (float chargeTime) {
	SPIT.chargeTime = chargeTime;
    }

    public float getChargeTime () {
	return SPIT.chargeTime;
    }

    public void setCastType (CastType type) {
	SPIT.castType = type;
    }

    public CastType getCastType () {
	return SPIT.castType;
    }

    public void setDeliveryType (DeliveryType type) {
	SPIT.targetType = type;
    }

    public DeliveryType getDeliveryType () {
	return SPIT.targetType;
    }

    public void setCastDuration (float duration) {
	SPIT.castDuration = duration;
    }

    public float getCastDuration () {
	return SPIT.castDuration;
    }

    public void setRange (float range) {
	SPIT.range = range;
    }

    public float getRange () {
	return SPIT.range;
    }

    /**
     *
     * @return The PERK ref associated with the SPEL.
     */
    public FormID getPerkRef() {
	return SPIT.perkType.getForm();
    }

    /**
     *
     * @param perkRef FormID to set the SPELs PERK ref to.
     * @throws NotFound This functionality to come. Skyproc does NOT confirm
     * that the FormID associated truly points to a correct record. You will
     * have to confirm the accuracy yourself for now.
     */
    public void setPerkRef(FormID perkRef) throws NotFound {
	SPIT.perkType.setForm(perkRef);
    }

}
