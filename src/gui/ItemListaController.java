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
import modelo.entidades.Item;
import modelo.servicos.FornecedorMarcaServico;
import modelo.servicos.ItemServico;

public class ItemListaController implements Initializable, DadosMudancaOuvintes{

	private ItemServico servico;
	
	private ObservableList<Item> obsLista;
	
	@FXML
	private TableView<Item> tableViewItem;
	
	@FXML
	private TableColumn<Item, Integer> tableColumnIdItem;
	
	@FXML
	private TableColumn<Item, String> tableColumnDescricao;
	
	@FXML
	private TableColumn<Item, String> tableColumnCodigoBarras;
	
	@FXML
	private TableColumn<Item, Double> tableColumnPrecoVenda;
	
	@FXML
	private TableColumn<Item, Integer> tableColumnQuantidade;
	
	@FXML
	private TableColumn<Item, Date> tableColumnValidade;
	
	@FXML
	private TableColumn<Item, String> tableColumnCorredor;
	
	@FXML
	private TableColumn<Item, Item> tableColumnEDITAR;

	@FXML
	private TableColumn<Item, Item> tableColumnREMOVER;
	
	@FXML
	private Button btNovo;
	
	@FXML
	public void onBtNovoAcao(ActionEvent evento) {
		Stage parentStage = Utils.stageAtual(evento);
		Item obj = new Item();
		criarFormularioItem(obj, "/gui/ItemFormulario.fxml", parentStage);
	}
	
	public void setItemServico(ItemServico servico) {
		this.servico = servico;
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		inicializarNodes();
	}
	
	public void inicializarNodes() {
		tableColumnIdItem.setCellValueFactory(new PropertyValueFactory<>("idItem"));
		tableColumnDescricao.setCellValueFactory(new PropertyValueFactory<>("descricaoItem"));
		tableColumnCodigoBarras.setCellValueFactory(new PropertyValueFactory<>("codigoBarras"));
		tableColumnPrecoVenda.setCellValueFactory(new PropertyValueFactory<>("precoVenda"));
		Utils.formatarTableColumnDouble(tableColumnPrecoVenda, 2);
		tableColumnQuantidade.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
		Utils.formatarTableColumnDate(tableColumnValidade, "dd/MM/yyyy");
		tableColumnValidade.setCellValueFactory(new PropertyValueFactory<>("validade"));
		tableColumnCorredor.setCellValueFactory(new PropertyValueFactory<>("corredor"));
		
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewItem.prefHeightProperty().bind(stage.heightProperty());
	}
	
	public void updateVisualizacaoTabela() {
		if (servico == null) {
			throw new IllegalStateException("Servico esta nulo");
		}
		List<Item> lista = servico.acharTodos();
		obsLista = FXCollections.observableArrayList(lista);
		tableViewItem.setItems(obsLista);
		iniciarBotoesEditar();
		iniciarBotoesExcluir();
	}
	
	private void criarFormularioItem(Item obj, String caminho, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(caminho));
			Pane pane = loader.load();
			
			ItemFormularioController controller = loader.getController();
			controller.setItem(new Item());
			controller.setItemServicos(new ItemServico(), new FornecedorMarcaServico());
			controller.carregarObjetosAssociados();
			controller.addOuvintes(this);
			controller.updateDadosFormulario();

			Stage dialogStage = new Stage();
			dialogStage.setTitle("Criar novo Item");
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
		tableColumnEDITAR.setCellFactory(param -> new TableCell<Item, Item>() {
			private final Button button = new Button("Editar");

			@Override
			protected void updateItem(Item obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(
						event -> criarFormularioItem(obj, "/gui/ItemFormulario.fxml", Utils.stageAtual(event)));
			}
		});
	}

	private void iniciarBotoesExcluir() {
		tableColumnREMOVER.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnREMOVER.setCellFactory(param -> new TableCell<Item, Item>() {
			private final Button button = new Button("Remover");

			@Override
			protected void updateItem(Item obj, boolean empty) {
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

	private void removerEntidade(Item obj) {
		Optional<ButtonType> result = Alerts.mostrarTelaConfirmacao("Confirmacao", "Tem certeza que irá deletar?");

		if (result.get() == ButtonType.OK) {

			if (servico == null) {
				throw new IllegalStateException("Servico esta nulo");
			}
			try {
				servico.delete(obj.getIdItem());
				updateVisualizacaoTabela();
			} catch (BDException e) {
				Alerts.mostrarAlerta("Erro removendo objeto", null, e.getMessage(), AlertType.ERROR);
			}

		}
	}
}
