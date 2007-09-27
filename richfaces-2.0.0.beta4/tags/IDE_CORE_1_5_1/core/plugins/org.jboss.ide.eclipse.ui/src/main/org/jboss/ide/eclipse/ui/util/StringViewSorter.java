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
package org.jboss.ide.eclipse.ui.util;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;

/**
 * Simple sorter for viewer which compares the String representation
 * for both objects.
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class StringViewSorter extends ViewerSorter
{
   /** Default constructor */
   public StringViewSorter()
   {
      super();
   }

   /**
    * Comparison of the String representation for both objects.
    *
    * @param viewer  The viewer that request the comparison
    * @param e1      First object
    * @param e2      Second object
    * @return        Return -1, 0 or 1 depending on the comparison result
    * @see           org.eclipse.jface.viewers.ViewerSorter#compare(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
    */
   public int compare(Viewer viewer, Object e1, Object e2)
   {
      String s1 = e1.toString();
      String s2 = e2.toString();
      return s1.compareTo(s2);
   }
}
