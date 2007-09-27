/*
 * JBoss, the OpenSource J2EE webOS
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.aop.core.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;

import javax.xml.bind.JAXBException;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.IClassFile;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.Signature;
import org.eclipse.jdt.internal.corext.util.JavaModelUtil;
import org.jboss.ide.eclipse.jdt.aop.core.AopCorePlugin;
import org.jboss.ide.eclipse.jdt.aop.core.jaxb.AopReport;
import org.jboss.ide.eclipse.jdt.aop.core.jaxb.CompilationUnit;
import org.jboss.ide.eclipse.jdt.aop.core.jaxb.PackageFragment;
import org.jboss.ide.eclipse.jdt.aop.core.jaxb.ReportAdvice;
import org.jboss.ide.eclipse.jdt.aop.core.jaxb.ReportInterceptor;
import org.jboss.ide.eclipse.jdt.aop.core.jaxb.UnboundBindings;
import org.jboss.ide.eclipse.jdt.aop.core.jaxb.Constructors.Constructor;
import org.jboss.ide.eclipse.jdt.aop.core.jaxb.Fields.Field;
import org.jboss.ide.eclipse.jdt.aop.core.jaxb.Methods.Method;
import org.jboss.ide.eclipse.jdt.aop.core.model.internal.ReportModelAdvice;
import org.jboss.ide.eclipse.jdt.aop.core.model.internal.ReportModelAdvised;
import org.jboss.ide.eclipse.jdt.aop.core.model.internal.ReportModelInterceptor;

/**
 * @author Marshall
 * 
 * This is the model for the advised children view, and the advised editor marker. 
 */
public class AopReportModel {
	private Hashtable projectAdvisors;
	private Hashtable projectListeners;
	private ArrayList globalListeners;
	private static AopReportModel instance;
	
	private CompilationUnit topLevelClass;
	private PackageFragment topLevelPackage;
	private IJavaElement topLevelElement;
	
	public static AopReportModel instance ()
	{
		if (instance == null)
			instance = new AopReportModel();
		
		return instance;
	}
	
	protected AopReportModel ()
	{
		projectListeners = new Hashtable();
		projectAdvisors = new Hashtable();
		globalListeners = new ArrayList();
		
	}
	
	/**
	 * Add a Report Model Listener to all projects and elements.
	 * 
	 * @param listener The listener to register
	 */
	public void addReportModelListener (IReportModelListener listener)
	{
		if (!globalListeners.contains(listener))
		{
			globalListeners.add(listener);
		}
	}
	
	/**
	 * Add a Report Model Listener to a specific project and it's elements
	 * @param project The specific project who's elements this listener will receive events for
	 * @param listener The listener to register
	 */
	public void addReportModelListener (IJavaProject project, IReportModelListener listener)
	{
		ArrayList listeners = getProjectListeners(project);
		
		if (! listeners.contains(listener))
		{
			// We want to make sure that "late" (after the model has been created)
			// listener registrations still get the info they expect
			Hashtable advisors = getProjectAdvisors(project);
			for (Iterator iter = advisors.keySet().iterator(); iter.hasNext(); )
			{
				IReportModelAdvised advised = (IReportModelAdvised) iter.next();
				Collection elementAdvisors = getElementAdvisors(advised);
				
				for (Iterator aIter = elementAdvisors.iterator(); aIter.hasNext(); )
				{
					IReportModelAdvisor advisor = (IReportModelAdvisor) aIter.next();
					fireSingleAdvisorEvent(listener, advised, advisor, ADVISOR_ADDED);
				}
			}
			
			listeners.add(listener);
		}
	}

	public void removeReportModelListener(IReportModelListener listener)
	{
		for (Iterator kIter = projectListeners.keySet().iterator(); kIter.hasNext(); )
		{
			ArrayList listeners = (ArrayList) projectListeners.get(kIter.next());
			
			if (listeners.contains(listener))
			{
				listeners.remove(listener);
			}
		}
	}
	
	public Hashtable getProjectAdvisors (IJavaProject project)
	{
		Hashtable table = (Hashtable) projectAdvisors.get(project);
		if (table == null)
		{
			table = new Hashtable();
			projectAdvisors.put(project, table);
		}
		
		return table;
	}
	
	public IReportModelAdvised findReportModelAdvised (IJavaElement element)
	{
		IReportModelAdvised advised[] = findAllReportModelAdvised(element);
		
		if (advised.length > 0)
			return advised[0];
		
		return null;
	}
	
	public IReportModelAdvised[] findAllReportModelAdvised (IJavaElement element)
	{
		ArrayList advisedElements = new ArrayList();
		
		Hashtable advisors = getProjectAdvisors(element.getJavaProject());
		for (Iterator kIter = advisors.keySet().iterator(); kIter.hasNext();)
		{
			IReportModelAdvised advised = (IReportModelAdvised) kIter.next();
			
			if (advised.getAdvisedElement().equals(element))
			{
				advisedElements.add(advised);
			}
		}
		
		return (IReportModelAdvised[]) advisedElements.toArray(new IReportModelAdvised[advisedElements.size()]);
	}
	
	public Collection getElementAdvisors (IJavaElement element)
	{	
		IReportModelAdvised advised = findReportModelAdvised(element);
		
		if (advised != null)
			return getElementAdvisors (advised);
		
		return null;
	}
	
	public Collection getElementAdvisors (IReportModelAdvised advised)
	{
		Hashtable advisors = getProjectAdvisors(advised.getAdvisedElement().getJavaProject());
		Collection elementAdvisors = (Collection) advisors.get(advised);
		if (elementAdvisors == null)
		{
			elementAdvisors = new ArrayList();
			advisors.put(advised, elementAdvisors);
		}
		
		return elementAdvisors;
	}
	
	private ArrayList getProjectListeners (IJavaProject project)
	{
		ArrayList listeners = (ArrayList) projectListeners.get(project);
		if (listeners == null)
		{
			listeners = new ArrayList();
			projectListeners.put(project, listeners);
		}
		
		return listeners;
	}
	
	protected static final int ADVISOR_ADDED = 1;
	protected static final int ADVISOR_REMOVED = 2;
	
	protected void fireAdvisorEvent (IReportModelAdvised advised, IReportModelAdvisor advisor, int type)
	{
		ArrayList listeners = getProjectListeners(advised.getAdvisedElement().getJavaProject());
		
		for (Iterator iter = listeners.iterator(); iter.hasNext(); )
		{
			IReportModelListener listener = (IReportModelListener) iter.next();
			fireSingleAdvisorEvent (listener, advised, advisor, type);
		}
		
		for (Iterator iter = globalListeners.iterator(); iter.hasNext(); )
		{
			IReportModelListener listener = (IReportModelListener) iter.next();
			fireSingleAdvisorEvent (listener, advised, advisor, type);
		}
	}
	
	protected void fireSingleAdvisorEvent (IReportModelListener listener, IReportModelAdvised advised,
		IReportModelAdvisor advisor, int type)
	{
		switch (type)
		{
			case ADVISOR_ADDED:
			{
				listener.advisorAdded(advised, advisor);
			} break;
			case ADVISOR_REMOVED:
			{
				listener.advisorRemoved(advised, advisor);
			} break;
		}
	}
	
	public void clearProjectModel (IJavaProject project)
	{
		Hashtable advisors = getProjectAdvisors(project);
		
		for (Iterator iter = advisors.keySet().iterator(); iter.hasNext(); )
		{
			IReportModelAdvised advised = (IReportModelAdvised) iter.next();
			Collection elementAdvisors = getElementAdvisors(advised);
			
			for (Iterator aIter = elementAdvisors.iterator(); aIter.hasNext(); )
			{
				IReportModelAdvisor advisor = (IReportModelAdvisor) aIter.next();
				aIter.remove();
				
				fireAdvisorEvent (advised, advisor, ADVISOR_REMOVED);
			}
			iter.remove();
		}
		
		advisors.clear();
	}
	
	public void updateModel (IJavaProject project)
	{
		AopCorePlugin.setCurrentJavaProject(project);
		clearProjectModel(project);
		
		AopReport report = AopCorePlugin.getDefault().getProjectReport(project);
		if (report != null && report.getElements() != null)
		{
			try {
				Iterator eIter = report.getElements().iterator();
				
				while (eIter.hasNext())
				{
					Object reportElement = eIter.next();
					if (reportElement instanceof UnboundBindings) {
						// For now we'll do nothing, in the future we'll probably want to add error markers in the Aspect Manager view
					}
					else if (reportElement instanceof CompilationUnit) {
						processCompilationUnit((CompilationUnit) reportElement);
					}
					else if (reportElement instanceof PackageFragment) {
						processPackageFragment((PackageFragment) reportElement);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private void processCompilationUnit (CompilationUnit unit)
		throws CoreException, JAXBException
	{
		processCompilationUnit(unit, unit.getName());
	}
	
	private void addAdvisors (IReportModelAdvised advised, Collection advisors)
		throws CoreException
	{
		System.out.println("add advisors ("+advised.getAdvisedElement().getElementName() + ")");
		
		Collection currentAdvisors = getElementAdvisors(advised);
		ArrayList modelAdvisors = toAdvisors(advisors);
		
		for (Iterator iter = modelAdvisors.iterator(); iter.hasNext(); )
		{
			IReportModelAdvisor advisor = (IReportModelAdvisor) iter.next();
			currentAdvisors.add(advisor);
			
			fireAdvisorEvent(advised, advisor, ADVISOR_ADDED);
		}
	}
	
	private IReportModelAdvised createAdvisedFromMethodExecution (IMethod advised)
	{
		return new ReportModelAdvised(IReportModelAdvised.TYPE_METHOD_EXECUTION, advised);
	}
	
	private IReportModelAdvised createAdvisedFromFieldSet (IField advised)
	{
		return new ReportModelAdvised(IReportModelAdvised.TYPE_FIELD_SET, advised);
	}
	
	private IReportModelAdvised createAdvisedFromFieldGet (IField advised)
	{
		return new ReportModelAdvised(IReportModelAdvised.TYPE_FIELD_GET, advised);
	}
	
	private void processCompilationUnit (CompilationUnit unit, String fullClassName)
		throws CoreException, JAXBException
	{
		Iterator ctorIter = unit.getConstructors().getConstructor().iterator();
		while (ctorIter.hasNext())
		{
			Constructor constructor = (Constructor) ctorIter.next();
			IMethod method = findConstructor(fullClassName, constructor.getSignature());
			
			addAdvisors (createAdvisedFromMethodExecution(method), constructor.getInterceptors().getInterceptorOrAdvice());
		}
		
		Iterator mIter = unit.getMethods().getMethod().iterator();
		while (mIter.hasNext())
		{
			Method method = (Method) mIter.next();
			IMethod m = (IMethod) findJavaElement(method.getSignature());
			
			addAdvisors (createAdvisedFromMethodExecution(m), method.getInterceptors().getInterceptorOrAdvice());
		}
		
		Iterator fIter = unit.getFieldsRead().getField().iterator();
		while (fIter.hasNext())
		{
			Field field = (Field) fIter.next();
			IField f = (IField) findJavaElement(fullClassName + "." + field.getName());
			
			addAdvisors (createAdvisedFromFieldGet(f), field.getInterceptors().getInterceptorOrAdvice());
		}
		
		fIter = unit.getFieldsWrite().getField().iterator();
		while (fIter.hasNext())
		{
			Field field = (Field) fIter.next();
			IField f = (IField) findJavaElement(fullClassName + "." + field.getName());
			
			addAdvisors (createAdvisedFromFieldSet(f), field.getInterceptors().getInterceptorOrAdvice());
		}
	}
	
	private void processPackageFragment (PackageFragment fragment)
		throws CoreException, JAXBException
	{
	    topLevelPackage = fragment;
		processPackageFragment (fragment, fragment.getName());
	}
	
	private void processPackageFragment (PackageFragment fragment, String packageName)
		throws CoreException, JAXBException
	{
		Iterator pocIter = fragment.getPackageOrClass().iterator();
		
		while (pocIter.hasNext())
		{
			Object pkgOrClass = pocIter.next();
			if (pkgOrClass instanceof PackageFragment.Package)
			{
				PackageFragment.Package pkg = (PackageFragment.Package) pkgOrClass;
				processPackageFragment (pkg, packageName == null ? pkg.getName() : packageName + "." + pkg.getName());
			}
			else if (pkgOrClass instanceof PackageFragment.Class)
			{
				PackageFragment.Class clazz = (PackageFragment.Class) pkgOrClass;
				processCompilationUnit (clazz, packageName == null ? clazz.getName() : packageName + "." + clazz.getName());
			}
		}
	}
	
	private boolean isFlag (String flag)
	{
		return flag != null && 
			(flag.equals("public") || flag.equals("protected") || flag.equals("private")
			|| flag.equals("static") || flag.equals("abstract") || flag.equals("final")
			|| flag.equals("native") || flag.equals("synchronized") || flag.equals("transient")
			|| flag.equals("volatile") || flag.equals("strictfp"));
	}
	
	private String stripArgType (String argType)
	{
		return argType.replaceAll("[,()]", "");
	}
	
	private IMethod findConstructor (String fullClassName, String reportSignature)
	{
		// All we really care about is the arguments to the constructor
		int leftParen = reportSignature.indexOf('(');
		int rightParen = reportSignature.indexOf(')');
		
		String argsString = reportSignature.substring(leftParen + 1, rightParen);
		String argTypes[] = argsString.split(",");

		try
		{
			IType type = AopCorePlugin.getCurrentJavaProject().findType(fullClassName);
			IMethod methods[] = type.getMethods();
			
			for (int i = 0; i < methods.length; i++)
			{
				IMethod method = methods[i];
				if (method.isConstructor())
				{
					if (leftParen == rightParen - 1 && methods[i].getNumberOfParameters() == 0)
					{
						return methods[i];
					}
					
					else if (argTypes.length > 0) 
					{
						boolean signaturesAreSame = true;
						for (int j = 0; j < argTypes.length; j++)
						{
							String argType = JavaModelUtil.getResolvedTypeName(methods[i].getParameterTypes()[j], type);
							
							if (! argType.equals(argTypes[j]))
							{
								signaturesAreSame = false;
								break;
							}
						}
						
						if (signaturesAreSame)
						{
							return methods[i];
						}
					}
				}
			}
		}
		catch (JavaModelException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	private IJavaElement findJavaElement (String reportSignature)
		throws CoreException
	{
		System.out.println("find java element: " + reportSignature);
		
		if (reportSignature != null)
		{
			String tokens[] = reportSignature.split(" ");
			
			if (tokens.length > 1)
			{
				// Methods have signature with "flags returnType classname.methodname(args..)"
				ArrayList flags = new ArrayList();
				String returnType = "";
				ArrayList argTypes = new ArrayList();
				String className = "";
				String methodName = "";
				boolean inFlags = true;
				boolean inClassAndMethod = false;
				boolean inArgs = false;
				
				for (int i = 0; i < tokens.length; i++)
				{
					if (inFlags)
					{
						if (isFlag(tokens[i]))
						{
							flags.add(tokens[i]);
							continue;
						} else inFlags = false;
					}
					if (!inClassAndMethod)
					{
						returnType = tokens[i];
						inClassAndMethod = true;
						continue;
					}
					if (!inArgs)
					{
						String classMethodAndFirstArg = tokens[i];
						int paren = classMethodAndFirstArg.indexOf('(');
						String classAndMethod = classMethodAndFirstArg.substring(0, paren);
						String firstArg = classMethodAndFirstArg.substring(paren + 1);
						
						int lastPeriod = classAndMethod.lastIndexOf('.');
						className = classAndMethod.substring(0, lastPeriod);
						methodName = classAndMethod.substring(lastPeriod + 1);
						
						argTypes.add(stripArgType(firstArg));
						inArgs = true;
						continue;
					}
					argTypes.add(stripArgType(tokens[i]));
				}
				
				String flagArray[] = (String[]) flags.toArray(new String[flags.size()]);
				String argTypeArray[] = (String[]) argTypes.toArray(new String[argTypes.size()]);
				return findMethod (className, flagArray, returnType, argTypeArray, methodName);
			}
			else
			{
				int lastPeriod = tokens[0].lastIndexOf('.');
				String className = tokens[0].substring(0, lastPeriod);
				String fieldName = tokens[0].substring(lastPeriod + 1);
				
				return findField (className, fieldName);
			}
		}
		
		return null;
	}
	
	private IMethod findMethod (String className, String flags[], String returnType, String argTypes[], String methodName)
		throws CoreException
	{
		IType type = AopCorePlugin.getCurrentJavaProject().findType(className);
		if (type != null)
		{
			
			IMethod methods[] = type.getMethods();
			for (int i = 0; i < methods.length; i++) {
				if (methods[i].getElementName().equals(methodName))
				{
					if (argTypes.length == 1 && argTypes[0].equals("") && methods[i].getNumberOfParameters() == 0)
					{
						return methods[i];
					}
					
					else if (argTypes.length == methods[i].getNumberOfParameters()) 
					{
						boolean signaturesAreSame = true;
						for (int j = 0; j < argTypes.length; j++)
						{
							String argType = JavaModelUtil.getResolvedTypeName(methods[i].getParameterTypes()[j], type);
							System.out.println("argType="+argType+",paramType="+argTypes[j]);
							
							if (! argType.equals(argTypes[j]))
							{
								signaturesAreSame = false;
								break;
							}
						}
						
						if (signaturesAreSame)
						{
							return methods[i];
						}
					}
				}
			}
		}
		else
		{
			type = AopCorePlugin.getCurrentJavaProject().findType(returnType.replaceAll("[()]", ""));
			if (type != null)
			{
				IMethod methods[] = type.getMethods();
				for (int i = 0; i < methods.length; i++)
				{
					if (methods[i].getElementName().equals(methodName))
					{
						if (argTypes.length == 0 && methods[i].getNumberOfParameters() == 0)
						{
							return methods[i];
						}
						
						else if (argTypes.length > 0) 
						{
							boolean signaturesAreSame = true;
							for (int j = 0; j < argTypes.length; j++)
							{
								String argType = argTypes[j];
								if (! argType.equals(methods[i].getParameterTypes()[j]))
								{
									signaturesAreSame = false;
									break;
								}
							}
							
							if (signaturesAreSame)
							{
								return methods[i];
							}
						}
					}
				}
			}
		}
		return null;
	}
	
	private IField findField (String className, String fieldName)
		throws CoreException
	{
		IType type = AopCorePlugin.getCurrentJavaProject().findType(className);
		if (type != null)
		{
			return type.getField(fieldName);
		}
		return null;
	}

	private String getReportSignature (IJavaElement element)
		throws CoreException
	{
		if (element instanceof IMethod)
		{
			IMethod method = (IMethod) element;
			String signature =
				Flags.toString(method.getFlags()) + " " +
				Signature.toString(Signature.getReturnType(method.getSignature())) + " " + 
				method.getDeclaringType().getFullyQualifiedName() + "." + Signature.toString(method.getSignature(), method.getElementName(), null, true, false);
			
			//System.out.println("[advised-members-content-provider] checking for method " + signature);
			return signature;
		}
		else if (element instanceof IField)
		{
			IField field = (IField) element;
			String signature =
				field.getDeclaringType().getFullyQualifiedName() + "." + field.getElementName();
			
			return signature;
		}
		return null;
	}
	
	public boolean isAdvised (IJavaElement element)
		throws CoreException
	{
		if (element instanceof IMethod || element instanceof IField)
		{
			Collection advisors =  getElementAdvisors(element);
			return (advisors != null && !advisors.isEmpty());
		}
		else if (element instanceof IType)
		{
			IType type = (IType) element;
			IJavaElement elements[] = type.getChildren();
			for (int i = 0; i < elements.length; i++)
			{
				if (isAdvised(elements[i]))
				{
					return true;
				}
			}
		}
		return false;
	}
	
	public ArrayList getAdvisedChildren (IType type)
		throws CoreException
	{
		ArrayList children = new ArrayList();
		IJavaElement elements[] = type.getChildren();
		for (int i = 0; i < elements.length; i++)
		{
			if (isAdvised(elements[i]))
			{
				children.add (elements[i]);
			}
		}
		
		return children;
	}
	
	public IReportModelAdvisor[] getReportModelAdvisors (IJavaElement advised)
	{
		Collection advisors = getElementAdvisors(advised);
		
		if (advisors != null)
		{
			return (IReportModelAdvisor[]) advisors.toArray(new IReportModelAdvisor[advisors.size()]);
		}
		
		return null;
	}
	
	public IReportModelAdvisor[] getReportModelAdvisors (IReportModelAdvised advised)
	{
		if (advised == null)
			return null;
		
		Collection advisors = getElementAdvisors(advised);
		
		if (advisors != null)
		{
			return (IReportModelAdvisor[]) advisors.toArray(new IReportModelAdvisor[advisors.size()]);
		}
		
		return null;
	}
	
	public IReportModelAdvisor findAdvisor (IJavaElement advisor)
		throws CoreException
	{
		Hashtable advisors = getProjectAdvisors(advisor.getJavaProject());
		
		for (Iterator iter = advisors.keySet().iterator(); iter.hasNext(); )
		{
			IReportModelAdvised advised = (IReportModelAdvised) iter.next();
			Collection elementAdvisors = getElementAdvisors(advised);
			
			for (Iterator aIter = elementAdvisors.iterator(); aIter.hasNext(); )
			{
				IReportModelAdvisor modelAdvisor = (IReportModelAdvisor) aIter.next();
				
				if (modelAdvisor.getAdvisingElement().equals(advisor))
				{
					return modelAdvisor;
				}
			}
		}
		
		return null;
	}
	
	protected ArrayList toAdvisors (Collection advisors)
		throws CoreException
	{
		ArrayList modelAdvisors = new ArrayList();
		for (Iterator iter = advisors.iterator(); iter.hasNext();)
		{
			Object advisor = iter.next();
			
			
			if (advisor instanceof ReportAdvice)
			{
				ReportAdvice advice = (ReportAdvice) advisor;
				System.out.println("[aop-report-model] advisor = " + advice.getName());
				modelAdvisors.add(
					new ReportModelAdvice(this, AopCorePlugin.getDefault().findAdviceMethod(advice), advice));
			}
			else if (advisor instanceof ReportInterceptor)
			{
				ReportInterceptor interceptor = (ReportInterceptor) advisor;
				System.out.println("[aop-report-model] advisor = " + interceptor.getClazz());
				IJavaElement srcElement = AopCorePlugin.getCurrentJavaProject().findElement(
					new Path(interceptor.getClazz().replace('.', Path.SEPARATOR) + ".java"));
				
				IType type = srcElement.getElementType() == IJavaElement.COMPILATION_UNIT ? ((ICompilationUnit)srcElement).findPrimaryType() : ((IClassFile)srcElement).getType();
				modelAdvisors.add(new ReportModelInterceptor(this, type, interceptor));
			}
		}
		
		return modelAdvisors;
	}

	public Hashtable getAllProjectAdvisors()
	{
		return projectAdvisors;
	}


}
