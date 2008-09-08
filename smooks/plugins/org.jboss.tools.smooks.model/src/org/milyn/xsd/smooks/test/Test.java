package org.milyn.xsd.smooks.test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.impl.EPackageRegistryImpl;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceImpl;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.ExtendedMetaData;
import org.eclipse.emf.ecore.util.FeatureMapUtil;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.emf.ecore.xml.type.AnyType;
import org.eclipse.emf.ecore.xml.type.SimpleAnyType;
import org.eclipse.emf.ecore.xml.type.XMLTypeDocumentRoot;
import org.eclipse.emf.ecore.xml.type.XMLTypePackage;

public class Test
{

  private static Resource createModel()
  {
    Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put(Resource.Factory.Registry.DEFAULT_EXTENSION,
        new XMIResourceFactoryImpl());

    Resource resource = new ResourceImpl(URI.createURI("test.xmi"));

    // add some anonymous content to the resource
    // e.g., an XML document consisting of
    // <test xmlns="urn:test">
    // <subTest>subTest1</subTest>
    // <subTest2 value="2"/>
    // </test>
    EPackage testPackage = ExtendedMetaData.INSTANCE.demandPackage("urn:test");
    // must add package to resource (uri=nsURI) so we can save feature refs
    Resource testPackageResource = new ResourceImpl(URI.createURI(testPackage.getNsURI()));
    testPackageResource.getContents().add(testPackage);

    EClass docEClass = ExtendedMetaData.INSTANCE.getDocumentRoot(testPackage);
    XMLTypeDocumentRoot doc = (XMLTypeDocumentRoot)EcoreUtil.create(docEClass);
    resource.getContents().add(doc);

    AnyType test = (AnyType)EcoreUtil.create(XMLTypePackage.Literals.ANY_TYPE);
    doc.getMixed().add(ExtendedMetaData.INSTANCE.demandFeature("urn:test", "test", true), test);

    SimpleAnyType subTest = (SimpleAnyType)EcoreUtil.create(XMLTypePackage.Literals.SIMPLE_ANY_TYPE);
    test.getMixed().add(ExtendedMetaData.INSTANCE.demandFeature("urn:test", "subTest", true), subTest);
    subTest.setInstanceType(XMLTypePackage.Literals.STRING);
    subTest.setValue("subTest1");

    // must use an AnyType subclass, or xsi:type won't be serialized
    // can't use SimpleAnyType as it is treated specially (xsi:type=instanceType)
    EClass subTest2EClass = (EClass)ExtendedMetaData.INSTANCE.demandType("urn:test", "SubTest2");
    AnyType subTest2 = (AnyType)EcoreUtil.create(subTest2EClass);
    test.getMixed().add(ExtendedMetaData.INSTANCE.demandFeature("urn:test", "subTest2", true), subTest2);
    subTest2.getAnyAttribute().add(ExtendedMetaData.INSTANCE.demandFeature("urn:test", "value", false), new Integer(2));

    ResourceSet resourceSet = new ResourceSetImpl();
    final ExtendedMetaData extendedMetaData = ExtendedMetaData.INSTANCE;
    Map<?, ?> options = Collections.singletonMap(XMLResource.OPTION_EXTENDED_META_DATA, extendedMetaData);
    resourceSet.getLoadOptions().putAll(options);

    @SuppressWarnings("serial")
    EPackage.Registry registry = new EPackageRegistryImpl(EPackage.Registry.INSTANCE)
      {
        @Override
        protected EPackage delegatedGetEPackage(String nsURI)
        {
          EPackage ePackage = super.delegatedGetEPackage(nsURI);
          if (ePackage != null)
            return ePackage;

          for (EPackage demandPackage : extendedMetaData.demandedPackages())
          {
            if (nsURI.equals(demandPackage.getNsURI()))
              return demandPackage;
          }

          return null;
        }

        @Override
        protected EFactory delegatedGetEFactory(String nsURI)
        {
          EFactory factory = super.delegatedGetEFactory(nsURI);
          if (factory != null)
            return factory;

          EPackage ePackage = getEPackage(nsURI);
          if (ePackage != null)
            return ePackage.getEFactoryInstance();

          return null;
        }
      };

    resourceSet.setPackageRegistry(registry);
    resourceSet.getResources().add(resource);
    return resource;
  }

  private static XMLTypeDocumentRoot getTestDocument(Resource resource)
  {
    return (XMLTypeDocumentRoot)resource.getContents().get(0);
  }

  private static AnyType getTest(Resource resource)
  {
    XMLTypeDocumentRoot doc = getTestDocument(resource);
    EStructuralFeature testFeature = ExtendedMetaData.INSTANCE.demandFeature("urn:test", "test", true);
    return (AnyType)doc.getMixed().get(testFeature, true);
  }

  private static SimpleAnyType getSubTest(Resource resource, int index)
  {
    AnyType test = getTest(resource);
    EStructuralFeature subTestFeature = ExtendedMetaData.INSTANCE.demandFeature("urn:test", "subTest", true);
    @SuppressWarnings("unchecked")
    List subTests = (List)test.getMixed().get(subTestFeature, true);
    return (SimpleAnyType)subTests.get(index);
  }

  private static AnyType getSubTest2(Resource resource, int index)
  {
    AnyType test = getTest(resource);
    EStructuralFeature subTest2Feature = ExtendedMetaData.INSTANCE.demandFeature("urn:test", "subTest2", true);
    @SuppressWarnings("unchecked")
    List subTests = (List)test.getMixed().get(subTest2Feature, true);
    return (AnyType)subTests.get(index);
  }

  public static void main(String[] args) throws Exception
  {
//    Resource model = createModel();
//    ChangeRecorder recorder = new ChangeRecorder(model);
//    SimpleAnyType subTest = getSubTest(model, 0);
//    subTest.setValue("Changed value");
//    EcoreUtil.remove(getSubTest2(model, 0));
//    ChangeDescription cd = recorder.endRecording();
//
//    ResourceSet resourceSet = model.getResourceSet();
//    Resource resource = resourceSet.createResource(URI.createURI("changes.xmi"));
//    resource.getContents().add(cd);
//
//    // in all feature map entries, replace feature names with their URIs
//    for (Map.Entry<EObject, EList<FeatureChange>> entry : cd.getObjectChanges())
//    {
//      for (FeatureChange change : entry.getValue())
//      {
//        EStructuralFeature feature = change.getFeature();
//        if (!FeatureMapUtil.isFeatureMap(feature))
//          continue;
//
//        for (ListChange listChange : change.getListChanges())
//        {
//          for (FeatureMapEntry fme : listChange.getFeatureMapEntryValues())
//          {
//            EStructuralFeature fmeFeature = fme.getFeature();
//            fme.unsetFeature();
//            fme.setFeatureName(EcoreUtil.getURI(fmeFeature).toString());
//          }
//        }
//      }
//    }
//
//    Map<String, Object> options = new HashMap<String, Object>();
//    // must set this option to avoid NPE in XMLSaveImpl, line 1023
//    options.put(XMLResource.OPTION_EXTENDED_META_DATA, ExtendedMetaData.INSTANCE);
//    // must set this option to avoid a ClassNotFound in XMLHandler, line 2097
//    options.put(XMLResource.OPTION_RECORD_UNKNOWN_FEATURE, Boolean.TRUE);
//
//    ByteArrayOutputStream out = new ByteArrayOutputStream();
//    resource.save(out, options);
//    resource.unload();
//
//    resource.load(new ByteArrayInputStream(out.toByteArray()), options);
//
//    cd = (ChangeDescription)resource.getContents().get(0);
//
//    // in all feature map entries, resolve features using their URIs (in featureName)
//    for (Map.Entry<EObject, EList<FeatureChange>> entry : cd.getObjectChanges())
//    {
//      for (FeatureChange change : entry.getValue())
//      {
//        EStructuralFeature feature = change.getFeature();
//        if (!FeatureMapUtil.isFeatureMap(feature))
//          continue;
//
//        for (ListChange listChange : change.getListChanges())
//        {
//          for (FeatureMapEntry fme : listChange.getFeatureMapEntryValues())
//          {
//            EStructuralFeature fmeFeature = fme.getFeature();
//            if (fmeFeature != null)
//              continue;
//
//            URI featureURI = URI.createURI(fme.getFeatureName());
//            fmeFeature = (EStructuralFeature)resourceSet.getEObject(featureURI, true);
//            fme.setFeature(fmeFeature);
//          }
//        }
//      }
//    }
//
//    cd.apply();
//
//    assert EcoreUtil.equals(model.getContents().get(0), createModel().getContents().get(0));
  }
}
