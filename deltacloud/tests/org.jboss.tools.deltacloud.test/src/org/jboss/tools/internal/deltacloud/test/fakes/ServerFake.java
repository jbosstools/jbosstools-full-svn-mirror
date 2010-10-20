/*******************************************************************************
 * Copyright (c) 2010 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.internal.deltacloud.test.fakes;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerFake {

	private ExecutorService executor;
	private int port;
	private String response;

	public ServerFake(int port, String response) {
		this.port = port;
		this.response = response;
	}

	public void start() {
		executor = Executors.newFixedThreadPool(1);
		executor.submit(new ServerFakeSocket(port, response));
	}

	public void stop() {
		executor.shutdownNow();
	}

	private class ServerFakeSocket implements Runnable {
		private String response;
		private ServerSocket serverSocket;

		private ServerFakeSocket(int port, String response) {

			this.response = response;

			try {
				this.serverSocket = new ServerSocket(port);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void run() {
			Socket socket;
			try {
				socket = serverSocket.accept();
				OutputStream outputStream = socket.getOutputStream();
				outputStream.write(response.getBytes());
				outputStream.flush();
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}