/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package skyproc.gui;

import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import lev.debug.LDebug;
import lev.gui.*;
import skyproc.GRUP_TYPE;
import skyproc.SPGlobal;
import skyproc.SPImporter;

/**
 *
 * @author Justin Swanson
 */
public class SPComplexGUI extends JFrame {

    static SPComplexGUI singleton = null;
    public final static Rectangle fullDimensions = new Rectangle(0, 0, 950, 632);
    public final static Rectangle leftDimensions = new Rectangle(0, 0, 299, fullDimensions.height - 28); // For status update
    public final static Rectangle middleDimensions = new Rectangle(leftDimensions.x + leftDimensions.width + 7, 0, 330, fullDimensions.height);
    public final static Rectangle rightDimensions = new Rectangle(middleDimensions.x + middleDimensions.width + 7, 0, 305, fullDimensions.height);
    public final static Rectangle middleRightDimensions = new Rectangle(leftDimensions.x + leftDimensions.width + 7, 0, middleDimensions.width + rightDimensions.width + 7, fullDimensions.height);
    public final static Rectangle middleLeftDimensions = new Rectangle(0, 0, leftDimensions.width + middleDimensions.width, middleDimensions.height);
    static final Color light = new Color(238, 233, 204);
    static final Color lightGray = new Color(190, 190, 190);
    static final Color darkGray = new Color(110, 110, 110);
    static final Color lightred = Color.red;
    static public boolean importAtStart = false;
    static SUM hook;
    static final String header = "SUM";
    public static Thread parser;
    static boolean imported = false;
    static boolean exitRequested = false;
    public static LProgressBarFrame progress = new LProgressBarFrame(
	    new Font("SansSerif", Font.PLAIN, 12), Color.GRAY,
	    new Font("SansSerif", Font.PLAIN, 10), Color.lightGray);
    public static LHelpPanel helpPanel = new LHelpPanel(rightDimensions, new Font("Serif", Font.BOLD, 25), light, lightGray, true, 10);
    // Non static
    static LImagePane backgroundPanel;
    LLabel willMakePatch;
    LImagePane skyProcLogo;
    JTextArea statusUpdate;
    LLabel versionNum;

    public SPComplexGUI() {
	super("Automatic Variants");
    }

    void addComponents() {
	try {

	    backgroundPanel = new LImagePane(SPComplexGUI.class.getResource("background.jpg"));
	    super.add(backgroundPanel);

	    willMakePatch = new LLabel("A patch will be generated upon exit.", new Font("SansSerif", Font.PLAIN, 10), Color.GRAY);
	    willMakePatch.setLocation(backgroundPanel.getWidth() - willMakePatch.getWidth() - 7, 5);
	    backgroundPanel.add(willMakePatch);

	    progress.addWindowListener(new WindowListener() {

		@Override
		public void windowClosed(WindowEvent arg0) {
		}

		@Override
		public void windowActivated(WindowEvent arg0) {
		}

		@Override
		public void windowClosing(WindowEvent arg0) {
		    if (progress.closeOp == JFrame.DISPOSE_ON_CLOSE) {
			SPGlobal.log(header, "Progress bar window closing");
			exitProgram();
		    }
		}

		@Override
		public void windowDeactivated(WindowEvent arg0) {
		}

		@Override
		public void windowDeiconified(WindowEvent arg0) {
		}

		@Override
		public void windowIconified(WindowEvent arg0) {
		}

		@Override
		public void windowOpened(WindowEvent arg0) {
		}
	    });
	    progress.setGUIref(singleton);

	    statusUpdate = new JTextArea();
	    statusUpdate.setSize(250, 18);
	    statusUpdate.setLocation(5, getFrameHeight() - statusUpdate.getHeight());
	    statusUpdate.setForeground(Color.LIGHT_GRAY);
	    statusUpdate.setOpaque(false);
	    statusUpdate.setText("Started application");
	    statusUpdate.setEditable(false);
	    statusUpdate.setVisible(true);
	    backgroundPanel.add(statusUpdate);

	    skyProcLogo = new LImagePane(SPDefaultGUI.class.getResource("SkyProc Logo Small.png"));
	    skyProcLogo.setLocation(5, statusUpdate.getY() - skyProcLogo.getHeight() - 5);
	    backgroundPanel.add(skyProcLogo);

	    helpPanel.setBounds(rightDimensions);
	    backgroundPanel.add(helpPanel);

	    SPProgressBarPlug.progress = new AVProgress();

	    singleton.setVisible(true);

	} catch (IOException ex) {
	    SPGlobal.logException(ex);
	}

    }

    public static void open(final SUM hook) {
	SwingUtilities.invokeLater(new Runnable() {

	    @Override
	    public void run() {
		if (singleton == null) {
		    SPComplexGUI.hook = hook;
		    singleton = new SPComplexGUI();
		    singleton.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		    singleton.setResizable(false);
		    singleton.setSize(954, 658);
		    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		    singleton.setLocation(dim.width / 2 - singleton.getWidth() / 2, dim.height / 2 - singleton.getHeight() / 2);
		    singleton.setLayout(null);
		    singleton.addWindowListener(new WindowListener() {

			@Override
			public void windowClosed(WindowEvent arg0) {
			}

			@Override
			public void windowActivated(WindowEvent arg0) {
			}

			@Override
			public void windowClosing(WindowEvent arg0) {
			    closingGUIwindow();
			}

			@Override
			public void windowDeactivated(WindowEvent arg0) {
			}

			@Override
			public void windowDeiconified(WindowEvent arg0) {
			}

			@Override
			public void windowIconified(WindowEvent arg0) {
			}

			@Override
			public void windowOpened(WindowEvent arg0) {
			}
		    });
		    singleton.addComponents();

		    if (hook.hasSave()) {
			hook.getSave().init();
		    }

		    if (hook.importAtStart()) {
			runThread();
		    }
		}

		helpPanel.setHeaderColor(hook.getHeaderColor());

		if (hook.hasStandardMenu()) {
		    singleton.add(hook.getStandardMenu());
		}
	    }
	});
    }

    int getFrameHeight() {
	return this.getHeight() - 28;
    }

    static private void closingGUIwindow() {
	SPGlobal.log(header, "Window Closing.");

	progress.setExitOnClose();
	progress.open(new ChangeListener() {

	    @Override
	    public void stateChanged(ChangeEvent e) {
		exitProgram();
	    }
	});
	exitRequested = true;
	runThread();
    }

    static public void exitProgram() {
	SPGlobal.log(header, "Exit requested.");
	hook.getSave().saveToFile();
	LDebug.wrapUpAndExit();
    }

    static class ProcessingThread implements Runnable {

	@Override
	public void run() {
	    SPGlobal.log("START IMPORT THREAD", "Starting of process thread.");
	    try {
		if (!imported) {
		    imported = true;
		    SPGlobal.setGlobalPatch(hook.getExportPatch());
		    SPImporter importer = new SPImporter();
		    importer.importActiveMods(hook.importRequests());
		    SPProgressBarPlug.progress.setStatus("Done importing.");
		}
		if (exitRequested) {
		    hook.runChangesToPatch();
		}
	    } catch (Exception e) {
		System.err.println(e.toString());
		SPGlobal.logException(e);
		JOptionPane.showMessageDialog(null, "There was an exception thrown during program execution: '" + e + "'  Check the debug logs.");

		// if exception occurs
		exitProgram();
	    }
	}

	public void main(String args[]) {
	    (new Thread(new ProcessingThread())).start();
	}
    }

    static void runThread() {
	if (parser == null || !parser.isAlive()) {
	    parser = new Thread(new ProcessingThread());
	    parser.start();
	}
    }

    @Override
    public Component add(Component comp) {
	return backgroundPanel.add(comp);
    }

    public class AVProgress implements LProgressBarInterface {

	@Override
	public void setMax(int in) {
	    progress.setMax(in);
	}

	@Override
	public void setMax(int in, String status) {
	    progress.setMax(in, status);
	    if (!progress.paused()) {
		statusUpdate.setText(status);
	    }
	}

	@Override
	public void setStatus(String status) {
	    progress.setStatus(status);
	    if (!progress.paused()) {
		statusUpdate.setText(status);
	    }
	}

	@Override
	public void setStatus(int min, int max, String status) {
	    progress.setStatus(min, max, status);
	    if (!progress.paused()) {
		statusUpdate.setText(status);
	    }
	}

	@Override
	public void incrementBar() {
	    progress.incrementBar();
	}

	@Override
	public void reset() {
	    progress.incrementBar();
	}

	@Override
	public void setBar(int in) {
	    progress.setBar(in);
	}

	@Override
	public int getBar() {
	    return progress.getBar();
	}

	@Override
	public int getMax() {
	    return progress.getMax();
	}

	@Override
	public void pause(boolean on) {
	    progress.pause(on);
	}

	@Override
	public boolean paused() {
	    return progress.paused();
	}

	@Override
	public void done() {
	    progress.done();
	}
    }
}
