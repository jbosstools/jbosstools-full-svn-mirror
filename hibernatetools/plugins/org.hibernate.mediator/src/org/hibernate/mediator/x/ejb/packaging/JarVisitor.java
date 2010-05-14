package org.hibernate.mediator.x.ejb.packaging;

import java.util.HashSet;
import java.util.Set;

import org.hibernate.mediator.base.HObject;

public class JarVisitor extends HObject {

	public static final String CL = "org.hibernate.ejb.packaging.JarVisitor"; //$NON-NLS-1$

	protected JarVisitor(Object jarVisitor) {
		super(jarVisitor, CL);
	}

	public String getUnqualifiedJarName() {
		return (String)invoke(mn());
	}

	@SuppressWarnings("unchecked")
	public Set<Entry>[] getMatchingEntries() {
		Set[] objs = (Set[])invoke(mn());
		Set[] res = new Set[objs.length];
		for (int i = 0; i < objs.length; i++) {
			Set<Entry> newSet = new HashSet<Entry>();
			for (Object o : objs[i]) {
				newSet.add(new Entry(o));
			}
			res[i] = newSet;
		}
		return res;
	}

	public Filter[] getFilters() {
		Object[] objs = (Object[])invoke(mn());
		Filter[] res = new Filter[objs.length];
		for (int i = 0; i < objs.length; i++) {
			res[i] = new Filter(objs[i]);
		}
		return res;
	}

}
