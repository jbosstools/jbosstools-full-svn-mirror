package org.jboss.tools.smooks.analyzer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.resource.ResourceItemProviderAdapterFactory;
import org.jboss.tools.smooks.model.DocumentRoot;
import org.jboss.tools.smooks.model.SmooksFactory;
import org.jboss.tools.smooks.model.SmooksPackage;
import org.jboss.tools.smooks.model.SmooksResourceListType;
import org.jboss.tools.smooks.model.provider.SmooksItemProviderAdapterFactory;
import org.jboss.tools.smooks.ui.modelparser.SmooksConfigurationFileGenerateContext;

public class SmooksFileBuilder {
	Resource smooksResource;
	EditingDomain domain;

	public SmooksFileBuilder(EditingDomain domain) {
		this.domain = domain;
	}

	public Resource getSmooksResource() {
		return smooksResource;
	}

	public void setSmooksResource(Resource smooksResource) {
		this.smooksResource = smooksResource;
	}

	public InputStream generateSmooksFile(
			SmooksConfigurationFileGenerateContext context,
			IProgressMonitor monitor) throws SmooksAnalyzerException,
			IOException, CoreException {
		AnalyzerFactory factory = AnalyzerFactory.getInstance();
		IMappingAnalyzer analyzer = factory.getMappingAnalyzer(context
				.getSourceDataTypeID(), context.getTargetDataTypeID());

		if (analyzer == null) {
			throw new SmooksAnalyzerException(
					"can't find the Analyzer for sourceID : "
							+ context.getSourceDataTypeID()
							+ " and the targetID : "
							+ context.getTargetDataTypeID());
		}
		if (smooksResource == null) {
			throw new SmooksAnalyzerException("SmooksResource is NULL");
		}
		DocumentRoot documentRoot = (DocumentRoot) smooksResource.getContents()
				.get(0);
		if (documentRoot == null) {
			documentRoot = SmooksFactory.eINSTANCE.createDocumentRoot();
			smooksResource.getContents().add(documentRoot);
		}

		SmooksResourceListType listType = documentRoot.getSmooksResourceList();
		if (listType == null) {
			listType = SmooksFactory.eINSTANCE.createSmooksResourceListType();
			documentRoot.setSmooksResourceList(listType);
		}

		// init the smooksresourcelist
		initSmooksParseStyle(context, listType);
		context.setGeneratorResourceList(new ArrayList());
		context.setSmooksResourceListModel(listType);
		context.setDomain(domain);

		analyzer.analyzeMappingGraphModel(context);

		insertResoureConfig(listType, context.getGeneratorResourceList());

		List test = listType.getAbstractResourceConfig();

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		smooksResource.save(outputStream, Collections.EMPTY_MAP);
		return new ByteArrayInputStream(outputStream.toByteArray());
	}

	private EditingDomain createEditingDomain() {
		ComposedAdapterFactory adapterFactory = new ComposedAdapterFactory(
				ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
		adapterFactory
				.addAdapterFactory(new ResourceItemProviderAdapterFactory());
		adapterFactory
				.addAdapterFactory(new SmooksItemProviderAdapterFactory());

		AdapterFactoryEditingDomain editingDomain = new AdapterFactoryEditingDomain(
				adapterFactory, createCommandStack(),
				new HashMap<Resource, Boolean>());

		return editingDomain;
	}

	protected CommandStack createCommandStack() {
		return new BasicCommandStack();
	}

	protected void insertResoureConfig(SmooksResourceListType list,
			List resourceConfigList) {
		EditingDomain domain = createEditingDomain();
		int length = resourceConfigList.size();
		List kk = list.getAbstractResourceConfig();
		for (int i = length - 1; i >= 0; i--) {
			Object obj = resourceConfigList.get(i);
			Command addResourceConfigCommand = AddCommand
					.create(
							domain,
							list,SmooksPackage.eINSTANCE.getSmooksResourceListType_AbstractResourceConfig(),
							obj);
			addResourceConfigCommand.execute();
		}
	}

	/**
	 * 
	 * <code> <resource-config selector="global-parameters">
	 *  	 <param name="stream.filter.type">SAX< /param>
	 * </resource-config> </code>
	 * 
	 * @param context
	 * @param resourceList
	 */
	protected void initSmooksParseStyle(
			SmooksConfigurationFileGenerateContext context,
			SmooksResourceListType resourceList) {
		// String type = context.getSmooksType();
		// if(type == null) return;
		// ResourceConfigType config =
		// SmooksFactory.eINSTANCE.createResourceConfigType();
		// config.setSelector(SmooksConstants.GLOBAL_PARAMETERS);
		// ParamType param = SmooksFactory.eINSTANCE.createParamType();
		// param.setName(SmooksConstants.STREAM_FILTER_TYPE);
		// SmooksModelUtils.appendTextToSmooksType(param, SmooksConstants.SAX);
		//		
		// config.getParam().add(param);

		// resourceList.getAbstractResourceConfig().add(0,config);
	}
}
