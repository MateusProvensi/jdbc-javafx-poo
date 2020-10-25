package gui;

import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import modelo.entidades.Cliente;
import modelo.servicos.ClienteServico;

public class ClienteListaController implements Initializable{

	private ClienteServico servico;
	
	private ObservableList<Cliente> obsList;
	
	@FXML
	private TableView<Cliente> tableViewCliente;
	
	@FXML
	private TableColumn<Cliente, Integer> tableColumnId;
	
	@FXML
	private TableColumn<Cliente, String> tableColumnNome;
	
	@FXML
	private TableColumn<Cliente, String> tableColumnSobrenome;
	
	@FXML
	private TableColumn<Cliente, String> tableColumnCpf;
	
	@FXML
	private TableColumn<Cliente, String> tableColumnRg;
	
	@FXML
	private TableColumn<Cliente, String> tableColumnTelefone;
	
	@FXML
	private Button btNovo;
	
	@FXML
	public void onBtNovoAcao() {
		System.out.println("onBtNovoAcao");
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		iniciarNodes();
		
	}

	public void setClienteServico(ClienteServico servico) {
		this.servico = servico;
	}
	
	private void iniciarNodes() {
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("idCliente"));
		tableColumnNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
		tableColumnSobrenome.setCellValueFactory(new PropertyValueFactory<>("sobrenome"));
		tableColumnCpf.setCellValueFactory(new PropertyValueFactory<>("cpf"));
		tableColumnRg.setCellValueFactory(new PropertyValueFactory<>("rg"));
		tableColumnTelefone.setCellValueFactory(new PropertyValueFactory<>("telefone"));
		
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewCliente.prefHeightProperty().bind(stage.heightProperty());
	}
	
	
	
}
