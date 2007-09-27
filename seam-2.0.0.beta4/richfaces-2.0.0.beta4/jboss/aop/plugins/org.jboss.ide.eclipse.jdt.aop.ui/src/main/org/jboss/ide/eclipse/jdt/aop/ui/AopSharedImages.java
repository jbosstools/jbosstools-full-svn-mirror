/*
 * JBoss, the OpenSource J2EE webOS
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.aop.ui;

import java.util.Hashtable;
import java.util.Iterator;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.osgi.framework.Bundle;

/**
 * @author Marshall
 *
 * A class that keeps references and disposes of the UI plugin's images
 */
public class AopSharedImages {

	public static final String IMG_ASPECT = "aspect";
	public static final String IMG_INTERCEPTOR = "interceptor";
	public static final String IMG_INTERCEPTOR_24 = "interceptor24";
	public static final String IMG_POINTCUT = "pointcut";
	public static final String IMG_BINDING = "binding";
	public static final String IMG_NEW_BINDING = "newbinding";
	public static final String IMG_ADVICE = "advice";
	public static final String IMG_ADVICE_24 = "advice24";
	
	private static AopSharedImages instance;
	
	private Hashtable images, descriptors;
	
	private AopSharedImages ()
	{
		instance = this;
		images = new Hashtable();
		descriptors = new Hashtable();
		Bundle pluginBundle = AopUiPlugin.getDefault().getBundle();
		
		descriptors.put(IMG_ASPECT, ImageDescriptor.createFromURL(pluginBundle.getEntry("/icons/aspect.gif")));
		descriptors.put(IMG_INTERCEPTOR,  ImageDescriptor.createFromURL(pluginBundle.getEntry("/icons/interceptor.gif")));
		descriptors.put(IMG_INTERCEPTOR_24,  ImageDescriptor.createFromURL(pluginBundle.getEntry("/icons/interceptor24.gif")));
		descriptors.put(IMG_POINTCUT, ImageDescriptor.createFromURL(pluginBundle.getEntry("/icons/pointcut.gif")));
		descriptors.put(IMG_BINDING, ImageDescriptor.createFromURL(pluginBundle.getEntry("/icons/binding.gif")));
		descriptors.put(IMG_NEW_BINDING, ImageDescriptor.createFromURL(pluginBundle.getEntry("/icons/newbinding.gif")));
		descriptors.put(IMG_ADVICE, ImageDescriptor.createFromURL(pluginBundle.getEntry("/icons/advice.gif")));
		descriptors.put(IMG_ADVICE_24, ImageDescriptor.createFromURL(pluginBundle.getEntry("/icons/advice24.gif")));
		
		Iterator iter = descriptors.keySet().iterator();
		while (iter.hasNext())
		{
			String key = (String) iter.next();
			ImageDescriptor descriptor = descriptor(key);
			images.put(key,  descriptor.createImage());	
		}
		
	}
	
	private static AopSharedImages instance() {
		if (instance == null)
			new AopSharedImages();
		
		return instance;
	}
	
	public static Image getImage(String key)
	{
		return instance().image(key);
	}
	
	public static ImageDescriptor getImageDescriptor(String key)
	{
		return instance().descriptor(key);
	}
	
	public Image image(String key)
	{
		return (Image) images.get(key);
	}
	
	public ImageDescriptor descriptor(String key)
	{
		return (ImageDescriptor) descriptors.get(key);
	}
	
	protected void finalize() throws Throwable {
		Iterator iter = images.keySet().iterator();
		while (iter.hasNext())
		{
			Image image = (Image) images.get(iter.next());
			image.dispose();
		}
		super.finalize();
	}
}
