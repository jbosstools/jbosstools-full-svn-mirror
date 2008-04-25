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
package org.jboss.ide.eclipse.jdt.aop.core.pointcut;

import org.eclipse.jdt.core.IType;
import org.jboss.aop.pointcut.Typedef;
import org.jboss.aop.pointcut.TypedefExpression;
import org.jboss.aop.pointcut.ast.ParseException;
import org.jboss.ide.eclipse.jdt.aop.core.matchers.JDTTypeMatcher;

/**
 * @author Marshall
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class JDTTypedefExpression extends TypedefExpression
{

   public JDTTypedefExpression(Typedef typedef) throws ParseException
   {
      super(typedef.getName(), typedef.getExpr());

   }

   public boolean matches(IType type)
   {
      JDTTypeMatcher matcher = new JDTTypeMatcher(type);
      return ((Boolean) ast.jjtAccept(matcher, null)).booleanValue();
   }
}
