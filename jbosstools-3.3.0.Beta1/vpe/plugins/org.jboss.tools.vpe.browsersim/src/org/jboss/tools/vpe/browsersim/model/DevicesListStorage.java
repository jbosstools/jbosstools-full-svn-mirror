/*******************************************************************************
 * Copyright (c) 2007-2011 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.browsersim.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jboss.tools.vpe.browsersim.util.ResourcesUtil;

/**
 * @author Yahor Radtsevich (yradtsevich)
 */
public class DevicesListStorage {
	
	private static final String DEFAULT_PREFERENCES_RESOURCE = "config/devices.cfg";
	private static final String USER_PREFERENCES_FOLDER = "org.jboss.tools.vpe.browsersim";
	private static final String USER_PREFERENCES_FILE = "devices.cfg";
	private static final int CURRENT_CONFIG_VERSION = 3;

	public static void saveUserDefinedDevicesList(DevicesList devicesList) {
		File configFolder = new File(USER_PREFERENCES_FOLDER);
		configFolder.mkdir();
		File configFile = new File(configFolder, USER_PREFERENCES_FILE);
		
		try {
			saveDevicesList(devicesList, configFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static DevicesList loadUserDefinedDevicesList() {
		File customConfigFile = new File(USER_PREFERENCES_FOLDER + '/' + USER_PREFERENCES_FILE);
		DevicesList devicesList = null;
		if (customConfigFile.exists()) {
			try {
				devicesList = loadDevicesList(customConfigFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return devicesList;
	}
	
	public static DevicesList loadDefaultDevicesList() {
		DevicesList devicesList = null;
		try {
			devicesList = loadDevicesList(ResourcesUtil.getResourceAsStream(
					DEFAULT_PREFERENCES_RESOURCE));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if (devicesList == null) {
			Device device = new Device("Default", 1024, 768, null, null);
			List<Device> devices = new ArrayList<Device>();
			devices.add(device);
			devicesList = new DevicesList(devices, 0, true);
		}

		return devicesList;
	}

	private static void saveDevicesList(DevicesList devicesList, File file) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(file));
		
		writer.write("ConfigVersion=" + String.valueOf(CURRENT_CONFIG_VERSION) + "\n");
		writer.write("SelectedDeviceIndex=" + String.valueOf(devicesList.getSelectedDeviceIndex()) + "\n");
		writer.write("UseSkins=" + String.valueOf(devicesList.getUseSkins()) + "\n");
		
		for (Device device : devicesList.getDevices()) {
			writer.write( encode(device.getName() ));
			writer.write('\t');
			writer.write(encode( String.valueOf(device.getWidth()) ));
			writer.write('\t');
			writer.write(encode( String.valueOf(device.getHeight()) ));
			writer.write('\t');
			if (device.getUserAgent() != null) {
				writer.write( encode(device.getUserAgent() ));
			}
			writer.write('\t');
			if (device.getSkinId() != null) {
				writer.write( encode(device.getSkinId() ));
			}
			writer.write('\n');
		}
		
		writer.close();
	}

	private static DevicesList loadDevicesList(File file) throws IOException {
		InputStream inputStream = new FileInputStream(file);
		return loadDevicesList(inputStream);
	}

	private static DevicesList loadDevicesList(InputStream inputStream) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		
		List<Device> devices = null;
		int selectedDeviceIndex = 0;
		boolean useSkins = true;
		try {
			String nextLine;
			
			int configVersion = 0;
			if ((nextLine = reader.readLine()) != null) {
				Pattern pattern = Pattern.compile("ConfigVersion=([0-9]+)");
				Matcher matcher = pattern.matcher(nextLine);
				if (matcher.matches()) {
					configVersion = Integer.parseInt(matcher.group(1));
				}
			}
			
			if (configVersion == CURRENT_CONFIG_VERSION) {
				if ((nextLine = reader.readLine()) != null) {
					Pattern pattern = Pattern.compile("SelectedDeviceIndex=([0-9]+)");
					Matcher matcher = pattern.matcher(nextLine);
					if (matcher.matches()) {
						selectedDeviceIndex = Integer.parseInt(matcher.group(1));
					}
				}
				
				if ((nextLine = reader.readLine()) != null) {
					Pattern pattern = Pattern.compile("UseSkins=(true|false)");
					Matcher matcher = pattern.matcher(nextLine);
					if (matcher.matches()) {
						useSkins = Boolean.parseBoolean(matcher.group(1));
					}
				}
				
				Pattern devicePattern = Pattern.compile("^(.*)\\t(\\-?[0-9]+)\\t(\\-?[0-9]+)\\t(.+)?\\t(.+)?$");
				
				devices = new ArrayList<Device>();
				while ((nextLine = reader.readLine()) != null) {
					Matcher deviceMatcher = devicePattern.matcher(nextLine);
					if (deviceMatcher.matches()) {
						devices.add(new Device(
								decode(deviceMatcher.group(1)),
								Integer.parseInt(deviceMatcher.group(2)),
								Integer.parseInt(deviceMatcher.group(3)),
								deviceMatcher.group(4) != null 
								? decode(deviceMatcher.group(4))
										: null,
										deviceMatcher.group(5) != null 
										? decode(deviceMatcher.group(5))
												: null
								));
					}
				}
			}	
		} finally {
			reader.close();
		}
		
		if (devices == null || devices.size() <= selectedDeviceIndex) {
			return null;
		} else { 
			return new DevicesList(devices, selectedDeviceIndex, useSkins);
		}
	}

	private static String encode(String string) {
		return string.replace("\\", "\\\\").replace("\n", "\\n").replace("\t", "\\t");
	}

	private static String decode(String string) {
		StringBuilder result = new StringBuilder();
			
		int i = 0;
		while (i < string.length() - 1) {
			char c0 = string.charAt(i);
			if (c0 == '\\') {
				char c1 = string.charAt(i + 1);
				switch (c1) {
				case '\\':
					result.append('\\');
					i++;
					break;
				case 'n':
					result.append('\n');
					i++;
					break;
				case 't':
					result.append('\t');
					i++;
					break;
				default:
					result.append(c0);
					break;
				}
			} else {
				result.append(c0);
			}
			i++;
		}
	
		if (i < string.length()) {
			result.append(string.charAt(i));
		}
				
		return result.toString();		
	}
}
