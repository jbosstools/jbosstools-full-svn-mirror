package org.jboss.jsr299.tck.tests.context.passivating.broken.dependentScopedProducerMethodReturnsNonSerializableObjectForInjectionIntoStatefulSessionBean;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.inject.Qualifier;


@Qualifier
@Target( { FIELD, PARAMETER, METHOD, TYPE })
@Retention(RUNTIME)
@interface British
{

}
