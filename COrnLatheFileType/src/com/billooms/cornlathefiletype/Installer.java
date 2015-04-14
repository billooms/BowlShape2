
package com.billooms.cornlathefiletype;

import javax.swing.JPopupMenu;
import javax.swing.ToolTipManager;
import org.openide.modules.ModuleInstall;

/**
 * Installer
 * @author Bill Ooms Copyright (c) 2011 Studio of Bill Ooms all rights reserved
 */
public class Installer extends ModuleInstall {

	@Override
	public void restored() {
		// By default, do nothing.
		// Put your startup code here.
		JPopupMenu.setDefaultLightWeightPopupEnabled(false);	// Req'd for menus to show on a PC
		ToolTipManager.sharedInstance().setLightWeightPopupEnabled(false);
	}
}
