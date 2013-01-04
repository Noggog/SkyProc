/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package skyproc;

import java.awt.Color;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JFrame;
import lev.Ln;
import lev.gui.LSaveFile;
import skyproc.gui.SPMainMenuPanel;
import skyproc.gui.SUM;
import skyproc.gui.SUMprogram;

/**
 *
 * @author Justin Swanson
 */
public class SUMMergerProgram implements SUM {

    @Override
    public String getName() {
	return "SUM Merger";
    }

    @Override
    public GRUP_TYPE[] dangerousRecordReport() {
	return new GRUP_TYPE[0];
    }

    @Override
    public GRUP_TYPE[] importRequests() {
	return new GRUP_TYPE[0];
    }

    @Override
    public boolean importAtStart() {
	return false;
    }

    @Override
    public boolean hasStandardMenu() {
	return false;
    }

    @Override
    public SPMainMenuPanel getStandardMenu() {
	throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean hasCustomMenu() {
	return false;
    }

    @Override
    public JFrame openCustomMenu() {
	throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean hasLogo() {
	return true;
    }

    @Override
    public URL getLogo() {
	return SUMprogram.class.getResource("SUM program.png");
    }

    @Override
    public boolean hasSave() {
	return false;
    }

    @Override
    public LSaveFile getSave() {
	throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getVersion() {
	return "1.0";
    }

    @Override
    public ModListing getListing() {
	return new ModListing("SUM.esp");
    }

    @Override
    public Mod getExportPatch() {
	Mod patch = new Mod(getListing());
	patch.setFlag(Mod.Mod_Flags.STRING_TABLED, false);
	patch.setAuthor("Leviathan1753 and friends");
	return patch;
    }

    @Override
    public Color getHeaderColor() {
	return Color.BLACK;
    }

    @Override
    public boolean needsPatching() {
	return true;
    }

    @Override
    public String description() {
	return "Merges SkyProc patchers into one patch.";
    }

    @Override
    public ArrayList<ModListing> requiredMods() {
	return new ArrayList<>(0);
    }

    @Override
    public void onStart() throws Exception {
	SPGlobal.debugConsistencyImport = true;
    }

    @Override
    public void onExit(boolean patchWasGenerated) throws Exception {
    }

    @Override
    public void runChangesToPatch() throws Exception {
	// Get SUM patch names
	ArrayList<String> SUMpatchStrings = Ln.loadFileToStrings(SUMprogram.getSUMPatchList(), false);
	ArrayList<ModListing> SUMpatches = new ArrayList<>(SUMpatchStrings.size());
	for (String modStr : SUMpatchStrings) {
	    SUMpatches.add(new ModListing(modStr));
	}

	// Import SUM patches
	SPImporter importer = new SPImporter();
	importer.importMods(SUMpatches);
	Mod source = new Mod("MergerTemporary", false);
	source.addAsOverrides(SPGlobal.getDB());

	Map<FormID, FormID> origToNew = new HashMap<>();
	for (GRUP<MajorRecord> g : source) {
	    for (MajorRecord m : g.getRecords()) {
		if (SUMpatches.contains(m.getFormMaster())) {
		    // If record was created by SkyProc, make it originate from merger
		    MajorRecord copy = SPGlobal.getGlobalPatch().makeCopy(m, m.getEDID());
		    origToNew.put(m.getForm(), copy.getForm());
		} else {
		    // If non-skyproc override record, just add it
		    SPGlobal.getGlobalPatch().addRecord(m);
		}
	    }
	}
	
	// Replace all formID references to new IDs
	ArrayList<FormID> allFormIDs = SPGlobal.getGlobalPatch().allFormIDs();
	for (FormID id : allFormIDs) {
	    FormID newID = origToNew.get(id);
	    if (newID != null) {
		id.form = newID.form;
		id.master = newID.master;
	    } else if (id.getMaster().toString().equals("Automatic Variants.esp")) {
		int wer = 23;
	    }
	}
	
	// Remove SkyProc patches from master list
	for (Mod m : SPGlobal.getDB()) {
	    SPGlobal.getGlobalPatch().tes.getMasters().remove(m.getInfo());
	}
    }
}
