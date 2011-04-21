/**
 * 
 */
package org.jboss.tools.smooks.test.xml2java;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import junit.framework.Assert;

import org.jboss.tools.smooks.analyzer.IMappingAnalyzer;
import org.jboss.tools.smooks.analyzer.ISourceModelAnalyzer;
import org.jboss.tools.smooks.analyzer.ITargetModelAnalyzer;
import org.jboss.tools.smooks.analyzer.MappingModel;
import org.jboss.tools.smooks.analyzer.MappingResourceConfigList;
import org.jboss.tools.smooks.graphical.GraphInformations;
import org.jboss.tools.smooks.javabean.analyzer.JavaBeanAnalyzer;
import org.jboss.tools.smooks.javabean.ui.AbstractJavaBeanBuilder;
import org.jboss.tools.smooks.javabean.ui.JavaBeanTargetBuilder;
import org.jboss.tools.smooks.model.DocumentRoot;
import org.jboss.tools.smooks.model.SmooksResourceListType;
import org.jboss.tools.smooks.test.AbstractModelTestCase;
import org.jboss.tools.smooks.test.java.SelectorTester;
import org.jboss.tools.smooks.test.java2java.NormalJ2JConfigFileAnalyzerTester;
import org.jboss.tools.smooks.ui.IXMLStructuredObject;
import org.jboss.tools.smooks.xml.model.AbstractXMLObject;
import org.jboss.tools.smooks.xml.model.TagList;
import org.jboss.tools.smooks.xml.model.TagObject;
import org.jboss.tools.smooks.xml.model.TagPropertyObject;
import org.jboss.tools.smooks.xml2java.analyzer.XML2JavaAnalyzer2;
import org.jboss.tools.smooks.xml2java.analyzer.XMLSourceModelAnalyzer;

/**
 * @author Dart
 * 
 */
public abstract class AbstractXML2JavaTestCase extends AbstractModelTestCase {

	protected Object source;
	protected Object target;
	protected MappingResourceConfigList mappingResourceConfigList;

	public Object getSource() {
		return source;
	}

	public void setSource(Object source) {
		this.source = source;
	}

	public Object getTarget() {
		return target;
	}

	public void setTarget(Object target) {
		this.target = target;
	}

	public MappingResourceConfigList getMappingResourceConfigList() {
		return mappingResourceConfigList;
	}

	public void setMappingResourceConfigList(
			MappingResourceConfigList mappingResourceConfigList) {
		this.mappingResourceConfigList = mappingResourceConfigList;
	}

	public void checkSelectors() {
		SelectorTester tester = new SelectorTester();
		SmooksResourceListType listType = ((DocumentRoot) smooksResource
				.getContents().get(0)).getSmooksResourceList();
//		tester.validSmooksConfigFile(listType, (IXMLStructuredObject) source,
//				(IXMLStructuredObject) ((List) target).get(0));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jboss.tools.smooks.test.AbstractModelTestCase#loadResources()
	 */
	@Override
	public void loadResources() throws RuntimeException {
		ClassLoader classLoader = NormalJ2JConfigFileAnalyzerTester.class
				.getClassLoader();
		InputStream stream1 = classLoader
				.getResourceAsStream(getSmooksConfigFilePath());
		InputStream stream2 = classLoader
				.getResourceAsStream(getSmooksConfigGraphFilePath());
		try {
			graphResource.load(stream2, Collections.EMPTY_MAP);

			graph = (GraphInformations) graphResource.getContents().get(0);

			smooksResource.load(stream1, Collections.EMPTY_MAP);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public MappingResourceConfigList analyzeSmooksConfigFile()
			throws InvocationTargetException {
		ClassLoader classLoader = AbstractXML2JavaTestCase.class
				.getClassLoader();
		SmooksResourceListType listType = ((DocumentRoot) smooksResource
				.getContents().get(0)).getSmooksResourceList();
		((AbstractJavaBeanBuilder) targetModelAnalyzer)
				.setClassLoader(classLoader);
		MappingResourceConfigList configList = connectionsAnalyzer
				.analyzeMappingSmooksModel(listType, getSource(), getTarget());
		return configList;
	}

	public Object loadSource() throws InvocationTargetException {
		SmooksResourceListType listType = ((DocumentRoot) smooksResource
				.getContents().get(0)).getSmooksResourceList();

		Object source = sourceModelAnalyzer.buildSourceInputObjects(graph,
				listType, null, null);
		return source;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	public void setUp() throws Exception {
		super.setUp();
		setSource(loadSource());
		setTarget(loadTarget());
		setMappingResourceConfigList(analyzeSmooksConfigFile());
	}

	public Object loadTarget() throws InvocationTargetException {
		ClassLoader classLoader = AbstractXML2JavaTestCase.class
				.getClassLoader();
		SmooksResourceListType listType = ((DocumentRoot) smooksResource
				.getContents().get(0)).getSmooksResourceList();
		((AbstractJavaBeanBuilder) targetModelAnalyzer)
				.setClassLoader(classLoader);
		Object target = targetModelAnalyzer.buildTargetInputObjects(graph,
				listType, null, null);
		return target;
	}

	protected abstract String getSmooksConfigFilePath();

	protected abstract String getSmooksConfigGraphFilePath();

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jboss.tools.smooks.test.AbstractModelTestCase#newConnectionModelAnalyzer
	 * ()
	 */
	@Override
	protected IMappingAnalyzer newConnectionModelAnalyzer() {
		return new XML2JavaAnalyzer2();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jboss.tools.smooks.test.AbstractModelTestCase#newSourceModelAnalyzer
	 * ()
	 */
	@Override
	protected ISourceModelAnalyzer newSourceModelAnalyzer() {
		return new XMLSourceModelAnalyzer();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jboss.tools.smooks.test.AbstractModelTestCase#newTargetModelAnalyzer
	 * ()
	 */
	@Override
	protected ITargetModelAnalyzer newTargetModelAnalyzer() {
		return new JavaBeanTargetBuilder();
	}

	protected void checkTargetConnectionCount(
			List<MappingModel> mappingModelList) throws Exception {
		// TODO new anaylzer allow connect to same target;
//		HashMap map = new HashMap();
//		for (Iterator iterator = mappingModelList.iterator(); iterator
//				.hasNext();) {
//			MappingModel mappingModel = (MappingModel) iterator.next();
//			String exsit = (String) map.get(mappingModel.getTarget());
//			if (exsit != null)
//				throw new Exception(
//						"Don't allow multiple connection have same target object");
//			map.put(mappingModel.getTarget(), "Exist");
//		}
	}

	public void checkXMLNodeModelValue(AbstractXMLObject tag) {
		Assert.assertNotNull(tag.getName());
		if (!(tag instanceof TagList))
			Assert.assertNotNull(tag.getParent());
		else
			Assert.assertNull(tag.getParent());
		if (tag instanceof TagObject) {
			List<AbstractXMLObject> children = ((TagObject) tag)
					.getXMLNodeChildren();
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
		List list = tag.getXMLNodeChildren();
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

}
