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

import org.jboss.tools.smooks.model.edi.EDIReader;
import org.jboss.tools.smooks.model.javabean.BindingsType;
import org.jboss.tools.smooks.model.javabean.ValueType;
import org.jboss.tools.smooks.model.javabean.WiringType;
import org.jboss.tools.smooks.model.smooks.AbstractReader;
import org.jboss.tools.smooks.model.smooks.AbstractResourceConfig;
import org.jboss.tools.smooks.model.smooks.SmooksResourceListType;


/**
 * @author Dart (dpeng@redhat.com)
 *
 */
public class EDI2JavaConfigFileTestCase extends AbstractSmooks11ModelTestCase {

	@Override
	protected String getFilePath() {
		return "org/jboss/tools/smooks/test/model/configfiles/smooks112/edi-to-java.xml";
	}

	@Override
	public void testModel() {
		
		SmooksResourceListType resourceConfig = getSmooksResourceList11();
		
		Assert.assertNotNull(resourceConfig);
		
		List<AbstractReader> readerList = resourceConfig.getAbstractReader();
		Assert.assertEquals(readerList.size(), 1);
		
		EDIReader reader = (EDIReader) readerList.get(0);
		
		Assert.assertEquals("/example/edi-to-xml-order-mapping.xml", reader.getMappingModel());
		
		List<AbstractResourceConfig> resourceConfigList = resourceConfig.getAbstractResourceConfig();
		
		
		Assert.assertEquals(5, resourceConfigList.size());
		
		BindingsType bindings1 = (BindingsType) resourceConfigList.get(0);
		Assert.assertEquals("order", bindings1.getBeanId());
		Assert.assertEquals("example.model.Order", bindings1.getClass_());
		Assert.assertEquals("order", bindings1.getCreateOnElement());
		
		Assert.assertEquals(2, bindings1.getWiring().size());
		
		WiringType jb1Wiring = bindings1.getWiring().get(0);
		Assert.assertEquals("header", jb1Wiring.getProperty().trim());
		Assert.assertEquals("header", jb1Wiring.getBeanIdRef());
		
		WiringType jb1Wiring2 = bindings1.getWiring().get(1);
		Assert.assertEquals("orderItems", jb1Wiring2.getProperty().trim());
		Assert.assertEquals("orderItemList", jb1Wiring2.getBeanIdRef());
		
		
		BindingsType bindings2 = (BindingsType) resourceConfigList.get(1);
		Assert.assertEquals("header", bindings2.getBeanId().trim());
		Assert.assertEquals("example.model.Header", bindings2.getClass_().trim());
		Assert.assertEquals("order", bindings2.getCreateOnElement().trim());
		
		WiringType jb2Wiring = bindings2.getWiring().get(0);
		Assert.assertEquals("customer", jb2Wiring.getProperty().trim());
		Assert.assertEquals("customer", jb2Wiring.getBeanIdRef());
		
		Assert.assertEquals(6, bindings2.getValue().size());

		ValueType jb2Value1 = bindings2.getValue().get(0);
		Assert.assertEquals("orderId", jb2Value1.getProperty().trim());
		Assert.assertEquals("header/order-id", jb2Value1.getData().trim());
		
		ValueType jb2Value2 = bindings2.getValue().get(1);
		Assert.assertEquals("orderStatus", jb2Value2.getProperty().trim());
		Assert.assertEquals("header/status-code", jb2Value2.getData().trim());
		Assert.assertEquals("Long", jb2Value2.getDecoder().trim());
		
		
		ValueType jb2Value3 = bindings2.getValue().get(2);
		Assert.assertEquals("netAmount", jb2Value3.getProperty().trim());
		Assert.assertEquals("header/net-amount", jb2Value3.getData().trim());
		Assert.assertEquals("BigDecimal", jb2Value3.getDecoder().trim());
		
		
		ValueType jb2Value4 = bindings2.getValue().get(3);
		Assert.assertEquals("totalAmount", jb2Value4.getProperty().trim());
		Assert.assertEquals("header/total-amount", jb2Value4.getData().trim());
		Assert.assertEquals("BigDecimal", jb2Value4.getDecoder().trim());
		
		
		ValueType jb2Value5 = bindings2.getValue().get(4);
		Assert.assertEquals("tax", jb2Value5.getProperty().trim());
		Assert.assertEquals("header/tax", jb2Value5.getData().trim());
		Assert.assertEquals("BigDecimal", jb2Value5.getDecoder().trim());
		
		
		ValueType jb2Value6 = bindings2.getValue().get(5);
		Assert.assertEquals("date", jb2Value6.getProperty().trim());
		Assert.assertEquals("header/date", jb2Value6.getData().trim());
		Assert.assertEquals("Date", jb2Value6.getDecoder().trim());
		Assert.assertEquals("format", jb2Value6.getDecodeParam().get(0).getName());
		Assert.assertEquals("EEE MMM dd HH:mm:ss z yyyy", jb2Value6.getDecodeParam().get(0).getValue());
		
		
		BindingsType bindings3 = (BindingsType) resourceConfigList.get(2);
		Assert.assertEquals("customer", bindings3.getBeanId().trim());
		Assert.assertEquals("example.model.Customer", bindings3.getClass_().trim());
		Assert.assertEquals("customer-details", bindings3.getCreateOnElement().trim());
		
		Assert.assertEquals(4, bindings3.getValue().size());
		
		ValueType jb3Value1 = bindings3.getValue().get(0);
		Assert.assertEquals("userName", jb3Value1.getProperty().trim());
		Assert.assertEquals("customer-details/username", jb3Value1.getData().trim());
		
		ValueType jb3Value2 = bindings3.getValue().get(1);
		Assert.assertEquals("firstName", jb3Value2.getProperty().trim());
		Assert.assertEquals("customer-details/name/firstname", jb3Value2.getData().trim());
		
		ValueType jb3Value3 = bindings3.getValue().get(2);
		Assert.assertEquals("lastName", jb3Value3.getProperty().trim());
		Assert.assertEquals("customer-details/name/lastname", jb3Value3.getData().trim());
		
		ValueType jb3Value4 = bindings3.getValue().get(3);
		Assert.assertEquals("state", jb3Value4.getProperty().trim());
		Assert.assertEquals("customer-details/state", jb3Value4.getData().trim());
		
		BindingsType bindings5 = (BindingsType) resourceConfigList.get(3);
		Assert.assertEquals("orderItemList", bindings5.getBeanId().trim());
		Assert.assertEquals("java.util.ArrayList", bindings5.getClass_().trim());
		Assert.assertEquals("order", bindings5.getCreateOnElement().trim());
		
		Assert.assertEquals("orderItem", bindings5.getWiring().get(0).getBeanIdRef());
		
		BindingsType bindings4 = (BindingsType) resourceConfigList.get(4);
		Assert.assertEquals("orderItem", bindings4.getBeanId().trim());
		Assert.assertEquals("example.model.OrderItem", bindings4.getClass_().trim());
		Assert.assertEquals("order-item", bindings4.getCreateOnElement().trim());
		
		Assert.assertEquals(4, bindings4.getValue().size());
		
		ValueType jb4Value1 = bindings4.getValue().get(0);
		Assert.assertEquals("quantity", jb4Value1.getProperty().trim());
		Assert.assertEquals("order-item/quantity", jb4Value1.getData().trim());
		Assert.assertEquals("Integer", jb4Value1.getDecoder());
		
		ValueType jb4Value2 = bindings4.getValue().get(1);
		Assert.assertEquals("productId", jb4Value2.getProperty().trim());
		Assert.assertEquals("order-item/product-id", jb4Value2.getData().trim());
		Assert.assertEquals("String", jb4Value2.getDecoder());
		
		ValueType jb4Value3 = bindings4.getValue().get(2);
		Assert.assertEquals("price", jb4Value3.getProperty().trim());
		Assert.assertEquals("order-item/price", jb4Value3.getData().trim());
		Assert.assertEquals("BigDecimal", jb4Value3.getDecoder());
		
		ValueType jb4Value4 = bindings4.getValue().get(3);
		Assert.assertEquals("title", jb4Value4.getProperty().trim());
		Assert.assertEquals("order-item/title", jb4Value4.getData().trim());
		
		
		
	}

}
