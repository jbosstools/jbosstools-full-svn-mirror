/**
 * 
 */
package org.jboss.tools.smooks.test.xml2java.order;

import java.util.Iterator;
import java.util.List;

import junit.framework.Assert;

import org.jboss.tools.smooks.analyzer.MappingModel;
import org.jboss.tools.smooks.analyzer.MappingResourceConfigList;
import org.jboss.tools.smooks.xml.model.TagList;
import org.jboss.tools.smooks.xml.model.TagObject;

/**
 * @author Dart
 *
 */
public class One2ManyTestCase extends ClassicX2JTestCase {

	@Override
	protected String getSmooksConfigFilePath() {
		return "org/jboss/tools/smooks/test/xml2java/order/one-many-mapping-x2j.smooks";
	}

	@Override
	public void testGraph() throws Exception {
		MappingResourceConfigList configList = this.getMappingResourceConfigList();
		List<MappingModel> mappingList = configList.getMappingModelList();
		// there are 11 connections here
		Assert.assertEquals(11, mappingList.size());
		
		this.checkTargetConnectionCount(mappingList);
		int rootSourceconnection = 0;
		TagList tagList = (TagList)getSource();
		TagObject root = tagList.getRootTagList().get(0);
		for (Iterator iterator = mappingList.iterator(); iterator.hasNext();) {
			MappingModel mappingModel = (MappingModel) iterator.next();
			if(mappingModel.getSource() == root){
				rootSourceconnection ++;
			}
		}
		Assert.assertEquals(3, rootSourceconnection);
	}

	@Override
	public void testSourceModel() {
		TagList tagList = (TagList) this.getSource();
		Assert.assertEquals(1, tagList.getRootTagList().size());
		this.checkXMLNodeModelValue(tagList.getRootTagList().get(0));
	}
}
