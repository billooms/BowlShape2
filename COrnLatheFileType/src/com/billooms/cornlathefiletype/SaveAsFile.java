
package com.billooms.cornlathefiletype;

import com.billooms.outline.api.Outline;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.awt.ActionRegistration;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionID;
import org.openide.filesystems.FileChooserBuilder;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.openide.util.NbBundle.Messages;

/**
 * Action to save a COrnLathe xml file with a new name.
 * Note: I can't use the default "Save As..." because apparently that only works for text files.
 * @author Bill Ooms Copyright (c) 2011 Studio of Bill Ooms all rights reserved
 */
@ActionID(category = "File",
id = "com.billooms.cornlathefiletype.SaveAsFile")
@ActionRegistration(displayName = "#CTL_SaveAsFile")
@ActionReferences({
	@ActionReference(path = "Menu/File", position = 300),
	@ActionReference(path = "Shortcuts", name = "S-D-S")
})
@Messages("CTL_SaveAsFile=Save File As...")
public final class SaveAsFile implements ActionListener {
	private final static String EXTENSION = "xml";
	private final static String DEFAULT_NAME = "COrnLathe";

	/**
	 * Action to save a COrnLathe XML file with a new name
	 * @param e ActionEvent
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		saveAs();
	}

	/**
	 * Save an xml file with a new name
	 */
	protected static void saveAs() {
		File home = new File (System.getProperty("user.home"));		//The default dir to use if no value is stored
		File saveFile;

		JFileChooser chooser = new FileChooserBuilder("openfile")	// "openfile" is key for NbPreferences
				.setTitle("Save File As...")
				.setDefaultWorkingDirectory(home)					// only if a last-used directory cannot be found for the key
				.setApproveText("Save")
				.setFileFilter(new FileNameExtensionFilter(EXTENSION + " files", EXTENSION))
				.createFileChooser();

		if (OpenFile.openedFile != null) {		// offer the same name and incremented suffix
			if (OpenFile.suffix == 0) {
				OpenFile.suffix++;				// increment suffix
				saveFile = new File(OpenFile.openedFile.toString().replace("." + EXTENSION, "_" + OpenFile.suffix + "." + EXTENSION));
			} else {
				saveFile = new File(OpenFile.openedFile.toString().replace("_" + OpenFile.suffix , "_" + (OpenFile.suffix +1)));
				OpenFile.suffix++;				// increment suffix
			}
		} else {
			saveFile = new File(DEFAULT_NAME + "." + EXTENSION);
		}

		chooser.setSelectedFile(saveFile);
        int option = chooser.showSaveDialog(null);
        if (option != JFileChooser.APPROVE_OPTION) {
			return;										// User canceled or clicked the dialog's close box.
        }
        saveFile = chooser.getSelectedFile();

		if (!(saveFile.toString()).endsWith("." + EXTENSION)) {	// make sure we have format right
			saveFile = new File(saveFile.toString() + "." + EXTENSION);
		}
		if (saveFile.exists()) {					// Ask the user whether to replace the file.
			NotifyDescriptor d = new NotifyDescriptor.Confirmation(
					"The file " + saveFile.getName() + " already exists.\nDo you want to replace it?",
					"Overwrite File Check",
					NotifyDescriptor.YES_NO_OPTION,
					NotifyDescriptor.WARNING_MESSAGE);
			d.setValue(NotifyDescriptor.CANCEL_OPTION);
			Object result = DialogDisplayer.getDefault().notify(d);
			if (result != DialogDescriptor.YES_OPTION) {
				return;
			}
		}
		
		forceSave(saveFile);
	}
	
	protected static synchronized void forceSave(File file) {
		PrintWriter out;
		try {
			out = new PrintWriter(new FileOutputStream(file));
		} catch (Exception e) {
			NotifyDescriptor d = new NotifyDescriptor.Message(
					"Error while trying to open the file:\n" + e,
					NotifyDescriptor.ERROR_MESSAGE);
			DialogDisplayer.getDefault().notify(d);
			return;
		}

		try {
			out.println("<?xml version=\"1.0\"?>");
			out.println("<!--");        // *** Remove this later???
			out.println("<!DOCTYPE " + 
					NbBundle.getMessage(COrnLatheDataObject.class, "XML_Type") + 
					" PUBLIC " + 
					NbBundle.getMessage(COrnLatheDataObject.class, "DTD_IPL") + 
					" " +
					NbBundle.getMessage(COrnLatheDataObject.class, "DTD_URL") + 
					">");
			out.println("-->");        // *** Remove this later???
			out.println("<" +
					NbBundle.getMessage(COrnLatheDataObject.class, "XML_Type") + 
					" version=\"" +
					NbBundle.getMessage(COrnLatheDataObject.class, "XML_Version") + 
					"\"" + ">");
		
			Lookup.getDefault().lookup(Outline.class).writeXML(out);

			out.println("</" +
					NbBundle.getMessage(COrnLatheDataObject.class, "XML_Type") + 
					">");
		} catch (Exception e) {
			NotifyDescriptor d = new NotifyDescriptor.Message("Error while trying to write the file:\n" + e,
					NotifyDescriptor.ERROR_MESSAGE);
			DialogDisplayer.getDefault().notify(d);
		} finally {
			out.close();
		}

		OpenFile.open(file);	// Now open the new file so everyone is happy
	}
}
