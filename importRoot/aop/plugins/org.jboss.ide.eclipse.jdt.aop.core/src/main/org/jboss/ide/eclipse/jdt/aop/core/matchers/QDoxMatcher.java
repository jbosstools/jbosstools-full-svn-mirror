/*
 * Created on Feb 11, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.jboss.ide.eclipse.jdt.aop.core.matchers;

import java.io.StringReader;
import java.util.StringTokenizer;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.Signature;

import com.thoughtworks.qdox.JavaDocBuilder;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaField;
import com.thoughtworks.qdox.model.JavaMethod;
import com.thoughtworks.qdox.model.JavaParameter;

/**
 * A Static class used to get an equivilent qDox representation
 * of a method, constructor, or field, given some jdt representation
 * of the same.
 * 
 * @author Stryker
 *
 */
public class QDoxMatcher {
	public static final boolean CONSTRUCTOR = true;
	public static final boolean METHOD      = false;
	
	
	
	public static JavaClass matchClass( IType jdtType ) {
        JavaDocBuilder builder = new JavaDocBuilder();
		try {
			builder.addSource(new StringReader(jdtType.getCompilationUnit().getSource()));
			String type = jdtType.getElementName();
			JavaClass[] classes = builder.getClasses();
        	for( int i = 0; i < classes.length; i++ ) {
        		//System.out.println("jdt: " + type + ", qdox: " + classes[i].getName());
        		if( !type.equals(classes[i].getName())) continue;
        		return classes[i];
        	}
		} catch( Exception jme ) {
			
		}
		return null;
	}
	
	/**
	 * Takes the contents of this constructor's compilation unit,
	 * feeds it to qDox, isolates the constructor, and returns it 
	 * for further processing (specifically annotation matching).
	 * @return a qDox JavaMethod 
	 */
	public static JavaField matchField(IField jdtField) {
		
        JavaDocBuilder builder = new JavaDocBuilder();
        try {
        	// Feeding source code to qdox, then locating the method from qdox 
        	builder.addSource(new StringReader(jdtField.getCompilationUnit().getSource()));
        	String type = jdtField.getDeclaringType().getElementName();
        	JavaClass[] classes = builder.getClasses();
        	for( int i = 0; i < classes.length; i++ ) {
        		//System.out.println("jdt: " + type + ", qdox: " + classes[i].getName());
        		if( !type.equals(classes[i].getName())) continue;
        		JavaField qDoxField = classes[i].getFieldByName(jdtField.getElementName());
        		return qDoxField;
        	}
        	// this is going to make it sooo slow
        } catch ( Exception e ) {
        	System.out.println("ERRORED");
        	//System.out.println(e.getMessage());
        	//e.printStackTrace();
        	return null;
        }
        return null;
	}

	
	/**
	 * Takes the contents of this IMethod's compilation unit,
	 * feeds it to qDox, isolates the method, and returns it. 
	 *
	 * @param IMethod jdtMethod   - a method we wish to match 
	 * @return a qDox JavaMethod  - the same method from qDox
	 */

	public static JavaMethod matchMethod(IMethod jdtMethod, boolean constructor) {
        JavaDocBuilder builder = new JavaDocBuilder();
        try {
        	// Feeding source code to qdox, then locating the method from qdox 
        	builder.addSource(new StringReader(jdtMethod.getCompilationUnit().getSource()));
        	String type = jdtMethod.getDeclaringType().getElementName();
        	JavaClass[] classes = builder.getClasses();
        	for( int i = 0; i < classes.length; i++ ) {
        		if( !type.equals(classes[i].getName())) continue;
    			JavaMethod[] methods = classes[i].getMethods();
    			for( int j = 0; j < methods.length; j++ ) {
    				String methodName = jdtMethod.getElementName();
    				String qDoxMethodName = methods[j].getName();
    				if( !methodName.equals(qDoxMethodName)) continue;

    				/* 
    				 * Well the names match. We should check other things too. 
					 */
    				JavaMethod theMethod = methods[j];
					String[] jdtParamNames = Signature.getParameterTypes(jdtMethod.getSignature());
					JavaParameter[] qDoxParams = theMethod.getParameters();


					if( !(jdtParamNames.length == qDoxParams.length)) continue;
					if( constructor &&  theMethod.getReturns() != null ) continue;
					
					if( jdtParamNames.length == 0 ) return theMethod;
					
					
					boolean paramsMatch = doParamsMatch( theMethod.getDeclarationSignature(false),
							Signature.toString(jdtMethod.getSignature(), methodName, 
									jdtMethod.getParameterNames(), false, !constructor));
					
					if( paramsMatch ) {
						// If the params match up too... 
	   					JavaMethod qDoxMethod = methods[j];
	   					return qDoxMethod;
					}
    			}
        	}
        } catch ( Exception e ) {
        	System.out.println("ERRORED");
        	System.out.println(e.getMessage());
        	//e.printStackTrace();
        }
        return null;
	}
	
	private static boolean doParamsMatch( String qDox, String jdt ) {
		//System.out.println("qdox: " + qDox + "\njdt: " + jdt);
		
		// chop off exceptions
		if( qDox.indexOf(") throws") != -1 ) {
			String tmp = qDox.substring(0, qDox.indexOf(") throws"));
			qDox = tmp.trim();
		}
		
		
		StringTokenizer qTokenizer = new StringTokenizer(qDox, " ");
		StringTokenizer jTokenizer = new StringTokenizer(jdt, " ");
		if( qTokenizer.countTokens() != jTokenizer.countTokens()) return false;
		//System.out.println("testing");
		while(qTokenizer.hasMoreTokens()) {
			if(!(signatureTrim(qTokenizer.nextToken())
					.equals(signatureTrim(jTokenizer.nextToken())))) {
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * Takes one token from a Method Signature.
	 * Removes everything before the last '(', then the last ".", 
	 * and finally the last '$'.
	 * @param s  The signature to trim
	 * @return   A trimmed signature
	 */
	private static String signatureTrim(String s) {
		String tmp1, retval = s;
		if( retval.lastIndexOf("(") != -1 ) {
			tmp1 = retval.substring(retval.lastIndexOf("(")+1);
			retval = tmp1;			
		}
		if( retval.lastIndexOf(".") != -1 ) {
			tmp1 = retval.substring(retval.lastIndexOf(".")+1);
			retval = tmp1;
		}
		if( retval.lastIndexOf("$") != -1 ) {
			tmp1 = retval.substring(retval.lastIndexOf("$")+1);
			retval = tmp1;
		}
		//System.out.println(retval);
		return retval;
	}

}
