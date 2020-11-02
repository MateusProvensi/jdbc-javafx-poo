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
import modelo.entidades.Funcionario;
import modelo.servicos.FuncionarioServico;

public class FuncionarioListaController implements Initializable, DadosMudancaOuvintes {

	private FuncionarioServico servico;
	
	private ObservableList<Funcionario> obsList;
	
	@FXML
	private TableView<Funcionario> tableViewFuncionario;
	
	@FXML
	private TableColumn<Funcionario, Integer> tableColumnId;
	
	@FXML
	private TableColumn<Funcionario, String> tableColumnNome;
	
	@FXML
	private TableColumn<Funcionario, String> tableColumnSobrenome;
	
	@FXML
	private TableColumn<Funcionario, String> tableColumnCpf;
	
	@FXML
	private TableColumn<Funcionario, String> tableColumnRg;
	
	@FXML
	private TableColumn<Funcionario, String> tableColumnTelefone;
	
	@FXML
	private TableColumn<Funcionario, Integer> tableColumnNumeroCaixa;
	
	@FXML
	private TableColumn<Funcionario, Funcionario> tableColumnEDITAR;

	@FXML
	private TableColumn<Funcionario, Funcionario> tableColumnREMOVER;
	
	@FXML
	private Button btNovo;
	
	@FXML
	public void onBtNovoAcao(ActionEvent evento) {
		Stage parentStage = Utils.stageAtual(evento);
		Funcionario obj = new Funcionario();
		criarFormularioFuncionario(obj, "/gui/FuncionarioFormulario.fxml", parentStage);
	}
	
	public void setFuncionarioServico(FuncionarioServico servico) {
		this.servico = servico;
	}
	
	@Override
	public void onMudancaDados() {
		updateVisualizacaoTabela();
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		iniciarNodes();
	}
	
	private void iniciarNodes() {
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("idFuncionario"));
		tableColumnNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
		tableColumnSobrenome.setCellValueFactory(new PropertyValueFactory<>("sobrenome"));
		tableColumnCpf.setCellValueFactory(new PropertyValueFactory<>("cpf"));
		tableColumnRg.setCellValueFactory(new PropertyValueFactory<>("rg"));
		tableColumnTelefone.setCellValueFactory(new PropertyValueFactory<>("telefone"));
		tableColumnNumeroCaixa.setCellValueFactory(new PropertyValueFactory<>("numeroCaixa"));
		
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewFuncionario.prefHeightProperty().bind(stage.heightProperty());
	}
	
	public void updateVisualizacaoTabela() {
		if (servico == null) {
			throw new IllegalStateException("Servico esta nulo");
		}
		List<Funcionario> lista = servico.acharTodos();
		obsList = FXCollections.observableArrayList(lista);
		tableViewFuncionario.setItems(obsList);
		iniciarBotoesEditar();
		iniciarBotoesExcluir();
	}
	
	private void criarFormularioFuncionario(Funcionario obj, String caminho, Stage parentStage) {
		try {
			
			FXMLLoader loader = new FXMLLoader(getClass().getResource(caminho));
			Pane pane = loader.load();
			
			FuncionarioFormularioController controller = loader.getController();
			controller.setFuncionario(obj);
			controller.setFuncionarioServico(new FuncionarioServico());
			controller.addOuvintes(this);
			controller.updateDadosFormulario();
			
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Criar Funcionario");
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
			dialogStage.initOwner(parentStage);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();
			
		} catch (IOException e) {
			Alerts.mostrarAlerta("IOException", "Erro ao carregar a view", e.getMessage(), AlertType.ERROR);
		}
	}

	private void iniciarBotoesEditar() {
		tableColumnEDITAR.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEDITAR.setCellFactory(param -> new TableCell<Funcionario, Funcionario>() {
			private final Button button = new Button("Editar");

			@Override
			protected void updateItem(Funcionario obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(
						event -> criarFormularioFuncionario(obj, "/gui/FuncionarioFormulario.fxml", Utils.stageAtual(event)));
			}
		});
	}

	private void iniciarBotoesExcluir() {
		tableColumnREMOVER.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnREMOVER.setCellFactory(param -> new TableCell<Funcionario, Funcionario>() {
			private final Button button = new Button("Remover");

			@Override
			protected void updateItem(Funcionario obj, boolean empty) {
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

	private void removerEntidade(Funcionario obj) {
		Optional<ButtonType> result = Alerts.mostrarTelaConfirmacao("Confirmacao", "Tem certeza que irá deletar?");

		if (result.get() == ButtonType.OK) {

			if (servico == null) {
				throw new IllegalStateException("Servico esta nulo");
			}
			try {
				servico.delete(obj.getIdFuncionario());
				updateVisualizacaoTabela();
			} catch (BDException e) {
				Alerts.mostrarAlerta("Erro removendo objeto", null, e.getMessage(), AlertType.ERROR);
			}

		}
	}
	
}
