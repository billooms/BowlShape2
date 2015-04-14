
package com.billooms.cornlathefiletype;

import com.billooms.outline.api.Outline;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.awt.ActionRegistration;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionID;
import org.openide.awt.StatusDisplayer;
import org.openide.filesystems.FileChooserBuilder;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;

/**
 * Action to save the outline to a tab-delimited file.
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
id = "com.billooms.cornlathefiletype.SaveOutline")
@ActionRegistration(displayName = "#CTL_SaveOutline")
@ActionReferences({
	@ActionReference(path = "Menu/File", position = 550)
})
@Messages("CTL_SaveOutline=Save Outline")
public final class SaveOutline implements ActionListener {
	public final static String EXTENSION = "txt";

	@Override
	public void actionPerformed(ActionEvent e) {
		File textFile;

		if (OpenFile.openedFile != null) {
			textFile = new File((OpenFile.openedFile.getAbsolutePath()).replaceAll("." + OpenFile.EXTENSION, "Outline." + SaveOutline.EXTENSION));
		} else {
			File home = new File(System.getProperty("user.home"));	//The default dir to use if no value is stored
			textFile = new FileChooserBuilder("openfile")
					.setTitle("Save Coordinate File As...")
					.setDefaultWorkingDirectory(home)
					.setApproveText("save")
					.setFileFilter(new FileNameExtensionFilter(EXTENSION + " files", EXTENSION))
					.showSaveDialog();
		}
		if (textFile == null) {
			return;
		}
		if (!(textFile.toString()).endsWith("." + EXTENSION)) {
			textFile = new File(textFile.toString() + "." + EXTENSION);
		}
		if (textFile.exists()) {					// Ask the user whether to replace the file.
			NotifyDescriptor d = new NotifyDescriptor.Confirmation(
					"The file " + textFile.getName() + " already exists.\nDo you want to replace it?",
					"Overwrite File Check",
					NotifyDescriptor.YES_NO_OPTION,
					NotifyDescriptor.WARNING_MESSAGE);
			d.setValue(NotifyDescriptor.CANCEL_OPTION);
			Object result = DialogDisplayer.getDefault().notify(d);
			if (result != DialogDescriptor.YES_OPTION) {
				return;
			}
		}
		StatusDisplayer.getDefault().setStatusText("Saving Coordinate File As: " + textFile.getName());
		saveOutline(textFile);
	}


	/**
	 * Save tab-delimited text to a file
	 * @param file
	 */
	private void saveOutline(File file) {
		PrintWriter out;
		try {
			FileOutputStream stream = new FileOutputStream(file);
			out = new PrintWriter(stream);
		} catch (Exception e) {
			NotifyDescriptor d = new NotifyDescriptor.Message("Error while trying to open the file:\n" + e,
					NotifyDescriptor.ERROR_MESSAGE);
			DialogDisplayer.getDefault().notify(d);
			return;
		}
        try {

			// find the outline and write the information
			Lookup.getDefault().lookup(Outline.class).writeOutline(out);

        } catch (Exception e) {
			NotifyDescriptor d = new NotifyDescriptor.Message("Error while trying to write the file:\n" + e,
					NotifyDescriptor.ERROR_MESSAGE);
        } finally {
			out.close();
		}
	}
}
