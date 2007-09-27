/*
 * JBoss, Home of Professional Open Source
 * Copyright 2005, JBoss Inc., and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.ide.eclipse.xdoclet.test;

import org.jboss.ide.eclipse.core.test.util.TestFileUtil;
import org.jboss.ide.eclipse.core.util.ProjectUtil;
import org.jboss.ide.eclipse.xdoclet.run.XDocletRunPlugin;
import org.jboss.ide.eclipse.xdoclet.run.builder.XDocletRunBuilder;
import org.jboss.ide.eclipse.xdoclet.run.ui.properties.ConfigurationPropertyPage;

/**
 * This test case is responsible for testing the ".xdoclet" and "xdoclet-build.xml" files that JBossIDE generates
 * 
 * @author <a href="marshall@jboss.org">Marshall Culpepper</a>
 * @version $Revision$
 */
public class XDocletPrefsTest extends XDocletTest
{
   
   public void testAddBuilder ()
   {
      assertFalse(ProjectUtil.projectHasBuilder(xdocletTestProject.getProject(), XDocletRunBuilder.BUILDER_ID));
     
      XDocletRunPlugin.getDefault().enableXDocletBuilder(xdocletTestProject, true);
      
      assertTrue(ProjectUtil.projectHasBuilder(xdocletTestProject.getProject(), XDocletRunBuilder.BUILDER_ID));
   }
   
   public void testAddBuilderInPrefs ()
   {
      assertFalse(ProjectUtil.projectHasBuilder(xdocletTestProject.getProject(), XDocletRunBuilder.BUILDER_ID));
      
      openXDocletPrefsDialog();
      
      propertyPage.getButton(ConfigurationPropertyPage.BUTTON_ENABLE_XDOCLET).setSelection(true);
      propertyPage.performOk();
      dialog.close();
      
      assertTrue(ProjectUtil.projectHasBuilder(xdocletTestProject.getProject(), XDocletRunBuilder.BUILDER_ID));
   }
   
   public void testXDocletPrefsEnabled ()
   {
      XDocletRunPlugin.getDefault().enableXDocletBuilder(xdocletTestProject, true);
      assertTrue(ProjectUtil.projectHasBuilder(xdocletTestProject.getProject(), XDocletRunBuilder.BUILDER_ID));
      
      openXDocletPrefsDialog();
      
      assertTrue(propertyPage.getButton(ConfigurationPropertyPage.BUTTON_ENABLE_XDOCLET).getSelection());    
      dialog.close();
   }
   
   public void testBuilder ()
   {
      assertFalse(ProjectUtil.projectHasBuilder(xdocletTestProject.getProject(), XDocletRunBuilder.BUILDER_ID));
      
      createTestConfiguration();
      
      assertTrue (TestFileUtil.projectFileExists(xdocletTestProject.getProject(), ".xdoclet"));
      assertTrue (TestFileUtil.projectFileExists(xdocletTestProject.getProject(), "xdoclet-build.xml"));
   }
}
