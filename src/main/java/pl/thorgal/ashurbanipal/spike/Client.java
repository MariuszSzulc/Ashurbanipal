//this is git status test //
package pl.thorgal.ashurbanipal.spike;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

public class Client {

	private Group root = new Group();
	private PrintWriter serverPipe;

	private TextField textInput;
	private TextArea textArea;

	public Client() {

		textInput = new TextField();

		Button sendButton = new Button("Send Message");
		sendButton.setOnAction(e -> sendMessageToServer());

		textArea = new TextArea();

		BorderPane panel = new BorderPane(textArea);
		panel.setTop(new HBox(textInput, sendButton));
		root.getChildren().add(panel);
	}

	private void sendMessageToServer() {
		serverPipe.println(textInput.getText());
		textArea.appendText(textInput.getText() + '\n');
		textInput.clear();
	}

	public Parent getGroupNode() {
		return root;
	}

	public void connectToServer(String hostName, int portNumber) {

		try {
			Socket echoSocket = new Socket(hostName, portNumber);
			serverPipe = new PrintWriter(echoSocket.getOutputStream(), true);

		} catch (UnknownHostException e) {
			textArea.appendText(e.getLocalizedMessage() + '\n');

		} catch (IOException e) {
			textArea.appendText(e.getLocalizedMessage() + '\n');
		}
	}
}
