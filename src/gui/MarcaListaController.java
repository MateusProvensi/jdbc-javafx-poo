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
import modelo.entidades.Marca;
import modelo.servicos.EmpresaServico;
import modelo.servicos.MarcaServico;

public class MarcaListaController implements Initializable, DadosMudancaOuvintes{

	private MarcaServico servico;
	
	private ObservableList<Marca> obsList;
	
	@FXML
	private TableView<Marca> tableViewMarca;
	
	@FXML
	private TableColumn<Marca, Integer> tableColumnIdMarca;
	
	@FXML
	private TableColumn<Marca, String> tableColumnNome;
	
	@FXML
	private TableColumn<Marca, String> tableColumnCnpj;
	
	@FXML
	private TableColumn<Marca, Marca> tableColumnEDITAR;
	
	@FXML
	private TableColumn<Marca, Marca> tableColumnREMOVER;
	
	@FXML
	private Button btNovo;
	
	@FXML
	public void onBtNovoAcao(ActionEvent evento) {
		Stage parentStage = Utils.stageAtual(evento);
		Marca obj = new Marca();
		criarFormularioMarca(obj, "/gui/MarcaFormulario.fxml", parentStage);
	}
	
	public void setMarcaServico(MarcaServico servico) {
		this.servico = servico;
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		inicializarNodes();		
	}
	
	private void inicializarNodes() {
		tableColumnIdMarca.setCellValueFactory(new PropertyValueFactory<>("idMarca"));
		tableColumnNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
		tableColumnCnpj.setCellValueFactory(new PropertyValueFactory<>("cnpj"));
		
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewMarca.prefHeightProperty().bind(stage.heightProperty());
	}
	
	public void updateVisualizacaoTabela() {
		if (servico == null) {
			throw new IllegalStateException("Servico esta nulo");
		}
		List<Marca> lista = servico.acharTodos();
		obsList = FXCollections.observableArrayList(lista);
		tableViewMarca.setItems(obsList);
		iniciarBotoesEditar();
		iniciarBotoesExcluir();
	}
	
	private void criarFormularioMarca(Marca obj, String caminho, Stage parentStage) {
		try {
			
			FXMLLoader loader = new FXMLLoader(getClass().getResource(caminho));
			Pane pane = loader.load();
			
			MarcaFormularioController controller = loader.getController();
			controller.setMarca(obj);
			controller.setMarcaServicos(new MarcaServico(), new EmpresaServico());
			controller.carregarObjetosAssociados();
			controller.addOuvintes(this);
			controller.updateDadosFormulario();
			
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Criar nova Marca");
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
		tableColumnEDITAR.setCellFactory(param -> new TableCell<Marca, Marca>() {
			private final Button button = new Button("Editar");

			@Override
			protected void updateItem(Marca obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(
						event -> criarFormularioMarca(obj, "/gui/MarcaFormulario.fxml", Utils.stageAtual(event)));
			}
		});
	}

	private void iniciarBotoesExcluir() {
		tableColumnREMOVER.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnREMOVER.setCellFactory(param -> new TableCell<Marca, Marca>() {
			private final Button button = new Button("Remover");

			@Override
			protected void updateItem(Marca obj, boolean empty) {
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

	private void removerEntidade(Marca obj) {
		Optional<ButtonType> result = Alerts.mostrarTelaConfirmacao("Confirmacao", "Tem certeza que irá deletar?");

		if (result.get() == ButtonType.OK) {

			if (servico == null) {
				throw new IllegalStateException("Servico esta nulo");
			}
			try {
				servico.delete(obj.getIdMarca());
				updateVisualizacaoTabela();
			} catch (BDException e) {
				Alerts.mostrarAlerta("Erro removendo objeto", null, e.getMessage(), AlertType.ERROR);
			}

		}
	}
	
}
