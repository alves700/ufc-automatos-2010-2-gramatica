package automatos.apd;

import java.util.ArrayList;
import java.util.List;

public class Transicao {
	
	public String simbolo;
	public List<String> topoPilhaEsperado = new ArrayList<String>();
	
	public Estado estadoDestino;
	public List<String> adicionarTopoPilha = new ArrayList<String>();
	
	public Transicao(String simbolo, List<String> topoPilhaEsperado) {
		super();
		this.simbolo = simbolo;
		this.topoPilhaEsperado = topoPilhaEsperado;
	}
	
	public Transicao(String simbolo, Estado estadoDestino, String[] topoPilhaEsperado, String[] adicionarTopoPilha) {
		super();
		this.simbolo = simbolo;
		this.estadoDestino = estadoDestino;
		for (String string : topoPilhaEsperado) {
			this.topoPilhaEsperado.add(string);
		}
		for (String string : adicionarTopoPilha) {
			this.adicionarTopoPilha.add(string);
		}
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((simbolo == null) ? 0 : simbolo.hashCode());
		result = prime * result + ((topoPilhaEsperado == null) ? 0 : topoPilhaEsperado.hashCode());
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
		Transicao other = (Transicao) obj;
		if (simbolo == null) {
			if (other.simbolo != null)
				return false;
		} else if (!simbolo.equals(other.simbolo))
			return false;
		if (topoPilhaEsperado == null) {
			if (other.topoPilhaEsperado != null)
				return false;
		} else if (!topoPilhaEsperado.equals(other.topoPilhaEsperado))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return String.format("%s(%s,%s,%s)", simbolo, estadoDestino.label, topoPilhaEsperado.toString(), adicionarTopoPilha.toString());
	}
	
	public String toString(Estado estado) {
		return String.format("(%s,%s),(%s,%s,%s)", simbolo, estadoDestino.label, topoPilhaEsperado.toString(), adicionarTopoPilha.toString());
	}
}
