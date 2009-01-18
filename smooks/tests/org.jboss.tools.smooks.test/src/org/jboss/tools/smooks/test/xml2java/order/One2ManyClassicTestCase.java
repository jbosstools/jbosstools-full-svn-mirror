/**
 * 
 */
package org.jboss.tools.smooks.test.xml2java.order;

import java.util.List;

import junit.framework.Assert;

import org.jboss.tools.smooks.analyzer.MappingModel;
import org.jboss.tools.smooks.analyzer.MappingResourceConfigList;
import org.jboss.tools.smooks.model.ResourceConfigType;
import org.jboss.tools.smooks.xml.model.TagList;
import org.jboss.tools.smooks.xml.model.TagObject;


/**
 * @author Dart
 *
 */
public class One2ManyClassicTestCase extends ClassicX2JTestCase {

	/* (non-Javadoc)
	 * @see org.jboss.tools.smooks.test.xml2java.AbstractXML2JavaTestCase#getSmooksConfigFilePath()
	 */
	@Override
	protected String getSmooksConfigFilePath() {
		return "org/jboss/tools/smooks/test/xml2java/order/order-01-smooks-config.smooks";
	}

	@Override
	public void testGraph() throws Exception {
		MappingResourceConfigList mappingConfigList = getMappingResourceConfigList();
		List<ResourceConfigType> renderingConfigList = mappingConfigList.getGraphRenderResourceConfigList();
		List<MappingModel> mappingModelList = mappingConfigList.getMappingModelList();
		// there are 4 resource config for rendering
		Assert.assertEquals(4, renderingConfigList.size());
		// there are 16 connections here
		Assert.assertEquals(16, mappingModelList.size());
		
		checkTargetConnectionCount(mappingModelList);
	}

	@Override
	public void testSourceModel() {
		TagList source = (TagList) getSource();
		// check model value
		checkXMLNodeModelValue(source);

		List<TagObject> tagList = source.getRootTagList();
		// there is only one tag object
		Assert.assertEquals(1, tagList.size());
	}


}
