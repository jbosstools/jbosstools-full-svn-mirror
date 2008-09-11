package org.jboss.tools.smooks.analyzer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.resource.Resource;
import org.jboss.tools.smooks.ui.modelparser.SmooksConfigurationFileGenerateContext;
import org.milyn.xsd.smooks.DocumentRoot;
import org.milyn.xsd.smooks.SmooksFactory;
import org.milyn.xsd.smooks.SmooksResourceListType;
import org.milyn.xsd.smooks.util.SmooksResourceFactoryImpl;

public class SmooksFileBuilder {
	Resource smooksResource;

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
		if(smooksResource == null) {
			throw new SmooksAnalyzerException("SmooksResource is NULL");
		}
		DocumentRoot documentRoot = (DocumentRoot)smooksResource.getContents().get(0);
		if(documentRoot == null){
			documentRoot = SmooksFactory.eINSTANCE.createDocumentRoot();
			smooksResource.getContents().add(documentRoot);
		}
		
		SmooksResourceListType listType = documentRoot.getSmooksResourceList();
		if(listType == null){
			listType = SmooksFactory.eINSTANCE.createSmooksResourceListType();
			documentRoot.setSmooksResourceList(listType);
		}

		// init the smooksresourcelist
		initSmooksParseStyle(context, listType);

		context.setSmooksResourceListModel(listType);
		analyzer.analyzeMappingGraphModel(context);

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		smooksResource.save(outputStream, Collections.EMPTY_MAP);
		return new ByteArrayInputStream(outputStream.toByteArray());
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
