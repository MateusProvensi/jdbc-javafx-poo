package modelo.entidades;

import java.io.Serializable;
import java.util.Date;

public class Item implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Integer idItem;
	private String descricaoItem;
	private String codigoBarras;
	private Double precoVenda;
	private Double precoCompra;
	private Integer quantidade;
	private Date validade;
	private String corredor;
	private Fornecedor fornecedor;

	public Item(Integer idItem, String descricaoItem, String codigoBarras, Double precoVenda, Double precoCompra,
			Integer quantidade, Date validade, String corredor, Fornecedor fornecedor) {
		this.idItem = idItem;
		this.descricaoItem = descricaoItem;
		this.codigoBarras = codigoBarras;
		this.precoVenda = precoVenda;
		this.precoCompra = precoCompra;
		this.quantidade = quantidade;
		this.validade = validade;
		this.corredor = corredor;
		this.fornecedor = fornecedor;
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

	public Double getPrecoCompra() {
		return precoCompra;
	}

	public void setPrecoCompra(Double precoCompra) {
		this.precoCompra = precoCompra;
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

	public Fornecedor getFornecedor() {
		return fornecedor;
	}

	public void setFornecedor(Fornecedor fornecedor) {
		this.fornecedor = fornecedor;
	}

	@Override
	public String toString() {
		return "Item [idItem=" + idItem + ", descricaoItem=" + descricaoItem + ", codigoBarras=" + codigoBarras
				+ ", precoVenda=" + precoVenda + ", precoCompra=" + precoCompra + ", quantidade=" + quantidade
				+ ", validade=" + validade + ", corredor=" + corredor + ", fornecedor=" + fornecedor + "]";
	}

}
