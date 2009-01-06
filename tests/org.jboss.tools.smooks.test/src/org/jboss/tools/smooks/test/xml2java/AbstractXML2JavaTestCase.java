/**
 * 
 */
package org.jboss.tools.smooks.test.xml2java;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;

import org.jboss.tools.smooks.analyzer.IMappingAnalyzer;
import org.jboss.tools.smooks.analyzer.ISourceModelAnalyzer;
import org.jboss.tools.smooks.analyzer.ITargetModelAnalyzer;
import org.jboss.tools.smooks.analyzer.MappingResourceConfigList;
import org.jboss.tools.smooks.graphical.GraphInformations;
import org.jboss.tools.smooks.javabean.analyzer.JavaBeanAnalyzer;
import org.jboss.tools.smooks.model.DocumentRoot;
import org.jboss.tools.smooks.model.SmooksResourceListType;
import org.jboss.tools.smooks.test.AbstractModelTestCase;
import org.jboss.tools.smooks.test.java2java.NormalJ2JConfigFileAnalyzerTester;
import org.jboss.tools.smooks.xml2java.analyzer.XML2JavaAnalyzer;
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
		((JavaBeanAnalyzer) targetModelAnalyzer)
				.setCurrentClassLoader(classLoader);
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
	 * @see junit.framework.TestCase#setUp()
	 */
	public void setUp() throws Exception{
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
		((JavaBeanAnalyzer) targetModelAnalyzer)
				.setCurrentClassLoader(classLoader);
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
		return new XML2JavaAnalyzer();
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
		return new JavaBeanAnalyzer();
	}

}
