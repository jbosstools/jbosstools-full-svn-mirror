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

import org.eclipse.jdt.core.IType;
import org.jboss.aop.pointcut.TypeMatcher;
import org.jboss.aop.pointcut.ast.ASTAllParameter;
import org.jboss.aop.pointcut.ast.ASTAnd;
import org.jboss.aop.pointcut.ast.ASTAttribute;
import org.jboss.aop.pointcut.ast.ASTBoolean;
import org.jboss.aop.pointcut.ast.ASTClass;
import org.jboss.aop.pointcut.ast.ASTComposite;
import org.jboss.aop.pointcut.ast.ASTConstructor;
import org.jboss.aop.pointcut.ast.ASTException;
import org.jboss.aop.pointcut.ast.ASTField;
import org.jboss.aop.pointcut.ast.ASTHas;
import org.jboss.aop.pointcut.ast.ASTHasField;
import org.jboss.aop.pointcut.ast.ASTMethod;
import org.jboss.aop.pointcut.ast.ASTNot;
import org.jboss.aop.pointcut.ast.ASTOr;
import org.jboss.aop.pointcut.ast.ASTParameter;
import org.jboss.aop.pointcut.ast.ASTStart;
import org.jboss.aop.pointcut.ast.ASTSub;
import org.jboss.aop.pointcut.ast.Node;
import org.jboss.aop.pointcut.ast.SimpleNode;
import org.jboss.aop.pointcut.ast.TypeExpressionParserVisitor;

/**
 * @author Marshall
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class JDTTypeMatcher extends TypeMatcher implements TypeExpressionParserVisitor
{

   //private TypeMatcher matcherDelegate;
   private IType type;

   public JDTTypeMatcher(IType type)
   {
      //matcherDelegate = new TypeMatcher(null, (Class) null);
      super(null, (Class) null);
      this.type = type;
   }

   public Object visit(ASTClass node, Object data)
   {
      return new Boolean(JDTPointcutUtil.matchesClassExpr(node.getClazz(), type));
   }

   public Object visit(ASTHas node, Object data)
   {
      Node n = node.jjtGetChild(0);
      if (n instanceof ASTMethod)
      {
         return new Boolean(JDTPointcutUtil.has(type, (ASTMethod) n));
      }
      else
      {
         return new Boolean(JDTPointcutUtil.has(type, (ASTConstructor) n));
      }
   }

   public Object visit(ASTHasField node, Object data)
   {
      ASTField f = (ASTField) node.jjtGetChild(0);
      return new Boolean(JDTPointcutUtil.has(type, f));
   }

}
