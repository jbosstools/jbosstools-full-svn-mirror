/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.j2ee.jsp.ui.editors;

import java.util.Map;

import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jdt.ui.text.JavaTextTools;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.rules.IPartitionTokenScanner;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.jboss.ide.eclipse.jdt.j2ee.jsp.ui.editors.text.JSPDocumentPartitioner;
import org.jboss.ide.eclipse.jdt.j2ee.jsp.ui.editors.text.scanners.JSPDirectiveScanner;
import org.jboss.ide.eclipse.jdt.j2ee.jsp.ui.editors.text.scanners.JSPPartitionScanner;
import org.jboss.ide.eclipse.jdt.j2ee.jsp.ui.editors.text.scanners.JSPScriptScanner;
import org.jboss.ide.eclipse.jdt.j2ee.jsp.ui.editors.text.scanners.SingleTokenScanner;
import org.jboss.ide.eclipse.jdt.ui.text.AbstractTextTools;
import org.jboss.ide.eclipse.jdt.xml.ui.JDTXMLUIPlugin;
import org.jboss.ide.eclipse.jdt.xml.ui.editors.XMLTextTools;

/*
 * This file contains materials derived from the
 * Solareclipse project. License can be found at :
 * http://solareclipse.sourceforge.net/legal/cpl-v10.html
 */
/**
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class JSPTextTools extends AbstractTextTools
{

   /** The Java Language text tools */
   private JavaTextTools javaTextTools;

   /** The JSP brackets scanner */
   private SingleTokenScanner jspBracketScanner;

   /** The JSP tags scanner */
   //	private JSPTagScanner jspTagScanner;
   /** The JSP comments scanner */
   private SingleTokenScanner jspCommentScanner;

   /** The JSP directive scanner */
   private RuleBasedScanner jspDirectiveScanner;

   /** The JSP partitions scanner */
   private JSPPartitionScanner jspPartitionScanner;

   /** The JSP script subpartitions scanner */
   private JSPScriptScanner jspScriptScanner;

   /** The JSP plain text scanner */
   private SingleTokenScanner jspTextScanner;

   /** The XML Language text tools */
   private XMLTextTools xmlTextTools;
   private final static String[] TOKENS = {//
   IJSPSyntaxConstants.JSP_DEFAULT, //
   IJSPSyntaxConstants.JSP_BRACKET, //
   IJSPSyntaxConstants.JSP_TAG, //
   IJSPSyntaxConstants.JSP_ATT_NAME, //
   IJSPSyntaxConstants.JSP_ATT_VALUE, //
   IJSPSyntaxConstants.JSP_COMMENT, //
   IJSPSyntaxConstants.JSP_DIRECTIVE,//
   };


   /**
    * Creates a new JSP text tools collection.
    *
    * @param store  Description of the Parameter
    */
   public JSPTextTools(IPreferenceStore store)
   {
      super(store, TOKENS);

      // REVISIT: preference store
      xmlTextTools = new XMLTextTools(JDTXMLUIPlugin.getDefault().getPreferenceStore());

      // REVISIT: preference store
      javaTextTools = new JavaTextTools(JavaPlugin.getDefault().getPreferenceStore());

      jspPartitionScanner = new JSPPartitionScanner();
      jspScriptScanner = new JSPScriptScanner();

      Map tokens = this.getTokens();

      jspBracketScanner = new SingleTokenScanner(tokens, IJSPSyntaxConstants.JSP_BRACKET);
      jspCommentScanner = new SingleTokenScanner(tokens, IJSPSyntaxConstants.JSP_COMMENT);
      jspDirectiveScanner = new JSPDirectiveScanner(tokens);
      //		jspTagScanner = new XMLTagScanner(tokens);
      //
      //		jspAttributeScanner = new TextScanner(
      //			tokens, '&', IJSPSyntaxConstants.XML_ATT_VALUE);
      jspTextScanner = new SingleTokenScanner(tokens, IJSPSyntaxConstants.JSP_DEFAULT);
   }


   /**
    * @return   Description of the Return Value
    */
   public IDocumentPartitioner createJSPPartitioner()
   {
      return new JSPDocumentPartitioner(jspPartitionScanner, jspScriptScanner);
   }


   /**
    * Returns a scanner which is configured to scan plain text in JSP.
    *
    * @return   a JSP text scanner
    */
   public RuleBasedScanner getJSPBracketScanner()
   {
      return jspBracketScanner;
   }


   /**
    * Returns a scanner which is configured to scan JSP tags.
    *
    * @return   a JSP tag scanner
    */
   //	public RuleBasedScanner getJSPTagScanner() {
   //		return jspTagScanner;
   //	}
   /**
    * Returns a scanner which is configured to scan JSP tag attributes.
    *
    * @return   a JSP attribute scanner
    */
   //	public RuleBasedScanner getJSPAttributeScanner() {
   //		return jspAttributeScanner;
   //	}
   /**
    * Returns a scanner which is configured to scan JSP comments.
    *
    * @return   a JSP comment scanner
    */
   public RuleBasedScanner getJSPCommentScanner()
   {
      return jspCommentScanner;
   }


   /**
    * Returns a scanner which is configured to scan JSP directives code.
    *
    * @return   a JSP directive scanner
    */
   public RuleBasedScanner getJSPDirectiveScanner()
   {
      return jspDirectiveScanner;
   }


   /**
    * @return   The jSPPartitionScanner value
    */
   public IPartitionTokenScanner getJSPPartitionScanner()
   {
      return jspPartitionScanner;
   }


   /**
    * @return   The jSPScriptScanner value
    */
   public IPartitionTokenScanner getJSPScriptScanner()
   {
      return jspScriptScanner;
   }


   /**
    * Returns a scanner which is configured to scan plain text in JSP.
    *
    * @return   a JSP text scanner
    */
   public RuleBasedScanner getJSPTextScanner()
   {
      return jspTextScanner;
   }


   /**
    * @return   The javaPartitionScanner value
    */
   public IPartitionTokenScanner getJavaPartitionScanner()
   {
      return javaTextTools.getPartitionScanner();
   }


   /**
    * @return   The javaTextTools value
    */
   public JavaTextTools getJavaTextTools()
   {
      return javaTextTools;
   }


   /**
    * @return
    */
   public IPreferenceStore getPreferenceStore()
   {
      return this.store;
   }


   /**
    * @return   The xMLTextTools value
    */
   public XMLTextTools getXMLTextTools()
   {
      return xmlTextTools;
   }
}
