/*******************************************************************************
 * Copyright (c) 2012 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.cdi.seam.text.ext.test;

import java.util.ArrayList;

import org.eclipse.core.resources.IFile;
import org.eclipse.osgi.util.NLS;
import org.jboss.tools.cdi.core.test.tck.TCKTest;
import org.jboss.tools.cdi.seam.text.ext.CDISeamExtMessages;
import org.jboss.tools.cdi.seam.text.ext.hyperlink.CDISeamResourceLoadingHyperlink;
import org.jboss.tools.cdi.seam.text.ext.hyperlink.CDISeamResourceLoadingHyperlinkDetector;
import org.jboss.tools.cdi.text.ext.test.CDIHyperlinkTestUtil;
import org.jboss.tools.common.util.FileUtil;
import org.jboss.tools.cdi.text.ext.test.CDIHyperlinkTestUtil.TestHyperlink;
import org.jboss.tools.cdi.text.ext.test.CDIHyperlinkTestUtil.TestRegion;

public class CDISeamResourceLoadingHyperlinkDetectorTest extends TCKTest{
	
	public void testCDISeamResourceLoadingHyperlinkDetector_Solder30() throws Exception {
		checkFile("JavaSource/org/jboss/jsr299/tck/tests/jbt/openon/ResourceLoader30.java");
	}

	public void testCDISeamResourceLoadingHyperlinkDetector_Solder31() throws Exception {
		checkFile("JavaSource/org/jboss/jsr299/tck/tests/jbt/openon/ResourceLoader31.java");
	}
	
	private void checkFile(String fileName) throws Exception{
		IFile file = tckProject.getFile(fileName);
		String text = FileUtil.readStream(file);
		
		
		ArrayList<TestRegion> regionList = new ArrayList<TestRegion>();
		
		int injectPosition = text.indexOf("@Inject");
		if(injectPosition > 0){
			regionList.add(new TestRegion(injectPosition, 58,
					new TestHyperlink[]{new TestHyperlink(CDISeamResourceLoadingHyperlink.class, NLS.bind(CDISeamExtMessages.CDI_SEAM_RESOURCE_LOADING_HYPERLINK,
							"WEB-INF/beans.xml",
							"WebContent/WEB-INF/beans.xml"))}));
		}
		injectPosition = text.indexOf("@Inject",injectPosition+1);
		if(injectPosition > 0){
			regionList.add(new TestRegion(injectPosition, 97,
					new TestHyperlink[]{new TestHyperlink(CDISeamResourceLoadingHyperlink.class, NLS.bind(CDISeamExtMessages.CDI_SEAM_RESOURCE_LOADING_HYPERLINK,
							"org/jboss/jsr299/tck/tests/jbt/openon/test.properties",
							"JavaSource/org/jboss/jsr299/tck/tests/jbt/openon/test.properties"))}));
		}
		injectPosition = text.indexOf("@Inject",injectPosition+1);
		if(injectPosition > 0){
			regionList.add(new TestRegion(injectPosition, 86,
					new TestHyperlink[]{new TestHyperlink(CDISeamResourceLoadingHyperlink.class, NLS.bind(CDISeamExtMessages.CDI_SEAM_RESOURCE_LOADING_HYPERLINK,
							"org/jboss/jsr299/tck/tests/jbt/openon/test",
							"JavaSource/org/jboss/jsr299/tck/tests/jbt/openon/test.properties"))}));
		}
		injectPosition = text.indexOf("@Inject",injectPosition+1);
		if(injectPosition > 0){
			regionList.add(new TestRegion(injectPosition, 97,
					new TestHyperlink[]{new TestHyperlink(CDISeamResourceLoadingHyperlink.class, NLS.bind(CDISeamExtMessages.CDI_SEAM_RESOURCE_LOADING_HYPERLINK,
							"org.jboss.jsr299.tck.tests.jbt.openon.test.properties",
							"JavaSource/org/jboss/jsr299/tck/tests/jbt/openon/test.properties"))}));
		}
		injectPosition = text.indexOf("@Inject",injectPosition+1);
		if(injectPosition > 0){
			regionList.add(new TestRegion(injectPosition, 86,
					new TestHyperlink[]{new TestHyperlink(CDISeamResourceLoadingHyperlink.class, NLS.bind(CDISeamExtMessages.CDI_SEAM_RESOURCE_LOADING_HYPERLINK,
							"org.jboss.jsr299.tck.tests.jbt.openon.test",
							"JavaSource/org/jboss/jsr299/tck/tests/jbt/openon/test.properties"))}));
		}
		 
		CDIHyperlinkTestUtil.checkRegions(tckProject, fileName, regionList, new CDISeamResourceLoadingHyperlinkDetector());
	}

}
