package org.jboss.tools.usage.tracker;

public interface IFocusPoint {

	public abstract String getName();

	public abstract IFocusPoint setChild(IFocusPoint childFocusPoint);

	public abstract IFocusPoint getChild();

	public abstract String getURI();

	public abstract String getTitle();

}