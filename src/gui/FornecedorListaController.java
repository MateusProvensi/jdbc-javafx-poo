package gui;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
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
import modelo.entidades.Fornecedor;
import modelo.servicos.EmpresaServico;
import modelo.servicos.FornecedorServico;

public class FornecedorListaController implements Initializable, DadosMudancaOuvintes{

	private FornecedorServico servico;
	
	private ObservableList<Fornecedor> obsList;
	
	@FXML
	private TableView<Fornecedor> tableViewFornecedor;
	
	@FXML
	private TableColumn<Fornecedor, Integer> tableColumnIdFornecedor;
	
	@FXML
	private TableColumn<Fornecedor, String> tableColumnNome;
	
	@FXML
	private TableColumn<Fornecedor, String> tableColumnSobrenome;
	
	@FXML
	private TableColumn<Fornecedor, String> tableColumnCpf;
	
	@FXML
	private TableColumn<Fornecedor, String> tableColumnRg;
	
	@FXML
	private TableColumn<Fornecedor, String> tableColumnTelefone;
	
	@FXML
	private TableColumn<Fornecedor, Date> tableColumnDataUltimaVisita;
	
	@FXML
	private TableColumn<Fornecedor, Fornecedor> tableColumnEDITAR;
	
	@FXML
	private TableColumn<Fornecedor, Fornecedor> tableColumnREMOVER;
	
	@FXML
	private Button btNovo;
	
	@FXML
	public void onBtNovoAcao(ActionEvent evento) {
		Stage parentStage = Utils.stageAtual(evento);
		Fornecedor obj = new Fornecedor();
		criarFormularioFornecedor(obj, "/gui/FornecedorFormulario.fxml", parentStage);
	}
	
	public void setFornecedorServico(FornecedorServico servico) {
		this.servico = servico;
	}
		
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		inicializarNodes();
		
	}
	
	private void inicializarNodes() {
		tableColumnIdFornecedor.setCellValueFactory(new PropertyValueFactory<>("idFornecedor"));
		tableColumnNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
		tableColumnSobrenome.setCellValueFactory(new PropertyValueFactory<>("sobrenome"));
		tableColumnCpf.setCellValueFactory(new PropertyValueFactory<>("cpf"));
		tableColumnRg.setCellValueFactory(new PropertyValueFactory<>("rg"));
		tableColumnTelefone.setCellValueFactory(new PropertyValueFactory<>("telefone"));
		tableColumnDataUltimaVisita.setCellValueFactory(new PropertyValueFactory<>("dataUltimaVisita"));
		Utils.formatarTableColumnDate(tableColumnDataUltimaVisita, "dd/MM/yyyy");
		
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewFornecedor.prefHeightProperty().bind(stage.heightProperty());
	}
	
	public void updateVisualizacaoTabela() {
		if (servico == null) {
			throw new IllegalStateException("Servico esta nulo");
		}
		List<Fornecedor> lista = servico.acharTodos();
		obsList = FXCollections.observableArrayList(lista);
		tableViewFornecedor.setItems(obsList);
		iniciarBotoesEditar();
		iniciarBotoesExcluir();
	}
	
	private void criarFormularioFornecedor(Fornecedor obj, String caminho, Stage parentStage) {
		try {
			
			FXMLLoader loader = new FXMLLoader(getClass().getResource(caminho));
			Pane pane = loader.load();
			
			FornecedorFormularioController controller = loader.getController();
			controller.setFornecedor(obj);
			controller.setFornecedorServicos(new FornecedorServico(), new EmpresaServico());
			controller.carregarObjetosAssociados();
			controller.addOuvintes(this);
			controller.updateDadosFormulario();
			
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Criar novo fornecedor");
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
		tableColumnEDITAR.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEDITAR.setCellFactory(param -> new TableCell<Fornecedor, Fornecedor>() {
			private final Button button = new Button("Editar");

			@Override
			protected void updateItem(Fornecedor obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(
						event -> criarFormularioFornecedor(obj, "/gui/ClienteFormulario.fxml", Utils.stageAtual(event)));
			}
		});
	}

	private void iniciarBotoesExcluir() {
		tableColumnREMOVER.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnREMOVER.setCellFactory(param -> new TableCell<Fornecedor, Fornecedor>() {
			private final Button button = new Button("Remover");

			@Override
			protected void updateItem(Fornecedor obj, boolean empty) {
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

	private void removerEntidade(Fornecedor obj) {
		Optional<ButtonType> result = Alerts.mostrarTelaConfirmacao("Confirmacao", "Tem certeza que irá deletar?");

		if (result.get() == ButtonType.OK) {

			if (servico == null) {
				throw new IllegalStateException("Servico esta nulo");
			}
			try {
				servico.delete(obj.getIdFornecedor());
				updateVisualizacaoTabela();
			} catch (BDException e) {
				Alerts.mostrarAlerta("Erro removendo objeto", null, e.getMessage(), AlertType.ERROR);
			}

		}
	}
	
}
