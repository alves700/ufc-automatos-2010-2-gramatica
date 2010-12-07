package automatos.apd;

import java.util.ArrayList;
import java.util.List;

public class Estado {
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
}
