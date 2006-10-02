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
package org.jboss.ide.eclipse.jdt.aop.core.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.jboss.ide.eclipse.jdt.aop.core.jaxb.Aop;
import org.jboss.ide.eclipse.jdt.aop.core.jaxb.ObjectFactory;

/**
 * @author aoswald
 */
public class JaxbAopUtil
{
   private static JaxbAopUtil instance = null;

   private ObjectFactory objectFactory;

   private JAXBContext context;

   private Unmarshaller unmarshaller;

   private Marshaller marshaller;

   private HashMap projectDescriptors;

   private JaxbAopUtil()
   {
      init();
   }

   public static JaxbAopUtil instance()
   {
      if (instance == null)
      {
         instance = new JaxbAopUtil();
      }
      return instance;
   }

   private void init()
   {
      try
      {
         objectFactory = new ObjectFactory();
         context = JAXBContext.newInstance("org.jboss.ide.eclipse.jdt.aop.core.jaxb", getClass().getClassLoader());
         unmarshaller = context.createUnmarshaller();
         marshaller = context.createMarshaller();
         marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, new Boolean(true));
      }
      catch (JAXBException e)
      {
         e.printStackTrace();
      }
   }

   //TODO: this has to have error handling, etc.
   public Aop unmarshal(InputStream stream)
   {
      Aop aop = null;
      try
      {
         aop = objectFactory.createAop();
         aop = (Aop) unmarshaller.unmarshal(stream);
      }
      catch (JAXBException e)
      {
         e.printStackTrace();
      }
      return aop;
   }

   public Aop unmarshal(File file)
   {
      if (file != null && file.getName().endsWith("-aop.xml") && file.exists())
      {
         try
         {
            return unmarshal(new FileInputStream(file));
         }
         catch (FileNotFoundException e)
         {
            e.printStackTrace();
         }
      }
      else if (!file.exists())
      {
         try
         {
            return getFactory().createAop();
         }
         catch (JAXBException e)
         {
            e.printStackTrace();
         }
      }
      return null;
   }

   //TODO: this has to have error handling, etc.
   public void marshal(Aop aopGraph, File targetFile)
   {
      try
      {
         marshaller.marshal(aopGraph, new FileOutputStream(targetFile));
      }
      catch (FileNotFoundException e)
      {
         e.printStackTrace();
      }
      catch (JAXBException e)
      {
         e.printStackTrace();
      }
   }

   public ObjectFactory getFactory()
   {
      return objectFactory;
   }

}
