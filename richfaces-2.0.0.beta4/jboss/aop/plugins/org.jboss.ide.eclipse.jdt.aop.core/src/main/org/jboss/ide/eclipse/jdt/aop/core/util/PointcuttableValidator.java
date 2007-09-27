/*
 * JBoss, the OpenSource J2EE webOS
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.aop.core.util;

import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IPackageDeclaration;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeHierarchy;
import org.eclipse.jdt.core.JavaModelException;


/**
 * This is a util class to determine whether or not a target is "pointcuttable".
 *
 * @author andrew oswald
 */
public class PointcuttableValidator {
    public static boolean validatePointcuttable(Object object) {
        if(object instanceof IPackageDeclaration) {
            return PointcuttableValidator.validatePackageDeclaration((IPackageDeclaration)object);
        }

        if(object instanceof IPackageFragment) {
            return PointcuttableValidator.validatePackageFragment((IPackageFragment)object);
        }

        if(object instanceof ICompilationUnit) {
            return PointcuttableValidator.validateCompilationUnit((ICompilationUnit)object);
        }

        if(object instanceof IType) {
            return PointcuttableValidator.validateType((IType)object);
        }

        if(object instanceof IField) {
            return PointcuttableValidator.validateField((IField)object);
        }

        if(object instanceof IMethod) {
            return PointcuttableValidator.validateMethod((IMethod)object);
        }

        return false;
    }

    public static boolean validateType(IType type) {
        if(!validatePackageFragment(type.getPackageFragment())) {
            return false;
        }

        try {
            ITypeHierarchy typeHierarchy = type.newSupertypeHierarchy(null);

            IType[] superTypes = typeHierarchy.getAllSupertypes(type);

            //we're guaranteed bottom-up order
            for(int i = 0; i<superTypes.length; i++) {
                IType superType = superTypes[i];
                IPackageFragment packageFragment = superType.getPackageFragment();

                if(packageFragment!=null) {
                    String packageName = packageFragment.getElementName();

                    //since system inheritance is perfectly fine, and the target
                    //type's package fragment was already validated, we only care
                    //about validating super-type non-system package fragments
                    if(validateNonSystemPackageFragment(packageName)) {
                        if(!validateNonJBossAOPPackageFragment(packageName)) {
                            return false;
                        }
                    }
                }
            }
        } catch(JavaModelException e) {
            return false;
        }

        return true;
    }

    public static boolean validateMethod(IMethod method) {
        IType type = method.getDeclaringType();

        if(!validateType(type)) {
            return false;
        }

        try {
            int methodFlags = method.getFlags();

            if(Flags.isAbstract(methodFlags) || Flags.isNative(methodFlags)) {
                return false;
            }
        } catch(JavaModelException e) {
            return false;
        }

        if(method.getElementName().startsWith("_")) {
            return false;
        }

        return true;
    }

    public static boolean validateField(IField field) {
        IType type = field.getDeclaringType();

        if(!validateType(type)) {
            return false;
        }

        String fieldName = field.getElementName();

        if(fieldName.startsWith("_")) {
            return false;
        }

        if(fieldName.indexOf("$")!=-1) {
            return false;
        }

        try {
            if(Flags.isFinal(field.getFlags())) {
                return false;
            }
        } catch(JavaModelException e) {
            return false;
        }

        return true;
    }

    //TODO: not sure if this is applicable -- perhaps for wildcard stuff
    public static boolean validateCompilationUnit(
        ICompilationUnit compilationUnit) {
        return false;
    }

    public static boolean validatePackageDeclaration(
        IPackageDeclaration packageDeclaration) {
        return validatePackageName(packageDeclaration.getElementName());
    }

    public static boolean validatePackageFragment(
        IPackageFragment packageFragment) {
        return validatePackageName(packageFragment.getElementName());
    }

    private static boolean validatePackageName(String packageName) {
        return (validateNonJBossAOPPackageFragment(packageName)
        && validateNonSystemPackageFragment(packageName));
    }

    private static boolean validateNonJBossAOPPackageFragment(
        String packageName) {
        if(packageName.startsWith("org.jboss.aop.")
           || (packageName.equals("org.jboss.aop"))
           
           || packageName.startsWith("org.jboss.util.") 
           || packageName.equals("org.jboss.util")
			
		   || packageName.startsWith("javassist.")
		   || (packageName.equals("javassist"))
		   
		   || packageName.startsWith("gnu.trove.")
		   || packageName.equals("gnu.trove")
		   
		   || packageName.startsWith("EDU.oswego.cs.dl.util.concurrent.")
		   || packageName.equals("EDU.oswego.cs.dl.util.concurrent")) {
           
            return false;
        }

        return true;
    }

    private static boolean validateNonSystemPackageFragment(String packageName) {
        if(
           packageName.startsWith("org.apache.crimson.")
			|| packageName.equals("org.apache.crimson")
			
			|| packageName.startsWith("org.apache.xalan.")
			|| packageName.equals("org.apache.xalan")
			
			|| packageName.startsWith("org.apache.xalan.")
			|| packageName.equals("org.apache.xalan")
			
			|| packageName.startsWith("org.apache.xml.")
			|| packageName.equals("org.apache.xml")
			
			|| packageName.startsWith("org.apache.xpath.")
			|| packageName.equals("org.apache.xpath")
			
			|| packageName.startsWith("org.ietf.")
			|| packageName.equals("org.ietf")
			
			|| packageName.startsWith("org.omg.")
			|| packageName.equals("org.omg")
			
			|| packageName.startsWith("org.w3c.")
			|| packageName.equals("org.w3c")
			
			|| packageName.startsWith("org.xml.sax.")
			|| packageName.equals("org.xml.sax")
			
			|| packageName.startsWith("sunw.")
			|| packageName.equals("sunw")
			
			|| packageName.startsWith("java.")
			|| packageName.startsWith("javax.")
			
			|| packageName.startsWith("com.sun.")
			|| packageName.equals("com.sun")) {
            
            return false;
        }

        return true;
    }
}
