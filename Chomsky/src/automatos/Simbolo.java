package automatos;

public class Simbolo<Tipo> {
	public Tipo valor;
	public boolean terminal;
	
	public Simbolo(Tipo valor, boolean terminal) {
		super();
		this.valor = valor;
		this.terminal = terminal;
	}
}
