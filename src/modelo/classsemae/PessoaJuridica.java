package modelo.classsemae;

import java.io.Serializable;

public class PessoaJuridica implements Serializable {

	private static final long serialVersionUID = 1L;

	private String nome;
	private String cnpj;

	public PessoaJuridica(String nome, String cnpj) {
		this.nome = nome;
		this.cnpj = cnpj;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

}
