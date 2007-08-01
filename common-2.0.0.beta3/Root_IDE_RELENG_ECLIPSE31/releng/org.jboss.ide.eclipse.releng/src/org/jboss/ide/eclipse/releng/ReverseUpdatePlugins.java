package org.jboss.ide.eclipse.releng;

import java.util.ArrayList;
import java.util.List;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

public class ReverseUpdatePlugins extends Task {

	private List plugins;
	
	public ReverseUpdatePlugins ()
	{
		plugins = new ArrayList();
	}
	
	public void addConfiguredPlugin (Plugin plugin)
	{
		plugins.add(plugin);
	}
	
	public List getPlugins ()
	{
		return plugins;
	}
	
	public static class Plugin extends Task
	{
		private String name = "";
		
		public void addText (String text) { name += text; }
		
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		
		public void execute () throws BuildException { }
	}
}
