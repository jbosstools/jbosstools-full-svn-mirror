package org.jboss.jsr299.tck.tests.implementation.enterprise.newBean;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Stereotype;
import javax.inject.Named;

@Stereotype
@Target( { TYPE, METHOD, FIELD })
@Retention(RUNTIME)
@RequestScoped
@Named
@interface AnimalStereotype
{

}
