package org.jboss.ide.eclipse.as.ui.views;

import java.util.Date;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.ui.internal.provisional.ManagedUIDecorator;
import org.jboss.ide.eclipse.as.core.extensions.events.IEventCodes;
import org.jboss.ide.eclipse.as.core.extensions.polling.JMXPoller;
import org.jboss.ide.eclipse.as.core.server.internal.PollThread;
import org.jboss.ide.eclipse.as.ui.JBossServerUISharedImages;
import org.jboss.ide.eclipse.as.ui.views.ServerLogView.EventCategory;

public class LogLabelProvider extends LabelProvider {
	public Image getImage(Object element) {
		if( element instanceof EventCategory ) {
			int type = ((EventCategory)element).getType();
			if( type == IEventCodes.POLLING_CODE) 
				return new ManagedUIDecorator().getStateImage(IServer.STATE_STARTING, ILaunchManager.RUN_MODE, 1);
			if( type == IEventCodes.PUBLISHING_CODE)
				return JBossServerUISharedImages.getImage(JBossServerUISharedImages.PUBLISH_IMAGE);
		}
		
		
		if( element instanceof LogEntry) {
			int code = ((LogEntry)element).getCode();
			int majorType = code & IEventCodes.MAJOR_TYPE_MASK;
			switch(majorType) {
			case IEventCodes.POLLING_CODE:
				return handlePollImage((LogEntry)element, code);
			case IEventCodes.PUBLISHING_CODE:
				return handlePublishImage((LogEntry)element, code);
			}
		}
		return null;
	}

	public Image handlePublishImage(LogEntry element, int code) {
		if( (code & IEventCodes.SINGLE_FILE_SUCCESS_MASK) == 0) 
			return PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJS_ERROR_TSK); // error
		if( (code & IEventCodes.SINGLE_FILE_TYPE_MASK) == 0) 
			return JBossServerUISharedImages.getImage(JBossServerUISharedImages.PUBLISH_IMAGE);
		else
			return JBossServerUISharedImages.getImage(JBossServerUISharedImages.UNPUBLISH_IMAGE);
	}
	
	public Image handlePollImage(LogEntry element, int code) {
		if( (code & IEventCodes.FULL_POLLER_MASK) == IEventCodes.POLLING_ROOT_CODE) {
			int state = (code & PollThread.STATE_MASK) >> 3;
			return new ManagedUIDecorator().getStateImage(state, ILaunchManager.RUN_MODE, 1);
		} else if( (code & IEventCodes.FULL_POLLER_MASK) == IEventCodes.JMXPOLLER_CODE) {
			if( element.getSeverity() == IStatus.WARNING)
				return PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJS_WARN_TSK);
			int state = code & 0xF;
			switch(state) {
			case JMXPoller.STATE_STARTED: 
				return new ManagedUIDecorator().getStateImage(IServer.STATE_STARTED, ILaunchManager.RUN_MODE, 1);
			case JMXPoller.STATE_STOPPED:
				return new ManagedUIDecorator().getStateImage(IServer.STATE_STOPPED, ILaunchManager.RUN_MODE, 1);
			case JMXPoller.STATE_TRANSITION:
				return new ManagedUIDecorator().getStateImage(IServer.STATE_STARTING, ILaunchManager.RUN_MODE, 1);
			}
		} else if( (code & IEventCodes.FULL_POLLER_MASK) == IEventCodes.BEHAVIOR_STATE_CODE) {
			switch(code) {
			case IEventCodes.BEHAVIOR_FORCE_STOP:
			case IEventCodes.BEHAVIOR_FORCE_STOP_FAILED:
				return PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJS_ERROR_TSK);
			case IEventCodes.BEHAVIOR_PROCESS_TERMINATED:
				return PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJS_INFO_TSK);
			}
		}		
		return null;
	}
	
	public String getText(Object element) {
		if( element instanceof EventCategory ) {
			int type = ((EventCategory)element).getType();
			if( type == IEventCodes.POLLING_CODE)
				return "Server Startup / Shutdown";
			if( type == IEventCodes.PUBLISHING_CODE)
				return "Publishing";
		}

		
		if( element instanceof LogEntry ) {
			// queue for Decoration
			String message = ((LogEntry)element).getMessage();
			return message + addSuffix((LogEntry)element);
		}
		return element == null ? "" : element.toString();//$NON-NLS-1$
	}
	
	protected String addSuffix(LogEntry entry) {
		long diff = new Date().getTime() - entry.getDate().getTime();
		long sec = diff / 1000;
		long minutes = sec / 60;
		if( minutes > 0 )
			sec -= (minutes * 60);
		long hours = minutes / 60;
		if( hours > 0 ) {
			minutes -= (hours * 60);
			sec -= (hours * 60 * 60);
		}
		if( hours > 0 ) {
			return "  [" + hours + " hours, " + minutes + " minutes ago]";
		} else if( minutes > 0 ) {
			return "  [" + minutes + " minutes, " + sec + " seconds ago]";
		} else {
			return "  [" + sec + " seconds ago]";
		}
	}
}
