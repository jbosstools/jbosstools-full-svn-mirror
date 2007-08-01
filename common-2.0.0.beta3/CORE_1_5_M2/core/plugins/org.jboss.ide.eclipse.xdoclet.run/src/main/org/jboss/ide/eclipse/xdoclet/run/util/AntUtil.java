/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.xdoclet.run.util;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.osgi.service.datalocation.Location;
import org.jboss.ide.eclipse.core.AbstractPlugin;

/**
 * @author    Laurent Etiemble
 * @version   $Revision$
 * @todo      Javadoc to complete
 */
public class AntUtil
{
   private static Map _substitutions;
   
   /**
    * Gets the classPathAsXml attribute of the AntUtil class
    *
    * @param project  Description of the Parameter
    * @return         The classPathAsXml value
    */
   public static String getClassPathAsXml(IJavaProject project)
   {
      StringBuffer result = new StringBuffer();
      try
      {
         ArrayList cp = new ArrayList();
         ProjectUtil.populateClassPath(project, project, cp);
         Iterator iterator = cp.iterator();
         while (iterator.hasNext())
         {
            String lib = (String) iterator.next();
            lib = performSubstitutions(lib);
            result.append("<pathelement location=\"");//$NON-NLS-1$
            result.append(lib);
            result.append("\"/>\n");//$NON-NLS-1$
         }
      }
      catch (IOException e)
      {
         AbstractPlugin.logError("Unable to build project classpath as XML", e);//$NON-NLS-1$
      }
      catch (CoreException ce)
      {
         AbstractPlugin.logError("Unable to build project classpath as XML", ce);//$NON-NLS-1$
      }
      return result.toString();
   }

   /**
    * @param path
    * @return
    * @throws IOException
    */
   public static String performSubstitutions(String path) throws IOException
   {
      Map substitutions = getSubstitutions();
      for (Iterator iter = substitutions.entrySet().iterator(); iter.hasNext();)
      {
         Map.Entry subst = (Map.Entry) iter.next();
         Pattern pattern = (Pattern) subst.getKey();
         String prop = (String) subst.getValue();
         path = pattern.matcher(path).replaceAll(prop);
      }
      return path;
   }

   /**
    * @return
    * @throws IOException
    */
   private static Map getSubstitutions() throws IOException
   {
      if (null == _substitutions)
      {
	      _substitutions = new LinkedHashMap();
	      _substitutions.put(getPattern(Platform.getInstallLocation()), escapeReplacement("${eclipse.home}"));
      }
      
      return _substitutions;
   }

   private static Pattern getPattern(Location location) throws IOException
   {
      URL url = location.getURL();
      String path = normalizePath(url);
      String regex = escapeRegex(path);
      return Pattern.compile(regex);
   }

   public static String normalizePath(URL url) throws IOException
   {
      String path = url.getFile();
      return normalizePath(path);
   }

   public static String normalizePath(String path) throws IOException
   {
      return new Path(new File(path).getCanonicalPath()).toString();
   }

   private static String escapeRegex(String str)
   {
      return "\\Q"+str+"\\E";
   }
   
   private static String escapeReplacement(String str)
   {
      return str.replaceAll("\\$", "\\\\\\$");
   }
}
