package skyproc;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.zip.DataFormatException;
import lev.*;
import skyproc.MajorRecord.MajorFlags;
import skyproc.SPGlobal.Language;
import skyproc.SubStringPointer.Files;
import skyproc.exceptions.BadMod;
import skyproc.gui.SPProgressBarPlug;

/**
 * Used to import data from files into Mod objects that are ready to be
 * manipulated/exported.
 *
 * @author Justin Swanson
 */
public class SPImporter {

    private static String header = "Importer";

    /**
     * A placeholder constructor not meant to be called.<br> An SPImporter
     * object should only be instantiated as an extended class that overrides
     * the importControl() function.
     */
    public SPImporter() {
    }

    /**
     * Creates a new thread and runs any code input importControl input the
     * background. This is useful for GUI programs where you don't want the
     * program to freeze while it processes. <br> <br> NOTE: You MUST override
     * importControl() with custom code telling the program what you want it to
     * do input the background.
     */
    static public void runBackgroundImport() {
	(new Thread(new StartImportThread())).start();
    }

    private static class StartImportThread implements Runnable {

	@Override
	public void run() {
	}

	public void main(String args[]) {
	    (new Thread(new StartImportThread())).start();
	}
    }

    /**
     * Loads input plugins.txt and reads input the mods the user has active and
     * returns an ArrayList of the ModListings input load order. If a mod being
     * read input fits the criteria of isModToSkip(), then it will be omitted
     * input the results.<br> If any mods on the list are not present input the
     * data folder, they will be skipped.<br><br> If the program cannot locate
     * plugins.txt, it will prompt the user to locate the file themselves. Once
     * they do, it will create a file and use that as a reference for future
     * patch generations.<br><br> Related settings input SPGlobal:<br> -
     * pluginsListPath<br> - pathToData<br> - pluginListBackupPath
     *
     * @see SPGlobal
     * @return An ArrayList of ModListings of all active mods present input the
     * data folder.
     * @throws java.io.IOException
     */
    static public ArrayList<ModListing> getActiveModList() throws java.io.IOException {
	if (SPDatabase.activePlugins.isEmpty()) {
	    SPGlobal.sync(true);
	    SPGlobal.newSyncLog("Get Active Mod List.txt");
	    String header = "IMPORT MODS";
	    BufferedReader ModFile;
	    String dataFolder = SPGlobal.getPluginsTxt();

	    //Open Plugin file
	    ModFile = new BufferedReader(new FileReader(dataFolder));

	    try {
		String line = ModFile.readLine();
		ArrayList<String> lines = new ArrayList<>();
		SPGlobal.logSync(header, "Loading in Active Plugins");
		File pluginName;

		//If Skyrim, add Skyrim.esm and Update.esm, as they
		//are automatically removed from the list, and assumed
		if (SPGlobal.gameName.equals("Skyrim")) {
		    lines.add("Skyrim.esm");
		    lines.add("Update.esm");
		}

		while (line != null) {
		    if (line.indexOf("#") >= 0) {
			line = line.substring(0, line.indexOf("#"));
		    }
		    line = line.trim();
		    if (!line.equals("")) {
			pluginName = new File(SPGlobal.pathToData + line);
			ModListing nextMod = new ModListing(line);
			if (SPGlobal.noModsAfter && nextMod.equals(SPGlobal.getGlobalPatch().getInfo())) {
			    SPGlobal.logSync(header, "Skipping the remaining mods as they were after the patch.");
			    break;
			} else if (!SPGlobal.modsToSkip.contains(nextMod)
				&& !Ln.hasAnyKeywords(line, SPGlobal.modsToSkipStr)
				&& ((SPGlobal.modsWhiteList.isEmpty() && SPGlobal.modsWhiteListStr.isEmpty())
				|| SPGlobal.modsWhiteList.contains(nextMod)
				|| Ln.hasAnyKeywords(line, SPGlobal.modsWhiteListStr))) {
			    if (pluginName.isFile()) {
				if (Ln.indexOfIgnoreCase(lines, line) == -1) {
				    SPGlobal.logSync(header, "Adding mod: " + line);
				    lines.add(line);
				} else if (SPGlobal.logging()) {
				    SPGlobal.logSync(header, "Mod was already added: ", line);
				}
			    } else if (SPGlobal.logging()) {
				SPGlobal.logSync(header, "Mod didn't exist: ", line);
			    }
			} else if (SPGlobal.logging()) {
			    SPGlobal.logSync(header, "Mod was on the list to skip: " + line);
			}
		    }
		    line = ModFile.readLine();
		}

		SPGlobal.sync(false);
		SPDatabase.activePlugins = sortModListings(lines);

	    } catch (java.io.FileNotFoundException e) {
		SPGlobal.logException(e);
		SPGlobal.sync(false);
		throw e;
	    } catch (java.io.IOException e) {
		SPGlobal.logException(e);
		SPGlobal.sync(false);
		throw e;
	    }
	}
	return SPDatabase.activePlugins;
    }

    /**
     * Will scan the data folder and ModListings for all .esp and .esm files,
     * regardless of if they are active. It will return a complete list input
     * load order. If a mod being read input fits the criteria of isModToSkip(),
     * then it will be omitted input the results.<br><br> Related settings input
     * SPGlobal:<br> - pathToData
     *
     * @see SPGlobal
     * @return ArrayList of ModListings of all mods present input the data
     * folder.
     */
    static public ArrayList<ModListing> getModList() {
	SPGlobal.newSyncLog("Get All Present Mod List.txt");
	File directory = new File(SPGlobal.pathToData);
	ArrayList<String> out = new ArrayList<>();
	if (directory.isDirectory()) {
	    File[] files = directory.listFiles();
	    for (File f : files) {
		String name = f.getName();
		if (name.contains(".esp") || name.contains(".esm")) {
		    if (!SPGlobal.modsToSkip.contains(new ModListing(name))) {
			out.add(name);
		    } else if (SPGlobal.logging()) {
			SPGlobal.logSync(header, "Mod was on the list to skip: " + name);
		    }
		}
	    }
	}
	return sortModListings(out);
    }

    static ArrayList<ModListing> sortModListings(ArrayList<String> lines) {
	SPGlobal.sync(true);
	//Read it input
	ArrayList<String> esms = new ArrayList<>();
	ArrayList<String> esps = new ArrayList<>();

	for (String line : lines) {
	    if (line.contains(".esm")) {
		esms.add(line);
	    } else {
		esps.add(line);
	    }
	}

	SPGlobal.flush();

	ArrayList<ModListing> listing = new ArrayList<>();
	for (String m : esms) {
	    listing.add(new ModListing(m));
	}
	for (String m : esps) {
	    listing.add(new ModListing(m));
	}

	if (SPGlobal.logging()) {
	    SPGlobal.logSync(header, "=========  Final sorted load order : ==========");
	    int counter = 0;
	    for (ModListing m : listing) {
		SPGlobal.logSync(header, Ln.prettyPrintHex(counter++) + " Name: " + m.print());
	    }
	}

	SPGlobal.sync(false);
	return listing;
    }

    /**
     * Imports all mods input the user's Data/ folder, no matter if they are
     * currently active or not. Imports all GRUPs currently supported by
     * SkyProc. If a mod being read input fits the criteria of isModToSkip(),
     * then it will be omitted input the results.<br><br> NOTE: It is suggested
     * for speed reasons to only import the GRUP types you are interested input
     * by using other import functions.
     *
     * @return A set of Mods with all their data imported and ready to be
     * manipulated.
     */
    static public Set<Mod> importAllMods() {
	return importAllMods(GRUP_TYPE.values());
    }

    /**
     * Imports all mods input the user's Data/ folder, no matter if they are
     * currently active or not. Imports only GRUPS specified input the
     * parameter. If a mod being read input fits the criteria of isModToSkip(),
     * then it will be omitted input the results.<br><br> Related settings input
     * SPGlobal:<br> - pathToData
     *
     * @see SPGlobal
     * @param grup_targets Any amount of GRUP targets, separated by commas, that
     * you wish to import.
     * @return A set of Mods with specified GRUPs imported and ready to be
     * manipulated.
     */
    static public Set<Mod> importAllMods(GRUP_TYPE... grup_targets) {
	return importMods(getModList(), SPGlobal.pathToData, grup_targets);
    }

    /**
     * Imports all mods input the user's Data/ folder, no matter if they are
     * currently active or not. Imports only GRUPS specified input the
     * parameter. If a mod being read input fits the criteria of isModToSkip(),
     * then it will be omitted input the results.<br><br> Related settings input
     * SPGlobal:<br> - pathToData
     *
     * @see SPGlobal
     * @param grup_targets An arraylist of GRUP_TYPE with the desired types to
     * import
     * @return A set of Mods with specified GRUPs imported and ready to be
     * manipulated.
     */
    static public Set<Mod> importAllMods(ArrayList<GRUP_TYPE> grup_targets) {
	GRUP_TYPE[] tmp = new GRUP_TYPE[0];
	return importMods(getModList(), SPGlobal.pathToData, grup_targets.toArray(tmp));
    }

    /**
     * Loads input plugins.txt and reads input the mods the user has active, and
     * loads only those that are also present input the data folder. Imports all
     * GRUPs currently supported by SkyProc. If a mod being read input fits the
     * criteria of isModToSkip(), then it will be omitted input the
     * results.<br><br> If the program cannot locate plugins.txt, it will prompt
     * the user to locate the file themselves. Once they do, it will create a
     * file and use that as a reference for future patch generations.<br><br>
     * NOTE: It is suggested for speed reasons to only import the GRUP types you
     * are interested input by using other import functions.<br><br> Related
     * settings input SPGlobal:<br> - pluginsListPath<br> - pathToData<br> -
     * pluginListBackupPath
     *
     * @see SPGlobal
     * @return A set of Mods with all their data imported and ready to be
     * manipulated.
     * @throws IOException
     */
    static public Set<Mod> importActiveMods() throws IOException {
	return importActiveMods(GRUP_TYPE.values());
    }

    /**
     * Loads input plugins.txt and reads input the mods the user has active, and
     * loads only those that are also present input the data folder. Imports
     * only GRUPS specified input the parameter. If a mod being read input fits
     * the criteria of isModToSkip(), then it will be omitted input the
     * results.<br><br> If the program cannot locate plugins.txt, it will prompt
     * the user to locate the file themselves. Once they do, it will create a
     * file and use that as a reference for future patch generations.<br><br>
     * Related settings input SPGlobal:<br> - pluginsListPath<br> -
     * pathToData<br> - pluginListBackupPath
     *
     * @see SPGlobal
     * @param grup_targets Any amount of GRUP targets, separated by commas, that
     * you wish to import.
     * @return A set of Mods with specified GRUPs imported and ready to be
     * manipulated.
     * @throws IOException
     */
    static public Set<Mod> importActiveMods(GRUP_TYPE... grup_targets) throws IOException {
	return importMods(getActiveModList(), SPGlobal.pathToData, grup_targets);
    }

    /**
     * Loads input plugins.txt and reads input the mods the user has active, and
     * loads only those that are also present input the data folder. Imports
     * only GRUPS specified input the parameter. If a mod being read input fits
     * the criteria of isModToSkip(), then it will be omitted input the
     * results.<br><br> If the program cannot locate plugins.txt, it will prompt
     * the user to locate the file themselves. Once they do, it will create a
     * file and use that as a reference for future patch generations.<br><br>
     * Related settings input SPGlobal:<br> - pluginsListPath<br> -
     * pathToData<br> - pluginListBackupPath
     *
     * @see SPGlobal
     * @param grup_targets An arraylist of GRUP_TYPE with the desired types to
     * import
     * @return A set of Mods with specified GRUPs imported and ready to be
     * manipulated.
     * @throws IOException
     */
    static public Set<Mod> importActiveMods(ArrayList<GRUP_TYPE> grup_targets) throws IOException {
	GRUP_TYPE[] tmp = new GRUP_TYPE[0];
	return importMods(getActiveModList(), SPGlobal.pathToData, grup_targets.toArray(tmp));
    }

    /**
     * Looks for mods that match the given ModListings inside the data folder.
     * It imports any that are properly located, and loads input only GRUPS
     * specified input the parameter.<br><br> Related settings input
     * SPGlobal:<br> - pathToData<br>
     *
     * @see SPGlobal
     * @param mods ModListings to look for and import from the data folder.
     * @param grup_targets Any amount of GRUP targets, separated by commas, that
     * you wish to import.
     * @return A set of Mods with specified GRUPs imported and ready to be
     * manipulated.
     */
    static public Set<Mod> importMods(ArrayList<ModListing> mods, GRUP_TYPE... grup_targets) {
	return importMods(mods, SPGlobal.pathToData, grup_targets);
    }

    /**
     * Looks for mods that match the given ModListings inside the data folder.
     * It imports any that are properly located, and loads input only GRUPS
     * specified input the parameter.<br><br> Related settings input
     * SPGlobal:<br> - pathToData<br>
     *
     * @see SPGlobal
     * @param mods ModListings to look for and import from the data folder.
     * @param grup_targets An arraylist of GRUP_TYPE with the desired types to
     * import
     * @return A set of Mods with specified GRUPs imported and ready to be
     * manipulated.
     */
    static public Set<Mod> importMods(ArrayList<ModListing> mods, ArrayList<GRUP_TYPE> grup_targets) {
	GRUP_TYPE[] tmp = new GRUP_TYPE[0];
	return importMods(mods, SPGlobal.pathToData, grup_targets.toArray(tmp));
    }

    /**
     * Looks for mods that match the given ModListings inside the data folder.
     * It imports any that are properly located, and loads ALL GRUPs supported
     * by SkyProc.<br><br> NOTE: It is suggested for speed reasons to only
     * import the GRUP types you are interested input by using other import
     * functions.<br><br> Related settings input SPGlobal:<br> - pathToData
     *
     * @see SPGlobal
     * @param mods ModListings to look for and import from the data folder.
     * @return A set of Mods with all GRUPs imported and ready to be
     * manipulated.
     */
    static public Set<Mod> importMods(ArrayList<ModListing> mods) {
	return importMods(mods, SPGlobal.pathToData, GRUP_TYPE.values());
    }

    /**
     * Looks for mods that match the given ModListings input the path specified.
     * It imports any that are properly located, and loads ALL GRUPs supported
     * by SkyProc.<br><br> NOTE: It is suggested for speed reasons to only
     * import the GRUP types you are interested input by using other import
     * functions.
     *
     * @param mods ModListings to look for and import from the data folder.
     * @param path Path from patch location to where to load mods from.
     * @return A set of Mods with all GRUPs imported and ready to be
     * manipulated.
     */
    static public Set<Mod> importMods(ArrayList<ModListing> mods, String path) {
	return importMods(mods, path, GRUP_TYPE.values());
    }

    /**
     * Looks for mods that match the given ModListings input the path specified.
     * It imports any that are properly located, and loads input only GRUPS
     * specified input the parameter.
     *
     * @param mods ModListings to look for and import from the data folder.
     * @param path Path from patch location to where to load mods from.
     * @param grup_targets Any amount of GRUP targets, separated by commas, that
     * you wish to import.
     * @return A set of Mods with specified GRUPs imported and ready to be
     * manipulated.
     */
    static public Set<Mod> importMods(final ArrayList<ModListing> mods, String path, GRUP_TYPE... grup_targets) {

	if (grup_targets.length == 0) {
	    SPGlobal.logMain(header, "Skipping import because requests were empty.");
	    return new HashSet<>(0);
	}

	SPGlobal.sync(true);
	if (SPGlobal.logging()) {
	    SPGlobal.logMain(header, "Starting import of targets: ");
	    String grups = "";
	    for (GRUP_TYPE g : grup_targets) {
		grups += "   " + g.toString() + " ";
	    }
	    SPGlobal.logMain(header, grups);
	    SPGlobal.logMain(header, "In mods: ");
	    for (ModListing m : mods) {
		SPGlobal.logMain(header, "   " + m.print());
	    }

	}
	String header = "Import Mods";
	String debugPath = "Mod Import/";

	Set<Mod> outSet = new TreeSet<>();

	SPProgressBarPlug.setMax(mods.size(), "Importing plugins.");

	for (int i = 0; i < mods.size(); i++) {
	    String mod = mods.get(i).print();
	    SPProgressBarPlug.setStatusNumbered(genStatus(mods.get(i)));
	    if (!SPGlobal.modsToSkip.contains(new ModListing(mod))) {
		SPGlobal.newSyncLog(debugPath + Integer.toString(i) + " - " + mod + ".txt");
		try {
		    outSet.add(importMod(new ModListing(mod), path, true, grup_targets));
		} catch (BadMod ex) {
		    SPGlobal.logError(header, "Skipping a bad mod: " + mod);
		    SPGlobal.logError(header, "  " + ex.toString());
		} catch (Exception e) {
		    SPGlobal.logError(header, "Exception occured while importing mod : " + mod);
		    SPGlobal.logError(header, "  Message: " + e);
		    SPGlobal.logError(header, "  Stack: ");
		    for (StackTraceElement s : e.getStackTrace()) {
			SPGlobal.logError(header, "  " + s.toString());
		    }
		}
	    } else {
		SPProgressBarPlug.setStatusNumbered(genStatus(mods.get(i)) + ": Skipped!");
	    }
	    SPProgressBarPlug.incrementBar();
	}

	if (SPGlobal.logging()) {
	    SPGlobal.logSync(header, "Done Importing Mods.");
	    SPGlobal.logMain(header, "Done Importing Mods.");
	}
	SPGlobal.sync(false);
	return outSet;
    }

    /**
     * Looks for a mod matching the ModListing inside the given path. If
     * properly located, it imports only GRUPS specified input the parameter.
     *
     * @param listing Mod name and suffix to look for.
     * @param path Path to look for the mod data.
     * @param grup_targets Any amount of GRUP targets, separated by commas, that
     * you wish to import.
     * @return A mod with the specified GRUPs imported and ready to be
     * manipulated.
     * @throws BadMod If SkyProc runs into any unexpected data structures, or
     * has any error importing a mod at all.
     */
    static public Mod importMod(ModListing listing, String path, GRUP_TYPE... grup_targets) throws BadMod {
	return importMod(listing, path, true, grup_targets);
    }

    /**
     * Looks for a mod matching the ModListing inside the given path. If
     * properly located, it imports only GRUPS specified input the parameter.
     *
     * @param listing Mod name and suffix to look for.
     * @param path Path to look for the mod data.
     * @param grup_targets An ArrayList of GRUP targets that you wish to import.
     * @return A mod with the specified GRUPs imported and ready to be
     * manipulated.
     * @throws BadMod If SkyProc runs into any unexpected data structures, or
     * has any error importing a mod at all.
     *
     * public Mod importMod(ModListing listing, String path,
     * ArrayList<GRUP_TYPE> grup_targets) throws BadMod { GRUP_static Type[]
     * types = new GRUP_TYPE[grup_targets.size()]; types =
     * grup_targets.toArray(types); return importMod(listing, path, types); }
     */
    static Mod importMod(ModListing listing, String path, Boolean addtoDb, GRUP_TYPE... grup_targets) throws BadMod {
	try {

	    SPGlobal.logSync(header, "Opening filestream to mod: " + listing.print());
	    RecordFileChannel input = new RecordFileChannel(path + listing.print());
	    Mod plugin = new Mod(listing, extractHeaderInfo(input));
	    plugin.input = input;
	    if (SPGlobal.streamMode) {
		plugin.input = input;
	    }

	    if (plugin.isFlag(Mod.Mod_Flags.STRING_TABLED)) {
		importStringLocations(plugin);
	    }

	    GRUPIterator iter = new GRUPIterator(grup_targets, input);
	    while (iter.hasNext()) {
		String result = iter.loading();
		SPProgressBarPlug.setStatusNumbered(genStatus(listing) + ": " + result);
		SPGlobal.logSync(header, "================== Loading in GRUP " + result + ": ", plugin.getName(), " ===================");
		plugin.parseData(result, iter.next());
		SPGlobal.flush();
	    }

	    SPProgressBarPlug.setStatusNumbered(genStatus(listing) + ": Fetching Strings");
	    plugin.fetchStringPointers();

	    if (addtoDb) {
		SPGlobal.getDB().add(plugin);
	    }

	    if (!SPGlobal.streamMode) {
		input.close();
	    }
	    SPProgressBarPlug.setStatusNumbered(genStatus(listing) + ": Done");

	    return plugin;
	} catch (Exception e) {
	    SPGlobal.logException(e);
	    throw new BadMod("Ran into an exception, check SPGlobal.logs for more details.");
	}

    }

    static public DirtyParsingIterator getSubRecordsInGRUPs(String typeString, String... grups) {
	return new DirtyParsingIterator(typeString, grups);
    }

    static public DirtyParsingIterator getSubRecordsInGRUPs(ModListing targetMod, String typeString, String... grups) {
	return new DirtyParsingIterator(targetMod, typeString, grups);
    }

    static class GRUPIterator implements Iterator<RecordShrinkArray> {

	LFileChannel input;
	ArrayList<String> targets;
	String loading;

	GRUPIterator() {
	    targets = new ArrayList<>(0);
	}

	GRUPIterator(GRUP_TYPE[] grup_targets, LFileChannel input) {
	    ArrayList<GRUP_TYPE> tmp = new ArrayList<>(Arrays.asList(grup_targets));
	    targets = new ArrayList<>(tmp.size());
	    for (GRUP_TYPE g : tmp) {
		targets.add(g.toString());
	    }
	    this.input = input;
	}

	@Override
	public boolean hasNext() {
	    if (targets.isEmpty()) {
		return false;
	    }
	    try {
		loading = scanToGRUPStart(input, targets);
		targets.remove(loading);
		return !"NULL".equals(loading);
	    } catch (IOException ex) {
		SPGlobal.logException(ex);
		return false;
	    }
	}

	@Override
	public RecordShrinkArray next() {
	    RecordShrinkArray out;
	    try {
		out = extractGRUPData(input);
	    } catch (IOException ex) {
		SPGlobal.logException(ex);
		out = new RecordShrinkArray();
	    }
	    return out;
	}

	public String loading() {
	    return loading;
	}

	@Override
	public void remove() {
	}
    }

    public static class DirtyParsingIterator implements Iterator<RecordShrinkArray> {

	String typeString;
	ArrayList<String> grups;
	LChannel input;
	LFileChannel fileInput;
	LShrinkArray uncompressed = new LShrinkArray(new byte[0]);
	String inputStr = "";
	String majorRecordType = "";
	RecordShrinkArray next;
	ArrayList<ModListing> activeMods;
	ModListing activeMod;

	DirtyParsingIterator(String typeString, String[] grups) {
	    init(typeString, grups);
	    try {
		activeMods = SPImporter.getActiveModList();
	    } catch (IOException ex) {
		activeMods = new ArrayList<>();
		SPGlobal.logException(ex);
	    }
	    switchToNextMod();
	}

	DirtyParsingIterator(ModListing mod, String typeString, String[] grups) {
	    init(typeString, grups);
	    activeMods = new ArrayList<>();
	    activeMods.add(mod);
	    switchToNextMod();
	}

	void init(String typeString, String[] grupTypes) {
	    this.typeString = typeString;
	    grups = new ArrayList<>(Arrays.asList(grupTypes));
	}

	public ModListing activeMod () {
	    return activeMod;
	}

	@Override
	public boolean hasNext() {
	    grabNext();
	    return next != null;
	}

	@Override
	public RecordShrinkArray next() {
	    return next;
	}

	void grabNext() {

	    while (fileInput.available() >= 4 || uncompressed.available() >= 4) {
		if (uncompressed.available() >= 4) {
		    input = uncompressed;
		} else {
		    input = fileInput;
		}
		inputStr = input.extractString(0, 4);
		if ("TES4".equals(inputStr)) {
		    input.skip(input.extractInt(4) + 16);
		} else if ("GRUP".equals(inputStr)) {
		    int size = input.extractInt(4);
		    String tmpMajorType = input.extractString(0, 4);
		    if (grups.contains(tmpMajorType)) {
			input.skip(12);
			majorRecordType = tmpMajorType;
		    } else if (!tmpMajorType.matches("[a-zA-Z0-9_]*")) {
			input.skip(12);
		    } else {
			input.skip(size - 12);
		    }
		} else if (majorRecordType.equals(inputStr)
			|| "REFR".equals(inputStr)
			|| "ACHR".equals(inputStr)) {
		    // If major record is target type
		    if (typeString.equals(inputStr)) {
			next = new RecordShrinkArray(input.extract(input.extractInt(4) + 16));
			return;
		    }

		    // Uncompress if compressed
		    int size = input.extractInt(4);
		    LFlags flags = new LFlags(input.extract(4));
		    input.skip(12);
		    if (flags.get(MajorFlags.Compressed.value)) {
			uncompressed = new LShrinkArray(input.extract(size));
			try {
			    uncompressed = uncompressed.correctForCompression();
			} catch (DataFormatException ex) {
			    SPGlobal.logException(ex);
			}
		    }

		} else if (typeString.equals(inputStr)) {
		    // Found target type
		    next = new RecordShrinkArray(input.extract(input.extractInt(2)));
		    return;
		} else {
		    // Skip Subrecord
		    input.skip(input.extractInt(2));
		}
	    }

	    if (switchToNextMod()) {
		grabNext();
	    }
	}

	boolean switchToNextMod() {
	    if (fileInput != null) {
		fileInput.close();
	    }
	    // If we have mods left, switch to it.
	    if (activeMods.size() > 0) {
		fileInput = new LFileChannel(SPGlobal.pathToData + activeMods.get(0).print());
		activeMod = activeMods.get(0);
		activeMods.remove(0);
		return true;
	    } else {
		next = null;
	    }
	    return false;
	}

	@Override
	public void remove() {
	}
    }

    static ByteBuffer extractHeaderInfo(LFileChannel in) {
	if (Ln.arrayToString(in.extractInts(0, 4)).equals("TES4")) {
	    int size = Ln.arrayToInt(in.extractInts(0, 4)) + 24;  // +24 for TES4 extra info
	    in.skip(-8); // To start of TES4 header
	    return in.extractByteBuffer(0, size);
	}
	return ByteBuffer.allocate(0);
    }

    static void importStringLocations(Mod mod) {
	String header = "Importing Strings";
	if (SPGlobal.logging()) {
	    SPGlobal.logSync(header, "Importing Strings");
	}
	for (Files f : SubStringPointer.Files.values()) {
	    try {
		importStringLocations(mod, f);
	    } catch (Exception e) {
		SPGlobal.logError(header, "Error Importing Strings " + f + ": " + e);
	    }
	}
    }

    static void importStringLocations(Mod plugin, SubStringPointer.Files file) throws FileNotFoundException, IOException, DataFormatException {

	ArrayList<Language> languageList = new ArrayList<>();
	languageList.add(SPGlobal.language);
	languageList.addAll(Arrays.asList(Language.values()));
	LShrinkArray in = null;
	int numRecords = 0;
	int recordsSize = 0;

	for (Language l : languageList) {
	    String strings = getStringFilePath(plugin, l, file);
	    File stringsFile = new File(SPGlobal.pathToData + strings);

	    // Open file
	    if (stringsFile.isFile()) {
		LFileChannel istream = new LFileChannel(stringsFile);
		// Read header
		numRecords = istream.extractInt(0, 4);
		recordsSize = numRecords * 8 + 8;
		in = new LShrinkArray(istream.extractByteBuffer(4, recordsSize));
	    } else if (BSA.hasBSA(plugin)) {
		//In BSA
		BSA bsa = BSA.getBSA(plugin);
		bsa.loadFolders();
		if (!bsa.hasFile(strings)) {
		    continue;
		}
		in = bsa.getFile(strings);
		numRecords = in.extractInt(4);
		recordsSize = numRecords * 8 + 8;
		//Skip bytes 4-8
		in.skip(4);
	    }

	    // Found strings, read entry pairs
	    if (in != null) {
		plugin.language = l;
		for (int i = 0; i < numRecords; i++) {
		    plugin.stringLocations.get(file).put(in.extractInt(4),
			    in.extractInt(4) + recordsSize);
		}
		break;
	    }
	}

	if (in == null) {
	    SPGlobal.logError(header, plugin.toString() + " did not have Strings files (loose or in BSA).");
	} else {
	    SPGlobal.logSync(header, "Loaded " + file + " from language: " + plugin.language);
	}
    }

    static String getStringFilePath(Mod plugin, Language l, SubStringPointer.Files file) {
	return "Strings\\" + plugin.getName().substring(0, plugin.getName().indexOf(".es")) + "_" + l + "." + file;
    }

    static String scanToGRUPStart(LFileChannel in, ArrayList<String> target) throws java.io.IOException {
	String type;
	String intro;
	int size;

	while (in.available() >= 12) {
	    intro = Ln.arrayToString(in.getBytes(0, 4));
	    if ("TES4".equals(intro)) {
		extractHeaderInfo(in);
	    }
	    size = Ln.arrayToInt(in.extractInts(4, 4));
	    type = Ln.arrayToString(in.extractInts(0, 4));
	    for (String t : target) {
		if (t.equals(type)) {
		    in.skip(-12); // Go to start of GRUP
		    return type;
		}
	    }
	    // else skip GRUP
	    in.skip(size - 12);  // -12 for parts already read input
	}

	return "NULL";
    }

    static RecordShrinkArray extractGRUPData(LFileChannel in) throws IOException {
	return new RecordShrinkArray(in, getGRUPsize(in));
    }

    static int getGRUPsize(LFileChannel in) {
	int size = Ln.arrayToInt(in.extractInts(4, 4));
	if (SPGlobal.logging()) {
	    SPGlobal.logSync(header, "Extract GRUP size: " + Ln.prettyPrintHex(size));
	}
	in.skip(-8); // Back to start of GRUP
	return size;
    }

    static private String genStatus(ModListing mod) {
	return "Importing " + mod.print();
    }
}
