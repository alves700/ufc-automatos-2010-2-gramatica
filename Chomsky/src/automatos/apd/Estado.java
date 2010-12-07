package automatos.apd;

import java.util.ArrayList;
import java.util.List;

public class Estado {
	public String label;
	public boolean isFinal;
	public List<Transicao> transicoes = new ArrayList<Transicao>();
	
	public void addTransicao(Transicao transicao) {
		transicoes.add(transicao);
	}
}
