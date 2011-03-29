/**
 * Copyright 2010 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jboss.tools.jbpm.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.jboss.tools.jbpm.JBPMEclipsePlugin;
import org.jboss.tools.jbpm.preferences.JBPMConstants;

public class JBPMRuntimeManager {
	
	private static final String JBPM_RUNTIME_RECOGNIZER = "org.jboss.tools.jbpm.runtimeRecognizer";

	public static void createDefaultRuntime(String location) {
		List<String> jars = new ArrayList<String>();
		// get all jbpm jars from jbpm eclipse plugin
		String s = getJBPMLocation();
        File file = (new Path(s)).toFile();
        File[] files = file.listFiles();
        for (int i = 0; i < files.length; i++) {
	        if (files[i].isDirectory() && files[i].getName().equals("lib")) {
	            File[] jarFiles = files[i].listFiles();
	            for (int j = 0; j < jarFiles.length; j++) {
	                if (jarFiles[j].getPath().endsWith(".jar")) {
	                    jars.add(jarFiles[j].getAbsolutePath());
	                }
	            }
            }
        }
        // get eclipse jdt jar
        String pluginRootString = Platform.getInstallLocation().getURL().getPath() + "plugins/";
	    File pluginRoot = new Path(pluginRootString).toFile();
	    files = pluginRoot.listFiles();
	    boolean found = false;
	    // search for eclipse jdt 3.6.x jar
	    for (int i = 0; i < files.length; i++) {
	        if (files[i].getAbsolutePath().indexOf("org.eclipse.jdt.core_3.6") > -1) {
	        	jars.add(files[i].getAbsolutePath());
	        	found = true;
	        	break;
	        }
	    }
	    // if not found, search for eclipse jdt 3.5.x jar
	    if (!found) {
	    	for (int i = 0; i < files.length; i++) {
		        if (files[i].getAbsolutePath().indexOf("org.eclipse.jdt.core_3.5") > -1) {
		        	jars.add(files[i].getAbsolutePath());
		        	found = true;
		        	break;
		        }
	    	}
	    }
	    // if not found, search for eclipse jdt 3.4.x jar
	    if (!found) {
		    for (int i = 0; i < files.length; i++) {
		        if (files[i].getAbsolutePath().indexOf("org.eclipse.jdt.core_3.4") > -1) {
		        	jars.add(files[i].getAbsolutePath());
		        	break;
		        }
		    }
	    }
	    // copy jars to specified location
	    if (!location.endsWith(File.separator)) {
	    	location = location + File.separator;
	    }
	    for (String jar: jars) {
	    	try {
	    		File jarFile = new File(jar);
		    	FileChannel inChannel = new FileInputStream(jarFile).getChannel();
		        FileChannel outChannel = new FileOutputStream(new File(
	        		location + jarFile.getName())).getChannel();
		        try {
		            inChannel.transferTo(0, inChannel.size(), outChannel);
		        } 
		        catch (IOException e) {
		            throw e;
		        }
		        finally {
		            if (inChannel != null) inChannel.close();
		            if (outChannel != null) outChannel.close();
		        }
	    	} catch (Throwable t) {
	    		JBPMEclipsePlugin.log(t);
	    	}
	    }
	}

	private static String getJBPMLocation() {
        try {
            return FileLocator.toFileURL(Platform.getBundle("org.jboss.tools.jbpm").getEntry("/")).getFile().toString();
        } catch (IOException e) {
            JBPMEclipsePlugin.log(e);
        }
        return null;
    }
	
	private static String generateString(JBPMRuntime[] jBPMRuntimes) {
		String result = "";
		for (JBPMRuntime runtime: jBPMRuntimes) {
			result += runtime.getName() + "#" + runtime.getPath() + "#" + runtime.isDefault() + "# ";
			if (runtime.getJars() != null) {
				for (String jar: runtime.getJars()) {
					result += jar + ";";
				}
			}
			result += "###";
		}
		return result;
	}
	
	private static JBPMRuntime[] generateRuntimes(String s) {
		List<JBPMRuntime> result = new ArrayList<JBPMRuntime>();
		if (s != null && !"".equals(s)) {
			String[] runtimeStrings = s.split("###");
			for (String runtimeString: runtimeStrings) {
				String[] properties = runtimeString.split("#");
				JBPMRuntime runtime = new JBPMRuntime();
				runtime.setName(properties[0]);
				runtime.setPath(properties[1]);
				runtime.setDefault("true".equals(properties[2]));
				if (properties.length > 3) {
					List<String> list = new ArrayList<String>();
					String[] jars = properties[3].split(";");
					for (String jar: jars) {
						jar = jar.trim();
						if (jar.length() > 0) {
							list.add(jar);
						}
					}
					runtime.setJars(list.toArray(new String[list.size()]));
				}
				result.add(runtime);
			}
		}
		return result.toArray(new JBPMRuntime[result.size()]);
	}
	
	public static JBPMRuntime[] getJBPMRuntimes() {
		String runtimesString = JBPMEclipsePlugin.getDefault().getPreferenceStore()
			.getString(JBPMConstants.JBPM_RUNTIMES);
		if (runtimesString != null) {
			return generateRuntimes(runtimesString);
		}
		return new JBPMRuntime[0];
	}
	
	public static void setJBPMRuntimes(JBPMRuntime[] runtimes) {
		JBPMEclipsePlugin.getDefault().getPreferenceStore().setValue(JBPMConstants.JBPM_RUNTIMES,
			JBPMRuntimeManager.generateString(runtimes));
	}
	
	public static JBPMRuntime getJBPMRuntime(String name) {
		JBPMRuntime[] runtimes = getJBPMRuntimes();
		for (JBPMRuntime runtime: runtimes) {
			if (runtime.getName().equals(name)) {
				return runtime;
			}
		}
		return null;
	}
	
	public static JBPMRuntime getDefaultJBPMRuntime() {
		JBPMRuntime[] runtimes = getJBPMRuntimes();
		for (JBPMRuntime runtime: runtimes) {
			if (runtime.isDefault()) {
				return runtime;
			}
		}
		return null;
	}
	
    public static String getJBPMRuntime(IProject project) {
        try {
        	IFile file = project.getFile(".settings/.jbpm.runtime");
        	if (file.exists()) {
        		BufferedReader reader = new BufferedReader(new InputStreamReader(file.getContents()));
        		String location = reader.readLine();
        		if (location.startsWith("<runtime>") && location.endsWith("</runtime>")) {
        			return location.substring(9, location.length() - 10);
        		}
        	}
        } catch (Exception e) {
            JBPMEclipsePlugin.log(e);
        }
        return null;
    }
    
    public static String[] getJBPMRuntimeJars(IProject project) {
        String runtimeName = getJBPMRuntime(project);
        JBPMRuntime runtime = null;
        if (runtimeName == null) {
        	runtime = getDefaultJBPMRuntime();
        } else {
        	runtime = getJBPMRuntime(runtimeName);
        }
        if (runtime == null) {
        	return null;
        }
        if (runtime.getJars() == null || runtime.getJars().length == 0) {
    		recognizeJars(runtime);
    	}
        return runtime.getJars();
    }
    
    public static void recognizeJars(JBPMRuntime runtime) {
    	String path = runtime.getPath();
		if (path != null) {
			try {
				IConfigurationElement[] config = Platform.getExtensionRegistry()
						.getConfigurationElementsFor(JBPM_RUNTIME_RECOGNIZER);
				for (IConfigurationElement e : config) {
					Object o = e.createExecutableExtension("class");
					if (o instanceof JBPMRuntimeRecognizer) {
						String[] jars = ((JBPMRuntimeRecognizer) o).recognizeJars(path);
						if (jars != null && jars.length > 0) {
							runtime.setJars(jars);
							return;
						}
					}
				}
			} catch (Exception ex) {
				System.out.println(ex.getMessage());
			}

			runtime.setJars(new DefaultJBPMRuntimeRecognizer().recognizeJars(path));
		}
	}

}
