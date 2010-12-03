package automatos;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Chomsky {
	
	private Chomsky() {
	}
	
	public void construirGramaticaChomsky(Gramatica g) {
		eliminarRegrasLongas(g);
	}

	private void eliminarRegrasLongas(Gramatica g) {
		List<Regra> novasRegras = new ArrayList<Regra>();
		Iterator<Regra> regrasIt = g.regras.iterator();
		
		while (regrasIt.hasNext()) {
			Regra regra = (Regra) regrasIt.next();
			//Se for uma regra longa, transforma-a em regras curtas:
			if (regra.direita.size() >= 3) {
				Simbolo<Integer> esquerdaNovaRegraCurta = regra.esquerda;
				//Para cada simbolo da regra longa, cria uma nova regra curta:
				for (int i = 0; i < regra.direita.size() - 1; i++) {
					Simbolo<?> simbolo = regra.direita.get(i);
					Regra regraCurta = new Regra(esquerdaNovaRegraCurta);
					regraCurta.direita.add(simbolo);

					/* Adiciona simbolo não terminal para a próxima regra curta
					 * caso esta não seja a última regra curta
					 */
					if (i < regra.direita.size() - 1) {
						esquerdaNovaRegraCurta = g.gerarProximoSimboloNaoTerminal(); 
						regraCurta.direita.add(esquerdaNovaRegraCurta);
					} else {
						esquerdaNovaRegraCurta = null;
						regraCurta.direita.add(regra.direita.get(i + 1));
					}
					novasRegras.add(regraCurta);
				}
				//Remove a regra longa atual:
				regrasIt.remove();
			}
		}
		g.regras.addAll(novasRegras);
	}
}
