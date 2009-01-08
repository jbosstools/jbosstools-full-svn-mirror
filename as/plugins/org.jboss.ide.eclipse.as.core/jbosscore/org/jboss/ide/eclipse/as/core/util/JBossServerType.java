/******************************************************************************* 
 * Copyright (c) 2007 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 

package org.jboss.ide.eclipse.as.core.util;

import java.io.File;

public class JBossServerType {
	
	private static final String SYSTEM_JAR_NAME = "twiddle.jar";
	private String type;
	private String jbossSystemJarPath;
	private String[] versions = new String[0];
	
	private JBossServerType(String type, String jbossSystemJarPath, String[] versions) {
		this.type = type;
		this.jbossSystemJarPath = jbossSystemJarPath;
		this.versions = versions;
	}

	public static final JBossServerType AS = new JBossServerType(
			"AS",
			"bin"+File.separatorChar + SYSTEM_JAR_NAME,
			new String[]{"5.0", "4.2", "4.0", "3.2"});
	
	public static final JBossServerType EAP = new JBossServerType(
			"EAP",
			"jboss-as" + File.separatorChar + "bin"+ File.separatorChar + SYSTEM_JAR_NAME, 
			new String[]{"4.2","4.3"});
	
	public static final JBossServerType SOAP = new JBossServerType(
			"SOA-P",
			"jboss-as" + File.separatorChar + "bin"+ File.separatorChar + SYSTEM_JAR_NAME,
			new String[]{"4.3"});
	
	public static final JBossServerType UNKNOWN = new JBossServerType(
			"UNKNOWN",
			"",
			new String[]{"5.0", "4.3", "4.2", "4.0", "3.2"});

	public String toString() {
		return type;
	}
	
	public static JBossServerType getType(String name) {
		if(AS.type.equals(name)) {
			return AS;
		} else if(EAP.type.equals(name)) {
			return EAP;
		} else if(SOAP.type.equals(name)) {
			return SOAP;
		}
		throw new IllegalArgumentException("Name '" + name + "' cannot be converted to ServerType");
	}

	public String[] getVersions() {
		return versions;
	}
	
	public String getType() {
		return type;
	}
	
	public String getSystemJarPath() {
		return jbossSystemJarPath;
	}
	
	public static final JBossServerType[] KNOWN_TYPES = {AS, EAP, SOAP};
	
}