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
import modelo.entidades.Empresa;
import modelo.servicos.EmpresaServico;

public class EmpresaListaControler implements Initializable, DadosMudancaOuvintes{

	private EmpresaServico servico;
	
	private ObservableList<Empresa> obsList;
	
	@FXML
	private Button btNovo;
	
	@FXML
	private TableView<Empresa> tableViewEmpresa;
	
	@FXML
	private TableColumn<Empresa, Integer> tableColumnId;
	
	@FXML
	private TableColumn<Empresa, String> tableColumnNome;
	
	@FXML
	private TableColumn<Empresa, String> tableColumnCnpj;
	
	@FXML
	private TableColumn<Empresa, String> tableColumnTelefone;
	
	@FXML
	private TableColumn<Empresa, Empresa> tableColumnEDITAR;
	
	@FXML
	private TableColumn<Empresa, Empresa> tableColumnREMOVER;
	
	@FXML
	public void onBtNovoAcao(ActionEvent evento) {
		Stage parentStage = Utils.stageAtual(evento);
		Empresa obj = new Empresa();
		criarFormularioEmpresa(obj, "/gui/EmpresaFormulario.fxml", parentStage);
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		iniciarNodes();
	}

	public void setEmpresaServico(EmpresaServico servico) {
		this.servico = servico;
	}
	
	public void iniciarNodes() {
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("idEmpresa"));
		tableColumnNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
		tableColumnCnpj.setCellValueFactory(new PropertyValueFactory<>("cnpj"));
		tableColumnTelefone.setCellValueFactory(new PropertyValueFactory<>("telefone"));
		
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewEmpresa.prefHeightProperty().bind(stage.heightProperty());
	}
	
	public void updateVisualizacaoTabela() {
		if (servico == null) {
			throw new IllegalStateException("Servico esta nulo");
		}
		List<Empresa> lista = servico.acharTodos();
		obsList = FXCollections.observableArrayList(lista);
		tableViewEmpresa.setItems(obsList);
		iniciarBotoesEditar();
		iniciarBotoesExcluir();
	}
	
	public void criarFormularioEmpresa(Empresa obj, String caminho, Stage parentStage) {
		
		try {
			
			FXMLLoader loader = new FXMLLoader(getClass().getResource(caminho));
			Pane pane = loader.load();
			
			EmpresaFormularioController controller = loader.getController();
			controller.setEmpresa(obj);
			controller.setEmpresaServico(new EmpresaServico());
			controller.addOuvintes(this);
			controller.updateDadosFormulario();
			
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Criar empresa");
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
			dialogStage.initOwner(parentStage);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();
			
		} catch (IOException e) {
			Alerts.mostrarAlerta("IO Exception", "Erro ao carregar a view", e.getMessage(), AlertType.ERROR);
		}
		
	}
	
	@Override
	public void onMudancaDados() {
		updateVisualizacaoTabela();
	}
	
	private void iniciarBotoesEditar() {
		tableColumnEDITAR.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEDITAR.setCellFactory(param -> new TableCell<Empresa, Empresa>() {
			private final Button button = new Button("Editar");

			@Override
			protected void updateItem(Empresa obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(
						event -> criarFormularioEmpresa(obj, "/gui/EmpresaFormulario.fxml", Utils.stageAtual(event)));
			}
		});
	}

	private void iniciarBotoesExcluir() {
		tableColumnREMOVER.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnREMOVER.setCellFactory(param -> new TableCell<Empresa, Empresa>() {
			private final Button button = new Button("Remover");

			@Override
			protected void updateItem(Empresa obj, boolean empty) {
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

	private void removerEntidade(Empresa obj) {
		Optional<ButtonType> result = Alerts.mostrarTelaConfirmacao("Confirmacao", "Tem certeza que irá deletar?");

		if (result.get() == ButtonType.OK) {

			if (servico == null) {
				throw new IllegalStateException("Service esta nulo");
			}
			try {
				servico.delete(obj);
				updateVisualizacaoTabela();
			} catch (BDException e) {
				Alerts.mostrarAlerta("Erro removendo objeto", null, e.getMessage(), AlertType.ERROR);
			}

		}
	}
}
