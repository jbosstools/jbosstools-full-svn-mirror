/**
 * 
 */
package org.jboss.tools.smooks.test.xml2java.order;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import junit.framework.Assert;

import org.jboss.tools.smooks.analyzer.MappingModel;
import org.jboss.tools.smooks.analyzer.MappingResourceConfigList;
import org.jboss.tools.smooks.test.xml2java.AbstractXML2JavaTestCase;
import org.jboss.tools.smooks.xml.model.AbstractXMLObject;
import org.jboss.tools.smooks.xml.model.TagList;
import org.jboss.tools.smooks.xml.model.TagObject;
import org.jboss.tools.smooks.xml.model.TagPropertyObject;

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
		MappingResourceConfigList configList = getMappingResourceConfigList();
		List<MappingModel> mappingModelList = configList.getMappingModelList();
		// there are 9 connections
		Assert.assertEquals(9, mappingModelList.size());

		// check the target connection cout
		checkTargetConnectionCount(mappingModelList);
	}

	protected void checkTargetConnectionCount(
			List<MappingModel> mappingModelList) throws Exception {
		HashMap map = new HashMap();
		for (Iterator iterator = mappingModelList.iterator(); iterator
				.hasNext();) {
			MappingModel mappingModel = (MappingModel) iterator.next();
			String exsit = (String) map.get(mappingModel.getTarget());
			if (exsit != null)
				throw new Exception(
						"Don't allow multiple connection have same target object");
			map.put(mappingModel.getTarget(), "Exist");
		}
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

	public void checkXMLNodeModelValue(AbstractXMLObject tag) {
		Assert.assertNotNull(tag.getName());
		if (!(tag instanceof TagList))
			Assert.assertNotNull(tag.getParent());
		else
			Assert.assertNull(tag.getParent());
		if (tag instanceof TagObject) {
			List<AbstractXMLObject> children = ((TagObject) tag).getChildren();
			for (Iterator iterator = children.iterator(); iterator.hasNext();) {
				AbstractXMLObject abstractXMLObject = (AbstractXMLObject) iterator
						.next();
				checkXMLNodeModelValue(abstractXMLObject);
			}

			List<TagPropertyObject> properties = ((TagObject) tag)
					.getProperties();
			for (Iterator iterator = properties.iterator(); iterator.hasNext();) {
				TagPropertyObject tagPropertyObject = (TagPropertyObject) iterator
						.next();
				checkXMLNodeModelValue(tagPropertyObject);
			}
		}

		if (tag instanceof TagList) {
			List<TagObject> list = ((TagList) tag).getRootTagList();
			for (Iterator iterator = list.iterator(); iterator.hasNext();) {
				TagObject tagObject = (TagObject) iterator.next();
				checkXMLNodeModelValue(tagObject);
			}
		}

	}

	protected TagObject findTag(TagObject tag, String name) {
		if (name.equalsIgnoreCase(tag.getName())) {
			return tag;
		}
		List list = tag.getChildren();
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			Object object = (Object) iterator.next();
			if (object instanceof TagObject) {
				TagObject child = findTag((TagObject) object, name);
				if (child != null)
					return child;
			}
		}
		return null;
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
		List<AbstractXMLObject> children = tag.getChildren();
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
