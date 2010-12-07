package automatos.apd;

import java.util.ArrayList;
import java.util.List;

public class AutomatoPilha {
	public List<Estado> estados = new ArrayList<Estado>();
	public Estado estadoInicial;
	
	public void addEstado(Estado estado) {
		estados.add(estado);
	}
}
