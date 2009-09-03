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
package org.jboss.tools.smooks.test.model11;

import java.util.List;

import junit.framework.Assert;

import org.jboss.tools.smooks.model.datasource.Direct;
import org.jboss.tools.smooks.model.smooks.AbstractResourceConfig;
import org.jboss.tools.smooks.model.smooks.SmooksResourceListType;


/**
 * @author Dart (dpeng@redhat.com)
 *
 */
public class DatasourcesConfigFileTestCase extends AbstractSmooks11ModelTestCase {

	@Override
	protected String getFilePath() {
		return "org/jboss/tools/smooks/test/model/configfiles/smooks112/datasources.xml";
	}

	@Override
	public void testModel() {
		SmooksResourceListType resourceConfig = getSmooksResourceList11();
		
		Assert.assertNotNull(resourceConfig);
		List<AbstractResourceConfig> readerList = resourceConfig.getAbstractResourceConfig();
		Assert.assertEquals(readerList.size(), 1);
		
		Direct direct = (Direct) readerList.get(0);
		
		Assert.assertEquals("$document", direct.getBindOnElement());
		Assert.assertEquals("DBExtractTransformLoadDS", direct.getDatasource());
		Assert.assertEquals("org.hsqldb.jdbcDriver", direct.getDriver());
		Assert.assertEquals("jdbc:hsqldb:hsql://localhost:9201/milyn-hsql-9201", direct.getUrl());
		Assert.assertEquals("sa", direct.getUsername());
		Assert.assertEquals("$document", direct.getBindOnElement());
		Assert.assertEquals(false, direct.isAutoCommit());
	}

}
