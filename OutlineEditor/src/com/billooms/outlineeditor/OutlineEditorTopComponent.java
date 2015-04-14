
package com.billooms.outlineeditor;

import com.billooms.outline.api.Outline;
import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;

/**
 * Top component which displays and edits the outline and cutter information.
 * @author Bill Ooms Copyright (c) 2011 Studio of Bill Ooms all rights reserved
 */
@ConvertAsProperties(dtd = "-//com.billooms.outlineeditor//OutlineEditor//EN",
autostore = false)
@TopComponent.Description(preferredID = "OutlineEditorTopComponent",
iconBase = "com/billooms/outlineeditor/icons/OutEd16.png",
persistenceType = TopComponent.PERSISTENCE_ALWAYS)
@TopComponent.Registration(mode = "editor", openAtStartup = true)
@ActionID(category = "Window", id = "com.billooms.outlineeditor.OutlineEditorTopComponent")
@ActionReference(path = "Menu/Window" , position = 100 )
@TopComponent.OpenActionRegistration(displayName = "#CTL_OutlineEditorAction",
preferredID = "OutlineEditorTopComponent")
public final class OutlineEditorTopComponent extends TopComponent implements PropertyChangeListener {
	
	private Outline outline;
	private OutlineEditPanel outlineEditPanel;

	/**
	 * Create a new Top Component to displays and edits the outline and cutter information.
	 */
	public OutlineEditorTopComponent() {
		initComponents();
		setName(NbBundle.getMessage(OutlineEditorTopComponent.class, "CTL_OutlineEditorTopComponent"));
		setToolTipText(NbBundle.getMessage(OutlineEditorTopComponent.class, "HINT_OutlineEditorTopComponent"));

		outline = Lookup.getDefault().lookup(Outline.class);

		outlineEditPanel = new OutlineEditPanel();
		this.add(outlineEditPanel, BorderLayout.CENTER);
		
		this.associateLookup(Lookups.fixed(outlineEditPanel));
	}

	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setLayout(new java.awt.BorderLayout());
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
	@Override
	public void componentOpened() {

		addMouseListener(outlineEditPanel.drawPanel);
		addMouseMotionListener(outlineEditPanel.drawPanel);
		addMouseWheelListener(outlineEditPanel.drawPanel);
		
		outline.addPropertyChangeListener(this);		// this listens for changes to the outline
	}

	@Override
	public void componentClosed() {

		removeMouseListener(outlineEditPanel.drawPanel);
		removeMouseMotionListener(outlineEditPanel.drawPanel);
		removeMouseWheelListener(outlineEditPanel.drawPanel);
		
		outline.removePropertyChangeListener(this);
	}

	void writeProperties(java.util.Properties p) {
		// better to version settings since initial version as advocated at
		// http://wiki.apidesign.org/wiki/PropertyFiles
		p.setProperty("version", "1.0");
		// TODO store your settings
	}

	void readProperties(java.util.Properties p) {
		String version = p.getProperty("version");
		// TODO read your settings according to their version
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
//		System.out.println("OutlineEditorTopComponent: " + evt.getPropertyName() + " " + evt.getOldValue() + " " + evt.getNewValue());
		outlineEditPanel.updateAll();
		if (evt.getPropertyName().endsWith("XML")) {
			this.setDisplayName("Outline Editor: " + evt.getNewValue());	// update display name
		}
	}
}
