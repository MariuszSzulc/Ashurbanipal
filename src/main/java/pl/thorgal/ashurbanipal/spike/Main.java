package pl.thorgal.ashurbanipal.spike;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * Hello world!
 *
 */
public class Main extends Application {

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
		primaryStage.setTitle("Hello World!");

		TextArea textArea = new TextArea();

		Button button = new Button();
		button.setText("Say 'Hello World'");
		button.setOnAction(e -> {
			textArea.appendText("Hello World!\n");
		});

		BorderPane root = new BorderPane(textArea);
		root.setTop(button);

		primaryStage.setScene(new Scene(root, 300, 250));
		primaryStage.show();
	}
}
