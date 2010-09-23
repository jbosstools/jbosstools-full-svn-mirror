package org.domain.SeamWebWarTestProject.session;

import java.util.ArrayList;
import java.util.List;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.datamodel.DataModel;
import org.jboss.seam.log.Log;
import org.jboss.seam.core.FacesMessages;

@Name("duplicate.factory.component12")
public class DuplicateFactory {
    @Factory("testFactory1") 
    public String getComp() {
   		return "";
    }

    @Factory("testFactory2") 
    public String getComp2() {
   		return "";
    }
}