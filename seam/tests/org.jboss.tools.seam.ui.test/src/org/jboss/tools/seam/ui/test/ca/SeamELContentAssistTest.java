package org.jboss.tools.seam.ui.test.ca;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wst.sse.core.internal.provisional.text.IStructuredDocumentRegion;
import org.eclipse.wst.sse.core.internal.provisional.text.ITextRegion;
import org.eclipse.wst.sse.core.internal.provisional.text.ITextRegionList;
import org.eclipse.wst.sse.ui.internal.contentassist.ContentAssistUtils;
import org.eclipse.wst.xml.core.internal.regions.DOMRegionContext;
import org.jboss.tools.common.test.util.TestProjectProvider;
import org.jboss.tools.jst.jsp.test.TestUtil;
import org.jboss.tools.jst.jsp.test.ca.ContentAssistantTestCase;
import org.jboss.tools.seam.ui.text.java.SeamELProposalProcessor;
import org.jboss.tools.test.util.JUnitUtils;
import org.jboss.tools.test.util.xpl.EditorTestHelper;

import sun.management.counter.Units;

public class SeamELContentAssistTest extends ContentAssistantTestCase {
	TestProjectProvider provider = null;
	boolean makeCopy = false;
	private static final String PROJECT_NAME = "TestSeamELContentAssist";
	private static final String PAGE_NAME = "/WebContent/login.xhtml";

	public static Test suite() {
		return new TestSuite(SeamELContentAssistTest.class);
	}

	public void setUp() throws Exception {
		provider = new TestProjectProvider("org.jboss.tools.seam.ui.test", null, PROJECT_NAME, makeCopy); 
		project = provider.getProject();
		Throwable exception = null;
		try {
			project.refreshLocal(IResource.DEPTH_INFINITE, null);
		} catch (Exception x) {
			exception = x;
			x.printStackTrace();
		}
		assertNull("An exception caught: " + (exception != null? exception.getMessage() : ""), exception);
	}

	protected void tearDown() throws Exception {
		if(provider != null) {
			provider.dispose();
		}
	}

	/**
	 * Test for http://jira.jboss.com/jira/browse/JBIDE-1258
	 */
	public void testMessages() {
		try {
			EditorTestHelper.joinBackgroundActivities();
		} catch (Exception e) {
			JUnitUtils.fail(e.getMessage(), e);;
		}
		assertTrue("Test project \"" + PROJECT_NAME + "\" is not loaded", (project != null));
		checkProposals("/WebContent/messages.xhtml", 494, new String[]{"messages.Text1", "messages.Text2"}, true);
	}

	/**
	 * Test for http://jira.jboss.com/jira/browse/JBIDE-1803
	 */
	public void testVarAttributes() {
		try {
			EditorTestHelper.joinBackgroundActivities();
		} catch (Exception e) {
			e.printStackTrace();
		} 
		assertTrue("Test project \"" + PROJECT_NAME + "\" is not loaded", (project != null));

		IFile component = project.getFile("src/action/demo/TestComponentForVarAttributes.java");
		IFile newComponent = project.getFile("src/action/demo/TestComponentForVarAttributes.1");
		IFile emptyComponent = project.getFile("src/action/demo/TestComponentForVarAttributes.2");
		try{
			component.setContents(newComponent.getContents(), true, false, new NullProgressMonitor());
		}catch(Exception e){
			JUnitUtils.fail("Error during changing 'TestComponentForVarAttributes.java' content to 'TestComponentForVarAttributes.1'", e);
		}
		EditorTestHelper.joinJobs(1000,10000,500);

		checkProposals("/WebContent/varAttributes.xhtml", 458, new String[]{"test.name"}, false);

		try{
			component.setContents(emptyComponent.getContents(), true, false, new NullProgressMonitor());
		}catch(Exception e){
			JUnitUtils.fail("Error during changing 'TestComponentForVarAttributes.java' content to 'TestComponentForVarAttributes.2'", e);
		}
		EditorTestHelper.joinJobs(1000,10000,500);
	}

	private static final String[] VALID_SEAM_EL_PROPOSALS = new String[] {
			"TestSeamELContentAssistEntityManagerFactory",
			"actor",
			"applicationContext",
			"authenticator",
			"businessProcessContext",
			"businessProcess",
			"captcha",
			"conversationContext",
			"conversationList",
			"conversationStack",
			"conversation",
			"currentDatetime",
			"currentDate",
			"currentTime",
			"entityManager",
			"eventContext",
			"events",
			"expressions",
			"facesContext",
			"facesMessages",
			"httpError",
			"identity",
			"identity:addRole", 
			"identity:addRole(arg0)", 
			"identity:authenticate", 
			"identity:authenticate()", 
			"identity:authenticate(loginContext)", 
			"identity:authenticateMethod", 
			"identity:checkEntityPermission", 
			"identity:checkEntityPermission(arg0, arg1)", 
			"identity:checkPermission", 
			"identity:checkPermission(name, action, arg)", 
			"identity:checkRestriction", 
			"identity:checkRestriction(expr)", 
			"identity:checkRole", 
			"identity:checkRole(role)", 
			"identity:clearDirty", 
			"identity:clearDirty()", 
			"identity:cookieEnabled", 
			"identity:cookieMaxAge", 
			"identity:create", 
			"identity:create()", 
			"identity:hasPermission", 
			"identity:hasPermission(name, action, arg)", 
			"identity:hasRole", 
			"identity:hasRole(arg0)", 
			"identity:jaasConfigName", 
			"identity:loggedIn", 
			"identity:login", 
			"identity:login()", 
			"identity:logout", 
			"identity:logout()", 
			"identity:password", 
			"identity:principal", 
			"identity:rememberMe", 
			"identity:removeRole", 
			"identity:removeRole(arg0)", 
			"identity:subject", 
			"identity:username",
			"image",
			"interpolator",
			"isUserInRole",
			"jbpmContext",
			"localeSelector",
			"locale",
			"mailSession",
			"messages",
			"methodContext",
			"mockSecureEntity",
			"org.jboss.seam.captcha.captchaImage",
			"org.jboss.seam.captcha.captcha",
			"org.jboss.seam.core.actor",
			"org.jboss.seam.core.applicationContext",
			"org.jboss.seam.core.businessProcessContext",
			"org.jboss.seam.core.businessProcess",
			"org.jboss.seam.core.conversationContext",
			"org.jboss.seam.core.conversationEntries",
			"org.jboss.seam.core.conversationList",
			"org.jboss.seam.core.conversationStack",
			"org.jboss.seam.core.conversation",
			"org.jboss.seam.core.dispatcher",
			"org.jboss.seam.core.ejb",
			"org.jboss.seam.core.eventContext",
			"org.jboss.seam.core.events",
			"org.jboss.seam.core.exceptions",
			"org.jboss.seam.core.expressions",
			"org.jboss.seam.core.facesContext",
			"org.jboss.seam.core.facesMessages",
			"org.jboss.seam.core.facesPage",
			"org.jboss.seam.core.httpError",
			"org.jboss.seam.core.image",
			"org.jboss.seam.core.init",
			"org.jboss.seam.core.interpolator",
			"org.jboss.seam.core.isUserInRole",
			"org.jboss.seam.core.jbpmContext",
			"org.jboss.seam.core.jbpm",
			"org.jboss.seam.core.localeSelector",
			"org.jboss.seam.core.locale",
			"org.jboss.seam.core.manager",
			"org.jboss.seam.core.messages",
			"org.jboss.seam.core.methodContext",
			"org.jboss.seam.core.microcontainer",
			"org.jboss.seam.core.pageContext",
			"org.jboss.seam.core.pageflow",
			"org.jboss.seam.core.pages",
			"org.jboss.seam.core.persistenceContexts",
			"org.jboss.seam.core.pojoCache",
			"org.jboss.seam.core.pooledTaskInstanceList",
			"org.jboss.seam.core.pooledTask",
			"org.jboss.seam.core.processInstanceFinder",
			"org.jboss.seam.core.processInstanceList",
			"org.jboss.seam.core.processInstance",
			"org.jboss.seam.core.redirect",
			"org.jboss.seam.core.renderer",
			"org.jboss.seam.core.resourceBundle",
			"org.jboss.seam.core.safeActions",
			"org.jboss.seam.core.sessionContext",
			"org.jboss.seam.core.switcher",
			"org.jboss.seam.core.taskInstanceListForType",
			"org.jboss.seam.core.taskInstanceList",
			"org.jboss.seam.core.taskInstance",
			"org.jboss.seam.core.timeZoneSelector",
			"org.jboss.seam.core.timeZone",
			"org.jboss.seam.core.transactionListener",
			"org.jboss.seam.core.transition",
			"org.jboss.seam.core.uiComponent",
			"org.jboss.seam.core.userPrincipal",
			"org.jboss.seam.core.validation",
			"org.jboss.seam.core.validators",
			"org.jboss.seam.debug.contexts",
			"org.jboss.seam.debug.hotDeployFilter",
			"org.jboss.seam.debug.introspector",
			"org.jboss.seam.framework.currentDatetime",
			"org.jboss.seam.framework.currentDate",
			"org.jboss.seam.framework.currentTime",
			"org.jboss.seam.ioc.spring.contextLoader",
			"org.jboss.seam.ioc.spring.springELResolver",
			"org.jboss.seam.jms.queueConnection",
			"org.jboss.seam.jms.queueSession",
			"org.jboss.seam.jms.topicConnection",
			"org.jboss.seam.jms.topicSession",
			"org.jboss.seam.mail.mailSession",
			"org.jboss.seam.pdf.documentStore",
			"org.jboss.seam.persistence.persistenceProvider",
			"org.jboss.seam.remoting.messaging.subscriptionRegistry",
			"org.jboss.seam.remoting.remoting",
			"org.jboss.seam.security.configuration",
			"org.jboss.seam.security.identity",
			"org.jboss.seam.servlet.characterEncodingFilter",
			"org.jboss.seam.servlet.contextFilter",
			"org.jboss.seam.servlet.exceptionFilter",
			"org.jboss.seam.servlet.multipartFilter",
			"org.jboss.seam.servlet.redirectFilter",
			"org.jboss.seam.theme.themeSelector",
			"org.jboss.seam.theme.theme",
			"org.jboss.seam.ui.entityConverterStore",
			"org.jboss.seam.ui.entityConverter",
			"org.jboss.seam.ui.graphicImage.dynamicImageResource",
			"org.jboss.seam.ui.graphicImage.dynamicImageStore",
			"org.jboss.seam.ui.resource.webResource",
			"pageContext",
			"pageflow",
			"pdfKeyStore",
			"pojoCache",
			"pooledTaskInstanceList",
			"pooledTask",
			"processInstanceFinder",
			"processInstanceList",
			"processInstance",
			"queueSession",
			"redirect",
			"renderer",
			"resourceBundle",
			"securityRules",
			"sessionContext",
			"switcher",
			"taskInstanceListForType",
			"taskInstanceList",
			"taskInstance",
			"themeSelector",
			"theme",
			"timeZoneSelector",
			"timeZone",
			"topicSession",
			"transition",
			"uiComponent",
			"userPrincipal",
			"validation"
			};

	
	
	
	/**
	 * Do not use this set as is because of colon used instead of dot to separate items of level 2 and more
	 * @return
	 */
	protected Set<String> getPageValidProposals() {
		TreeSet<String> pSet = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
		for (int i = 0; i < VALID_SEAM_EL_PROPOSALS.length; i++) {
			pSet.add(VALID_SEAM_EL_PROPOSALS[i]);
		}

		return pSet;
	}

	private int getDotIndex (String expr) {
		if (expr == null)
			return 0;
		
		int count = 0;
		int index = 0;
		while ((index = expr.indexOf('.', index+1)) != -1) {
			count++;
		}
		
		return count;
	}
	
	private int getValidDotIndex (String expr) {
		if (expr == null)
			return 0;

		int count = 0;
		int index = 0;
		while ((index = expr.indexOf(':', index+1)) != -1) {
			count++;
		}
		
		return count;
	}
	
	protected Set<String> getFilteredProposals(Set<String> proposals, String filter) {
		TreeSet<String> fSet = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
		
		int dotIndex = getDotIndex(filter);
		for (String proposal : proposals) {
			if (getValidDotIndex(proposal) <= dotIndex) {
				proposal = proposal.replace(':', '.');
				if (proposal.startsWith(filter) ) { 
					fSet.add(proposal);
				}
			}
		}

		return fSet;
	}
	
	protected Set<String> renewWithPrefixAndPostfix(Set<String> proposals, String prefix, String suffix) {
		TreeSet<String> rSet = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
		
		for (String proposal : proposals) {
			rSet.add(prefix + proposal + suffix);
		}

		return rSet;
		
	}
	
	private int indexOfElBOB (String content, int startFrom) {
		int index1 = content.indexOf("${", startFrom);
		int index2 = content.indexOf("#{", startFrom);
		
		if (index1 == -1)
			return index2;
		
		if (index2 == -1)
			return index1;
		
		return (index1 < index2 ? index1 : index2);
	}
	
	protected List<IRegion> getAttributeValueRegions(ITextViewer viewer) {
		List<IRegion> regions = new ArrayList<IRegion>();
		IDocument document = viewer.getDocument();
		int startOffset = 0;
		int endOffset = document.getLength();

		IStructuredDocumentRegion sdRegion = null;
		
		while (startOffset < endOffset && (sdRegion = ContentAssistUtils.getStructuredDocumentRegion(viewer, startOffset)) != null) {
			ITextRegionList list = sdRegion.getRegions();
			
			for (int i = 0; list != null && i < list.size(); i++) {
				ITextRegion region = list.get(i);
				if (region.getType() == DOMRegionContext.XML_TAG_ATTRIBUTE_VALUE) {

					final int regionOffset = sdRegion.getStartOffset() + region.getStart();
					final int regionLength = region.getTextLength();

					regions.add(new IRegion () {
						public int getLength() {
							return regionLength;
						}

						public int getOffset() {
							return regionOffset;
						}
					});
					
				}
			}
			startOffset += sdRegion.getLength(); 
		}
		
		return regions;
	}
	
	protected List<IRegion> getELRegionsToTest(IDocument document) {
		List<IRegion> regions = new ArrayList<IRegion>();
		String  documentContent = document.get();
		if (documentContent == null)
			return regions;
		
		int fromIndex = indexOfElBOB(documentContent, 0);
		
		while (fromIndex != -1) {
			int endIndex = documentContent.indexOf("}", fromIndex);
			final int regionOffset = fromIndex;
			final int regionLength = (endIndex != -1 ? endIndex - fromIndex : 
				documentContent.length() - fromIndex) + 1;
			
			regions.add(new IRegion () {
				public int getLength() {
					return regionLength;
				}

				public int getOffset() {
					return regionOffset;
				}
			});
			
			fromIndex = (endIndex != -1 ? indexOfElBOB(documentContent, endIndex) : -1);
		}
		
		return regions;
	}
	
	public void testSeamELContentAssist() {
		openEditor(PAGE_NAME);

		List<IRegion> regionsToTest = getELRegionsToTest(document);
		if (regionsToTest != null) {
			for (IRegion region : regionsToTest) {
				try {
//					System.out.println("Seam EL Region To Test: [" + region.getOffset() + "/" + region.getLength() + "] ==> [" + 
//							document.get(region.getOffset(), region.getLength()) + "]");
					
					int startOffset = region.getOffset() + 2;
					for (int i = 2; i < region.getLength(); i++) {
						int offset = region.getOffset() + i;
						
						String filter = document.get(startOffset, offset - startOffset);
						Set<String> filteredValidProposals = getFilteredProposals(getPageValidProposals(), filter);
						
						ICompletionProposal[] result= null;
						String errorMessage = null;

						IContentAssistProcessor p= TestUtil.getProcessor(viewer, offset, contentAssistant);
						if (p != null) {
							try {
								result= p.computeCompletionProposals(viewer, offset);
							} catch (Throwable x) {
								x.printStackTrace();
							}
							errorMessage= p.getErrorMessage();
						}
						
//						if (errorMessage != null && errorMessage.trim().length() > 0) {
//							System.out.println("#" + offset + ": ERROR MESSAGE: " + errorMessage);
//						}
						
						// compare SeamELCompletionProposals in the result to the filtered valid proposals
						Set<String> existingProposals = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
						Set<String> nonExistingProposals = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
						
						if (result != null && result.length > 0) {
							for (int j = 0; j < result.length; j++) {
//								System.out.println("Result#" + i + "-" + j + " ==> " + result[j].getClass().getName());
								// Look only for SeamELProposalProcessor proposals
								if (result[j] instanceof SeamELProposalProcessor.Proposal) {
									SeamELProposalProcessor.Proposal proposal = (SeamELProposalProcessor.Proposal)result[j];
									String proposalString = proposal.getPrefixCompletionText(document, offset).toString();
									if (filteredValidProposals.contains(proposalString)) {
										existingProposals.add(proposalString);
										filteredValidProposals.remove(proposalString);
									} else {
										nonExistingProposals.add(proposalString);
									}
								}
							}
						}
						assertTrue("Some Seam EL proposals werent\'t shown in the Content Assistant", filteredValidProposals.isEmpty());
						assertTrue("Some Seam EL proposals were shown in the Content Assistant but they shouldn\'t", nonExistingProposals.isEmpty());
					}
					
					
				} catch (BadLocationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					assertNull("An exception caught: " + (e != null? e.getMessage() : ""), e);
				}
			}
		}
		regionsToTest = getAttributeValueRegions(viewer);
		if (regionsToTest != null) {
			for (IRegion region : regionsToTest) {
				try {
//					System.out.println("Attribute Region To Test: [" + region.getOffset() + "/" + region.getLength() + "] ==> [" + 
//							document.get(region.getOffset(), region.getLength()) + "]");

					String attributeText = document.get(region.getOffset(), region.getLength());
					int openQuoteIndex = attributeText.indexOf('"');
					if (openQuoteIndex == -1)
						openQuoteIndex = attributeText.indexOf('\'');
					else {
						int openQuoteIndex2 = attributeText.indexOf('\'');
						if (openQuoteIndex2 != -1) {
							openQuoteIndex = (openQuoteIndex < openQuoteIndex2 ? openQuoteIndex : openQuoteIndex2);
						}
					}
					
					int closeQuoteIndex = (openQuoteIndex == -1 ? -1 : attributeText.lastIndexOf(attributeText.charAt(openQuoteIndex)));
					
					int startOffset = region.getOffset();
					for (int i = 0; i < region.getLength(); i++) {
						int offset = startOffset + i;
						if ((openQuoteIndex != -1 && i <= openQuoteIndex) ||
								(closeQuoteIndex != -1 && i >= closeQuoteIndex)) {
							// - Before and at opening quotation mark (single or double quote)
							// - or at and after closing quotation mark (single or double quote)
							// There is no prompting acceptable
						} else {
							String filter = document.get(startOffset + openQuoteIndex + 1, offset - startOffset - openQuoteIndex - 1);
							
							String clearedFilter = filter;
							if (filter.startsWith("#{")) {
								clearedFilter = filter.substring(2);
							} else if (filter.startsWith("#")) {
								clearedFilter = filter.substring(1);
							} else {
								clearedFilter = "";
							}
								
							Set<String> filteredValidProposals = getFilteredProposals(getPageValidProposals(), clearedFilter);
							
							if (filter.indexOf("#") == -1) {
								filteredValidProposals = renewWithPrefixAndPostfix(filteredValidProposals, "#{", "}");
							} else if (filter.indexOf("#{") == -1) {
								filteredValidProposals = renewWithPrefixAndPostfix(filteredValidProposals, "{", "}");
							}

							
							ICompletionProposal[] result= null;
							String errorMessage = null;
	
							IContentAssistProcessor p= TestUtil.getProcessor(viewer, offset, contentAssistant);
							if (p != null) {
								try {
									result= p.computeCompletionProposals(viewer, offset);
								} catch (Throwable x) {
									x.printStackTrace();
								}
								errorMessage= p.getErrorMessage();
							}
							
//							if (errorMessage != null && errorMessage.trim().length() > 0) {
//								System.out.println("#" + offset + ": ERROR MESSAGE: " + errorMessage);
//							}
							
							// compare SeamELCompletionProposals in the result to the filtered valid proposals
							Set<String> existingProposals = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
							Set<String> nonExistingProposals = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
							
							if (result != null && result.length > 0) {
								for (int j = 0; j < result.length; j++) {
//									System.out.println("Result#" + i + "/" + j + " ==> " + result[j].getClass().getName());
									// Look only for SeamELProposalProcessor proposals
									if (result[j] instanceof SeamELProposalProcessor.Proposal) {
										SeamELProposalProcessor.Proposal proposal = (SeamELProposalProcessor.Proposal)result[j];
										String proposalString = proposal.getPrefixCompletionText(document, offset).toString();
										
										if (filteredValidProposals.contains(proposalString)) {
											existingProposals.add(proposalString);
											filteredValidProposals.remove(proposalString);
										} else {
											nonExistingProposals.add(proposalString);
										}
									}
								}
							}
							assertTrue("Some in-attribute Seam EL proposals werent\'t shown in the Content Assistant", filteredValidProposals.isEmpty());
							assertTrue("Some in-attribute Seam EL proposals were shown in the Content Assistant but they shouldn\'t", nonExistingProposals.isEmpty());
						}
					}
				} catch (BadLocationException e) {
					e.printStackTrace();
					assertNull("An exception caught: " + (e != null? e.getMessage() : ""), e);
				}
			}
		}
		
		closeEditor();

	}
}
