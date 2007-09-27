package example6ResourceTracking;

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jdt.core.ElementChangedEvent;
import org.eclipse.jdt.core.IElementChangedListener;
import org.eclipse.jdt.core.JavaCore;

public class Example6Model implements IResourceChangeListener, IElementChangedListener {
	
	public static final String RESOURCES = "RESOURCES";
	public static final String ELEMENTS = "ELEMENTS";
	
	
	
	private IWorkspace workspace;
	private ModelRoot root;
	private ArrayList listeners;
	
	private static Example6Model instance;
	public static Example6Model getDefault() {
		if( Example6Model.instance == null ) 
			instance = new Example6Model();
		return instance;
	}

	public Example6Model() {
		root = new ModelRoot();
		listeners = new ArrayList();
		workspace = ResourcesPlugin.getWorkspace();
		workspace.addResourceChangeListener(this);
		JavaCore.addElementChangedListener(this);
		instance = this;
	}
	
	
	public void resourceChanged(IResourceChangeEvent event) {
		root.addResourceChange(event);
		for( Iterator i = listeners.iterator(); i.hasNext();) {
			((MyChangeListener)i.next()).fireMyChangeEvent();
		}
	}


	public void elementChanged(ElementChangedEvent event) {
		root.addElementChange(event);
		for( Iterator i = listeners.iterator(); i.hasNext();) {
			System.out.println("firing");
			((MyChangeListener)i.next()).fireMyChangeEvent();
		}
	}

	public ModelRoot getModel() {
		return root;
	}
	
	public void addListener(MyChangeListener listener) {
		listeners.add(listener);
	}
	
	public interface MyChangeListener {
		public void fireMyChangeEvent();
	}

	public class ModelRoot {
		private ArrayList resourceChanges;
		private ArrayList elementChanges;
		public ModelRoot() {
			resourceChanges = new ArrayList();
			elementChanges = new ArrayList();			
		}
		
		public ArrayList getResourceChanges() {
			return resourceChanges;
		}
		public ArrayList getElementChanges() {
			return elementChanges;
		}
		
		public void addElementChange(ElementChangedEvent e) {
			elementChanges.add(e);
		}
		public void addResourceChange(IResourceChangeEvent e) {
			resourceChanges.add(e);
		}
		
		
	}
}
