/*******************************************************************************
 * Copyright (c) 2007 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.jst.jsp.outline.cssdialog.common;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.wst.css.core.internal.format.FormatProcessorCSS;
import org.eclipse.wst.css.core.internal.provisional.document.ICSSDocument;
import org.eclipse.wst.css.core.internal.provisional.document.ICSSModel;
import org.eclipse.wst.css.core.internal.provisional.document.ICSSStyleSheet;
import org.eclipse.wst.sse.core.StructuredModelManager;
import org.eclipse.wst.sse.core.internal.provisional.IModelManager;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.sse.core.internal.provisional.IndexedRegion;
import org.jboss.tools.jst.jsp.JspEditorPlugin;
import org.jboss.tools.jst.jsp.outline.cssdialog.events.StyleAttributes;
import org.w3c.dom.css.CSSRuleList;
import org.w3c.dom.css.CSSStyleDeclaration;
import org.w3c.dom.css.CSSStyleRule;
import org.w3c.dom.css.CSSStyleSheet;

/**
 * CSS class model.
 * 
 * 
 */
public class CSSModel {

    private static String startBraces = "{"; //$NON-NLS-1$
    private static String endBraces = "}"; //$NON-NLS-1$

    private FormatProcessorCSS formatProcessorCSS = null;
    private IStructuredModel model = null;
    private IFile styleFile = null;

    private CSSStyleSheet styleSheet = null;
    private ICSSStyleSheet eclipseStyleSheet = null;
    
    

	/**
     * Constructor.
     *
     * @param styleFile CSS style class that should initialize CSS model
     */
    public CSSModel(IFile styleFile) {
    	init(styleFile);
    }

    public void init(IFile styleFile) {
        try {
        	this.styleFile = styleFile;
        	if (model != null) {
        		releaseModel();
        	}
        	formatProcessorCSS = new FormatProcessorCSS();
            IModelManager modelManager = StructuredModelManager.getModelManager();
            model = modelManager.getExistingModelForEdit(styleFile);
			if (model == null)
				model = modelManager.getModelForEdit(styleFile);
            if (model instanceof ICSSModel) {
                ICSSModel cssModel = (ICSSModel) model;
                ICSSDocument document = cssModel.getDocument();
                if (document instanceof CSSStyleSheet) {
                    styleSheet = (CSSStyleSheet) document;
                }
				if (document instanceof ICSSStyleSheet) {
					eclipseStyleSheet = (ICSSStyleSheet) document;
				}
				
		        
            }
        } catch (IOException e) {
            JspEditorPlugin.getPluginLog().logError(e.getMessage());
        } catch (CoreException e) {
            JspEditorPlugin.getPluginLog().logError(e.getMessage());
        }
    }

    /**
     * Method is used to select area that corresponds to specific selector.
     *
     * @param selector the selector that should be selected in editor area
     * @param index if CSS file contains more then one elements with the same selector name,
     * 		then index is serial number of this selector
     */
    public IndexedRegion getSelectorRegion(String selector, int index) {
    	//FIXED by sdzmitrovich - JBIDE-3148
//    	if (eclipseStyleSheet != null) {
//			if (selector != null && !selector.equals(Constants.EMPTY)) {
//				ICSSStyleRule styleRule = Util.getSelector(eclipseStyleSheet, selector, index);
//				if (styleRule != null) {
//					if (styleRule instanceof IndexedRegion) {
//						return (IndexedRegion) styleRule;
//					}
//				}
//			}
//    	}
    	
    	if ( selector != null) {
			CSSStyleRule rule = getRulesMapping().get(selector);
			if (rule != null)
				if (rule instanceof IndexedRegion) {
					return (IndexedRegion) rule;
				}

		}
    	return null;
    }
    
    
    public List<String> getSelectorLabels() {

		List<String> selectorLabels;

		selectorLabels = new ArrayList<String>(getRulesMapping().keySet());

		Collections.sort(selectorLabels);

		return selectorLabels;
	}

    /**
     * Get selectors
     *
     * @return List<String>
     */
    public List<Selector> getSelectors() {
        List<Selector> selectors = new ArrayList<Selector>();

        if (styleSheet != null) {
            CSSRuleList list = styleSheet.getCssRules();

            if (list != null) {
                for (int i = 0; i < list.getLength(); i++) {
                    if (list.item(i) instanceof CSSStyleRule) {
                    	CSSStyleRule rule = ((CSSStyleRule) list.item(i));
                    	Selector selector = new Selector(rule.toString(), rule.getSelectorText());
                    	selectors.add(selector);
                    }
                }
            }
        }

        return selectors;
    }
    
    

    /**
     * Gets CSS attributes for the given selector in string representation.
     *
     * @param selector CSS selector value
     * @return CSS attributes string representation
     */
    public String getCSSText(String selector) {
    	//FIXED by sdzmitrovich - JBIDE-3148
//        if (styleSheet != null && selector != null) {
//            CSSRuleList list = styleSheet.getCssRules();
//
//            if (list != null) {
//                for (int i = 0; i < list.getLength(); i++) {
//                    if (list.item(i) instanceof CSSStyleRule &&
//                            ((CSSStyleRule) list.item(i)).getSelectorText().equals(selector)) {
//                        return ((CSSStyleRule) list.item(i)).getCssText();
//                    }
//                }
//            }
//    }
		if ( selector != null) {
			CSSStyleRule rule = getRulesMapping().get(selector);
			if (rule != null)
				return rule.getCssText();

		}

        return null;
    }

    /**
     * Get style by selectorName
     *
     * @param selectorName
     * @return style
     */
    public String getStyle(String selectorName) {
    	//FIXED by sdzmitrovich - JBIDE-3148
//        if (styleSheet != null) {
//            CSSRuleList list = styleSheet.getCssRules();
//
//            if (list != null) {
//                for (int i = 0; i < list.getLength(); i++) {
//                    if (list.item(i) instanceof CSSStyleRule &&
//                            ((CSSStyleRule) list.item(i)).getSelectorText().equals(selectorName)) {
//                        return ((CSSStyleRule) list.item(i)).getStyle().getCssText();
//                    }
//                }
//            }
//        }
    	if (selectorName != null) {
			CSSStyleRule rule = getRulesMapping().get(selectorName);
			if (rule != null)
				return rule.getStyle().getCssText();

		}

        return null;
    }

    /**
     * Sets CSS style for the given selector.
     *
     * @param selector CSS selector value
     * @param styleAttribute the style to be set
     */
    public void setCSS(String selector, StyleAttributes styleAttributes) {
        if (styleSheet != null && selector != null && !selector.equals(Constants.EMPTY)) {
        	CSSRuleList list = styleSheet.getCssRules();
        	//FIXED by sdzmitrovich - JBIDE-3148
//            if (list != null) {
//            	// check if selector passed by parameter already exists in CSS
//                for (int i = 0; i < list.getLength(); i++) {
//                    if (list.item(i) instanceof CSSStyleRule &&
//                            ((CSSStyleRule) list.item(i)).getSelectorText().equals(selector)) {
//
//                        CSSStyleRule rule = (CSSStyleRule) list.item(i);
//                        styleSheet.deleteRule(i);
//
//                        i = styleSheet.insertRule(selector + startBraces + styleAttributes.getStyle() + endBraces, i);
//                        rule = (CSSStyleRule) list.item(i);
//                        CSSStyleDeclaration declaration = rule.getStyle();
//                        // set properties
//                        Set<Entry<String, String>> set = styleAttributes.entrySet();
//                        for (Map.Entry<String, String> me : set) {
//                        	declaration.setProperty(me.getKey(), me.getValue(), Constants.EMPTY);
//                        }
//
//                        formatProcessorCSS.formatModel(model);
//                        return;
//                    }
//                }
//                // insert NEW selector to style sheet
//                styleSheet.insertRule(selector + startBraces + styleAttributes.getStyle() + endBraces, list.getLength());
//                formatProcessorCSS.formatModel(model);
			CSSStyleRule rule = getRulesMapping().get(selector);
			if (rule == null) {
				styleSheet.insertRule(selector + startBraces
						+ styleAttributes.getStyle() + endBraces, list
						.getLength());
			} else {

				CSSStyleDeclaration declaration = rule.getStyle();
				// set properties
				Set<Entry<String, String>> set = styleAttributes.entrySet();
				
				if ((set.size() == 0) && (declaration.getLength()>0))
					declaration.setCssText(Constants.EMPTY);
				else
					for (Map.Entry<String, String> me : set) {
						declaration.setProperty(me.getKey(), me.getValue(),
								Constants.EMPTY);
					}
			}

			formatProcessorCSS.formatModel(model);
        	
        }
    }

    /**
     * Gets file associated with current model.
     *
	 * @return the styleFile
	 */
	public IFile getStyleFile() {
		return this.styleFile;
	}

    /**
     * Release CSS model correctly from editing.
     */
    public void releaseModel() {
    	IModelManager modelManager = StructuredModelManager.getModelManager();
    	if(!modelManager.isShared(model.getId()))
    	model.releaseFromEdit();
    }

    /**
     * Save model. Associate file will be saved automatically.
     */
    public void saveModel() {
        try {
            model.save();
        } catch (IOException e) {
            JspEditorPlugin.getPluginLog().logError(e.getMessage());
        } catch (CoreException e) {
            JspEditorPlugin.getPluginLog().logError(e.getMessage());
        }
    }
    
    /**
	 * get mapping key is label ( label = class name + sequence number of such
	 * css class ) value is CSSStyleRule
	 * 
	 * now rule mapping is generated always ... keeping of ruleMapping is more
	 * right but it demands more complex synchronization data
	 * 
	 */
    private Map<String, CSSStyleRule> getRulesMapping() {

		Map<String, CSSStyleRule> rulesMapping = new HashMap<String, CSSStyleRule>();
		if (styleSheet != null) {
			CSSRuleList list = styleSheet.getCssRules();

			Map<String, Integer> frequencyMap = new HashMap<String, Integer>();

			if (list != null) {
				for (int i = 0; i < list.getLength(); i++) {
					if (list.item(i) instanceof CSSStyleRule) {

						CSSStyleRule rule = ((CSSStyleRule) list.item(i));

						Integer freq = frequencyMap.get(rule.getSelectorText());

						freq = freq == null ? 1 : freq + 1;

						frequencyMap.put(rule.getSelectorText(), freq);

						String ruleLabel = rule.getSelectorText()
								+ (freq > 1 ? Constants.START_BRACKET + freq
										+ Constants.END_BRACKET
										: Constants.EMPTY);

						rulesMapping.put(ruleLabel, rule);

					}
				}
			}

		}

		return rulesMapping;
	}
}
