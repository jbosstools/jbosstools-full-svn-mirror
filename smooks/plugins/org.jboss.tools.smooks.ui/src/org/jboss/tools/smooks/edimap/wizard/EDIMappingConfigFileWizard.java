/*******************************************************************************
 * Copyright (c) 2008 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.smooks.edimap.wizard;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.jboss.tools.smooks.configuration.SmooksConfigurationActivator;
import org.jboss.tools.smooks.model.medi.Delimiters;
import org.jboss.tools.smooks.model.medi.Description;
import org.jboss.tools.smooks.model.medi.DocumentRoot;
import org.jboss.tools.smooks.model.medi.EdiMap;
import org.jboss.tools.smooks.model.medi.MEdiFactory;
import org.jboss.tools.smooks.model.medi.Segments;
import org.jboss.tools.smooks.model.medi.util.MEdiResourceFactoryImpl;

/**
 * @author Dart (dpeng@redhat.com)
 * 
 */
public class EDIMappingConfigFileWizard extends Wizard implements INewWizard {
	private EDIMappingConfigFileContainerSelectionPage pathPage;

	private EDIMappingConfigFileConfigPage configPage;

	private IStructuredSelection selection;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
	@Override
	public boolean performFinish() {

		final IPath containerPath = pathPage.getContainerFullPath();
		final String fileName = pathPage.getFileName();
		
		IRunnableWithProgress op = new IRunnableWithProgress() {
			public void run(IProgressMonitor monitor) throws InvocationTargetException {
				try {
					doFinish(containerPath, fileName, monitor);
				} catch (CoreException e) {
					throw new InvocationTargetException(e);
				} finally {
					monitor.done();
				}
			}
		};
		try {
			getContainer().run(true, false, op);
		} catch (InterruptedException e) {
			return false;
		} catch (InvocationTargetException e) {
			Throwable realException = e.getTargetException();
			MessageDialog.openError(getShell(), "Error", realException.getMessage());
			return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#addPages()
	 */
	@Override
	public void addPages() {
		if (pathPage == null) {
			pathPage = new EDIMappingConfigFileContainerSelectionPage("File Selection", this.selection);
		}
		this.addPage(pathPage);
		if (configPage == null) {
			configPage = new EDIMappingConfigFileConfigPage("Config", this.selection);
		}
		this.addPage(configPage);
		super.addPages();
	}

	private void doFinish(IPath containerPath, String fileName, IProgressMonitor monitor) throws CoreException {
		// create a sample file
		monitor.beginTask("Creating " + fileName, 2);
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IResource resource = root.findMember(containerPath);
		IContainer container = null;
		if (resource.exists() && resource instanceof IContainer) {
			container = (IContainer) resource;
		}
		if (container == null)
			throwCoreException("Container \"" + containerPath.toPortableString() + "\" does not exist.");
		final IFile configFile = container.getFile(new Path(fileName));
		String extFileName = fileName + ".ext";
		final IFile extFile = container.getFile(new Path(extFileName));
		try {
			// create config file
			InputStream stream = openContentStream();
			if (configFile.exists()) {
				configFile.setContents(stream, true, true, monitor);
			} else {
				configFile.create(stream, true, monitor);
			}
			stream.close();
			// create ext file:
			createExtentionFile(extFile, monitor, configPage.getEdiFilePath());
		} catch (IOException e) {
			SmooksConfigurationActivator.getDefault().log(e);
		}
		monitor.worked(1);
		monitor.setTaskName("Opening file with Smooks Editor.");
		getShell().getDisplay().asyncExec(new Runnable() {
			public void run() {
				IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
				try {
					IDE.openEditor(page, configFile);
//					page.openEditor(editorInput, SmooksMultiFormEditor.EDITOR_ID, true);
				} catch (PartInitException e) {
					SmooksConfigurationActivator.getDefault().log(e);
				}
			}
		});
		monitor.worked(1);
	}

	private InputStream openContentStream() throws IOException {
		Resource resource = new MEdiResourceFactoryImpl().createResource(null);

		DocumentRoot docRoot = MEdiFactory.eINSTANCE.createDocumentRoot();
		resource.getContents().add(docRoot);

		EdiMap map = MEdiFactory.eINSTANCE.createEdiMap();
		docRoot.setEdimap(map);

		Description description = MEdiFactory.eINSTANCE.createDescription();
		map.setDescription(description);
		initDescription(description);

		Delimiters delimiters = MEdiFactory.eINSTANCE.createDelimiters();
		map.setDelimiters(delimiters);
		initDelimiters(delimiters);

		Segments segments = MEdiFactory.eINSTANCE.createSegments();
		map.setSegments(segments);
		String se = configPage.getSegments();
		if (se != null) {
			segments.setXmltag(se);
		}
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		resource.save(output, Collections.emptyMap());
		return new ByteArrayInputStream(output.toByteArray());
	}

	private void initDelimiters(Delimiters delimiters) {
		if (configPage != null) {
			final String segment = configPage.getSegmentDelimiter();
			if(segment != null){
				delimiters.setSegment(segment);
			}
			final String field = configPage.getFieldDelimiter();
			if(field != null){
				delimiters.setField(field);
			}
			final String component = configPage.getComponentDelimiter();
			if(component != null){
				delimiters.setComponent(component);
			}
			final String subComponent = configPage.getSubComponentDelimiter();
			if(subComponent != null){
				delimiters.setSubComponent(subComponent);
			}

		}
	}

	private void initDescription(Description description) {
		if (configPage != null) {
			final String name = configPage.getName();
			if (name != null) {
				description.setName(name);
			}
			final String version = configPage.getVersion();
			if (version != null) {
				description.setVersion(version);
			}
		}
	}

	public static void createExtentionFile(IFile file, IProgressMonitor monitor, String ediPath) throws CoreException,
			IOException {
		if (monitor == null) {
			monitor = new NullProgressMonitor();
		}
		InputStream stream1 = createExtContentStream(ediPath);
		if (file.exists()) {
			file.setContents(stream1, true, true, monitor);
		} else {
			file.create(stream1, true, monitor);
		}
		stream1.close();
	}

	public static InputStream createExtContentStream(String filePath) {
		String path = filePath;
		if (path == null) {
			path = "";
		}
		String contents = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
				+ "    <smooks-graphics-ext xmlns=\"http://www.jboss.org/jbosstools/smooks/smooks-graphics-ext.xsd\">\n"
				+ "        <input type=\"EDI\">\n" + "            <param name=\"path\">" + path + "</param>\n"
				+ "        </input>\n" + "        <graph/>\n" + "    </smooks-graphics-ext>";
		return new ByteArrayInputStream(contents.getBytes());
	}

	private void throwCoreException(String message) throws CoreException {
		IStatus status = new Status(IStatus.ERROR, SmooksConfigurationActivator.PLUGIN_ID, IStatus.OK, message, null);
		throw new CoreException(status);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchWizard#init(org.eclipse.ui.IWorkbench,
	 * org.eclipse.jface.viewers.IStructuredSelection)
	 */
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.selection = selection;
	}

}
