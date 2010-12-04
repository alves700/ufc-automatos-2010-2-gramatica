package automatos;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Regra implements Comparable<Regra> {
	
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
		Iterator<Simbolo<?>> direitaIt = direita.iterator();
		while (direitaIt.hasNext()) {
			Simbolo<?> simbolo = (Simbolo<?>) direitaIt.next();
			str += simbolo;
			if (direitaIt.hasNext()) {
				str += ".";
			}
		}
		return str;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((direita == null) ? 0 : direita.hashCode());
		result = prime * result + ((esquerda == null) ? 0 : esquerda.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Regra other = (Regra) obj;
		if (direita == null) {
			if (other.direita != null)
				return false;
		} else if (!direita.equals(other.direita))
			return false;
		if (esquerda == null) {
			if (other.esquerda != null)
				return false;
		} else if (!esquerda.equals(other.esquerda))
			return false;
		return true;
	}

	@Override
	public int compareTo(Regra regra) {
		return this.esquerda.compareTo(regra.esquerda);
	}
}
