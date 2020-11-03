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
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import modelo.entidades.Cliente;
import modelo.entidades.Funcionario;
import modelo.entidades.Venda;
import modelo.exceptions.ValidacaoException;
import modelo.servicos.ClienteServico;
import modelo.servicos.FuncionarioServico;
import modelo.servicos.VendaServico;

public class VendaFormularioController implements Initializable{

	private Venda entidade;
	
	private VendaServico servico;
	
	private ClienteServico clienteServico;
	
	private FuncionarioServico funcionarioServico;
	
	private List<DadosMudancaOuvintes> dadosMudancasOuvintes = new ArrayList<>();
	
	private ObservableList<Cliente> obsListaCliente;
	
	private ObservableList<Funcionario> obsListaFuncionario;
	
	@FXML
	private TextField txtIdVenda;
	
	@FXML
	private TextField txtPrecoTotal;
	
	@FXML
	private DatePicker dpDataHoraVenda;
	
	@FXML
	private ComboBox<Funcionario> comboBoxFuncionario;

	@FXML
	private ComboBox<Cliente> comboBoxCliente;
	
	@FXML
	private Label txtDataHoraVendaErro;
	
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
	
	public void setVenda(Venda entidade) {
		this.entidade = entidade;
	}
	
	public void setVendaServicos(VendaServico servico, ClienteServico clienteServico, FuncionarioServico funcionarioServico) {
		this.servico = servico;
		this.clienteServico = clienteServico;
		this.funcionarioServico = funcionarioServico;
	}
	
	public void addOuvintes(DadosMudancaOuvintes ouvinte) {
		dadosMudancasOuvintes.add(ouvinte);
	}
	
	private void notificarOuvintes() {
		for (DadosMudancaOuvintes ouvinte : dadosMudancasOuvintes) {
			ouvinte.onMudancaDados();
		}
	}
	
	private Venda getDadosFormulario() {
		Venda obj = new Venda();
		
		ValidacaoException excecao = new ValidacaoException("Erro de validacao");
		
		if (dpDataHoraVenda.getValue() == null) {
			excecao.addErros("dataUltimaVisita", "O campo não pode ser vazio");
		} else {
			Instant instante = Instant.from(dpDataHoraVenda.getValue().atStartOfDay(ZoneId.systemDefault()));
			obj.setDataHoraVenda(Date.from(instante));
		}
		
		obj.setIdVenda(Utils.transformarInteger(txtIdVenda.getText()));
		obj.setPrecoTotal(Utils.transformarDouble(txtPrecoTotal.getText()));
		obj.setCliente(comboBoxCliente.getValue());
		obj.setFuncionario(comboBoxFuncionario.getValue());
		
		if (excecao.getErros().size() > 0) {
			throw excecao;
		}
		
		return obj;
	}

	private void inicializarNodes() {
		Constraints.setTextFieldInteger(txtIdVenda);
		Utils.formatarDatePicker(dpDataHoraVenda, "dd/MM/yyyy");
		inicializarComboBoxCliente();
		inicializarComboBoxFuncionario();
	}
	
	public void updateDadosFormulario() {
		if (entidade == null) {
			throw new IllegalStateException("Entidade esta vazia");
		}
		
		txtIdVenda.setText(String.valueOf(entidade.getIdVenda()));
		Locale.setDefault((Locale.US));
		if (entidade.getPrecoTotal() == null) {
			txtPrecoTotal.setText(String.format("%.2f", 0.0));
		} else {
			txtPrecoTotal.setText(String.format("%.2f", entidade.getPrecoTotal()));
		}
		if (entidade.getDataHoraVenda() != null) {
			dpDataHoraVenda.setValue(LocalDate.ofInstant(entidade.getDataHoraVenda().toInstant(), ZoneId.systemDefault()));
		}
		if (entidade.getCliente() == null) {
			comboBoxCliente.getSelectionModel().selectFirst();			
		} else {
			comboBoxCliente.setValue(entidade.getCliente());
		}
		if (entidade.getFuncionario() == null) {
			comboBoxFuncionario.getSelectionModel().selectFirst();
		} else {
			comboBoxFuncionario.setValue(entidade.getFuncionario());
		}
	}
	
	private void setMensagensErros(Map<String, String> erros) {
		Set<String> campos = erros.keySet();
		
		txtDataHoraVendaErro.setText(campos.contains("dataUltimaVisita") ? erros.get("dataUltimaVisita") : "");
	}
	
	public void carregarObjetosAssociados() {
		if (clienteServico == null) {
			throw new IllegalStateException("ClienteServico esta nulo");			
		}
		if (funcionarioServico == null) {
			throw new IllegalStateException("FuncionarioServico esta nulo");
		}
		
		List<Cliente> listaCliente = clienteServico.acharTodos();
		obsListaCliente = FXCollections.observableArrayList(listaCliente);
		comboBoxCliente.setItems(obsListaCliente);

		List<Funcionario> listaFuncionario = funcionarioServico.acharTodos();
		obsListaFuncionario = FXCollections.observableArrayList(listaFuncionario);
		comboBoxFuncionario.setItems(obsListaFuncionario);	
	}
	
	private void inicializarComboBoxCliente() {
		Callback<ListView<Cliente>, ListCell<Cliente>> factory = lv -> new ListCell<Cliente>() {
			@Override
			protected void updateItem(Cliente item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getNome());
			}
		};
		comboBoxCliente.setCellFactory(factory);
		comboBoxCliente.setButtonCell(factory.call(null));
	}
	
	private void inicializarComboBoxFuncionario() {
		Callback<ListView<Funcionario>, ListCell<Funcionario>> factory = lv -> new ListCell<Funcionario>() {
			@Override
			protected void updateItem(Funcionario item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getNome());
			}
		};
		comboBoxFuncionario.setCellFactory(factory);
		comboBoxFuncionario.setButtonCell(factory.call(null));
	}
}
