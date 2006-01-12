/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.j2ee.ui.wizards.projects;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;
import org.jboss.ide.eclipse.jdt.core.classpath.ClassPathContainerRepository;
import org.jboss.ide.eclipse.jdt.core.util.JavaProjectUtil;
import org.jboss.ide.eclipse.jdt.j2ee.core.JDTJ2EECorePlugin;
import org.jboss.ide.eclipse.jdt.j2ee.core.classpath.J2EE14ClasspathContainer;
import org.jboss.ide.eclipse.jdt.j2ee.ui.JDTJ2EEUIMessages;
import org.jboss.ide.eclipse.jdt.ui.wizards.ProjectWizardPage;
import org.jboss.ide.eclipse.jdt.ws.core.classpath.WSIBasicProfile10ClasspathContainer;

/**
 * Description of the Class
 * 
 * @author Laurent Etiemble
 * @version $Revision$
 */
public class J2EE14ProjectCreationWizardPage extends ProjectWizardPage {
	/**
	 * Constructor for the J2EE14ProjectCreationWizardPage object
	 * 
	 * @param mainPage
	 *            Description of the Parameter
	 */
	public J2EE14ProjectCreationWizardPage(WizardNewProjectCreationPage mainPage) {
		super(mainPage);
		this.setTitle(JDTJ2EEUIMessages
				.getString("J2EE14ProjectCreationWizardPage.title"));//$NON-NLS-1$
		this.setDescription(JDTJ2EEUIMessages
				.getString("J2EE14ProjectCreationWizardPage.description"));//$NON-NLS-1$
	}

	/**
	 * Add necessary source and libraries
	 * 
	 * @param entries
	 * @return
	 * @throws CoreException
	 */
	protected IClasspathEntry[] checkEntries(IClasspathEntry[] entries)
			throws CoreException {
		// entries = JavaProjectUtil.mergeClasspathEntry(entries,
		// JavaProjectUtil.createSourceClasspathEntry(fCurrProject, "src"));
		entries = JavaProjectUtil.mergeClasspathEntry(entries, JavaRuntime
				.getDefaultJREContainerEntry());
		entries = JavaProjectUtil.mergeClasspathEntry(entries,
				ClassPathContainerRepository.getInstance().getEntry(
						J2EE14ClasspathContainer.CLASSPATH_CONTAINER));
		entries = JavaProjectUtil
				.mergeClasspathEntry(
						entries,
						ClassPathContainerRepository
								.getInstance()
								.getEntry(
										WSIBasicProfile10ClasspathContainer.CLASSPATH_CONTAINER));
		return entries;
	}

	protected String[] getBuilders()
	{
		return new String[] { JDTJ2EECorePlugin.WST_VALIDATION_BUILDER_ID };
	}
}
