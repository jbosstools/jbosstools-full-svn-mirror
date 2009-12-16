package org.jboss.jsr299.tck.tests.implementation.enterprise.newBean;

import javax.ejb.Local;
import javax.enterprise.inject.New;

import org.jboss.jsr299.tck.literals.NewLiteral;

@Local
public interface OrderLocal
{
   
   public static final New NEW = new NewLiteral()
   {
      
      public Class<?> value()
      {
         return Order.class;
      }
   };
   
}