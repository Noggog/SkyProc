/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package skyproc;

import java.util.HashMap;
import java.util.Map;
import skyproc.ConditionOption.Cond_Axis;
import skyproc.ConditionOption.Cond_CastingSource;
import skyproc.ConditionOption.Cond_CastingSource_FormID;
import skyproc.ConditionOption.Cond_FormID;
import skyproc.ConditionOption.Cond_FormID_Axis;
import skyproc.ConditionOption.Cond_FormID_CastingSource;
import skyproc.ConditionOption.Cond_FormID_CrimeType;
import skyproc.ConditionOption.Cond_FormID_Float;
import skyproc.ConditionOption.Cond_FormID_FormID;
import skyproc.ConditionOption.Cond_FormID_Int;
import skyproc.ConditionOption.Cond_FormID_String;
import skyproc.ConditionOption.Cond_Gender;
import skyproc.ConditionOption.Cond_Int;
import skyproc.ConditionOption.Cond_Int_FormID;
import skyproc.ConditionOption.Cond_Int_FormID_Int;
import skyproc.ConditionOption.Cond_Int_Int;
import skyproc.ConditionOption.Cond_String;
import skyproc.ConditionOption.Cond_WardState;
import skyproc.EmbeddedScripts.Param;
import skyproc.EmbeddedScripts.ParamType;

/**
 *
 * @author Justin Swanson
 */
public class Condition extends SubShell {

    static Map<Integer, Enum> scriptMap = new HashMap<>();
    static Type[] types = {Type.CTDA, Type.CIS1, Type.CIS2};
    ConditionBase cond = new ConditionBase();
    SubString CIS1 = new SubString(Type.CIS1, true);
    SubString CIS2 = new SubString(Type.CIS2, true);

    Condition() {
	super(types);
	subRecords.add(cond);
	subRecords.add(CIS1);
	subRecords.add(CIS2);
    }

    /**
     * Condition ctor for functions with no parameters.
     * @param function
     */
    public Condition(P_NoParams function) {
	this();
	init(function, function.index);
    }

    /**
     * Condition ctor for functions with a single FormID parameter.
     * @param function
     * @param id
     */
    public Condition(P_FormID function, FormID id) {
	this();
	cond.option = new Cond_FormID(id);
	init(function, function.index);
    }

    /**
     * Condition ctor for functions with a single Axis parameter.
     * @param function
     * @param a
     */
    public Condition(P_Axis function, Axis a) {
	this();
	cond.option = new Cond_Axis(a);
	init(function, function.index);
    }

    /**
     * Condition ctor for functions with FormID and CastingSource parameters.
     * @param function
     * @param id
     * @param source
     */
    public Condition(P_FormID_CastingSource function, FormID id, CastingSource source) {
	this();
	cond.option = new Cond_FormID_CastingSource(id, source);
	init(function, function.index);
    }

    /**
     * Condition ctor for functions with FormID and Int parameters.
     * @param function
     * @param id
     * @param i
     */
    public Condition(P_FormID_Int function, FormID id, int i) {
	this();
	cond.option = new Cond_FormID_Int(id, i);
	init(function, function.index);
    }

    /**
     * Condition ctor for functions with two FormID parameters.
     * @param function
     * @param id1
     * @param id2
     */
    public Condition(P_FormID_FormID function, FormID id1, FormID id2) {
	this();
	cond.option = new Cond_FormID_FormID(id1, id2);
	init(function, function.index);
    }

    /**
     * Condition ctor for functions with a CastingSource and FormID parameter.
     * @param function
     * @param source
     * @param id
     */
    public Condition(P_CastingSource_FormID function, CastingSource source, FormID id) {
	this();
	cond.option = new Cond_CastingSource_FormID(source, id);
	init(function, function.index);
    }

    /**
     * Condition ctor for functions with a Gender parameter.
     * @param function
     * @param g
     */
    public Condition(P_Gender function, Gender g) {
	this();
	cond.option = new Cond_Gender(g);
	init(function, function.index);
    }

    /**
     * Condition ctor for functions with a CastingSource parameter.
     * @param function
     * @param source
     */
    public Condition(P_CastingSource function, CastingSource source) {
	this();
	cond.option = new Cond_CastingSource(source);
	init(function, function.index);
    }

    /**
     * Condition ctor for functions with Int, FormID, Int parameters.
     * @param function
     * @param i1
     * @param id
     * @param i2
     */
    public Condition(P_Int_FormID_Int function, int i1, FormID id, int i2) {
	this();
	cond.option = new Cond_Int_FormID_Int(i1, id, i2);
	init(function, function.index);
    }

    /**
     * Condition ctor for functions with an Int and FormID parameter.
     * @param function
     * @param i1
     * @param id
     */
    public Condition(P_Int_FormID function, int i1, FormID id) {
	this();
	cond.option = new Cond_Int_FormID(i1, id);
	init(function, function.index);
    }

    /**
     * Condition ctor for functions with a WardState parameter.
     * @param function
     * @param state
     */
    public Condition(P_WardState function, WardState state) {
	this();
	cond.option = new Cond_WardState(state);
	init(function, function.index);
    }

    /**
     * Condition ctor for functions with an Int parameter.
     * @param function
     * @param i
     */
    public Condition(P_Int function, int i) {
	this();
	cond.option = new Cond_Int(i);
	init(function, function.index);
    }

    /**
     *
     * @param function
     * @param id
     * @param s
     */
    public Condition(P_FormID_String function, FormID id, String s) {
	this();
	cond.option = new Cond_FormID_String(id, s);
	CIS2.setString(s);
	init(function, function.index);
    }

    /**
     *
     * @param function
     * @param id
     * @param a
     */
    public Condition(P_FormID_Axis function, FormID id, Axis a) {
	this();
	cond.option = new Cond_FormID_Axis(id, a);
	init(function, function.index);
    }

    /**
     * Condition ctor for functions with a FormID and CrimeType parameter.
     * @param function
     * @param id
     * @param c
     */
    public Condition(P_FormID_CrimeType function, FormID id, CrimeType c) {
	this();
	cond.option = new Cond_FormID_CrimeType(id, c);
	init(function, function.index);
    }

    /**
     * Condition ctor for functions with a FormID and Float parameter.
     * @param function
     * @param id
     * @param f
     */
    public Condition(P_FormID_Float function, FormID id, float f) {
	this();
	cond.option = new Cond_FormID_Float(id, f);
	init(function, function.index);
    }

    /**
     * Condition ctor for functions with two Int parameters.
     * @param function
     * @param i1
     * @param i2
     */
    public Condition(P_Int_Int function, int i1, int i2) {
	this();
	cond.option = new Cond_Int_Int(i1, i2);
	init(function, function.index);
    }

    /**
     * Condition ctor for functions with a string parameter.
     * @param function
     * @param s
     */
    public Condition(P_String function, String s) {
	this();
	cond.option = new Cond_String(s);
	CIS1.setString(s);
	init(function, function.index);
    }

    final void init(Enum function, int index) {
	cond.option.script = function;
	cond.option.index = index;
    }

    @Override
    SubRecord getNew(Type type) {
	return new Condition();
    }

    @Override
    Boolean isValid() {
	return cond.isValid();
    }

    /**
     *
     * @param flag
     * @return
     */
    public boolean get(CondFlag flag) {
	return cond.get(flag);
    }

    /**
     *
     * @param flag
     * @param on
     */
    public void set(CondFlag flag, boolean on) {
	cond.set(flag, on);
    }

    /**
     *
     */
    public enum CondFlag {

	/**
	 *
	 */
	OR(0),
	/**
	 *
	 */
	UseAliases(1),
	/**
	 *
	 */
	UseGlobal(3),
	/**
	 *
	 */
	UsePackData(4),
	/**
	 *
	 */
	SwapSubjectAndTarget(5);
	int value;

	CondFlag(int value) {
	    this.value = value;
	}
    }

    /**
     *
     * @param id
     */
    public void setValue(FormID id) {
	cond.comparisonValueForm = id;
    }

    /**
     *
     * @param f
     */
    public void setValue(float f) {
	cond.comparisonValueFloat = f;
    }

    /**
     *
     * @return
     */
    public FormID getValueGlobal() {
	return cond.comparisonValueForm;
    }

    /**
     *
     * @return
     */
    public float getValueFloat() {
	return cond.comparisonValueFloat;
    }

    /**
     *
     * @param o
     */
    public void setOperator(Operator o) {
	cond.operator = o;
    }

    /**
     *
     * @return
     */
    public Operator getOperator() {
	return cond.operator;
    }

    /**
     *
     * @param t
     */
    public void setRunOnType (RunOnType t) {
	cond.option.runType = t;
    }

    /**
     *
     * @return
     */
    public RunOnType getRunOnType () {
	return cond.option.runType;
    }

    /**
     *
     * @param id
     */
    public void setReference (FormID id) {
	cond.option.reference = id;
    }

    /**
     *
     * @return
     */
    public FormID getReference () {
	return cond.option.reference;
    }

    /**
     *
     * @return
     */
    public Enum getFunction () {
	return cond.option.script;
    }

    /**
     *
     * @return
     */
    public int getFunctionIndex () {
	return cond.option.index;
    }

    /**
     *
     */
    public enum RunOnType {

	/**
	 *
	 */
	Subject,
	/**
	 *
	 */
	Target,
	/**
	 *
	 */
	Reference,
	/**
	 *
	 */
	CombatTarget,
	/**
	 *
	 */
	LinkedRef,
	/**
	 *
	 */
	QuestAlias,
	/**
	 *
	 */
	PackageData,
	/**
	 *
	 */
	EventData;
    }

    /**
     *
     */
    public enum Operator {

	/**
	 *
	 */
	EqualTo,
	/**
	 *
	 */
	NotEqualTo,
	/**
	 *
	 */
	GreaterThan,
	/**
	 *
	 */
	GreaterThanOrEqual,
	/**
	 *
	 */
	LessThan,
	/**
	 *
	 */
	LessThanOrEqual;
    }

    /**
     * Returns the Script Function associated with an function index (based on Bethesda standards)
     * @param index
     * @return
     */
    static public Enum getScript(Integer index) {
	if (scriptMap.isEmpty()) {
	    for (P_Axis e : P_Axis.values()) {
		scriptMap.put(e.index, e);
	    }

	    for (P_FormID_CastingSource e : P_FormID_CastingSource.values()) {
		scriptMap.put(e.index, e);
	    }

	    for (P_FormID_Int e : P_FormID_Int.values()) {
		scriptMap.put(e.index, e);
	    }

	    for (P_FormID_FormID e : P_FormID_FormID.values()) {
		scriptMap.put(e.index, e);
	    }

	    for (P_CastingSource_FormID e : P_CastingSource_FormID.values()) {
		scriptMap.put(e.index, e);
	    }

	    for (P_FormID e : P_FormID.values()) {
		scriptMap.put(e.index, e);
	    }

	    for (P_Gender e : P_Gender.values()) {
		scriptMap.put(e.index, e);
	    }

	    for (P_CastingSource e : P_CastingSource.values()) {
		scriptMap.put(e.index, e);
	    }

	    for (P_Int_FormID_Int e : P_Int_FormID_Int.values()) {
		scriptMap.put(e.index, e);
	    }

	    for (P_Int_FormID e : P_Int_FormID.values()) {
		scriptMap.put(e.index, e);
	    }

	    for (P_WardState e : P_WardState.values()) {
		scriptMap.put(e.index, e);
	    }

	    for (P_Int e : P_Int.values()) {
		scriptMap.put(e.index, e);
	    }

	    for (P_FormID_String e : P_FormID_String.values()) {
		scriptMap.put(e.index, e);
	    }

	    for (P_FormID_Axis e : P_FormID_Axis.values()) {
		scriptMap.put(e.index, e);
	    }

	    for (P_FormID_CrimeType e : P_FormID_CrimeType.values()) {
		scriptMap.put(e.index, e);
	    }

	    for (P_FormID_Float e : P_FormID_Float.values()) {
		scriptMap.put(e.index, e);
	    }

	    for (P_Int_Int e : P_Int_Int.values()) {
		scriptMap.put(e.index, e);
	    }

	    for (P_String e : P_String.values()) {
		scriptMap.put(e.index, e);
	    }

	    for (P_NoParams e : P_NoParams.values()) {
		scriptMap.put(e.index, e);
	    }
	}
	return scriptMap.get(index);
    }

    /**
     *
     */
    public enum P_Axis {

	/**
	 *
	 */
	GetPos(6),
	/**
	 *
	 */
	GetAngle(8),
	/**
	 *
	 */
	GetStartingPos(10),
	/**
	 *
	 */
	GetStartingAngle(11),
	/**
	 *
	 */
	GetVelocity(446),
	/**
	 *
	 */
	GetPathingTargetOffset(619),
	/**
	 *
	 */
	GetPathingTargetAngleOffset(620),
	/**
	 *
	 */
	GetPathingTargetSpeedAngle(622),
	/**
	 *
	 */
	GetPathingCurrentSpeedAngle(684);
	int index;

	P_Axis(int index) {
	    this.index = index;
	}

	/**
	 *
	 * @param p
	 * @return
	 */
	public ParamType getType(Param p) {
	    switch (p) {
		case One:
		    return ParamType.Axis;
		case Two:
		    return ParamType.NULL;
		case Three:
		    return ParamType.NULL;
		default:
		    return ParamType.NULL;
	    }
	}
    }

    /**
     *
     */
    public enum P_FormID_CastingSource {

	/**
	 *
	 */
	IsCurrentSpell(595);
	int index;

	P_FormID_CastingSource(int index) {
	    this.index = index;
	}

	/**
	 *
	 * @param p
	 * @return
	 */
	public ParamType getType(Param p) {
	    switch (p) {
		case One:
		    return ParamType.FormID;
		case Two:
		    return ParamType.CastingSource;
		case Three:
		    return ParamType.NULL;
		default:
		    return ParamType.NULL;
	    }
	}
    }

    /**
     *
     */
    public enum P_FormID_Int {

	/**
	 *
	 */
	GetStageDone(59),
	/**
	 *
	 */
	IsSceneActionComplete(550);
	int index;

	P_FormID_Int(int index) {
	    this.index = index;
	}

	/**
	 *
	 * @param p
	 * @return
	 */
	public ParamType getType(Param p) {
	    switch (p) {
		case One:
		    return ParamType.FormID;
		case Two:
		    return ParamType.Int;
		case Three:
		    return ParamType.NULL;
		default:
		    return ParamType.NULL;
	    }
	}
    }

    /**
     *
     */
    public enum P_FormID_FormID {

	/**
	 *
	 */
	GetFactionRankDifference(60),
	/**
	 *
	 */
	HasSameEditorLocAsRef(180),
	/**
	 *
	 */
	GetInCellParam(230),
	/**
	 *
	 */
	HasAssociationType(258),
	/**
	 *
	 */
	IsCellOwner(280),
	/**
	 *
	 */
	GetFactionCombatReaction(410),
	/**
	 *
	 */
	IsCloserToAThanB(577),
	/**
	 *
	 */
	GetRefTypeDeadCount(591),
	/**
	 *
	 */
	GetRefTypeAliveCount(592),
	/**
	 *
	 */
	IsInSameCurrentLocAsRef(603),
	/**
	 *
	 */
	GetKeywordDataForLocation(606),
	/**
	 *
	 */
	IsLinkedTo(650);
	int index;

	P_FormID_FormID(int index) {
	    this.index = index;
	}

	/**
	 *
	 * @param p
	 * @return
	 */
	public ParamType getType(Param p) {
	    switch (p) {
		case One:
		    return ParamType.FormID;
		case Two:
		    return ParamType.FormID;
		case Three:
		    return ParamType.NULL;
		default:
		    return ParamType.NULL;
	    }
	}
    }

    /**
     *
     */
    public enum P_CastingSource_FormID {

	/**
	 *
	 */
	SpellHasKeyword(596);
	int index;

	P_CastingSource_FormID(int index) {
	    this.index = index;
	}

	/**
	 *
	 * @param p
	 * @return
	 */
	public ParamType getType(Param p) {
	    switch (p) {
		case One:
		    return ParamType.CastingSource;
		case Two:
		    return ParamType.FormID;
		case Three:
		    return ParamType.NULL;
		default:
		    return ParamType.NULL;
	    }
	}
    }

    /**
     *
     */
    public enum P_FormID {

	/**
	 *
	 */
	GetDistance(1),
	/**
	 *
	 */
	GetActorValue(14),
	/**
	 *
	 */
	GetLineOfSight(27),
	/**
	 *
	 */
	GetInSameCell(32),
	/**
	 *
	 */
	SameFaction(42),
	/**
	 *
	 */
	SameRace(43),
	/**
	 *
	 */
	SameSex(44),
	/**
	 *
	 */
	GetDetected(45),
	/**
	 *
	 */
	GetItemCount(47),
	/**
	 *
	 */
	GetQuestRunning(56),
	/**
	 *
	 */
	GetStage(58),
	/**
	 *
	 */
	GetShouldAttack(66),
	/**
	 *
	 */
	GetInCell(67),
	/**
	 *
	 */
	GetIsClass(68),
	/**
	 *
	 */
	GetIsRace(69),
	/**
	 *
	 */
	GetInFaction(71),
	/**
	 *
	 */
	GetIsID(72),
	/**
	 *
	 */
	GetFactionRank(73),
	/**
	 *
	 */
	GetGlobalValue(74),
	/**
	 *
	 */
	GetDeadCount(84),
	/**
	 *
	 */
	GetHeadingAngle(99),
	/**
	 *
	 */
	IsWeaponSkillType(109),
	/**
	 *
	 */
	IsPlayerInRegion(117),
	/**
	 *
	 */
	GetPCIsClass(129),
	/**
	 *
	 */
	GetPCIsRace(130),
	/**
	 *
	 */
	GetPCInFaction(132),
	/**
	 *
	 */
	GetIsReference(136),
	/**
	 *
	 */
	GetIsCurrentWeather(149),
	/**
	 *
	 */
	GetIsCurrentPackage(161),
	/**
	 *
	 */
	IsCurrentFurnitureRef(162),
	/**
	 *
	 */
	IsCurrentFurnitureObj(163),
	/**
	 *
	 */
	GetTalkedToPCParam(172),
	/**
	 *
	 */
	GetEquipped(182),
	/**
	 *
	 */
	GetPCExpelled(193),
	/**
	 *
	 */
	GetPCFactionMurder(195),
	/**
	 *
	 */
	GetPCEnemyofFaction(197),
	/**
	 *
	 */
	GetPCFactionAttack(199),
	/**
	 *
	 */
	HasMagicEffect(214),
	/**
	 *
	 */
	IsSpellTarget(223),
	/**
	 *
	 */
	GetIsClassDefault(228),
	/**
	 *
	 */
	GetIsUsedItem(246),
	/**
	 *
	 */
	IsScenePlaying(248),
	/**
	 *
	 */
	GetLocationCleared(250),
	/**
	 *
	 */
	HasFamilyRelationship(259),
	/**
	 *
	 */
	HasParentRelationship(261),
	/**
	 *
	 */
	IsWarningAbout(262),
	/**
	 *
	 */
	HasSpell(264),
	/**
	 *
	 */
	GetBaseActorValue(277),
	/**
	 *
	 */
	IsOwner(278),
	/**
	 *
	 */
	GetInWorldspace(310),
	/**
	 *
	 */
	GetPCMiscStat(312),
	/**
	 *
	 */
	GetWithinPackageLocation(325),
	/**
	 *
	 */
	GetInCurrentLoc(359),
	/**
	 *
	 */
	HasLinkedRef(362),
	/**
	 *
	 */
	GetStolenItemValueNoCrime(366),
	/**
	 *
	 */
	IsTalkingActivatorActor(370),
	/**
	 *
	 */
	IsInList(372),
	/**
	 *
	 */
	GetStolenItemValue(373),
	/**
	 *
	 */
	GetCrimeGoldViolent(375),
	/**
	 *
	 */
	GetCrimeGoldNonviolent(376),
	/**
	 *
	 */
	HasShout(378),
	/**
	 *
	 */
	GetHasNote(381),
	/**
	 *
	 */
	IsWeaponInList(398),
	/**
	 *
	 */
	GetRelationshipRank(403),
	/**
	 *
	 */
	IsKiller(408),
	/**
	 *
	 */
	IsKillerObject(409),
	/**
	 *
	 */
	Exists(414),
	/**
	 *
	 */
	GetIsVoiceType(426),
	/**
	 *
	 */
	GetInCurrentLocFormList(444),
	/**
	 *
	 */
	GetInZone(445),
	/**
	 *
	 */
	HasPerk(448),
	/**
	 *
	 */
	GetFactionRelation(449),
	/**
	 *
	 */
	IsLastIdlePlayed(450),
	/**
	 *
	 */
	GetCrimeGold(459),
	/**
	 *
	 */
	IsPlayerGrabbedRef(463),
	/**
	 *
	 */
	GetKeywordItemCount(465),
	/**
	 *
	 */
	GetThreatRatio(477),
	/**
	 *
	 */
	GetIsUsedItemEquipType(479),
	/**
	 *
	 */
	PlayerKnows(493),
	/**
	 *
	 */
	GetPermanentActorValue(494),
	/**
	 *
	 */
	CanPayCrimeGold(497),
	/**
	 *
	 */
	EPAlchemyEffectHasHeyword(501),
	/**
	 *
	 */
	IsCombatTarget(513),
	/**
	 *
	 */
	GetVATSRightAreaFree(515),
	/**
	 *
	 */
	GetVATSLeftAreaFree(516),
	/**
	 *
	 */
	GetVATSBackAreaFree(517),
	/**
	 *
	 */
	GetVATSFrontAreaFree(518),
	/**
	 *
	 */
	GetVATSRightTargetVisible(522),
	/**
	 *
	 */
	GetVATSLeftTargetVisible(523),
	/**
	 *
	 */
	GetVATSBackTargetVisible(524),
	/**
	 *
	 */
	GetVATSFrontTargetVisible(525),
	/**
	 *
	 */
	GetInfamy(533),
	/**
	 *
	 */
	GetInfamyViolent(534),
	/**
	 *
	 */
	GetInfamyNonViolent(535),
	/**
	 *
	 */
	GetQuestCompleted(543),
	/**
	 *
	 */
	GetSpellUsageNum(552),
	/**
	 *
	 */
	HasKeyword(560),
	/**
	 *
	 */
	HasRefType(561),
	/**
	 *
	 */
	LocationHasKeyword(562),
	/**
	 *
	 */
	LocationHasRefType(563),
	/**
	 *
	 */
	GetIsEditorLocation(565),
	/**
	 *
	 */
	GetEquippedShout(579),
	/**
	 *
	 */
	IsNullPackageData(611),
	/**
	 *
	 */
	GetNumericPackageData(612),
	/**
	 *
	 */
	HasAssociationTypeAny(617),
	/**
	 *
	 */
	GetInContainer(624),
	/**
	 *
	 */
	IsLocationLoaded(625),
	/**
	 *
	 */
	GetActorValuePercent(640),
	/**
	 *
	 */
	GetKeywordDataForCurrentLocation(651),
	/**
	 *
	 */
	GetInSharedCrimeFaction(652),
	/**
	 *
	 */
	EPTemperingItemHasKeyword(660),
	/**
	 *
	 */
	ShouldAttackKill(678),
	/**
	 *
	 */
	EPModSkillUsage_IsAdvanceSkill(681),
	/**
	 *
	 */
	WornHasKeyword(682),
	/**
	 *
	 */
	EPModSkillUsage_AdvanceObjectHasKeyword(691),
	/**
	 *
	 */
	EPMagic_SpellHasKeyword(693),
	/**
	 *
	 */
	EPMagic_SpellHasSkill(696),
	/**
	 *
	 */
	IsAttackType(697),
	/**
	 *
	 */
	HasMagicEffectKeyword(699),
	/**
	 *
	 */
	GetShouldHelp(705),
	/**
	 *
	 */
	GetCombatTargetHasKeyword(707),
	/**
	 *
	 */
	SpellHasCastingPerk(713),
	/**
	 *
	 */
	IsHostileToActor(719),
	/**
	 *
	 */
	GetTargetHeight(720),
	/**
	 *
	 */
	WornApparelHasKeywordCount(722);
	int index;

	P_FormID(int index) {
	    this.index = index;
	}

	/**
	 *
	 * @param p
	 * @return
	 */
	public ParamType getType(Param p) {
	    switch (p) {
		case One:
		    return ParamType.FormID;
		case Two:
		    return ParamType.NULL;
		case Three:
		    return ParamType.NULL;
		default:
		    return ParamType.NULL;
	    }
	}
    }

    /**
     *
     */
    public enum P_Gender {

	/**
	 *
	 */
	GetIsSex(70),
	/**
	 *
	 */
	GetPCIsSex(131);
	int index;

	P_Gender(int index) {
	    this.index = index;
	}

	/**
	 *
	 * @param p
	 * @return
	 */
	public ParamType getType(Param p) {
	    switch (p) {
		case One:
		    return ParamType.Gender;
		case Two:
		    return ParamType.NULL;
		case Three:
		    return ParamType.NULL;
		default:
		    return ParamType.NULL;
	    }
	}
    }

    /**
     *
     */
    public enum P_CastingSource {

	/**
	 *
	 */
	HasEquippedSpell(570),
	/**
	 *
	 */
	GetCurrentCastingType(571),
	/**
	 *
	 */
	GetCurrentDeliveryType(572),
	/**
	 *
	 */
	GetEquippedItemType(597),
	/**
	 *
	 */
	GetReplacedItemType(664),
	/**
	 *
	 */
	HasBoundWeaponEquipped(706);
	int index;

	P_CastingSource(int index) {
	    this.index = index;
	}

	/**
	 *
	 * @param p
	 * @return
	 */
	public ParamType getType(Param p) {
	    switch (p) {
		case One:
		    return ParamType.CastingSource;
		case Two:
		    return ParamType.NULL;
		case Three:
		    return ParamType.NULL;
		default:
		    return ParamType.NULL;
	    }
	}
    }

    /**
     *
     */
    public enum P_Int_FormID_Int {

	/**
	 *
	 */
	GetEventData(576);
	int index;

	P_Int_FormID_Int(int index) {
	    this.index = index;
	}

	/**
	 *
	 * @param p
	 * @return
	 */
	public ParamType getType(Param p) {
	    switch (p) {
		case One:
		    return ParamType.Int;
		case Two:
		    return ParamType.FormID;
		case Three:
		    return ParamType.Int;
		default:
		    return ParamType.NULL;
	    }
	}
    }

    /**
     *
     */
    public enum P_Int_FormID {

	/**
	 *
	 */
	HasSameEditorLocAsRefAlias(181),
	/**
	 *
	 */
	GetVATSValue(407),
	/**
	 *
	 */
	GetLocAliasRefTypeDeadCount(600),
	/**
	 *
	 */
	GetLocAliasRefTypeAliveCount(601),
	/**
	 *
	 */
	IsInSameCurrentLocAsRefAlias(604),
	/**
	 *
	 */
	LocAliasIsLocation(605),
	/**
	 *
	 */
	GetKeywordDataForAlias(608),
	/**
	 *
	 */
	LocAliasHasKeyword(610);
	int index;

	P_Int_FormID(int index) {
	    this.index = index;
	}

	/**
	 *
	 * @param p
	 * @return
	 */
	public ParamType getType(Param p) {
	    switch (p) {
		case One:
		    return ParamType.Int;
		case Two:
		    return ParamType.FormID;
		case Three:
		    return ParamType.NULL;
		default:
		    return ParamType.NULL;
	    }
	}
    }

    /**
     *
     */
    public enum P_WardState {

	/**
	 *
	 */
	IsWardState(602);
	int index;

	P_WardState(int index) {
	    this.index = index;
	}

	/**
	 *
	 * @param p
	 * @return
	 */
	public ParamType getType(Param p) {
	    switch (p) {
		case One:
		    return ParamType.WardState;
		case Two:
		    return ParamType.NULL;
		case Three:
		    return ParamType.NULL;
		default:
		    return ParamType.NULL;
	    }
	}
    }

    /**
     *
     */
    public enum P_Int {

	/**
	 *
	 */
	MenuMode(36),
	/**
	 *
	 */
	GetIsUsedItemType(247),
	/**
	 *
	 */
	IsInCombat(289),
	/**
	 *
	 */
	GetInCurrentLocAlias(360),
	/**
	 *
	 */
	IsPlayerActionActive(368),
	/**
	 *
	 */
	IsLimbGone(397),
	/**
	 *
	 */
	GetIsObjectType(432),
	/**
	 *
	 */
	GetIsCreatureType(437),
	/**
	 *
	 */
	GetIsAlignment(473),
	/**
	 *
	 */
	IsInCriticalStage(528),
	/**
	 *
	 */
	GetIsAliasRef(566),
	/**
	 *
	 */
	GetIsEditorLocAlias(567),
	/**
	 *
	 */
	GetLocationAliasCleared(598),
	/**
	 *
	 */
	IsFurnitureAnimType(613),
	/**
	 *
	 */
	IsFurnitureEntryType(614),
	/**
	 *
	 */
	IsLocAliasLoaded(626),
	/**
	 *
	 */
	IsInFurnitureState(644),
	/**
	 *
	 */
	EPModSkillUsage_IsAdvanceAction(692);
	int index;

	P_Int(int index) {
	    this.index = index;
	}

	/**
	 *
	 * @param p
	 * @return
	 */
	public ParamType getType(Param p) {
	    switch (p) {
		case One:
		    return ParamType.Int;
		case Two:
		    return ParamType.NULL;
		case Three:
		    return ParamType.NULL;
		default:
		    return ParamType.NULL;
	    }
	}
    }

    /**
     *
     */
    public enum P_FormID_String {

	/**
	 *
	 */
	GetScriptVariable(53),
	/**
	 *
	 */
	GetQuestVariable(79),
	/**
	 *
	 */
	GetVMQuestVariable(629),
	/**
	 *
	 */
	GetVMScriptVariable(630);
	int index;

	P_FormID_String(int index) {
	    this.index = index;
	}

	/**
	 *
	 * @param p
	 * @return
	 */
	public ParamType getType(Param p) {
	    switch (p) {
		case One:
		    return ParamType.FormID;
		case Two:
		    return ParamType.String;
		case Three:
		    return ParamType.NULL;
		default:
		    return ParamType.NULL;
	    }
	}
    }

    /**
     *
     */
    public enum P_FormID_Axis {

	/**
	 *
	 */
	GetRelativeAngle(584);
	int index;

	P_FormID_Axis(int index) {
	    this.index = index;
	}

	/**
	 *
	 * @param p
	 * @return
	 */
	public ParamType getType(Param p) {
	    switch (p) {
		case One:
		    return ParamType.FormID;
		case Two:
		    return ParamType.Axis;
		case Three:
		    return ParamType.NULL;
		default:
		    return ParamType.NULL;
	    }
	}
    }

    /**
     *
     */
    public enum P_FormID_CrimeType {

	/**
	 *
	 */
	GetCrime(122);
	int index;

	P_FormID_CrimeType(int index) {
	    this.index = index;
	}

	/**
	 *
	 * @param p
	 * @return
	 */
	public ParamType getType(Param p) {
	    switch (p) {
		case One:
		    return ParamType.FormID;
		case Two:
		    return ParamType.CrimeType;
		case Three:
		    return ParamType.NULL;
		default:
		    return ParamType.NULL;
	    }
	}
    }

    /**
     *
     */
    public enum P_FormID_Float {

	/**
	 *
	 */
	GetWithinDistance(639);
	int index;

	P_FormID_Float(int index) {
	    this.index = index;
	}

	/**
	 *
	 * @param p
	 * @return
	 */
	public ParamType getType(Param p) {
	    switch (p) {
		case One:
		    return ParamType.FormID;
		case Two:
		    return ParamType.Float;
		case Three:
		    return ParamType.NULL;
		default:
		    return ParamType.NULL;
	    }
	}
    }

    /**
     *
     */
    public enum P_Int_Int {

	/**
	 *
	 */
	GetPlayerControlsDisabled(98);
	int index;

	P_Int_Int(int index) {
	    this.index = index;
	}

	/**
	 *
	 * @param p
	 * @return
	 */
	public ParamType getType(Param p) {
	    switch (p) {
		case One:
		    return ParamType.Int;
		case Two:
		    return ParamType.Int;
		case Three:
		    return ParamType.NULL;
		default:
		    return ParamType.NULL;
	    }
	}
    }

    /**
     *
     */
    public enum P_String {

	/**
	 *
	 */
	GetGraphVariableFloat(447),
	/**
	 *
	 */
	GetGraphVariableInt(675);
	int index;

	P_String(int index) {
	    this.index = index;
	}

	/**
	 *
	 * @param p
	 * @return
	 */
	public ParamType getType(Param p) {
	    switch (p) {
		case One:
		    return ParamType.String;
		case Two:
		    return ParamType.NULL;
		case Three:
		    return ParamType.NULL;
		default:
		    return ParamType.NULL;
	    }
	}
    }

    /**
     *
     */
    public enum P_NoParams {

	/**
	 *
	 */
	GetWantBlocking(0),
	/**
	 *
	 */
	GetLocked(5),
	/**
	 *
	 */
	GetSecondsPassed(12),
	/**
	 *
	 */
	GetCurrentTime(18),
	/**
	 *
	 */
	GetScale(24),
	/**
	 *
	 */
	IsMoving(25),
	/**
	 *
	 */
	IsTurning(26),
	/**
	 *
	 */
	GetDisabled(35),
	/**
	 *
	 */
	GetDisease(39),
	/**
	 *
	 */
	GetClothingValue(41),
	/**
	 *
	 */
	GetDead(46),
	/**
	 *
	 */
	GetGold(48),
	/**
	 *
	 */
	GetSleeping(49),
	/**
	 *
	 */
	GetTalkedToPC(50),
	/**
	 *
	 */
	GetAlarmed(61),
	/**
	 *
	 */
	IsRaining(62),
	/**
	 *
	 */
	GetAttacked(63),
	/**
	 *
	 */
	GetIsCreature(64),
	/**
	 *
	 */
	GetLockLevel(65),
	/**
	 *
	 */
	IsSnowing(75),
	/**
	 *
	 */
	GetRandomPercent(77),
	/**
	 *
	 */
	GetLevel(80),
	/**
	 *
	 */
	IsRotating(81),
	/**
	 *
	 */
	GetIsAlerted(91),
	/**
	 *
	 */
	IsWeaponMagicOut(101),
	/**
	 *
	 */
	IsTorchOut(102),
	/**
	 *
	 */
	IsShieldOut(103),
	/**
	 *
	 */
	IsFacingUp(106),
	/**
	 *
	 */
	GetKnockedState(107),
	/**
	 *
	 */
	GetWeaponAnimType(108),
	/**
	 *
	 */
	GetCurrentAIPackage(110),
	/**
	 *
	 */
	IsWaiting(111),
	/**
	 *
	 */
	IsIdlePlaying(112),
	/**
	 *
	 */
	IsIntimidatedbyPlayer(116),
	/**
	 *
	 */
	GetActorAggroRadiusViolated(118),
	/**
	 *
	 */
	IsGreetingPlayer(123),
	/**
	 *
	 */
	IsGuard(125),
	/**
	 *
	 */
	HasBeenEaten(127),
	/**
	 *
	 */
	GetStaminaPercentage(128),
	/**
	 *
	 */
	SameFactionAsPC(133),
	/**
	 *
	 */
	SameRaceAsPC(134),
	/**
	 *
	 */
	SameSexAsPC(135),
	/**
	 *
	 */
	IsTalking(141),
	/**
	 *
	 */
	GetWalkSpeed(142),
	/**
	 *
	 */
	GetCurrentAIProcedure(143),
	/**
	 *
	 */
	GetTrespassWarningLevel(144),
	/**
	 *
	 */
	IsTrespassing(145),
	/**
	 *
	 */
	IsInMyOwnedCell(146),
	/**
	 *
	 */
	GetWindSpeed(147),
	/**
	 *
	 */
	GetCurrentWeatherPercent(148),
	/**
	 *
	 */
	IsContinuingPackagePCNear(150),
	/**
	 *
	 */
	GetIsCrimeFaction(152),
	/**
	 *
	 */
	CanHaveFlames(153),
	/**
	 *
	 */
	HasFlames(154),
	/**
	 *
	 */
	GetOpenState(157),
	/**
	 *
	 */
	GetSitting(159),
	/**
	 *
	 */
	GetDayOfWeek(170),
	/**
	 *
	 */
	IsPCSleeping(175),
	/**
	 *
	 */
	IsPCAMurderer(176),
	/**
	 *
	 */
	IsSwimming(185),
	/**
	 *
	 */
	GetAmountSoldStolen(190),
	/**
	 *
	 */
	GetIgnoreCrime(192),
	/**
	 *
	 */
	GetDestroyed(203),
	/**
	 *
	 */
	GetDefaultOpen(215),
	/**
	 *
	 */
	GetAnimAction(219),
	/**
	 *
	 */
	GetVATSMode(224),
	/**
	 *
	 */
	GetPersuasionNumber(225),
	/**
	 *
	 */
	GetVampireFeed(226),
	/**
	 *
	 */
	GetCannibal(227),
	/**
	 *
	 */
	GetClassDefaultMatch(229),
	/**
	 *
	 */
	GetVatsTargetHeight(235),
	/**
	 *
	 */
	GetIsGhost(237),
	/**
	 *
	 */
	GetUnconscious(242),
	/**
	 *
	 */
	GetRestrained(244),
	/**
	 *
	 */
	IsInDialogueWithPlayer(249),
	/**
	 *
	 */
	GetIsPlayableRace(254),
	/**
	 *
	 */
	GetOffersServicesNow(255),
	/**
	 *
	 */
	IsWeaponOut(263),
	/**
	 *
	 */
	IsTimePassing(265),
	/**
	 *
	 */
	IsPleasant(266),
	/**
	 *
	 */
	IsCloudy(267),
	/**
	 *
	 */
	IsSmallBump(274),
	/**
	 *
	 */
	IsHorseStolen(282),
	/**
	 *
	 */
	IsSneaking(286),
	/**
	 *
	 */
	IsRunning(287),
	/**
	 *
	 */
	GetFriendHit(288),
	/**
	 *
	 */
	IsInInterior(300),
	/**
	 *
	 */
	IsWaterObject(304),
	/**
	 *
	 */
	GetPlayerAction(305),
	/**
	 *
	 */
	IsActorUsingATorch(306),
	/**
	 *
	 */
	IsXBox(309),
	/**
	 *
	 */
	GetPairedAnimation(313),
	/**
	 *
	 */
	IsActorAVictim(314),
	/**
	 *
	 */
	GetTotalPersuasionNumber(315),
	/**
	 *
	 */
	GetIdleDoneOnce(318),
	/**
	 *
	 */
	GetNoRumors(320),
	/**
	 *
	 */
	GetCombatState(323),
	/**
	 *
	 */
	IsRidingMount(327),
	/**
	 *
	 */
	IsFleeing(329),
	/**
	 *
	 */
	IsInDangerousWater(332),
	/**
	 *
	 */
	GetIgnoreFriendlyHits(338),
	/**
	 *
	 */
	IsPlayersLastRiddenMount(339),
	/**
	 *
	 */
	IsActor(353),
	/**
	 *
	 */
	IsEssential(354),
	/**
	 *
	 */
	IsPlayerMovingIntoNewSpace(358),
	/**
	 *
	 */
	GetTimeDead(361),
	/**
	 *
	 */
	IsChild(365),
	/**
	 *
	 */
	GetLastPlayerAction(367),
	/**
	 *
	 */
	GetHitLocation(390),
	/**
	 *
	 */
	IsPC1stPerson(391),
	/**
	 *
	 */
	GetCauseofDeath(396),
	/**
	 *
	 */
	IsBribedbyPlayer(402),
	/**
	 *
	 */
	GetGroupMemberCount(415),
	/**
	 *
	 */
	GetGroupTargetCount(416),
	/**
	 *
	 */
	GetPlantedExplosive(427),
	/**
	 *
	 */
	IsScenePackageRunning(429),
	/**
	 *
	 */
	GetHealthPercentage(430),
	/**
	 *
	 */
	GetDialogueEmotion(434),
	/**
	 *
	 */
	GetDialogueEmotionValue(435),
	/**
	 *
	 */
	GetPlayerTeammate(453),
	/**
	 *
	 */
	GetPlayerTeammateCount(454),
	/**
	 *
	 */
	GetActorCrimePlayerEnemy(458),
	/**
	 *
	 */
	IsLeftUp(465),
	/**
	 *
	 */
	GetDestructionStage(470),
	/**
	 *
	 */
	IsProtected(476),
	/**
	 *
	 */
	IsCarryable(487),
	/**
	 *
	 */
	GetConcussed(488),
	/**
	 *
	 */
	GetMapMarkerVisible(491),
	/**
	 *
	 */
	GetKillingBlowLimb(495),
	/**
	 *
	 */
	GetDaysInJail(499),
	/**
	 *
	 */
	EPAlchemyGetMakingPoison(500),
	/**
	 *
	 */
	GetAllowWorldInteractions(503),
	/**
	 *
	 */
	GetLastHitCritical(508),
	/**
	 *
	 */
	GetIsLockBroken(519),
	/**
	 *
	 */
	IsPS3(520),
	/**
	 *
	 */
	IsWin32(521),
	/**
	 *
	 */
	GetXPForNextLevel(530),
	/**
	 *
	 */
	IsGoreDisabled(547),
	/**
	 *
	 */
	GetActorsInHigh(554),
	/**
	 *
	 */
	HasLoaded3D(555),
	/**
	 *
	 */
	IsSprinting(568),
	/**
	 *
	 */
	IsBlocking(569),
	/**
	 *
	 */
	GetAttackState(574),
	/**
	 *
	 */
	IsBleedingOut(580),
	/**
	 *
	 */
	GetMovementDirection(589),
	/**
	 *
	 */
	IsInScene(590),
	/**
	 *
	 */
	GetIsFlying(594),
	/**
	 *
	 */
	GetHighestRelationshipRank(615),
	/**
	 *
	 */
	GetLowestRelationshipRank(616),
	/**
	 *
	 */
	HasFamilyRelationshipAny(618),
	/**
	 *
	 */
	GetPathingTargetSpeed(621),
	/**
	 *
	 */
	GetMovementSpeed(623),
	/**
	 *
	 */
	IsDualCasting(627),
	/**
	 *
	 */
	IsEnteringInteractionQuick(631),
	/**
	 *
	 */
	IsCasting(632),
	/**
	 *
	 */
	GetFlyingState(633),
	/**
	 *
	 */
	IsInFavorState(635),
	/**
	 *
	 */
	HasTwoHandedWeaponEquipped(636),
	/**
	 *
	 */
	IsExitingInstant(637),
	/**
	 *
	 */
	IsInFriendStatewithPlayer(638),
	/**
	 *
	 */
	IsUnique(641),
	/**
	 *
	 */
	GetLastBumpDirection(642),
	/**
	 *
	 */
	GetIsInjured(645),
	/**
	 *
	 */
	GetIsCrashLandRequest(646),
	/**
	 *
	 */
	GetIsHastyLandRequest(647),
	/**
	 *
	 */
	GetBribeSuccess(654),
	/**
	 *
	 */
	GetIntimidateSuccess(655),
	/**
	 *
	 */
	GetArrestedState(656),
	/**
	 *
	 */
	GetArrestingActor(657),
	/**
	 *
	 */
	EPTemperingItemIsEnchanted(659),
	/**
	 *
	 */
	IsAttacking(672),
	/**
	 *
	 */
	IsPowerAttacking(673),
	/**
	 *
	 */
	IsLastHostileActor(674),
	/**
	 *
	 */
	GetCurrentShoutVariation(676),
	/**
	 *
	 */
	GetActivationHeight(680),
	/**
	 *
	 */
	GetPathingCurrentSpeed(683),
	/**
	 *
	 */
	GetNoBleedoutRecovery(694),
	/**
	 *
	 */
	IsAllowedToFly(698),
	/**
	 *
	 */
	IsCommandedActor(700),
	/**
	 *
	 */
	IsStaggered(701),
	/**
	 *
	 */
	IsRecoiling(702),
	/**
	 *
	 */
	IsExitingInteractionQuick(703),
	/**
	 *
	 */
	IsPathing(704),
	/**
	 *
	 */
	GetCombatGroupMemberCount(709),
	/**
	 *
	 */
	IsIgnoringCombat(710),
	/**
	 *
	 */
	GetLightLevel(711),
	/**
	 *
	 */
	IsBeingRidden(714),
	/**
	 *
	 */
	IsUndead(715),
	/**
	 *
	 */
	GetRealHoursPassed(716),
	/**
	 *
	 */
	IsUnlockedDoor(718),
	/**
	 *
	 */
	IsPoison(721),
	/**
	 *
	 */
	GetItemHealthPercent(723),
	/**
	 *
	 */
	EffectWasDualCast(724),
	/**
	 *
	 */
	GetKnockStateEnum(725),
	/**
	 *
	 */
	DoesNotExist(726),
	/**
	 *
	 */
	CanFlyHere(731);
	int index;

	P_NoParams(int index) {
	    this.index = index;
	}

	/**
	 *
	 * @param p
	 * @return
	 */
	public ParamType getType(Param p) {
	    switch (p) {
		case One:
		    return ParamType.NULL;
		case Two:
		    return ParamType.NULL;
		case Three:
		    return ParamType.NULL;
		default:
		    return ParamType.NULL;
	    }
	}
    }
}
