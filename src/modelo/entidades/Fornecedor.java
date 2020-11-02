package modelo.entidades;

import java.io.Serializable;
import java.util.Date;

import modelo.classsemae.PessoaFisica;

public class Fornecedor extends PessoaFisica implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer idFornecedor;
	private Date dataUltimaVisita;
	private Empresa empresa;

	public Fornecedor() {
	}
	
	public Fornecedor(Integer idFornecedor, String nome, String sobrenome, String cpf, String rg, String telefone,
			Date dataUltimaVisita, Empresa empresa) {
		super(nome, sobrenome, cpf, rg, telefone);
		this.idFornecedor = idFornecedor;
		this.dataUltimaVisita = dataUltimaVisita;
		this.empresa = empresa;
	}

	public Integer getIdFornecedor() {
		return idFornecedor;
	}

	public void setIdFornecedor(Integer idFornecedor) {
		this.idFornecedor = idFornecedor;
	}

	public Date getDataUltimaVisita() {
		return dataUltimaVisita;
	}

	public void setDataUltimaVisita(Date dataUltimaVisita) {
		this.dataUltimaVisita = dataUltimaVisita;
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}
/*
	@Override
	public String toString() {
		return "Fornecedor [idFornecedor=" + idFornecedor + ", dataUltimaVisita=" + dataUltimaVisita + ", empresa="
				+ empresa + ", getNome()=" + getNome() + ", getSobrenome()=" + getSobrenome() + ", getCpf()=" + getCpf()
				+ ", getRg()=" + getRg() + ", getTelefone()=" + getTelefone() + "]";
	}
*/
	@Override
	public String toString() {
		return getNome();
	}
}
