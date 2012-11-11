/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package skyproc;

import lev.LChannel;

/**
 *
 * @author Justin Swanson
 */
public abstract class SubShellBulkNumber extends SubShell {

    int numForced;

    SubShellBulkNumber(Type type_, int numForced) {
	super(type_);
	this.numForced = numForced;
    }

    /**
     *
     * @param in
     * @return
     */
    @Override
    public int getRecordLength(LChannel in) {
	int size = 0;
	for (int i = 0 ; i < numForced ; i++) {
	    int newSize = super.getRecordLength(in);
	    size += newSize;
	    in.skip(newSize);
	}
	in.pos(in.pos() - size);
	return size;
    }
}
