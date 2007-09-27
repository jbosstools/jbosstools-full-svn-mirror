/*
 * JBoss, the OpenSource J2EE webOS
 * 
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package org.jboss.ide.eclipse.jbosscache.internal;

import org.eclipse.osgi.util.NLS;

/**
 * Message class for plugin messages.  These messages are used 
 * throughout the plugin. 
 *
 */
public class CacheMessages extends NLS
{
   private static final String BUNDLE_NAME = "org.jboss.ide.eclipse.jbosscache.internal.messages";//$NON-NLS-1$

   public static String Column_Properties;

public static String Column_Value;

public static String ContentViewDialog_titleForObjectType;

   public static String ContentViewDialog_fieldName;

   public static String ContentViewDialog_valueName;

public static String Dialog_Title;

   public static String NodeContentView_showObjectsFieldActionText;

   public static String ShowObjectsFieldsAction_MessageDialog_Title;

   public static String ShowObjectsFieldsAction_MessageDialog_Message;

   //Action text
   public static String ObjectGraphView_clearActionText;

   public static String TreeCacheView_importAction;

   public static String TreeCacheView_exportAction;

   public static String TreeCacheView_duplicateAction;

   //Property Page
   public static String CachePropertyPage_lblCacheName;

   public static String CachePropertyPage_grpText;

   public static String StandartConfigurationPage_lblConfFileName;

   public static String NewCacheConfigurationWizard_Same_Cache_Instance_Name_ErrorDialog_Title;

   public static String NewCacheConfigurationWizard_Same_Cache_Instance_Name_ErrorDialog_Message;

   public static String EditConfigurationAction_FileNot_Found_Error_Dialog_Title;

   public static String EditConfigurationAction_FileNot_Found_Error_Dialog_Message;

   public static String ExportAction_FileNot_Found_Error_Dialog_Title;

   public static String ExportAction_FileNot_Found_Error_Dialog_Message;

   public static String DuplicateAction_Duplicated_CacheName;

   public static String RenameAction_Action_Name;

   public static String RenameDialog_Dialog_Title;

   public static String RenameDialog_Label_Text;

   public static String RenameAction_Error_Dialog_Title;

   public static String RenameAction_Error_Dialog_Message;

   static
   {
      // load message values from bundle file
      NLS.initializeMessages(BUNDLE_NAME, CacheMessages.class);
   }

   public static String ExportAction_TreeCacheView_Export_Action_FileDialog_Title;

   public static String ExportAction_TreeCacheView_AlreadyExistFile_Dialog_Title;

   public static String ExportAction_TreeCacheView_AlreadyExistFile_Dialog_Message;

   public static String CachePropertyPage_CachePropoerty_Page_LblConfDirectory;

   public static String ImportAction_TreeCacheView_Import_Action_FileDialog_Title;

   public static String ImportActionDialog_Select_File_To_Import;

   public static String ImportActionDialog_Already_Exist_Cache_Dialog_Message;

   public static String ImportActionDialog_Error_Dialog_Title;

   public static String ImportActionDialog_Fill_Blanks;

   public static String ImportActionDialog_lblCacheType;
   
   public static String DisConnectAction_Monitor_Dialog_Task_Name;
   
   public static String ConnectAction_Monitor_Dialog_Task_Name;
   
   public static String ConnectAction_Job_Monitor_Dialog_Task_Name;
   
   public static String ConnectAction_Dialog_Title;

   public static String NewCacheProjectWizard_Title;

   public static String NewCacheProjectWizard_Description;

   public static String RefreshDialog_0;

   public static String RefreshDialog_Content_Error;

   public static String RefreshDialog_Content_Error_Message;

}