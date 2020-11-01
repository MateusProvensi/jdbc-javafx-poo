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
import modelo.entidades.Funcionario;
import modelo.exceptions.ValidacaoException;
import modelo.servicos.FuncionarioServico;

public class FuncionarioFormularioController implements Initializable{

	Funcionario entidade;
	
	FuncionarioServico servico;
	
	List<DadosMudancaOuvintes> dadosMudancasOuvintes = new ArrayList<>();
	
	@FXML
	private TextField txtIdFuncionario;
	
	@FXML
	private TextField txtNome;
	
	@FXML
	private TextField txtSobrenome;
	
	@FXML
	private TextField txtCpf;
	
	@FXML
	private TextField txtRG;
	
	@FXML
	private TextField txtTelefone;
	
	@FXML
	private TextField txtNumeroCaixa;
	
	@FXML
	private Label txtNomeErro;
	
	@FXML
	private Label txtSobrenomeErro;
	
	@FXML
	private Label txtCpfErro;
	
	@FXML
	private Label txtRGErro;
	
	@FXML
	private Label txtTelefoneErro;
	
	@FXML
	private Label txtNumeroCaixaErro;
	
	@FXML
	private Button btSalvar;
	
	@FXML
	private Button btCancelar;
	
	@FXML
	public void onBtSalvarAcao(ActionEvent evento){
		if (entidade == null) {
			throw new IllegalStateException("Entidade esta vazia");
		}
		if (servico == null) {
			throw new IllegalStateException("Servico esta vazio");
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
	
	@Override
	public void initialize(URL url, ResourceBundle rs) {
		inicializarNodes();
	}
	
	public void setFuncionario(Funcionario entidade) {
		this.entidade = entidade;
	}
	
	public void setFuncionarioServico(FuncionarioServico servico) {
		this.servico = servico;
	}
	
	public void addOuvintes(DadosMudancaOuvintes ouvinte) {
		dadosMudancasOuvintes.add(ouvinte);
	}
	
	private void notificarOuvintes() {
		for (DadosMudancaOuvintes ouvinte : dadosMudancasOuvintes) {
			ouvinte.onMudancaDados();
		}
	}
	
	private Funcionario getDadosFormulario() {
		Funcionario obj = new Funcionario();
		
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
		if (txtRG.getText() == null || txtRG.getText().trim().equals("")) {
			excecao.addErros("rg", "O campo não pode ser vazio");
		}		
		if (txtTelefone.getText() == null || txtTelefone.getText().trim().equals("")) {
			excecao.addErros("telefone", "O campo não pode ser vazio");
		}
		if (txtNumeroCaixa.getText() == null || txtNumeroCaixa.getText().trim().equals("")) {
			excecao.addErros("numeroCaixa", "O campo não pode ser vazio");
		}
		
		obj.setIdFuncionario(Utils.transformarInteger(txtIdFuncionario.getText()));
		obj.setNome(txtNome.getText());
		obj.setSobrenome(txtSobrenome.getText());
		obj.setCpf(txtCpf.getText());
		obj.setRg(txtRG.getText());
		obj.setTelefone(txtTelefone.getText());
		obj.setNumeroCaixa(Utils.transformarInteger(txtNumeroCaixa.getText()));
		
		if (excecao.getErros().size() > 0) {
			throw excecao;
		}
		
		return obj;
	}

	private void inicializarNodes() {
		Constraints.setTextFieldInteger(txtIdFuncionario);
		Constraints.setTextFieldMaxLength(txtNome, 30); 
		Constraints.setTextFieldMaxLength(txtSobrenome, 50);
		Constraints.setTextFieldInteger(txtCpf);
		Constraints.setTextFieldInteger(txtRG);
		Constraints.setTextFieldMaxLength(txtTelefone, 16);
		Constraints.setTextFieldInteger(txtNumeroCaixa);
	}
	
	public void updateDadosFormulario() {
		if (entidade == null) {
			throw new IllegalStateException("Entidade esta vazia");
		}
		
		txtIdFuncionario.setText(String.valueOf(entidade.getIdFuncionario()));
		txtNome.setText(entidade.getNome());
		txtSobrenome.setText(entidade.getSobrenome());
		txtCpf.setText(entidade.getSobrenome());
		txtRG.setText(entidade.getRg());
		txtTelefone.setText(entidade.getTelefone());
		txtNumeroCaixa.setText(String.valueOf(entidade.getNumeroCaixa()));
	}
	
	private void setMensagensErros(Map<String, String> erros) {
		Set<String> campos = erros.keySet();
		
		txtNomeErro.setText(campos.contains("nome") ? erros.get("nome") : "");
		txtSobrenomeErro.setText(campos.contains("sobrenome") ? erros.get("sobrenome") : "");
		txtCpfErro.setText(campos.contains("cpf") ? erros.get("cpf") : "");
		txtRGErro.setText(campos.contains("rg") ? erros.get("rg") : "");
		txtTelefoneErro.setText(campos.contains("telefone") ? erros.get("telefone") : "");
		txtNumeroCaixaErro.setText(campos.contains("numeroCaixa") ? erros.get("numeroCaixa") : "");
		
	}
	
}
