/*******************************************************************************
 * Copyright (c) 2007-2011 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.browsersim.browser.test;

import junit.framework.TestCase;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.BrowserFunction;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.jboss.tools.vpe.browsersim.browser.AbstractWebKitBrowser;
import org.jboss.tools.vpe.browsersim.browser.WebKitBrowserFactory;

/**
 * @author Yahor Radtsevich (yradtsevich)
 */
public class WebKitTests extends TestCase {
	private static final String CUSTOM_USER_AGENT = "A Custom User-Agent";

	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}
	
	public void testWebKitSetDefaultUserAgent() {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());
		final AbstractWebKitBrowser webKitBrowser = new WebKitBrowserFactory().createBrowser(shell, SWT.NONE);
		assertNotNull(webKitBrowser);
		ExpressionsEvaluator expressionsEvaluator = new ExpressionsEvaluator(webKitBrowser);
		
		String initialUserAgent = (String) expressionsEvaluator.evaluate("navigator.userAgent");
		
		webKitBrowser.setDefaultUserAgent(CUSTOM_USER_AGENT);
		String customUserAgent = (String) expressionsEvaluator.evaluate("navigator.userAgent");
		assertEquals(CUSTOM_USER_AGENT, customUserAgent);
		
		webKitBrowser.setDefaultUserAgent(null);
		String finalUserAgent = (String) expressionsEvaluator.evaluate("navigator.userAgent");
		assertEquals(initialUserAgent, finalUserAgent);
		
		expressionsEvaluator.dispose();
		display.dispose();
	}
}

/**
 * Evaluator of JavaScript expressions. Typical usage:
 * <pre>
 * ExpressionsEvaluator expressionsEvaluator = new ExpressionsEvaluator(browser);
 * String userAgent = (String) expressionsEvaluator.evaluate("navigator.userAgent");
 * expressionsEvaluator.dispose();
 * </pre>
 * 
 * @author Yahor Radtsevich (yradtsevich)
 */
class ExpressionsEvaluator {
	private static long expressionExecutorFunctionId = 0;
	
	private final String EXPRESSION_EXECUTOR_RESULT_EXTRACTOR_FUNCTION_NAME = "__resultExtractor" + expressionExecutorFunctionId++;
	private Browser browser;
	private ResultExtractorFunction resultExtractorFunction;

	public ExpressionsEvaluator(Browser browser) {
		this.browser = browser;
		resultExtractorFunction = new ResultExtractorFunction(browser, EXPRESSION_EXECUTOR_RESULT_EXTRACTOR_FUNCTION_NAME);
	}
	
	public void dispose() {
		resultExtractorFunction.dispose();
	}
	
	public Object evaluate(String expression) {
		resultExtractorFunction.reset();
		browser.execute(EXPRESSION_EXECUTOR_RESULT_EXTRACTOR_FUNCTION_NAME + "(eval('" + expression.replace("'", "\\'") + "'))");
		return resultExtractorFunction.getResult().length > 0 ? resultExtractorFunction.getResult()[0] : null; 
	}
	
	/**
	 * @author Yahor Radtsevich (yradtsevich)
	 */
	private class ResultExtractorFunction extends BrowserFunction {
		private Object[] result;
		
		public ResultExtractorFunction(Browser browser, String name) {
			super(browser, name);
		}

		public Object function(Object[] arguments) {
			result = arguments;
			return null;
		}

		public Object[] getResult() {
			return result;
		}
		
		public void reset() {
			result = null;
		}
	}
}
