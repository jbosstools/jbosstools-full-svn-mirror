package org.jboss.tools.portlet.operations;

import static org.eclipse.jst.j2ee.internal.common.operations.INewJavaClassDataModelProperties.ABSTRACT_METHODS;
import static org.eclipse.jst.j2ee.internal.web.operations.INewServletClassDataModelProperties.DESTROY;
import static org.jboss.tools.portlet.ui.INewPortletClassDataModelProperties.DO_VIEW;
import static org.jboss.tools.portlet.ui.INewPortletClassDataModelProperties.DO_EDIT;
import static org.jboss.tools.portlet.ui.INewPortletClassDataModelProperties.DO_HELP;
import static org.jboss.tools.portlet.ui.INewPortletClassDataModelProperties.DO_DISPATCH;
import static org.jboss.tools.portlet.ui.INewPortletClassDataModelProperties.PROCESS_ACTION;
import static org.jboss.tools.portlet.ui.INewPortletClassDataModelProperties.RENDER;
import static org.jboss.tools.portlet.ui.INewPortletClassDataModelProperties.GET_PORTLET_CONFIG;
import static org.eclipse.jst.j2ee.internal.web.operations.INewServletClassDataModelProperties.INIT;
import static org.eclipse.jst.j2ee.internal.web.operations.INewServletClassDataModelProperties.INIT_PARAM;
import static org.jboss.tools.portlet.ui.IPortletUIConstants.DESTROY_SIGNATURE;
import static org.jboss.tools.portlet.ui.IPortletUIConstants.PORTLET_INIT_SIGNATURE;
import static org.jboss.tools.portlet.ui.IPortletUIConstants.DO_DISPATCH_SIGNATURE;
import static org.jboss.tools.portlet.ui.IPortletUIConstants.DO_EDIT_SIGNATURE;
import static org.jboss.tools.portlet.ui.IPortletUIConstants.DO_VIEW_SIGNATURE;
import static org.jboss.tools.portlet.ui.IPortletUIConstants.DO_HELP_SIGNATURE;
import static org.jboss.tools.portlet.ui.IPortletUIConstants.GET_PORTLET_CONFIG_SIGNATURE;
import static org.jboss.tools.portlet.ui.IPortletUIConstants.PROCESS_ACTION_SIGNATURE;
import static org.jboss.tools.portlet.ui.IPortletUIConstants.RENDER_SIGNATURE;

import static org.jboss.tools.portlet.ui.IPortletUIConstants.METHOD_DESTROY;
import static org.jboss.tools.portlet.ui.IPortletUIConstants.METHOD_INIT;
import static org.jboss.tools.portlet.ui.IPortletUIConstants.METHOD_GET_PORTLET_CONFIG;
import static org.jboss.tools.portlet.ui.IPortletUIConstants.METHOD_DO_DISPATCH;
import static org.jboss.tools.portlet.ui.IPortletUIConstants.METHOD_DO_EDIT;
import static org.jboss.tools.portlet.ui.IPortletUIConstants.METHOD_DO_HELP;
import static org.jboss.tools.portlet.ui.IPortletUIConstants.METHOD_DO_VIEW;
import static org.jboss.tools.portlet.ui.IPortletUIConstants.METHOD_PROCESS_ACTION;
import static org.jboss.tools.portlet.ui.IPortletUIConstants.METHOD_RENDER;
import static org.jboss.tools.portlet.ui.IPortletUIConstants.METHOD_TO_STRING;

import static org.jboss.tools.portlet.ui.IPortletUIConstants.QUALIFIED_PORTLET_CONFIG;
import static org.jboss.tools.portlet.ui.IPortletUIConstants.QUALIFIED_PORTLET_EXCEPTION;
import static org.jboss.tools.portlet.ui.IPortletUIConstants.QUALIFIED_IO_EXCEPTION;
import static org.jboss.tools.portlet.ui.IPortletUIConstants.QUALIFIED_SECURITY_EXCEPTION;
import static org.jboss.tools.portlet.ui.IPortletUIConstants.QUALIFIED_UNAVALIABLE_EXCEPTION;
import static org.jboss.tools.portlet.ui.IPortletUIConstants.QUALIFIED_PRINTWRITER;

import static org.jboss.tools.portlet.ui.IPortletUIConstants.QUALIFIED_PORTLET_REQUEST;
import static org.jboss.tools.portlet.ui.IPortletUIConstants.QUALIFIED_PORTLET_RESPONSE;

import static org.jboss.tools.portlet.ui.IPortletUIConstants.QUALIFIED_ACTION_REQUEST;
import static org.jboss.tools.portlet.ui.IPortletUIConstants.QUALIFIED_ACTION_RESPONSE;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jst.j2ee.internal.common.operations.Method;
import org.eclipse.jst.j2ee.internal.web.operations.CreateWebClassTemplateModel;
import org.eclipse.wst.common.frameworks.datamodel.IDataModel;

/**
 * @author snjeza
 */
public class CreatePortletTemplateModel extends CreateWebClassTemplateModel {

	public static final int NAME = 0;
	public static final int VALUE = 1;
	public static final int DESCRIPTION = 2;

	public CreatePortletTemplateModel(IDataModel dataModel) {
		super(dataModel);
	}

	public Collection<String> getImports() {
		Collection<String> collection = super.getImports();

		if (shouldGenInit()) {
			collection.add(QUALIFIED_PORTLET_EXCEPTION);
		}

		if (shouldGenGetPortletConfig()) {
			collection.add(QUALIFIED_PORTLET_CONFIG);
		}

		if (shouldGenDoHelp() || shouldGenDoEdit()
				|| shouldGenDoDispatch() || shouldGenRender()) {

			collection.add(QUALIFIED_PORTLET_REQUEST);
			collection.add(QUALIFIED_PORTLET_RESPONSE);
			collection.add(QUALIFIED_PORTLET_EXCEPTION);
			collection.add(QUALIFIED_IO_EXCEPTION);
			collection.add(QUALIFIED_SECURITY_EXCEPTION);
		}
		
		if (shouldGenDoView()) {
			collection.add(QUALIFIED_PORTLET_REQUEST);
			collection.add(QUALIFIED_PORTLET_RESPONSE);
			collection.add(QUALIFIED_PORTLET_EXCEPTION);
			collection.add(QUALIFIED_IO_EXCEPTION);
			collection.add(QUALIFIED_UNAVALIABLE_EXCEPTION);
			collection.add(QUALIFIED_PRINTWRITER);
		}
		
		if (shouldGenProcessAction()) {
			collection.add(QUALIFIED_ACTION_REQUEST);
			collection.add(QUALIFIED_ACTION_RESPONSE);
			collection.add(QUALIFIED_PORTLET_EXCEPTION);
			collection.add(QUALIFIED_IO_EXCEPTION);
			collection.add(QUALIFIED_SECURITY_EXCEPTION);
		}

		return collection;
	}

	public boolean shouldGenInit() {
		return implementImplementedMethod(METHOD_INIT);
	}

	public boolean shouldGenDestroy() {
		return implementImplementedMethod(METHOD_DESTROY);
	}

	public boolean shouldGenGetPortletConfig() {
		return implementImplementedMethod(METHOD_GET_PORTLET_CONFIG);
	}

	public boolean shouldGenDoView() {
		return implementImplementedMethod(METHOD_DO_VIEW);
	}

	public boolean shouldGenDoEdit() {
		return implementImplementedMethod(METHOD_DO_EDIT);
	}

	public boolean shouldGenDoHelp() {
		return implementImplementedMethod(METHOD_DO_HELP);
	}

	public boolean shouldGenDoDispatch() {
		return implementImplementedMethod(METHOD_DO_DISPATCH);
	}

	public boolean shouldGenRender() {
		return implementImplementedMethod(METHOD_RENDER);
	}

	public boolean shouldGenProcessAction() {
		return implementImplementedMethod(METHOD_PROCESS_ACTION);
	}

	public boolean shouldGenToString() {
		return implementImplementedMethod(METHOD_TO_STRING);
	}

	public boolean isGenericPortletSuperclass() {
		return true;
	}

	public List<String[]> getInitParams() {
		return (List) dataModel.getProperty(INIT_PARAM);
	}

	public String getInitParam(int index, int type) {
		List<String[]> params = getInitParams();
		if (index < params.size()) {
			String[] stringArray = params.get(index);
			return stringArray[type];
		}
		return null;
	}

	protected boolean implementImplementedMethod(String methodName) {
		if (dataModel.getBooleanProperty(ABSTRACT_METHODS)) {
			if (methodName.equals(METHOD_INIT))
				return dataModel.getBooleanProperty(INIT);
			else if (methodName.equals(METHOD_DESTROY))
				return dataModel.getBooleanProperty(DESTROY);
			else if (methodName.equals(METHOD_GET_PORTLET_CONFIG))
				return dataModel.getBooleanProperty(GET_PORTLET_CONFIG);
			else if (methodName.equals(METHOD_DO_EDIT))
				return dataModel.getBooleanProperty(DO_EDIT);
			else if (methodName.equals(METHOD_DO_VIEW))
				return dataModel.getBooleanProperty(DO_VIEW);
			else if (methodName.equals(METHOD_DO_HELP))
				return dataModel.getBooleanProperty(DO_HELP);
			else if (methodName.equals(METHOD_DO_DISPATCH))
				return dataModel.getBooleanProperty(DO_DISPATCH);
			else if (methodName.equals(METHOD_RENDER))
				return dataModel.getBooleanProperty(RENDER);
			else if (methodName.equals(METHOD_PROCESS_ACTION))
				return dataModel.getBooleanProperty(PROCESS_ACTION);
		}
		return false;
	}

	@Override
	public Collection<Method> getUnimplementedMethods() {
		Collection<Method> unimplementedMethods = super
				.getUnimplementedMethods();
		Iterator<Method> iterator = unimplementedMethods.iterator();

		while (iterator.hasNext()) {
			Method method = iterator.next();
			if ((METHOD_INIT.equals(method.getName()) && PORTLET_INIT_SIGNATURE
					.equals(method.getSignature()))
					|| (METHOD_DESTROY.equals(method.getName()) && DESTROY_SIGNATURE
							.equals(method.getSignature()))
					|| (METHOD_GET_PORTLET_CONFIG.equals(method.getName()) && GET_PORTLET_CONFIG_SIGNATURE
							.equals(method.getSignature()))
					|| (METHOD_DO_VIEW.equals(method.getName()) && DO_VIEW_SIGNATURE
							.equals(method.getSignature()))
					|| (METHOD_DO_EDIT.equals(method.getName()) && DO_EDIT_SIGNATURE
							.equals(method.getSignature()))
					|| (METHOD_DO_HELP.equals(method.getName()) && DO_HELP_SIGNATURE
							.equals(method.getSignature()))
					|| (METHOD_DO_DISPATCH.equals(method.getName()) && DO_DISPATCH_SIGNATURE
							.equals(method.getSignature()))
					|| (METHOD_RENDER.equals(method.getName()) && RENDER_SIGNATURE
							.equals(method.getSignature()))
					|| (METHOD_PROCESS_ACTION.equals(method.getName()) && PROCESS_ACTION_SIGNATURE
							.equals(method.getSignature()))) {
				iterator.remove();
			}
		}

		return unimplementedMethods;
	}

}
