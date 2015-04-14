
package com.billooms.view3d;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractAction;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenuItem;
import org.openide.awt.ActionRegistration;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionID;
import org.openide.util.NbBundle.Messages;
import org.openide.util.actions.Presenter;
import org.openide.windows.WindowManager;

/**
 * Action to show or hide the grid in the View3D window
 * @author Bill Ooms Copyright (c) 2011 Studio of Bill Ooms all rights reserved
 */
@ActionID(category = "Edit",
id = "com.billooms.view3d.ShowGrid")
@ActionRegistration(displayName = "#CTL_ShowGrid")
@ActionReferences({
	@ActionReference(path = "Menu/View3D", position = 300)
})
@Messages("CTL_ShowGrid=Show Grid")
public final class ShowGrid extends AbstractAction implements Presenter.Menu, ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
        // nothing needs to happen here
	}

	@Override
	public JMenuItem getMenuPresenter() {
		final JCheckBoxMenuItem showGrid = new JCheckBoxMenuItem("Show Grid", null);
        showGrid.setSelected(WindowManager.getDefault().findTopComponent("View3DTopComponent").getLookup().lookup(DisplayPanel.class).isGridShowing());
        showGrid.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
				DisplayPanel panel3d = WindowManager.getDefault().findTopComponent("View3DTopComponent").getLookup().lookup(DisplayPanel.class);
				panel3d.showGrid(showGrid.getState());
			}
        });
        return showGrid;
	}

}
