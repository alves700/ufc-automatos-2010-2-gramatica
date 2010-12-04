package automatos;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Chomsky {
	
	private Chomsky() {
	}
	
	public static void construirGramaticaChomsky(Gramatica g) {
		eliminarRegrasLongas(g);
	}

	private static void eliminarRegrasLongas(Gramatica g) {
		List<Regra> novasRegras = new ArrayList<Regra>();
		Iterator<Regra> regrasIt = g.regras.iterator();
		
		while (regrasIt.hasNext()) {
			Regra regraLonga = (Regra) regrasIt.next();
			//Se for uma regra longa, transforma-a em regras curtas:
			if (regraLonga.direita.size() >= 3) {
				Simbolo<Integer> esquerdaNovaRegraCurta = regraLonga.esquerda;
				//Para cada simbolo da regra longa, cria uma nova regra curta:
				for (int i = 0; i < regraLonga.direita.size() - 1; i++) {
					Simbolo<?> simbolo = regraLonga.direita.get(i);
					Regra regraCurta = new Regra(esquerdaNovaRegraCurta);
					regraCurta.direita.add(simbolo);

					/* Adiciona simbolo não terminal para a próxima regra curta
					 * caso esta não seja a última regra curta
					 */
					if (i < regraLonga.direita.size() - 1) {
						esquerdaNovaRegraCurta = g.gerarProximoSimboloNaoTerminal(); 
						regraCurta.direita.add(esquerdaNovaRegraCurta);
					} else {
						esquerdaNovaRegraCurta = null;
						regraCurta.direita.add(regraLonga.direita.get(i + 1));
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
