package org.jboss.tools.vpe.ui.bot.test;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.eclipse.swtbot.swt.finder.SWTBotTestCase;
import org.jboss.tools.vpe.ui.bot.test.editor.BlockCommentTest;
import org.jboss.tools.vpe.ui.bot.test.editor.EditingActionsTest;
import org.jboss.tools.vpe.ui.bot.test.editor.InsertActionsTest;
import org.jboss.tools.vpe.ui.bot.test.editor.MinMaxPanesTest;
import org.jboss.tools.vpe.ui.bot.test.editor.StylesOnThePageTest;
import org.jboss.tools.vpe.ui.bot.test.editor.TextEditingActionsTest;
import org.jboss.tools.vpe.ui.bot.test.editor.TextSelectionTest;
import org.jboss.tools.vpe.ui.bot.test.editor.ToggleCommentTest;
import org.jboss.tools.vpe.ui.bot.test.editor.VerificationOfNameSpacesTest;
import org.jboss.tools.vpe.ui.bot.test.editor.JspFileEditingTest;
import org.jboss.tools.vpe.ui.bot.test.editor.VisualEditorContextMenuTest;
import org.jboss.tools.vpe.ui.bot.test.editor.XhtmlFilePerformanceTest;
import org.jboss.tools.vpe.ui.bot.test.editor.pagedesign.AddSubstitutedELExpressionFolderScopeTest;
import org.jboss.tools.vpe.ui.bot.test.editor.pagedesign.EditingELValueTest;
import org.jboss.tools.vpe.ui.bot.test.editor.pagedesign.IncludedCssFilesJSPTest;
import org.jboss.tools.vpe.ui.bot.test.editor.pagedesign.IncludedCssFilesTest;
import org.jboss.tools.vpe.ui.bot.test.editor.pagedesign.IncludedTagLibsTest;
import org.jboss.tools.vpe.ui.bot.test.editor.pagedesign.ManipulatingELValueTest;
import org.jboss.tools.vpe.ui.bot.test.editor.pagedesign.SubstitutedELExressionsTest;
import org.jboss.tools.vpe.ui.bot.test.editor.pagedesign.ToolbarTextFormattingTest;
import org.jboss.tools.vpe.ui.bot.test.editor.preferences.AlwaysHideSelectionBarWithoutPromptTest;
import org.jboss.tools.vpe.ui.bot.test.editor.preferences.BorderForUnknownTagsTest;
import org.jboss.tools.vpe.ui.bot.test.editor.preferences.ChangeEditorTabForTheFirstOpenPageTest;
import org.jboss.tools.vpe.ui.bot.test.editor.preferences.GlobalELVariablesTest;
import org.jboss.tools.vpe.ui.bot.test.editor.preferences.PromptForTagAttributesDuringTagInsertTest;
import org.jboss.tools.vpe.ui.bot.test.editor.preferences.ShowNonVisualTagsTest;
import org.jboss.tools.vpe.ui.bot.test.editor.preferences.ShowResourceBundlesUsageasELexpressionsTest;
import org.jboss.tools.vpe.ui.bot.test.editor.preferences.ShowSelectionTagBarTest;
import org.jboss.tools.vpe.ui.bot.test.editor.selectionbar.SelectionBarTest;
import org.jboss.tools.vpe.ui.bot.test.editor.tags.ActionParamTagTest;
import org.jboss.tools.vpe.ui.bot.test.editor.tags.AjaxInvisibleTagsTest;
import org.jboss.tools.vpe.ui.bot.test.editor.tags.AjaxValidatorTagTest;
import org.jboss.tools.vpe.ui.bot.test.editor.tags.BeanValidatorTagTest;
import org.jboss.tools.vpe.ui.bot.test.editor.tags.CalendarTagTest;
import org.jboss.tools.vpe.ui.bot.test.editor.tags.ColumnGroupTagTest;
import org.jboss.tools.vpe.ui.bot.test.editor.tags.ColumnTagTest;
import org.jboss.tools.vpe.ui.bot.test.editor.tags.ColumnsTagTest;
import org.jboss.tools.vpe.ui.bot.test.editor.tags.ComboBoxTagTest;
import org.jboss.tools.vpe.ui.bot.test.editor.tags.CommandButtonTagTest;
import org.jboss.tools.vpe.ui.bot.test.editor.tags.CommandLinkTagTest;
import org.jboss.tools.vpe.ui.bot.test.editor.tags.CoreHTMLTagsTest;
import org.jboss.tools.vpe.ui.bot.test.editor.tags.DataDefinitionTagTest;
import org.jboss.tools.vpe.ui.bot.test.editor.tags.DataGridTagTest;
import org.jboss.tools.vpe.ui.bot.test.editor.tags.DataListTagTest;
import org.jboss.tools.vpe.ui.bot.test.editor.tags.DataOrderedListTagTest;
import org.jboss.tools.vpe.ui.bot.test.editor.tags.DataScrollerTagTest;
import org.jboss.tools.vpe.ui.bot.test.editor.tags.DataTableTagTest;
import org.jboss.tools.vpe.ui.bot.test.editor.tags.EditorTagTest;
import org.jboss.tools.vpe.ui.bot.test.editor.tags.ExtendedDataTableTagTest;
import org.jboss.tools.vpe.ui.bot.test.editor.tags.FileUploadTagTest;
import org.jboss.tools.vpe.ui.bot.test.editor.tags.HtmlCommandLinkTagTest;
import org.jboss.tools.vpe.ui.bot.test.editor.tags.IncludeTagTest;
import org.jboss.tools.vpe.ui.bot.test.editor.tags.InplaceInputTagTest;
import org.jboss.tools.vpe.ui.bot.test.editor.tags.InplaceSelectInputTagTest;
import org.jboss.tools.vpe.ui.bot.test.editor.tags.JSFTagsTest;
import org.jboss.tools.vpe.ui.bot.test.editor.tags.ListShuttleTagTest;
import org.jboss.tools.vpe.ui.bot.test.editor.tags.LogTagTest;
import org.jboss.tools.vpe.ui.bot.test.editor.tags.PanelMenuTagTest;
import org.jboss.tools.vpe.ui.bot.test.editor.tags.PanelTagTest;
import org.jboss.tools.vpe.ui.bot.test.editor.tags.PickListTagTest;
import org.jboss.tools.vpe.ui.bot.test.editor.tags.ProgressTagTest;
import org.jboss.tools.vpe.ui.bot.test.editor.tags.SpacerTagTest;
import org.jboss.tools.vpe.ui.bot.test.editor.tags.TogglePanelAndToogleControlTagTest;
import org.jboss.tools.vpe.ui.bot.test.editor.tags.ToolbarAndToolbarGroupTagTest;
import org.jboss.tools.vpe.ui.bot.test.editor.tags.TreeTagTest;
import org.jboss.tools.vpe.ui.bot.test.editor.tags.VirtualEarthTagTest;
import org.jboss.tools.vpe.ui.bot.test.jbide.JBIDE4556Test;
import org.jboss.tools.vpe.ui.bot.test.jbide.JBIDE9445Test_DuplicateSourceMenu;
import org.jboss.tools.vpe.ui.bot.test.palette.CancelTagLibDefenitionTest;
import org.jboss.tools.vpe.ui.bot.test.palette.ImportTagsFromTLDFileTest;
import org.jboss.tools.vpe.ui.bot.test.palette.ManagePaletteGroupsTest;
import org.jboss.tools.vpe.ui.bot.test.palette.PaletteEditorTest;
import org.jboss.tools.vpe.ui.bot.test.smoke.CodeCompletionTest;
import org.jboss.tools.vpe.ui.bot.test.smoke.EditorSynchronizationTest;
import org.jboss.tools.vpe.ui.bot.test.smoke.JSPPageCreationTest;
import org.jboss.tools.vpe.ui.bot.test.smoke.MarkersTest;
import org.jboss.tools.vpe.ui.bot.test.smoke.OpenOnTest;
import org.jboss.tools.vpe.ui.bot.test.smoke.RenameFacesConfigFileTest;
import org.jboss.tools.vpe.ui.bot.test.smoke.RenameJSPFileTest;
import org.jboss.tools.vpe.ui.bot.test.smoke.RenameXHTMLFileTest;
import org.jboss.tools.vpe.ui.bot.test.smoke.XHTMLPageCreationTest;
import org.jboss.tools.vpe.ui.bot.test.wizard.ExternalizeStringsDialogTest;
import org.jboss.tools.vpe.ui.bot.test.wizard.ImportUnknownTagsWizardTest;
import org.jboss.tools.vpe.ui.bot.test.wizard.NewXHTMLPageWizardTest;
import org.jboss.tools.vpe.ui.bot.test.wizard.VPESourceCodeTemplatesPreferencePageTest;

public class VPEAllBotTests extends SWTBotTestCase{
	public static Test suite(){
		TestSuite suite = new TestSuite("VPE All Tests"); //$NON-NLS-1$
		suite.addTestSuite(VisualEditorContextMenuTest.class);
		suite.addTestSuite(EditingActionsTest.class);
    suite.addTestSuite(NewXHTMLPageWizardTest.class);
    suite.addTestSuite(CodeCompletionTest.class);
    suite.addTestSuite(ExternalizeStringsDialogTest.class);
		suite.addTestSuite(CancelTagLibDefenitionTest.class);
		suite.addTestSuite(ImportTagsFromTLDFileTest.class);
		suite.addTestSuite(ToggleCommentTest.class);
		suite.addTestSuite(BlockCommentTest.class);
		suite.addTestSuite(ChangeEditorTabForTheFirstOpenPageTest.class);
		suite.addTestSuite(JBIDE4556Test.class);
		suite.addTestSuite(VerificationOfNameSpacesTest.class);
		suite.addTestSuite(BorderForUnknownTagsTest.class);
		suite.addTestSuite(ShowResourceBundlesUsageasELexpressionsTest.class);
		suite.addTestSuite(ShowSelectionTagBarTest.class);
		suite.addTestSuite(AlwaysHideSelectionBarWithoutPromptTest.class);
		suite.addTestSuite(ShowNonVisualTagsTest.class);
		suite.addTestSuite(AddSubstitutedELExpressionFolderScopeTest.class);
		suite.addTestSuite(EditorSynchronizationTest.class);
		suite.addTestSuite(JSPPageCreationTest.class);
		suite.addTestSuite(XHTMLPageCreationTest.class);
		suite.addTestSuite(RenameFacesConfigFileTest.class);
		suite.addTestSuite(RenameJSPFileTest.class);
		suite.addTestSuite(RenameXHTMLFileTest.class);
		suite.addTestSuite(ImportUnknownTagsWizardTest.class);
		suite.addTestSuite(VPESourceCodeTemplatesPreferencePageTest.class);
		suite.addTestSuite(JspFileEditingTest.class);
		suite.addTestSuite(ManagePaletteGroupsTest.class);
		suite.addTestSuite(PaletteEditorTest.class);
		suite.addTestSuite(ToolbarTextFormattingTest.class);
		suite.addTestSuite(InsertActionsTest.class);
		suite.addTestSuite(TextEditingActionsTest.class);
		suite.addTestSuite(PromptForTagAttributesDuringTagInsertTest.class);
		suite.addTestSuite(IncludedTagLibsTest.class);
		suite.addTestSuite(SubstitutedELExressionsTest.class);
		suite.addTestSuite(MinMaxPanesTest.class);
		suite.addTestSuite(EditingELValueTest.class);
		suite.addTestSuite(ManipulatingELValueTest.class);
		suite.addTestSuite(SelectionBarTest.class);
		suite.addTestSuite(IncludedCssFilesTest.class);
		suite.addTestSuite(GlobalELVariablesTest.class);
		suite.addTestSuite(IncludedCssFilesJSPTest.class);
		suite.addTestSuite(StylesOnThePageTest.class);
		suite.addTestSuite(TextSelectionTest.class);
		suite.addTestSuite(CoreHTMLTagsTest.class);
		suite.addTestSuite(JSFTagsTest.class);
		suite.addTestSuite(ColumnsTagTest.class);
		suite.addTestSuite(ComboBoxTagTest.class);
		suite.addTestSuite(FileUploadTagTest.class);
		suite.addTestSuite(InplaceInputTagTest.class);
		suite.addTestSuite(InplaceSelectInputTagTest.class);
		suite.addTestSuite(PickListTagTest.class);
    suite.addTestSuite(ProgressTagTest.class);
		suite.addTestSuite(PanelMenuTagTest.class);
		suite.addTestSuite(ListShuttleTagTest.class);
		suite.addTestSuite(DataDefinitionTagTest.class);
		suite.addTestSuite(EditorTagTest.class);
		suite.addTestSuite(TreeTagTest.class);
		suite.addTestSuite(CalendarTagTest.class);
    suite.addTestSuite(PanelTagTest.class);
    suite.addTestSuite(DataTableTagTest.class);
		suite.addTestSuite(SpacerTagTest.class);
		suite.addTestSuite(DataScrollerTagTest.class);
		suite.addTestSuite(ColumnTagTest.class);
  	suite.addTestSuite(ActionParamTagTest.class);
		suite.addTestSuite(AjaxValidatorTagTest.class);
		suite.addTestSuite(BeanValidatorTagTest.class);
    suite.addTestSuite(ColumnGroupTagTest.class);
    suite.addTestSuite(DataGridTagTest.class);    		
    suite.addTestSuite(VirtualEarthTagTest.class);
    suite.addTestSuite(DataListTagTest.class);
		suite.addTestSuite(DataOrderedListTagTest.class);
		suite.addTestSuite(ExtendedDataTableTagTest.class);
		suite.addTestSuite(ToolbarAndToolbarGroupTagTest.class);
		suite.addTestSuite(TogglePanelAndToogleControlTagTest.class);
		suite.addTestSuite(CommandButtonTagTest.class);
		suite.addTestSuite(CommandLinkTagTest.class);
		suite.addTestSuite(HtmlCommandLinkTagTest.class);
   	suite.addTestSuite(IncludeTagTest.class);
		suite.addTestSuite(AjaxInvisibleTagsTest.class);
		suite.addTestSuite(LogTagTest.class);
		suite.addTestSuite(OpenOnTest.class);
		suite.addTestSuite(XhtmlFilePerformanceTest.class);
		suite.addTestSuite(MarkersTest.class);
		suite.addTestSuite(JBIDE9445Test_DuplicateSourceMenu.class);
		return new TestSetup(suite);
	}
}
