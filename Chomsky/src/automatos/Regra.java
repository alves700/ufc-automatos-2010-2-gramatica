package automatos;

import java.util.ArrayList;
import java.util.List;

public class Regra {
	public Simbolo<Integer> esquerda;
	public List<Simbolo<?>> direita = new ArrayList<Simbolo<?>>();
	
	public Regra(Simbolo<Integer> esquerda) {
		super();
		this.esquerda = esquerda;
	}
	
	public void addSimboloTerminalDireita(String valor) {
		direita.add(new Simbolo<String>(valor, true));
	}
	
	public void addSimboloNaoTerminalDireita(Integer valor) {
		direita.add(new Simbolo<Integer>(valor, false));
	}
	
	public void addSimboloDireita(Simbolo<?> simbolo) {
		direita.add(simbolo);
	}
	
	@Override
	public String toString() {
		String str = esquerda + " -> ";
		for (Simbolo<?> simboloDireita : direita) {
			str += simboloDireita;
		}
		return str;
	}
}
