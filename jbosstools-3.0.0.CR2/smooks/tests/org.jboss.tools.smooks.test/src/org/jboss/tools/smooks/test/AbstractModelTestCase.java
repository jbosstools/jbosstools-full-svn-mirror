/**
 * 
 */
package org.jboss.tools.smooks.test;

import java.util.HashMap;

import junit.framework.TestCase;

import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.ecore.EPackage.Registry;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.impl.XMLResourceFactoryImpl;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.resource.ResourceItemProviderAdapterFactory;
import org.jboss.tools.smooks.analyzer.IMappingAnalyzer;
import org.jboss.tools.smooks.analyzer.ISourceModelAnalyzer;
import org.jboss.tools.smooks.analyzer.ITargetModelAnalyzer;
import org.jboss.tools.smooks.graphical.GraphInformations;
import org.jboss.tools.smooks.graphical.GraphicalPackage;
import org.jboss.tools.smooks.model.SmooksPackage;
import org.jboss.tools.smooks.model.provider.SmooksItemProviderAdapterFactory;
import org.jboss.tools.smooks.model.util.SmooksResourceFactoryImpl;

/**
 * @author Dart
 * 
 */
public abstract class AbstractModelTestCase extends TestCase {

	protected ISourceModelAnalyzer sourceModelAnalyzer = newSourceModelAnalyzer();
	protected ITargetModelAnalyzer targetModelAnalyzer = newTargetModelAnalyzer();
	protected IMappingAnalyzer connectionsAnalyzer = newConnectionModelAnalyzer();

	protected GraphInformations graph;

	public GraphInformations getGraph() {
		return graph;
	}

	protected abstract IMappingAnalyzer newConnectionModelAnalyzer();

	protected abstract ITargetModelAnalyzer newTargetModelAnalyzer();

	protected abstract ISourceModelAnalyzer newSourceModelAnalyzer();

	public void setGraph(GraphInformations graph) {
		this.graph = graph;
	}

	public ComposedAdapterFactory getAdapterFactory() {
		return adapterFactory;
	}

	public void setAdapterFactory(ComposedAdapterFactory adapterFactory) {
		this.adapterFactory = adapterFactory;
	}

	public AdapterFactoryEditingDomain getEditingDomain() {
		return editingDomain;
	}

	public void setEditingDomain(AdapterFactoryEditingDomain editingDomain) {
		this.editingDomain = editingDomain;
	}

	public Resource getSmooksResource() {
		return smooksResource;
	}

	public void setSmooksResource(Resource smooksResource) {
		this.smooksResource = smooksResource;
	}

	protected ComposedAdapterFactory adapterFactory;
	protected AdapterFactoryEditingDomain editingDomain;
	protected Resource smooksResource;
	protected Resource graphResource;

	public Resource getGraphResource() {
		return graphResource;
	}

	public void setGraphResource(Resource graphResource) {
		this.graphResource = graphResource;
	}

	public AbstractModelTestCase() {
	}
	
	

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		adapterFactory = new ComposedAdapterFactory(
				ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
		adapterFactory
				.addAdapterFactory(new ResourceItemProviderAdapterFactory());
		adapterFactory
				.addAdapterFactory(new SmooksItemProviderAdapterFactory());
		editingDomain = new AdapterFactoryEditingDomain(adapterFactory,
				createCommandStack(), new HashMap<Resource, Boolean>());
		Registry.INSTANCE.put(GraphicalPackage.eNS_URI,
				GraphicalPackage.eINSTANCE);
		Registry.INSTANCE.put(SmooksPackage.eNS_URI, SmooksPackage.eINSTANCE);

		smooksResource = new SmooksResourceFactoryImpl().createResource(null);
		graphResource = new XMLResourceFactoryImpl().createResource(null);
		loadResources();
	}

	public abstract void loadResources() throws RuntimeException;

	protected CommandStack createCommandStack() {
		return new BasicCommandStack();
	}
}
