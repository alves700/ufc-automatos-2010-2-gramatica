package automatos;

public class Simbolo<Tipo> {
	public Tipo valor;
	public boolean terminal;
	
	public Simbolo(Tipo valor, boolean terminal) {
		super();
		this.valor = valor;
		this.terminal = terminal;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (terminal ? 1231 : 1237);
		result = prime * result + ((valor == null) ? 0 : valor.hashCode());
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
		Simbolo<?> other = (Simbolo<?>) obj;
		if (terminal != other.terminal)
			return false;
		if (valor == null) {
			if (other.valor != null)
				return false;
		} else if (!valor.equals(other.valor))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return valor.toString();
	}
}
