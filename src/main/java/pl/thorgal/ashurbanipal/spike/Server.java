package pl.thorgal.ashurbanipal.spike;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CountDownLatch;

import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.Group;
import javafx.scene.control.TextArea;

public class Server {

	private Group root = new Group();
	private TextArea textLog;
	String inputLine;

	public Server() {
		textLog = new TextArea();
		// textLog.setDisable(true);

		root.getChildren().addAll(textLog);
	}

	public Group getGroupNode() {
		return root;
	}

	public void startListeningForConnections(int portNumber) {
		textLog.appendText("Awaiting connections on port " + portNumber + '\n');

		Service<Void> service = new Service<Void>() {

			@Override
			protected Task<Void> createTask() {
				return new Task<Void>() {

					@Override
					protected Void call() throws Exception {

						// Background work
						ServerSocket serverSocket = new ServerSocket(portNumber);
						Socket clientSocket = serverSocket.accept();
						BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

						while ((inputLine = in.readLine()) != null) {

							final CountDownLatch latch = new CountDownLatch(1);
							Platform.runLater(new Runnable() {
								@Override
								public void run() {
									try {
										// FX Stuff done here
										textLog.appendText("Message received: " + inputLine + '\n');

									} finally {
										latch.countDown();
									}
								}
							});
							latch.await();
						}

						// Keep with the background work
						serverSocket.close();
						return null;
					}
				};
			}
		};
		service.start();
	}
}
