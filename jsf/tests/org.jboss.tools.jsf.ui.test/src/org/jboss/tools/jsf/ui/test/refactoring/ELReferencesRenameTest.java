package org.jboss.tools.jsf.ui.test.refactoring;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.ltk.core.refactoring.CompositeChange;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.text.edits.MultiTextEdit;
import org.jboss.tools.common.refactoring.BaseFileChange;
import org.jboss.tools.common.util.FileUtil;
import org.jboss.tools.jsf.ui.el.refactoring.RenameMethodParticipant;
import org.jboss.tools.test.util.JobUtils;

public class ELReferencesRenameTest extends ELRefactoringTest {

	public ELReferencesRenameTest(){
		super("Rename Method Refactoring Test");
	}

	public void testRenameMethod() throws CoreException {
		ArrayList<TestChangeStructure> list = new ArrayList<TestChangeStructure>();

		TestChangeStructure structure = new TestChangeStructure(jsfProject, "/WebContent/pages/hello.jsp");
		TestTextChange change = new TestTextChange(353, 4, "name");
		structure.addTextChange(change);
		list.add(structure);

		structure = new TestChangeStructure(jsfProject, "/WebContent/pages/inputUserName.jsp");
		change = new TestTextChange(499, 4, "name");
		structure.addTextChange(change);
		list.add(structure);

		IMethod method = getJavaMethod(jsfProject, "demo.User", "getName");

		renameELReferences(method, "alias", list);
	}

	private void renameELReferences(IJavaElement element, String newName, List<TestChangeStructure> changeList) throws CoreException{
		JobUtils.waitForIdle();

		// Rename EL references
		RenameMethodParticipant participant = new RenameMethodParticipant();
		participant.initialize(element, newName);
		RefactoringStatus status = participant.checkConditions(new NullProgressMonitor(), null);

		assertNotNull("Rename participant returned null status", status);

		assertFalse("There is fatal errors in rename participant", status.hasFatalError());

		CompositeChange rootChange = (CompositeChange)participant.createChange(new NullProgressMonitor());

		assertEquals("There is unexpected number of changes",changeList.size(), rootChange.getChildren().length);

		for(int i = 0; i < rootChange.getChildren().length;i++){
			BaseFileChange fileChange = (BaseFileChange)rootChange.getChildren()[i];

			MultiTextEdit edit = (MultiTextEdit)fileChange.getEdit();

			TestChangeStructure change = findChange(changeList, fileChange.getFile());
			if(change != null){
				assertEquals(change.size(), edit.getChildrenSize());
			}
		}

		rootChange.perform(new NullProgressMonitor());
		JobUtils.waitForIdle();
		// Test results

		for(TestChangeStructure changeStructure : changeList){
			IFile file = changeStructure.getProject().getFile(changeStructure.getFileName());
			String content = null;
			try {
				content = FileUtil.readStream(file);
			} catch (CoreException e) {
				e.printStackTrace();
				fail(e.getMessage());
			}
			//System.out.println("File - "+file.getName()+" offset - "+changeStructure.getOffset()+" expected - ["+changeStructure.getText()+"] actual - ["+content.substring(changeStructure.getOffset(), changeStructure.getOffset()+changeStructure.getLength())+"]");
			for(TestTextChange change : changeStructure.getTextChanges()){
				assertEquals("There is unexpected change in resource - "+file.getName(), newName, content.substring(change.getOffset(), change.getOffset()+newName.length()));
			}
		}
	}
}