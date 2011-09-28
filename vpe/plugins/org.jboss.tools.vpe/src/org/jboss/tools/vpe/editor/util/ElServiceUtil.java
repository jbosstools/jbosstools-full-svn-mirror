package org.jboss.tools.vpe.editor.util;

import java.util.Arrays;
import java.util.Comparator;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Platform;
import org.jboss.tools.common.el.core.ELReferenceList;
import org.jboss.tools.common.el.core.GlobalELReferenceList;
import org.jboss.tools.common.resref.core.ResourceReference;

public class ElServiceUtil {
	
	public static final String DOLLAR_PREFIX = "${"; //$NON-NLS-1$

	public static final String SUFFIX = "}"; //$NON-NLS-1$

	public static final String SHARP_PREFIX = "#{"; //$NON-NLS-1$

	/**
	 * Replace el.
	 * 
	 * @param resourceFile
	 *            the resource file
	 * @param resourceString
	 *            the resource string
	 * 
	 * @return the string
	 * 
	 * @see IELService#replaceEl(IFile, String)
	 */
	public static String replaceEl(IFile file, String resourceString) {
		if (resourceString == null) {
			return ""; //$NON-NLS-1$
		}
		String rst = resourceString;
		ResourceReference[] references = getAllResources(file);
		if (references == null || references.length == 0) {
			return rst;
		}
		references = sortReferencesByScope2(references);
		rst = replace(resourceString, references);
		return rst;
	}

	/**
	 * Creates a copy of {@code references} array and sorts its elements by scope value.
	 * 
	 * References with the lowest scope value ({@link ResourceReference#FILE_SCOPE}) become the first in the array and
	 * so on.
	 * 
	 * @param references
	 *            array to be sorted
	 * @return sorted copy of {@code references}
	 * @author yradtsevich
	 */
	public static ResourceReference[] sortReferencesByScope(ResourceReference[] references) {
		ResourceReference[] sortedReferences = references.clone();
		return sortReferencesByScope(sortedReferences);
	}

	public static ResourceReference[] sortReferencesByScope2(ResourceReference[] references) {
		Arrays.sort(references, new Comparator<ResourceReference>() {
			public int compare(ResourceReference r1, ResourceReference r2) {
				return r1.getScope() - r2.getScope();
			}
		});
		return references;
	}

	public static ResourceReference[] getAllResources(IFile resourceFile) {
		final IPath workspacePath = Platform.getLocation();
		final ResourceReference[] gResources = GlobalELReferenceList.getInstance().getAllResources(workspacePath);
		final ResourceReference[] elResources = ELReferenceList.getInstance().getAllResources(resourceFile);

		int size = gResources.length;
		size += elResources.length;
		ResourceReference[] rst = new ResourceReference[size];

		if (gResources.length > 0) {
			System.arraycopy(gResources, 0, rst, 0, gResources.length);
		}
		if (elResources.length > 0) {
			System.arraycopy(elResources, 0, rst, gResources == null ? 0 : gResources.length, elResources.length);
		}
		return rst;

	}
	
	/**
	 * Replace.
	 * 
	 * @param resourceString
	 *            the resource string
	 * @param references
	 *            the references
	 * 
	 * @return the string
	 */
	public static String replace(String resourceString, ResourceReference[] sortedReferences) {
		String result = resourceString;
		// Values with higher precedence should be replaced in the first place.
		// Expect sorted by scope references.
		final int nPrefLen = getPrefLen(); // for small-simple optimization
		for (ResourceReference rf : sortedReferences) {
			final String tmp = rf.getLocation();
			if (tmp.length() + nPrefLen > resourceString.length()) {
				continue; // no sense for sequence contains checks...
			}
			final String dollarEl = envelopeInDollarEl(tmp);
			final String sharpEl = envelopeInSharpEl(tmp);
			if (resourceString.contains(dollarEl)) {
				result = result.replace(dollarEl, rf.getProperties());
			}
			if (resourceString.contains(sharpEl)) {
				result = result.replace(sharpEl, rf.getProperties());
			}
		}
		return result;
	}

	public static int getPrefLen() {
		int nPrefLen = DOLLAR_PREFIX.length();
		if (nPrefLen > SHARP_PREFIX.length()) {
			nPrefLen = SHARP_PREFIX.length();
		}
		nPrefLen += SUFFIX.length();
		return nPrefLen;
	}

	public static boolean equalsExppression(String value, String expression) {
		final String dollarEl = envelopeInDollarEl(expression);
		final String sharpEl = envelopeInSharpEl(expression);
		if (value.contains(dollarEl) || value.contains(sharpEl)) {
			return true;
		}
		return false;
	}

	/**
	 * Checks if is in reference resources list.
	 * 
	 * @param value
	 *            the value
	 * @param references
	 *            the references
	 * 
	 * @return true, if is in reference resources list
	 */
	public static boolean isInReferenceResourcesList(ResourceReference[] references, String value) {
		for (ResourceReference ref : references) {
			// FIXED FOR JBIDE-3149 by sdzmitrovich
			if (equalsExppression(value, ref.getLocation())) {
				return true;
			}
		}
		return false;
	}

	public static String envelopeInDollarEl(String str) {
		return DOLLAR_PREFIX + str + SUFFIX;
	}

	public static String envelopeInSharpEl(String str) {
		return SHARP_PREFIX + str + SUFFIX;
	}
}
