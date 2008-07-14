package org.jboss.ide.eclipse.archives.core.model;

public interface IVariableProvider extends IVariableManager {
	public String getId();
	public String getName();
	public boolean getEnabled();
	public void setEnabled(boolean val);
	public int getWeight();
	public void setWeight(int weight);
	public int getDefaultWeight();
}
