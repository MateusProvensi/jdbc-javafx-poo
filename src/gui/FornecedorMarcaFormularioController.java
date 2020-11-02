package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

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
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import modelo.entidades.Fornecedor;
import modelo.entidades.FornecedorMarca;
import modelo.entidades.Marca;
import modelo.servicos.FornecedorMarcaServico;
import modelo.servicos.FornecedorServico;
import modelo.servicos.MarcaServico;

public class FornecedorMarcaFormularioController implements Initializable{

	private FornecedorMarca entidade;
	
	private FornecedorMarcaServico servico;
	
	private FornecedorServico fornecedorServico;
	
	private MarcaServico marcaServico;
	
	private List<DadosMudancaOuvintes> dadosMudancasOuvintes = new ArrayList<>();
	
	private ObservableList<Fornecedor> obsListaFornecedor;
	
	private ObservableList<Marca> obsListaMarca;
	
	@FXML
	private TextField txtIdFornecedorMarca;
	
	@FXML
	private ComboBox<Fornecedor> comboBoxFornecedor;
	
	@FXML
	private ComboBox<Marca> comboBoxMarca;
	
	@FXML
	private Button btSalvar;
	
	@FXML
	private Button btCancelar;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		inicializarNodes();
	}
	
	public void setFornecedorMarca(FornecedorMarca entidade) {
		this.entidade = entidade;
	}
	
	public void setFornecedorMarcaServicos(FornecedorMarcaServico servico, 
			FornecedorServico fornecedorServico, MarcaServico marcaServico) {
		this.servico = servico;
		this.fornecedorServico = fornecedorServico;
		this.marcaServico = marcaServico;
	}
	
	public void addOuvintes(DadosMudancaOuvintes ouvinte) {
		dadosMudancasOuvintes.add(ouvinte);
	}
	
	@FXML
	public void onBtSalvarAcao(ActionEvent evento) {
		if (servico == null) {
			throw new IllegalStateException("Servico esta nulo");
		}
		if (entidade == null) {
			throw new IllegalStateException("Entidade esta nula");
		}
		
		try {
			entidade = getDadosFormulario();
			servico.insertOuUpdate(entidade);
			notificarOuvintes();
			Utils.stageAtual(evento).close();
		} catch (BDException e) {
			Alerts.mostrarAlerta("Erro ao salvar o objeto", null, e.getMessage(), AlertType.ERROR);
		}
	}
	
	@FXML
	public void onBtCancelarAcao(ActionEvent evento) {
		Utils.stageAtual(evento).close();
	}
	
	private void notificarOuvintes() {
		for (DadosMudancaOuvintes ouvinte : dadosMudancasOuvintes) {
			ouvinte.onMudancaDados();
		}
	}
	
	private FornecedorMarca getDadosFormulario() {
		FornecedorMarca obj = new FornecedorMarca();
		
		obj.setIdFornecedorMarca(Utils.transformarInteger(txtIdFornecedorMarca.getText()));
		obj.setFornecedor(comboBoxFornecedor.getValue());
		obj.setMarca(comboBoxMarca.getValue());
		
		return obj;
	}
	
	public void inicializarNodes() {
		Constraints.setTextFieldInteger(txtIdFornecedorMarca);
		inicializarComboBoxFornecedor();
		inicializarComboBoxMarca();
	}
	
	public void updateDadosFormulario() {
		if (entidade == null) {
			throw new IllegalStateException("Entidade esta nula");
		}
		txtIdFornecedorMarca.setText(String.valueOf(entidade.getIdFornecedorMarca()));
		if (entidade.getFornecedor() == null) {
			comboBoxFornecedor.getSelectionModel().selectFirst();
		} else {
			comboBoxFornecedor.setValue(entidade.getFornecedor());
		}
		if (entidade.getMarca() == null) {
			comboBoxMarca.getSelectionModel().selectFirst();
		} else {
			comboBoxMarca.setValue(entidade.getMarca());
		}			
	}
	
	public void carregarObjetosAssociados() {
		if (fornecedorServico == null) {
			throw new IllegalStateException("O fornecedorServico esta nulo");
		}
		if (marcaServico == null) {
			throw new IllegalStateException("O marcaServico esta nulo");
		}
		
		List<Fornecedor> listaFornecedor = fornecedorServico.acharTodos();
		obsListaFornecedor = FXCollections.observableArrayList(listaFornecedor);
		comboBoxFornecedor.setItems(obsListaFornecedor);

		List<Marca> listaMarca = marcaServico.acharTodos();
		obsListaMarca = FXCollections.observableArrayList(listaMarca);
		comboBoxMarca.setItems(obsListaMarca);	
	}
	
	private void inicializarComboBoxFornecedor() {
		Callback<ListView<Fornecedor>, ListCell<Fornecedor>> factory = lv -> new ListCell<Fornecedor>() {
			@Override
			protected void updateItem(Fornecedor item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getNome());
			}
		};
		comboBoxFornecedor.setCellFactory(factory);
		comboBoxFornecedor.setButtonCell(factory.call(null));
	}
	
	private void inicializarComboBoxMarca() {
		Callback<ListView<Marca>, ListCell<Marca>> factory = lv -> new ListCell<Marca>() {
			@Override
			protected void updateItem(Marca item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getNome());
			}
		};
		comboBoxMarca.setCellFactory(factory);
		comboBoxMarca.setButtonCell(factory.call(null));
	}
	
}
