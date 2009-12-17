package org.jboss.jsr299.tck.tests.definition.stereotype.broken.tooManyScopes;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Stereotype;

@Stereotype
@Target( { TYPE })
@Retention(RUNTIME)
@ApplicationScoped
@RequestScoped
@interface StereotypeWithTooManyScopeTypes_Broken
{
}
