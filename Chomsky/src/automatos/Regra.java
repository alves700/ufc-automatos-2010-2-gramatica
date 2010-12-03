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
}
