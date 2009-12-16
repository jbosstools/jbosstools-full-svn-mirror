/*
 * JBoss, Home of Professional Open Source
 * Copyright 2008, Red Hat Middleware LLC, and individual contributors
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
package org.jboss.jsr299.tck.tests.decorators.invocation.producer.method;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;

import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.jsr299.BeansXml;
import org.testng.annotations.Test;

/**
 * @author pmuir
 *
 */
@Artifact
@BeansXml("beans.xml")
@SpecVersion(spec="cdi", version="20091101")
public class DecoratorInvocationTest extends AbstractJSR299Test
{

   @Test
   @SpecAssertions({
      @SpecAssertion(section="7.2", id="ib"),
      @SpecAssertion(section="7.2", id="id")
   })
   public void testDecoratorInvocation()
   {
      ProducerDecorator.reset();
      ProducerImpl.reset();
      Bean<Foo> bean = (Bean<Foo>) getCurrentManager().resolve(getCurrentManager().getBeans(Foo.class));
      CreationalContext<Foo> creationalContext = getCurrentManager().createCreationalContext(bean);
      Foo foo = bean.create(creationalContext);
      assert foo.getFoo().equals("foo!!!");
      bean.destroy(foo, creationalContext);
      assert ProducerImpl.isDisposedCorrectly();
   }

}
