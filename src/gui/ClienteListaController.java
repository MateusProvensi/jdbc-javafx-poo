package gui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import application.Main;
import bd.BDException;
import gui.ouvintes.DadosMudancaOuvintes;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import modelo.entidades.Cliente;
import modelo.servicos.ClienteServico;

public class ClienteListaController implements Initializable, DadosMudancaOuvintes{

	private ClienteServico servico;
	
	private ObservableList<Cliente> obsList;
	
	@FXML
	private TableView<Cliente> tableViewCliente;
	
	@FXML
	private TableColumn<Cliente, Integer> tableColumnId;
	
	@FXML
	private TableColumn<Cliente, String> tableColumnNome;
	
	@FXML
	private TableColumn<Cliente, String> tableColumnSobrenome;
	
	@FXML
	private TableColumn<Cliente, String> tableColumnCpf;
	
	@FXML
	private TableColumn<Cliente, String> tableColumnRg;
	
	@FXML
	private TableColumn<Cliente, String> tableColumnTelefone;
	
	@FXML 
	TableColumn<Cliente, Cliente> tableColumnEDIT;
	
	@FXML 
	TableColumn<Cliente, Cliente> tableColumnREMOVE;
	
	@FXML
	private Button btNovo;
	
	@FXML
	public void onBtNovoAcao(ActionEvent evento) {
		Stage parentStage = Utils.stageAtual(evento);
		Cliente obj = new Cliente();
		criarFormularioCliente(obj, "/gui/ClienteFormulario.fxml", parentStage);
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		iniciarNodes();		
	}

	public void setClienteServico(ClienteServico servico) {
		this.servico = servico;
	}
	
	private void iniciarNodes() {
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("idCliente"));
		tableColumnNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
		tableColumnSobrenome.setCellValueFactory(new PropertyValueFactory<>("sobrenome"));
		tableColumnCpf.setCellValueFactory(new PropertyValueFactory<>("cpf"));
		tableColumnRg.setCellValueFactory(new PropertyValueFactory<>("rg"));
		tableColumnTelefone.setCellValueFactory(new PropertyValueFactory<>("telefone"));
		
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewCliente.prefHeightProperty().bind(stage.heightProperty());
	}
	
	public void updateVisualizacaoTabela() {
		if (servico == null) {
			throw new IllegalStateException("Servico ta nulo");
		}
		List<Cliente> lista = servico.acharTodos();
		obsList = FXCollections.observableArrayList(lista);
		tableViewCliente.setItems(obsList);
		iniciarBotoesEditar();
		iniciarBotoesExcluir();
	}
	
	private void criarFormularioCliente(Cliente obj, String caminho, Stage parentStage) {
		
		try {
			
			FXMLLoader loader = new FXMLLoader(getClass().getResource(caminho));
			Pane pane = loader.load();
			
			ClienteFormularioController controller = loader.getController();
			controller.setCliente(obj);
			controller.setClienteServico(new ClienteServico());
			controller.addOuvintes(this);
			controller.updateDadosFormulario();
			
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Criar cliente");
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
			dialogStage.initOwner(parentStage);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();
			
		} catch (IOException e) {
			Alerts.mostrarAlerta("IOException", "Erro ao carregar a view", e.getMessage(), AlertType.ERROR);
		}
		
	}

	@Override
	public void onMudancaDados() {
		updateVisualizacaoTabela();
	}
	
	private void iniciarBotoesEditar() {
		tableColumnEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEDIT.setCellFactory(param -> new TableCell<Cliente, Cliente>() {
			private final Button button = new Button("Editar");

			@Override
			protected void updateItem(Cliente obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(
						event -> criarFormularioCliente(obj, "/gui/ClienteFormulario.fxml", Utils.stageAtual(event)));
			}
		});
	}

	private void iniciarBotoesExcluir() {
		tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnREMOVE.setCellFactory(param -> new TableCell<Cliente, Cliente>() {
			private final Button button = new Button("Remover");

			@Override
			protected void updateItem(Cliente obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event -> removerEntidade(obj));
			}
		});
	}

	private void removerEntidade(Cliente obj) {
		Optional<ButtonType> result = Alerts.mostrarTelaConfirmacao("Conformation", "Are you sure to delete?");

		if (result.get() == ButtonType.OK) {

			if (servico == null) {
				throw new IllegalStateException("Service was null");
			}
			try {
				servico.delete(obj);
				updateVisualizacaoTabela();
			} catch (BDException e) {
				Alerts.mostrarAlerta("Error removing object", null, e.getMessage(), AlertType.ERROR);
			}

		}
	}
	
}
