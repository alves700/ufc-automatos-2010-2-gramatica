package automatos.apd;

import java.util.ArrayList;
import java.util.List;

public class Estado implements Comparable<Estado> {
	public String label;
	public boolean isFinal;
	public List<Transicao> transicoes = new ArrayList<Transicao>();
	
	public Estado(String label, boolean isFinal) {
		super();
		this.label = label;
		this.isFinal = isFinal;
	}

	public void addTransicao(Transicao transicao) {
		transicoes.add(transicao);
	}
	
	@Override
	public String toString() {
		return String.format("<%s, Final: %s>", label, isFinal);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((label == null) ? 0 : label.hashCode());
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
		Estado other = (Estado) obj;
		if (label == null) {
			if (other.label != null)
				return false;
		} else if (!label.equals(other.label))
			return false;
		return true;
	}

	@Override
	public int compareTo(Estado estado) {
		return label.compareTo(estado.label);
	}
}
