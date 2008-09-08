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
import org.milyn.xsd.smooks.ParamType;
import org.milyn.xsd.smooks.ResourceConfigType;
import org.milyn.xsd.smooks.SmooksFactory;
import org.milyn.xsd.smooks.SmooksResourceListType;
import org.milyn.xsd.smooks.util.SmooksConstants;
import org.milyn.xsd.smooks.util.SmooksModelUtils;
import org.milyn.xsd.smooks.util.SmooksResourceFactoryImpl;

public class SmooksFileBuilder {

	public InputStream generateSmooksFile(
			SmooksConfigurationFileGenerateContext context,
			IProgressMonitor monitor) throws SmooksAnalyzerException,
			IOException, CoreException {
		AnalyzerFactory factory = AnalyzerFactory.getInstance();
		IMappingAnalyzer analyzer = factory.getMappingAnalyzer(context.getSourceDataTypeID(),
				context.getTargetDataTypeID());

		if (analyzer == null)
			throw new SmooksAnalyzerException(
					"can't find the Analyzer for sourceID : "
							+ context.getSourceDataTypeID()
							+ " and the targetID : "
							+ context.getTargetDataTypeID());

		SmooksResourceListType listType = SmooksFactory.eINSTANCE
				.createSmooksResourceListType();
		context.setSmooksResourceListModel(listType);
		analyzer.analyzeMappingGraphModel(context);

		// init the smooksresourcelist
		initSmooksParseStyle(context, listType);

		// serialize model to stream
		Resource resource = new SmooksResourceFactoryImpl()
				.createResource(null);
		DocumentRoot dr = SmooksFactory.eINSTANCE.createDocumentRoot();

		// add all model of the config file
		dr.setSmooksResourceList(listType);
		resource.getContents().add(dr);

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		resource.save(outputStream, Collections.EMPTY_MAP);
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
