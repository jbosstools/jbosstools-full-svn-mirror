package org.hibernate.eclipse.jdt.ui.test.hbmexporter;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import junit.framework.TestCase;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.osgi.util.NLS;
import org.hibernate.eclipse.console.test.ConsoleTestMessages;
import org.hibernate.eclipse.console.test.project.TestProject;
import org.hibernate.eclipse.console.test.utils.FilesTransfer;
import org.hibernate.eclipse.console.utils.ProjectUtils;
import org.hibernate.eclipse.jdt.ui.internal.jpa.collect.AllEntitiesInfoCollector;
import org.hibernate.eclipse.jdt.ui.internal.jpa.common.Utils;
import org.hibernate.eclipse.jdt.ui.internal.jpa.process.AllEntitiesProcessor;
import org.hibernate.eclipse.jdt.ui.test.HibernateJDTuiTestPlugin;
import org.hibernate.eclipse.jdt.ui.wizards.ConfigurationActor;
import org.hibernate.mediator.stubs.ArrayStub;
import org.hibernate.mediator.stubs.ConfigurationStub;
import org.hibernate.mediator.stubs.IntegerTypeStub;
import org.hibernate.mediator.stubs.ListStub;
import org.hibernate.mediator.stubs.ManyToOneStub;
import org.hibernate.mediator.stubs.MapStub;
import org.hibernate.mediator.stubs.OneToManyStub;
import org.hibernate.mediator.stubs.PersistentClassStub;
import org.hibernate.mediator.stubs.PrimitiveArrayStub;
import org.hibernate.mediator.stubs.PropertyStub;
import org.hibernate.mediator.stubs.SetStub;
import org.hibernate.mediator.stubs.ValueStub;

public class HbmExporterTest extends TestCase {
	
	public static final String PROJECT_NAME = "TestProject"; //$NON-NLS-1$
	public static final String RESOURCE_PATH = "res/hbm/".replaceAll("//", File.separator); //$NON-NLS-1$ //$NON-NLS-2$
	public static final String TESTRESOURCE_PATH = "testresources"; //$NON-NLS-1$

	protected AllEntitiesInfoCollector collector = new AllEntitiesInfoCollector();
	protected AllEntitiesProcessor processor = new AllEntitiesProcessor();

	protected TestProject project = null;
	
	protected void setUp() throws Exception {
		try {
			createTestProject();
		} catch (JavaModelException e1) {
			fail(e1.getMessage());
		} catch (CoreException e1) {
			fail(e1.getMessage());
		} catch (IOException e1) {
			fail(e1.getMessage());
		}
		assertNotNull(project);
		IJavaProject javaProject = ProjectUtils.findJavaProject(PROJECT_NAME);
		assertNotNull(javaProject);
		try {
			javaProject.getProject().open(null);
		} catch (CoreException e) {
			fail(e.getMessage());
		}
	}
	
	/**
	 * Method returns Configuration object for selected ICompilationUnits.
	 * Fails if configuration is null.
	 * @return
	 */
	protected ConfigurationStub getConfigurationFor(String... cuNames){
		Set<ICompilationUnit> selectionCU = new HashSet<ICompilationUnit>();
		for (int i = 0; i < cuNames.length; i++) {
			ICompilationUnit icu = Utils.findCompilationUnit(project.getIJavaProject(),
					cuNames[i]);
			assertNotNull(icu);
			selectionCU.add(icu);
		}
		ConfigurationActor actor = new ConfigurationActor(selectionCU);
		Map<IJavaProject, ConfigurationStub> configurations = actor.createConfigurations(Integer.MAX_VALUE);
		assertEquals(1, configurations.size());
		ConfigurationStub config = configurations.get(project.getIJavaProject());
		assertNotNull(config);
		return config;
	}
	
	protected void checkClassesMaped(ConfigurationStub config, String... classesNames){
		for (int i = 0; i < classesNames.length; i++) {
			assertNotNull(config.getClassMapping(classesNames[i]));
		}
	}
	
	public void testId(){
		ConfigurationStub config = getConfigurationFor("pack.A"); //$NON-NLS-1$
		checkClassesMaped(config, "pack.A", "pack.B"); //$NON-NLS-1$ //$NON-NLS-2$
		PersistentClassStub a = config.getClassMapping("pack.A"); //$NON-NLS-1$
		PersistentClassStub b = config.getClassMapping("pack.B"); //$NON-NLS-1$
		
		PropertyStub aId= a.getIdentifierProperty();
		PropertyStub bId= b.getIdentifierProperty();
		assertNotNull(aId);
		assertNotNull(bId);
		assertEquals("id", aId.getName()); //$NON-NLS-1$
		assertEquals("id", bId.getName()); //$NON-NLS-1$
	}
	
	public void testProperty(){
		ConfigurationStub config = getConfigurationFor("pack.A"); //$NON-NLS-1$
		checkClassesMaped(config, "pack.A", "pack.B"); //$NON-NLS-1$ //$NON-NLS-2$
		PersistentClassStub a = config.getClassMapping("pack.A"); //$NON-NLS-1$
		
		PropertyStub prop = a.getProperty("prop"); //$NON-NLS-1$
		ValueStub value = prop.getValue();
		assertNotNull(value);
		assertTrue("Expected to get ManyToOne-type mapping", value.getClass()== ManyToOneStub.class); //$NON-NLS-1$
		ManyToOneStub mto = (ManyToOneStub)value;
		assertEquals("pack.B", mto.getTypeName()); //$NON-NLS-1$
	}
	
	public void testArray(){
		ConfigurationStub config = getConfigurationFor("pack.A"); //$NON-NLS-1$
		checkClassesMaped(config, "pack.A", "pack.B"); //$NON-NLS-1$ //$NON-NLS-2$
		PersistentClassStub a = config.getClassMapping("pack.A"); //$NON-NLS-1$
		PersistentClassStub b = config.getClassMapping("pack.B"); //$NON-NLS-1$
		
		PropertyStub bs = a.getProperty("bs"); //$NON-NLS-1$
		ValueStub value = bs.getValue();
		assertNotNull(value);
		assertTrue("Expected to get Array-type mapping", value.getClass()==ArrayStub.class); //$NON-NLS-1$
		ArrayStub ar = (ArrayStub)value;
		assertEquals("pack.B", ar.getElementClassName()); //$NON-NLS-1$
		assertTrue("Expected to get one-to-many array's element type", //$NON-NLS-1$
				ar.getElement().getClass() == OneToManyStub.class);
		
		PropertyStub testIntArray = b.getProperty("testIntArray"); //$NON-NLS-1$
		assertNotNull(testIntArray);
		value = testIntArray.getValue();
		assertNotNull(value);
		assertTrue("Expected to get PrimitiveArray-type mapping", //$NON-NLS-1$  
				value.getClass()==PrimitiveArrayStub.class);
		PrimitiveArrayStub pAr = (PrimitiveArrayStub) value;
		assertNotNull(pAr.getElement());
		assertTrue("Expected to get int-type primitive array", pAr.getElement().getType().getClass()==IntegerTypeStub.class); //$NON-NLS-1$
	}
	
	public void testList(){
		ConfigurationStub config = getConfigurationFor("pack.A"); //$NON-NLS-1$
		checkClassesMaped(config, "pack.A", "pack.B"); //$NON-NLS-1$ //$NON-NLS-2$
		PersistentClassStub a = config.getClassMapping("pack.A"); //$NON-NLS-1$
		PersistentClassStub b = config.getClassMapping("pack.B"); //$NON-NLS-1$
		
		PropertyStub listProp = a.getProperty("list"); //$NON-NLS-1$
		ValueStub value = listProp.getValue();
		assertNotNull(value);
		assertTrue("Expected to get List-type mapping", //$NON-NLS-1$ 
				value.getClass()==ListStub.class);
		ListStub list = (ListStub)value;
		assertTrue(list.getElement() instanceof OneToManyStub);
		assertTrue(list.getCollectionTable().equals(b.getTable()));
		assertNotNull(list.getIndex());
		assertNotNull(list.getKey());
	}
	
	public void testSet(){
		ConfigurationStub config = getConfigurationFor("pack.A"); //$NON-NLS-1$
		checkClassesMaped(config, "pack.A", "pack.B"); //$NON-NLS-1$ //$NON-NLS-2$
		PersistentClassStub a = config.getClassMapping("pack.A"); //$NON-NLS-1$
		PersistentClassStub b = config.getClassMapping("pack.B"); //$NON-NLS-1$
		
		PropertyStub setProp = a.getProperty("set"); //$NON-NLS-1$
		ValueStub value = setProp.getValue();
		assertNotNull(value);
		assertTrue("Expected to get Set-type mapping",  //$NON-NLS-1$
				value.getClass()==SetStub.class);
		SetStub set = (SetStub)value;
		assertTrue(set.getElement() instanceof OneToManyStub);
		assertTrue(set.getCollectionTable().equals(b.getTable()));
		assertNotNull(set.getKey());
	}
	
	public void testMap(){
		ConfigurationStub config = getConfigurationFor("pack.A"); //$NON-NLS-1$
		checkClassesMaped(config, "pack.A", "pack.B"); //$NON-NLS-1$ //$NON-NLS-2$
		PersistentClassStub a = config.getClassMapping("pack.A"); //$NON-NLS-1$
		PersistentClassStub b = config.getClassMapping("pack.B"); //$NON-NLS-1$
		
		PropertyStub mapValue = a.getProperty("mapValue"); //$NON-NLS-1$
		ValueStub value = mapValue.getValue();
		assertNotNull(value);
		assertTrue("Expected to get Map-type mapping", //$NON-NLS-1$ 
				value.getClass()==MapStub.class);
		MapStub map = (MapStub)value;
		assertTrue(map.getElement() instanceof OneToManyStub);
		assertTrue(map.getCollectionTable().equals(b.getTable()));
		assertNotNull(map.getKey());
		assertEquals("string", map.getKey().getType().getName()); //$NON-NLS-1$
	}
	

	protected void createTestProject() throws JavaModelException,
			CoreException, IOException {
		project = new TestProject(PROJECT_NAME);
		File resourceFolder = getResourceItem(RESOURCE_PATH);
		if (!resourceFolder.exists()) {
			String out = NLS.bind(
					ConsoleTestMessages.MappingTestProject_folder_not_found,
					RESOURCE_PATH);
			throw new RuntimeException(out);
		}
		IPackageFragmentRoot sourceFolder = project.createSourceFolder();
		FilesTransfer.copyFolder(resourceFolder, (IFolder) sourceFolder
				.getResource());
		File resourceFolderLib = getResourceItem(TESTRESOURCE_PATH);
		if (!resourceFolderLib.exists()) {
			String out = NLS.bind(
					ConsoleTestMessages.MappingTestProject_folder_not_found,
					RESOURCE_PATH);
			throw new RuntimeException(out);
		}
		List<IPath> libs = project.copyLibs2(resourceFolderLib.getAbsolutePath());
		project.generateClassPath(libs, sourceFolder);
	}
	
	protected File getResourceItem(String strResPath) throws IOException {
		IPath resourcePath = new Path(strResPath);
		File resourceFolder = resourcePath.toFile();
		URL entry = HibernateJDTuiTestPlugin.getDefault().getBundle().getEntry(
				strResPath);
		URL resProject = FileLocator.resolve(entry);
		String tplPrjLcStr = FileLocator.resolve(resProject).getFile();
		resourceFolder = new File(tplPrjLcStr);
		return resourceFolder;
	}
	
	protected void tearDown() throws Exception {
		assertNotNull(project);
		project.deleteIProject();
		project = null;
	}

}
