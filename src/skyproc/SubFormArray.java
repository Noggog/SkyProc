/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package skyproc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.zip.DataFormatException;
import lev.LExporter;
import lev.Ln;
import lev.LChannel;
import skyproc.exceptions.BadParameter;
import skyproc.exceptions.BadRecord;

/**
 *
 * @author Justin Swanson
 */
class SubFormArray extends SubRecordTyped implements Iterable<FormID> {

    ArrayList<FormID> IDs;

    public SubFormArray(Type type_, int size) {
	super(type_);
	IDs = new ArrayList<>(size);
	for (int i = 0; i < size; i++) {
	    IDs.add(new FormID());
	}
    }

    @Override
    SubRecord getNew(Type type) {
	return new SubFormArray(type, 0);
    }

    @Override
    void export(LExporter out, Mod srcMod) throws IOException {
	super.export(out, srcMod);
	if (isValid()) {
	    for (FormID ID : IDs) {
		out.write(ID.getInternal(true), 4);
	    }
	}
    }

    @Override
    void parseData(LChannel in) throws BadRecord, DataFormatException, BadParameter {
	super.parseData(in);
	int size = IDs.size();
	if (size != 0) {
	    for (int i = 0; i < size; i++) {
		setIth(i, in.extract(4));
	    }
	} else {
	    while (!in.isDone()) {
		FormID id = new FormID();
		id.setInternal(in.extract(4));
		add(id);
	    }
	}
    }

    void setIth(int i, byte[] in) {
	if (logging()) {
	    logSync(toString(), "Setting " + toString() + " FormID[" + i + "]: " + Ln.printHex(in, false, true));
	}
	IDs.get(i).setInternal(in);
    }

    int size() {
	return IDs.size();
    }

    @Override
    ArrayList<FormID> allFormIDs() {
	return new ArrayList<>(IDs);
    }

    @Override
    Boolean isValid() {
	for (FormID ID : IDs) {
	    if (ID.isValid()) {
		return true;
	    }
	}
	return false;
    }

    @Override
    int getContentLength(Mod srcMod) {
	return IDs.size() * 4;
    }

    @Override
    public Iterator<FormID> iterator() {
	return IDs.iterator();
    }

    public boolean remove(FormID id) {
	if (IDs.contains(id)) {
	    IDs.remove(id);
	    return true;
	} else {
	    return false;
	}
    }

    public void add(FormID id) {
	IDs.add(id);
    }

    public boolean containedIn(SubFormArray in) {
	for (FormID id : IDs) {
	    if (!in.IDs.contains(id)) {
		return false;
	    }
	}
	return true;
    }

    @Override
    public boolean equals(Object obj) {
	if (obj == null) {
	    return false;
	}
	if (getClass() != obj.getClass()) {
	    return false;
	}
	final SubFormArray other = (SubFormArray) obj;
	if (this.IDs != other.IDs && (this.IDs == null || !this.IDs.equals(other.IDs))) {
	    return false;
	}
	return true;
    }

    public void clear() {
	IDs.clear();
    }

    @Override
    public int hashCode() {
	int hash = 3;
	hash = 17 * hash + (this.IDs != null ? this.IDs.hashCode() : 0);
	return hash;
    }
}
