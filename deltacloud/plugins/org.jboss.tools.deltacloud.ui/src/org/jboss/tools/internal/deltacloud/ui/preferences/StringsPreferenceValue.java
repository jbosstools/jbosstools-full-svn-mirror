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
package org.jboss.tools.internal.deltacloud.ui.preferences;

import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * A class that offers access to a collection of values that is stored in the
 * preferences under a single key.
 * 
 * @author Andre Dietisheim
 */
public class StringsPreferenceValue extends AbstractPreferenceValue<String[]> {

	private String delimiter;

	public StringsPreferenceValue(char delimiter, String prefsKey, String pluginId) {
		super(prefsKey, pluginId);
		this.delimiter = String.valueOf(delimiter);
	}

	public String[] get() {
		return get(null);
	}

	public String[] get(String[] currentValues) {

		String string = doGet(null);
		String[] prefValues = split(string);
		return overrideValues(currentValues, prefValues);
	}

	private String[] split(String string) {
		ArrayList<String> values = new ArrayList<String>();
		StringTokenizer tokenizer = new StringTokenizer(string, delimiter);
		while (tokenizer.hasMoreTokens()) {
			values.add(tokenizer.nextToken());
		}
		return values.toArray(new String[values.size()]);
	}

	private String[] overrideValues(String[] newValues, String[] prefValues) {
		if (prefValues == null) {
			return newValues;
		}

		for (int i = 0; i < prefValues.length; i++) {
			if (newValues == null
					|| newValues.length < i) {
				break;
			}
			prefValues[i] = newValues[i];
		}
		return prefValues;
	}

	/**
	 * Adds the given string value to this preference value(s). Duplicate values
	 * are not added.
	 * 
	 * @param value
	 *            the value to add
	 */
	public void add(String value) {
		String currentValues = doGet();
		StringBuilder builder = new StringBuilder(currentValues);
		if (!contains(value, currentValues)) {
			if (hasValues(currentValues)) {
				builder.append(delimiter);
			}
			builder.append(value);
			doStore(builder.toString());
		}
	}

	private boolean contains(String value, String currentValues) {
		return currentValues != null
				&& currentValues.length() > 0
				&& currentValues.indexOf(value) >= 0;
	}

	private boolean hasValues(String currentValues) {
		return currentValues != null && currentValues.length() > 0;
	}

	/**
	 * Removes the given values from the strings stored in the preferences and
	 * stores the preferences.
	 * 
	 * @param values
	 *            the values
	 */
	public void remove(String... valuesToRemove) {
		boolean removed = false;
		String[] currentValues = get();
		if (valuesToRemove != null) {
			for (int i = 0; i < currentValues.length; i++) {
				for (String valueToRemove : valuesToRemove) {
					if (valueToRemove.equals(currentValues[i])) {
						currentValues[i] = null;
						removed = true;
					}
				}
			}
		}
		if (removed) {
			store(currentValues);
		}
	}

	/**
	 * Overrides the current values in the preferences with the values in the
	 * given array (value in the preferences at index x is overridden with the
	 * value in the given array at index x) and stores the preferences.
	 */
	public void store(String[] newValues) {
		String[] currentValues = get();
		overrideValues(newValues, currentValues);
		doStore(concatenate(currentValues));
	}

	public void store() {
		store(null);
	}

	protected String concatenate(String[] values) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < values.length; i++) {
			if (values[i] != null) {
				if (builder.length() > 0) {
					builder.append(delimiter);
				}
				builder.append(values[i]);
			}
		}
		return builder.toString();
	}
}
