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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import modelo.entidades.Empresa;
import modelo.exceptions.ValidacaoException;
import modelo.servicos.EmpresaServico;

public class EmpresaFormularioController implements Initializable{

	private Empresa entidade;
	
	private EmpresaServico servico;
	
	private List<DadosMudancaOuvintes> dadosMudancasOuvintes = new ArrayList<>();	
	
	@FXML
	private TextField txtId;
	
	@FXML 
	private TextField txtNome;
	
	@FXML
	private TextField txtCnpj;
	
	@FXML
	private TextField txtTelefone;
	
	@FXML
	private Label txtNomeErro;
	
	@FXML
	private Label txtCnpjErro;
	
	@FXML
	private Label txtTelefoneErro;
	
	@FXML
	private Button btSalvar;
	
	@FXML
	private Button btCancelar;
	
	public void setEmpresaServico(EmpresaServico servico) {
		this.servico = servico;
	}
	
	public void setEmpresa(Empresa entidade) {
		this.entidade = entidade;
	}
	
	public void addOuvintes(DadosMudancaOuvintes ouvinte) {
		dadosMudancasOuvintes.add(ouvinte);
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		inicializarNodes();
	}
	
	@FXML
	public void onBtSalvarAcao(ActionEvent evento) {
		if (servico == null) {
			throw new IllegalStateException("Servico esta nulo");
		}
		if (entidade == null) {
			throw new IllegalStateException("Entidade esta nulo");
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

	private Empresa getDadosFormulario() {
		
		Empresa obj = new Empresa();
		
		ValidacaoException excecao = new ValidacaoException("Erro de validacao");
		
		if (txtNome.getText() == null || txtNome.getText().trim().equals("")) {
			excecao.addErros("nome", "O campo não pode ser vazio");
		}
		
		if (txtCnpj.getText() == null || txtCnpj.getText().trim().equals("")) {
			excecao.addErros("cnpj", "O campo não pode ser vazio");
		} else if (txtCnpj.getText().trim().length() != 14) {
			excecao.addErros("cnpj", "O campo deve ter 14 caracteres");
		}
		
		if (txtTelefone.getText() == null || txtTelefone.getText().trim().equals("")) {
			excecao.addErros("telefone", "O campo não pode ser vazio");
		}
		
		obj.setIdEmpresa(Utils.transformarInteger(txtId.getText()));
		obj.setNome(txtNome.getText());
		obj.setCnpj(txtCnpj.getText());
		obj.setTelefone(txtTelefone.getText());
		
		if (excecao.getErros().size() > 0) {
			throw excecao;
		}
		
		return obj;
	}

	private void notificarOuvintes() {
		for (DadosMudancaOuvintes ouvintes : dadosMudancasOuvintes) {
			ouvintes.onMudancaDados();
		}		
	}
	
	@FXML
	public void onBtCancelarAcao(ActionEvent evento) {
		Utils.stageAtual(evento).close();
	}
	
	private void inicializarNodes() {
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtNome, 60);
		Constraints.setTextFieldInteger(txtCnpj);
		Constraints.setTextFieldMaxLength(txtTelefone, 16);
	}
	
	public void updateDadosFormulario() {
		if (entidade == null) {
			throw new IllegalStateException("Entidade esta nulo");
		}
		
		txtId.setText(String.valueOf(entidade.getIdEmpresa()));
		txtNome.setText(entidade.getNome());
		txtCnpj.setText(entidade.getCnpj());
		txtTelefone.setText(entidade.getTelefone());
	}
	
	private void setMensagensErros(Map<String, String> erros) {
		Set<String> campos = erros.keySet();
		
		txtNomeErro.setText(campos.contains("nome") ? erros.get("nome") : "");
		txtCnpjErro.setText(campos.contains("cnpj") ? erros.get("cnpj") : "");
		txtTelefoneErro.setText(campos.contains("telefone") ? erros.get("telefone") : "");
		
	}
}
