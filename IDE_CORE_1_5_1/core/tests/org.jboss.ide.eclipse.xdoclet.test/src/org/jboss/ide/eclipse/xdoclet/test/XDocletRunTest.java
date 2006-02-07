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

import org.eclipse.jface.viewers.StructuredSelection;
import org.jboss.ide.eclipse.core.test.util.TestFileUtil;
import org.jboss.ide.eclipse.core.test.util.TestUIUtil;
import org.jboss.ide.eclipse.core.util.ProjectUtil;
import org.jboss.ide.eclipse.xdoclet.run.builder.XDocletRunBuilder;
import org.jboss.ide.eclipse.xdoclet.run.ui.actions.XDocletRunAction;

/**
 * This test case is responsible for testing the "Run XDoclet" functionality
 * 
 * @author <a href="marshall@jboss.org">Marshall Culpepper</a>
 * @version $Revision$
 */
public class XDocletRunTest extends XDocletTest
{
   public void testRunXDoclet ()
   {
      assertFalse(ProjectUtil.projectHasBuilder(xdocletTestProject.getProject(), XDocletRunBuilder.BUILDER_ID));
      createTestConfiguration();
      
      XDocletRunAction action = new XDocletRunAction();
      action.selectionChanged(null, new StructuredSelection(xdocletTestProject));
      action.run(null);
      
      TestUIUtil.delay(1000);
      TestUIUtil.waitForJobs();
      
      assertTrue (TestFileUtil.projectFileExists(xdocletTestProject.getProject(), "/src/META-INF/jboss.xml"));
      assertTrue (TestFileUtil.projectFileExists(xdocletTestProject.getProject(), "/src/META-INF/ejb-jar.xml"));
      assertTrue (TestFileUtil.projectFileExists(xdocletTestProject.getProject(), "/src/org/jboss/ide/eclipse/test/interfaces/XDocletTestService.java"));
   }
}
