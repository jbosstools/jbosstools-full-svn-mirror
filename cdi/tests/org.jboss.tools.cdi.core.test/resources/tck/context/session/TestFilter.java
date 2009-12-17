package org.jboss.jsr299.tck.tests.context.session;

import java.io.IOException;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class TestFilter implements Filter
{
   @Inject
   private BeanManager jsr299Manager;

   public void destroy()
   {
      jsr299Manager = null;
   }

   public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
   {
      if (!jsr299Manager.getContext(SessionScoped.class).isActive())
      {
         throw new ServletException("Session is not active");
      }
      else
      {
         chain.doFilter(request, response);
      }
   }

   public void init(FilterConfig filterConfig) throws ServletException
   {
   }

}
