package org.jboss.tools.jsf.test.refactoring;

import java.util.ArrayList;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.jboss.tools.common.el.core.parser.LexicalToken;
import org.jboss.tools.common.el.core.resolver.MessagePropertyELSegmentImpl;
import org.jboss.tools.common.util.FileUtil;
import org.jboss.tools.jsf.el.refactoring.RenameMessagePropertyProcessor;
import org.jboss.tools.test.util.ProjectImportTestSetup;
import org.jboss.tools.tests.AbstractRefactorTest;

public class MessagePropertyRefactoringTest extends AbstractRefactorTest{
	static String projectName = "JSFKickStartOldFormat";
	static IProject project;
	
	public MessagePropertyRefactoringTest(){
		super("Resource Bundle Message Refactoring Test");
	}
	
	protected void setUp() throws Exception {
		project = ProjectImportTestSetup.loadProject(projectName);
		project.build(IncrementalProjectBuilder.FULL_BUILD, new NullProgressMonitor());
	}
	
	public void testELVariableRename() throws CoreException {
		ArrayList<TestChangeStructure> list = new ArrayList<TestChangeStructure>();

		IFile sourceFile = project.getProject().getFile("/WebContent/pages/hello.jsp");
		
		String sourceFileContent = FileUtil.getContentFromEditorOrFile(sourceFile);
		
		int position = sourceFileContent.indexOf("Messages.hello_message");
		
		TestChangeStructure structure = new TestChangeStructure(project.getProject(), "/WebContent/pages/hello.jsp");
		TestTextChange change = new TestTextChange(position+9, 8, "good_bye");
		structure.addTextChange(change);
		list.add(structure);
		
		IFile propertyFile = project.getProject().getFile("/JavaSource/demo/bundle/Messages.properties");
		String propertyFileContent = FileUtil.getContentFromEditorOrFile(propertyFile);
		
		position = propertyFileContent.indexOf("hello_message");
		
		structure = new TestChangeStructure(project.getProject(), "/JavaSource/demo/bundle/Messages.properties");
		change = new TestTextChange(position, 8, "good_bye");
		structure.addTextChange(change);
		list.add(structure);
		
		MessagePropertyELSegmentImpl segment = new MessagePropertyELSegmentImpl();
		segment.setToken(new LexicalToken(position,13,"hello_message",-1000));
		segment.setMessageBundleResource(propertyFile);
		segment.setBaseName("demo.Messages");
		segment.setMessagePropertySourceReference(0,10);
		RenameMessagePropertyProcessor processor = new RenameMessagePropertyProcessor(sourceFile, segment);
		processor.setNewName("good_bye");

		checkRename(processor, list);
	}

}
