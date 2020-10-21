package modelo.entidades;

import java.io.Serializable;

public class FornecedorMarca implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer idFornecedorMarca;
	private Fornecedor fornecedor;
	private Marca marca;

	public FornecedorMarca(Integer idFornecedorMarca, Fornecedor fornecedor, Marca marca) {
		this.idFornecedorMarca = idFornecedorMarca;
		this.fornecedor = fornecedor;
		this.marca = marca;
	}

	public Integer getIdFornecedorMarca() {
		return idFornecedorMarca;
	}

	public void setIdFornecedorMarca(Integer idFornecedorMarca) {
		this.idFornecedorMarca = idFornecedorMarca;
	}

	public Fornecedor getFornecedor() {
		return fornecedor;
	}

	public void setFornecedor(Fornecedor fornecedor) {
		this.fornecedor = fornecedor;
	}

	public Marca getMarca() {
		return marca;
	}

	public void setMarca(Marca marca) {
		this.marca = marca;
	}

	@Override
	public String toString() {
		return "FornecedorMarca [idFornecedorMarca=" + idFornecedorMarca + ", fornecedor=" + fornecedor + ", marca="
				+ marca + "]";
	}

}
