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
package org.jboss.ide.eclipse.jdt.ws.core.generation;

import java.util.HashMap;

import org.apache.axis.wsdl.toJava.Emitter;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.jboss.ide.eclipse.core.AbstractPlugin;
import org.jboss.ide.eclipse.jdt.core.wizards.generation.IGenerationEngine;

/**
 * Description of the Class
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class WSDL2JavaGenerationEngine implements IGenerationEngine
{
   private Emitter emitter;

   private String uri;

   /**Constructor for the WSDL2JavaGenerationEngine object */
   public WSDL2JavaGenerationEngine()
   {
      this.emitter = new Emitter();
   }

   /**
    * Description of the Method
    *
    * @param type                    Description of the Parameter
    * @param monitor                 Description of the Parameter
    * @exception JavaModelException  Description of the Exception
    */
   public void generate(IType type, IProgressMonitor monitor) throws JavaModelException
   {
      try
      {
         emitter.run(this.uri);
      }
      catch (Exception e)
      {
         AbstractPlugin.logError("Unable to generate web services test client", e);//$NON-NLS-1$
      }
   }

   /**
    * Sets the allWanted attribute of the WSDL2JavaGenerationEngine object
    *
    * @param value  The new allWanted value
    */
   public void setAllWanted(boolean value)
   {
      this.emitter.setAllWanted(value);
   }

   /**
    * Sets the helperWanted attribute of the WSDL2JavaGenerationEngine object
    *
    * @param value  The new helperWanted value
    */
   public void setHelperWanted(boolean value)
   {
      this.emitter.setHelperWanted(value);
   }

   /**
    * Sets the imports attribute of the WSDL2JavaGenerationEngine object
    *
    * @param value  The new imports value
    */
   public void setImports(boolean value)
   {
      this.emitter.setImports(value);
   }

   /**
    * Sets the nStoPkg attribute of the WSDL2JavaGenerationEngine object
    *
    * @param value  The new nStoPkg value
    */
   public void setNStoPkg(String value)
   {
      this.emitter.setNStoPkg(value);
   }

   /**
    * Sets the namespaceMap attribute of the WSDL2JavaGenerationEngine object
    *
    * @param value  The new namespaceMap value
    */
   public void setNamespaceMap(HashMap value)
   {
      this.emitter.setNamespaceMap(value);
   }

   /**
    * Sets the nowrap attribute of the WSDL2JavaGenerationEngine object
    *
    * @param value  The new nowrap value
    */
   public void setNowrap(boolean value)
   {
      this.emitter.setNowrap(value);
   }

   /**
    * Sets the outputDir attribute of the WSDL2JavaGenerationEngine object
    *
    * @param value  The new outputDir value
    */
   public void setOutputDir(String value)
   {
      this.emitter.setOutputDir(value);
   }

   /**
    * Sets the packageName attribute of the WSDL2JavaGenerationEngine object
    *
    * @param value  The new packageName value
    */
   public void setPackageName(String value)
   {
      this.emitter.setPackageName(value);
   }

   /**
    * Sets the password attribute of the WSDL2JavaGenerationEngine object
    *
    * @param value  The new password value
    */
   public void setPassword(String value)
   {
      this.emitter.setPassword(value);
   }

   /**
    * Sets the testCaseWanted attribute of the WSDL2JavaGenerationEngine object
    *
    * @param value  The new testCaseWanted value
    */
   public void setTestCaseWanted(boolean value)
   {
      this.emitter.setTestCaseWanted(value);
   }

   /**
    * Sets the typeMappingVersion attribute of the WSDL2JavaGenerationEngine object
    *
    * @param version  The new typeMappingVersion value
    */
   public void setTypeMappingVersion(String version)
   {
      this.emitter.setTypeMappingVersion(version);
   }

   /**
    * Sets the username attribute of the WSDL2JavaGenerationEngine object
    *
    * @param value  The new username value
    */
   public void setUsername(String value)
   {
      this.emitter.setUsername(value);
   }

   /**
    * Sets the wSDL attribute of the WSDL2JavaGenerationEngine object
    *
    * @param uri  The new wSDL value
    */
   public void setWSDL(String uri)
   {
      this.uri = uri;
   }

}
