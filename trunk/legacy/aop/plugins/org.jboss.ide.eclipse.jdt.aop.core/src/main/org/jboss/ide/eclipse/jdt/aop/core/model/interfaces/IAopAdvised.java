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
package org.jboss.ide.eclipse.jdt.aop.core.model.interfaces;

import org.eclipse.jdt.core.IJavaElement;

/**
 * @author Marshall
 */
public interface IAopAdvised
{

   /**
    * This advised element will be advised on field get
    */
   public static final int TYPE_FIELD_GET = 0;

   /**
    * This advised element will be advised on field set
    */
   public static final int TYPE_FIELD_SET = 1;

   /**
    * This advised element will be advised when a method is executed
    */
   public static final int TYPE_METHOD_EXECUTION = 2;

   /**
    * This advised element represents a class that matches some type pattern.
    */
   public static final int TYPE_CLASS = 3;

   /**
    * Return the IJavaElement that corresponds with this object
    * @return IJavaElement
    */
   public IJavaElement getAdvisedElement();

   /**
    * Return the type of execution this advised element represents
    * @return int
    */
   public int getType();
}
