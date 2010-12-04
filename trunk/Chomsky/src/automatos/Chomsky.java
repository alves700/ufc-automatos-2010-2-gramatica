package automatos;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
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

        /*
         *  passos do exemplo 3.6.1 - pag 152
         */

        private static void eliminarRegraE(Gramatica g) {
           Iterator<Regra> regra = g.regras.iterator();
           HashSet<Simbolo> set = new HashSet<Simbolo>();

           // regras que deverao ser eliminadas
           List<Regra> regrasVazia = new ArrayList<Regra>();
           
           //para um simbolo e no conjunto inicial Ne
           Simbolo e = new Simbolo("e", true);
           set.add(e);

           // enquanto existir uma regra A->alfa com alfa pertencente a Ne*
           while (regra.hasNext()) {
              Regra atual = regra.next();
              if ( atual.direita.size()==1) {

                 // pelo que entendi, alfa = e(vazio)
                 if ( atual.direita.get(0).equals(e)){

                    // adicione alfa ao conjunto Ne
                    set.add(atual.esquerda);
                 }
              }

              // regra vazia 'marcada' para ser eliminada
              regrasVazia.add(atual);
           }

           // feito o conjunto Ne
           // excluir de G todas as regras vazias
           for ( Regra r : regrasVazia) {
            
           }
           // para cada regra A-> BC ou A->CB
           // para B pertencente a Ne, ou seja, vazio
           // para C pertecente a V(alfabeto)
           // criamos A->C


           
        }
}
