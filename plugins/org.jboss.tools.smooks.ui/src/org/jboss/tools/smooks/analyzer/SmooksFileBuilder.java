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
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.MoveCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.resource.ResourceItemProviderAdapterFactory;
import org.jboss.tools.smooks.model.DocumentRoot;
import org.jboss.tools.smooks.model.ParamType;
import org.jboss.tools.smooks.model.ResourceConfigType;
import org.jboss.tools.smooks.model.SmooksFactory;
import org.jboss.tools.smooks.model.SmooksPackage;
import org.jboss.tools.smooks.model.SmooksResourceListType;
import org.jboss.tools.smooks.model.provider.SmooksItemProviderAdapterFactory;
import org.jboss.tools.smooks.model.util.SmooksModelConstants;
import org.jboss.tools.smooks.model.util.SmooksModelUtils;
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
		if(monitor == null){
			monitor = new NullProgressMonitor();
		}
		AnalyzerFactory factory = AnalyzerFactory.getInstance();
		IMappingAnalyzer analyzer = factory.getMappingAnalyzer(context
				.getSourceDataTypeID(), context.getTargetDataTypeID());

		if (smooksResource == null) {
			throw new SmooksAnalyzerException(Messages
					.getString("SmooksFileBuilder.ResourceIsNull")); //$NON-NLS-1$
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
		if (analyzer != null)
			analyzer.analyzeMappingGraphModel(context);

		insertResoureConfig(listType, context.getGeneratorResourceList());

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		smooksResource.save(outputStream, Collections.EMPTY_MAP);
		removeSavedResourceConfig(listType, context.getGeneratorResourceList());
		context.getGeneratorResourceList().clear();
		context.setGeneratorResourceList(null);
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

	private void removeSavedResourceConfig(SmooksResourceListType list,
			List resourceConfigList) {
		for (Iterator iterator = resourceConfigList.iterator(); iterator
				.hasNext();) {
			Object object = (Object) iterator.next();
			RemoveCommand
					.create(
							domain,
							list,
							SmooksPackage.eINSTANCE
									.getSmooksResourceListType_AbstractResourceConfig(),
							object).execute();
		}
	}

	/**
	 * TODO The Method should improve!!!!
	 * 
	 * @param list
	 * @param resourceConfigList
	 */
	protected void insertResoureConfig(SmooksResourceListType list,
			List resourceConfigList) {
		// EditingDomain domain = createEditingDomain();
		int length = resourceConfigList.size();
		List kk = list.getAbstractResourceConfig();
		int existingLength = kk.size();
		for (int i = length - 1; i >= 0; i--) {
			Object obj = resourceConfigList.get(i);
			Command addResourceConfigCommand = AddCommand
					.create(
							domain,
							list,
							SmooksPackage.eINSTANCE
									.getSmooksResourceListType_AbstractResourceConfig(),
							obj);
			addResourceConfigCommand.execute();
			int moveIndex = 1;
			if (existingLength < 1) {
				moveIndex = 0;
			}
			MoveCommand
					.create(
							domain,
							list,
							SmooksPackage.eINSTANCE
									.getSmooksResourceListType_AbstractResourceConfig(),
							obj, moveIndex).execute();

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
		// if (type == null)
		// return;
		// ResourceConfigType config = null;
		// if (resourceList.getAbstractResourceConfig().size() < 1) {
		// } else {
		// config = (ResourceConfigType) resourceList
		// .getAbstractResourceConfig().get(0);
		// String selector = config.getSelector();
		// if (!SmooksModelConstants.GLOBAL_PARAMETERS.equals(selector)) {
		// config = null;
		// } else {
		// if (config.getParam().isEmpty()) {
		// config = null;
		// } else {
		// ParamType param = config.getParam().get(0);
		// if (!SmooksModelConstants.STREAM_FILTER_TYPE.equals(param
		// .getName())) {
		// config = null;
		// }
		// }
		// }
		// }
		// ParamType param = null;
		// if (config == null) {
		// config = SmooksFactory.eINSTANCE.createResourceConfigType();
		// AddCommand.create(domain, resourceList, SmooksPackage.eINSTANCE.
		// getSmooksResourceListType_AbstractResourceConfig(),
		// config).execute();
		// MoveCommand.create(domain, resourceList, SmooksPackage.eINSTANCE.
		// getSmooksResourceListType_AbstractResourceConfig(), config, 0);
		// config.setSelector(SmooksModelConstants.GLOBAL_PARAMETERS);
		// param = SmooksFactory.eINSTANCE.createParamType();
		// param.setName(SmooksModelConstants.STREAM_FILTER_TYPE);
		// config.getParam().add(param);
		// }else{
		// List paramList = config.getParam();
		// for (Iterator iterator = paramList.iterator(); iterator.hasNext();) {
		// ParamType p = (ParamType) iterator.next();
		// if(SmooksModelConstants.STREAM_FILTER_TYPE.equals(p.getName())){
		// param = p;
		// break;
		// }
		// }
		// if(param == null){
		// param = SmooksFactory.eINSTANCE.createParamType();
		// param.setName(SmooksModelConstants.STREAM_FILTER_TYPE);
		// config.getParam().add(param);
		// }
		// }
		// SmooksModelUtils.cleanTextToSmooksType(param);
		// SmooksModelUtils.appendTextToSmooksType(param,
		// context.getSmooksType());
	}
}
