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
public class ImageResponseFakes {

	public static class ImageResponse {
		public static final String url = "http://try.steamcannon.org/deltacloud/api/images/ami-16a3577f";
		public static final String id = "ami-16a3577f";
		public static final String name = "sles-10-sp3-v1.00.i386";
		public static final String ownerId = "013907871322";
		public static final String description = "SUSE Linux Enterprise Server 10 Service Pack 3 for x86 (v1.00)";
		public static final String architecture = "i386";

		public static final String response = getImageResponseXML(url, id, name, ownerId, description, architecture);
	}

	public static class ImagesResponse {

		public static final String url1 = "http://try.steamcannon.org/deltacloud/api/images/ami-16a3577f";
		public static final String id1 = "ami-16a3577f";
		public static final String name1 = "sles-10-sp3-v1.00.i386";
		public static final String ownerId1 = "013907871322";
		public static final String description1 = "SUSE Linux Enterprise Server 10 Service Pack 3 for x86 (v1.00)";
		public static final String architecture1 = "i386";

		public static final String url2 = "http://try.steamcannon.org/deltacloud/api/images/ami-16a3578f";
		public static final String id2 = "ami-16a3578f";
		public static final String name2 = "sles-10-sp3-v2.00.i686";
		public static final String ownerId2 = "013907871422";
		public static final String description2 = "SUSE Linux Enterprise Server 10 Service Pack 3 for x86 (v2.00)";
		public static final String architecture2 = "i686";

		public static final String response =
				"<images>"
						+ getImageResponseXML(url1, id1, name1, ownerId1, description1, architecture1)
						+ getImageResponseXML(url2, id2, name2, ownerId2, description2, architecture2)
						+ "</images>";

	}

	private static String getImageResponseXML(String url, String id, String name, String ownerId,
			String description, String architecture) {
		return "<image href='" + url + "' id='" + id + "'>"
				+ "<name>" + name + "</name>"
				+ "<owner_id>" + ownerId + "</owner_id>"
				+ "<description>" + description + "</description>"
				+ "<architecture>" + architecture + "</architecture>"
				+ "<state></state>"
				+ "</image>";
	}
}
