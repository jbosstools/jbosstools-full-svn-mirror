/*
 * JBoss, a division of Red Hat
 * Copyright 2006, Red Hat Middleware, LLC, and individual contributors as indicated
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

import javassist.NotFoundException;

import org.eclipse.jdt.core.IMethod;
import org.jboss.aop.pointcut.Pointcut;
import org.jboss.aop.pointcut.ast.ASTConstructor;
import org.jboss.aop.pointcut.ast.ASTExecution;
import org.jboss.aop.pointcut.ast.ASTField;
import org.jboss.aop.pointcut.ast.ASTHas;
import org.jboss.aop.pointcut.ast.ASTHasField;
import org.jboss.aop.pointcut.ast.ASTMethod;
import org.jboss.aop.pointcut.ast.ASTStart;
import org.jboss.aop.pointcut.ast.Node;
import org.jboss.aop.pointcut.ast.ParseException;
import org.jboss.ide.eclipse.jdt.aop.core.pointcut.JDTPointcutExpression;

/**
 * @author Marshall
 */
public class JDTExecutionConstructorMatcher extends JDTConstructorMatcher
{

   public JDTExecutionConstructorMatcher(IMethod method, ASTStart start) throws NotFoundException
   {
      super(method, start);
      // TODO Auto-generated constructor stub
   }

   public Object visit(ASTExecution node, Object data)
   {
      return node.jjtGetChild(0).jjtAccept(this, data);
   }

   protected Boolean resolvePointcut(Pointcut p)
   {

      try
      {
         JDTPointcutExpression expr = new JDTPointcutExpression(p);

         return new Boolean(expr.matchesExecution(constructor));
      }
      catch (ParseException e)
      {
         throw new RuntimeException(e);
      }
   }

   public Object visit(ASTHas node, Object data)
   {

      Node n = node.jjtGetChild(0);
      if (n instanceof ASTMethod)
      {
         return new Boolean(JDTPointcutUtil.has(constructor.getDeclaringType(), (ASTMethod) n));
      }
      else
      {
         return new Boolean(JDTPointcutUtil.has(constructor.getDeclaringType(), (ASTConstructor) n));
      }
   }

   public Object visit(ASTHasField node, Object data)
   {
      ASTField f = (ASTField) node.jjtGetChild(0);

      return new Boolean(JDTPointcutUtil.has(constructor.getDeclaringType(), f));
   }

}
