package org.jboss.jsr299.tck.tests.interceptors.definition.inheritance;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.interceptor.InterceptorBinding;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@InterceptorBinding
@Target({ TYPE, METHOD })
@Retention(RUNTIME)
@Documented
public @interface BindingB {
}
