package modelo.entidades;

import java.io.Serializable;

import modelo.classsemae.PessoaFisica;

public class Cliente extends PessoaFisica implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer idCliente;

	public Cliente() {
	}
	
	public Cliente( Integer idCliente, String nome, String sobrenome, String cpf, String rg, String telefone) {
		super(nome, sobrenome, cpf, rg, telefone);
		this.idCliente = idCliente;
	}

	public Integer getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(Integer idCliente) {
		this.idCliente = idCliente;
	}

	@Override
	public String toString() {
		return "Cliente [idCliente=" + idCliente + ", getNome()=" + getNome() + ", getSobrenome()=" + getSobrenome()
				+ ", getCpf()=" + getCpf() + ", getRg()=" + getRg() + ", getTelefone()=" + getTelefone() + "]";
	}

}
