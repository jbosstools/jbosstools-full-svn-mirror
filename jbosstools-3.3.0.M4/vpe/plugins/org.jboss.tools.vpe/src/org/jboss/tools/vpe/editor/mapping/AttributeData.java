package org.jboss.tools.vpe.editor.mapping;

import org.mozilla.interfaces.nsIDOMNode;
import org.w3c.dom.Attr;

/**
 * 
 * @author Sergey Dzmitrovich
 * 
 *         Keep information about output Attribute. Set up a correspondence
 *         source node and visual node
 * 
 */
public class AttributeData extends NodeData {

	/**
	 * some attributes can have a visual representation but have not a source
	 * representation
	 */
	private String attributeName;

	public AttributeData(Attr attr, nsIDOMNode visualNode, boolean editable) {
		super(attr, visualNode, editable);
	}

	public AttributeData(String attributeName, nsIDOMNode visualNode,
			boolean editable) {

		super(null, visualNode, editable);

		// initialize attributeName field
		this.attributeName = attributeName;

	}

	public AttributeData(String attributeName, nsIDOMNode visualNode) {

		super(null, visualNode, true);

		// initialize attributeName field
		this.attributeName = attributeName;

	}

	public String getAttributeName() {
		return attributeName;
	}

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	@Override
	public int getType() {
		return ATTRIBUTE;
	}
}
