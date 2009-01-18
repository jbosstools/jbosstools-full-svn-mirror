/**
 * 
 */
package org.jboss.tools.smooks.test.xml2java.dvdstore;

import java.util.List;

import junit.framework.Assert;

import org.jboss.tools.smooks.analyzer.MappingModel;
import org.jboss.tools.smooks.analyzer.MappingResourceConfigList;
import org.jboss.tools.smooks.javabean.model.JavaBeanList;
import org.jboss.tools.smooks.test.xml2java.AbstractXML2JavaTestCase;
import org.jboss.tools.smooks.xml.model.TagList;

/**
 * @author Dart
 *
 */
public class DVDStoreTestCase extends AbstractXML2JavaTestCase{

	@Override
	protected String getSmooksConfigFilePath() {
		return "org/jboss/tools/smooks/test/xml2java/dvdstore/smooks-res.smooks";
	}

	@Override
	protected String getSmooksConfigGraphFilePath() {
		return "org/jboss/tools/smooks/test/xml2java/dvdstore/smooks-res.smooks.graph";
	}
	
	public void testGraph() throws Exception{
		MappingResourceConfigList mappingResourceConfigList = getMappingResourceConfigList();
		List<MappingModel> mappingModelList = mappingResourceConfigList.getMappingModelList();
		checkTargetConnectionCount(mappingModelList);
		
		// there are 18 connections :
		Assert.assertEquals(18, mappingModelList.size());
	}
	
	public void testTransformModel(){
		TagList source = (TagList)getSource();
		JavaBeanList targetList = (JavaBeanList)getTarget();
		// there is only one model
		Assert.assertEquals(1, source.getRootTagList().size());
		
		// check xml model value
		checkXMLNodeModelValue(source);
		
//		// there are 3 target models:
		Assert.assertEquals(3, targetList.getChildren().size());
	}

}
