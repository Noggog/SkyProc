/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package skyproc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.zip.DataFormatException;
import lev.LExporter;
import lev.LShrinkArray;
import skyproc.exceptions.BadParameter;
import skyproc.exceptions.BadRecord;

/**
 * Race Records
 *
 * @author Justin Swanson
 */
public class RACE extends MajorRecordDescription {

    private static final Type[] type = {Type.RACE};
    SubList<SubForm> spells = new SubList<SubForm>(Type.SPCT, 4, new SubForm(Type.SPLO));
    SubForm WNAM = new SubForm(Type.WNAM);
    /**
     *
     */
    public BodyTemplate bodyTemplate = new BodyTemplate();
    /**
     *
     */
    public KeywordSet keywords = new KeywordSet();
    DATA DATA = new DATA();
    SubMarkerSet<MFNAMdata> MFNAM = new SubMarkerSet<MFNAMdata>(new MFNAMdata(), Type.MNAM, Type.FNAM);
    SubList<SubString> MTNMs = new SubList<SubString>(new SubString(Type.MTNM, false));
    SubFormArray VTCK = new SubFormArray(Type.VTCK, 2);
    SubFormArray DNAM = new SubFormArray(Type.DNAM, 2);
    SubFormArray HCLF = new SubFormArray(Type.HCLF, 2);
    SubData TINL = new SubData(Type.TINL);
    SubData PNAM = new SubData(Type.PNAM);
    SubData UNAM = new SubData(Type.UNAM);
    SubForm ATKR = new SubForm(Type.ATKR);
    SubList<ATKDpackage> ATKDs = new SubList<ATKDpackage>(new ATKDpackage());
    SubData NAM1 = new SubData(Type.NAM1);
    SubMarkerSet<EGTmodel> EGTrecords = new SubMarkerSet(new EGTmodel(), Type.MNAM, Type.FNAM);
    SubForm GNAM = new SubForm(Type.GNAM);
    SubData NAM3 = new SubData(Type.NAM3);
    SubMarkerSet<HKXmodel> HKXrecords = new SubMarkerSet(new HKXmodel(), Type.MNAM, Type.FNAM);
    SubForm NAM4 = new SubForm(Type.NAM4);
    SubForm NAM5 = new SubForm(Type.NAM5);
    SubForm NAM7 = new SubForm(Type.NAM7);
    SubForm ONAM = new SubForm(Type.ONAM);
    SubForm LNAM = new SubForm(Type.LNAM);
    SubList<SubString> NAMEs = new SubList<SubString>(new SubString(Type.NAME, true));
    SubList<MTYPpackage> MTYPs = new SubList<MTYPpackage>(new MTYPpackage());
    SubData VNAM = new SubData(Type.VNAM);
    SubList<SubForm> QNAM = new SubList<SubForm>(new SubForm(Type.QNAM));
    SubForm UNES = new SubForm(Type.UNES);
    SubList<SubString> PHTN = new SubList<SubString>(new SubString(Type.PHTN, true));
    SubList<SubData> PHWT = new SubList<SubData>(new SubData(Type.PHWT));
    SubList<HeadData> headData = new SubList<HeadData>(new HeadData());
    SubForm NAM8 = new SubForm(Type.NAM8);
    SubForm RNAM = new SubForm(Type.RNAM);
    SubForm WKMV = new SubForm(Type.WKMV);
    SubForm RNMV = new SubForm(Type.RNMV);
    SubForm SWMV = new SubForm(Type.SWMV);
    SubForm FLMV = new SubForm(Type.FLMV);
    SubForm SNMV = new SubForm(Type.SNMV);

    /**
     *
     */
    RACE() {
        super();

	subRecords.add(spells);
	subRecords.add(WNAM);
	subRecords.add(bodyTemplate);
	subRecords.add(keywords);
	subRecords.add(DATA);
	MFNAM.forceMarkers = true;
	subRecords.add(MFNAM);
	MTNMs.allowDups = false;
	subRecords.add(MTNMs);
	subRecords.add(VTCK);
	subRecords.add(DNAM);
	subRecords.add(HCLF);
	subRecords.add(TINL);
	subRecords.add(PNAM);
	subRecords.add(UNAM);
	subRecords.add(ATKR);
	subRecords.add(ATKDs);
	NAM1.forceExport(true);
	subRecords.add(NAM1);
	EGTrecords.forceMarkers = true;
	subRecords.addSilent(EGTrecords);
	subRecords.add(GNAM);
	NAM3.forceExport(true);
	subRecords.add(NAM3);
	HKXrecords.forceMarkers = true;
	subRecords.addSilent(HKXrecords);
	subRecords.add(NAM4);
	subRecords.add(NAM5);
	subRecords.add(NAM7);
	subRecords.add(ONAM);
	subRecords.add(LNAM);
	subRecords.add(NAMEs);
	subRecords.add(MTYPs);
	subRecords.add(VNAM);
	subRecords.add(QNAM);
	subRecords.add(UNES);
	subRecords.add(PHTN);
	subRecords.add(PHWT);
	subRecords.add(headData);
	subRecords.add(NAM8);
	subRecords.add(RNAM);
	subRecords.add(WKMV);
	subRecords.add(RNMV);
	subRecords.add(SWMV);
	subRecords.add(FLMV);
	subRecords.add(SNMV);
    }

    @Override
    Type[] getTypes() {
        return type;
    }

    @Override
    Record getNew() {
        return new RACE();
    }

    @Override
    void importSubRecords(LShrinkArray in, Mask mask) throws BadRecord, DataFormatException, BadParameter {
        Type nextType;
        while (!in.isEmpty() && (mask == null || !mask.done())) {
            nextType = getNextType(in);
            boolean allowed = mask == null || mask.allowed.get(nextType);
            if (nextType == Type.NAM1) {
                subRecords.importSubRecord(in, mask); // import NAM1
                for (int i = 0; i < 8; i++) {
                    if (allowed) {
                        EGTrecords.parseData(EGTrecords.extractRecordData(in));
                    } else {
                        EGTrecords.extractRecordData(in);
                    }
                }
            } else if (nextType == Type.NAM3) {
                subRecords.importSubRecord(in, mask); // import NAM3
                for (int i = 0; i < 6; i++) {
                    if (allowed) {
                        HKXrecords.parseData(HKXrecords.extractRecordData(in));
                    } else {
                        HKXrecords.extractRecordData(in);
                    }
                }
            } else if (nextType == Type.NAM0) {
                subRecords.importSubRecord(in, mask); // import NAM0
                while (!in.isEmpty() && getNextType(in) != Type.WKMV) {
                    if (allowed) {
                        headData.parseData(headData.extractRecordData(in));
                    } else {
                        headData.extractRecordData(in);
                    }
                }
            } else {
                subRecords.importSubRecord(in, mask);
            }
        }
    }

    static class MFNAMdata extends SubShell {

        SubString ANAM = new SubString(Type.ANAM, true);
        SubData MODT = new SubData(Type.MODT);
        private static Type[] types = {Type.ANAM, Type.MODT};

        public MFNAMdata() {
            super(types);
            subRecords.add(ANAM);
            subRecords.add(MODT);
        }

        @Override
        SubRecord getNew(Type type) {
            return new MFNAMdata();
        }
    }

    static class ATKDpackage extends SubShell {

        SubData ATKD = new SubData(Type.ATKD);
        SubString ATKE = new SubString(Type.ATKE, true);
        private static Type[] types = {Type.ATKD, Type.ATKE};

        public ATKDpackage() {
            super(types);
            subRecords.add(ATKD);
            subRecords.add(ATKE);
        }

        @Override
        SubRecord getNew(Type type) {
            return new ATKDpackage();
        }
    }

    static class EGTmodel extends SubShell {

        SubData INDX = new SubData(Type.INDX);
        SubString MODL = new SubString(Type.MODL, true);
        SubData MODT = new SubData(Type.MODT);
        private static Type[] types = {Type.INDX, Type.MODL, Type.MODT};

        public EGTmodel() {
            super(types);
            subRecords.add(INDX);
            subRecords.add(MODL);
            subRecords.add(MODT);
        }

        @Override
        SubRecord getNew(Type type) {
            return new EGTmodel();
        }
    }

    static class HKXmodel extends SubShell {

        SubString MODL = new SubString(Type.MODL, true);
        SubData MODT = new SubData(Type.MODT);
        private static Type[] types = {Type.MODL, Type.MODT};

        public HKXmodel() {
            super(types);
            subRecords.add(MODL);
            subRecords.add(MODT);
        }

        @Override
        SubRecord getNew(Type type) {
            return new HKXmodel();
        }
    }

    static class MTYPpackage extends SubShell {

        SubForm MTYP = new SubForm(Type.MTYP);
        SubData SPED = new SubData(Type.SPED);
        private static Type[] types = {Type.MTYP, Type.SPED};

        public MTYPpackage() {
            super(types);
            subRecords.add(MTYP);
            subRecords.add(SPED);
        }

        @Override
        SubRecord getNew(Type type) {
            return new MTYPpackage();
        }
    }

    static class HeadData extends SubShell {

        private static Type[] types = {Type.NAM0};
        SubData NAM0 = new SubData(Type.NAM0);
        SubData MNAM = new SubData(Type.MNAM);
        SubData FNAM = new SubData(Type.FNAM);
        SubList<HEADs> INDXs = new SubList<HEADs>(new HEADs());
        SubList<MPAVs> MPAVs = new SubList<MPAVs>(new MPAVs());
        SubList<SubForm> RPRM = new SubList<SubForm>(new SubForm(Type.RPRM));
        SubList<SubForm> RPRF = new SubList<SubForm>(new SubForm(Type.RPRF));
        SubList<SubForm> AHCM = new SubList<SubForm>(new SubForm(Type.AHCM));
        SubList<SubForm> AHCF = new SubList<SubForm>(new SubForm(Type.AHCF));
        SubList<SubForm> FTSM = new SubList<SubForm>(new SubForm(Type.FTSM));
        SubList<SubForm> FTSF = new SubList<SubForm>(new SubForm(Type.FTSF));
        SubList<SubForm> DFTM = new SubList<SubForm>(new SubForm(Type.DFTM));
        SubList<SubForm> DFTF = new SubList<SubForm>(new SubForm(Type.DFTF));
        SubList<TINIs> TINIs = new SubList<TINIs>(new TINIs());
        SubForm NAM8 = new SubForm(Type.NAM8);
        SubForm RNAM = new SubForm(Type.RNAM);

        public HeadData() {
            super(types);
            subRecords.add(NAM0);
            subRecords.add(MNAM);
            subRecords.add(FNAM);
            subRecords.add(INDXs);
            subRecords.add(MPAVs);
            subRecords.add(RPRM);
            subRecords.add(RPRF);
            subRecords.add(AHCM);
            subRecords.add(AHCF);
            subRecords.add(FTSM);
            subRecords.add(FTSF);
            subRecords.add(DFTM);
            subRecords.add(DFTF);
            subRecords.add(TINIs);
            subRecords.add(NAM8);
            subRecords.add(RNAM);
        }

        @Override
        SubRecord getNew(Type type) {
            return new HeadData();
        }

        @Override
        Boolean isValid() {
            return true;
        }
    }

    static class HEADs extends SubShell {

        SubData INDX = new SubData(Type.INDX);
        SubData HEAD = new SubData(Type.HEAD);
        private static Type[] types = {Type.INDX, Type.HEAD};

        public HEADs() {
            super(types);
            subRecords.add(INDX);
            subRecords.add(HEAD);
        }

        @Override
        SubRecord getNew(Type type) {
            return new HEADs();
        }

        @Override
        Boolean isValid() {
            return true;
        }
    }

    static class MPAVs extends SubShell {

        SubData MPAI = new SubData(Type.MPAI);
        SubData MPAV = new SubData(Type.MPAV);
        private static Type[] types = {Type.MPAI, Type.MPAV};

        public MPAVs() {
            super(types);
            subRecords.add(MPAI);
            subRecords.add(MPAV);
        }

        @Override
        SubRecord getNew(Type type) {
            return new MPAVs();
        }

        @Override
        Boolean isValid() {
            return true;
        }
    }

    static class TINIs extends SubShell {

        SubData TINI = new SubData(Type.TINI);
        SubString TINT = new SubString(Type.TINT, true);
        SubData TINP = new SubData(Type.TINP);
        SubForm TIND = new SubForm(Type.TIND);
        SubList<TINCs> TINCs = new SubList<TINCs>(new TINCs());
        private static Type[] types = {Type.TINI, Type.TINT, Type.TINP, Type.TIND, Type.TINC, Type.TINV, Type.TIRS};

        public TINIs() {
            super(types);
            subRecords.add(TINI);
            subRecords.add(TINT);
            subRecords.add(TINP);
            subRecords.add(TIND);
            subRecords.add(TINCs);
        }

        @Override
        SubRecord getNew(Type type) {
            return new TINIs();
        }

        @Override
        Boolean isValid() {
            return true;
        }
    }

    static class TINCs extends SubShell {

        SubData TINC = new SubData(Type.TINC);
        SubData TINV = new SubData(Type.TINV);
        SubData TIRS = new SubData(Type.TIRS);
        private static Type[] types = {Type.TINC, Type.TINV, Type.TIRS};

        public TINCs() {
            super(types);
            subRecords.add(TINC);
            subRecords.add(TINV);
            subRecords.add(TIRS);
        }

        @Override
        SubRecord getNew(Type type) {
            return new TINCs();
        }

        @Override
        Boolean isValid() {
            return true;
        }
    }

    static class DATA extends SubRecord {

        byte[] fluff1 = new byte[16];
        float maleHeight = 0;
        float femaleHeight = 0;
        float maleWeight = 0;
        float femaleWeight = 0;
        byte[] fluff3 = new byte[4];
        float startingHealth = 0;
        float startingMagicka = 0;
        float startingStamina = 0;
        float baseCarryWeight = 0;
        float baseMass = 0;
        float accelerationRate = 0;
        float decelerationRate = 0;
        Size size = Size.MEDIUM;
        byte[] fluff2 = new byte[8];
        float injuredHealthPct = 0;
        byte[] fluff4 = new byte[4];
        float healthRegen = 0;
        float magickaRegen = 0;
        float staminaRegen = 0;
        float unarmedDamage = 0;
        float unarmedReach = 0;
        byte[] fluff5 = new byte[4];
        float aimAngleTolerance = 0;
        float flightRadius = 0;
        float angularAcceleration = 0;
        float angularTolerance = 0;
        byte[] fluff6 = new byte[4];

        DATA() {
            super(Type.DATA);
        }

        @Override
        void export(LExporter out, Mod srcMod) throws IOException {
            super.export(out, srcMod);
            out.write(fluff1, 16);
            out.write(maleHeight);
            out.write(femaleHeight);
            out.write(maleWeight);
            out.write(femaleWeight);
            out.write(fluff2, 4);
            out.write(startingHealth);
            out.write(startingMagicka);
            out.write(startingStamina);
            out.write(baseCarryWeight);
            out.write(baseMass);
            out.write(accelerationRate);
            out.write(decelerationRate);
            out.write(size.ordinal(), 4);
            out.write(fluff3, 8);
            out.write(injuredHealthPct);
            out.write(fluff4, 4);
            out.write(healthRegen);
            out.write(magickaRegen);
            out.write(staminaRegen);
            out.write(unarmedDamage);
            out.write(unarmedReach);
            out.write(fluff5, 4);
            out.write(aimAngleTolerance);
            out.write(flightRadius);
            out.write(angularAcceleration);
            out.write(angularTolerance);
            out.write(fluff6, 4);
        }

        @Override
        void parseData(LShrinkArray in) throws BadRecord, DataFormatException, BadParameter {
            super.parseData(in);
            fluff1 = in.extract(16);
            maleHeight = in.extractFloat();
            femaleHeight = in.extractFloat();
            maleWeight = in.extractFloat();
            femaleWeight = in.extractFloat();
            fluff2 = in.extract(4);
            startingHealth = in.extractFloat();
            startingMagicka = in.extractFloat();
            startingStamina = in.extractFloat();
            baseCarryWeight = in.extractFloat();
            baseMass = in.extractFloat();
            accelerationRate = in.extractFloat();
            decelerationRate = in.extractFloat();
            size = Size.values()[in.extractInt(4)];
            fluff3 = in.extract(8);
            injuredHealthPct = in.extractFloat();
            fluff4 = in.extract(4);
            healthRegen = in.extractFloat();
            magickaRegen = in.extractFloat();
            staminaRegen = in.extractFloat();
            unarmedDamage = in.extractFloat();
            unarmedReach = in.extractFloat();
            fluff5 = in.extract(4);
            aimAngleTolerance = in.extractFloat();
            flightRadius = in.extractFloat();
            angularAcceleration = in.extractFloat();
            angularTolerance = in.extractFloat();
            fluff6 = in.extract(4);
        }

        @Override
        SubRecord getNew(Type type) {
            return new DATA();
        }

        @Override
        int getContentLength(Mod srcMod) {
            return 128;
        }
    }

    public enum Size {

        SMALL,
        MEDIUM,
        LARGE,
        EXTRALARGE,
    }

    // Get / set
    /**
     *
     * @return FormID of the ARMO record that is worn.
     */
    public FormID getWornArmor() {
        return WNAM.getForm();
    }

    /**
     *
     * @param id FormID to set the worn ARMO record to.
     */
    public void setWornArmor(FormID id) {
        WNAM.setForm(id);
    }

    /**
     *
     * @return
     */
    public ArrayList<FormID> getSpells() {
        return SubList.subFormToPublic(spells);
    }

    /**
     *
     * @param spell
     */
    public void addSpell(FormID spell) {
        spells.add(new SubForm(Type.SPLO, spell));
    }

    /**
     *
     * @param spell
     */
    public void removeSpell(FormID spell) {
        spells.remove(new SubForm(Type.SPLO, spell));
    }

    /**
     *
     */
    public void clearSpells() {
        spells.clear();
    }

    /**
     *
     * @param gender
     * @param model
     */
    public void setModel(Gender gender, String model) {
        switch (gender) {
            case MALE:
                MFNAM.set.get(Type.MNAM).ANAM.setString(model);
                break;
            default:
                MFNAM.set.get(Type.FNAM).ANAM.setString(model);
                break;
        }
    }

    /**
     *
     * @param gender
     * @return
     */
    public String getModel(Gender gender) {
        switch (gender) {
            case MALE:
                return MFNAM.set.get(Type.MNAM).ANAM.string;
            default:
                return MFNAM.set.get(Type.FNAM).ANAM.string;
        }
    }

    /**
     *
     * @param gender
     * @param voice
     */
    public void setVoiceType(Gender gender, FormID voice) {
        switch (gender) {
            case MALE:
                VTCK.IDs.set(0, voice);
                break;
            default:
                VTCK.IDs.set(1, voice);
                break;
        }
    }

    /**
     *
     * @param gender
     * @return
     */
    public FormID getVoiceType(Gender gender) {
        switch (gender) {
            case MALE:
                return VTCK.IDs.get(0);
            default:
                return VTCK.IDs.get(1);
        }
    }

    /**
     *
     * @param gender
     * @param color
     */
    public void setHairColor(Gender gender, FormID color) {
        switch (gender) {
            case MALE:
                HCLF.IDs.set(0, color);
                break;
            default:
                HCLF.IDs.set(1, color);
                break;
        }
    }

    /**
     *
     * @param gender
     * @return
     */
    public FormID getHairColor(Gender gender) {
        switch (gender) {
            case MALE:
                return HCLF.IDs.get(0);
            default:
                return HCLF.IDs.get(1);
        }
    }

    /**
     *
     * @param gender
     * @param part
     */
    public void setDecapHeadPart(Gender gender, FormID part) {
        switch (gender) {
            case MALE:
                DNAM.IDs.set(0, part);
                break;
            default:
                DNAM.IDs.set(1, part);
                break;
        }
    }

    /**
     *
     * @param gender
     * @return
     */
    public FormID getDecapHeadPart(Gender gender) {
        switch (gender) {
            case MALE:
                return DNAM.IDs.get(0);
            default:
                return DNAM.IDs.get(1);
        }
    }

    /**
     *
     * @param gender
     * @param value
     */
    public void setHeight(Gender gender, float value) {
        switch (gender) {
            case MALE:
                DATA.maleHeight = value;
            case FEMALE:
                DATA.femaleHeight = value;
        }
    }

    /**
     *
     * @param gender
     * @return
     */
    public float getHeight(Gender gender) {
        switch (gender) {
            case MALE:
                return DATA.maleHeight;
            default:
                return DATA.femaleHeight;
        }
    }

    public float getAccelerationRate() {
        return DATA.accelerationRate;
    }

    public void setAccelerationRate(float accelerationRate) {
        this.DATA.accelerationRate = accelerationRate;
    }

    public float getAimAngleTolerance() {
        return DATA.aimAngleTolerance;
    }

    public void setAimAngleTolerance(float aimAngleTolerance) {
        this.DATA.aimAngleTolerance = aimAngleTolerance;
    }

    public float getAngularAcceleration() {
        return DATA.angularAcceleration;
    }

    public void setAngularAcceleration(float angularAcceleration) {
        this.DATA.angularAcceleration = angularAcceleration;
    }

    public float getAngularTolerance() {
        return DATA.angularTolerance;
    }

    public void setAngularTolerance(float angularTolerance) {
        this.DATA.angularTolerance = angularTolerance;
    }

    public float getBaseCarryWeight() {
        return DATA.baseCarryWeight;
    }

    public void setBaseCarryWeight(float baseCarryWeight) {
        this.DATA.baseCarryWeight = baseCarryWeight;
    }

    public float getBaseMass() {
        return DATA.baseMass;
    }

    public void setBaseMass(float baseMass) {
        this.DATA.baseMass = baseMass;
    }

    public float getDecelerationRate() {
        return DATA.decelerationRate;
    }

    public void setDecelerationRate(float decelerationRate) {
        this.DATA.decelerationRate = decelerationRate;
    }

    public float getFemaleWeight() {
        return DATA.femaleWeight;
    }

    public void setFemaleWeight(float femaleWeight) {
        this.DATA.femaleWeight = femaleWeight;
    }

    public float getFlightRadius() {
        return DATA.flightRadius;
    }

    public void setFlightRadius(float flightRadius) {
        this.DATA.flightRadius = flightRadius;
    }

    public float getHealthRegen() {
        return DATA.healthRegen;
    }

    public void setHealthRegen(float healthRegen) {
        this.DATA.healthRegen = healthRegen;
    }

    public float getInjuredHealthPct() {
        return DATA.injuredHealthPct;
    }

    public void setInjuredHealthPct(float injuredHealthPct) {
        this.DATA.injuredHealthPct = injuredHealthPct;
    }

    public float getMagickaRegen() {
        return DATA.magickaRegen;
    }

    public void setMagickaRegen(float magickaRegen) {
        this.DATA.magickaRegen = magickaRegen;
    }

    public float getMaleHeight() {
        return DATA.maleHeight;
    }

    public void setMaleHeight(float maleHeight) {
        this.DATA.maleHeight = maleHeight;
    }

    public float getMaleWeight() {
        return DATA.maleWeight;
    }

    public void setMaleWeight(float maleWeight) {
        this.DATA.maleWeight = maleWeight;
    }

    public Size getSize() {
        return DATA.size;
    }

    public void setSize(Size size) {
        this.DATA.size = size;
    }

    public float getStaminaRegen() {
        return DATA.staminaRegen;
    }

    public void setStaminaRegen(float staminaRegen) {
        this.DATA.staminaRegen = staminaRegen;
    }

    public float getStartingHealth() {
        return DATA.startingHealth;
    }

    public void setStartingHealth(float startingHealth) {
        this.DATA.startingHealth = startingHealth;
    }

    public float getStartingMagicka() {
        return DATA.startingMagicka;
    }

    public void setStartingMagicka(float startingMagicka) {
        this.DATA.startingMagicka = startingMagicka;
    }

    public float getStartingStamina() {
        return DATA.startingStamina;
    }

    public void setStartingStamina(float startingStamina) {
        this.DATA.startingStamina = startingStamina;
    }

    public float getUnarmedDamage() {
        return DATA.unarmedDamage;
    }

    public void setUnarmedDamage(float unarmedDamage) {
        this.DATA.unarmedDamage = unarmedDamage;
    }

    public float getUnarmedReach() {
        return DATA.unarmedReach;
    }

    public void setUnarmedReach(float unarmedReach) {
        this.DATA.unarmedReach = unarmedReach;
    }

    public void clearAttackData() {
        ATKDs.clear();
    }

    public FormID getMaterialType() {
        return NAM4.getForm();
    }

    public void setMaterialType(FormID id) {
        NAM4.setForm(id);
    }

    public FormID getImpactDataSet() {
        return NAM5.getForm();
    }

    public void setImpactDataSet(FormID id) {
        NAM5.setForm(id);
    }

    public FormID getDecapitationFX() {
        return NAM7.getForm();
    }

    public void setDecapitationFX(FormID id) {
        NAM7.setForm(id);
    }

    public FormID getOpenLootSound() {
        return ONAM.getForm();
    }

    public void setOpenLootSound(FormID id) {
        ONAM.setForm(id);
    }

    public FormID getCloseLootSound() {
        return LNAM.getForm();
    }

    public void setCloseLootSound(FormID id) {
        LNAM.setForm(id);
    }

    public FormID getUnarmedEquipSlot() {
        return UNES.getForm();
    }

    public void setUnarmedEquipSlot(FormID id) {
        UNES.setForm(id);
    }

    public void clearTinting() {
        headData.clear();
    }

    public String getLightingModels(Gender gender) {
        switch (gender) {
            case MALE:
                return EGTrecords.set.get(Type.MNAM).MODL.string;
            default:
                return EGTrecords.set.get(Type.FNAM).MODL.string;
        }
    }

    public void setLightingModels(Gender gender, String s) {
        switch (gender) {
            case MALE:
                EGTrecords.set.get(Type.MNAM).MODL.setString(s);
            default:
                EGTrecords.set.get(Type.FNAM).MODL.setString(s);
        }
    }

    public String getPhysicsModels(Gender gender) {
        switch (gender) {
            case MALE:
                return HKXrecords.set.get(Type.MNAM).MODL.string;
            default:
                return HKXrecords.set.get(Type.FNAM).MODL.string;
        }
    }

    public void setPhysicsModels(Gender gender, String s) {
        switch (gender) {
            case MALE:
                HKXrecords.set.get(Type.MNAM).MODL.setString(s);
            default:
                HKXrecords.set.get(Type.FNAM).MODL.setString(s);
        }
    }

    public ArrayList<FormID> getEquipSlots() {
        return SubList.subFormToPublic(QNAM);
    }

    public void addEquipSlot(FormID in) {
        QNAM.add(new SubForm(Type.QNAM, in));
    }

    public void removeEquipSlot(FormID in) {
        QNAM.remove(new SubForm(Type.QNAM, in));
    }

    public void clearEquipSlots() {
        QNAM.clear();
    }


}
