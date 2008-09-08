package org.milyn.xsd.smooks.test;

import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EPackage.Registry;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.ExtendedMetaData;
import org.eclipse.emf.ecore.xml.type.AnyType;
import org.eclipse.emf.ecore.xml.type.XMLTypePackage;
import org.milyn.xsd.smooks.DocumentRoot;
import org.milyn.xsd.smooks.ParamType;
import org.milyn.xsd.smooks.ResourceConfigType;
import org.milyn.xsd.smooks.SmooksPackage;
import org.milyn.xsd.smooks.SmooksResourceListType;
import org.milyn.xsd.smooks.util.SmooksResourceFactoryImpl;

public class Tes extends TestCase {
	public void testModel(){
		Registry.INSTANCE.put(SmooksPackage.eNS_URI, SmooksPackage.eINSTANCE);
		Resource r = new SmooksResourceFactoryImpl().createResource(URI.createFileURI("/home/user/smooks-config.xml"));
		try {
			r.load(Collections.EMPTY_MAP);
			DocumentRoot obj = (DocumentRoot) r.getContents().get(0);
			SmooksResourceListType rl  = obj.getSmooksResourceList();
			List rec = rl.getAbstractResourceConfig();
			for (Iterator iterator = rec.iterator(); iterator.hasNext();) {
				ResourceConfigType rct = (ResourceConfigType) iterator.next();
				List pl = rct.getParam();
				for (Iterator iterator2 = pl.iterator(); iterator2.hasNext();) {
					ParamType pt = (ParamType) iterator2.next();
//					pt.get
					
					AnyType a = (AnyType) EcoreUtil.create(XMLTypePackage.Literals.ANY_TYPE);
					System.out.println(XMLTypePackage.Literals.XML_TYPE_DOCUMENT_ROOT__TEXT);
//					XMLTypePackage.eINSTANCE.getx
					pt.getMixed().add(ExtendedMetaData.INSTANCE.demandFeature(SmooksPackage.eNS_URI, "denny", true), a);
					System.out.println(pt);
//					pt.getMixed().add(XMLTypePackage.Literals.XML_TYPE_DOCUMENT_ROOT__TEXT, arg1)
				}
			}
//			System.out.println(obj);
//			r.getContents().clear();
//			r.getContents().add(rl);
			r.save(Collections.EMPTY_MAP);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
