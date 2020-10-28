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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import modelo.entidades.Cliente;
import modelo.exceptions.ValidacaoException;
import modelo.servicos.ClienteServico;

public class ClienteFormularioController implements Initializable{

	private Cliente entidade;
	
	private ClienteServico servico;
	
	private List<DadosMudancaOuvintes> dadosMudancasOuvintes = new ArrayList<>();
	
	@FXML
	private TextField txtId;
	
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
	private Button btSalvar;
	
	@FXML
	private Button btCancelar;
	
	public void setClienteServico(ClienteServico servico) {
		this.servico = servico;
	}
	
	public void setCliente(Cliente entidade) {
		this.entidade = entidade;
	}
	
	public void addOuvintes(DadosMudancaOuvintes ouvinte) {
		dadosMudancasOuvintes.add(ouvinte);
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rs) {
		inicializarNodes();
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
	
	private void notificarOuvintes() {
		for (DadosMudancaOuvintes ouvintes : dadosMudancasOuvintes) {
			ouvintes.onMudancaDados();
		}
	}
	
	private Cliente getDadosFormulario() {
		Cliente obj = new Cliente();
		
		ValidacaoException excecao = new ValidacaoException("Erro de validacao");
		
		// VALIDAR OS DADOS DO FORMULARIO POSTERIORMENTE
		
		obj.setIdCliente(Utils.transformarInteger(txtId.getText()));
		obj.setNome(txtNome.getText());
		obj.setSobrenome(txtSobrenome.getText());
		obj.setCpf(txtCpf.getText());
		obj.setRg(txtRg.getText());
		obj.setTelefone(txtTelefone.getText());
		
		if (excecao.getErros().size() > 0) {
			throw excecao;
		}
		
		return obj;
	}
	
	@FXML
	public void onBtCancelarAction(ActionEvent evento) {
		Utils.stageAtual(evento).close();
	}
	
	private void inicializarNodes() {
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtNome, 30);
		Constraints.setTextFieldMaxLength(txtSobrenome, 50);
		Constraints.setTextFieldMaxLength(txtCpf, 11);
		Constraints.setTextFieldMaxLength(txtRg, 15);
		Constraints.setTextFieldMaxLength(txtRg, 16);
	}
	
	public void updateDadosFormulario() {
		
		if (entidade == null) {
			throw new IllegalStateException("Entidade esta nulo");
		}
		
		txtId.setText(String.valueOf(entidade.getIdCliente()));
		txtNome.setText(entidade.getNome());
		txtSobrenome.setText(entidade.getSobrenome());
		txtCpf.setText(entidade.getCpf());
		txtRg.setText(entidade.getRg());
		txtTelefone.setText(entidade.getTelefone());
	}
	
	// CRIAR UM METODO PARA ADICIONAR ERROS E VERIFICAR DEPOIS
	
}
