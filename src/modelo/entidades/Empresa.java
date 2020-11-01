package modelo.entidades;

import java.io.Serializable;

import modelo.classsemae.PessoaJuridica;

public class Empresa extends PessoaJuridica implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer idEmpresa;
	private String telefone;
	
	public Empresa() {
	}
	
	public Empresa( Integer idEmpresa, String nome, String cnpj, String telefone) {
		super(nome, cnpj);
		this.idEmpresa = idEmpresa;
		this.telefone = telefone;
	}

	public Integer getIdEmpresa() {
		return idEmpresa;
	}

	public void setIdEmpresa(Integer idEmpresa) {
		this.idEmpresa = idEmpresa;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	@Override
	public String toString() {
		return "Empresa [idEmpresa=" + idEmpresa + ", telefone=" + telefone + ", getNome()=" + getNome()
				+ ", getCnpj()=" + getCnpj() + "]";
	}

}
