/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.xml.ui.preferences;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.internal.UIPlugin;
import org.eclipse.ui.internal.WorkbenchPlugin;
import org.eclipse.ui.texteditor.AbstractDecoratedTextEditorPreferenceConstants;
import org.eclipse.ui.texteditor.AbstractTextEditor;
import org.eclipse.ui.texteditor.AnnotationPreference;
import org.eclipse.ui.texteditor.MarkerAnnotationPreferences;
import org.jboss.ide.eclipse.jdt.ui.editors.ColorEditor;
import org.jboss.ide.eclipse.jdt.ui.preferences.ChainedPreferenceStore;
import org.jboss.ide.eclipse.jdt.ui.preferences.ITextStylePreferences;
import org.jboss.ide.eclipse.jdt.ui.preferences.OverlayPreferenceStore;
import org.jboss.ide.eclipse.jdt.ui.preferences.PreferenceDescriptor;
import org.jboss.ide.eclipse.jdt.xml.ui.JDTXMLUIMessages;
import org.jboss.ide.eclipse.jdt.xml.ui.JDTXMLUIPlugin;
import org.jboss.ide.eclipse.jdt.xml.ui.editors.IXMLSyntaxConstants;
import org.jboss.ide.eclipse.jdt.xml.ui.editors.XMLConfiguration;
import org.jboss.ide.eclipse.jdt.xml.ui.editors.XMLTextTools;

/*
 * This file contains materials derived from the
 * Solareclipse project. License can be found at :
 * http://solareclipse.sourceforge.net/legal/cpl-v10.html
 */
/**
 * The XMLSyntaxPreferencePage is a preference page that
 * handles setting the colors used by the XML editors.
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class XMLSyntaxPreferencePage extends PreferencePage implements IWorkbenchPreferencePage
{

   ColorEditor bgColorEditor;
   Button bgCustom;

   Button bgDefault;

   List colors;
   Button fgBold;

   ColorEditor fgColorEditor;

   OverlayPreferenceStore overlay;

   SourceViewer preview;

   private Color bgColor;

   private XMLTextTools xmlTextTools;
   /** Description of the Field */
   public final PreferenceDescriptor[] fKeys = new PreferenceDescriptor[]{
      new PreferenceDescriptor(PreferenceDescriptor.BOOLEAN, AbstractTextEditor.PREFERENCE_COLOR_BACKGROUND_SYSTEM_DEFAULT),
      new PreferenceDescriptor(PreferenceDescriptor.STRING, AbstractTextEditor.PREFERENCE_COLOR_BACKGROUND),
      new PreferenceDescriptor(PreferenceDescriptor.STRING, IXMLSyntaxConstants.XML_DEFAULT + ITextStylePreferences.SUFFIX_FOREGROUND),
      new PreferenceDescriptor(PreferenceDescriptor.STRING, IXMLSyntaxConstants.XML_DEFAULT + ITextStylePreferences.SUFFIX_STYLE),
      new PreferenceDescriptor(PreferenceDescriptor.STRING, IXMLSyntaxConstants.XML_TAG + ITextStylePreferences.SUFFIX_FOREGROUND),
      new PreferenceDescriptor(PreferenceDescriptor.STRING, IXMLSyntaxConstants.XML_TAG + ITextStylePreferences.SUFFIX_STYLE),
      new PreferenceDescriptor(PreferenceDescriptor.STRING, IXMLSyntaxConstants.XML_ATT_NAME + ITextStylePreferences.SUFFIX_FOREGROUND),
      new PreferenceDescriptor(PreferenceDescriptor.STRING, IXMLSyntaxConstants.XML_ATT_NAME + ITextStylePreferences.SUFFIX_STYLE),
      new PreferenceDescriptor(PreferenceDescriptor.STRING, IXMLSyntaxConstants.XML_ATT_VALUE + ITextStylePreferences.SUFFIX_FOREGROUND),
      new PreferenceDescriptor(PreferenceDescriptor.STRING, IXMLSyntaxConstants.XML_ATT_VALUE + ITextStylePreferences.SUFFIX_STYLE),
      new PreferenceDescriptor(PreferenceDescriptor.STRING, IXMLSyntaxConstants.XML_ENTITY + ITextStylePreferences.SUFFIX_FOREGROUND),
      new PreferenceDescriptor(PreferenceDescriptor.STRING, IXMLSyntaxConstants.XML_ENTITY + ITextStylePreferences.SUFFIX_STYLE),
      new PreferenceDescriptor(PreferenceDescriptor.STRING, IXMLSyntaxConstants.XML_CDATA + ITextStylePreferences.SUFFIX_FOREGROUND),
      new PreferenceDescriptor(PreferenceDescriptor.STRING, IXMLSyntaxConstants.XML_CDATA + ITextStylePreferences.SUFFIX_STYLE),
      new PreferenceDescriptor(PreferenceDescriptor.STRING, IXMLSyntaxConstants.XML_PI + ITextStylePreferences.SUFFIX_FOREGROUND),
      new PreferenceDescriptor(PreferenceDescriptor.STRING, IXMLSyntaxConstants.XML_PI + ITextStylePreferences.SUFFIX_STYLE),
      new PreferenceDescriptor(PreferenceDescriptor.STRING, IXMLSyntaxConstants.XML_COMMENT + ITextStylePreferences.SUFFIX_FOREGROUND),
      new PreferenceDescriptor(PreferenceDescriptor.STRING, IXMLSyntaxConstants.XML_COMMENT + ITextStylePreferences.SUFFIX_STYLE),
      new PreferenceDescriptor(PreferenceDescriptor.STRING, IXMLSyntaxConstants.XML_DECL + ITextStylePreferences.SUFFIX_FOREGROUND),
      new PreferenceDescriptor(PreferenceDescriptor.STRING, IXMLSyntaxConstants.XML_DECL + ITextStylePreferences.SUFFIX_STYLE),
      new PreferenceDescriptor(PreferenceDescriptor.STRING, IXMLSyntaxConstants.DTD_CONDITIONAL + ITextStylePreferences.SUFFIX_FOREGROUND),
      new PreferenceDescriptor(PreferenceDescriptor.STRING, IXMLSyntaxConstants.DTD_CONDITIONAL + ITextStylePreferences.SUFFIX_STYLE),};

   final String[][] fSyntaxColorListModel = new String[][]{{JDTXMLUIMessages.getString("XmlSyntaxPreferencePage.others"), //$NON-NLS-1$
   IXMLSyntaxConstants.XML_DEFAULT}, {JDTXMLUIMessages.getString("XmlSyntaxPreferencePage.Tag"), //$NON-NLS-1$
   IXMLSyntaxConstants.XML_TAG}, {JDTXMLUIMessages.getString("XmlSyntaxPreferencePage.AttName"), //$NON-NLS-1$
   IXMLSyntaxConstants.XML_ATT_NAME}, {JDTXMLUIMessages.getString("XmlSyntaxPreferencePage.AttValue"), //$NON-NLS-1$
   IXMLSyntaxConstants.XML_ATT_VALUE}, {JDTXMLUIMessages.getString("XmlSyntaxPreferencePage.Entity"), //$NON-NLS-1$
   IXMLSyntaxConstants.XML_ENTITY}, {JDTXMLUIMessages.getString("XmlSyntaxPreferencePage.CDATA"), //$NON-NLS-1$
   IXMLSyntaxConstants.XML_CDATA}, {JDTXMLUIMessages.getString("XmlSyntaxPreferencePage.PI"), //$NON-NLS-1$
   IXMLSyntaxConstants.XML_PI}, {JDTXMLUIMessages.getString("XmlSyntaxPreferencePage.Comment"), //$NON-NLS-1$
   IXMLSyntaxConstants.XML_COMMENT}, {JDTXMLUIMessages.getString("XmlSyntaxPreferencePage.Declaration"), //$NON-NLS-1$
   IXMLSyntaxConstants.XML_DECL}, {JDTXMLUIMessages.getString("XmlSyntaxPreferencePage.Conditional"), //$NON-NLS-1$
   IXMLSyntaxConstants.DTD_CONDITIONAL},};


   /** Constructor for XMLSyntaxPreferencePage. */
   public XMLSyntaxPreferencePage()
   {
      this.setDescription(JDTXMLUIMessages.getString("XmlSyntaxPreferencePage.description"));//$NON-NLS-1$
      this.setPreferenceStore(JDTXMLUIPlugin.getDefault().getPreferenceStore());
      this.overlay = new OverlayPreferenceStore(this.getPreferenceStore(), this.fKeys);
   }


   /*
    * @see org.eclipse.jface.dialogs.IDialogPage#dispose()
    */
   /** Description of the Method */
   public void dispose()
   {
      if (xmlTextTools != null)
      {
         xmlTextTools.dispose();
         xmlTextTools = null;
      }

      if (overlay != null)
      {
         overlay.stop();
         overlay = null;
      }

      super.dispose();
   }


   /*
    * @see IWorkbenchPreferencePage#init(IWorkbench)
    */
   /**
    * Description of the Method
    *
    * @param workbench  Description of the Parameter
    */
   public void init(IWorkbench workbench)
   {
   }


   /*
    * @see org.eclipse.jface.preference.IPreferencePage#performOk()
    */
   /**
    * Description of the Method
    *
    * @return   Description of the Return Value
    */
   public boolean performOk()
   {
      overlay.propagate();
      JDTXMLUIPlugin.getDefault().savePluginPreferences();

      return true;
   }


   /**
    * Description of the Method
    *
    * @param parent  Description of the Parameter
    * @return        Description of the Return Value
    */
   protected Control createContents(Composite parent)
   {
      this.overlay.load();
      this.overlay.start();

      Composite colorComposite = new Composite(parent, SWT.NULL);
      colorComposite.setLayout(new GridLayout());

      Group backgroundComposite = new Group(colorComposite, SWT.SHADOW_ETCHED_IN);

      backgroundComposite.setLayout(new RowLayout());
      backgroundComposite.setText(JDTXMLUIMessages.getString("XmlSyntaxPreferencePage.backgroundColor"));//$NON-NLS-1$

      SelectionListener backgroundSelectionListener =
         new SelectionListener()
         {

            public void widgetDefaultSelected(SelectionEvent e)
            {
            }


            public void widgetSelected(SelectionEvent e)
            {
               boolean custom = bgCustom.getSelection();
               bgColorEditor.getButton().setEnabled(custom);
               overlay.setValue(AbstractTextEditor.PREFERENCE_COLOR_BACKGROUND_SYSTEM_DEFAULT, !custom);
            }
         };

      bgDefault = new Button(backgroundComposite, SWT.RADIO | SWT.LEFT);
      bgDefault.setText(JDTXMLUIMessages.getString("XmlSyntaxPreferencePage.systemDefault"));//$NON-NLS-1$
      bgDefault.addSelectionListener(backgroundSelectionListener);

      bgCustom = new Button(backgroundComposite, SWT.RADIO | SWT.LEFT);
      bgCustom.setText(JDTXMLUIMessages.getString("XmlSyntaxPreferencePage.custom"));//$NON-NLS-1$
      bgCustom.addSelectionListener(backgroundSelectionListener);

      bgColorEditor = new ColorEditor(backgroundComposite);

      Label label = new Label(colorComposite, SWT.LEFT);
      label.setText(JDTXMLUIMessages.getString("XmlSyntaxPreferencePage.foreground"));//$NON-NLS-1$
      label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

      Composite editorComposite = new Composite(colorComposite, SWT.NONE);
      GridLayout layout = new GridLayout();
      layout.numColumns = 2;
      layout.marginHeight = 0;
      layout.marginWidth = 0;
      editorComposite.setLayout(layout);
      GridData gd = new GridData(GridData.FILL_BOTH);
      editorComposite.setLayoutData(gd);

      colors = new List(editorComposite, SWT.SINGLE | SWT.V_SCROLL | SWT.BORDER);
      gd = new GridData(GridData.FILL_BOTH);
      gd.heightHint = convertHeightInCharsToPixels(5);
      colors.setLayoutData(gd);

      Composite stylesComposite = new Composite(editorComposite, SWT.NONE);
      layout = new GridLayout();
      layout.marginHeight = 0;
      layout.marginWidth = 0;
      layout.numColumns = 2;
      stylesComposite.setLayout(layout);
      stylesComposite.setLayoutData(new GridData(GridData.FILL_BOTH));

      label = new Label(stylesComposite, SWT.LEFT);
      label.setText(JDTXMLUIMessages.getString("XmlSyntaxPreferencePage.color"));//$NON-NLS-1$
      gd = new GridData();
      gd.horizontalAlignment = GridData.BEGINNING;
      label.setLayoutData(gd);

      fgColorEditor = new ColorEditor(stylesComposite);

      Button fgColorButton = fgColorEditor.getButton();
      gd = new GridData(GridData.FILL_HORIZONTAL);
      gd.horizontalAlignment = GridData.BEGINNING;
      fgColorButton.setLayoutData(gd);

      label = new Label(stylesComposite, SWT.LEFT);
      label.setText(JDTXMLUIMessages.getString("XmlSyntaxPreferencePage.bold"));//$NON-NLS-1$
      gd = new GridData();
      gd.horizontalAlignment = GridData.BEGINNING;
      label.setLayoutData(gd);

      fgBold = new Button(stylesComposite, SWT.CHECK);
      gd = new GridData(GridData.FILL_HORIZONTAL);
      gd.horizontalAlignment = GridData.BEGINNING;
      fgBold.setLayoutData(gd);

      label = new Label(colorComposite, SWT.LEFT);
      label.setText(JDTXMLUIMessages.getString("XmlSyntaxPreferencePage.preview"));//$NON-NLS-1$
      label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

      Control previewer = createPreviewer(colorComposite);
      gd = new GridData(GridData.FILL_BOTH);
      gd.widthHint = convertWidthInCharsToPixels(20);
      gd.heightHint = convertHeightInCharsToPixels(5);
      previewer.setLayoutData(gd);

      colors.addSelectionListener(
         new SelectionListener()
         {
            public void widgetDefaultSelected(SelectionEvent e)
            {
            }


            public void widgetSelected(SelectionEvent e)
            {
               handleSyntaxColorListSelection();
            }
         });

      bgColorEditor.getButton().addSelectionListener(
         new SelectionListener()
         {
            public void widgetDefaultSelected(SelectionEvent e)
            {
            }


            public void widgetSelected(SelectionEvent e)
            {
               PreferenceConverter.setValue(overlay, AbstractTextEditor.PREFERENCE_COLOR_BACKGROUND, bgColorEditor.getColorValue());
            }
         });

      fgColorButton.addSelectionListener(
         new SelectionListener()
         {
            public void widgetDefaultSelected(SelectionEvent e)
            {
            }


            public void widgetSelected(SelectionEvent e)
            {
               int i = colors.getSelectionIndex();

               String key = fSyntaxColorListModel[i][1];

               PreferenceConverter.setValue(overlay, key + ITextStylePreferences.SUFFIX_FOREGROUND, fgColorEditor.getColorValue());
            }
         });

      fgBold.addSelectionListener(
         new SelectionListener()
         {
            public void widgetDefaultSelected(SelectionEvent e)
            {
            }


            public void widgetSelected(SelectionEvent e)
            {
               int i = colors.getSelectionIndex();

               String key = fSyntaxColorListModel[i][1];

               String value = (fgBold.getSelection()) ? ITextStylePreferences.STYLE_BOLD : ITextStylePreferences.STYLE_NORMAL;

               overlay.setValue(key + ITextStylePreferences.SUFFIX_STYLE, value);
            }
         });

      this.initialize();

      return colorComposite;
   }


   /*
    * @see PreferencePage#performDefaults()
    */
   /** Description of the Method */
   protected void performDefaults()
   {
      overlay.loadDefaults();
      //initializeFields();
      handleSyntaxColorListSelection();

      super.performDefaults();

      preview.invalidateTextPresentation();
   }


   /** Description of the Method */
   void handleSyntaxColorListSelection()
   {
      int i = colors.getSelectionIndex();

      String key = fSyntaxColorListModel[i][1];

      RGB rgb = PreferenceConverter.getColor(overlay, key + ITextStylePreferences.SUFFIX_FOREGROUND);

      fgColorEditor.setColorValue(rgb);

      // REVISIT
      fgBold.setSelection(overlay.getString(key + ITextStylePreferences.SUFFIX_STYLE).indexOf(ITextStylePreferences.STYLE_BOLD) >= 0);
   }


   /**
    * Initializes the given viewer's colors.
    *
    * @param viewer  the viewer to be initialized
    */
   void initializeViewerColors(ISourceViewer viewer)
   {
      if (overlay != null)
      {
         StyledText styledText = viewer.getTextWidget();

         // ---------- background color ----------------------
         Color color = null;
         if (!overlay.getBoolean(AbstractTextEditor.PREFERENCE_COLOR_BACKGROUND_SYSTEM_DEFAULT))
         {
            color = createColor(overlay, AbstractTextEditor.PREFERENCE_COLOR_BACKGROUND, styledText.getDisplay());
         }

         styledText.setBackground(color);

         if (bgColor != null)
         {
            bgColor.dispose();
         }

         bgColor = color;
      }
   }


   /**
    * Creates a color from the information stored in the given preference store.
    * Returns <code>null</code> if there is no such information available.
    *
    * @param store    Description of the Parameter
    * @param key      Description of the Parameter
    * @param display  Description of the Parameter
    * @return         Description of the Return Value
    */
   private Color createColor(IPreferenceStore store, String key, Display display)
   {
      RGB rgb = null;

      if (store.contains(key))
      {
         if (store.isDefault(key))
         {
            rgb = PreferenceConverter.getDefaultColor(store, key);
         }
         else
         {
            rgb = PreferenceConverter.getColor(store, key);
         }

         if (rgb != null)
         {
            return new Color(display, rgb);
         }
      }

      return null;
   }


   /**
    * Description of the Method
    *
    * @param parent  Description of the Parameter
    * @return        Description of the Return Value
    */
   private Control createPreviewer(Composite parent)
   {
      this.xmlTextTools = new XMLTextTools(this.overlay);// REVISIT: DTD

      this.preview = new SourceViewer(parent, null, SWT.V_SCROLL | SWT.H_SCROLL | SWT.BORDER);
      this.preview.configure(new XMLConfiguration(xmlTextTools));
      this.preview.getTextWidget().setFont(JFaceResources.getFontRegistry().get(JFaceResources.TEXT_FONT));
      this.preview.setEditable(false);

      this.initializeViewerColors(preview);

      String content = this.loadPreviewContentFromFile("preview.xml");//$NON-NLS-1$
      IDocument document = new Document(content);
      IDocumentPartitioner partitioner = xmlTextTools.createXMLPartitioner();// REVISIT: DTD

      partitioner.connect(document);
      document.setDocumentPartitioner(partitioner);

      this.preview.setDocument(document);

      this.overlay.addPropertyChangeListener(
         new IPropertyChangeListener()
         {
            public void propertyChange(PropertyChangeEvent event)
            {
               String p = event.getProperty();
               if (p.equals(AbstractTextEditor.PREFERENCE_COLOR_BACKGROUND) || p.equals(AbstractTextEditor.PREFERENCE_COLOR_BACKGROUND_SYSTEM_DEFAULT))
               {
                  initializeViewerColors(preview);
               }

               preview.invalidateTextPresentation();
            }
         });

      return preview.getControl();
   }


   /** */
   private void initialize()
   {
      initializeFields();

      for (int i = 0; i < fSyntaxColorListModel.length; i++)
      {
         colors.add(fSyntaxColorListModel[i][0]);
      }

      colors.getDisplay().asyncExec(
         new Runnable()
         {
            public void run()
            {
               colors.select(0);
               handleSyntaxColorListSelection();
            }
         });
   }


   /** Description of the Method */
   private void initializeFields()
   {
      RGB rgb = PreferenceConverter.getColor(overlay, AbstractTextEditor.PREFERENCE_COLOR_BACKGROUND);
      bgColorEditor.setColorValue(rgb);

      boolean def = overlay.getBoolean(AbstractTextEditor.PREFERENCE_COLOR_BACKGROUND_SYSTEM_DEFAULT);
      bgDefault.setSelection(def);
      bgCustom.setSelection(!def);
      bgColorEditor.getButton().setEnabled(!def);
   }


   /**
    * Description of the Method
    *
    * @param filename  Description of the Parameter
    * @return          Description of the Return Value
    */
   private String loadPreviewContentFromFile(String filename)
   {
      StringBuffer string = new StringBuffer(512);

      try
      {
         char[] buf = new char[512];
         BufferedReader reader = new BufferedReader(new InputStreamReader(XMLSyntaxPreferencePage.class.getResourceAsStream(filename)));

         try
         {
            while (true)
            {
               int n = reader.read(buf);
               if (n < 0)
               {
                  break;
               }

               string.append(buf, 0, n);
            }
         }
         finally
         {
            reader.close();
         }
      }
      catch (IOException e)
      {
      }

      return string.toString();
   }


   /**
    * Description of the Method
    *
    * @param store  Description of the Parameter
    */
   public static void initDefaults(IPreferenceStore store)
   {
      // REVISIT: start of common preferences

      PreferenceConverter.setDefault(store, AbstractTextEditor.PREFERENCE_COLOR_FOREGROUND, Display.getDefault().getSystemColor(SWT.COLOR_LIST_FOREGROUND)
         .getRGB());

      store.setDefault(AbstractTextEditor.PREFERENCE_COLOR_FOREGROUND_SYSTEM_DEFAULT, true);

      PreferenceConverter.setDefault(store, AbstractTextEditor.PREFERENCE_COLOR_BACKGROUND, Display.getDefault().getSystemColor(SWT.COLOR_LIST_BACKGROUND)
         .getRGB());

      store.setDefault(AbstractTextEditor.PREFERENCE_COLOR_BACKGROUND_SYSTEM_DEFAULT, true);

      IPreferenceStore uiStore = UIPlugin.getDefault().getPreferenceStore();

      if (uiStore != null)
      {
         Set keys = new HashSet(Arrays.asList(new String[]{//
         AbstractTextEditor.PREFERENCE_NAVIGATION_SMART_HOME_END, //
         AbstractTextEditor.PREFERENCE_COLOR_FIND_SCOPE, //
         AbstractDecoratedTextEditorPreferenceConstants.EDITOR_CURRENT_LINE, //
         AbstractDecoratedTextEditorPreferenceConstants.EDITOR_CURRENT_LINE_COLOR, //
         AbstractDecoratedTextEditorPreferenceConstants.EDITOR_PRINT_MARGIN, //
         AbstractDecoratedTextEditorPreferenceConstants.EDITOR_PRINT_MARGIN_COLOR, //
         AbstractDecoratedTextEditorPreferenceConstants.EDITOR_PRINT_MARGIN_COLUMN, //
         AbstractDecoratedTextEditorPreferenceConstants.EDITOR_OVERVIEW_RULER, //
         AbstractDecoratedTextEditorPreferenceConstants.EDITOR_LINE_NUMBER_RULER, //
         AbstractDecoratedTextEditorPreferenceConstants.EDITOR_LINE_NUMBER_RULER_COLOR,//
         }));

         Iterator i = new MarkerAnnotationPreferences().getAnnotationPreferences().iterator();

         while (i.hasNext())
         {
            AnnotationPreference info = (AnnotationPreference) i.next();
            keys.add(info.getColorPreferenceKey());
            keys.add(info.getTextPreferenceKey());
            keys.add(info.getOverviewRulerPreferenceKey());
         }

         ChainedPreferenceStore.startPropagating(uiStore, store, keys);
      }

      WorkbenchPlugin.getDefault().getPreferenceStore();
      IPreferenceStore wkStore = WorkbenchPlugin.getDefault().getPreferenceStore();
      if (wkStore != null)
      {
         ChainedPreferenceStore.startPropagating(wkStore, store, new String[]{JFaceResources.TEXT_FONT});
      }

      setDefault(store, IXMLSyntaxConstants.XML_DEFAULT, "0,0,0", ITextStylePreferences.STYLE_NORMAL);//$NON-NLS-1$
      setDefault(store, IXMLSyntaxConstants.XML_TAG, "127,0,127", ITextStylePreferences.STYLE_NORMAL);//$NON-NLS-1$
      setDefault(store, IXMLSyntaxConstants.XML_ATT_NAME, "0,127,0", ITextStylePreferences.STYLE_NORMAL);//$NON-NLS-1$
      setDefault(store, IXMLSyntaxConstants.XML_ATT_VALUE, "0,0,255", ITextStylePreferences.STYLE_NORMAL);//$NON-NLS-1$
      setDefault(store, IXMLSyntaxConstants.XML_ENTITY, "127,127,0", ITextStylePreferences.STYLE_NORMAL);//$NON-NLS-1$
      setDefault(store, IXMLSyntaxConstants.XML_CDATA, "127,127,0", ITextStylePreferences.STYLE_NORMAL);//$NON-NLS-1$
      setDefault(store, IXMLSyntaxConstants.XML_PI, "127,127,127", ITextStylePreferences.STYLE_BOLD);//$NON-NLS-1$
      setDefault(store, IXMLSyntaxConstants.XML_COMMENT, "127,0,0", ITextStylePreferences.STYLE_NORMAL);//$NON-NLS-1$
      setDefault(store, IXMLSyntaxConstants.XML_DECL, "127,0,127", ITextStylePreferences.STYLE_BOLD);//$NON-NLS-1$
      setDefault(store, IXMLSyntaxConstants.DTD_CONDITIONAL, "127,127,0", ITextStylePreferences.STYLE_BOLD);//$NON-NLS-1$
   }


   /**
    * Sets the default attribute of the XMLSyntaxPreferencePage class
    *
    * @param store     The new default value
    * @param constant  The new default value
    * @param color     The new default value
    * @param style     The new default value
    */
   private static void setDefault(IPreferenceStore store, String constant, String color, String style)
   {
      store.setDefault(constant + ITextStylePreferences.SUFFIX_FOREGROUND, color);
      store.setDefault(constant + ITextStylePreferences.SUFFIX_STYLE, style);
   }

}
