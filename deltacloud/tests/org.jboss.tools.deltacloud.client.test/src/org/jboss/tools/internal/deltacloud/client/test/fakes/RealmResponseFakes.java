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

import org.jboss.tools.deltacloud.client.Realm.RealmState;

/**
 * @author André Dietisheim
 */
public class RealmResponseFakes {

	public static final RealmResponse realmResponse = new RealmResponse(
			"http://try.steamcannon.org/deltacloud/api/realms/us-east-1a",
			"us-east-1a",
			"us-east-1a",
			RealmState.AVAILABLE.toString().toLowerCase(),
			"22");
	public static final RealmResponse invalidLimitRealmResponse = new RealmResponse(
			"http://try.steamcannon.org/deltacloud/api/realms/us-east-1a",
			"us-east-1a",
			"us-east-1a",
			RealmState.AVAILABLE.toString().toLowerCase(),
			"aa");

	public static class RealmResponse {

		public RealmResponse(String url, String id, String name, String state, String limit) {
			this.url = url;
			this.id = id;
			this.name = name;
			this.state = state;
			this.limit = limit;
			this.response = getRealmResponseXML(url, id, name, state, limit);
		}

		public String url = "http://try.steamcannon.org/deltacloud/api/realms/us-east-1a";
		public String id = "us-east-1a";
		public String name = "us-east-1a";
		public String state = RealmState.AVAILABLE.toString().toLowerCase();
		public String limit = "22";
		public String response = getRealmResponseXML(url, id, name, state, limit);

		public int getIntLimit() {
			return Integer.parseInt(limit);
		}
	}

	public static class RealmsResponse {

		public static final String url1 = "http://try.steamcannon.org/deltacloud/api/realms/us-east-1a";
		public static final String id1 = "us-east-1a";
		public static final String name1 = "us-east-1a";
		public static final String state1 = RealmState.AVAILABLE.toString().toLowerCase();
		public static final String limit1 = "2";

		public static final String url2 = "http://try.steamcannon.org/deltacloud/api/realms/us-east-2a";
		public static final String id2 = "us-east-2a";
		public static final String name2 = "us-east-2a";
		public static final String state2 = RealmState.AVAILABLE.toString().toLowerCase();
		public static final String limit2 = "12";

		public static final String response =
				"<realms>"
						+ getRealmResponseXML(url1, id1, name1, state1, limit1)
						+ getRealmResponseXML(url2, id2, name2, state2, limit2)
						+ "</realms>";

	}

	private static String getRealmResponseXML(String url, String id, String name, String state, String limit) {
		return "<realm href='" + url + "' id='" + id + "'>"
				+ "<name>" + name + "</name>"
				+ "<state>" + state + "</state>"
				+ "<limit>" + limit + "</limit>"
				+ "</realm>";
	}
}
