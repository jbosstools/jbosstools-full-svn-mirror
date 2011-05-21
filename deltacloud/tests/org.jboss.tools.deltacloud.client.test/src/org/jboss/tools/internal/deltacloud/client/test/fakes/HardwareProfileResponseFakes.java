/*******************************************************************************
 * Copyright (c) 2010 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.internal.deltacloud.client.test.fakes;

import org.jboss.tools.deltacloud.client.Property;
import org.jboss.tools.deltacloud.client.Property.UNIT;

/**
 * @author André Dietisheim
 */
public class HardwareProfileResponseFakes {

	public static class HardwareProfile1Response {
		public static final String id = "m1-small";
		public static final String propMemKind = Property.Kind.FIXED.name().toLowerCase();
		public static final String propMemUnit = UNIT.MB.name();
		public static final String propMemValue = "1740.8";
		public static final String propStorageKind = Property.Kind.FIXED.name().toLowerCase();
		public static final String propStorageUnit = UNIT.GB.name();
		public static final String propStorageValue = "160";
		public static final String propCPUKind = Property.Kind.FIXED.name().toLowerCase();
		public static final String propCPUUnit = UNIT.COUNT.name().toLowerCase();
		public static final String propCPUValue = "1";
		public static final String propArchKind = Property.Kind.FIXED.name().toLowerCase();
		public static final String propArchUnit = UNIT.LABEL.name().toLowerCase();
		public static final String propArchValue = "i386";

		public static final String response = getHardwareProfileResponseXML(
				id,
				new String[] {
						getFixedPropertyXML(Property.Names.MEMORY.name().toLowerCase(), propMemUnit, propMemValue),
						getFixedPropertyXML(Property.Names.STORAGE.name().toLowerCase(), propStorageUnit,
								propStorageValue),
						getFixedPropertyXML(Property.Names.CPU.name().toLowerCase(), propCPUUnit, propCPUValue),
						getFixedPropertyXML(Property.Names.ARCHITECTURE.name().toLowerCase(), propArchUnit,
								propArchValue)
										});
	}

	public static class HardwareProfile2Response {
		public static final String id = "m1-large";
		public static final String propMemKind = Property.Kind.RANGE.name().toLowerCase();
		public static final String propMemUnit = UNIT.MB.name();
		public static final String propMemValue = "10240";
		public static final String propMemRangeFirst = "7680.0";
		public static final String propMemRangeLast = "15360";
		public static final String propStorageKind = Property.Kind.ENUM.name().toLowerCase();
		public static final String propStorageUnit = UNIT.GB.name();
		public static final String propStorageValue = "160";
		public static final String propStorageEnum1 = "850";
		public static final String propStorageEnum2 = "1024";
		public static final String propCPUKind = Property.Kind.FIXED.name().toLowerCase();
		public static final String propCPUUnit = UNIT.COUNT.name().toLowerCase();
		public static final String propCPUValue = "2";
		public static final String propArchKind = Property.Kind.FIXED.name().toLowerCase();
		public static final String propArchUnit = UNIT.LABEL.name().toLowerCase();
		public static final String propArchValue = "x86_64";

		public static final String response = getHardwareProfileResponseXML(
				id,
				new String[] {
						getRangePropertyXML(Property.Names.MEMORY.name().toLowerCase(), propMemUnit, propMemValue,
								propMemRangeFirst, propMemRangeLast),
						getEnumPropertyXML(Property.Names.STORAGE.name().toLowerCase(), propStorageUnit,
								propStorageValue, propStorageEnum1, propStorageEnum2),
						getFixedPropertyXML(Property.Names.CPU.name().toLowerCase(), propCPUUnit, propCPUValue),
						getFixedPropertyXML(Property.Names.ARCHITECTURE.name().toLowerCase(), propArchUnit,
								propArchValue)
								});
	}

	public static class HardwareProfilesResponse {

		public static final String response = 
			"<hardware_profiles>"
				+ HardwareProfile1Response.response
				+ HardwareProfile2Response.response
			+"</hardware_profiles>";
	}

	private static final String getHardwareProfileResponseXML(String id, String[] properties) {
		StringBuilder builder = new StringBuilder();
		for (String propertyString : properties) {
			builder.append(propertyString);
		}
		return getHardwareProfileResponseXML(id, builder.toString());
	}

	private static final String getHardwareProfileResponseXML(String id, String properties) {
		return new StringBuilder()
				.append("<hardware_profile href=\"fakeUrl\" id=\"").append(id).append("\">")
				.append("<name>fakeName</name>")
				.append(properties)
				.append("</hardware_profile>")
				.toString();
	}

	private static String getFixedPropertyXML(String name, String unit, String value) {
		return getPropertyXML(name, "fixed", unit, value)
				+ getClodingPropertyTag();
	}

	private static String getRangePropertyXML(String name, String unit, String value, String first, String last) {
		return getPropertyXML(name, "range", unit, value)
				+ "<range first='" + first + "' last='" + last + "'/>"
				+ getClodingPropertyTag();
	}

	private static String getEnumPropertyXML(String name, String unit, String value, String... enumValues) {
		StringBuilder builder = new StringBuilder(getPropertyXML(name, "enum", unit, value));
		builder.append("<enum>");
		for (String enumValue : enumValues) {
			builder.append("<entry value='").append(enumValue).append("' />");
		}
		builder.append("</enum>");
		builder.append(getClodingPropertyTag());
		return builder.toString();
	}

	private static String getPropertyXML(String name, String kind, String unit, String value) {
		return "<property kind=\"" + kind + "\" name=\"" + name + "\" unit=\"" + unit + "\" value=\"" + value + "\">";
	}

	private static String getClodingPropertyTag() {
		return "</property>";

	}

}
