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
package org.jboss.ide.eclipse.xdoclet.assist.ui;

import org.apache.oro.text.regex.MalformedPatternException;
import org.apache.oro.text.regex.Perl5Compiler;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.widgets.Composite;

/**
 * @author    Hans Dockter
 * @version   $Revision$
 * @created   17 mai 2003
 */
public class PatternFieldEditor extends StringFieldEditor
{

   Perl5Compiler compiler = new Perl5Compiler();

   /** Constructor for PatternFieldEditor. */
   public PatternFieldEditor()
   {
      super();
   }

   /**
    * Constructor for PatternFieldEditor.
    *
    * @param name
    * @param labelText
    * @param width
    * @param strategy
    * @param parent
    */
   public PatternFieldEditor(String name, String labelText, int width, int strategy, Composite parent)
   {
      super(name, labelText, width, strategy, parent);
   }

   /**
    * Constructor for PatternFieldEditor.
    *
    * @param name
    * @param labelText
    * @param width
    * @param parent
    */
   public PatternFieldEditor(String name, String labelText, int width, Composite parent)
   {
      super(name, labelText, width, parent);
   }

   /**
    * Constructor for PatternFieldEditor.
    *
    * @param name
    * @param labelText
    * @param parent
    */
   public PatternFieldEditor(String name, String labelText, Composite parent)
   {
      super(name, labelText, parent);
   }

   /**
    * @return   Description of the Return Value
    * @see      org.eclipse.jface.preference.StringFieldEditor#isValid()
    */
   protected boolean doCheckState()
   {
      clearErrorMessage();
      try
      {
         compiler.compile(getStringValue());
      }
      catch (MalformedPatternException e)
      {
         return false;
      }
      return true;
   }
}
