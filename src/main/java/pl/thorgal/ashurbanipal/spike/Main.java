package pl.thorgal.ashurbanipal.spike;

import javafx.application.Application;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 * Hello world!
 *
 */
public class Main extends Application {

	private Scene scene;
	private TextField hostInput;
	private TextField portInput;

	private UpnpConfiguration upnpConfiguration = new UpnpConfiguration();
	private TextField forwardingHostInput;
	private TextField forwardingPortInput;

	/**
	 * Required to start the jar as an executable.
	 *
	 * @param args
	 *            the arguments
	 */
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("Connectivity Test");

		// Port forwarding stuff
		Button registerUpnpButton = new Button("Forward the port");
		registerUpnpButton.setOnAction(e -> {
			upnpConfiguration.createPortForwardingRule(forwardingHostInput.getText(),
					Integer.parseInt(forwardingPortInput.getText()));
		});

		forwardingHostInput = new TextField("192.168.5.68");
		forwardingPortInput = new TextField("50255");
		HBox forwardingBox = new HBox(forwardingHostInput, forwardingPortInput);

		// Client server stuff
		Button serverButton = new Button("Start server");
		serverButton.setOnAction(e -> {
			Server server = new Server();
			scene.setRoot(server.getGroupNode());
			server.startListeningForConnections(Integer.parseInt(portInput.getText()));
		});

		Button clientButton = new Button("Start client");
		clientButton.setOnAction(e -> {
			Client client = new Client();
			scene.setRoot(client.getGroupNode());
			client.connectToServer(hostInput.getText(), Integer.parseInt(portInput.getText()));
		});
		HBox buttonsBox = new HBox(serverButton, clientButton);

		hostInput = new TextField("localhost");
		portInput = new TextField("50255");
		HBox inputBox = new HBox(hostInput, portInput);

		FlowPane panel = new FlowPane(Orientation.VERTICAL);
		panel.getChildren().addAll(forwardingBox, registerUpnpButton, inputBox, buttonsBox);

		scene = new Scene(panel, 300, 250);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	@Override
	public void stop() {
		upnpConfiguration.cleanUp();
	}
}
