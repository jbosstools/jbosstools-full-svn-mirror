package org.hibernate.eclipse.mapper.views.contentoutline;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.graphics.Image;
import org.eclipse.wst.sse.core.internal.provisional.INodeAdapterFactory;
import org.eclipse.wst.sse.ui.internal.contentoutline.IJFaceNodeAdapter;
import org.eclipse.wst.xml.ui.internal.contentoutline.JFaceNodeAdapter;
import org.eclipse.wst.xml.ui.internal.contentoutline.JFaceNodeAdapterFactory;
import org.hibernate.console.ImageConstants;
import org.hibernate.eclipse.console.utils.EclipseImages;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;


/**
 * For xml files in general.
 * 
 * Adapts a DOM node to a JFace viewer.
 */
public class JFaceNodeAdapterForXML extends JFaceNodeAdapter {
	final static Class ADAPTER_KEY = IJFaceNodeAdapter.class;
	protected INodeAdapterFactory adapterFactory;

	public JFaceNodeAdapterForXML(JFaceNodeAdapterFactory adapterFactory) {
		super(adapterFactory);
		this.adapterFactory = adapterFactory;
	}

		/**
	 * Fetches the label text specific to this object instance.
	 */
	public String getLabelText(Object object) {
		String result = getNodeName(object);
		Node node = (Node) object;		
		NamedNodeMap attributes = node.getAttributes();
		if(attributes!=null) {
			Node firstAttribute = attributes.item(0);
			if(firstAttribute!=null) {
				return result + " " + firstAttribute.getNodeName() + "=\"" + firstAttribute.getNodeValue() + "\"";
			} 
		} 
		
		return result;
	}

	private String getNodeName(Object object) {
		Node node = (Node) object;
		String nodeName = node.getNodeName();
		if(node.getNodeType()==Node.PROCESSING_INSTRUCTION_NODE && "xml".equals(nodeName)) {
			return "xml (Hibernate Tools)";
		}
		return nodeName;
	}

	public Object getParent(Object object) {

		Node node = (Node) object;
		return node.getParentNode();
	}

	public boolean hasChildren(Object object) {
		return super.hasChildren(object);
	}

	/**
	 * Allowing the INodeAdapter to compare itself against the type allows it
	 * to return true in more than one case.
	 */
	public boolean isAdapterForType(Object type) {
		return type.equals(ADAPTER_KEY);
	}
	
		
	static Map nameToMap = new HashMap();
	static {
		nameToMap.put("many-to-one", ImageConstants.MANYTOONE);
		nameToMap.put("one-to-many", ImageConstants.ONETOMANY);
		nameToMap.put("property", ImageConstants.PROPERTY);	
		nameToMap.put("class", ImageConstants.MAPPEDCLASS);
		nameToMap.put("subclass", ImageConstants.MAPPEDCLASS);
		nameToMap.put("joined-subclass", ImageConstants.MAPPEDCLASS);
		nameToMap.put("union-subclass", ImageConstants.MAPPEDCLASS);
		nameToMap.put("id", ImageConstants.IDPROPERTY);
		nameToMap.put("one-to-one", ImageConstants.ONETOONE);
		nameToMap.put("component", ImageConstants.ONETOONE);
	}
	
	protected Image createImage(Object object) {
		Node node = (Node) object;
		if(node.getNodeType()==Node.ELEMENT_NODE) {
			String key = (String) nameToMap.get( getNodeName(node) );
			if(key!=null) {
				return EclipseImages.getImage(key);
			}
		}
		return super.createImage( object );
	}
}
