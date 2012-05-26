package org.jboss.tools.deltacloud.ui.views.cloud.cnf;

import java.util.Collection;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.jboss.tools.deltacloud.core.DeltaCloud;
import org.jboss.tools.deltacloud.core.DeltaCloudException;
import org.jboss.tools.deltacloud.core.DeltaCloudImage;
import org.jboss.tools.deltacloud.core.IImageFilter;
import org.jboss.tools.deltacloud.ui.views.cloud.cnf.CloudContentProvider.CloudAdaptable;

public class FiltersContentProvider implements ITreeContentProvider {

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// TODO Auto-generated method stub

	}

	@Override
	public Object[] getElements(Object inputElement) {
		return getChildren(inputElement);
	}

	public class CloudFilterPair extends CloudAdaptable {
		private IImageFilter filter;
		public CloudFilterPair(DeltaCloud cloud, IImageFilter filter) {
			super(cloud);
			this.filter = filter;
		}
		public String toString() {
			return "Filter: " + filter.toString();
		}
	}
	
	@Override
	public Object[] getChildren(Object parentElement) {
		if( parentElement instanceof DeltaCloud ) {
			IImageFilter filter = ((DeltaCloud)parentElement).getImageFilter();
			if( filter != null ) {
				CloudFilterPair cfp = new CloudFilterPair(((DeltaCloud)parentElement), filter);
				return new Object[]{ cfp };
			}
		}
		if( parentElement instanceof CloudFilterPair ) {
			try {
				CloudFilterPair cfp = (CloudFilterPair)parentElement;
				Collection<DeltaCloudImage> filteredImages = cfp.filter.filter(cfp.getCloud().getImages());
				return filteredImages.toArray(new DeltaCloudImage[filteredImages.size()]);
			} catch(DeltaCloudException dce) { /* ignore */ }
		}
		return new Object[]{};
	}

	@Override
	public Object getParent(Object element) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		// TODO Auto-generated method stub
		return false;
	}

}
