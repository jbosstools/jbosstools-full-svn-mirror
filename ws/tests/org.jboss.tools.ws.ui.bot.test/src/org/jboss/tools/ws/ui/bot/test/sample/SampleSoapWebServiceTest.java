/*******************************************************************************
 * Copyright (c) 2010 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.ws.ui.bot.test.sample;

import org.eclipse.core.resources.IFile;
import org.junit.Test;

public class SampleSoapWebServiceTest extends SampleWSBase {

    @Override
    protected String getWsProjectName() {
        return "SampleSOAPWS";
    }

    @Test
    public void testSampleSoapWS() {
    	IFile dd = getDD(getWsProjectName());
        if (!dd.exists()) {
            createDD(getWsProjectName());
        }
        assertTrue(dd.exists());
        createSampleSOAPWS(getWsProjectName(), "HelloService", "sample", "SampleService");
        checkSOAPService(getWsProjectName(), "HelloService", "sample", "SampleService", "You");

        createSampleSOAPWS(getWsProjectName(), "GreetService", "greeter", "Greeter");
        checkSOAPService(getWsProjectName(), "GreetService", "greeter", "Greeter", "Tester");
    }
    
    @Test
    public void testSimpleSoapWS() {
    	
    }

}
