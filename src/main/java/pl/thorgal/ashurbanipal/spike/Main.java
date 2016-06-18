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

	private static final int PORT_NUMBER = 48127;
	private Scene scene;
	private TextField hostInput;

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

		Button serverButton = new Button("Start server at port: " + PORT_NUMBER);
		serverButton.setOnAction(e -> {
			Server server = new Server();
			scene.setRoot(server.getGroupNode());
			server.startListeningForConnections(PORT_NUMBER);
		});

		Button clientButton = new Button("Start client");
		clientButton.setOnAction(e -> {
			Client client = new Client();
			scene.setRoot(client.getGroupNode());
			client.connectToServer(hostInput.getText(), PORT_NUMBER);
		});

		hostInput = new TextField("localhost");
		HBox clientRequestBox = new HBox(hostInput, clientButton);
		FlowPane panel = new FlowPane(Orientation.VERTICAL);
		panel.getChildren().addAll(serverButton, clientRequestBox);

		scene = new Scene(panel, 300, 250);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
}
