package gui;

import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import modelo.entidades.Empresa;
import modelo.entidades.Fornecedor;
import modelo.exceptions.ValidacaoException;
import modelo.servicos.EmpresaServico;
import modelo.servicos.FornecedorServico;

public class FornecedorFormularioController implements Initializable{

	private Fornecedor entidade;
	
	private FornecedorServico servico;
	
	private EmpresaServico empresaServico;
	
	private List<DadosMudancaOuvintes> DadosMudancasOuvintes = new ArrayList<>();
	
	private ObservableList<Empresa> obsLista;
	
	@FXML
	private TextField txtIdFornecedor;
	
	@FXML
	private TextField txtNome;
	
	@FXML
	private TextField txtSobrenome;
	
	@FXML
	private TextField txtCpf;
	
	@FXML
	private TextField txtRg;
	
	@FXML
	private TextField txtTelefone;
	
	@FXML
	private DatePicker dpDataUltimaVisita;
	
	@FXML
	private ComboBox<Empresa> comboBoxEmpresa;
	
	@FXML
	private Label txtNomeErro;
	
	@FXML
	private Label txtSobrenomeErro;
	
	@FXML
	private Label txtCpfErro;
	
	@FXML
	private Label txtRgErro;
	
	@FXML
	private Label txtTelefoneErro;
	
	@FXML
	private Label txtDataUltimaVisitaErro;
	
	@FXML
	private Button btSalvar;
	
	@FXML
	private Button btCancelar;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		inicializarNodes();
	}
	
	public void setFornecedor(Fornecedor entidade) {
		this.entidade = entidade;
	}
	
	public void setFornecedorServicos(FornecedorServico servico, EmpresaServico empresaServico) {
		this.servico = servico;
		this.empresaServico = empresaServico;
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
	
	private Fornecedor getDadosFormulario() {
		Fornecedor obj = new Fornecedor();
		
		ValidacaoException excecao = new ValidacaoException("Erro de validacao");
		
		if (txtNome.getText() == null || txtNome.getText().trim().equals("")) {
			excecao.addErros("nome", "O campo não pode ser vazio");
		}		
		if (txtSobrenome.getText() == null || txtSobrenome.getText().trim().equals("")) {
			excecao.addErros("sobrenome", "O campo não pode ser vazio");
		}		
		if (txtCpf.getText() == null || txtCpf.getText().trim().equals("")) {
			excecao.addErros("cpf", "O campo não pode ser vazio");
		} else if (txtCpf.getText().trim().length() != 11) {
			excecao.addErros("cpf", "O campo deve ter 11 caracteres");
		}		
		if (txtRg.getText() == null || txtRg.getText().trim().equals("")) {
			excecao.addErros("rg", "O campo não pode ser vazio");
		}		
		if (txtTelefone.getText() == null || txtTelefone.getText().trim().equals("")) {
			excecao.addErros("telefone", "O campo não pode ser vazio");
		}
		if (dpDataUltimaVisita.getValue() == null) {
			excecao.addErros("dataUltimaVisita", "O campo não pode ser vazio");
		} else {
			Instant instante = Instant.from(dpDataUltimaVisita.getValue().atStartOfDay(ZoneId.systemDefault()));
			obj.setDataUltimaVisita(Date.from(instante));
		}
		
		obj.setIdFornecedor(Utils.transformarInteger(txtIdFornecedor.getText()));
		obj.setNome(txtNome.getText());
		obj.setSobrenome(txtSobrenome.getText());
		obj.setCpf(txtCpf.getText());
		obj.setRg(txtRg.getText());
		obj.setEmpresa(comboBoxEmpresa.getValue());
		
		if (excecao.getErros().size() > 0) {
			throw excecao;
		}
		
		return obj; 
	}
	
	public void inicializarNodes() {
		Constraints.setTextFieldInteger(txtIdFornecedor);
		Constraints.setTextFieldMaxLength(txtNome, 30); 
		Constraints.setTextFieldMaxLength(txtSobrenome, 50);
		Constraints.setTextFieldMaxLength(txtCpf, 11);
		Constraints.setTextFieldMaxLength(txtRg, 15);
		Constraints.setTextFieldMaxLength(txtTelefone, 16);
		Utils.formatarDatePicker(dpDataUltimaVisita, "dd/MM/yyyy");
		inicializarComboBoxEmpresa();
	}
	
	public void updateDadosFormulario() {
		if (entidade == null) {
			throw new IllegalStateException("Entidade esta nula");
		}
		txtIdFornecedor.setText(String.valueOf(entidade.getIdFornecedor()));
		txtNome.setText(entidade.getNome());
		txtSobrenome.setText(entidade.getSobrenome());
		txtCpf.setText(entidade.getCpf());
		txtRg.setText(entidade.getRg());
		txtTelefone.setText(entidade.getTelefone());
		if (entidade.getDataUltimaVisita() != null) {
			dpDataUltimaVisita.setValue(LocalDate.ofInstant(entidade.getDataUltimaVisita().toInstant(), ZoneId.systemDefault()));
		}
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
		obsLista = FXCollections.observableArrayList(listaEmpresas);
		comboBoxEmpresa.setItems(obsLista);
	}
	
	private void setMensagensErros(Map<String, String> erros) {
		Set<String> campos = erros.keySet();
		
		txtNomeErro.setText(campos.contains("nome") ? erros.get("nome") : "");
		txtSobrenomeErro.setText(campos.contains("sobrenome") ? erros.get("sobrenome") : "");
		txtCpfErro.setText(campos.contains("cpf") ? erros.get("cpf") : "");
		txtRgErro.setText(campos.contains("rg") ? erros.get("rg") : "");
		txtTelefoneErro.setText(campos.contains("telefone") ? erros.get("telefone") : "");
		txtDataUltimaVisitaErro.setText(campos.contains("dataUltimaVisita") ? erros.get("dataUltimaVisita") : "");
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
