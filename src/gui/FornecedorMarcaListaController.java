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
import modelo.entidades.FornecedorMarca;
import modelo.servicos.FornecedorMarcaServico;
import modelo.servicos.FornecedorServico;
import modelo.servicos.MarcaServico;

public class FornecedorMarcaListaController implements Initializable, DadosMudancaOuvintes{

	private FornecedorMarcaServico servico;
	
	private ObservableList<FornecedorMarca> obsLista;
	
	@FXML
	private TableView<FornecedorMarca> tableViewFornecedorMarca;
	
	@FXML
	private TableColumn<FornecedorMarca, Integer> tableColumnIdFornecedorMarca;
	
	@FXML
	private TableColumn<FornecedorMarca, FornecedorMarca> tableColumnEDITAR;
	
	@FXML
	private TableColumn<FornecedorMarca, FornecedorMarca> tableColumnREMOVER;
	
	@FXML
	private Button btNovo;
	
	@FXML
	public void onBtNovoAcao(ActionEvent evento) {
		Stage parentStage = Utils.stageAtual(evento);
		FornecedorMarca obj = new FornecedorMarca();
		criarFormularioFornecedorMarca(obj, "/gui/FornecedorMarcaFormulario.fxml", parentStage);
	}
	
	public void setFornecedorMarcaServico(FornecedorMarcaServico servico) {
		this.servico = servico;
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		inicializarNodes();
	}
	
	private void inicializarNodes() {
		tableColumnIdFornecedorMarca.setCellValueFactory(new PropertyValueFactory<>("idFornecedorMarca"));
		
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewFornecedorMarca.prefHeightProperty().bind(stage.heightProperty());
	}
	
	public void updateVisualizacaoTabela() {
		if (servico == null) {
			throw new IllegalStateException("Servico esta nulo");
		}
		List<FornecedorMarca> lista = servico.acharTodos();
		obsLista = FXCollections.observableArrayList(lista);
		tableViewFornecedorMarca.setItems(obsLista);
		iniciarBotoesEditar();
		iniciarBotoesExcluir();
	}
	
	private void criarFormularioFornecedorMarca(FornecedorMarca obj, String caminho, Stage parentStage) {
		try {
			
			FXMLLoader loader = new FXMLLoader(getClass().getResource(caminho));
			Pane pane = loader.load();
			
			FornecedorMarcaFormularioController controller = loader.getController();
			controller.setFornecedorMarca(obj);
			controller.setFornecedorMarcaServicos(servico, new FornecedorServico(), new MarcaServico());
			controller.carregarObjetosAssociados();
			controller.addOuvintes(this);
			controller.updateDadosFormulario();
			
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Criar novo Fornecedor Marca");
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
		tableColumnEDITAR.setCellFactory(param -> new TableCell<FornecedorMarca, FornecedorMarca>() {
			private final Button button = new Button("Editar");

			@Override
			protected void updateItem(FornecedorMarca obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(
						event -> criarFormularioFornecedorMarca(obj, "/gui/FornecedorMarcaFormulario.fxml", Utils.stageAtual(event)));
			}
		});
	}

	private void iniciarBotoesExcluir() {
		tableColumnREMOVER.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnREMOVER.setCellFactory(param -> new TableCell<FornecedorMarca, FornecedorMarca>() {
			private final Button button = new Button("Remover");

			@Override
			protected void updateItem(FornecedorMarca obj, boolean empty) {
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

	private void removerEntidade(FornecedorMarca obj) {
		Optional<ButtonType> result = Alerts.mostrarTelaConfirmacao("Confirmacao", "Tem certeza que irá deletar?");

		if (result.get() == ButtonType.OK) {

			if (servico == null) {
				throw new IllegalStateException("Servico esta nulo");
			}
			try {
				servico.delete(obj.getIdFornecedorMarca());
				updateVisualizacaoTabela();
			} catch (BDException e) {
				Alerts.mostrarAlerta("Erro removendo objeto", null, e.getMessage(), AlertType.ERROR);
			}

		}
	}
	
}
