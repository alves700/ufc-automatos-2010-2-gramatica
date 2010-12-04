package automatos;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class Chomsky {

   private Chomsky() {
   }

   public static void construirGramaticaChomsky(Gramatica g) {
      eliminarRegrasLongas(g);
      eliminarRegraE(g);
   }

   private static void eliminarRegrasLongas(Gramatica g) {
      List<Regra> novasRegras = new ArrayList<Regra>();
      Iterator<Regra> regrasIt = g.regras.iterator();

      while (regrasIt.hasNext()) {
         Regra regraLonga = (Regra) regrasIt.next();
         //Se for uma regraIt longa, transforma-a em regras curtas:
         if (regraLonga.direita.size() >= 3) {
            Simbolo<Integer> simbEsquerdaNovaRegraCurta = regraLonga.esquerda;
            //Para cada simbolo da regraIt longa, cria uma nova regraIt curta:
            for (int i = 0; i < regraLonga.direita.size() - 1; i++) {
               Simbolo<?> simbolo = regraLonga.direita.get(i);
               Regra regraCurta = new Regra(simbEsquerdaNovaRegraCurta);
               regraCurta.direita.add(simbolo);

               /* Adiciona simbolo não terminal para a próxima regraIt curta
                * caso esta não seja a última regraIt curta
                */
               if (i < regraLonga.direita.size() - 2) {
                  simbEsquerdaNovaRegraCurta = g.gerarProximoSimboloNaoTerminal();
                  g.addSimbNaoTerminal(simbEsquerdaNovaRegraCurta);
                  regraCurta.direita.add(simbEsquerdaNovaRegraCurta);
               } else {
                  simbEsquerdaNovaRegraCurta = null;
                  regraCurta.direita.add(regraLonga.direita.get(i + 1));
               }
               novasRegras.add(regraCurta);
            }
            //Remove a regraIt longa atual:
            regrasIt.remove();
         }
      }
      g.regras.addAll(novasRegras);
   }

   /*
    * passos do exemplo 3.6.1 - pag 152
    */
   private static void eliminarRegraE(Gramatica g) {
      Iterator<Regra> regraIt = g.regras.iterator();
      HashSet<Simbolo> set = new HashSet<Simbolo>();

      // regras que deverao ser eliminadas
      List<Regra> regrasVazia = new ArrayList<Regra>();

      // Ne = e
      // para cada simbolo no conjunto inicial Ne

      Simbolo e = new Simbolo("e", true);
      set.add(e);

      // enquanto existir uma regraIt A->alfa com alfa pertencente a Ne*
      while (regraIt.hasNext()) {
         Regra atual = (Regra) regraIt.next();
         if (atual.direita.size() == 1) {
            // pelo que entendi, alfa = e(vazio)

            // se a regraIt eh do tipo X->e onde 'e' é vazio
            // o vetor de regras tem tamanho 1
            // 'e' sempre é terminal
            if (atual.direita.get(0).equals(e)) {
               // adicione alfa ao conjunto Ne
               set.add(atual.esquerda);
               // regraIt vazia 'marcada' para ser eliminada
               regrasVazia.add(atual);
            }
         }
      }

      // feito o conjunto Ne
      // excluir de G todas as regras vazias
      for (Regra r : regrasVazia) {
         g.regras.remove(r);
      }

      List<Regra> novasRegras = new ArrayList<Regra>();
      novasRegras = g.regras;

      // criamos A->C
      for (Regra regra : novasRegras) {

         // para uma regra de tamanho 2
         if (regra.direita.size() == 2) {
            Simbolo naoTerminalEsquerda = regra.esquerda;
            Simbolo naoTerminalDireitaVazio = regra.direita.get(0);
            Simbolo naoTerminalDireita = regra.direita.get(1);

            Regra novaRegra = new Regra(naoTerminalEsquerda);
            // para cada regraIt A-> BC ou A->CB
            // para B pertencente a Ne, ou seja, vazio
            // para C pertecente a V(alfabeto)
            if ((set.contains(naoTerminalDireitaVazio)) && (g.alfabeto.contains(naoTerminalDireita))) {

               // se o simbolo naoTerminalDireita pertence ao alfabeto da Gramatica, ele é adicionado
               novaRegra.addSimboloDireita(naoTerminalDireita);
            } else if ((set.contains(naoTerminalDireita)) && (g.alfabeto.contains(naoTerminalDireitaVazio))) {

               // se o simbolo naoTerminalDireitaVazio pertence ao alfabeto da Gramatica, ele é adicionado
               novaRegra.addSimboloDireita(naoTerminalDireitaVazio);
            }
            g.addRegra(novaRegra);
         } else {
         }
      }
   }
}
