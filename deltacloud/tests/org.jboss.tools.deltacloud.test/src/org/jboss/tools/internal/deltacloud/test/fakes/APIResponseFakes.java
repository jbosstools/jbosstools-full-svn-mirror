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
package org.jboss.tools.internal.deltacloud.test.fakes;

/**
 * @author André Dietisheim
 */
public class APIResponseFakes {

	public static class APIResponse {
		public static final String url = "http://localhost:3001/api/keys/test1292840175447";
		public static final String driver = "ec2";

		public static final String apiResponse = getApiResponseXML(url, driver);
		public static final String invalidDriverApiResponse = getApiResponseXML(url, "foo");
	}

	private static final String getApiResponseXML(String url, String driver) {
		return "<api driver='" + driver + "' version='0.1'>"
				+ "  <link href='" + url + "realms' rel='realms'>"
				+ "  </link>"
				+ "  <link href='" + url + "images' rel='images'>"
				+ "    <feature name='owner_id'></feature>"
				+ "  </link>"
				+ "  <link href='" + url + "instance_states' rel='instance_states'>"
				+ "  </link>"
				+ "  <link href='" + url + "instances' rel='instances'>"
				+ "    <feature name='user_data'></feature>"
				+ "    <feature name='authentication_key'></feature>"
				+ "    <feature name='public_ip'></feature>"
				+ "    <feature name='security_group'></feature>"
				+ "  </link>"
				+ "  <link href='" + url + "hardware_profiles' rel='hardware_profiles'>"
				+ "  </link>"
				+ "  <link href='" + url + "storage_snapshots' rel='storage_snapshots'>"
				+ "  </link>"
				+ "  <link href='" + url + "storage_volumes' rel='storage_volumes'>"
				+ "  </link>"
				+ "  <link href='" + url + "keys' rel='keys'>"
				+ "  </link>"
				+ "  <link href='" + url + "buckets' rel='buckets'>"
				+ "    <feature name='bucket_location'></feature>"
				+ "  </link>"
				+ "</api>";

	}

}
