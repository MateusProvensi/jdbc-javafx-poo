package modelo.entidades;

import java.io.Serializable;
import java.util.Date;

public class Venda implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Integer idVenda;
	private Double precoTotal;
	private Date dataHoraVenda;
	private Cliente cliente;
	private Funcionario funcionario;

	public Venda() {
	}
	
	public Venda(Integer idVenda, Double precoTotal, Date dataHoraVenda, Cliente cliente, Funcionario funcionario) {
		this.idVenda = idVenda;
		this.precoTotal = precoTotal;
		this.dataHoraVenda = dataHoraVenda;
		this.cliente = cliente;
		this.funcionario = funcionario;
	}

	public Integer getIdVenda() {
		return idVenda;
	}

	public void setIdVenda(Integer idVenda) {
		this.idVenda = idVenda;
	}

	public Double getPrecoTotal() {
		return precoTotal;
	}

	public void setPrecoTotal(Double precoTotal) {
		this.precoTotal = precoTotal;
	}

	public Date getDataHoraVenda() {
		return dataHoraVenda;
	}

	public void setDataHoraVenda(Date dataHoraVenda) {
		this.dataHoraVenda = dataHoraVenda;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Funcionario getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}

	@Override
	public String toString() {
		return "Venda [idVenda=" + idVenda + ", precoTotal=" + precoTotal + ", dataHoraVenda=" + dataHoraVenda
				+ ", cliente=" + cliente + ", funcionario=" + funcionario + "]";
	}

}
