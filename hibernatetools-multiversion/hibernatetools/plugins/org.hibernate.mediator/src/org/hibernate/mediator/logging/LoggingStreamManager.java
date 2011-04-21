package org.hibernate.mediator.logging;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;
import org.hibernate.mediator.Messages;

public class LoggingStreamManager {

	private static LoggingStreamManager instance;

	public static synchronized LoggingStreamManager getInstance() {
		if (instance == null) {
			instance = new LoggingStreamManager();
		}
		return instance;
	}
	
	private Map<String, Object[]> loggingStreams = new HashMap<String, Object[]>();

	public MessageConsoleStream findLoggingStream(String name) {
		Object[] console = loggingStreams.get(name);
		if (console == null) {
			console = new Object[2];
			String secondaryId = Messages.KnownConfigurations_hibernate_log
					+ (name == null ? Messages.KnownConfigurations_unknown : name);
			console[0] = new MessageConsole(secondaryId, null);
			IConsoleManager consoleManager = ConsolePlugin.getDefault().getConsoleManager();
			consoleManager.addConsoles(new IConsole[] { (IConsole) console[0] });
			console[1] = ((MessageConsole) console[0]).newMessageStream();
			loggingStreams.put(name, console);
		}
		return (MessageConsoleStream) console[1];
	}

	public void removeLoggingStream(String consoleConfigName) {
		Object[] object = loggingStreams.remove(consoleConfigName);
		if (object != null) {
			MessageConsole mc = (MessageConsole) object[0];
			MessageConsoleStream stream = (MessageConsoleStream) object[1];
			try {
				stream.close();
			} catch (IOException ie) {
				// ignore
			}
			IConsoleManager consoleManager = ConsolePlugin.getDefault().getConsoleManager();
			consoleManager.removeConsoles(new IConsole[] { mc });
		}
	}

}
