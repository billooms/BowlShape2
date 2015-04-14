
package com.billooms.cornlathefiletype;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilderFactory;
import org.openide.filesystems.FileUtil;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Create children for the COrnLatheDataObject.
 * Note: this is not currently used for anything.
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
public class COrnLatheChildFactory extends ChildFactory<String> {

    private final COrnLatheDataObject dObj;

    public COrnLatheChildFactory(COrnLatheDataObject dObj) {
        this.dObj = dObj;
    }

    @Override
    protected boolean createKeys(List list) {
		Document xmldoc = null;
		File file = FileUtil.toFile(dObj.getPrimaryFile());
		try {
			xmldoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file);
		} catch (Exception e) {
            Exceptions.printStackTrace(e);
			return true;
		}
		
		List<String> eList = new ArrayList<String>();
		Element root = xmldoc.getDocumentElement();
		NodeList nodes = root.getChildNodes();
		for (int i = 0; i < nodes.getLength(); i++) {
			if (nodes.item(i) instanceof Element) {
				Element element = (Element) nodes.item(i);
				eList.add(element.getTagName());
			}
		}
		list.addAll(eList);

//		try {
//            List<String> dObjContent = fObj.asLines();
//            list.addAll(dObjContent);
//        } catch (IOException ex) {
//            Exceptions.printStackTrace(ex);
//        }
        return true;
    }

    @Override
    protected Node createNodeForKey(String key) {
        Node childNode = new AbstractNode(Children.LEAF);
        childNode.setDisplayName(key);
        return childNode;
    }

}