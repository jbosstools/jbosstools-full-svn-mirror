/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.jsr299.tck.tests.decorators.invocation;

import javax.decorator.Decorator;
import javax.decorator.Delegate;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;

/**
 * @author pmuir
 *
 */
@Decorator
public class FooDecorator2 implements Foo
{
   
   private static InjectionPoint injectionPoint;
   
   @Inject InjectionPoint ip;
   
   public static InjectionPoint getInjectionPoint()
   {
      return injectionPoint;
   }
   
   public static final String SUFFIX = "fooDecorator2";
   
   private static String message;
   
   /**
    * @return the message
    */
   public static String getMessage()
   {
      return message;
   }
   
   /**
    * @param message the message to set
    */
   public static void reset()
   {
      FooDecorator2.message = null;
      FooDecorator2.injectionPoint = null;
   }
   
   @Inject @Delegate Foo foo;
   
   public void log(String message)
   {
      FooDecorator2.message = message;
      foo.log(message + SUFFIX);
      injectionPoint = ip;
   }

}
