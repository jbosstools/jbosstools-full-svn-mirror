/*
 * JBoss, Home of Professional Open Source
 * Copyright 2005, JBoss Inc., and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.ide.eclipse.jdt.aop.core.matchers;

import java.util.Iterator;
import java.util.List;

import javassist.NotFoundException;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MarkerAnnotation;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.internal.corext.util.JavaModelUtil;
import org.jboss.aop.AspectManager;
import org.jboss.aop.pointcut.MatcherHelper;
import org.jboss.aop.pointcut.Pointcut;
import org.jboss.aop.pointcut.ast.ASTAll;
import org.jboss.aop.pointcut.ast.ASTAttribute;
import org.jboss.aop.pointcut.ast.ASTConstructor;
import org.jboss.aop.pointcut.ast.ASTField;
import org.jboss.aop.pointcut.ast.ASTFieldExecution;
import org.jboss.aop.pointcut.ast.ASTHas;
import org.jboss.aop.pointcut.ast.ASTHasField;
import org.jboss.aop.pointcut.ast.ASTMethod;
import org.jboss.aop.pointcut.ast.ASTStart;
import org.jboss.aop.pointcut.ast.ClassExpression;
import org.jboss.aop.pointcut.ast.Node;
import org.jboss.ide.eclipse.jdt.aop.core.AopCorePlugin;
import org.jboss.ide.eclipse.jdt.aop.core.matchers.JDTMethodMatcher.TempBool;

import com.thoughtworks.qdox.model.DocletTag;
import com.thoughtworks.qdox.model.JavaField;

/**
 * @author Marshall
 */
public class JDTFieldMatcher extends MatcherHelper
{

   protected IField jdtField;

   protected ASTStart start;

   public JDTFieldMatcher(IField field, ASTStart start) throws NotFoundException
   {
      super(start, AspectManager.instance());
      this.jdtField = field;
      this.start = start;
   }

   protected Boolean resolvePointcut(Pointcut p)
   {
      throw new RuntimeException("SHOULD NOT BE CALLED");
   }

   public Object visit(ASTField node, Object data)
   {

      try
      {
         if (node.getAttributes().size() > 0)
         {
            for (int i = 0; i < node.getAttributes().size(); i++)
            {
               ASTAttribute attr = (ASTAttribute) node.getAttributes().get(i);
               if (!JDTPointcutUtil.matchModifiers(attr, jdtField.getFlags()))
                  ;
            }
         }

         ClassExpression type = node.getType();

         String typeSig = JavaModelUtil.getResolvedTypeName(jdtField.getTypeSignature(), jdtField.getDeclaringType());
         IType fieldType = null;
         try
         {
            fieldType = JavaModelUtil.findType(jdtField.getJavaProject(), typeSig);
         }
         catch (JavaModelException jme)
         {
         }
         if (fieldType == null)
         {
            if (!JDTPointcutUtil.matchesClassExprPrimitive(type, typeSig))
               return Boolean.FALSE;
         }
         else
         {
            if (!JDTPointcutUtil.matchesClassExpr(type, fieldType))
               return Boolean.FALSE;
         }
         if (!JDTPointcutUtil.matchesClassExpr(node.getClazz(), jdtField.getDeclaringType()))
            return Boolean.FALSE;

         if (node.getFieldIdentifier().isAnnotation())
         {
            if (AopCorePlugin.getDefault().hasJava50CompilerCompliance(jdtField.getJavaProject()))
            {
               ASTParser c = ASTParser.newParser(AST.JLS3);
               c.setSource(jdtField.getCompilationUnit().getSource().toCharArray());
               c.setResolveBindings(true);
               CompilationUnit beanAstUnit = (CompilationUnit) c.createAST(null);
               AST ast = beanAstUnit.getAST();

               final String targetAnnot = node.getFieldIdentifier().getOriginal().substring(1);
               final String targetMethodName = jdtField.getElementName();

               final TempBool tempBool = new TempBool(false);

               beanAstUnit.accept(new ASTVisitor()
               {
                  public boolean visit(MarkerAnnotation node)
                  {
                     Name name = node.getTypeName();
                     String annotationName = name.getFullyQualifiedName();
                     if (annotationName.equals(targetAnnot))
                     {
                        tempBool.setValue(true);
                     }
                     return true;
                  }

                  public boolean visit(MethodDeclaration node)
                  {
                     return false;
                  }

                  public boolean visit(FieldDeclaration node)
                  {
                     List list = node.fragments();
                     for (Iterator i = list.iterator(); i.hasNext();)
                     {
                        VariableDeclarationFragment frag = (VariableDeclarationFragment) i.next();
                        String name = frag.getName().getFullyQualifiedName();
                        if (name.equals(targetMethodName))
                        {
                           return true;
                        }
                     }
                     return false;
                  }
               });
               return tempBool.getBool();

            }
            else
            {

               //TODO implement annotation code here
               JavaField qDoxField = QDoxMatcher.matchField(jdtField);
               // qDoxField will be null if there's an exception, such as a compile exception.
               //System.out.println("QDOXFIELD: " + qDoxField);
               if (qDoxField == null)
                  return Boolean.FALSE;
               DocletTag[] tags = qDoxField.getTags();
               for (int k = 0; k < tags.length; k++)
               {
                  if (node.getFieldIdentifier().getOriginal().equals(tags[k].getName()))
                     return Boolean.TRUE;
               }
               return Boolean.FALSE;
            }
         }
         else
         {
            if (node.getFieldIdentifier().matches(jdtField.getElementName()))
            {
               return Boolean.TRUE;
            }
         }

         return Boolean.FALSE;
      }
      catch (JavaModelException e)
      {
         return Boolean.FALSE;
      }
   }

   public Object visit(ASTFieldExecution node, Object data)
   {
      return node.jjtGetChild(0).jjtAccept(this, null);
   }

   public Object visit(ASTAll node, Object data)
   {
      if (node.getClazz().isAnnotation())
      {
         JavaField qDoxField = QDoxMatcher.matchField(jdtField);
         // qDoxField will be null if there's an exception, such as a compile exception.
         if (qDoxField == null)
            return Boolean.FALSE;
         DocletTag[] tags = qDoxField.getTags();
         for (int k = 0; k < tags.length; k++)
         {
            if (node.getClasseExpression().equals(tags[k].getName()))
               return Boolean.TRUE;
         }
         return Boolean.FALSE;
      }
      else if (node.getClazz().isInstanceOf())
      {
         if (!JDTPointcutUtil.subtypeOf(jdtField.getDeclaringType(), node.getClazz()))
            return Boolean.FALSE;
      }
      else if (node.getClazz().isTypedef())
      {
         if (!JDTPointcutUtil.matchesTypedef(jdtField.getDeclaringType(), node.getClazz()))
            return Boolean.FALSE;
      }
      else if (!node.getClazz().matches(jdtField.getDeclaringType().getFullyQualifiedName()))
      {
         return Boolean.FALSE;
      }

      return Boolean.TRUE;
   }

   public Object visit(ASTHasField node, Object data)
   {
      ASTField field = (ASTField) node.jjtGetChild(0);

      return new Boolean(JDTPointcutUtil.has(jdtField.getDeclaringType(), field));
   }

   public Object visit(ASTHas node, Object data)
   {
      Node n = node.jjtGetChild(0);
      if (n instanceof ASTMethod)
      {
         return new Boolean(JDTPointcutUtil.has(jdtField.getDeclaringType(), (ASTMethod) n));
      }
      else
      {
         return new Boolean(JDTPointcutUtil.has(jdtField.getDeclaringType(), (ASTConstructor) n));
      }
   }

}
