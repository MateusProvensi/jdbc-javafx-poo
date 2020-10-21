package gui.util;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class Alerts {

	public static void mostrarAlerta(String titulo, String cabecalho, String mensagem, AlertType tipoAlerta) {
		
		Alert alerta = new Alert(tipoAlerta);
		
		alerta.setTitle(titulo);
		alerta.setHeaderText(cabecalho);
		alerta.setContentText(mensagem);
		
		alerta.show();
	}
	
}
