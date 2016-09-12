package pl.thorgal.ashurbanipal.spike;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.application.Application;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 * Hello world!
 *
 */
public class Main extends Application {
	
	private static final Logger logger = LoggerFactory.getLogger(Main.class);

	private Scene scene;
	private TextField hostInput;
	private TextField portInput;

	private UpnpConfiguration upnpConfiguration = new UpnpConfiguration();
	private TextField forwardingPortInput;
	private TextArea forwardingResult;

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
		logger.debug("Program started");

		// Port forwarding stuff
		Button registerUpnpButton = new Button("Forward the port");
		registerUpnpButton.setOnAction(e -> {
			try {
				String result = upnpConfiguration.createPortForwardingRule(Integer.parseInt(forwardingPortInput.getText()));
				forwardingResult.appendText(result + '\n');

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		});

		forwardingPortInput = new TextField("50255");
		forwardingResult = new TextArea();
		forwardingResult.setPrefRowCount(4);
		HBox forwardingBox = new HBox(forwardingPortInput, registerUpnpButton);

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
		panel.getChildren().addAll(forwardingBox, forwardingResult, inputBox, buttonsBox);

		scene = new Scene(panel, 300, 250);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	@Override
	public void stop() throws Exception {
		upnpConfiguration.cleanUp();
	}
}
