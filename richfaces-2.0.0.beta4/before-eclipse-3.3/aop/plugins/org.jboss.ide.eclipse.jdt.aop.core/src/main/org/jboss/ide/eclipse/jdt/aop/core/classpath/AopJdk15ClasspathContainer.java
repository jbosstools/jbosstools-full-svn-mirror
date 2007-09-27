/*
 * JBoss, a division of Red Hat
 * Copyright 2006, Red Hat Middleware, LLC, and individual contributors as indicated
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
package org.jboss.ide.eclipse.jdt.aop.core.classpath;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

/**
 * @author Marshall
 */
public class AopJdk15ClasspathContainer extends AopClasspathContainer
{
   public static final String CONTAINER_ID = "org.jboss.ide.eclipse.jdt.aop.core.classpath.AOP_15_CONTAINER";

   public static final String DESCRIPTION = "JBossAOP 1.3 Libraries (jdk 1.5)";

   protected final static IPath aopJars[] = new IPath[]
   {new Path("jboss-aop-jdk50.jar"), new Path("jboss-common.jar"), new Path("qdox.jar"), new Path("concurrent.jar"),
         new Path("trove.jar"), new Path("javassist.jar"), new Path("jboss-aspect-library-jdk50.jar"),
         new Path("pluggable-instrumentor.jar")};

   public AopJdk15ClasspathContainer(IPath path)
   {
      super(path);
   }

   protected IPath[] getAopJarRelativePaths()
   {
      return aopJars;
   }

   public String getContainerId()
   {
      return CONTAINER_ID;
   }

   public String getDescription()
   {
      return DESCRIPTION;
   }
}
