package modelo.entidades;

public class ItemVenda {

	private Integer idItemVenda;
	private Item item;
	private Venda venda;

	public ItemVenda() {
	}
	
	public ItemVenda(Integer idItemVenda, Item item, Venda venda) {
		this.idItemVenda = idItemVenda;
		this.item = item;
		this.venda = venda;
	}

	public Integer getIdItemVenda() {
		return idItemVenda;
	}

	public void setIdItemVenda(Integer idItemVenda) {
		this.idItemVenda = idItemVenda;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public Venda getVenda() {
		return venda;
	}

	public void setVenda(Venda venda) {
		this.venda = venda;
	}

	@Override
	public String toString() {
		return "ItemVenda [idItemVenda=" + idItemVenda + ", item=" + item + ", venda=" + venda + "]";
	}

}
