package org.jboss.jsr299.tck.tests.context.passivating;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;

@SuppressWarnings("serial")
@SessionScoped
class Vaasa implements Serializable
{
   @SuppressWarnings("unused")
   @Inject private HelsinkiLocal ejb;
}
