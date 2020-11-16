
package gui;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import bd.BDException;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import modelo.entidades.Item;
import modelo.entidades.ItemVenda;
import modelo.entidades.Venda;
import modelo.servicos.ItemServico;
import modelo.servicos.ItemVendaServico;
import modelo.servicos.VendaServico;

public class RegistrarItemFormularioController implements Initializable{

	private ItemVenda entidade;
	
	private ItemVendaServico servico;

	private VendaServico vendaServico;

	private ItemServico itemServico;

	private ObservableList<Venda> obsListaVenda;

	private ObservableList<Item> obsListaItem;

	@FXML
	private TextField txtIdItemVenda;

	@FXML
	private ComboBox<Venda> comboBoxVenda;

	@FXML
	private ComboBox<Item> comboBoxItem;

	@FXML
	private Button btSalvar;

	@FXML
	private Button btCancelar;

	@FXML
	public void onBtSalvarAcao(ActionEvent evento) {
		if (entidade == null) {
			throw new IllegalStateException("Entidade esta vazia");
		}
		if (servico == null) {
			throw new IllegalStateException("Servico esta vazio");
		}

		try {
			entidade = getDadosFormulario();
			servico.insertOuUpdate(entidade);
			mudarValorVenda(entidade.getVenda().getIdVenda());
			Utils.stageAtual(evento).close();
		} catch (BDException e) {
			Alerts.mostrarAlerta("Erro ao salvar o objeto", null, e.getMessage(), AlertType.ERROR);
		}
	}

	@FXML
	public void onBtCancelarAcao(ActionEvent evento) {
		Utils.stageAtual(evento).close();
	}

	@Override
	public void initialize(URL url, ResourceBundle rs) {
		inicializarNodes();
	}

	public void setItemVenda(ItemVenda entidade) {
		this.entidade = entidade;
	}

	public void setItemVendaServicos(ItemVendaServico servico, VendaServico vendaServico,
			ItemServico itemServico) {
		this.servico = servico;
		this.vendaServico = vendaServico;
		this.itemServico = itemServico;
	}

	private ItemVenda getDadosFormulario() {
		ItemVenda obj = new ItemVenda();

		obj.setIdItemVenda(Utils.transformarInteger(txtIdItemVenda.getText()));
		obj.setItem(comboBoxItem.getValue());
		obj.setVenda(comboBoxVenda.getValue());

		return obj;
	}

	private void inicializarNodes() {
		Constraints.setTextFieldInteger(txtIdItemVenda);
		inicializarComboBoxItem();
		inicializarComboBoxVenda();
	}

	public void updateDadosFormulario() {
		if (entidade == null) {
			throw new IllegalStateException("Entidade esta vazia");
		}

		txtIdItemVenda.setText(String.valueOf(entidade.getIdItemVenda()));
		if (entidade.getItem() == null) {
			comboBoxItem.getSelectionModel().selectFirst();
		} else {
			comboBoxItem.setValue(entidade.getItem());
		}
		if (entidade.getVenda() == null) {
			comboBoxVenda.getSelectionModel().selectFirst();
		} else {
			comboBoxVenda.setValue(entidade.getVenda());
		}
	}

	public void carregarObjetosAssociados() {
		if (itemServico == null) {
			throw new IllegalStateException("ClienteServico esta nulo");
		}
		if (vendaServico == null) {
			throw new IllegalStateException("FuncionarioServico esta nulo");
		}

		List<Item> listaItem = itemServico.acharTodos();
		obsListaItem = FXCollections.observableArrayList(listaItem);
		comboBoxItem.setItems(obsListaItem);

		List<Venda> listaVenda = vendaServico.acharTodos();
		obsListaVenda = FXCollections.observableArrayList(listaVenda);
		comboBoxVenda.setItems(obsListaVenda);
	}

	private void inicializarComboBoxItem() {
		Callback<ListView<Item>, ListCell<Item>> factory = lv -> new ListCell<Item>() {
			@Override
			protected void updateItem(Item item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getDescricaoItem());
			}
		};
		comboBoxItem.setCellFactory(factory);
		comboBoxItem.setButtonCell(factory.call(null));
	}

	private void inicializarComboBoxVenda() {
		Callback<ListView<Venda>, ListCell<Venda>> factory = lv -> new ListCell<Venda>() {
			@Override
			protected void updateItem(Venda item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getIdVenda() + " / " + item.getDataHoraVenda());
			}
		};
		comboBoxVenda.setCellFactory(factory);
		comboBoxVenda.setButtonCell(factory.call(null));
	}
	
	private void mudarValorVenda(Integer idVenda) {
		Venda venda = vendaServico.acharPeloId(idVenda);
		venda.acrescentarValorVenda(comboBoxItem.getValue().getPrecoVenda());
		vendaServico.insertOuUpdate(venda);
	}
}
