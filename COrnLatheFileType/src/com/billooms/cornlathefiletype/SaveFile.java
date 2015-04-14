
package com.billooms.cornlathefiletype;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import org.openide.awt.ActionRegistration;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionID;
import org.openide.cookies.SaveCookie;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle.Messages;

/**
 * Action to save a COrnLathe xml file.
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
@ActionID(category = "File",
id = "com.billooms.cornlathefiletype.SaveFile")
@ActionRegistration(iconBase = "com/billooms/cornlathefiletype/icons/save.png",
displayName = "#CTL_SaveFile")
@ActionReferences({
	@ActionReference(path = "Menu/File", position = 200),
	@ActionReference(path = "Shortcuts", name = "D-S")
})
@Messages("CTL_SaveFile=Save File")
public final class SaveFile implements ActionListener {

	/**
	 * Action to save a COrnLathe XML file
	 * @param e ActionEvent
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		save();
	}

	/**
	 * Save the previously opened file, or call SaveAsFile if there is no previous saved file
	 */
	protected static void save() {
		if (OpenFile.openedFile != null) {	// save to the same file if possible
			FileObject openFO = FileUtil.toFileObject(FileUtil.normalizeFile(OpenFile.openedFile));
			DataObject openDO = null;
			try {
				openDO = DataObject.find(openFO);
			} catch (DataObjectNotFoundException dataObjectNotFoundException) {
				Exceptions.printStackTrace(dataObjectNotFoundException);
			}
			SaveCookie saveCookie = openDO.getLookup().lookup(SaveCookie.class);
			if (saveCookie != null) {		// Need saving?
				try {
					saveCookie.save();		// this is what does the saving
				} catch (IOException ex) {
					Exceptions.printStackTrace(ex);
				}
			}
		} else {
			SaveAsFile.saveAs();		// Ask the user for a file name
		}
	}
}
