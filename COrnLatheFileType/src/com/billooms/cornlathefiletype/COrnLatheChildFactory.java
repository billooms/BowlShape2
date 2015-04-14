
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