package modelo.entidades;

import java.io.Serializable;
import java.util.Date;

public class Venda implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Integer idVenda;
	private Double valorTotal;
	private Date dataUltimaVisita;
	private Cliente cliente;
	private Funcionario funcionario;

	public Venda(Integer idVenda, Double valorTotal, Date dataUltimaVisita, Cliente cliente, Funcionario funcionario) {
		this.idVenda = idVenda;
		this.valorTotal = valorTotal;
		this.dataUltimaVisita = dataUltimaVisita;
		this.cliente = cliente;
		this.funcionario = funcionario;
	}

	public Integer getIdVenda() {
		return idVenda;
	}

	public void setIdVenda(Integer idVenda) {
		this.idVenda = idVenda;
	}

	public Double getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(Double valorTotal) {
		this.valorTotal = valorTotal;
	}

	public Date getDataUltimaVisita() {
		return dataUltimaVisita;
	}

	public void setDataUltimaVisita(Date dataUltimaVisita) {
		this.dataUltimaVisita = dataUltimaVisita;
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
		return "Venda [idVenda=" + idVenda + ", valorTotal=" + valorTotal + ", dataUltimaVisita=" + dataUltimaVisita
				+ ", cliente=" + cliente + ", funcionario=" + funcionario + "]";
	}

}
