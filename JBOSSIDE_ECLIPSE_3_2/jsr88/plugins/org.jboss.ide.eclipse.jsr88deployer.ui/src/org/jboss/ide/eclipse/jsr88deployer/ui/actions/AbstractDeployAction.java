package org.jboss.ide.eclipse.jsr88deployer.ui.actions;

import java.util.Iterator;

import org.eclipse.core.resources.IResource;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;




public abstract class AbstractDeployAction implements IObjectActionDelegate, IWorkbenchWindowActionDelegate {

	/* Static variables */
	public static final int UNDEPLOYABLE = -1;
	public static final int DESCRIPTOR = 0;
	public static final int DEPLOYABLE = 1;
	public static final int J2EEAPP = 2;

	
	/** Description of the Field */
	   protected IWorkbenchPart part = null;
	   /** Description of the Field */
	   protected ISelection selection = null;
	   /** Description of the Field */
	   protected IWorkbenchWindow window = null;
	   protected IAction action = null;


	   /**Constructor for the XDocletRunAction object */
	   public AbstractDeployAction()
	   {
	      super();
	   }


	   /** Description of the Method */
	   public void dispose() { }


	   /**
	    * Description of the Method
	    *
	    * @param window  Description of the Parameter
	    */
	   public void init(IWorkbenchWindow window)
	   {
	      this.window = window;
	   }


	   /**
	    * @param action  Description of the Parameter
	    */
	   public void run(IAction action)
	   {
	      if (this.selection != null && (this.selection instanceof IStructuredSelection))
	      {
	         IStructuredSelection sel = (IStructuredSelection) this.selection;
	         if( sel.size() > 1 ) {
	        	 // only one deploy at a time
	        	 return;
	         }
	         
	         Iterator it = sel.iterator();

	         /*
	         Collection resources = new ArrayList();
	         while (it.hasNext())
	         {
	            Object o = it.next();
	            // For each resource
	            if (o instanceof IResource)
	            {
	               resources.add(o);
	            }
	         }
	         this.process(resources);
	         */
	         
	         Object o = it.next();
	         if( o instanceof IResource ) {
	        	 this.process((IResource)o);
	         }
	      }
	   }


	   /**
	    * Description of the Method
	    *
	    * @param action     Description of the Parameter
	    * @param selection  Description of the Parameter
	    */
	   public void selectionChanged(IAction action, ISelection selection)
	   {
	      this.selection = selection;
	      this.action = action;
	   }


	   /**
	    * @param action      The new ActivePart value
	    * @param targetPart  The new ActivePart value
	    * @see               org.eclipse.ui.IObjectActionDelegate#setActivePart(IAction, IWorkbenchPart)
	    */
	   public void setActivePart(IAction action, IWorkbenchPart targetPart)
	   {
	      this.part = targetPart;
	   }

	   
	   protected static int getDeploymentType(IResource resource) {
		   String extension = resource.getFileExtension().toLowerCase();
		   if( extension.equals("ear")) return AbstractDeployAction.J2EEAPP;
		   if( extension.equals("xml")) return AbstractDeployAction.DESCRIPTOR;
		   if( extension.equals("sar") || extension.equals("war")
		   	|| extension.equals("jar") || extension.equals("rar"))
			   return AbstractDeployAction.DEPLOYABLE;
		   
		   return AbstractDeployAction.UNDEPLOYABLE;
	   }
	   
	   protected abstract void process(IResource resource);
	   
	   
}
