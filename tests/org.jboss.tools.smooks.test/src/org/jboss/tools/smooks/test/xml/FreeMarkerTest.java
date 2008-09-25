/*******************************************************************************
 * Copyright (c) 2008 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.smooks.test.xml;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import junit.framework.TestCase;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.jboss.tools.smooks.java2xml.analyzer.DOM2FreeMarkerTransformor;
import org.jboss.tools.smooks.java2xml.analyzer.FreeMarkerContentReplacer;

/**
 * @author Dart Peng
 * @Date : Sep 25, 2008
 */
public class FreeMarkerTest extends TestCase {
	public void testParse() throws DocumentException, IOException {
		Reader reader = new InputStreamReader(FreeMarkerTest.class
				.getResourceAsStream("test.flt"));
		FreeMarkerContentReplacer replacer = new FreeMarkerContentReplacer();
		String contents = replacer.replaceFreeMarkerTemplate(reader);
		System.out.println(contents);

		SAXReader sax = new SAXReader();
		Document doc = sax.read(new ByteArrayInputStream(contents.getBytes()));
		DOM2FreeMarkerTransformor trans = new DOM2FreeMarkerTransformor();
		String result = trans.transformDOM(doc);
		
		System.out.println(result);
	}
}
