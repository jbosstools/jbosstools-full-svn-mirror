package org.jboss.tools.seam.core.test;

import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.jboss.tools.common.xml.XMLUtilities;
import org.jboss.tools.seam.core.ISeamComponent;
import org.jboss.tools.seam.core.ISeamComponentDeclaration;
import org.jboss.tools.seam.core.ISeamFactory;
import org.jboss.tools.seam.core.ISeamProject;
import org.jboss.tools.seam.core.event.Change;
import org.jboss.tools.seam.internal.core.SeamAnnotatedFactory;
import org.jboss.tools.seam.internal.core.SeamJavaComponentDeclaration;
import org.jboss.tools.seam.internal.core.SeamProject;
import org.jboss.tools.seam.internal.core.SeamPropertiesDeclaration;
import org.jboss.tools.seam.internal.core.SeamXMLConstants;
import org.jboss.tools.seam.internal.core.SeamXmlComponentDeclaration;
import org.jboss.tools.seam.internal.core.SeamXmlFactory;
import org.jboss.tools.test.util.JUnitUtils;
import org.jboss.tools.test.util.ResourcesUtils;
import org.jboss.tools.test.util.xpl.EditorTestHelper;
import org.w3c.dom.Element;

import junit.framework.TestCase;

public class SerializationTest extends TestCase {
	IProject project = null;
	boolean makeCopy = true;
	
	public SerializationTest() {
		super("Seam Serialization test");
	}

	protected void setUp() throws Exception {
		project = ResourcesUtils.importProject(
				"org.jboss.tools.seam.core.test","/projects/TestScanner" , new NullProgressMonitor());
		project.refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor());
		EditorTestHelper.joinBackgroundActivities();
	}

	private ISeamProject getSeamProject() {
		ISeamProject seamProject = null;
		try {
			seamProject = (ISeamProject)project.getNature(SeamProject.NATURE_ID);
		} catch (Exception e) {
			JUnitUtils.fail("Cannot get seam nature.",e);
		}
		assertNotNull("Seam project is null", seamProject);
		return seamProject;
	}
	
	public void testXMLSerialization() {
		Element root = XMLUtilities.createDocumentElement("root");
		ISeamProject seamProject = getSeamProject();
		Set<ISeamComponent> cs = seamProject.getComponents();
		for (ISeamComponent c: cs) {
			Set<ISeamComponentDeclaration> ds = c.getAllDeclarations();
			for (ISeamComponentDeclaration d: ds) {
				Properties context = new Properties();
				context.put(SeamXMLConstants.ATTR_PATH, d.getSourcePath());
				Element e = d.toXML(root, context);
				String cls = e.getAttribute(SeamXMLConstants.ATTR_CLASS);
				ISeamComponentDeclaration d2 = null;
				if(SeamXMLConstants.CLS_JAVA.equals(cls)) {
					d2 = new SeamJavaComponentDeclaration();
				} else if(SeamXMLConstants.CLS_XML.equals(cls)) {
					d2 = new SeamXmlComponentDeclaration();
				} else if(SeamXMLConstants.CLS_PROPERTIES.equals(cls)) {
					d2 = new SeamPropertiesDeclaration();
				}
				assertTrue("Cannot restore declaration " + d.getName() + " " + d.getClass().getName(), d2 != null);
				d2.loadXML(e, context);
				List<Change> changes = d2.merge(d);
				if(changes != null && changes.size() > 0) {
					//TODO Analyze changes to fail test with messages 
					//     that will help readily find the problem.
					System.out.println(d.getName() + " " + d.getClass().getName() + " " + changes.size());
				}
				root.removeChild(e);
			}
		}
		Set<ISeamFactory> fs = seamProject.getFactories();
		for (ISeamFactory f: fs) {
			Properties context = new Properties();
			context.put(SeamXMLConstants.ATTR_PATH, f.getSourcePath());
			Element e = f.toXML(root, context);
			String cls = e.getAttribute(SeamXMLConstants.ATTR_CLASS);
			ISeamFactory f2 = null;
			if(SeamXMLConstants.CLS_XML.equals(cls)) {
				f2 = new SeamXmlFactory();
			} else if(SeamXMLConstants.CLS_JAVA.equals(cls)) {
				f2 = new SeamAnnotatedFactory();
			}
			assertTrue("Cannot restore factory declaration " + f.getName() + " " + f.getClass().getName(), f2 != null);
			f2.loadXML(e, context);
			List<Change> changes = f2.merge(f);
			if(changes != null && changes.size() > 0) {
				//TODO Analyze changes to fail test with messages 
				//     that will help readily find the problem.
				System.out.println(f.getName() + " " + f.getClass().getName() + " " + changes.size());
			}
		}
		
	}

}
