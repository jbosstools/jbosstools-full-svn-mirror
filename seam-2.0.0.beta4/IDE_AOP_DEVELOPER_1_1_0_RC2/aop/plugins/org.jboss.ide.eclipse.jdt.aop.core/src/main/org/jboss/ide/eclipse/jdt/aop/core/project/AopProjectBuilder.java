/*
 * JBoss, the OpenSource J2EE webOS
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.aop.core.project;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Iterator;
import java.util.Map;

import javassist.bytecode.ClassFile;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.jboss.aop.AspectManager;
import org.jboss.aop.Deployment;
import org.jboss.aop.advice.AspectDefinition;
import org.jboss.aop.annotation.compiler.ByteCodeAnnotationCompiler;
import org.jboss.aop.standalone.Compiler;
import org.jboss.ide.eclipse.jdt.aop.core.AopCorePlugin;

/**
 * @author Marshall
 * 
 * This class uses the JBossAOP AspectManager to instrument eclipse built java class files.
 */
public class AopProjectBuilder extends IncrementalProjectBuilder {

	public static final String BUILDER_ID = "org.jboss.ide.eclipse.jdt.aop.core.AopProjectBuilder";
	
	private int classesToBuild;
	private IJavaProject javaProject;
	private URLClassLoader projectLoader;
	private ClassLoader systemLoader;
	private ByteCodeAnnotationCompiler annotationCompiler;
	private static boolean building;
	
	static {
		building = false;
	}
	
	protected void startupOnInitialize() {
		super.startupOnInitialize();
	}
	
	/**
	 * This is a long-running blocking operation. Mostly needed for our tests.
	 */
	public static void waitForBuild ()
	{
		try {
			if (!building)
			{
				System.out.println("[aop-builder] waiting for builder to start building...");
				while (!building)
					Thread.sleep((long) 300);
			}
			
			System.out.println("[aop-builder] waiting for builder to finish building...");
			while (building)
				Thread.sleep((long) 300);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void initClassLoader ()
	{
		CustomAspectManager.cleanInstance();
		CustomAspectManager.optimize = true;
		//CustomAspectManager.verbose = true;
		
		javaProject = JavaCore.create(getProject());
		AopCorePlugin.getDefault().updateAopPath(javaProject);
		
		systemLoader = Thread.currentThread().getContextClassLoader();
		projectLoader = AopCorePlugin.getDefault().getProjectClassLoader(javaProject);
		
		CustomAspectManager.instance().registerClassLoader(projectLoader);
		Thread.currentThread().setContextClassLoader(projectLoader);
	}
	
	private static class CustomAspectManager extends AspectManager
	{
		private static AspectManager myOldManager;
		private static boolean saveOldManagerData;
		
		/*
		 * Essentially steal the entire constructor of the original...
		 */
		   public static synchronized AspectManager instance()
		   {
		      if (manager != null) return manager;
		      AccessController.doPrivileged(new PrivilegedAction()
		      {
		         public Object run()
		         {
		            String optimized = System.getProperty("jboss.aop.optimized", null);
		            if (optimized != null)
		            {
		               optimize = (new Boolean(optimized)).booleanValue();
		            }
					
					// Make sure we're returning one of our own.
		            manager = new CustomAspectManager();
					
					// Then fill it with important info from the last instance.
					((CustomAspectManager)manager).saveOurOldManagerData();

		            if (!verbose)
		            {
		               verbose = (new Boolean(System.getProperty("jboss.aop.verbose", "false"))).booleanValue();
		            }
		            Deployment.deploy();
		            return null;
		         }
		      });
		      return manager;
		   }

		/**
		 * This method is here to restore things that, for some reason,
		 * are not filled in to the new manager by the xml file alone. 
		 *
		 */
		protected void saveOurOldManagerData() {
			if (myOldManager != null)
			{
				Map aspectMap = myOldManager.getAspectDefinitions();
				Iterator i = aspectMap.keySet().iterator();
				String key;
				AspectDefinition def;
				while(i.hasNext()) {
					// String -> AspectDefinition
					key = (String)i.next();
					def = (AspectDefinition)aspectMap.get(key);
					System.out.println("Adding " + def.getName() + " to new manager");
					try {
						addAspectDefinition(def);
					} catch( Exception e) {
						System.out.println("[builder - saveOurOldManagerData] " + e.getMessage());
					}
				}
			}
		}
				
		public static void  cleanInstance ()
		{
			myOldManager = manager;
			manager = null;
			saveOldManagerData = true;
		}
	}
	
	private class OptimizedClassVisitor
	{
		private IResourceVisitor visitor;
		public void accept (IResourceVisitor visitor)
		{
			this.visitor = visitor;
			try {
				getProject().accept(new IResourceVisitor() {
					public boolean visit(IResource resource) throws CoreException {
						try {
							if (resource.getFileExtension() == null)
								return true;
							if (resource.getType() == IResource.FILE && resource.getFileExtension().equals("class"))
							{
								if (isOptimizedClassName(resource.getName()))
								{
									return OptimizedClassVisitor.this.visitor.visit(resource);
								}
							}
						} catch (CoreException e) {
							e.printStackTrace();
						}
						return true;
					}
				});
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
	}
	
	// Aspect Manager blows up if it finds the generated optimized classes in the class pool when trying to regenerate them.
	// This method finds all optimized invocation classes for a given class and deletes them
	private void cleanOptimizedClasses (final String className)
	{
		try {
			final NullProgressMonitor monitor = new NullProgressMonitor();

			new OptimizedClassVisitor().accept(new IResourceVisitor () {
				public boolean visit (IResource resource) throws CoreException
				{
					try {
						String nonQualifiedClassName = 
							className.lastIndexOf(".") > -1 ?
								className.substring(className.lastIndexOf(".")) :
								className;
						if (resource.getName().indexOf(nonQualifiedClassName) != -1)
						{
							System.out.println("[aop-project-builder] removing optimized class: " + resource.getName());
							resource.delete(true, monitor);
						}
					} catch (CoreException e) {
						e.printStackTrace();
					}
					return true;
				}
			});
			
			getProject().refreshLocal(IResource.DEPTH_INFINITE, monitor);
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}
	
	private void cleanAllOptimizedClasses ()
	{
		final IProgressMonitor monitor = new NullProgressMonitor();
		
		new OptimizedClassVisitor().accept(new IResourceVisitor() {
			public boolean visit(IResource resource) throws CoreException
			{
				System.out.println("[aop-project-builder] cleaning optimized class: " + resource.getName());
				resource.delete(true, monitor);
				return true;
			}
		});
	}
	
	protected IProject[] build(int kind, Map args, IProgressMonitor monitor)
		throws CoreException
	{
		initBuild(monitor);
		
		switch (kind)
		{
			case FULL_BUILD:
			case CLEAN_BUILD:
				cleanBuild(monitor); break;
			
			case AUTO_BUILD:
			case INCREMENTAL_BUILD:
				incrementalBuild(monitor); break;
				
			default: break;
		}
		
		finishBuild(monitor);
		
		return null;
	}
	
	protected void initBuild (IProgressMonitor monitor)
		throws CoreException
	{
		building = true;
		getProject().refreshLocal(IResource.DEPTH_INFINITE, monitor);
		
		initClassLoader();
		Compiler.loader = projectLoader;
		
		annotationCompiler = new ByteCodeAnnotationCompiler();
		monitor.beginTask("Instrumenting Bound Objects...", IProgressMonitor.UNKNOWN);
	}
	
	protected void incrementalBuild (IProgressMonitor monitor)
		throws CoreException
	{
		IResourceDelta delta = getDelta(getProject());
		if (delta != null)
		{
			if (! AopCorePlugin.getDefault().hasJava50CompilerCompliance(javaProject))
			{
				delta.accept (new AnnotationBuildVisitor(monitor));
			}
			
			delta.accept (new AopBuildVisitor(monitor));
		}
	}
	
	protected void cleanBuild (IProgressMonitor monitor)
		throws CoreException
	{
		if (! AopCorePlugin.getDefault().hasJava50CompilerCompliance(javaProject))
		{
			getProject().accept(new AnnotationBuildVisitor(monitor));
		}
		
		getProject().accept(new AopBuildVisitor(monitor));
	}
	
	protected void finishBuild (IProgressMonitor monitor)
		throws CoreException
	{
		Compiler.loader = null;
		projectLoader = null;
		
		
		Thread.currentThread().setContextClassLoader(systemLoader);
		//AopCorePlugin.getDefault().updateModel(javaProject);
		
		getProject().refreshLocal(IResource.DEPTH_INFINITE, monitor);
		building = false;
	}
	
	protected class AnnotationBuildVisitor implements IResourceVisitor, IResourceDeltaVisitor
	{
		private IProgressMonitor monitor;
		
		public AnnotationBuildVisitor (IProgressMonitor monitor)
		{
			this.monitor = monitor;
		}
		
		public boolean visit(IResource resource) throws CoreException {
			visitResource(resource);
			return true;
		}
		
		public boolean visit(IResourceDelta delta) throws CoreException {
			monitor.subTask(delta.getResource().getName());
			
			switch (delta.getKind())
			{
				case IResourceDelta.ADDED:
				case IResourceDelta.CHANGED:
				case IResourceDelta.CONTENT:
					visitResource(delta.getResource()); break;
				
				default: break;
			}
			
			monitor.worked(1);
			return true;
		}
		
		private void visitResource (IResource resource)
		{
			if (resource.getType() == IResource.FILE && resource.getFileExtension() != null)
			{
				if (resource.getFileExtension().equals("java"))
				{
					monitor.subTask(resource.getName());
					
					compileAnnotations(resource);
					
					monitor.worked(1);
				}
				
			}
		}
	}
	
	protected class AopBuildVisitor implements IResourceVisitor, IResourceDeltaVisitor
	{
		private IProgressMonitor monitor;
		
		public AopBuildVisitor (IProgressMonitor monitor)
		{
			this.monitor = monitor;
		}
		
		public boolean visit (IResource resource)
			throws CoreException
		{
			visitResource(resource);
			return true;
		}
		
		public boolean visit (IResourceDelta delta)
			throws CoreException
		{
			IResource resource = delta.getResource();
		
			monitor.subTask(resource.getName());
			
			switch (delta.getKind())
			{
				case IResourceDelta.ADDED:
				case IResourceDelta.CHANGED:
				case IResourceDelta.CONTENT:
					visitResource(resource); break;			
				default: break;
			}
				
			monitor.worked(1);
		
			return true;
		}
		
		private void visitResource (IResource resource)
		{
			if (resource.getType() == IResource.FILE && resource.getFileExtension() != null)
			{
				if (resource.getFileExtension().equals("class"))
				{
					monitor.subTask(resource.getName());
					
					compile(resource);
					
					monitor.worked(1);
				}
				
			}
		}
	}
	
	private void compileAnnotations (IResource resource)
	{
		try {
			annotationCompiler.compile(new String[] { resource.getRawLocation().toFile().getAbsolutePath() });
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void compile(IResource resource)
	{
		compile (resource, -2);
	}
	
	private void compile(IResource resource, int deltaKind)
	{
		try {
			File resourceFile = resource.getRawLocation().toFile();
			if (! isOptimizedClassName(resource.getName()))
			{
				DataInputStream is = new DataInputStream(new FileInputStream(resourceFile));
				ClassFile cf = new ClassFile(is);
				is.close();
				
				String className = cf.getName();
				cleanOptimizedClasses(className);
				URL classUrl = projectLoader.getResource(className.replace('.', '/') + ".class");
				
//				if (deltaKind == IResourceDelta.ADDED)
//				{
//					projectLoader.addURL (classUrl);
//				}
				
				byte[] bytes = CustomAspectManager.instance().transform(projectLoader, className, null, null, null);
				
				if (bytes != null)
				{
					FileOutputStream os = new FileOutputStream(resourceFile);
					os.write(bytes);
					os.close();
					System.out.println("[aop-builder] succesfully transformed " + className);
				}
				else {
					System.out.println("[aop-builder] " + className + " was unchanged");
				}
			}
		} catch (NoClassDefFoundError er) {
			er.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
	private boolean isOptimizedClassName (String className)
	{
		return (
			className.indexOf("OptimizedMethodInvocation") != -1
			|| className.indexOf("OptimizedConstructorInvocation") != -1
			|| className.indexOf("OptimizedGetFieldInvocation") != -1
			|| className.indexOf("OptimizedSetFieldInvocation") != -1);
	}
}
