
package com.billooms.cornlathefiletype;

import com.billooms.outline.api.Outline;
import com.billooms.outlineeditor.OutlineEditPanel;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import javax.xml.parsers.DocumentBuilderFactory;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.cookies.OpenCookie;
import org.openide.cookies.SaveCookie;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataNode;
import org.openide.loaders.DataObjectExistsException;
import org.openide.loaders.MultiDataObject;
import org.openide.loaders.MultiFileLoader;
import org.openide.nodes.Node;
import org.openide.nodes.Children;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.openide.windows.WindowManager;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * DataObject for COrnLathe xml files.
 * @author Bill Ooms. Copyright 2011 Studio of Bill Ooms. All rights reserved.
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
public class COrnLatheDataObject extends MultiDataObject implements PropertyChangeListener {

	private InstanceContent ic = new InstanceContent();
	private Lookup lookup = new AbstractLookup(ic);
	private Saver saver = new Saver();
	private Opener opener = new Opener();
	private OutlineEditPanel outlineEdit;
	private Outline outline;
	
	public COrnLatheDataObject(FileObject pf, MultiFileLoader loader) throws DataObjectExistsException, IOException {
		super(pf, loader);

		outlineEdit = WindowManager.getDefault().findTopComponent("OutlineEditorTopComponent").getLookup().lookup(OutlineEditPanel.class);
		outline = Lookup.getDefault().lookup(Outline.class);
				
        enableSaveAction(false);
		enableOpenAction(true);		// this enables the file to be opened
	}

	@Override
	protected Node createNodeDelegate() {
		return new DataNode(this, 
				Children.create(new COrnLatheChildFactory(this), true),
				getLookup());
	}

	@Override
	public Lookup getLookup() {
		return this.lookup;
	}

	/**
	 * Listen for PropertyChangeEvents
	 * @param evt event
	 */
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
//		System.out.println(getPrimaryFile().getNameExt() + " " + evt.getPropertyName() + " " + evt.getOldValue() + " " + evt.getNewValue());
		if (evt.getPropertyName().endsWith("XML")) {
			return;		// don't consider reading XML a change to the current file
		}
        enableSaveAction(true);
	}
	
	/**
	 * Remove any Saver from lookup and mark this as unmodified. 
	 * Un-saved changes are not un-done. 
	 * The file is not modified.
	 * This is mainly used so that the "Save All..." action doesn't try 
	 * to save the changes later.
	 */
	public void forgetChanges() {
		enableSaveAction(false);
	}
	
	/**
	 * Remove all propertyChangeListeners for this DataObject,  
	 * clear all data, and dispose of this.
	 * This is usually done just prior to opening a new file.
	 */
	public void quitListening() {
		outline.removePropertyChangeListener(this);
		outline.clear();
		enableSaveAction(false);
		this.dispose();
	}

	/**
	 * Implementation of SaveCookie for saving the data.
	 */
	private class Saver implements SaveCookie {
		@Override
		public void save() throws IOException {
			PrintWriter out;
			File file = FileUtil.toFile(getPrimaryFile());
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
				
				outline.writeXML(out);		// dotLocation, dotCurve, safePath

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
			enableSaveAction(false);
		}

	}

	/**
	 * Enable the Save All... action by marking this as modified 
	 * and add a Saver SaveCookie to the lookup.
	 * @param enableSave true=need to save this
	 */
	private void enableSaveAction(boolean enableSave) {
		this.setModified(enableSave);
		if (enableSave) {
			ic.add(saver);
		} else {
			ic.remove(saver);
		}
	}

	/**
	 * Implementation of OpenCookie for reading the data from the file.
	 */
	private class Opener implements OpenCookie {
		@Override
		public void open() {
			Document xmldoc;
			File file = FileUtil.toFile(getPrimaryFile());
			try {
				xmldoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file);
			} catch (Exception e) {
				NotifyDescriptor d = new NotifyDescriptor.Message(
						"Error while trying to read/parse the file:\n" + e,
						NotifyDescriptor.ERROR_MESSAGE);
				DialogDisplayer.getDefault().notify(d);
				return;
			}

			try {
				Element rootElement = xmldoc.getDocumentElement();
				String version = "99.0";	// make this high, so error if not found in file
				version = rootElement.getAttribute("version");
				if (Double.parseDouble(version) > Double.parseDouble(NbBundle.getMessage(COrnLatheDataObject.class, "XML_Version"))) {
					throw new Exception("File was written with a newer version of " + NbBundle.getMessage(COrnLatheDataObject.class, "XML_Type"));
				}
				
				outline.readXML(rootElement, file);			// dotLocation, dotCurve, safePath

				outlineEdit.zoomToFit();		// also for convenience
				 
				outline.addPropertyChangeListener(COrnLatheDataObject.this);
			} catch (Exception e) {
				NotifyDescriptor d = new NotifyDescriptor.Message("Error while trying to read the xml data:\n" + e,
						NotifyDescriptor.ERROR_MESSAGE);
				DialogDisplayer.getDefault().notify(d);
			}
			enableSaveAction(false);		// Just read it in
//			enableOpenAction(false);		// not sure why you would want this here
		}
	}
	
	/**
	 * Enable opening by adding a Opener OpenCookie to the lookup.
	 * @param enableOpen true=this can be opened
	 */
	private void enableOpenAction(boolean enableOpen) {
		if (enableOpen) {
			ic.add(opener);
		} else {
			ic.remove(opener);
		}
	}
}
