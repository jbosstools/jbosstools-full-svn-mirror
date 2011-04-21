package org.dom4j.mediator.x;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.mediator.base.HObject;

public class Element extends Node {

	public static final String CL = "org.dom4j.Element"; //$NON-NLS-1$

	protected Element(Object element) {
		super(element, CL);
	}

	public static Element newInstance() {
		return new Element(HObject.newInstance(CL));
	}
	
	public Element element(String name) {
		return new Element(invoke(mn(), name));
	}

	@SuppressWarnings("unchecked")
	public List<Element> elements() {
		List list = (List)invoke(mn());
		Iterator it = list.iterator();
		ArrayList<Element> al = new ArrayList<Element>();
		while (it.hasNext()) {
			Object obj = it.next();
			if (obj != null) {
				al.add(new Element(obj));
			}
		}
		return al;
	}

	@SuppressWarnings("unchecked")
	public List<Element> elements(String name) {
		List list = (List)invoke(mn(), name);
		Iterator it = list.iterator();
		ArrayList<Element> al = new ArrayList<Element>();
		while (it.hasNext()) {
			Object obj = it.next();
			if (obj != null) {
				al.add(new Element(obj));
			}
		}
		return al;
	}
	
	public boolean remove(Node node) {
		return (Boolean)invoke(mn(), node);
	}

	public String attributeValue(String name) {
		return (String)invoke(mn(), name);
	}
	
	public Attribute attribute(String name) {
		Object obj = invoke(mn(), name);
		if (obj == null) {
			return null;
		}
		return new Attribute(obj);
	}

	public String getName() {
		return (String)invoke(mn());
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}
}
