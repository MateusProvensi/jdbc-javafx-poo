package modelo.entidades;

import java.io.Serializable;
import java.util.Date;

public class Item implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Integer idItem;
	private String descricaoItem;
	private String codigoBarras;
	private Double precoVenda;
	private Integer quantidade;
	private Date validade;
	private String corredor;
	private FornecedorMarca fornecedorMarca;
	
	public Item() {
	}

	public Item(Integer idItem, String descricaoItem, String codigoBarras, Double precoVenda,
			Integer quantidade, Date validade, String corredor, FornecedorMarca fornecedorMarca) {
		this.idItem = idItem;
		this.descricaoItem = descricaoItem;
		this.codigoBarras = codigoBarras;
		this.precoVenda = precoVenda;
		this.quantidade = quantidade;
		this.validade = validade;
		this.corredor = corredor;
		this.fornecedorMarca = fornecedorMarca;
	}

	public Integer getIdItem() {
		return idItem;
	}

	public void setIdItem(Integer idItem) {
		this.idItem = idItem;
	}

	public String getDescricaoItem() {
		return descricaoItem;
	}

	public void setDescricaoItem(String descricaoItem) {
		this.descricaoItem = descricaoItem;
	}

	public String getCodigoBarras() {
		return codigoBarras;
	}

	public void setCodigoBarras(String codigoBarras) {
		this.codigoBarras = codigoBarras;
	}

	public Double getPrecoVenda() {
		return precoVenda;
	}

	public void setPrecoVenda(Double precoVenda) {
		this.precoVenda = precoVenda;
	}


	public Integer getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}

	public Date getValidade() {
		return validade;
	}

	public void setValidade(Date validade) {
		this.validade = validade;
	}

	public String getCorredor() {
		return corredor;
	}

	public void setCorredor(String corredor) {
		this.corredor = corredor;
	}

	public FornecedorMarca getFornecedorMarca() {
		return fornecedorMarca;
	}

	public void setFornecedorMarca(FornecedorMarca fornecedorMarca) {
		this.fornecedorMarca = fornecedorMarca;
	}

	@Override
	public String toString() {
		return "Item [idItem=" + idItem + ", descricaoItem=" + descricaoItem + ", codigoBarras=" + codigoBarras
				+ ", precoVenda=" + precoVenda + ", quantidade=" + quantidade + ", validade=" + validade 
				+ ", corredor=" + corredor + ", fornecedorMarca=" + fornecedorMarca + "]";
	}

}
