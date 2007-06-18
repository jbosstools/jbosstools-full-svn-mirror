/*
 * JBoss, the OpenSource J2EE webOS
 * 
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package org.jboss.ide.eclipse.jbosscache.model.internal;

import org.jboss.cache.ExtendedTreeCacheListener;

/**
 * /**
 * ExtendedTreeCacheListener related operations
 * Note: Only use internal; not implement this, just extend AbstractTreeCacheManagerListener
 * Just extends ExtendedTreeCacheListener. Using this not break the API when cahnges in ExtendedTreeCacheListener
 * @see org.jboss.ide.eclipse.jbosscache.model.cache.AbstractTreeCacheManagerListener
 * @author Owner
 */
public interface ITreeCacheManagerExtendListener extends ExtendedTreeCacheListener
{

}
