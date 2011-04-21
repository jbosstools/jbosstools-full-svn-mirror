/**
 * 
 */
package org.jboss.tools.smooks.test.model11;

import junit.framework.TestCase;

import org.eclipse.emf.ecore.EPackage.Registry;
import org.jboss.tools.smooks.model.calc.CalcPackage;
import org.jboss.tools.smooks.model.common.CommonPackage;
import org.jboss.tools.smooks.model.csv12.Csv12Package;
import org.jboss.tools.smooks.model.datasource.DatasourcePackage;
import org.jboss.tools.smooks.model.dbrouting.DbroutingPackage;
import org.jboss.tools.smooks.model.edi12.Edi12Package;
import org.jboss.tools.smooks.model.esbrouting.EsbroutingPackage;
import org.jboss.tools.smooks.model.fileRouting.FileRoutingPackage;
import org.jboss.tools.smooks.model.freemarker.FreemarkerPackage;
import org.jboss.tools.smooks.model.groovy.GroovyPackage;
import org.jboss.tools.smooks.model.iorouting.IoroutingPackage;
import org.jboss.tools.smooks.model.javabean12.Javabean12Package;
import org.jboss.tools.smooks.model.jmsrouting.JmsroutingPackage;
import org.jboss.tools.smooks.model.jmsrouting12.Jmsrouting12Package;
import org.jboss.tools.smooks.model.json12.Json12Package;
import org.jboss.tools.smooks.model.medi.MEdiPackage;
import org.jboss.tools.smooks.model.persistence12.Persistence12Package;
import org.jboss.tools.smooks.model.rules10.Rules10Package;
import org.jboss.tools.smooks.model.smooks.SmooksPackage;
import org.jboss.tools.smooks.model.validation10.Validation10Package;
import org.jboss.tools.smooks.model.xsl.XslPackage;

/**
 * @author Dart
 *
 */
public class BaseTestCase extends TestCase {
	static {
		// regist emf model uri mapping 
		
		// smooks 1.2
		Registry.INSTANCE.put(Javabean12Package.eNS_URI, Javabean12Package.eINSTANCE);
		Registry.INSTANCE.put(Json12Package.eNS_URI, Json12Package.eINSTANCE);
		Registry.INSTANCE.put(Jmsrouting12Package.eNS_URI, Jmsrouting12Package.eINSTANCE);
		Registry.INSTANCE.put(Validation10Package.eNS_URI, Validation10Package.eINSTANCE);
		Registry.INSTANCE.put(Rules10Package.eNS_URI, Rules10Package.eINSTANCE);
		Registry.INSTANCE.put(Persistence12Package.eNS_URI, Persistence12Package.eINSTANCE);
		Registry.INSTANCE.put(Csv12Package.eNS_URI, Csv12Package.eINSTANCE);
		Registry.INSTANCE.put(Edi12Package.eNS_URI, Edi12Package.eINSTANCE);
		// smooks 1.1.2 and smooks 1.0
		Registry.INSTANCE.put(SmooksPackage.eNS_URI, SmooksPackage.eINSTANCE);
		Registry.INSTANCE.put(CalcPackage.eNS_URI, CalcPackage.eINSTANCE);
		Registry.INSTANCE.put(CommonPackage.eNS_URI, CommonPackage.eINSTANCE);
		Registry.INSTANCE.put(DatasourcePackage.eNS_URI, DatasourcePackage.eINSTANCE);
		Registry.INSTANCE.put(DbroutingPackage.eNS_URI, DbroutingPackage.eINSTANCE);
		Registry.INSTANCE.put(FileRoutingPackage.eNS_URI, FileRoutingPackage.eINSTANCE);

		Registry.INSTANCE.put(FreemarkerPackage.eNS_URI, FreemarkerPackage.eINSTANCE);
		Registry.INSTANCE.put(GroovyPackage.eNS_URI, GroovyPackage.eINSTANCE);
		Registry.INSTANCE.put(IoroutingPackage.eNS_URI, IoroutingPackage.eINSTANCE);
		Registry.INSTANCE.put(JmsroutingPackage.eNS_URI, JmsroutingPackage.eINSTANCE);
		Registry.INSTANCE.put(MEdiPackage.eNS_URI, MEdiPackage.eINSTANCE);
		Registry.INSTANCE.put(XslPackage.eNS_URI, XslPackage.eINSTANCE);
		Registry.INSTANCE.put(EsbroutingPackage.eNS_URI, EsbroutingPackage.eINSTANCE);
	}
	public void testNull(){//ignore
	}
}
