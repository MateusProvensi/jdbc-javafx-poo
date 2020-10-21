package modelo.entidades;

import java.io.Serializable;

import modelo.classsemae.PessoaFisica;

public class Funcionario extends PessoaFisica implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer idFuncionario;
	private Integer numeroCaixa;

	public Funcionario( Integer idFuncionario, String nome, String sobrenome, String cpf, String rg, String telefone,
			Integer numeroCaixa) {
		super(nome, sobrenome, cpf, rg, telefone);
		this.idFuncionario = idFuncionario;
		this.numeroCaixa = numeroCaixa;
	}

	public Integer getIdFuncionario() {
		return idFuncionario;
	}

	public void setIdFuncionario(Integer idFuncionario) {
		this.idFuncionario = idFuncionario;
	}

	public Integer getNumeroCaixa() {
		return numeroCaixa;
	}

	public void setNumeroCaixa(Integer numeroCaixa) {
		this.numeroCaixa = numeroCaixa;
	}

	@Override
	public String toString() {
		return "Funcionario [idFuncionario=" + idFuncionario + ", numeroCaixa=" + numeroCaixa + ", getNome()="
				+ getNome() + ", getSobrenome()=" + getSobrenome() + ", getCpf()=" + getCpf() + ", getRg()=" + getRg()
				+ ", getTelefone()=" + getTelefone() + "]";
	}

}
