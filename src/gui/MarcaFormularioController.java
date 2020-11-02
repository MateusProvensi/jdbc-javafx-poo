package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
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
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.util.Callback;
import modelo.entidades.Empresa;
import modelo.entidades.Marca;
import modelo.exceptions.ValidacaoException;
import modelo.servicos.EmpresaServico;
import modelo.servicos.MarcaServico;

public class MarcaFormularioController implements Initializable{

	private Marca entidade;
	
	private MarcaServico servico;
	
	private EmpresaServico empresaServico;
	
	private List<DadosMudancaOuvintes> dadosMudancasOuvintes = new ArrayList<>();
	
	private ObservableList<Empresa> obsList;
	
	@FXML
	private TextField txtIdMarca;
	
	@FXML
	private TextField txtNome;
	
	@FXML
	private TextField txtCnpj;
	
	@FXML
	private ComboBox<Empresa> comboBoxEmpresa;
	
	@FXML
	private Label txtNomeErro;
	
	@FXML
	private Label txtCnpjErro;
	
	@FXML
	private Button btSalvar;
	
	@FXML
	private Button btCancelar;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		inicializarNodes();
	}
	
	public void setMarca(Marca entidade) {
		this.entidade = entidade;
	}
	
	public void setMarcaServicos(MarcaServico servico, EmpresaServico empresaServico) {
		this.servico = servico;
		this.empresaServico = empresaServico;
	}
	
	public void addOuvintes(DadosMudancaOuvintes ouvinte) {
		dadosMudancasOuvintes.add(ouvinte);
	}
	
	@FXML
	public void onBtSalvarAcao(ActionEvent evento) {
		if (servico == null) {
			throw new IllegalStateException("Servico esta vazio");
		}
		if (entidade == null) {
			throw new IllegalStateException("Entidade esta vazio");
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
	public void onBtCancelarAcao(ActionEvent evento) {
		Utils.stageAtual(evento).close();
	}
	
	public void notificarOuvintes() {
		for (DadosMudancaOuvintes ouvinte : dadosMudancasOuvintes) {
			ouvinte.onMudancaDados();
		}
	}
	
	private Marca getDadosFormulario() {
		Marca obj = new Marca();
		
		ValidacaoException excecao = new ValidacaoException("Erro de validacao");
		
		if (txtNome.getText() == null || txtNome.getText().trim().equals("")) {
			excecao.addErros("nome", "O campo não pode ser vazio");
		}		
		if (txtCnpj.getText() == null || txtCnpj.getText().trim().equals("")) {
			excecao.addErros("cnpj", "O campo não pode ser vazio");
		} else if (txtCnpj.getText().trim().length() != 14) {
			excecao.addErros("cnpj", "O campo deve ter 14 caracteres");
		}
		
		obj.setIdMarca(Utils.transformarInteger(txtIdMarca.getText()));
		obj.setNome(txtNome.getText());
		obj.setCnpj(txtCnpj.getText());
		obj.setEmpresa(comboBoxEmpresa.getValue());
		
		if (excecao.getErros().size() > 0) {
			throw excecao;
		}
		
		return obj;
	}
	
	public void inicializarNodes() {
		Constraints.setTextFieldInteger(txtIdMarca);
		Constraints.setTextFieldMaxLength(txtNome, 60);
		Constraints.setTextFieldInteger(txtCnpj);
		inicializarComboBoxEmpresa();
	}
	
	public void updateDadosFormulario() {
		if (entidade == null) {
			throw new IllegalStateException("Entidade esta nula");
		}
		txtIdMarca.setText(String.valueOf(entidade.getIdMarca()));
		txtNome.setText(entidade.getNome());
		txtCnpj.setText(entidade.getCnpj());
		if (entidade.getEmpresa() == null) {
			comboBoxEmpresa.getSelectionModel().selectFirst();
		} else {
			comboBoxEmpresa.setValue(entidade.getEmpresa());
		}
	}
	
	public void carregarObjetosAssociados() {
		if (empresaServico == null) {
			throw new IllegalStateException("EmpresaServico esta nulo");
		}
		List<Empresa> listaEmpresas = empresaServico.acharTodos();
		obsList = FXCollections.observableArrayList(listaEmpresas);
		comboBoxEmpresa.setItems(obsList);
	}
	
	public void setMensagensErros(Map<String, String> erros) {
		Set<String> campos = erros.keySet();
		
		txtNomeErro.setText(campos.contains("nome") ? erros.get("nome") : "");
		txtCnpjErro.setText(campos.contains("cnpj") ? erros.get("cnpj") : "");
	}
	
	private void inicializarComboBoxEmpresa() {
		Callback<ListView<Empresa>, ListCell<Empresa>> factory = lv -> new ListCell<Empresa>() {
			@Override
			protected void updateItem(Empresa item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getNome());
			}
		};
		comboBoxEmpresa.setCellFactory(factory);
		comboBoxEmpresa.setButtonCell(factory.call(null));
	}
}
