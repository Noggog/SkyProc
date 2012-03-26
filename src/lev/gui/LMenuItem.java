/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lev.gui;

import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import javax.swing.JMenuItem;

/**
 *
 * @author Justin Swanson
 */
public class LMenuItem extends LHelpComponent {

    JMenuItem menuItem;

    public LMenuItem (String title_) {
	super(title_);
	menuItem = new JMenuItem(title_);
    }

    @Override
    public void addHelpHandler() {
	menuItem.addMouseListener(new HelpMouseHandler());
    }

    public JMenuItem getItem() {
	return menuItem;
    }

    @Override
    public void updateHelp() {
	super.updateHelp();
        if (help != null) {
            help.setSettingPos(75);
            help.hideArrow();
        }
    }

    public void addActionListener (ActionListener a) {
	menuItem.addActionListener(a);
    }

    @Override
    public void addMouseListener (MouseListener m) {
	menuItem.addMouseListener(m);
    }
}

