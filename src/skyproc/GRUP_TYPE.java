package skyproc;

/**
 * Types of GRUP records that skyproc can currently import.
 * @see SPImporter
 * @author Justin Swanson
 */
public enum GRUP_TYPE {

    /**
     * Keywords
     */
    KYWD,
    /**
     * Texture Sets
     */
    TXST,
    /**
     * Races
     */
    RACE,
    /**
     * Magic Effects
     */
    MGEF,
    /**
     * Spells
     */
    SPEL,
    /**
     * Armors
     */
    ARMO,
    /**
     * Ingredients
     */
    INGR,
    /**
     * Alchemy
     */
    ALCH,
    /**
     * Weapons
     */
    WEAP,
    /**
     * Non-Player Characters (Actors)
     */
    NPC_,
    /**
     * Leveled Lists
     */
    LVLN,
    /**
     * Image Spaces
     */
    IMGS,
    /**
     * Form Lists
     */
    FLST,
    /**
     * Perks
     */
    PERK,
    /**
     * Armatures
     */
    ARMA;

    static GRUP_TYPE toRecord (Enum e) {
	return GRUP_TYPE.valueOf(e.toString());
    }
}