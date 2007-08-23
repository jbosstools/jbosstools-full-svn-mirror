package org.jboss.tools.seam.ui;

import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class SeamPerspectiveFactory implements IPerspectiveFactory {

	public static final String PERSPECTIVE_ID = "org.jboss.tools.seam.ui.SeamPerspective";

	public void createInitialLayout(IPageLayout layout) {
 		String editorArea = layout.getEditorArea();

		IFolderLayout leftTop = layout.createFolder("leftTop", IPageLayout.LEFT, (float)0.2, editorArea); //$NON-NLS-1$
		leftTop.addView(JavaUI.ID_PACKAGES);
		leftTop.addView("org.jboss.tools.seam.ui.views.SeamComponentsNavigator");
		leftTop.addPlaceholder(IPageLayout.ID_RES_NAV);
		

		IFolderLayout leftBottom = layout.createFolder("leftBottom", IPageLayout.BOTTOM, 0.64f, "leftTop");
		//leftBottom.addView(IPageLayout.ID_OUTLINE);
		leftBottom.addView(IPageLayout.ID_PROP_SHEET);			
		

		IFolderLayout bottom = layout.createFolder("bottom", IPageLayout.BOTTOM, (float)0.78, editorArea); //$NON-NLS-1$
		bottom.addView(IPageLayout.ID_PROBLEM_VIEW);
		bottom.addView(IPageLayout.ID_TASK_LIST);


		IFolderLayout rightTop = layout.createFolder("right", IPageLayout.RIGHT, (float)0.8, editorArea); //$NON-NLS-1$
		rightTop.addView("com.redhat.palette.swt");
		IFolderLayout rightBottom = layout.createFolder("rightBottom", IPageLayout.BOTTOM, (float)0.64, "right"); //$NON-NLS-1$
		rightBottom.addView(IPageLayout.ID_OUTLINE);

		layout.addActionSet(JavaUI.ID_ACTION_SET);
		layout.addActionSet(JavaUI.ID_ELEMENT_CREATION_ACTION_SET);
		layout.addActionSet(IPageLayout.ID_NAVIGATE_ACTION_SET);

		// views - seam
		layout.addShowViewShortcut("org.jboss.tools.seam.ui.views.SeamComponentsNavigator");
		
		// views - java
		layout.addShowViewShortcut(JavaUI.ID_PACKAGES);
		layout.addShowViewShortcut(JavaUI.ID_TYPE_HIERARCHY);
		layout.addShowViewShortcut(JavaUI.ID_SOURCE_VIEW);

		// views - standard workbench
		layout.addShowViewShortcut(IPageLayout.ID_OUTLINE);
		layout.addShowViewShortcut(IPageLayout.ID_PROBLEM_VIEW);
		layout.addShowViewShortcut(IPageLayout.ID_RES_NAV);

		// new actions - Java project creation wizard
		layout.addNewWizardShortcut("org.eclipse.jdt.ui.wizards.NewPackageCreationWizard"); //$NON-NLS-1$
		layout.addNewWizardShortcut("org.eclipse.jdt.ui.wizards.NewClassCreationWizard"); //$NON-NLS-1$
		layout.addNewWizardShortcut("org.eclipse.jdt.ui.wizards.NewInterfaceCreationWizard"); //$NON-NLS-1$
		layout.addNewWizardShortcut("org.eclipse.jdt.ui.wizards.NewSourceFolderCreationWizard");	 //$NON-NLS-1$
		layout.addNewWizardShortcut("org.eclipse.jdt.ui.wizards.NewSnippetFileCreationWizard"); //$NON-NLS-1$
		layout.addNewWizardShortcut("org.eclipse.ui.wizards.new.folder");//$NON-NLS-1$
		layout.addNewWizardShortcut("org.eclipse.ui.wizards.new.file");//$NON-NLS-1$
	
	}

}
