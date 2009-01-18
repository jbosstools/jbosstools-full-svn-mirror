package org.jboss.tools.smooks.test.java2java;

import org.jboss.tools.smooks.analyzer.IMappingAnalyzer;
import org.jboss.tools.smooks.analyzer.ISourceModelAnalyzer;
import org.jboss.tools.smooks.analyzer.ITargetModelAnalyzer;
import org.jboss.tools.smooks.javabean.ui.BeanPopulatorMappingAnalyzer;
import org.jboss.tools.smooks.javabean.ui.JavaBeanSourceBuilder;
import org.jboss.tools.smooks.javabean.ui.JavaBeanTargetBuilder;
import org.jboss.tools.smooks.test.AbstractModelTestCase;

public abstract class AbstractJava2JavaModelTestCase extends AbstractModelTestCase {

	public AbstractJava2JavaModelTestCase() {
		super();

	}

	public ISourceModelAnalyzer newSourceModelAnalyzer() {
		return new JavaBeanSourceBuilder();
	}

	public ITargetModelAnalyzer newTargetModelAnalyzer() {
		return new JavaBeanTargetBuilder();
	}

	public IMappingAnalyzer newConnectionModelAnalyzer() {
		return new BeanPopulatorMappingAnalyzer();
	}

}
