
package com.billooms.outlineeditor;

import com.billooms.drawables.BoundingBox;
import com.billooms.drawables.Dot;
import com.billooms.drawables.Grid;
import com.billooms.outline.api.Outline;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Point2D;
import java.text.DecimalFormat;
import javax.swing.JPanel;
import org.openide.util.Lookup;

/**
 * Panel for editing the outline
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
public class OutlineEditPanel extends JPanel {
	private final DecimalFormat F3 = new DecimalFormat("0.000");
	
	private Outline outline;
	
	/** The panel where the outline is drawn */
	protected DrawPanel drawPanel;
	
	/**
	 * Creates a new OutlineEditPanel
	 * @param cut cutter
	 */
	public OutlineEditPanel() {
		outline = Lookup.getDefault().lookup(Outline.class);
		
		initComponents();

		drawPanel = new DrawPanel();
		add(drawPanel, BorderLayout.CENTER);

        putClientProperty("print.printable", Boolean.TRUE);	// this can be printed
		updateAll();
	}

	/**
	 * Zoom the drawPanel to fit the curves
	 */
	public void zoomToFit() {
		drawPanel.zoomToFit();
	}
	
	/**
	 * Update all the display information.
	 */
	public void updateAll() {
		thickField.setValue(outline.getThickness());
		resolutionField.setValue(outline.getResolution());
		
		lengthLabel.setText("Outside length: " + F3.format(outline.getOutsideLength()));
		
		drawPanel.repaint();
	}

	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        controlPanel = new javax.swing.JPanel();
        coordsLabel = new javax.swing.JLabel();
        lengthLabel = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        thickField = new javax.swing.JFormattedTextField();
        jLabel3 = new javax.swing.JLabel();
        resolutionField = new javax.swing.JFormattedTextField();

        setBackground(new java.awt.Color(0, 0, 0));
        setLayout(new java.awt.BorderLayout());

        coordsLabel.setText(org.openide.util.NbBundle.getMessage(OutlineEditPanel.class, "OutlineEditPanel.coordsLabel.text")); // NOI18N

        lengthLabel.setText(org.openide.util.NbBundle.getMessage(OutlineEditPanel.class, "OutlineEditPanel.lengthLabel.text")); // NOI18N

        jLabel1.setText(org.openide.util.NbBundle.getMessage(OutlineEditPanel.class, "OutlineEditPanel.jLabel1.text")); // NOI18N

        thickField.setColumns(5);
        thickField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.000"))));
        thickField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        thickField.setToolTipText(org.openide.util.NbBundle.getMessage(OutlineEditPanel.class, "OutlineEditPanel.thickField.toolTipText")); // NOI18N
        thickField.addMouseWheelListener(new java.awt.event.MouseWheelListener() {
            public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
                scrollThickness(evt);
            }
        });
        thickField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                changeThickness(evt);
            }
        });

        jLabel3.setText(org.openide.util.NbBundle.getMessage(OutlineEditPanel.class, "OutlineEditPanel.jLabel3.text")); // NOI18N

        resolutionField.setColumns(5);
        resolutionField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.000"))));
        resolutionField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        resolutionField.setToolTipText(org.openide.util.NbBundle.getMessage(OutlineEditPanel.class, "OutlineEditPanel.resolutionField.toolTipText")); // NOI18N
        resolutionField.addMouseWheelListener(new java.awt.event.MouseWheelListener() {
            public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
                scrollResolution(evt);
            }
        });
        resolutionField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                changeResolution(evt);
            }
        });

        org.jdesktop.layout.GroupLayout controlPanelLayout = new org.jdesktop.layout.GroupLayout(controlPanel);
        controlPanel.setLayout(controlPanelLayout);
        controlPanelLayout.setHorizontalGroup(
            controlPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, controlPanelLayout.createSequentialGroup()
                .add(coordsLabel)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 334, Short.MAX_VALUE)
                .add(lengthLabel))
            .add(controlPanelLayout.createSequentialGroup()
                .add(jLabel1)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(thickField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(18, 18, 18)
                .add(jLabel3)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(resolutionField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(31, Short.MAX_VALUE))
        );
        controlPanelLayout.setVerticalGroup(
            controlPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(controlPanelLayout.createSequentialGroup()
                .add(controlPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lengthLabel)
                    .add(coordsLabel))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(controlPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel1)
                    .add(thickField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel3)
                    .add(resolutionField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
        );

        add(controlPanel, java.awt.BorderLayout.SOUTH);
    }// </editor-fold>//GEN-END:initComponents

private void scrollThickness(java.awt.event.MouseWheelEvent evt) {//GEN-FIRST:event_scrollThickness
		if (thickField.isFocusOwner()) {
			outline.setThickness(((Number)thickField.getValue()).doubleValue() - 0.001*evt.getWheelRotation());
		}
}//GEN-LAST:event_scrollThickness

private void changeThickness(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_changeThickness
		if (thickField.isFocusOwner()) {
			outline.setThickness(((Number)thickField.getValue()).doubleValue());
		}
}//GEN-LAST:event_changeThickness

private void scrollResolution(java.awt.event.MouseWheelEvent evt) {//GEN-FIRST:event_scrollResolution
		if (resolutionField.isFocusOwner()) {
			outline.setResolution(((Number)resolutionField.getValue()).doubleValue() - 0.001*evt.getWheelRotation());
		}
}//GEN-LAST:event_scrollResolution

private void changeResolution(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_changeResolution
		if (resolutionField.isFocusOwner()) {
			outline.setResolution(((Number) resolutionField.getValue()).doubleValue());
		}
}//GEN-LAST:event_changeResolution

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel controlPanel;
    private javax.swing.JLabel coordsLabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel lengthLabel;
    private javax.swing.JFormattedTextField resolutionField;
    private javax.swing.JFormattedTextField thickField;
    // End of variables declaration//GEN-END:variables

	
	
	
	/**
	 * Nested class for the drawing panel
	 */
	public class DrawPanel extends JPanel implements MouseListener, MouseMotionListener, MouseWheelListener {
		private final Color DEFAULT_BACKGROUND = Color.BLACK;
		private final double WINDOW_PERCENT = 0.9;		// use 90% of the window for the view
		private final double INITIAL_DPI = 150.0;		// for the first time the window comes up
		private final Point INITIAL_ZPIX = new Point(150, 200);	// arbitrary
		private final int CLOSEST = 7;					// closest point means within 7 pixels
		private final Color DRAG_COLOR = Color.BLUE;
		
		private double dpi = INITIAL_DPI;
		private Point zeroPix = INITIAL_ZPIX;

		private Dot grabbedDot;             // dot being moved/dragged
		private boolean panning = false;
		private boolean measuring = false;
		private int dzX, dzY;				// delta between clicked point and original zeroPix
		private Point2D.Double savedPos;			// original position of a point before dragging
	
		/**
		 * Create a new drawing panel
		 */
		public DrawPanel() {
			setBackground(DEFAULT_BACKGROUND);
		}   //end constructor

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);

			Graphics2D g2d = (Graphics2D) g;
			g2d.translate(zeroPix.x, zeroPix.y);
			g2d.scale(dpi, -dpi);	// positive y is up

			new Grid(-(double) zeroPix.x / dpi, -(double) (getHeight() - zeroPix.y) / dpi,
						      (double) getWidth() / dpi, (double) getHeight() / dpi).paint(g2d);

			outline.paint(g2d);		// paint the curves
		}

		/**
		 * Scale so that the curves fit in the window
		 */
		private void zoomToFit() {
			BoundingBox bb = outline.getBoundingBox();		// bounding box for the curves
			dpi = (int) Math.min(WINDOW_PERCENT * getWidth() / bb.getWidth(),
								 WINDOW_PERCENT * getHeight() / bb.getHeight());
			zeroPix = new Point((getWidth() - (int) (bb.getWidth() * dpi)) / 2 - (int) (bb.min.x * dpi),
								(getHeight() - (int) (bb.getHeight() * dpi)) / 2 + (int) (bb.max.y * dpi));
			repaint();
		}
		
		@Override
		public void mouseClicked(MouseEvent e) {
			if (e.getClickCount() >= 2) {
				Point2D.Double pt = scalePixToInch(new Point(e.getX(), e.getY()));
				outline.addPt(pt);		// otherwise curve point
			}
		}

		@Override
		public void mousePressed(MouseEvent e) {
			if (e.getClickCount() == 1) {
				dzX = e.getX() - zeroPix.x;
				dzY = e.getY() - zeroPix.y;
				Point2D.Double pt = scalePixToInch(new Point(e.getX(), e.getY()));
				if (e.isMetaDown()) {					// right click for safePath plus
					panning = true;
				} else {
					measuring = true;		// enable measuring
					savedPos = scalePixToInch(e.getPoint());
					grabbedDot = outline.getClosestPt(pt, (double) CLOSEST / dpi);
					if (grabbedDot != null) {
						grabbedDot.setColor(DRAG_COLOR);
					}
				}
			}
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			Point p = new Point(e.getX(), e.getY());
			Point2D.Double pt = scalePixToInch(p);
			if (measuring) {
				double d = Math.hypot(pt.x - savedPos.x, pt.y - savedPos.y);
				coordsLabel.setText(F3.format(pt.x) + ", " + F3.format(pt.y) + ", distance: " + F3.format(d));
			} else {
				coordsLabel.setText(F3.format(pt.x) + ", " + F3.format(pt.y));
			}
			if (panning) {
				zeroPix = new Point(p.x - dzX, p.y - dzY);
			} else if (grabbedDot != null) {
				grabbedDot.drag(pt);
			}
			repaint();
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			Point p = new Point(e.getX(), e.getY());
			Point2D.Double pt = scalePixToInch(p);
			if (measuring) {
				double d = Math.hypot(pt.x - savedPos.x, pt.y - savedPos.y);
				coordsLabel.setText(F3.format(pt.x) + ", " + F3.format(pt.y) + ", distance: " + F3.format(d));
				measuring = false;
			} else {
				coordsLabel.setText(F3.format(pt.x) + ", " + F3.format(pt.y));
			}
			if (panning) {
				zeroPix = new Point(p.x - dzX, p.y - dzY);
				panning = false;
			} else if (grabbedDot != null) {
				grabbedDot.setPos(pt);     // if dragging, move the point here
				grabbedDot.setColor(Outline.DOT_CURVE_COLOR);
				grabbedDot = null;
			}
			repaint();
		}

		@Override
		public void mouseEntered(MouseEvent e) {
		}

		@Override
		public void mouseExited(MouseEvent e) {
			if (panning) {
				panning = false;
			} else if (grabbedDot != null) {
				outline.deletePt(grabbedDot);
			}
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			Point2D.Double pt = scalePixToInch(new Point(e.getX(), e.getY()));
			coordsLabel.setText(F3.format(pt.x) + ", " + F3.format(pt.y));
		}

		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {
			int notches = e.getWheelRotation();
			dpi = dpi + notches;
			repaint();		// force display to update
		}

		/**
		 * Convert a point in pixels to inches
		 * @param p point in pixels
		 * @return point in inches
		 */
		private Point2D.Double scalePixToInch(Point p) {
			return new Point2D.Double((double) (p.x - zeroPix.x) / dpi,
					(double) (zeroPix.y - p.y) / dpi);
		}
	}
}
