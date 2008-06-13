package org.jboss.tools.ws.core.classpath;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;


/**
 * 
 * @author Grid Qian
 *
 */
public class JbossWSRuntimeListConverter {

		/*
		 * Constants definitions 
		 */
		private static final String REGEXP_ESCAPE = "\\";
		private static final String COMMA = ",";
		private static final String LIBRARY_SEPARATOR = ";";
		private static final String EMPTY_STRING = "";
		private static final String FIELD_SEPARATOR = "|";
		private static final String DEFAULT = "default";
		private static final String HOME_DIR = "homeDir";
		private static final String VERSION = "version";
		private static final String NAME = "name";
		private static final String USER_CONFIG_CLASSPATH = "userConfig";
		private static final String LIBRARY = "libraries";

		/**
		 * Load String to JbossWSRuntime map from String
		 * @param input
		 * 		String representation of map
		 * @return
		 * 		Map&lt;String, JbossWSRuntime&gt; loaded from string
		 * TODO - switch to XML?
		 * TODO - write converter from old serialization format to XML?
		 * TODO - handle errors in string format
		 */
		public Map<String, JbossWSRuntime> getMap(String input) {

			Map<String, JbossWSRuntime> result = new HashMap<String, JbossWSRuntime>();
			if (input == null || EMPTY_STRING.equals(input.trim())) {
				return result;
			}
			StringTokenizer runtimes = new StringTokenizer(input, COMMA);
			while (runtimes.hasMoreTokens()) {
				String runtime = runtimes.nextToken();
				String[] map = runtime.split(REGEXP_ESCAPE + FIELD_SEPARATOR);
				JbossWSRuntime rt = new JbossWSRuntime();
				final int step = 2;
				for (int i = 0; i < map.length; i += step) {
					String name = map[i];
					String value = i + 1 < map.length ? map[i + 1] : EMPTY_STRING;
					if (NAME.equals(name)) {
						rt.setName(value);
					} else if (HOME_DIR.equals(name)) {
						rt.setHomeDir(value);
					} else if (VERSION.equals(name)) {
						rt.setVersion(value);
					} else if (DEFAULT.equals(name)) {
						rt.setDefault(Boolean.parseBoolean(value));
					}else if(USER_CONFIG_CLASSPATH.equals(name)){
						rt.setUserConfigClasspath(Boolean.parseBoolean(value));
					}else if(LIBRARY.equals(name)){
						if(value != null && !EMPTY_STRING.equals(value)){
							rt.setLibraries(getLibrariesFromString(value));
						}
					}
					
				}
				result.put(rt.getName(), rt);
			}

			return result;
		}
		
		private List<String> getLibrariesFromString(String strLibraries){
			List<String> libraries = new ArrayList<String>();
			StringTokenizer st = new StringTokenizer(strLibraries, LIBRARY_SEPARATOR);
			while(st.hasMoreTokens()){
				String library = st.nextToken();
				if(!libraries.contains(library)){
					libraries.add(library);
				}
			}
			return libraries;
			
		}
		
		private String convertListToString(List<String> libraries){
			String strLib = "";
			for(String library: libraries){
				if("".equals(strLib)){
					strLib = library;
				}else{
					strLib = strLib + LIBRARY_SEPARATOR + library;
				}
			}
			return strLib;
		}

		/**
		 * Convert map String to JbossWSRUntime to string representation 
		 * @param runtimeMap
		 * 		Map&lt;String, JbossWSRuntime&gt; - map of String to JbossWS Runtime to convert 
		 * 		in String 
		 * @return
		 * 		String representation of String to JbossWS Runtime map
		 */
		public String getString(Map<String, JbossWSRuntime> runtimeMap) {
			StringBuffer buffer = new StringBuffer();
			JbossWSRuntime[] runtimes = runtimeMap.values().toArray(
					new JbossWSRuntime[runtimeMap.size()]);
			for (int i = 0; i < runtimes.length; i++) {
				buffer.append(NAME).append(FIELD_SEPARATOR);
				buffer.append(runtimes[i].getName());
				buffer.append(FIELD_SEPARATOR).append(VERSION).append(
						FIELD_SEPARATOR);
				buffer.append(runtimes[i].getVersion());
				buffer.append(FIELD_SEPARATOR).append(HOME_DIR).append(
						FIELD_SEPARATOR);
				buffer.append(runtimes[i].getHomeDir());
				buffer.append(FIELD_SEPARATOR).append(DEFAULT).append(
						FIELD_SEPARATOR);
				buffer.append(runtimes[i].isDefault());
				buffer.append(FIELD_SEPARATOR).append(USER_CONFIG_CLASSPATH).append(FIELD_SEPARATOR);
				buffer.append(runtimes[i].isUserConfigClasspath());
				buffer.append(FIELD_SEPARATOR).append(LIBRARY).append(FIELD_SEPARATOR);
				buffer.append(convertListToString(runtimes[i].getLibraries()));
				if (i != runtimes.length - 1) {
					buffer.append(COMMA);
				}
			}
			return buffer.toString();
		}
	}

