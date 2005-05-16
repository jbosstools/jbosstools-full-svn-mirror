package org.jboss.ide.eclipse.jdt.aop.core.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ElementChangedEvent;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IElementChangedListener;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaElementDelta;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.core.CompilationUnit;
import org.eclipse.jdt.internal.core.JavaElementDelta;
import org.jboss.ide.eclipse.jdt.aop.core.model.interfaces.IAopAdvised;
import org.jboss.ide.eclipse.jdt.aop.core.model.internal.AopAdvised;
import org.jboss.ide.eclipse.jdt.aop.core.model.internal.AopTypedef;
import org.jboss.ide.eclipse.jdt.aop.core.model.internal.ProjectAdvisors;
import org.jboss.ide.eclipse.jdt.aop.core.pointcut.JDTPointcutExpression;
import org.jboss.ide.eclipse.jdt.aop.core.pointcut.JDTTypedefExpression;

/**
 * @author Rob Stryker
 */
public class AopModelElementChangedListener implements IElementChangedListener {
	
	public AopModelElementChangedListener() {
		JavaCore.addElementChangedListener(this);
	}
	
	
	public void elementChanged (ElementChangedEvent event)
	{
		ArrayList changed = new ArrayList();
		IJavaProject project = event.getDelta().getElement().getJavaProject();
		ICompilationUnit unit = elementChangedGetCompilationUnit(event);
		if( project == null ) {
			if( unit == null ) {
				//System.out.println("Problem"); 
				return;
			}
			project = unit.getJavaProject();
			//System.out.println("Saved");
		}

		boolean annotationsHaveChanged = false;
		
		if( event.getDelta().getElement() instanceof CompilationUnit ) {
			annotationsHaveChanged = true;
		}

		
		// Check to make sure typedef integrity remains.
		reconcileTypedefs(unit);

		
		changed.addAll(elementChangedGetAllAffected(event.getDelta()));		
		
		
		
		// The removed elements should be at the top now.  The added elements at the bottom. 
		Collections.sort(changed, new Comparator() {

			public int compare(Object first, Object second) {
				if( ((IJavaElementDelta)first).getKind() ==  ((IJavaElementDelta)second).getKind())
					return 0;
				
				if(((IJavaElementDelta)first).getKind() == IJavaElementDelta.REMOVED ) {
					return -1;
				}
				
				return 1;
			}
			
		});
		JDTPointcutExpression expressions[] = AopModel.instance().getProjectPointcuts( project );

		for( Iterator i = changed.iterator(); i.hasNext();) {
			IJavaElementDelta t = ((IJavaElementDelta)(i.next()));

			if( t.getKind() == IJavaElementDelta.ADDED ) 
				handleElementAdded(t, expressions);
			else 
				handleElementRemoved(t, expressions);

		}

		
		
		
		// Check the annotations
		if( annotationsHaveChanged ) {
			try {
				IType types[] = ((CompilationUnit)event.getDelta().getElement()).getAllTypes();
				for( int i = 0; i < types.length; i++ ) {
					elementChangedCompilationUnit(types[i], expressions);
				}
			} catch( CoreException e ) {
				//e.printStackTrace();
			}
		}
		
	}

	
	/**
	 * This method is used when elementChanged's delta's element is not directly
	 * associated with a project. Here, we search for its compilation unit
	 * recursively through the deltas until we find it. 
	 * 
	 * @param event The element changed event sent to the elementChanged method
	 * @return The compilation unit that's been changed
	 */
	public ICompilationUnit elementChangedGetCompilationUnit(ElementChangedEvent event) {
		ArrayList list = elementChangedGetAllAffected(event.getDelta(), true);
		Iterator i = list.iterator();
		while( i.hasNext() ) {
			JavaElementDelta delta = (JavaElementDelta)i.next();
			IJavaElement element = delta.getElement();
			if( element instanceof CompilationUnit ) {
				return (CompilationUnit)element;
			}
		}
		return null;
	}
	
	
	

	
	/**
	 * Convenience method
	 */
	private ArrayList elementChangedGetAllAffected(IJavaElementDelta delta ) {
		return elementChangedGetAllAffected(delta, false);
	}
	
	/**
	 * This is a private method that will recursively get a list
	 * of added, or deleted children to be used by the elementChanged 
	 * method. 
	 * It will not include anything except methods or fields. 
	 * It will not include changed elements. Only added or removed.
	 * @param delta The original event's delta
	 * @param includeCompilationUnit  Do we include the compilation unit in the returned list?
	 * @return tmp An arraylist 
	 */
	private ArrayList elementChangedGetAllAffected(IJavaElementDelta delta, boolean includeCompilationUnit) {
		ArrayList changed = new ArrayList();
//		System.out.println("[element changed get all affected] - " + delta.getElement().getClass().getName()
//				+ " - " + delta.getElement().getElementName() + ", " + delta.getKind());
		IJavaElementDelta changedChildren[] = delta.getAffectedChildren();
		if( delta.getElement() instanceof IMethod 
				|| delta.getElement() instanceof IField 
				|| delta.getElement() instanceof IType
				|| (delta.getElement() instanceof CompilationUnit && includeCompilationUnit)) {
			
			// We only care about added or removed... not changed.
			if( delta.getKind() != IJavaElementDelta.CHANGED)
				changed.add(delta);

			// unless we're including the compilation unit
			if( includeCompilationUnit && delta.getKind() == IJavaElementDelta.CHANGED) {
				changed.add(delta);				
			}
		}
		for( int i = 0; i < changedChildren.length; i++ ) {
			changed.addAll(elementChangedGetAllAffected(changedChildren[i], includeCompilationUnit));
		}
		return changed;
	}
	

	
	
	
	/**
	 * A new element has been added through a delta to the elementChanged method. 
	 * We must check if it matches anything, and if so, add the match to the model.
	 * 
	 * 
	 * @param addedDelta
	 * @param expressions
	 */
	private void handleElementAdded (IJavaElementDelta addedDelta, JDTPointcutExpression expressions[])
	{
		IJavaElement added = addedDelta.getElement();
		
		if( added.getElementType() == IJavaElement.TYPE) {
			AopModel.instance().registerType((IType)added);
		} else {
			for (int j = 0; j < expressions.length; j++)
			{
				if (added.getElementType() == IJavaElement.METHOD)
				{
					if (expressions[j].matchesExecution((IMethod) added))
					{
						AopModel.instance().addAdvisedToPointcutAdvisors(added, IAopAdvised.TYPE_METHOD_EXECUTION, expressions[j]);
					}
				}
				else if (added.getElementType() == IJavaElement.FIELD)
				{
					if (expressions[j].matchesGet((IField) added))
					{
						AopModel.instance().addAdvisedToPointcutAdvisors(added, IAopAdvised.TYPE_FIELD_GET, expressions[j]);
					}
					if (expressions[j].matchesSet((IField) added))
					{
						AopModel.instance().addAdvisedToPointcutAdvisors(added, IAopAdvised.TYPE_FIELD_SET, expressions[j]);
					}
				}
			}
		}
	}
	
	/**
	 * An element has been removed through a delta to the changeEvent method.
	 * 
	 * @param removedDelta
	 * @param expressions
	 */
	private void handleElementRemoved (IJavaElementDelta removedDelta, JDTPointcutExpression expressions[])
	{
		IJavaElement removed = removedDelta.getElement();
		if( removed instanceof IMethod ) elementChangedRemoveMethod(((IMethod)removed), expressions);
		if( removed instanceof IField ) elementChangedRemoveField((IField)removed, expressions);
		if( removed instanceof IType ) elementChangedRemoveType((IType)removed, expressions);
	}
	
	private void elementChangedRemoveMethod(IMethod method, JDTPointcutExpression expressions[])
	{
		IJavaProject project = method.getCompilationUnit().getJavaProject();
		for( int i = 0; i < expressions.length; i++ ) {
			IAopAdvised advised = expressions[i].getAdvised(method);
			if( advised != null ) AopModel.instance().removeAdvised(project, new IAopAdvised[] { advised });
		}
	}
	

	private void elementChangedRemoveField(IField field, JDTPointcutExpression expressions[]) {
		IJavaProject project = field.getCompilationUnit().getJavaProject();
		for( int i = 0; i < expressions.length; i++ ) {
			IAopAdvised getVal = 
				expressions[i].getAdvised(field, IAopAdvised.TYPE_FIELD_GET);
			IAopAdvised setVal = 
				expressions[i].getAdvised(field, IAopAdvised.TYPE_FIELD_SET);
			if( getVal != null ) AopModel.instance().removeAdvised(project, new IAopAdvised[] { getVal });
			if( setVal != null ) AopModel.instance().removeAdvised(project, new IAopAdvised[] { setVal });
		}
	}
	
	private void elementChangedRemoveType(IType type, JDTPointcutExpression expressions[] ) {
		AopModel.instance().removeSourceElement(type);
	}
	/**
	 * This method is only reached if the compilation unit has changed,
	 * which implies the annotations have changed.
	 * 
	 * This will add or remove advisors based on the changed annotations.
	 * Because the jdt model here only returns a compilation unit, 
	 * we're not exactly sure what's changed, so we have to check
	 * every method, field, and inner class for changes.
	 * 
	 * @param type
	 * @param expressions
	 */
	private void elementChangedCompilationUnit(IType type, JDTPointcutExpression expressions[] ) {
		try {
			IMethod[] methods = type.getMethods();
			IField[] fields = type.getFields();
			IType[] innerTypes = type.getTypes();
						

			/*
			 * Checking what fields are changed and update 
			 * the model accordingly. 
			 */
			for( int i = 0; i < fields.length; i++ ) {
				elementChangedVerifyField( fields[i], expressions);
			}
			
			/*
			 * Check what methods (and constructors) are changed 
			 * and update the model accordingly. 
			 */
			for( int i = 0; i < methods.length; i++ ) {
				elementChangedVerifyMethod(methods[i], expressions);
			}
			
			/*
			 * Finally, we should go recursively through subtypes.
			 */
			for( int i = 0; i < innerTypes.length; i++) {
				elementChangedCompilationUnit(innerTypes[i], expressions);
			}
			
			
		} catch (JavaModelException e ) {
		}
	}
	
	private void elementChangedVerifyField( IField field, JDTPointcutExpression expressions[]) {
		for( int j = 0; j < expressions.length; j++ ) {
			boolean matchesGet = expressions[j].matchesGet(field);
			boolean matchesSet = expressions[j].matchesSet(field);
			IAopAdvised getVal = 
				expressions[j].getAdvised(field, IAopAdvised.TYPE_FIELD_GET);
			IAopAdvised setVal = 
				expressions[j].getAdvised(field, IAopAdvised.TYPE_FIELD_SET);
			
			
			if( matchesGet && getVal == null ) {  // add him
				AopModel.instance().addAdvisedToPointcutAdvisors(field, IAopAdvised.TYPE_FIELD_GET, expressions[j]);
			} else if( !matchesGet && getVal != null ) { // remove him
				AopModel.instance().removeAdvisedFromPointcutAdvisors(getVal, expressions[j]);
			}
			
			if( matchesSet && setVal == null ) {
				AopModel.instance().addAdvisedToPointcutAdvisors(field, IAopAdvised.TYPE_FIELD_SET, expressions[j]);						
			} else if( !matchesSet && setVal != null ) {
				AopModel.instance().removeAdvisedFromPointcutAdvisors(setVal, expressions[j]);
			}
		}
	}

	private void elementChangedVerifyMethod(IMethod method, JDTPointcutExpression expressions[]) {
		for( int j = 0; j < expressions.length; j++ ) {
			boolean matches = expressions[j].matchesExecution(method);
			IAopAdvised advisedObj = expressions[j].getAdvised(method);
			
			if( matches && advisedObj == null ) {
				AopModel.instance().addAdvisedToPointcutAdvisors(method, 
						IAopAdvised.TYPE_METHOD_EXECUTION, expressions[j]);
			} else if( !matches && advisedObj != null ) {
				AopModel.instance().removeAdvisedFromPointcutAdvisors(advisedObj, expressions[j]);
			}
		}
	}


	
	
	private void reconcileTypedefs(ICompilationUnit unit) {
		try {
			IType[] types = unit.getAllTypes();
			IJavaProject project = unit.getJavaProject();
			ProjectAdvisors advisors = AopModel.instance().getProjectAdvisors(project);
			
			AopTypedef[] typedefs = advisors.getTypedefs();
			for( int i = 0; i < typedefs.length; i++ ) {
				JDTTypedefExpression expr = typedefs[i].getTypedef();
				for( int j = 0; j < types.length; j++ ) {
					boolean currentlyAdvises = typedefs[i].advises(types[j]);
					boolean shouldAdvise = expr.matches(types[j]);
					
//					System.out.println("currently: " + currentlyAdvises + ", should: " + shouldAdvise);
					
					if( currentlyAdvises  &&  shouldAdvise ) continue;
					if( !currentlyAdvises && !shouldAdvise ) continue;
					

					if( shouldAdvise ) {
						IAopAdvised advised = new AopAdvised(IAopAdvised.TYPE_CLASS, types[j]);
						typedefs[i].addAdvised(advised);
						AopModel.instance().fireAdvisorAdded(advised, typedefs[i]);
					} else {
						IAopAdvised advised = typedefs[i].getAdvised(types[j]);
						typedefs[i].removeAdvised(advised);
						AopModel.instance().fireAdvisorRemoved(advised, typedefs[i]);
					}
					
					
					
				}
			}
		} catch( Exception e) {
			System.out.println("[reconcileTypedefs error]");
			e.printStackTrace();
		}
	}
}
