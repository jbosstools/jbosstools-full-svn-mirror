/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.j2ee.jsp.ui.assist;

import org.eclipse.jdt.core.CompletionProposal;
import org.eclipse.jdt.core.CompletionRequestor;
import org.eclipse.jdt.core.Signature;
import org.eclipse.jdt.internal.ui.text.java.ResultCollector;

/**
 * Description of the Class
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class JSPResultCollector extends ResultCollector
{
   private final static String JASPER_PACKAGE = "org.apache.jasper";//$NON-NLS-1$

   public class Requestor extends CompletionRequestor
   {
	   	/* (non-Javadoc)
		 * @see org.eclipse.jdt.core.CompletionRequestor#accept(org.eclipse.jdt.core.CompletionProposal)
		 */
		public void accept(CompletionProposal proposal) {
			// TODO Auto-generated method stub
		}
   }
   

   /**Constructor for the JSPResultCollector object */
   public JSPResultCollector() { }


   /**
    * Description of the Method
    *
    * @param superTypePackageName   Description of the Parameter
    * @param superTypeName          Description of the Parameter
    * @param parameterPackageNames  Description of the Parameter
    * @param parameterTypeNames     Description of the Parameter
    * @param parameterNames         Description of the Parameter
    * @param completionName         Description of the Parameter
    * @param modifiers              Description of the Parameter
    * @param completionStart        Description of the Parameter
    * @param completionEnd          Description of the Parameter
    * @param relevance              Description of the Parameter
    */
   public void internalAcceptAnonymousType(char[] superTypePackageName, char[] superTypeName, char[][] parameterPackageNames, char[][] parameterTypeNames,
      char[][] parameterNames, char[] completionName, int modifiers, int completionStart, int completionEnd, int relevance)
   {
      if (this.filterPackage(superTypePackageName))
      {
         return;
      }

      super.internalAcceptAnonymousType(superTypePackageName, superTypeName, parameterPackageNames, parameterTypeNames, parameterNames, completionName, modifiers,
         completionStart, completionEnd, relevance);
   }


   /**
    * Description of the Method
    *
    * @param packageName     Description of the Parameter
    * @param typeName        Description of the Parameter
    * @param completionName  Description of the Parameter
    * @param modifiers       Description of the Parameter
    * @param start           Description of the Parameter
    * @param end             Description of the Parameter
    * @param relevance       Description of the Parameter
    */

   public void internalAcceptType(CompletionProposal proposal) {
	   
	   char[] packageName = Signature.getSignatureQualifier(proposal.getSignature());
	   
	   if (this.filterPackage(packageName))
      {
         return;
      }

      super.internalAcceptType(proposal);
   }


   /**
    * Description of the Method
    *
    * @param declaringTypePackageName  Description of the Parameter
    * @param declaringTypeName         Description of the Parameter
    * @param name                      Description of the Parameter
    * @param typePackageName           Description of the Parameter
    * @param typeName                  Description of the Parameter
    * @param completionName            Description of the Parameter
    * @param modifiers                 Description of the Parameter
    * @param start                     Description of the Parameter
    * @param end                       Description of the Parameter
    * @param relevance                 Description of the Parameter
    */
   public void internalAcceptField(char[] declaringTypePackageName, char[] declaringTypeName, char[] name, char[] typePackageName, char[] typeName,
      char[] completionName, int modifiers, int start, int end, int relevance)
   {
      if (this.filterPackage(typePackageName))
      {
         return;
      }
      if (this.filterName(name))
      {
         return;
      }

      super.internalAcceptField(declaringTypePackageName, declaringTypeName, name, typePackageName, typeName, completionName, modifiers, start, end, relevance);
   }


   /**
    * Description of the Method
    *
    * @param name             Description of the Parameter
    * @param typePackageName  Description of the Parameter
    * @param typeName         Description of the Parameter
    * @param modifiers        Description of the Parameter
    * @param start            Description of the Parameter
    * @param end              Description of the Parameter
    * @param relevance        Description of the Parameter
    */
   public void internalAcceptLocalVariable(char[] name, char[] typePackageName, char[] typeName, int modifiers, int start, int end, int relevance)
   {
      if (this.filterPackage(typePackageName))
      {
         return;
      }
      if (this.filterName(name))
      {
         return;
      }

      super.internalAcceptLocalVariable(name, typePackageName, typeName, modifiers, start, end, relevance);
   }


   /**
    * Description of the Method
    *
    * @param declaringTypePackageName  Description of the Parameter
    * @param declaringTypeName         Description of the Parameter
    * @param name                      Description of the Parameter
    * @param parameterPackageNames     Description of the Parameter
    * @param parameterTypeNames        Description of the Parameter
    * @param parameterNames            Description of the Parameter
    * @param returnTypePackageName     Description of the Parameter
    * @param returnTypeName            Description of the Parameter
    * @param completionName            Description of the Parameter
    * @param modifiers                 Description of the Parameter
    * @param start                     Description of the Parameter
    * @param end                       Description of the Parameter
    * @param relevance                 Description of the Parameter
    */
   /*
   void internalAcceptMethod(CompletionProposal proposal)
   {
      if (this.filterPackage(declaringTypePackageName))
      {
         return;
      }

<<<<<<< JSPResultCollector.java
      super.internalAcceptMethodDeclaration(declaringTypePackageName, declaringTypeName, name, parameterPackageNames, parameterTypeNames, parameterNames, returnTypePackageName,
         returnTypeName, completionName, modifiers, start, end, relevance);
   }
=======
      super.internalAcceptMethod(proposal);
   }/*
>>>>>>> 1.1.4.1


   /**
    * Description of the Method
    *
    * @param declaringTypePackageName  Description of the Parameter
    * @param declaringTypeName         Description of the Parameter
    * @param name                      Description of the Parameter
    * @param parameterPackageNames     Description of the Parameter
    * @param parameterTypeNames        Description of the Parameter
    * @param parameterNames            Description of the Parameter
    * @param returnTypePackageName     Description of the Parameter
    * @param returnTypeName            Description of the Parameter
    * @param completionName            Description of the Parameter
    * @param modifiers                 Description of the Parameter
    * @param start                     Description of the Parameter
    * @param end                       Description of the Parameter
    * @param relevance                 Description of the Parameter
    */
   public void internalAcceptMethodDeclaration(char[] declaringTypePackageName, char[] declaringTypeName, char[] name, char[][] parameterPackageNames,
      char[][] parameterTypeNames, char[][] parameterNames, char[] returnTypePackageName, char[] returnTypeName, char[] completionName, int modifiers,
      int start, int end, int relevance)
   {
      if (this.filterPackage(declaringTypePackageName))
      {
         return;
      }

      super.internalAcceptMethodDeclaration(declaringTypePackageName, declaringTypeName, name, parameterPackageNames, parameterTypeNames, parameterNames,
         returnTypePackageName, returnTypeName, completionName, modifiers, start, end, relevance);
   }


   /**
    * Description of the Method
    *
    * @param packageName     Description of the Parameter
    * @param completionName  Description of the Parameter
    * @param start           Description of the Parameter
    * @param end             Description of the Parameter
    * @param relevance       Description of the Parameter
    */
   public void internalAcceptPackage(char[] packageName, char[] completionName, int start, int end, int relevance)
   {
      if (this.filterPackage(packageName))
      {
         return;
      }

      super.internalAcceptPackage(packageName, completionName, start, end, relevance);
   }


   /**
    * Description of the Method
    *
    * @param declaringTypePackageName  Description of the Parameter
    * @param declaringTypeName         Description of the Parameter
    * @param selector                  Description of the Parameter
    * @param completionStart           Description of the Parameter
    * @param completionEnd             Description of the Parameter
    * @param relevance                 Description of the Parameter
    */
   public void internalAcceptPotentialMethodDeclaration(char[] declaringTypePackageName, char[] declaringTypeName, char[] selector, int completionStart,
      int completionEnd, int relevance)
   {
      if (this.filterPackage(declaringTypePackageName))
      {
         return;
      }

      super.internalAcceptPotentialMethodDeclaration(declaringTypePackageName, declaringTypeName, selector, completionStart, completionEnd, relevance);
   }

   /**
    * Description of the Method
    *
    * @param typePackageName  Description of the Parameter
    * @param typeName         Description of the Parameter
    * @param name             Description of the Parameter
    * @param completionName   Description of the Parameter
    * @param start            Description of the Parameter
    * @param end              Description of the Parameter
    * @param relevance        Description of the Parameter
    */
   public void internalAcceptVariableName(char[] typePackageName, char[] typeName, char[] name, char[] completionName, int start, int end, int relevance)
   {
      if (this.filterPackage(typePackageName))
      {
         return;
      }

      super.internalAcceptVariableName(typePackageName, typeName, name, completionName, start, end, relevance);
   }


   /**
    * Description of the Method
    *
    * @param name  Description of the Parameter
    * @return      Description of the Return Value
    */
   protected boolean filterName(char[] name)
   {
      String n = new String(name);
      return n.startsWith("_jspx");//$NON-NLS-1$
   }


   /**
    * Description of the Method
    *
    * @param name  Description of the Parameter
    * @return      Description of the Return Value
    */
   protected boolean filterPackage(char[] name)
   {
      String pkg = new String(name);
      return (pkg.startsWith(JASPER_PACKAGE));
   }

}
