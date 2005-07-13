/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.j2ee.jsp.ui.editors;

import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.ui.texteditor.AbstractDecoratedTextEditorPreferenceConstants;
import org.jboss.ide.eclipse.jdt.j2ee.jsp.ui.JDTJ2EEJSPUIPlugin;
import org.jboss.ide.eclipse.jdt.ui.editors.I18NTextEditor;
import org.jboss.ide.eclipse.jdt.xml.ui.reconciler.IReconcilierHolder;
import org.jboss.ide.eclipse.jdt.xml.ui.reconciler.NodeReconciler;

/*
 * This file contains materials derived from the
 * Solareclipse project. License can be found at :
 * http://solareclipse.sourceforge.net/legal/cpl-v10.html
 */
/**
 * JSP Editor.
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class JSPEditor extends I18NTextEditor implements IReconcilierHolder
{
   private NodeReconciler reconciler;


   /**Constructor for the JSPEditor object */
   public JSPEditor() { }


   /**
    * Gets the reconcilier attribute of the JSPEditor object
    *
    * @return   The reconcilier value
    */
   public NodeReconciler getReconcilier()
   {
      return reconciler;
   }


   /**
    * Sets the reconcilier attribute of the JSPEditor object
    *
    * @param reconciler  The new reconcilier value
    */
   public void setReconcilier(NodeReconciler reconciler)
   {
      this.reconciler = reconciler;
   }


   /**
    * Description of the Method
    *
    * @param event  Description of the Parameter
    * @return       Description of the Return Value
    */
   protected boolean affectsTextPresentation(PropertyChangeEvent event)
   {
      return JDTJ2EEJSPUIPlugin.getDefault().getJSPTextTools().affectsBehavior(event);
   }


   /**
    * Description of the Method
    *
    * @param event  Description of the Parameter
    */
   protected void handlePreferenceStoreChanged(PropertyChangeEvent event)
   {
      try
      {
         ISourceViewer sourceViewer = getSourceViewer();
         if (sourceViewer == null)
         {
            return;
         }

         String property = event.getProperty();

         if (AbstractDecoratedTextEditorPreferenceConstants.EDITOR_TAB_WIDTH.equals(property))
         {
            Object value = event.getNewValue();
            if (value instanceof Integer)
            {
               sourceViewer.getTextWidget().setTabs(((Integer) value).intValue());
            }
            else if (value instanceof String)
            {
               sourceViewer.getTextWidget().setTabs(Integer.parseInt((String) value));
            }

            return;
         }

         if (AbstractDecoratedTextEditorPreferenceConstants.EDITOR_OVERVIEW_RULER.equals(property))
         {
            if (isOverviewRulerVisible())
            {
               showOverviewRuler();
            }
            else
            {
               hideOverviewRuler();
            }

            return;
         }

         if (fLineNumberRulerColumn != null
            && (AbstractDecoratedTextEditorPreferenceConstants.EDITOR_LINE_NUMBER_RULER_COLOR.equals(property)
            || PREFERENCE_COLOR_BACKGROUND_SYSTEM_DEFAULT.equals(property) || PREFERENCE_COLOR_BACKGROUND.equals(property)))
         {
            initializeLineNumberRulerColumn(fLineNumberRulerColumn);
         }

      }
      finally
      {
         super.handlePreferenceStoreChanged(event);
      }
   }


   /** Description of the Method */
   protected void initializeEditor()
   {
      super.initializeEditor();

      this.setPreferenceStore(JDTJ2EEJSPUIPlugin.getDefault().getPreferenceStore());
      JSPTextTools jspTextTools = JDTJ2EEJSPUIPlugin.getDefault().getJSPTextTools();
      this.setSourceViewerConfiguration(new JSPConfiguration(jspTextTools, this));
      this.setDocumentProvider(new JSPDocumentProvider(this));
   }

}
