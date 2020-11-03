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
import modelo.entidades.Venda;
import modelo.servicos.ClienteServico;
import modelo.servicos.FuncionarioServico;
import modelo.servicos.VendaServico;

public class VendaListaController implements Initializable, DadosMudancaOuvintes {

	private VendaServico servico;
	
	private ObservableList<Venda> obsList;
	
	@FXML
	private TableView<Venda> tableViewVenda;
	
	@FXML
	private TableColumn<Venda, Integer> tableColumnId;
	
	@FXML
	private TableColumn<Venda, String> tableColumnPrecoTotal;
	
	@FXML
	private TableColumn<Venda, String> tableColumnDataHoraVenda;
	
	@FXML
	private TableColumn<Venda, Venda> tableColumnEDITAR;

	@FXML
	private TableColumn<Venda, Venda> tableColumnREMOVER;
	
	@FXML
	private Button btNovo;
	
	@FXML
	public void onBtNovoAcao(ActionEvent evento) {
		Stage parentStage = Utils.stageAtual(evento);
		Venda obj = new Venda();
		criarFormularioVenda(obj, "/gui/VendaFormulario.fxml", parentStage);
	}
	
	public void setVendaServico(VendaServico servico) {
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
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("idVenda"));
		tableColumnPrecoTotal.setCellValueFactory(new PropertyValueFactory<>("precoTotal"));
		tableColumnDataHoraVenda.setCellValueFactory(new PropertyValueFactory<>("dataHoraVenda"));

		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewVenda.prefHeightProperty().bind(stage.heightProperty());
	}
	
	public void updateVisualizacaoTabela() {
		if (servico == null) {
			throw new IllegalStateException("Servico esta nulo");
		}
		List<Venda> lista = servico.acharTodos();
		obsList = FXCollections.observableArrayList(lista);
		tableViewVenda.setItems(obsList);
		iniciarBotoesEditar();
		iniciarBotoesExcluir();
	}
	
	private void criarFormularioVenda(Venda obj, String caminho, Stage parentStage) {
		try {
			
			FXMLLoader loader = new FXMLLoader(getClass().getResource(caminho));
			Pane pane = loader.load();
			
			VendaFormularioController controller = loader.getController();
			controller.setVenda(obj);
			controller.setVendaServicos(new VendaServico(), new ClienteServico(), new FuncionarioServico());
			controller.carregarObjetosAssociados();
			controller.addOuvintes(this);
			controller.updateDadosFormulario();
			
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Criar Venda");
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
		tableColumnEDITAR.setCellFactory(param -> new TableCell<Venda, Venda>() {
			private final Button button = new Button("Editar");

			@Override
			protected void updateItem(Venda obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(
						event -> criarFormularioVenda(obj, "/gui/VendaFormulario.fxml", Utils.stageAtual(event)));
			}
		});
	}

	private void iniciarBotoesExcluir() {
		tableColumnREMOVER.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnREMOVER.setCellFactory(param -> new TableCell<Venda, Venda>() {
			private final Button button = new Button("Remover");

			@Override
			protected void updateItem(Venda obj, boolean empty) {
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

	private void removerEntidade(Venda obj) {
		Optional<ButtonType> result = Alerts.mostrarTelaConfirmacao("Confirmacao", "Tem certeza que irá deletar?");

		if (result.get() == ButtonType.OK) {

			if (servico == null) {
				throw new IllegalStateException("Servico esta nulo");
			}
			try {
				servico.delete(obj.getIdVenda());
				updateVisualizacaoTabela();
			} catch (BDException e) {
				Alerts.mostrarAlerta("Erro removendo objeto", null, e.getMessage(), AlertType.ERROR);
			}

		}
	}
	
}
