package automatos;

import java.util.ArrayList;
import java.util.List;

public class Gramatica {

	public String simbInicial;
	public List<Simbolo<String>> alfabeto = new ArrayList<Simbolo<String>>();
	public List<Simbolo<Integer>> simbNaoTerminais = new ArrayList<Simbolo<Integer>>();
	public List<Regra> regras = new ArrayList<Regra>();

	public Simbolo<Integer> gerarProximoSimboloNaoTerminal() {
		Integer maior;
		if (simbNaoTerminais.isEmpty()) {
			maior = 0;
		} else {
			maior = simbNaoTerminais.get(0).valor;
			for (Simbolo<Integer> simbolo : simbNaoTerminais) {
				if (simbolo.valor > maior) {
					maior = simbolo.valor;
				}
			}
		}
		Simbolo<Integer> simbolo = new Simbolo<Integer>(maior, false);
		return simbolo;
	}
}
