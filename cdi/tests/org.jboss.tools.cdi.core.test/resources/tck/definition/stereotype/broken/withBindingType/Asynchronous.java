package org.jboss.jsr299.tck.tests.definition.stereotype.broken.withBindingType;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.inject.Qualifier;

@Target( { TYPE, METHOD, PARAMETER })
@Retention(RUNTIME)
@Documented
@Qualifier
@interface Asynchronous
{
}
