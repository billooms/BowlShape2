
package com.billooms.cornlathefiletype;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.awt.ActionRegistration;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionID;
import org.openide.awt.StatusDisplayer;
import org.openide.cookies.OpenCookie;
import org.openide.filesystems.FileChooserBuilder;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle;
import org.openide.util.NbBundle.Messages;

/**
 * Action to open a COrnLathe xml file.
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
id = "com.billooms.cornlathefiletype.OpenFile")
@ActionRegistration(iconBase = "com/billooms/cornlathefiletype/icons/openFile.png",
displayName = "#CTL_OpenFile")
@ActionReferences({
	@ActionReference(path = "Menu/File", position = 0),
	@ActionReference(path = "Toolbars/File", position = 100),
	@ActionReference(path = "Shortcuts", name = "D-O")
})
@Messages("CTL_OpenFile=Open File...")
public final class OpenFile implements ActionListener {
	/** File Extension used */
	public final static String EXTENSION = "xml";
	/** The most recently opened file */
	public static File openedFile = null;
	/** Numerical suffix for future versions of the file */
	protected static int suffix = 0;			// suffix for versions
	
//	protected static COrnLatheDataObject dObj = null;	// DataObject for openedFile
	public static COrnLatheDataObject dObj = null;	// DataObject for openedFile

	/**
	 * Action to open an Indexer XML file
	 * @param e ActionEvent
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if ((dObj != null) && dObj.isModified()) {		// Any unsaved changes?
			NotifyDescriptor d = new NotifyDescriptor.Confirmation(
					"Changes have not been saved!\nDo you want to save changes?",
					"Changes not saved",
					NotifyDescriptor.YES_NO_CANCEL_OPTION,
					NotifyDescriptor.WARNING_MESSAGE);
			d.setValue(NotifyDescriptor.YES_OPTION);
			Object result = DialogDisplayer.getDefault().notify(d);
			if ((result == DialogDescriptor.CLOSED_OPTION) || (result == DialogDescriptor.CANCEL_OPTION)) {
				return;										// Cancel/Close: don't open a new file
			}
			if (result == DialogDescriptor.YES_OPTION) {
				SaveFile.save();				// YES: save and continue
			}	
			dObj.setModified(false);			// NO: don't save changes, continue to open a new file
		}
		
		File home = new File (System.getProperty("user.home"));	//The default dir to use if no value is stored
		File file = new FileChooserBuilder("openfile")		// "openfile" is key for NbPreferences
				.setTitle("Open File")
				.setDefaultWorkingDirectory(home)			// only if a last-used directory cannot be found for the key
				.setApproveText("Open")
				.setFileFilter(new FileNameExtensionFilter(EXTENSION + " files", EXTENSION))
				.showOpenDialog();
		if (file != null) {
			open(file);
		}
	}

	/**
	 * Find the numerical suffix: the number xx in "name_xx.xml"
	 * @param file the given file
	 * @return the numerical suffix, or 0 if there is none
	 */
	private static int findSuffix(File file) {
		String str = file.getName().replace("." + EXTENSION, "");
		int i = str.lastIndexOf("_");
		if (i < 0) {
			return 0;
		}
		int suf;
		try {
			suf = Integer.parseInt(str.substring(i+1));
		} catch (Exception e) {
			suf = 0;
		}
		return suf;
	}

	/**
	 * Open the given file, create a DataObject for the file, 
	 * and read the xml into the IndexWheelMgr.
	 * @param file File to open
	 */
	protected static void open(File file) {
		if (file != null) {
			suffix = findSuffix(file);
			FileObject fo = FileUtil.toFileObject(FileUtil.normalizeFile(file));
//			System.out.println("MIMEType: " + fo.getMIMEType().toString());
			if (fo.getMIMEType().equals(NbBundle.getMessage(OpenFile.class, "BOWLSHAPEMIMETYPE"))) {
				NotifyDescriptor d = new NotifyDescriptor.Confirmation(
						"Opening an older format BowlShape file!\n"
						+ "Do you want to convert to new format?\n"
						+ "The new file will be named New" + file.getName(),
						"Old BowlShape File",
						NotifyDescriptor.YES_NO_CANCEL_OPTION,
						NotifyDescriptor.WARNING_MESSAGE);
				d.setValue(NotifyDescriptor.YES_OPTION);
				Object result = DialogDisplayer.getDefault().notify(d);
				if ((result == DialogDescriptor.CLOSED_OPTION) || 
					(result == DialogDescriptor.CANCEL_OPTION) || 
					(result == DialogDescriptor.NO_OPTION)) {
					return;										// No/Cancel/Close: don't open a new file
				}
				if (result == DialogDescriptor.YES_OPTION) {
					openOldFormat(file);				// YES: Open the old format
					file = new File(file.getParent(), "New" + file.getName());
					SaveAsFile.forceSave(file);			// Save as new format
					fo = FileUtil.toFileObject(FileUtil.normalizeFile(file));	// continue on to read in again
				}	
			}
			if (!fo.getMIMEType().equals(NbBundle.getMessage(OpenFile.class, "MIMETYPE"))) {
				NotifyDescriptor d = new NotifyDescriptor.Message(
						"File " + file.getName() + " is not a COrnLathe xml file!",
						NotifyDescriptor.ERROR_MESSAGE);
				DialogDisplayer.getDefault().notify(d);
				return;
			}
			try {
				if (dObj != null) {
					dObj.quitListening();	// the old DataObject should quit listening for changes (disposes it)
				}
				dObj = (COrnLatheDataObject) DataObject.find(fo);	// this creates the new DataObject for the file
				
				OpenCookie openCookie = dObj.getLookup().lookup(OpenCookie.class);
				if (openCookie != null) {		// this should always be the case
					openCookie.open();			// this is what reads the xml file to the IndexerMgr
					openedFile = file;			// this is the last file opened
					StatusDisplayer.getDefault().setStatusText("Open File: " + file.getName());
				}
			} catch (DataObjectNotFoundException ex) {
				Exceptions.printStackTrace(ex);
			}
		}
	}
	
	private static void openOldFormat(File file) {
		FileObject fo = FileUtil.toFileObject(FileUtil.normalizeFile(file));
		try {
			if (dObj != null) {
				dObj.quitListening();	// the old DataObject should quit listening for changes (disposes it)
			}
			BowlShapeDataObject bsdObj = (BowlShapeDataObject)DataObject.find(fo);	// this creates the new DataObject for the file

			OpenCookie openCookie = bsdObj.getLookup().lookup(OpenCookie.class);
			if (openCookie != null) {		// this should always be the case
				openCookie.open();			// this is what reads the xml file to the IndexerMgr
				openedFile = file;			// this is the last file opened
				StatusDisplayer.getDefault().setStatusText("Open File: " + file.getName());
			}
		} catch (DataObjectNotFoundException ex) {
			Exceptions.printStackTrace(ex);
		}
	}
}
