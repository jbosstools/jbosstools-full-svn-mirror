package org.jboss.tools.vpe.browsersim.ui.skin.android;

import org.jboss.tools.vpe.browsersim.ui.skin.ImageDescriptor;
import org.jboss.tools.vpe.browsersim.ui.skin.ios.ButtonDescriptor;

public class AndroidSkinDescriptor {
	private int bodyGridSize;
	private ImageDescriptor[] bodyGridImageDescriptors;
	private ImageDescriptor iOSDescriptor;
	private ButtonDescriptor backButton;
	private ButtonDescriptor forwardButton;
	private ButtonDescriptor homeButton;
	private ButtonDescriptor refreshButton;

	public AndroidSkinDescriptor(int bodyGridSize,
			ImageDescriptor[] bodyGridImageDescriptors,
			ImageDescriptor iOSDescriptor, ButtonDescriptor backButton,
			ButtonDescriptor forwardButton, ButtonDescriptor homeButton,
			ButtonDescriptor refreshButton) {
		this.bodyGridSize = bodyGridSize;
		this.bodyGridImageDescriptors = bodyGridImageDescriptors;
		this.iOSDescriptor = iOSDescriptor;
		this.backButton = backButton;
		this.forwardButton = forwardButton;
		this.homeButton = homeButton;
		this.refreshButton = refreshButton;
	}

	public int getBodyGridSize() {
		return bodyGridSize;
	}

	public ImageDescriptor[] getBodyGridImageDescriptors() {
		return bodyGridImageDescriptors;
	}

	public ImageDescriptor getAndroidOSDescriptor() {
		return iOSDescriptor;
	}

	public ButtonDescriptor getBackButton() {
		return backButton;
	}

	public ButtonDescriptor getForwardButton() {
		return forwardButton;
	}
	public ButtonDescriptor getHomeButton() {
		return homeButton;
	}
	
	public ButtonDescriptor getRefreshButton() {
		return refreshButton;
	}
}
