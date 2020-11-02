package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import application.Main;
import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import modelo.servicos.ClienteServico;
import modelo.servicos.EmpresaServico;
import modelo.servicos.FornecedorMarcaServico;
import modelo.servicos.FornecedorServico;
import modelo.servicos.FuncionarioServico;
import modelo.servicos.MarcaServico;

public class MainViewController implements Initializable{

	@FXML
	private MenuItem menuItemRegistrarCliente;
	
	@FXML 
	private MenuItem menuItemRegistrarEmpresa;
	
	@FXML
	private MenuItem menuItemRegistrarFornecedor;
	
	@FXML
	private MenuItem menuItemRegistrarFornecedorMarca;
	
	@FXML
	private MenuItem menuItemRegistrarFuncionario;
	
	@FXML
	private MenuItem menuItemRegistrarItem;
	
	@FXML
	private MenuItem menuItemRegistrarMarca;
	
	@FXML
	private MenuItem menuItemVendaFazerVenda;
	
	@FXML
	private MenuItem menuItemAjudaAjuda;
	
	@FXML
	public void onMenuItemRegistrarClienteAcao(){
		carregarView("/gui/ClienteLista.fxml", (ClienteListaController controller) -> {
			controller.setClienteServico(new ClienteServico());
			controller.updateVisualizacaoTabela();
		});
	}
	
	@FXML
	public void onMenuItemRegistrarEmpresaAcao() {
		carregarView("/gui/EmpresaLista.fxml", (EmpresaListaControler controller) -> {
			controller.setEmpresaServico(new EmpresaServico());
			controller.updateVisualizacaoTabela();
		});;
	}
	
	@FXML
	public void onMenuItemRegistrarFornecedorAcao() {
		carregarView("/gui/FornecedorLista.fxml", (FornecedorListaController controller) -> {
			controller.setFornecedorServico(new FornecedorServico());
			controller.updateVisualizacaoTabela();
		});
	}
	
	@FXML
	public void onMenuItemRegistrarFornecedorMarcaAcao() {
		carregarView("/gui/FornecedorMarcaLista.fxml", (FornecedorMarcaListaController controller) -> {
			controller.setFornecedorMarcaServico(new FornecedorMarcaServico());
			controller.updateVisualizacaoTabela();
		});
	}
	
	@FXML
	public void onMenuItemRegistrarFuncionario() {
		carregarView("/gui/FuncionarioLista.fxml", (FuncionarioListaController controller) -> {
			controller.setFuncionarioServico(new FuncionarioServico());
			controller.updateVisualizacaoTabela();
		});
	}
	
	@FXML
	public void onMenuItemRegistrarItemAcao() {
		System.out.println("onMenuItemRegistrarItemAcao");
	}
	
	@FXML
	public void onMenuItemRegistrarMarcaAcao() {
		carregarView("/gui/MarcaLista.fxml", (MarcaListaController controller) -> {
			controller.setMarcaServico(new MarcaServico());
			controller.updateVisualizacaoTabela();
		});
	}
	
	@FXML
	public void onMenuItemVendaFazerVendaAcao() {
		System.out.println("onMenuItemVendaFazerVendaAcao");
	}
	
	@FXML
	public void onMenuItemAjudaAjudaAcao() {
		System.out.println("onMenuItemAjudaAjudaAcao");
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
	}

	private synchronized <T> void carregarView(String caminho, Consumer <T> acaoInicializacao) {
		try {
			
			FXMLLoader loader = new FXMLLoader(getClass().getResource(caminho));
			VBox novaVBox = loader.load();
			
			Scene mainScene = Main.getMainScene();
			VBox mainVbox = (VBox) ((ScrollPane)mainScene.getRoot()).getContent();
			
			Node mainMenu = mainVbox.getChildren().get(0);
			mainVbox.getChildren().clear();
			mainVbox.getChildren().add(mainMenu);
			mainVbox.getChildren().addAll(novaVBox.getChildren());
			
			T controller = loader.getController();
			acaoInicializacao.accept(controller);
			
		} catch (IOException e) {
			Alerts.mostrarAlerta("IO Exception", "Erro ao carregar a view", e.getMessage(), AlertType.ERROR);
		}
	}
	
}
