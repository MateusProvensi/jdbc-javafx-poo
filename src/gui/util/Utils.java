package gui.util;

import javafx.event.Event;
import javafx.scene.Node;
import javafx.stage.Stage;

public class Utils {

	public static Stage stageAtual(Event evento) {
		return (Stage) ((Node) evento.getSource()).getScene().getWindow();
	}
	
	public static Integer transformarInteger(String str) {
		try {
			return Integer.parseInt(str);
		} catch (NumberFormatException e) {
			return null;
		}
	}
}
