package org.jboss.jsr299.tck.tests.decorators.resolution;

import javax.decorator.Decorator;
import javax.decorator.Delegate;
import javax.inject.Inject;

@Decorator
public class QuxDecorator
{
   
   @Inject @Delegate
   private Qux<String> qux;

}
