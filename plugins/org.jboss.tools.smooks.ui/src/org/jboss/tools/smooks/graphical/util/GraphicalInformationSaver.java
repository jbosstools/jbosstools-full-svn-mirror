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
package org.jboss.tools.smooks.graphical.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.impl.XMLResourceFactoryImpl;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.jboss.tools.smooks.graphical.GraphInformations;
import org.jboss.tools.smooks.graphical.GraphicalFactory;
import org.jboss.tools.smooks.graphical.MappingDataType;
import org.jboss.tools.smooks.graphical.Param;
import org.jboss.tools.smooks.graphical.Params;
import org.jboss.tools.smooks.ui.modelparser.SmooksConfigurationFileGenerateContext;

/**
 * @author Dart Peng
 * @Date Aug 25, 2008
 */
public class GraphicalInformationSaver {
	private IEditorInput input;

	protected Resource graphicalFileResource;

	private IFile file = null;

	/**
	 * Create the instance during init step
	 * 
	 * @param input
	 *            EditorInput of Smooks graphical editor
	 */
	public GraphicalInformationSaver(IEditorInput input) {
		if (input instanceof IFileEditorInput) {
			IFile file = ((IFileEditorInput) input).getFile();
			IContainer container = file.getParent();
			String fileName = file.getName();
			if (fileName.endsWith(".smooks")) {
				String gfileName = fileName + ".graph";
				if (container != null) {
					IFile gfile = container.getFile(new Path(gfileName));
					if (!gfile.exists()) {
						try {
							gfile.create(
									new ByteArrayInputStream("".getBytes()),
									true, null);
						} catch (CoreException e) {
							e.printStackTrace();
						}
					}
					setFile(gfile);
					String osString = gfile.getLocation().toOSString();
					graphicalFileResource = new XMLResourceFactoryImpl()
							.createResource(URI.createFileURI(osString));
				}
			}
		}
	}

	public GraphicalInformationSaver(IFile file) {
		IContainer container = file.getParent();
		String fileName = file.getName();
		if (fileName.endsWith(".smooks")) {
			String gfileName = fileName + ".graph";
			if (container != null) {
				IFile gfile = container.getFile(new Path(gfileName));
				if (!gfile.exists()) {
					try {
						gfile.create(new ByteArrayInputStream("".getBytes()),
								true, null);
					} catch (CoreException e) {
						e.printStackTrace();
					}
				}
				setFile(gfile);
				String osString = gfile.getLocation().toOSString();
				graphicalFileResource = new XMLResourceFactoryImpl()
						.createResource(URI.createFileURI(osString));
			}
		}
	}

	public GraphInformations doLoad() throws IOException {
		// throw new IOException("can't find the file");
		if (graphicalFileResource != null) {
			graphicalFileResource.load(Collections.EMPTY_MAP);
			GraphInformations graph = (GraphInformations) graphicalFileResource
					.getContents().get(0);
			return graph;
		}
		return null;
	}

	public void doSave(IProgressMonitor monitor, String sourceid,
			String targetid) throws IOException, CoreException {
		this.doSave(monitor, sourceid,targetid,null);
	}

	public void doSave(IProgressMonitor monitor, String sourceid,
			String targetid, Properties properties) throws IOException,
			CoreException {
		GraphInformations graph = null;
		if (graphicalFileResource != null) {
			if (graphicalFileResource.getContents().isEmpty()) {
				graph = GraphicalFactory.eINSTANCE.createGraphInformations();
				graphicalFileResource.getContents().add(graph);
			} else {
				graph = (GraphInformations) graphicalFileResource.getContents()
						.get(0);
			}
			if (graph != null) {
				initMappingTypes(graph, sourceid, targetid);
				
				Params params = graph.getParams();
				if(params == null){
					params = GraphicalFactory.eINSTANCE.createParams();
					graph.setParams(params);
				}
				initParams(params, properties);
			}
			graphicalFileResource.save(Collections.EMPTY_MAP);
			if (this.file != null)
				file.refreshLocal(IResource.DEPTH_ONE, monitor);
		}

	}

	public void doSave(IProgressMonitor monitor,
			SmooksConfigurationFileGenerateContext context) throws IOException,
			CoreException {
		String sourceID = context.getSourceDataTypeID();
		String targetID = context.getTargetDataTypeID();
		Properties properties = context.getProperties();
		this.doSave(monitor, sourceID,targetID,properties);
	}

	private void initParams(Params params, Properties properties) {
		if(properties == null) return;
		Enumeration<Object> keys = properties.keys();
		while (keys.hasMoreElements()) {
			String key = (String) keys.nextElement();
			List list = params.getParam();
			Param param = null;
			for (Iterator iterator = list.iterator(); iterator.hasNext();) {
				Param para = (Param) iterator.next();
				if(para.getName().equals(key)){
					param = para;
					break;
				}
			}
			if(param == null){
				param = GraphicalFactory.eINSTANCE.createParam();
				params.getParam().add(param);
				param.setName(key);
			}
			param.setValue(properties.getProperty(key));
			
		}
	}

	protected void initMappingTypes(GraphInformations infor, String sourceID,
			String targetID) {
		MappingDataType mapping = infor.getMappingType();
		if (mapping == null) {
			mapping = GraphicalFactory.eINSTANCE.createMappingDataType();
			infor.setMappingType(mapping);
		}

		mapping.setSourceTypeID(sourceID);
		mapping.setTargetTypeID(targetID);
	}

	public IFile getFile() {
		return file;
	}

	public void setFile(IFile file) {
		this.file = file;
	}

}
