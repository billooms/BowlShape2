
package com.billooms.drawables;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.openide.ErrorManager;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.PropertySupport;
import org.openide.nodes.Sheet;
import org.openide.util.lookup.Lookups;

/**
 * This is a Node wrapped around an Outline point to provide property editing, tree viewing, etc.
 * @author Bill Ooms Copyright (c) 2011 Studio of Bill Ooms, all rights reserved
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
public class PointNode extends AbstractNode implements PropertyChangeListener {
	
	private Pt pt;
	
	/**
	 * Create a new PointNode for the given point
	 * @param pt 
	 */
    public PointNode(Pt pt) {
        super(Children.LEAF, Lookups.singleton(pt));	// there will be no children
		this.pt = pt;
		this.setName("Point");
		this.setDisplayName(pt.toString());
		this.setIconBaseWithExtension("com/billooms/drawables/Point16.png");
		
		pt.addPropertyChangeListener(this);
    }
	
    /**
     * Initialize a property sheet
     * @return property sheet
     */
	@Override
	protected Sheet createSheet() {
		Sheet sheet = Sheet.createDefault();
		Sheet.Set set = Sheet.createPropertiesSet();
		set.setDisplayName("Point Properties");
		try {
			Property xProp = new PropertySupport.Reflection(pt, double.class, "X");
			xProp.setName("x-location");
			set.put(xProp);
			
			Property yProp = new PropertySupport.Reflection(pt, double.class, "Y");
			yProp.setName("y-location");
			set.put(yProp);
		} catch (NoSuchMethodException ex) {
			ErrorManager.getDefault();
		}
		sheet.put(set);
		return sheet;
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		this.setDisplayName(pt.toString());
	}
	
}
