package org.jboss.tools.deltacloud.core;

import org.jboss.tools.deltacloud.core.client.Image;


public class DeltaCloudImage {

	private Image image;
	
	public DeltaCloudImage(Image image) {
		this.image = image;
	}
	
	public String getName() {
		return image.getName();
	}
	
	public String getArchitecture() {
		return image.getArchitecture();
	}
	
	public String getDescription() {
		return image.getDescription();
	}
	
	public String getId() {
		return image.getId();
	}
}
