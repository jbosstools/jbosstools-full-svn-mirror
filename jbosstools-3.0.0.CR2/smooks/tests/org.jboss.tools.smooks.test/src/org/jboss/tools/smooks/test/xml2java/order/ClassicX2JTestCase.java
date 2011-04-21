/**
 * 
 */
package org.jboss.tools.smooks.test.xml2java.order;

import java.util.Iterator;
import java.util.List;

import junit.framework.Assert;

import org.jboss.tools.smooks.analyzer.MappingModel;
import org.jboss.tools.smooks.analyzer.MappingResourceConfigList;
import org.jboss.tools.smooks.test.xml2java.AbstractXML2JavaTestCase;
import org.jboss.tools.smooks.xml.model.AbstractXMLObject;
import org.jboss.tools.smooks.xml.model.TagList;
import org.jboss.tools.smooks.xml.model.TagObject;

/**
 * @author Dart
 * 
 */
public class ClassicX2JTestCase extends AbstractXML2JavaTestCase {
	public ClassicX2JTestCase() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.jboss.tools.smooks.test.xml2java.AbstractXML2JavaTestCase#
	 * getSmooksConfigFilePath()
	 */
	@Override
	protected String getSmooksConfigFilePath() {
		return "org/jboss/tools/smooks/test/xml2java/order/classic-x2j.smooks";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.jboss.tools.smooks.test.xml2java.AbstractXML2JavaTestCase#
	 * getSmooksConfigGraphFilePath()
	 */
	@Override
	protected String getSmooksConfigGraphFilePath() {
		return "org/jboss/tools/smooks/test/xml2java/order/classic-x2j.smooks.graph";
	}

	public void testGraph() throws Exception {
		
		checkSelectors();
		
		MappingResourceConfigList configList = getMappingResourceConfigList();
		List<MappingModel> mappingModelList = configList.getMappingModelList();
		// there are 12 connections
		Assert.assertEquals(12, mappingModelList.size());

	}

	public void testSourceModel() {
		TagList source = (TagList) getSource();
		// check model value
		checkXMLNodeModelValue(source);

		List<TagObject> tagList = source.getRootTagList();
		// there is only one tag object
		Assert.assertEquals(1, tagList.size());
		
		// check the namespace
		checkNameSpace(tagList.get(0));
	}
	
	protected void checkNameSpace(TagObject tag){
		checkTagURL(tag, new String[] { "date" }, "http://x");
		TagObject dateTag = findTag(tag, "date");
		Assert.assertEquals("http://y", dateTag.getNamespaceURL());
	}

	protected void checkTagURL(TagObject tag, String[] ignoreTagName,
			String equalsUrl) {
		for (int i = 0; i < ignoreTagName.length; i++) {
			String ignoreName = ignoreTagName[i];
			if (ignoreName.equals(tag.getName())) {
				return;
			}
		}
		Assert.assertEquals(equalsUrl, tag.getNamespaceURL());
		List<AbstractXMLObject> children = tag.getXMLNodeChildren();
		for (Iterator<AbstractXMLObject> iterator = children.iterator(); iterator
				.hasNext();) {
			AbstractXMLObject abstractXMLObject = (AbstractXMLObject) iterator
					.next();
			if (abstractXMLObject instanceof TagObject) {
				checkTagURL((TagObject) abstractXMLObject, ignoreTagName,
						equalsUrl);
			}
		}
	}
}
