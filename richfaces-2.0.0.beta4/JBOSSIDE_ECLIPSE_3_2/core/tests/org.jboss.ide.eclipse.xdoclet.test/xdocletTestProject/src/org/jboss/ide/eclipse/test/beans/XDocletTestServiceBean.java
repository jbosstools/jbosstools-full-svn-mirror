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
package org.jboss.ide.eclipse.test.beans;

import javax.rmi.RemoteException;
import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import javax.ejb.CreateException;

/**
 * A XDocletTestServiceBean.
 * 
 * @ejb.bean
 *   name= "XDocletTestService"
 *   type="Stateless"
 *   display-name="XDocletTest Service Bean"
 *   jndi-name="ejb/XDocletTest"
 *   view-type = "both"
 * 
 * @author <a href="marshall@jboss.org">Marshall Culpepper</a>
 * @version $Revision$
 */
public class XDocletTestServiceBean implements SessionBean
{

   public void ejbActivate() throws EJBException, RemoteException {
       // TODO Auto-generated method stub
   }

   public void ejbPassivate() throws EJBException, RemoteException {
       // TODO Auto-generated method stub
   }

   public void ejbRemove() throws EJBException, RemoteException {
       // TODO Auto-generated method stub
   }

   public void setSessionContext(SessionContext ctx) throws EJBException,
           RemoteException {
       // TODO Auto-generated method stub
   }

   /**
    * @ejb.create-method
    */
   public void ejbCreate() throws CreateException {
       // TODO Auto-generated method stub
   }
   
   /**
    * @ejb.interface-method
    *       view-type= "both"
    */
   public void someServiceMethod () {
      
   }
   
   /**
    * @ejb.interface-method
    *       view-type = "local"
    */
   public void someLocalServiceMethod () {
      
   }
   
   /**
    * @ejb.interface-method
    *   view-type = "remote"
    */
   public void someRemoteServiceMethod () {
      
   }
   
   public void someNonViewableServiceMethod () {
      
   }
}
