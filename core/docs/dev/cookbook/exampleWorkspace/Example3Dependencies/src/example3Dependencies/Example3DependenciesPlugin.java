package example3Dependencies;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import com.thoughtworks.qdox.JavaDocBuilder;

import example3Z.Example3ZPlugin;

/**
 * The main plugin class to be used in the desktop.
 */
public class Example3DependenciesPlugin extends AbstractUIPlugin {
	//The shared instance.
	private static Example3DependenciesPlugin plugin;
	
	/**
	 * The constructor.
	 */
	public Example3DependenciesPlugin() {
		super();
		plugin = this;
	}

	/**
	 * This method is called upon plug-in activation
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
	}

	/**
	 * This method is called when the plug-in is stopped
	 */
	public void stop(BundleContext context) throws Exception {
		super.stop(context);
		plugin = null;
	}

	/**
	 * Returns the shared instance.
	 */
	public static Example3DependenciesPlugin getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path.
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return AbstractUIPlugin.imageDescriptorFromPlugin("Example3Dependencies", path);
	}
	
	public void neverCalled() {
		JavaDocBuilder builder = new JavaDocBuilder();
	}
	
	public void neverCalled2() {
		Example3ZPlugin.getDefault();
	}
	
}
