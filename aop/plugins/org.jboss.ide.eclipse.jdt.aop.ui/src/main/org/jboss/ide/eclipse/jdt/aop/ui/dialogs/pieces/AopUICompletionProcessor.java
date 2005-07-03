/**
 * 
 */
package org.jboss.ide.eclipse.jdt.aop.ui.dialogs.pieces;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IClassFile;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.search.IJavaSearchConstants;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.core.search.SearchMatch;
import org.eclipse.jdt.core.search.SearchParticipant;
import org.eclipse.jdt.core.search.SearchPattern;
import org.eclipse.jdt.core.search.SearchRequestor;
import org.eclipse.jdt.internal.ui.refactoring.contentassist.ControlContentAssistHelper;
import org.eclipse.jdt.internal.ui.viewsupport.JavaUILabelProvider;
import org.eclipse.jface.contentassist.IContentAssistSubjectControl;
import org.eclipse.jface.contentassist.ISubjectControlContentAssistProcessor;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.contentassist.IContextInformationValidator;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Text;

/**
 * @author Rob Stryker
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class AopUICompletionProcessor implements IContentAssistProcessor, ISubjectControlContentAssistProcessor {
	
	public static final int CLAZZ = 0;
	public static final int ANNOTATION = 1;
	public static final int TYPEDEF = 2;
	public static final int INSTANCEOF = 3;
	
	
	
	private Text text;
	protected String textBoxString, packageName, remainder;
	protected ArrayList packageMatches, typeMatches;

	protected boolean allowsClasses, allowsInterfaces, 
					  allowsAnnotations, hasPackageWildcards;
	
	
	
	public AopUICompletionProcessor(Text text) {
		this(text, true, true, false, false);
	}

	public AopUICompletionProcessor(Text text, boolean allowsClasses, boolean allowsInterfaces, 
				boolean allowsAnnotations, boolean hasPackageWildcards) {
		this.text = text;
		this.allowsAnnotations = allowsAnnotations;
		this.allowsClasses = allowsClasses;
		this.allowsInterfaces = allowsInterfaces;
		this.hasPackageWildcards = hasPackageWildcards;
		ControlContentAssistHelper.createTextContentAssistant(text, this);

	}
	
	public ICompletionProposal[] computeCompletionProposals(ITextViewer viewer, int offset) {
		return null;
	}

	public IContextInformation[] computeContextInformation(ITextViewer viewer, int offset) {
		return null;
	}

	public char[] getCompletionProposalAutoActivationCharacters() {
		return null;
	}

	public char[] getContextInformationAutoActivationCharacters() {
		return null;
	}

	public String getErrorMessage() {
		return null;
	}

	public IContextInformationValidator getContextInformationValidator() {
		return null;
	}

	public IContextInformation[] computeContextInformation(IContentAssistSubjectControl contentAssistSubjectControl, int documentOffset) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public ICompletionProposal[] computeCompletionProposals(IContentAssistSubjectControl contentAssistSubjectControl, int documentOffset) {
		
		parseTextboxString();
		
		packageMatches = getPackageProposals(packageName, textBoxString, remainder);
		IPackageFragment packageMatch = getPackageMatch(packageName, textBoxString);
		typeMatches = getTypeMatches(packageMatch, remainder);
		
		filterMatches();
		
		AopUICompletionProposal[] proposals = generateProposals(packageMatches, typeMatches, textBoxString);
		ICompletionProposal[] processedProposals = postProcessProposals(proposals);
		return proposals;
	}
	
	
	protected void filterMatches() {
		String lastName = "";
		for(Iterator i = typeMatches.iterator(); i.hasNext();) {
			try {
				IType type = (IType)i.next();
				if( type.getFullyQualifiedName().equals(lastName)) { i.remove(); continue; }
				if( type.isAnnotation() && !allowsAnnotations ) { i.remove(); continue; }
				if( type.isInterface() && !allowsInterfaces ) { i.remove(); continue; }
				if( type.isClass() && !allowsClasses ) { i.remove(); continue; }
				lastName = type.getFullyQualifiedName();
			} catch( JavaModelException jme ) {
			}
		}

		lastName = "";
		for(Iterator i = packageMatches.iterator(); i.hasNext();) {
			IPackageFragment type = (IPackageFragment)i.next();
			if( type.getElementName().equals(lastName)) { i.remove(); continue; }
			lastName = type.getElementName();
		}

		
		
		
	}

	protected void parseTextboxString() {
		textBoxString = text.getText();


		

		int lastDot = textBoxString.lastIndexOf('.');
		if( lastDot == -1 ) {
			packageName = textBoxString;
			remainder = null;				
		} else {
			packageName = textBoxString.substring(0, lastDot);
			remainder = textBoxString.substring(lastDot+1);
		}
		
	}
	
	protected ArrayList getTypeMatches(IPackageFragment packageElement, String remainder ) {
		if( packageElement != null && remainder != null ) {
			try {
				IClassFile[] classFiles = packageElement.getClassFiles();
				ICompilationUnit[] compUnits = packageElement.getCompilationUnits();
				ArrayList returnList = new ArrayList();
				
				for( int i = 0; i < classFiles.length; i++ ) {
					String typeName = classFiles[i].getType().getElementName();
					if( typeName.equals("")) continue;
					if( typeName.toLowerCase().startsWith(remainder.toLowerCase()))
						returnList.add(classFiles[i].getType());
				}

				for( int i = 0; i < compUnits.length; i++ ) {
					IType type = compUnits[i].findPrimaryType();
					String typeName = type.getElementName();
					if( typeName.toLowerCase().startsWith(remainder.toLowerCase()))
						returnList.add(type);
				}

				
				return returnList;
			} catch( JavaModelException jme ) {
				
			}
		} 

		return new ArrayList();
	}
	
	protected IPackageFragment getPackageMatch(String packageName, String textBoxString) {
		IJavaSearchScope scope = SearchEngine.createWorkspaceScope();
		SearchPattern packagePattern = SearchPattern.createPattern(packageName, 
				IJavaSearchConstants.PACKAGE, IJavaSearchConstants.DECLARATIONS,
				SearchPattern.R_EXACT_MATCH );
		
		
		
		
		SearchEngine searchEngine = new SearchEngine();
		
		LocalTextfieldSearchRequestor requestor = new LocalTextfieldSearchRequestor(this);
		try {
			searchEngine.search(packagePattern, new SearchParticipant[] {SearchEngine.getDefaultSearchParticipant()}, 
					scope, requestor, new NullProgressMonitor());
			
			ArrayList results = requestor.getResults();
			if( results.size() != 1 ) return null;
			
			return (IPackageFragment)results.get(0);
			
		} catch ( CoreException ce ) {
			
		}
		return null;
	}
	
	protected ArrayList getPackageProposals( String packageName, String textBoxString, String remainder ) {
		IJavaSearchScope scope = SearchEngine.createWorkspaceScope();
		SearchPattern packagePattern = SearchPattern.createPattern(textBoxString, 
				IJavaSearchConstants.PACKAGE, IJavaSearchConstants.DECLARATIONS,
				SearchPattern.R_PREFIX_MATCH);
		
		
		
		
		SearchEngine searchEngine = new SearchEngine();
		
		LocalTextfieldSearchRequestor requestor = new LocalTextfieldSearchRequestor(this);
		try {
			searchEngine.search(packagePattern, new SearchParticipant[] {SearchEngine.getDefaultSearchParticipant()}, 
					scope, requestor, new NullProgressMonitor());
			
			ArrayList results = requestor.getResults();
			Collections.sort(results, new Comparator() {

				public int compare(Object o1, Object o2) {
					if( !(o1 instanceof IPackageFragment)) return 0;
					if( !(o2 instanceof IPackageFragment)) return 0;
					
					IPackageFragment o1a = (IPackageFragment)o1;
					IPackageFragment o2a = (IPackageFragment)o2;
					return o1a.getElementName().compareTo(o2a.getElementName());
				} 
				
			});


			return results;

		} catch( CoreException ce) {
			
		}
		return new ArrayList();
	}
	
	
	public AopUICompletionProposal[] generateProposals(ArrayList packages, ArrayList types, String textboxString) {
		JavaUILabelProvider imageDelegate = new JavaUILabelProvider();

		ArrayList list = new ArrayList();
		
//		int size = packages.size() + types.size();
//		AopUICompletionProposal[] props = new AopUICompletionProposal[size];
		
//		int index = 0;
		

		for( Iterator i = types.iterator(); i.hasNext();) {
			IType type  = (IType)i.next();
			CompletionProposal p = new CompletionProposal(
					type.getFullyQualifiedName(), 0, textboxString.length(), 
					type.getFullyQualifiedName().length(), imageDelegate.getImage(type), type.getElementName(), null, null);
//			props[index++] = new AopUICompletionProposal(AopUICompletionProposal.TYPE, p);
			list.add( new AopUICompletionProposal(AopUICompletionProposal.TYPE, p));
		}
		
		for( Iterator i = packages.iterator(); i.hasNext();) {
			IPackageFragment fragment = (IPackageFragment)i.next();
			CompletionProposal p= new CompletionProposal(
					fragment.getElementName(), 0, textboxString.length(), 
					fragment.getElementName().length(), imageDelegate.getImage(fragment), null, null, null);
			list.add( new AopUICompletionProposal(AopUICompletionProposal.TYPE, p));
			
			// if we allow .*, also add that as a suggestion.
			if( hasPackageWildcards ) {
				CompletionProposal p2 = new CompletionProposal(
						fragment.getElementName() + ".*", 0, textboxString.length(), 
						fragment.getElementName().length()+2, imageDelegate.getImage(fragment), null, null, null);
				list.add( new AopUICompletionProposal(AopUICompletionProposal.TYPE, p2));
			}
		}
		
		
		AopUICompletionProposal props[] = new AopUICompletionProposal[list.size()];
		// Finally convert to array
		for( int i = 0; i < list.size(); i++ ) {
			props[i] = (AopUICompletionProposal)list.get(i);
		}
		return props;
	}
		
	public ICompletionProposal[] postProcessProposals(AopUICompletionProposal[] proposals) {
//		ArrayList newVals = new ArrayList();
//		
//		for( int i = 0; i < proposals.length; i++ ) {
//			newVals.add(proposals[i]);
//			if( proposals[i].getType() == AopUICompletionProposal.PACKAGE ) {
//				if( hasPackageWildcards ) {
//					
//				}
//			}
//		}
		return proposals;
	}

	
	private static class LocalTextfieldSearchRequestor extends SearchRequestor {

		private AopUICompletionProcessor processor;
		private ArrayList results;
		
		public LocalTextfieldSearchRequestor ( AopUICompletionProcessor processor) {
			this.processor = processor;
			results = new ArrayList();
		}

		public void acceptSearchMatch(SearchMatch match) throws CoreException {
			results.add(match.getElement());			
		}
		
		public void endReporting() {
		}
		
		public ArrayList getResults() {
			return results;
		}

		
	}

	public void setType(int type) {
		switch(type) {
			case CLAZZ:
				allowsAnnotations = false;
				allowsClasses = allowsInterfaces = hasPackageWildcards = true;
				break;
			case ANNOTATION:
				allowsAnnotations = allowsInterfaces = true;
				hasPackageWildcards = allowsClasses = false;
				break;
			case INSTANCEOF:
				allowsInterfaces = allowsClasses = true;
				allowsAnnotations = hasPackageWildcards = false;
				break;
			case TYPEDEF:
				allowsInterfaces = allowsClasses = 
					allowsAnnotations = hasPackageWildcards = false;
				break;
		
		}
	}

	
	protected static class AopUICompletionProposal implements ICompletionProposal {
		public static final int PACKAGE = 0;
		public static final int TYPE = 1;
		
		private int type;
		private CompletionProposal delegate;
		
		public AopUICompletionProposal(int type, CompletionProposal delegate) {
			this.type = type;
			this.delegate = delegate;
		}

		public void apply(IDocument document) {
			delegate.apply(document);
		}

		public Point getSelection(IDocument document) {
			return delegate.getSelection(document);
		}

		public String getAdditionalProposalInfo() {
			return delegate.getAdditionalProposalInfo();
		}

		public String getDisplayString() {
			return delegate.getDisplayString();
		}

		public Image getImage() {
			return delegate.getImage();
		}

		public IContextInformation getContextInformation() {
			return delegate.getContextInformation();
		}
		
		public int getType() {
			return this.type;
		}
		
	}


	public boolean allowsAnnotations() {
		return allowsAnnotations;
	}

	public void setAllowsAnnotations(boolean allowAnnotations) {
		this.allowsAnnotations = allowAnnotations;
	}

	public boolean allowsClasses() {
		return allowsClasses;
	}

	public void setAllowsClasses(boolean allowsClasses) {
		this.allowsClasses = allowsClasses;
	}

	public boolean allowsInterfaces() {
		return allowsInterfaces;
	}

	public void setAllowsInterfaces(boolean allowsInterfaces) {
		this.allowsInterfaces = allowsInterfaces;
	}

	public boolean hasPackageWildcards() {
		return hasPackageWildcards;
	}

	public void setHasPackageWildcards(boolean hasPackageWildcards) {
		this.hasPackageWildcards = hasPackageWildcards;
	}

}
