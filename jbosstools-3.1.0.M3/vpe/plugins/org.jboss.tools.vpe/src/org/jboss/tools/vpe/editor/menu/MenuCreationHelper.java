/*******************************************************************************
  * Copyright (c) 2007-2008 Red Hat, Inc.
  * Distributed under license by Red Hat, Inc. All rights reserved.
  * This program is made available under the terms of the
  * Eclipse Public License v1.0 which accompanies this distribution,
  * and is available at http://www.eclipse.org/legal/epl-v10.html
  *
  * Contributor:
  *     Red Hat, Inc. - initial API and implementation
  ******************************************************************************/
package org.jboss.tools.vpe.editor.menu;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.wst.sse.ui.StructuredTextEditor;
import org.eclipse.wst.xml.core.internal.document.NodeImpl;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.common.model.ui.objecteditor.ExtendedProperties;
import org.jboss.tools.common.model.ui.objecteditor.ExtendedPropertiesWizard;
import org.jboss.tools.common.model.ui.util.ModelUtilities;
import org.jboss.tools.common.model.ui.views.palette.PaletteInsertHelper;
import org.jboss.tools.common.model.util.ModelFeatureFactory;
import org.jboss.tools.jst.web.tld.URIConstants;
import org.jboss.tools.vpe.VpeDebug;
import org.jboss.tools.vpe.VpePlugin;
import org.jboss.tools.vpe.editor.TextNodeSplitterImpl;
import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.jboss.tools.vpe.editor.mapping.VpeDomMapping;
import org.jboss.tools.vpe.editor.mapping.VpeElementMapping;
import org.jboss.tools.vpe.editor.mapping.VpeNodeMapping;
import org.jboss.tools.vpe.editor.menu.BaseActionManager.MyMenuManager;
import org.jboss.tools.vpe.editor.menu.action.EditAttributesAction;
import org.jboss.tools.vpe.editor.menu.action.InsertAction;
import org.jboss.tools.vpe.editor.menu.action.VpeMenuListener;
import org.jboss.tools.vpe.editor.menu.action.VpeTextOperationAction;
import org.jboss.tools.vpe.editor.mozilla.MozillaEditor;
import org.jboss.tools.vpe.editor.template.VpeAnyData;
import org.jboss.tools.vpe.editor.template.VpeEditAnyDialog;
import org.jboss.tools.vpe.editor.template.VpeHtmlTemplate;
import org.jboss.tools.vpe.editor.template.VpeTemplate;
import org.jboss.tools.vpe.editor.template.VpeTemplateManager;
import org.jboss.tools.vpe.editor.util.Constants;
import org.jboss.tools.vpe.editor.util.HTML;
import org.jboss.tools.vpe.editor.util.NodesManagingUtil;
import org.jboss.tools.vpe.editor.util.SelectionUtil;
import org.jboss.tools.vpe.messages.VpeUIMessages;
import org.jboss.tools.vpe.xulrunner.browser.util.DOMTreeDumper;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

/**
 * This is helper class is used to create context menu for VPE.
 *
 * @author Igor Zhukov (izhukov@exadel.com)
 * @deprecated use {@link VpeMenuCreator} instead
 */
public class MenuCreationHelper {

	public static final String CUT_ACTION = VpeUIMessages.MenuCreationHelper_Cut;
	public static final String COPY_ACTION = JFaceResources.getString("copy"); //$NON-NLS-1$
	public static final String PASTE_ACTION = VpeUIMessages.MenuCreationHelper_Paste;
	public static final String DELETE_ACTION = JFaceResources.getString("Delete"); //$NON-NLS-1$
	public static final String TEST_ACTION = VpeUIMessages.MenuCreationHelper_Test;

	private VpeDomMapping domMapping = null;
	private VpePageContext pageContext = null;
	private StructuredTextEditor sourceEditor = null;
	private MozillaEditor visualEditor = null;
	
	/**
	 * Constructor.
	 *
	 * @param domMapping VpeDomMapping
	 * @param pageContext VpePageContext
	 * @param sourceEditor StructuredTextEditor
	 * @param visualEditor MozillaEditor
	 */
	public MenuCreationHelper(VpeDomMapping domMapping, VpePageContext pageContext,
			StructuredTextEditor sourceEditor, MozillaEditor visualEditor) {
		this.domMapping = domMapping;
		this.pageContext = pageContext;
		this.sourceEditor = sourceEditor;
		this.visualEditor = visualEditor;
	}

	/**
	 * Create menu visual element for the node passed by parameter.
	 *
	 * @param node the Node object
	 * @param manager MenuManager object
	 */
	public void createMenuForNode(Node node, MenuManager manager) {
		createMenuForNode(node, manager, false);
	}

	/**
	 * Create menu visual element for the node passed by parameter.
	 *
	 * @param node the Node object
	 * @param manager MenuManager object
	 * @param topLevelFlag in case of "true" indicates if top level flag in relevant
	 */
	public void createMenuForNode(Node node, MenuManager manager, boolean topLevelFlag) {
		NodeActionManager.setTextNodeSplitter(null);
		StructuredSelection structuredSelection = null;
		if (node != null) {
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				VpeElementMapping elementMapping = (VpeElementMapping) domMapping.getNodeMapping(node);
				if (elementMapping != null && elementMapping.getTemplate() != null) {
					manager.add(new VpeAction(
							"<" + node.getNodeName() + "> Attributes", node) { //$NON-NLS-1$ //$NON-NLS-2$
								public void run() {
									showProperties(actionNode);
								}
							});
	
					if (!topLevelFlag) {
						manager.add(new VpeAction("Select This Tag", node) { //$NON-NLS-1$
									public void run() {
										SelectionUtil.setSourceSelection(pageContext, actionNode);
									}
								});
					}
					Node parent = node.getParentNode();
					if (parent != null && parent.getNodeType() == Node.ELEMENT_NODE) {
						MenuManager menuManager = new MenuManager("Parent Tag"); //$NON-NLS-1$
						menuManager.setParent(manager);
						manager.add(menuManager);
						createMenuForNode(parent, menuManager);
					}
	
					manager.add(new Separator());
				}
			}
			if (node.getNodeType() == Node.TEXT_NODE) {
				Point range = sourceEditor.getTextViewer().getSelectedRange();
				TextNodeSplitterImpl splitter = new TextNodeSplitterImpl(range, (Text) node);
				NodeActionManager.setTextNodeSplitter(splitter);
			}
			structuredSelection = new StructuredSelection(node);
		} else {
			structuredSelection = new StructuredSelection();
		}
		NodeActionManager actionManager = new NodeActionManager(sourceEditor.getModel(), null);
		if (actionManager != null) {
			actionManager.fillContextMenuForVpe(manager, structuredSelection);
		}

		IContributionItem[] items = manager.getItems();
		/*
		 * Fix https://jira.jboss.org/jira/browse/JBIDE-3532
		 */
		boolean insertFromPalette = false;
		// fixed for JBIDE-3072
		// add "insert arround",
		for (int i = 0; i < items.length; i++) {
			if (items[i] instanceof MenuManager) {
				MenuManager mm = (MenuManager) items[i];
				int type = 0;
				Point region = null;
				if (NodeActionManager.INSERT_AROUND_MENU.equals(mm.getMenuText())) {
					type = ITextNodeSplitter.INSERT_AROUND;

					// if node is text then allow to wrap only selected text
					if (node.getNodeType() == Node.TEXT_NODE) {
						region = SelectionUtil.getSourceSelectionRange(sourceEditor);
					}
					// else wrap all tag
					else {
						region = NodesManagingUtil.getNodeRange(node);
					}
					insertFromPalette = true;
				} else if (NodeActionManager.INSERT_BEFORE_MENU.equals(mm.getMenuText())) {
					type = ITextNodeSplitter.INSERT_BEFORE;
					region = new Point(NodesManagingUtil.getStartOffsetNode(node), 0);
					insertFromPalette = true;
				} else if (NodeActionManager.INSERT_AFTER_MENU.equals(mm.getMenuText())) {
					type = ITextNodeSplitter.INSERT_AFTER;
					region = new Point(NodesManagingUtil.getEndOffsetNode(node), 0);
					insertFromPalette = true;
				} else if (NodeActionManager.REPLACE_TAG_MENU.equals(mm.getMenuText())){
					//added by Max Areshkau, fix for JBIDE-3428
					type = ITextNodeSplitter.REPLACE_TAG;
					//post start and end offset of node
					region = new Point(NodesManagingUtil.getStartOffsetNode(node),NodesManagingUtil.getNodeLength(node));
					insertFromPalette = true;
				}
				if (insertFromPalette) {
				    listenContextMenu(mm, region, type);
				}
			}
		}

		manager.add(new Separator());

		if (node != null) {
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				VpeElementMapping elementMapping = (VpeElementMapping) domMapping.getNodeMapping(node);
				if (elementMapping != null
						&& elementMapping.getTemplate() != null
						&& elementMapping.getTemplate().getType() == VpeHtmlTemplate.TYPE_ANY) {
					final VpeTemplate selectedTemplate = elementMapping.getTemplate();
					manager.add(new VpeAction(NLS.bind(VpeUIMessages.SETUP_TEMPLATE_FOR_MENU,
							 node.getNodeName() ), node) { 
								public void run() {
									boolean isCorrectNS = pageContext.isCorrectNS(actionNode);
									VpeAnyData data = null;
									if (isCorrectNS) {
										data = selectedTemplate.getAnyData();
										data.setUri(pageContext.getSourceTaglibUri(actionNode));
										data.setName(actionNode.getNodeName());
									}
									data = editAnyData(sourceEditor, isCorrectNS,data);
									if (data != null && data.isChanged())
										VpeTemplateManager.getInstance().setAnyTemplate(data);
								}
							});
	
					manager.add(new Separator());
				}
	
				manager.add(new VpeTextOperationAction(CUT_ACTION, ActionFactory.CUT.getId(), node, pageContext,sourceEditor));
				manager.add(new VpeTextOperationAction(COPY_ACTION, ActionFactory.COPY.getId(), node, pageContext,sourceEditor));
				manager.add(new VpeTextOperationAction(PASTE_ACTION, ActionFactory.PASTE.getId(), node, pageContext,sourceEditor));
			} else if (node.getNodeType() == Node.TEXT_NODE) {
				manager.add(new Action(CUT_ACTION) {
							public void run() {
								sourceEditor.getAction(ActionFactory.CUT.getId()).run();
							}
						});
				manager.add(new Action(COPY_ACTION) {
							public void run() {
								sourceEditor.getAction(ActionFactory.COPY.getId()).run();
							}
						});
				manager.add(new Action(PASTE_ACTION) {
							public void run() {
								sourceEditor.getAction(ActionFactory.PASTE.getId()).run();
							}
						});
			}

			manager.add(new Separator());

			if (actionManager != null && node != null) {
				structuredSelection = node.getNodeType() == Node.ELEMENT_NODE ? new StructuredSelection(node): null;
				actionManager.addContextMenuForVpe(manager, structuredSelection);
			}

			if (node.getNodeType() == Node.ELEMENT_NODE) {
				boolean stripEnable = false;
				NodeImpl impl = (NodeImpl) node;
				if (impl.isContainer()) {
					NodeList list = impl.getChildNodes();
					if (list.getLength() > 0) {
						if (list.getLength() == 1) {
							Node child = list.item(0);
							if (child.getNodeType() == Node.TEXT_NODE) {
								if (Constants.EMPTY.equals(child.getNodeValue().trim()))
									stripEnable = false;
								else
									stripEnable = true;
							} else
								stripEnable = true;
						} else
							stripEnable = true;
					}
				}
				if (stripEnable)
					manager.add(new VpeAction("Strip Tag", node) { //$NON-NLS-1$
								public void run() {
									Node parent = actionNode.getParentNode();
									if (parent != null) {
										int index = ((NodeImpl) actionNode).getIndex();
										parent.removeChild(actionNode);
										NodeList children = actionNode.getChildNodes();
										int lengh = children.getLength();
										Node child;
										for (int i = 0; i < lengh; i++) {
											child = children.item(0);
											actionNode.removeChild(child);
											insertNode(parent, child, index++);
										}
									}
								}

								private void insertNode(Node parent, Node node, int index) {
									Node oldNode = null;
									int childSize = parent.getChildNodes().getLength();
									if (index <= (childSize - 1))
										oldNode = parent.getChildNodes().item(index);
									if (oldNode != null)
										parent.insertBefore(node, oldNode);
									else
										parent.appendChild(node);
								}
							});
			}
			if (node.getNodeType() == Node.TEXT_NODE) {
				manager.add(new Action(DELETE_ACTION) {
							public void run() {
								sourceEditor.getAction(ActionFactory.DELETE.getId()).run();
							}
						});
			}
			if (VpeDebug.VISUAL_CONTEXTMENU_DUMP_SOURCE) {
				manager.add(new Action("Dump Source") { //$NON-NLS-1$
							public void run() {
								DOMTreeDumper dumper = new DOMTreeDumper(VpeDebug.VISUAL_DUMP_PRINT_HASH);
								dumper.setIgnoredAttributes(VpeDebug.VISUAL_DUMP_IGNORED_ATTRIBUTES);
								dumper.dumpToStream(System.out, visualEditor.getDomDocument());
							}
						});
			}
			if (VpeDebug.VISUAL_CONTEXTMENU_DUMP_SELECTED_ELEMENT) {
				manager.add(new Action("Dump Selected Element") { //$NON-NLS-1$
							public void run() {
								VpeNodeMapping nodeMapping =
									SelectionUtil.getNodeMappingBySourceSelection(sourceEditor, domMapping);
								if (nodeMapping != null) {
									DOMTreeDumper dumper = new DOMTreeDumper(VpeDebug.VISUAL_DUMP_PRINT_HASH);
									dumper.setIgnoredAttributes(VpeDebug.VISUAL_DUMP_IGNORED_ATTRIBUTES);
									dumper.dumpNode(nodeMapping.getVisualNode());
								}
							}
						});
			}
			if (VpeDebug.VISUAL_CONTEXTMENU_DUMP_MAPPING) {
				manager.add(new Action("Dump Mapping") { //$NON-NLS-1$
							public void run() {
								printMapping();
							}
						});
			}
			if (VpeDebug.VISUAL_CONTEXTMENU_TEST) {
				manager.add(new VpeAction(TEST_ACTION, node) {
					public void run() {
						test(actionNode);
					}
				});
			}
		} else {
			manager.add(new Action(PASTE_ACTION) {
				public void run() {
					sourceEditor.getAction(ActionFactory.PASTE.getId()).run();
				}
			});
		}
	}

	/**
	 * Handle menu test operation.
	 * @param node the Node object
	 */
	private void test(Node node) {}

	/**
	 * Edit any data.
	 *
	 * @param sourceEditor StructuredTextEditor object
	 * @param isCorrectNS checks if NS is correct
	 * @param data VpeAnyData object
	 * @return VpeAnyData to be edited
	 */
	private VpeAnyData editAnyData(StructuredTextEditor sourceEditor, boolean isCorrectNS, VpeAnyData data) {
		Shell shell = sourceEditor.getEditorPart().getSite().getShell();
		if (isCorrectNS) {
			VpeEditAnyDialog editDialog = new VpeEditAnyDialog(shell, data);
			editDialog.open();
		} else {
			MessageBox message = new MessageBox(shell, SWT.ICON_WARNING | SWT.OK);
			message.setMessage(VpeUIMessages.NAMESPACE_NOT_DEFINED);
			message.open();
		}
		return data;
	}

	/**
	 * Method is used to show properties for the current node.
	 *
	 * @param node the Node object
	 */
	private void showProperties(Node node) {
		ExtendedProperties p = EditAttributesAction.createExtendedProperties(node);
		if (p != null) {
			ExtendedPropertiesWizard.run(p);
		}
	}


	/**
	 * Method is used to print dump mapping.
	 */
	private void printMapping() {
		domMapping.printMapping();
	}

	/**
	 * Method is used to fill-in context menu with corresponding elements.
	 *
	 * @param manager the MenuManager object
	 * @param region the Point object
	 * @param type the type of menu element
	 */
	private void listenContextMenu(MenuManager manager, final Point region, final int type) {
	    final MenuManager paletteMenuManager = new MyMenuManager(VpeUIMessages.FROM_PALETTE, true);
		manager.add(paletteMenuManager);
		manager.addMenuListener(new VpeMenuListener(paletteMenuManager) {
			@Override
			protected void fillContextMenu() {
				fillContextMenuFromPalette(paletteMenuManager, region, type);
			}
		});
	}

	/**
	 * 
	 * @param manager
	 * @param region
	 * @param type
	 * @return
	 */
	private MenuManager fillContextMenuFromPalette(MenuManager manager, Point region, int type) {
		XModelObject model = ModelUtilities.getPreferenceModel().getByPath("%Palette%"); //$NON-NLS-1$

		XModelObject[] folders = model.getChildren();
		for (int i = 0; i < folders.length; i++) {
			if (Constants.YES_STRING.equals(folders[i].getAttributeValue(HTML.VALUE_TYPE_HIDDEN))) {
				continue;
			}
			MenuManager mm = new MenuManager(folders[i].getAttributeValue("name")); //$NON-NLS-1$
			manager.add(mm);
			fillPaletteFolder(mm, region, folders[i], type);
		}
		return manager;
	}

	private void fillPaletteFolder(MenuManager menu, Point region, XModelObject folder, int type) {
		XModelObject[] groups = folder.getChildren();
		for (int i = 0; i < groups.length; i++) {
			if (Constants.YES_STRING.equals(groups[i].getAttributeValue(HTML.VALUE_TYPE_HIDDEN))) {
				continue;
			}
			MenuManager mm = new MenuManager(groups[i].getAttributeValue("name")); //$NON-NLS-1$
			menu.add(mm);
			fillPaletteGroup(mm, region, groups[i], type);
		}
	}

	private void fillPaletteGroup(MenuManager menu, Point region, XModelObject group, int type) {
		XModelObject[] items = group.getChildren();
		String endText;

		for (int i = 0; i < items.length; i++) {
			if (Constants.YES_STRING.equals(items[i].getAttributeValue(HTML.VALUE_TYPE_HIDDEN))) {
				continue;
			}
			endText = items[i].getAttributeValue("end text"); //$NON-NLS-1$

			if (type == ITextNodeSplitter.INSERT_AROUND &&
					(endText == null || Constants.EMPTY.equals(endText))) {
				continue;
			} if(type ==ITextNodeSplitter.REPLACE_TAG){
				//mareshkau, fix for JBIDE-3428, here we create replace action 
				createReplaceAction(menu, region,items[i]);
				continue;
			}

			createInsertAction(menu, region, items[i]);
		}
	}
	/**
	 * @author mareshkau 
	 * Creates replace Actions for VPE context menu
	 * 
	 */
	private void createReplaceAction(MenuManager menu, Point region, XModelObject item){
		String tagName = getTagName(menu, region, item);
		menu.add(new InsertAction(tagName, region, item, pageContext, sourceEditor, true));
	}
	

	private void createInsertAction(MenuManager menu, Point region, XModelObject item) {
		String tagName = getTagName(menu, region, item);
		menu.add(new InsertAction(tagName, region, item, pageContext, sourceEditor));
	}
	/**
	 * @author mareshkau
	 * Returns tag name for insert and replace actions
	 * @return tag name
	 */
	private  String getTagName(MenuManager menu, Point region, XModelObject item) {
		XModelObject parent = item.getParent();
		String uri = (parent == null) ? Constants.EMPTY : parent.getAttributeValue(URIConstants.LIBRARY_URI);
		String defaultPrefix = (parent == null) ? Constants.EMPTY : parent.getAttributeValue(URIConstants.DEFAULT_PREFIX);
		String tagName = item.getAttributeValue("name"); //$NON-NLS-1$
		String[] texts = new String[] { "<" + tagName + ">" }; //$NON-NLS-1$ //$NON-NLS-2$
		if (tagName.indexOf("taglib") < 0) { //$NON-NLS-1$
			PaletteInsertHelper.applyPrefix(texts, sourceEditor, tagName, uri, defaultPrefix);
		}
		tagName = texts[0];
		
		return tagName;
	}
	/**
	 * For inner usage only. Was create just for simplicity.
	 */
	class VpeAction extends Action {
		public Node actionNode;

		public VpeAction(String name, Node node) {
			super(name);
			this.actionNode = node;
		}
	}
}
