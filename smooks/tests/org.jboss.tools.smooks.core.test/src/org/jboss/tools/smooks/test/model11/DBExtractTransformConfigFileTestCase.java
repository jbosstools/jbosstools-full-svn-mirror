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

import java.util.Iterator;
import java.util.List;

import junit.framework.Assert;

import org.jboss.tools.smooks.model.dbrouting.Executor;
import org.jboss.tools.smooks.model.smooks.AbstractResourceConfig;
import org.jboss.tools.smooks.model.smooks.ImportType;
import org.jboss.tools.smooks.model.smooks.ParamType;
import org.jboss.tools.smooks.model.smooks.SmooksResourceListType;


/**
 * @author Dart (dpeng@redhat.com)
 *
 */
public class DBExtractTransformConfigFileTestCase extends AbstractSmooks11ModelTestCase {

	@Override
	protected String getFilePath() {
		return "org/jboss/tools/smooks/test/model/configfiles/smooks112/db-extract-transform-load.xml";
	}

	@Override
	public void testModel() {
		SmooksResourceListType resourceConfig = getSmooksResourceList11();
		
		Assert.assertNotNull(resourceConfig);
		
		Assert.assertEquals("stream.filter.type", resourceConfig.getParams().getParam().get(0).getName());
		Assert.assertEquals("SAX", ((ParamType) resourceConfig.getParams().getParam().get(0)).getStringValue());
		
		List<AbstractResourceConfig> imports = resourceConfig.getAbstractResourceConfig();
		int importCount = 0;
		int executorCount = 0;
		for (Iterator<?> iterator = imports.iterator(); iterator.hasNext();) {
			AbstractResourceConfig abstractResourceConfig = (AbstractResourceConfig) iterator.next();
			if(abstractResourceConfig instanceof ImportType){
				if(importCount <=2){
					ImportType import1 = (ImportType)abstractResourceConfig;
					if(importCount == 0){
						Assert.assertEquals("edi-orders-parser.xml", import1.getFile());
					}
					if(importCount == 1){
						Assert.assertEquals("datasources.xml", import1.getFile());
					}
					if(importCount == 2){
						Assert.assertEquals("bindings.xml", import1.getFile());
					}
					importCount++;
				}
			}
			
			if(abstractResourceConfig instanceof Executor){
				if(executorCount <= 2){
					Executor executor = (Executor)abstractResourceConfig;
					if(executorCount == 0){
						Assert.assertEquals("customer-details", executor.getExecuteOnElement());
						Assert.assertEquals("DBExtractTransformLoadDS", executor.getDatasource());
						Assert.assertEquals(true, executor.isExecuteBefore());
						Assert.assertEquals("select ORDERNUMBER from ORDERS where ORDERNUMBER = ${order.orderNum}", executor.getStatement().trim());
						Assert.assertEquals("orderExistsRS", executor.getResultSet().getName());
					}
					if(executorCount == 1){
						Assert.assertEquals("customer-details", executor.getExecuteOnElement());
						Assert.assertEquals("DBExtractTransformLoadDS", executor.getDatasource());
						Assert.assertEquals(false, executor.isExecuteBefore());
						Assert.assertEquals("INSERT INTO ORDERS VALUES(${order.orderNum}, ${order.customerUname}, ${order.status}, ${order.net}, ${order.total}, ${message.date})", executor.getStatement().trim());
						Assert.assertEquals("orderExistsRS.isEmpty()", executor.getCondition().getStringValue());
					}
					if(executorCount == 2){
						Assert.assertEquals("order-item", executor.getExecuteOnElement());
						Assert.assertEquals("DBExtractTransformLoadDS", executor.getDatasource());
						Assert.assertEquals(false, executor.isExecuteBefore());
						Assert.assertEquals("INSERT INTO ORDERITEMS VALUES (${order.orderNum}, ${orderItem.quantity}, ${orderItem.productId}, ${orderItem.title}, ${orderItem.price})", executor.getStatement().trim());
						Assert.assertEquals("orderExistsRS.isEmpty()", executor.getCondition().getStringValue());
					}
					executorCount ++;
				}
			}
		}
		
		Assert.assertEquals(3, executorCount);
		Assert.assertEquals(3, importCount);
	}

}
