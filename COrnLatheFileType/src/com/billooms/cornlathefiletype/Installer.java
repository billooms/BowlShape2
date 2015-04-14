
package com.billooms.cornlathefiletype;

import javax.swing.JPopupMenu;
import javax.swing.ToolTipManager;
import org.openide.modules.ModuleInstall;

/**
 * Installer
 * @author Bill Ooms Copyright (c) 2011 Studio of Bill Ooms all rights reserved
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
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
