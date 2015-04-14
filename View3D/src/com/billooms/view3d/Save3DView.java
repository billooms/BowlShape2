
package com.billooms.view3d;

import com.billooms.cornlathefiletype.OpenFile;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JFileChooser;
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
import org.openide.util.NbBundle.Messages;
import org.openide.windows.WindowManager;

/**
 * Action to save the View3D window as a jpeg file
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
id = "com.billooms.view3d.Save3DView")
@ActionRegistration(displayName = "#CTL_Save3DView")
@ActionReferences({
	@ActionReference(path = "Menu/View3D", position = 500, separatorBefore = 499)
})
@Messages("CTL_Save3DView=Save 3D View...")
public final class Save3DView implements ActionListener {
	public final static String FORMAT = "jpeg";
	
	@Override
	public void actionPerformed(ActionEvent e) {
		File home = new File (System.getProperty("user.home"));	//The default dir to use if no value is stored
		File saveFile;

		JFileChooser chooser = new FileChooserBuilder("openfile")
				.setTitle("Save 3D Graphic View")
				.setDefaultWorkingDirectory(home)
				.setApproveText("save")
				.setFileFilter(new FileNameExtensionFilter(FORMAT + " files", FORMAT))
				.createFileChooser();

		if (OpenFile.openedFile != null) {		// offer the same name if possible
			saveFile = new File(OpenFile.openedFile.toString().replace(".xml", "." + FORMAT));
		} else {
			saveFile = new File("view3D." + FORMAT);
		}

		chooser.setSelectedFile(saveFile);
        int option = chooser.showSaveDialog(null);
        if (option != JFileChooser.APPROVE_OPTION) {
			return;										// User canceled or clicked the dialog's close box.
        }
        saveFile = chooser.getSelectedFile();

		if (saveFile != null) {
			if (!(saveFile.toString()).endsWith("." + FORMAT)) {	// make sure we have format right
				saveFile = new File(saveFile.toString() + "." + FORMAT);
			}
			if (saveFile.exists()) {					// Ask the user whether to replace the file.
				NotifyDescriptor d = new NotifyDescriptor.Confirmation(
						"The file " + saveFile.getName() + " already exists.\nDo you want to replace it?",
						"Overwrite File Check",
						NotifyDescriptor.OK_CANCEL_OPTION,
						NotifyDescriptor.WARNING_MESSAGE);
				d.setValue(NotifyDescriptor.CANCEL_OPTION);
				Object result = DialogDisplayer.getDefault().notify(d);
				if (result != null && result == DialogDescriptor.CANCEL_OPTION) {
					return;
				}
			}
			((View3DTopComponent)WindowManager.getDefault().findTopComponent("View3DTopComponent")).saveGraphic(saveFile, FORMAT);
            StatusDisplayer.getDefault().setStatusText("Save 3D View to: " + saveFile.getName());
		}
	}
}
