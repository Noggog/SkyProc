/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package skyproc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.DataFormatException;
import lev.LExporter;
import lev.LShrinkArray;
import lev.LChannel;
import skyproc.exceptions.BadParameter;
import skyproc.exceptions.BadRecord;

/**
 * A Magic Effect reference object that is used to represent attached MGEF
 * records on spells.
 *
 * @author Justin Swanson
 */
public class MagicEffectRef extends SubShellBulkType {

    static SubPrototype magicEffProto = new SubPrototype() {
	@Override
	protected void addRecords() {
	    add(new SubForm(Type.EFID));
	    add(new EFIT());
	    add(new SubList<>(new Condition()));
	}
    };

    /**
     * @param magicEffectRef A formID to a MGEF record.
     */
    public MagicEffectRef(FormID magicEffectRef) {
	this();
	subRecords.setSubForm(Type.EFID, magicEffectRef);
    }

    MagicEffectRef() {
	super(magicEffProto, false);
    }

    MagicEffectRef(LShrinkArray in) throws DataFormatException, BadParameter, BadRecord, IOException {
	this();
	parseData(in);
    }

    @Override
    SubRecord getNew(Type type) {
	return new MagicEffectRef();
    }

    @Override
    Boolean isValid() {
	return subRecords.isAnyValid();
    }

    /**
     *
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
	if (obj == null) {
	    return false;
	}
	if (getClass() != obj.getClass()) {
	    return false;
	}
	final MagicEffectRef other = (MagicEffectRef) obj;
	if (!this.getMagicRef().equals(other.getMagicRef())) {
	    return false;
	}
	return true;
    }

    /**
     *
     * @return
     */
    @Override
    public int hashCode() {
	int hash = 7;
	hash = 71 * hash + getMagicRef().hashCode();
	return hash;
    }

    static class EFIT extends SubRecordTyped {

	float magnitude = 0;
	int AOE = 0;
	int duration = 0;

	EFIT() {
	    super(Type.EFIT);
	}

	@Override
	void export(LExporter out, Mod srcMod) throws IOException {
	    super.export(out, srcMod);
	    out.write(magnitude);
	    out.write(AOE);
	    out.write(duration);
	}

	@Override
	void parseData(LChannel in) throws BadRecord, DataFormatException, BadParameter {
	    super.parseData(in);
	    magnitude = in.extractFloat();
	    AOE = in.extractInt(4);
	    duration = in.extractInt(4);
	}

	@Override
	SubRecord getNew(Type type) {
	    return new EFIT();
	}

	@Override
	Boolean isValid() {
	    return true;
	}

	@Override
	int getContentLength(Mod srcMod) {
	    return 12;
	}
    }

    // Get/Set
    /**
     *
     * @param magicRef
     */
    public void setMagicRef(FormID magicRef) {
	subRecords.setSubForm(Type.EFID, magicRef);
    }

    /**
     *
     * @return
     */
    public FormID getMagicRef() {
	return subRecords.getSubForm(Type.EFID).getForm();
    }

    EFIT getEFIT() {
	return (EFIT)subRecords.get(Type.EFIT);
    }

    /**
     *
     * @param magnitude
     */
    public void setMagnitude(float magnitude) {
	getEFIT().magnitude = magnitude;
    }

    /**
     *
     * @return
     */
    public float getMagnitude() {
	return getEFIT().magnitude;
    }

    /**
     *
     * @param aoe
     */
    public void setAreaOfEffect(int aoe) {
	getEFIT().AOE = aoe;
    }

    /**
     *
     * @return
     */
    public int getAreaOfEffect() {
	return getEFIT().AOE;
    }

    /**
     *
     * @param duration
     */
    public void setDuration(int duration) {
	getEFIT().duration = duration;
    }

    /**
     *
     * @return
     */
    public int getDuration() {
	return getEFIT().duration;
    }
}