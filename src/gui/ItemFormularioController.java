package gui;

import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import bd.BDException;
import gui.ouvintes.DadosMudancaOuvintes;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import modelo.entidades.FornecedorMarca;
import modelo.entidades.Item;
import modelo.exceptions.ValidacaoException;
import modelo.servicos.FornecedorMarcaServico;
import modelo.servicos.ItemServico;

public class ItemFormularioController implements Initializable{

	private Item entidade;
	
	private ItemServico servico;
	
	private FornecedorMarcaServico fornecedorMarcaServico;
	
	private List<DadosMudancaOuvintes> DadosMudancasOuvintes = new ArrayList<>();
	
	private ObservableList<FornecedorMarca> obsLista;
	
	@FXML
	private TextField txtIdItem;
	
	@FXML
	private TextField txtDescricaoItem;
	
	@FXML
	private TextField txtCodigoBarras;
	
	@FXML
	private TextField txtPrecoVenda;
	
	@FXML
	private TextField txtQuantidade;
	
	@FXML
	private DatePicker dpValidade;
	
	@FXML
	private TextField txtCorredor;
	
	@FXML
	private ComboBox<FornecedorMarca> comboBoxFornecedorMarca;
	
	@FXML
	private Label txtDescricaoItemErro;
	
	@FXML
	private Label txtCodigoBarrasErro;
	
	@FXML
	private Label txtPrecoVendaErro;
	
	@FXML
	private Label txtQuantidadeErro;
	
	@FXML
	private Label txtValidadeErro;
	
	@FXML
	private Label txtCorredorErro;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		inicializarNodes();
	}
	
	public void setItem(Item entidade) {
		this.entidade = entidade;
	}
	
	public void setItemServicos(ItemServico servico, FornecedorMarcaServico fornecedorMarcaServico) {
		this.servico = servico;
		this.fornecedorMarcaServico = fornecedorMarcaServico;
	}
	
	public void addOuvintes(DadosMudancaOuvintes ouvinte) {
		DadosMudancasOuvintes.add(ouvinte);
	}
	
	@FXML
	public void onBtSalvarAcao(ActionEvent evento) {
		if (servico == null) {
			throw new IllegalStateException("Servico esta vazio");
		}
		if (entidade == null) {
			throw new IllegalStateException("Entidade esta vazia");
		}
		try {
			entidade = getDadosFormulario();
			servico.insertOuUpdate(entidade);
			notificarOuvintes();
			Utils.stageAtual(evento).close();
		} catch (ValidacaoException e) {
			setMensagensErros(e.getErros());
		} catch (BDException e) {
			Alerts.mostrarAlerta("Erro ao salvar o objeto", null, e.getMessage(), AlertType.ERROR);
		}
		
	}

	@FXML
	public void onBtCancelarAcao(ActionEvent evento){
		Utils.stageAtual(evento).close();
	}
	
	private void notificarOuvintes() {
		for (DadosMudancaOuvintes ouvinte : DadosMudancasOuvintes) {
			ouvinte.onMudancaDados();
		}
	}
	
	private Item getDadosFormulario() {
		Item obj = new Item();
		
		ValidacaoException excecao = new ValidacaoException("Erro de validacao");
		
		if (txtDescricaoItem.getText() == null || txtDescricaoItem.getText().trim().equals("")) {
			excecao.addErros("descricaoItem", "O campo não pode ser vazio");
		}		
		if (txtCodigoBarras.getText() == null || txtCodigoBarras.getText().trim().equals("")) {
			excecao.addErros("codigoBarras", "O campo não pode ser vazio");
		}		
		if (txtPrecoVenda.getText() == null || txtPrecoVenda.getText().trim().equals("")) {
			excecao.addErros("precoVenda", "O campo não pode ser vazio");
		} 	
		if (txtQuantidade.getText() == null || txtQuantidade.getText().trim().equals("")) {
			excecao.addErros("quantidade", "O campo não pode ser vazio");
		}		
		if (txtCorredor.getText() == null || txtCorredor.getText().trim().equals("")) {
			excecao.addErros("corredor", "O campo não pode ser vazio");
		}
		if (dpValidade.getValue() == null) {
			excecao.addErros("validade", "O campo não pode ser vazio");
		} else {
			Instant instante = Instant.from(dpValidade.getValue().atStartOfDay(ZoneId.systemDefault()));
			obj.setValidade(Date.from(instante));
		}
		
		obj.setIdItem(Utils.transformarInteger(txtIdItem.getText()));
		obj.setDescricaoItem(txtDescricaoItem.getText());
		obj.setCodigoBarras(txtCodigoBarras.getText());
		obj.setPrecoVenda(Utils.transformarDouble(txtPrecoVenda.getText()));
		obj.setQuantidade(Utils.transformarInteger(txtQuantidade.getText()));
		obj.setCorredor(txtCorredor.getText());
		
		if (excecao.getErros().size() > 0) {
			throw excecao;
		}
		
		return obj; 
	}
	
	public void inicializarNodes() {
		Constraints.setTextFieldInteger(txtIdItem);
		Constraints.setTextFieldMaxLength(txtDescricaoItem, 60); 
		Constraints.setTextFieldMaxLength(txtCodigoBarras, 13);
		Constraints.setTextFieldDouble(txtPrecoVenda);
		Constraints.setTextFieldInteger(txtQuantidade);
		Constraints.setTextFieldMaxLength(txtCorredor, 2);
		Utils.formatarDatePicker(dpValidade, "dd/MM/yyyy");
		inicializarComboBoxFornecedorMarca();
	}
	
	public void updateDadosFormulario() {
		if (entidade == null) {
			throw new IllegalStateException("Entidade esta nula");
		}
		txtIdItem.setText(String.valueOf(entidade.getIdItem()));
		txtDescricaoItem.setText(entidade.getDescricaoItem());
		txtCodigoBarras.setText(entidade.getCodigoBarras());
		Locale.setDefault(Locale.US);
		txtPrecoVenda.setText(String.format("%.2f", entidade.getPrecoVenda()));
		txtQuantidade.setText(String.valueOf(entidade.getQuantidade()));
		txtCorredor.setText(entidade.getCorredor());
		if (entidade.getValidade() != null) {
			dpValidade.setValue(LocalDate.ofInstant(entidade.getValidade().toInstant(), ZoneId.systemDefault()));
		}
		if (entidade.getFornecedorMarca() == null) {
			comboBoxFornecedorMarca.getSelectionModel().selectFirst();			
		} else {
			comboBoxFornecedorMarca.setValue(entidade.getFornecedorMarca());
		}
	}
	
	public void carregarObjetosAssociados() {
		if (fornecedorMarcaServico == null) {
			throw new IllegalStateException("fornecedorMarcaServico esta nulo");			
		}
		List<FornecedorMarca> listafornecedorMarca = fornecedorMarcaServico.acharTodos();
		obsLista = FXCollections.observableArrayList(listafornecedorMarca);
		comboBoxFornecedorMarca.setItems(obsLista);
	}
	
	private void setMensagensErros(Map<String, String> erros) {
		Set<String> campos = erros.keySet();
		
		txtDescricaoItemErro.setText(campos.contains("descricaoItem") ? erros.get("descricaoItem") : "");
		txtCodigoBarrasErro.setText(campos.contains("codigoBarras") ? erros.get("codigoBarras") : "");
		txtPrecoVendaErro.setText(campos.contains("precoVenda") ? erros.get("precoVenda") : "");
		txtQuantidadeErro.setText(campos.contains("quantidade") ? erros.get("quantidade") : "");
		txtCorredorErro.setText(campos.contains("corredor") ? erros.get("corredor") : "");
		txtValidadeErro.setText(campos.contains("validade") ? erros.get("validade") : "");
	}
	
	private void inicializarComboBoxFornecedorMarca() {
		Callback<ListView<FornecedorMarca>, ListCell<FornecedorMarca>> factory = lv -> new ListCell<FornecedorMarca>() {
			@Override
			protected void updateItem(FornecedorMarca item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : "" + item.getFornecedor().getNome() + " / " + item.getMarca().getNome() + "");
			}
		};
		comboBoxFornecedorMarca.setCellFactory(factory);
		comboBoxFornecedorMarca.setButtonCell(factory.call(null));
	}
	

}
