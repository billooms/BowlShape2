
package com.billooms.cornlathefiletype;

import com.billooms.outline.api.Outline;
import com.billooms.outlineeditor.OutlineEditPanel;
import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilderFactory;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.cookies.OpenCookie;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataNode;
import org.openide.loaders.DataObjectExistsException;
import org.openide.loaders.MultiDataObject;
import org.openide.loaders.MultiFileLoader;
import org.openide.nodes.CookieSet;
import org.openide.nodes.Node;
import org.openide.nodes.Children;
import org.openide.util.Lookup;
import org.openide.text.DataEditorSupport;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.openide.windows.WindowManager;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * DataObject for old BowlShape xml files (version 1.2).
 * @author Bill Ooms. Copyright 2011 Studio of Bill Ooms. All rights reserved.
 */
public class BowlShapeDataObject extends MultiDataObject {

	private InstanceContent ic = new InstanceContent();
	private Lookup lookup = new AbstractLookup(ic);
	private Opener opener = new Opener();
	private OutlineEditPanel outlineEdit;
	private Outline outline;
	
	public BowlShapeDataObject(FileObject pf, MultiFileLoader loader) throws DataObjectExistsException, IOException {
		super(pf, loader);
		CookieSet cookies = getCookieSet();
		cookies.add((Node.Cookie) DataEditorSupport.create(this, getPrimaryEntry(), cookies));

		outlineEdit = WindowManager.getDefault().findTopComponent("OutlineEditorTopComponent").getLookup().lookup(OutlineEditPanel.class);
		outline = Lookup.getDefault().lookup(Outline.class);
		
		enableOpenAction(true);		// this enables the file to be opened
	}

	@Override
	protected Node createNodeDelegate() {
		return new DataNode(this, Children.LEAF, getLookup());
	}

	@Override
	public Lookup getLookup() {
		return this.lookup;
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
				
				outline.readOldXML(rootElement, file);	// read old BowlShape format

				outlineEdit.zoomToFit();		// also for convenience
				 
			} catch (Exception e) {
				NotifyDescriptor d = new NotifyDescriptor.Message("Error while trying to read the xml data:\n" + e,
						NotifyDescriptor.ERROR_MESSAGE);
				DialogDisplayer.getDefault().notify(d);
			}
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
