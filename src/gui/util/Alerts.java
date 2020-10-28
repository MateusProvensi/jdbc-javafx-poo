package gui.util;

import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

public class Alerts {

	public static void mostrarAlerta(String titulo, String cabecalho, String mensagem, AlertType tipoAlerta) {
		
		Alert alerta = new Alert(tipoAlerta);
		
		alerta.setTitle(titulo);
		alerta.setHeaderText(cabecalho);
		alerta.setContentText(mensagem);
		
		alerta.show();
	}
	
	public static Optional<ButtonType> mostrarTelaConfirmacao(String titulo, String conteudo){
		Alert alerta = new Alert(AlertType.CONFIRMATION);
		alerta.setTitle(titulo);
		alerta.setHeaderText(null);
		alerta.setContentText(conteudo);
		
		return alerta.showAndWait();
	}
	
}
