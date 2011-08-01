package org.jboss.ide.eclipse.as.core.extensions.polling;

import java.io.FileNotFoundException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.wst.server.core.IServer;
import org.jboss.ide.eclipse.as.core.JBossServerCorePlugin;
import org.jboss.ide.eclipse.as.core.server.IServerStatePoller2;
import org.jboss.ide.eclipse.as.core.server.internal.JBossServer;
import org.jboss.ide.eclipse.as.core.server.internal.PollThread;
import org.jboss.ide.eclipse.as.core.server.internal.ServerStatePollerType;
import org.jboss.ide.eclipse.as.core.util.ServerConverter;

public class WebPortPoller implements IServerStatePoller2 {

	public static final String WEB_POLLER_ID = "org.jboss.ide.eclipse.as.core.runtime.server.WebPoller"; //$NON-NLS-1$
	private IServer server;
	private ServerStatePollerType type;
	private boolean canceled, done;
	private boolean state;
	private boolean expectedState;

	@Deprecated
	public void beginPolling(IServer server, boolean expectedState, PollThread pollTread) throws Exception {
	}

	public void beginPolling(IServer server, boolean expectedState) {
		this.server = server;
		this.canceled = done = false;
		this.expectedState = expectedState;
		this.state = !expectedState;
		launchThread();
	}

	protected void launchThread() {
		Thread t = new Thread(new Runnable(){
			public void run() {
				pollerRun();
			}
		}, "Web Poller"); //$NON-NLS-1$
		t.start();
	}
	
	private void pollerRun() {
		done = false;
		String url = getURL(getServer());
		while(!canceled && !done) {
			boolean up = onePing(url);
			if( up == expectedState ) {
				done = true;
				state = expectedState;
			}
		}
	}
	
	private static String getURL(IServer server) {
		String url = "http://"+server.getHost(); //$NON-NLS-1$
		JBossServer jbs = ServerConverter.getJBossServer(server);
		int port = jbs.getJBossWebPort();
		url += ":" + port; //$NON-NLS-1$
		return url;
	}
	
	public static boolean onePing(IServer server) {
		return onePing(getURL(server));
	}
	
	private static boolean onePing(String url) {
		try {
			URL pingUrl = new URL(url);
			URLConnection conn = pingUrl.openConnection();
			((HttpURLConnection)conn).getResponseCode();
			return true;
		} catch( FileNotFoundException fnfe ) {
			return true;
		} catch( Exception e) {
		}
		return false;
	}
	
	public ServerStatePollerType getPollerType() {
		return type;
	}

	public void setPollerType(ServerStatePollerType type) {
		this.type = type;
	}

	public IServer getServer() {
		return server;
	}

	public boolean isComplete() throws PollingException, RequiresInfoException {
		return done;
	}

	public boolean getState() throws PollingException, RequiresInfoException {
		return state;
	}

	public void cleanup() {
	}

	public List<String> getRequiredProperties() {
		return new ArrayList<String>();
	}

	public void failureHandled(Properties properties) {
	}

	public void cancel(int type) {
		canceled = true;
	}

	public int getTimeoutBehavior() {
		return TIMEOUT_BEHAVIOR_FAIL;
	}

	public IStatus getCurrentStateSynchronous(IServer server) {
		String url = getURL(server);
		boolean b = onePing(url);
		if( b ) {
			Status s = new Status(IStatus.OK, JBossServerCorePlugin.PLUGIN_ID, 
					"Web Poller find a running server at url " + url); //$NON-NLS-1$
			return s;
		}
		Status s = new Status(IStatus.INFO, JBossServerCorePlugin.PLUGIN_ID, 
				"Web Poller did not find a running server at url " + url); //$NON-NLS-1$
		return s;
	}
}
