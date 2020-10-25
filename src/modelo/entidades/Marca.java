package modelo.entidades;

import java.io.Serializable;

import modelo.classsemae.PessoaJuridica;

public class Marca extends PessoaJuridica implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer idMarca;
	private Empresa empresa;

	public Marca() {
	}
	
	public Marca(Integer idMarca, String nome, String cnpj, Empresa empresa) {
		super(nome, cnpj);
		this.idMarca = idMarca;
		this.empresa = empresa;
	}

	public Integer getIdMarca() {
		return idMarca;
	}

	public void setIdMarca(Integer idMarca) {
		this.idMarca = idMarca;
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	@Override
	public String toString() {
		return "Marca [idMarca=" + idMarca + ", empresa=" + empresa + ", getNome()=" + getNome() + ", getCnpj()="
				+ getCnpj() + "]";
	}

}
