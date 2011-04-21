/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
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
   public PatternFieldEditor(
         String name,
         String labelText,
         int width,
         int strategy,
         Composite parent)
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
   public PatternFieldEditor(
         String name,
         String labelText,
         int width,
         Composite parent)
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
