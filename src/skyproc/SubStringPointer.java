package skyproc;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.zip.DataFormatException;
import lev.LChannel;
import lev.LExporter;
import lev.Ln;
import skyproc.Mod.Mod_Flags;
import skyproc.exceptions.BadParameter;
import skyproc.exceptions.BadRecord;

/**
 *
 * @author Justin Swanson
 */
class SubStringPointer extends SubRecordTyped {

    SubData data;
    SubString text;
    SubStringPointer.Files file;
    boolean forceExport = false;
    static boolean shortNull = true;

    SubStringPointer(String type, SubStringPointer.Files file) {
	super(type);
	data = new SubData(type, new byte[1]);
	text = SubString.getNew(type, true);
	this.file = file;
    }

    @Override
    SubRecord getNew(String type) {
	SubStringPointer out = new SubStringPointer(type, file);
	out.forceExport = forceExport;
	return out;
    }

    void setText(String textIn) {
	text.setString(textIn);
    }

    @Override
    boolean isValid() {
	return (text.isValid() && !text.print().equals("")) || forceExport;
    }

    @Override
    void export(LExporter out, Mod srcMod) throws IOException {
	if (isValid()) {
	    if (srcMod.isFlag(Mod.Mod_Flags.STRING_TABLED)) {
		data.setData(Ln.toByteArray(srcMod.addOutString(text.string, file), 4));
		data.export(out, srcMod);
	    } else {
		if (text.isValid()) {
		    text.export(out, srcMod);
		} else if (forceExport) {
		    data.setData(0, shortNull? 1 : 4); // If short null 1, else 4
		    data.export(out, srcMod);
		}
	    }
	}
    }

    @Override
    void parseData(LChannel in, Mod srcMod) throws BadRecord, DataFormatException, BadParameter {
	data.parseData(in, srcMod);
	if (logging()) {
	    logSync(toString(), "Setting " + toString() + " to : " + Ln.arrayToString(data.getData()));
	}
    }

    @Override
    void fetchStringPointers(Mod srcMod) {
	if (srcMod.isFlag(Mod_Flags.STRING_TABLED)) {
	    Map<SubStringPointer.Files, LChannel> streams = srcMod.stringStreams;
	    if (data.isValid() && streams.containsKey(file)) {
		int index = Ln.arrayToInt(data.getData());
		if (srcMod.stringLocations.get(file).containsKey(index)) {
		    int offset = srcMod.stringLocations.get(file).get(index);
		    LChannel stream = streams.get(file);

		    stream.pos(offset);

		    switch (file) {
			case STRINGS:
			    int input;
			    String string = "";
			    while ((input = stream.read()) != 0) {
				string += (char) input;
			    }
			    text.setString(string);
			    break;
			default:
			    int length = Ln.arrayToInt(stream.extractInts(0, 4));
			    String in = Ln.arrayToString(stream.extractInts(0, length - 1)); // -1 to exclude null end
			    if (!in.equals("")) {
				text.setString(in);
			    }
		    }

		    if (logging() && SPGlobal.debugStringPairing) {
			logSync("", file + " pointer " + Ln.printHex(data.getData(), true, false) + " set to : " + print());
		    }

		} else {
		    if (logging() && SPGlobal.debugStringPairing) {
			boolean nullPtr = true;
			for (byte b : data.getData()) {
			    if (b != 0) {
				nullPtr = false;
				break;
			    }
			}
			if (!nullPtr) {
			    logSync("", file + " pointer " + Ln.printHex(data.getData(), true, false) + " COULD NOT BE PAIRED");
			}
		    }
		    data.setData(0, 1); // Invalidate data to stop export
		}
	    }
	} else {
	    text.setString(Ln.arrayToString(data.getData()));
	}
    }

    @Override
    public String print() {
	if (text.isValid()) {
	    return text.print();
	} else {
	    return "<NO TEXT>";
	}
    }

    @Override
    int getContentLength(Mod srcMod) {
	if (isValid()) {
	    if (srcMod.isFlag(Mod_Flags.STRING_TABLED)) {
		return 4; // length of 4
	    } else if (text.isValid()) {
		return text.getContentLength(srcMod);
	    }
	}

	return shortNull ? 1 : 4; // returning null ptr length
    }

    @Override
    int getTotalLength(Mod srcMod) {
	if (isValid() || forceExport) {
	    return getContentLength(srcMod) + getHeaderLength();
	} else {
	    return 0;
	}
    }

    enum Files {

	STRINGS, ILSTRINGS, DLSTRINGS;
    }

    @Override
    public int hashCode() {
	int hash = 7;
	hash = 97 * hash + Objects.hashCode(this.text);
	return hash;
    }

    @Override
    public boolean equals(Object obj) {
	if (obj == null) {
	    return false;
	}
	if (getClass() != obj.getClass()) {
	    return false;
	}
	final SubStringPointer other = (SubStringPointer) obj;
	if (!Objects.equals(this.text, other.text)) {
	    return false;
	}
	return true;
    }

    
}
