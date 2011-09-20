/******************************************************************************* 
 * Copyright (c) 2009-2011 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.jst.web.kb.internal.proposal;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.jboss.tools.common.text.TextProposal;
import org.jboss.tools.jst.web.kb.ICSSContainerSupport;
import org.jboss.tools.jst.web.kb.IPageContext;
import org.jboss.tools.jst.web.kb.KbQuery;
import org.jboss.tools.jst.web.kb.PageContextFactory.CSSStyleSheetDescriptor;
import org.jboss.tools.jst.web.kb.WebKbPlugin;
import org.w3c.dom.css.CSSMediaRule;
import org.w3c.dom.css.CSSRule;
import org.w3c.dom.css.CSSRuleList;
import org.w3c.dom.css.CSSStyleRule;

/**
 * The CSS Class proposal type. Is used to collect and return the proposals on
 * the CSS classes
 * 
 * @author Victor Rubezhny
 *
 */
@SuppressWarnings("restriction")
public class CSSClassProposalType extends CustomProposalType {
	private static final String IMAGE_NAME = "EnumerationProposal.gif"; //$NON-NLS-1$
	private static Image ICON;

	static String ID = "cssclass"; //$NON-NLS-1$
	static String QUOTE_1 = "'"; //$NON-NLS-1$
	static String QUOTE_2 = "\""; //$NON-NLS-1$
	Set<String> idList = new TreeSet<String>();

	@Override
	protected void init(IPageContext context) {
		idList.clear();
		if (context instanceof ICSSContainerSupport) {
			ICSSContainerSupport cssSource = (ICSSContainerSupport)context;
			
			List<CSSStyleSheetDescriptor> descrs = cssSource.getCSSStyleSheetDescriptors();
			if (descrs != null) {
				for (CSSStyleSheetDescriptor descr : descrs) {
					CSSRuleList rules = descr.sheet.getCssRules();
					for (int i = 0; rules != null && i < rules.getLength(); i++) {
						CSSRule rule = rules.item(i);
						idList.addAll(getClassNamesFromCSSRule(rule));
					}
				}
			}
		}
	}

	/**
	 * Returns the style class name found in the specified CSS Rule 
	 * 
	 * @param cssRule
	 * @param styleName
	 * @return
	 */
	public static Set<String> getClassNamesFromCSSRule(CSSRule cssRule) {
		Set<String> styleNames = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);

		if (cssRule instanceof CSSMediaRule) {
			CSSMediaRule cssMediaRule = (CSSMediaRule)cssRule;
			CSSRuleList rules = cssMediaRule.getCssRules();
			for (int i = 0; rules != null && i < rules.getLength(); i++) {
				CSSRule rule = rules.item(i);
				styleNames.addAll(getClassNamesFromCSSRule(rule));
			}
			return styleNames;
		}
		if (!(cssRule instanceof CSSStyleRule))
			return styleNames;
		
		// get selector text
		String selectorText = ((CSSStyleRule) cssRule).getSelectorText();

		if (selectorText != null) {
			String styles[] = selectorText.trim().split(","); //$NON-NLS-1$
			for (String styleText : styles) {
				String[] styleWords = styleText.trim().split(" ");  //$NON-NLS-1$
				if (styleWords != null) {
					for (String styleWord : styleWords) {
						String[] anotherStyleWords = styleWord.split(":");
						for (String name : anotherStyleWords) {
							String nameWithoutArgs = name;
							// Add (if exists) class name defined before args
							if (name.indexOf('[') >= 0) { //$NON-NLS-1$
								nameWithoutArgs = name.substring(0, name.indexOf("[")); //$NON-NLS-1$
							}
							
							if (nameWithoutArgs != null && nameWithoutArgs.indexOf(".") >= 0) { //$NON-NLS-1$
								nameWithoutArgs = nameWithoutArgs.substring(nameWithoutArgs.indexOf(".") + 1); //$NON-NLS-1$

								styleNames.add(nameWithoutArgs);
							}

							if (name.lastIndexOf(']') >= 0) {
								nameWithoutArgs = name.substring(name.indexOf(']') + 1);
								if (nameWithoutArgs != null && nameWithoutArgs.indexOf(".") >= 0) { //$NON-NLS-1$
									nameWithoutArgs = nameWithoutArgs.substring(nameWithoutArgs.indexOf(".") + 1); //$NON-NLS-1$

									styleNames.add(nameWithoutArgs);
								}
							}
						}
					}
				}
			}
		}
		return styleNames;
	}
	
	@Override
	public TextProposal[] getProposals(KbQuery query) {
		String v = query.getValue();
		int offset = v.length();
		int b = v.lastIndexOf(',');
		if(b < 0) b = 0; else b += 1;
		String tail = v.substring(offset);
		int e = tail.indexOf(',');
		if(e < 0) e = v.length(); else e += offset;
		String prefix = v.substring(b).trim();

		List<TextProposal> proposals = new ArrayList<TextProposal>();
		for (String text: idList) {
			if(text.startsWith(prefix)) {
				TextProposal proposal = new TextProposal();
				proposal.setLabel(text);
				proposal.setReplacementString(text);
				proposal.setPosition(b + text.length());
				proposal.setStart(b);
				proposal.setEnd(e);
				if(ICON==null) {
					ICON = ImageDescriptor.createFromFile(WebKbPlugin.class, IMAGE_NAME).createImage();
				}
				proposal.setImage(ICON);
				
				proposals.add(proposal);
			}
		}

		return proposals.toArray(new TextProposal[0]);
	}

}
