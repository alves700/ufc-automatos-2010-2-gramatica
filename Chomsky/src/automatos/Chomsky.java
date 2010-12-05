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
      System.out.println("STRING DEPOIS DA PRIMEIRA FASE:");
      System.out.println(g.toString());
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
      HashSet<Simbolo<?>> set = new HashSet<Simbolo<?>>();

      // regras que deverao ser eliminadas
      HashSet<Regra> regrasVazia = new HashSet<Regra>();

      // Ne = e
      // para cada simbolo no conjunto inicial Ne

      Simbolo<String> e = new Simbolo<String>("e", true); //tem q ser simbolo tanto terminal quanto nao terminal
      set.add(e);
      int tamanhoAntigo = set.size();
      int tamanhoAtual = set.size();
      do { //Forma conjunto Ne: enquanto existe A->alpha com alpha pertencente ao conjunto A entra no conjunto.
    	  tamanhoAntigo = set.size();
    	  for(Regra atual : g.regras) { //Da uma volta a procura de elementos para serem adicionados, se possivel adiciona.
    		  if(set.containsAll(atual.direita)) {
    			  if(atual.direita.size() == 1 && atual.direita.get(0).terminal) regrasVazia.add(atual);
    			  set.add(atual.esquerda);
    		  }
    	  }
    	  tamanhoAtual = set.size();
      }while((tamanhoAtual - tamanhoAntigo) > 0); //se nenhum elemento foi adicionado, sai do laco.
      System.out.println("CONJUNTO NE:");
      System.out.println(set.toString());
      // enquanto existir uma regraIt A->alfa com alfa pertencente a Ne*
      
      /*
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
      */

      // feito o conjunto Ne
      // excluir de G todas as regras vazias
      for (Regra r : regrasVazia) {
         g.regras.remove(r);
      }

      List<Regra> outrasRegras = new ArrayList<Regra>();

      //Procura regras A->CB | A->BC onde B pertence a Ne e C nao (foi o q eu entendi)
      for(Regra regra : g.regras) { //Cria regras novas de acordo com o comentario acima e aiciona a novas regras.
    	  if (regra.direita.size() == 2) { //regra com 2 caracteres...
    		  if(set.contains(regra.direita.get(0)) || set.contains(regra.direita.get(1))) { //se um dos 2 esta no conjunto
    			  Regra nova = new Regra(regra.esquerda); //cria uma regra
    			  Simbolo<?> s = (!set.contains(regra.direita.get(0))) ? regra.direita.get(0):regra.direita.get(1);
    			  nova.addSimboloDireita(s); //adiciona o simbolo q nao esta no conjunto dos terminais, a direita da regra.

    			  outrasRegras.add(nova); //Marca para posterior adicao a gramatica.
    			  
    			  //DUVIDA NESSA PARTE, NAO ENTENDI O LIVRO: CASO: A->BC onde B e C estao em NE
    			  //DEVE SER ADICIONADO TANTO A->B quanto A->C, OU BASTA UM DOS DOIS?
    			  
    			  /*if (set.contains(regra.direita.get(0)) && set.contains(regra.direita.get(1))) {
    				  nova = new Regra(regra.esquerda);
    				  s = regra.direita.get(0);
    				  outrasRegras.add(nova);
    			  }
    			  */
    		  }
    	  }
      }
      for (Regra regra : outrasRegras) {
    	  g.addRegra(regra);
      }
      
      
      
      
      /*
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
      */
   }
   private static void eliminarRegrasCurtas(Gramatica g) {
	 //parte 3, montando conjuntos D{X} = { B: X->*B } pra todo X pertencente a V
      List<HashSet<Simbolo<?>>> conjuntosD = new ArrayList<HashSet<Simbolo<?>>>(); //Cria lista de conjuntos de simbolos.

      for(int i = 0; i < g.simbNaoTerminais.size(); i++) { //Para cada simbolo nao terminal da gramatica:
    	  //cria o grupo do nao terminal e adiciona na lista de conjuntos.
    	  conjuntosD.add(new HashSet<Simbolo<?>>());
    	  conjuntosD.get(i).add(g.simbNaoTerminais.get(i));
    	  int tamAntigo = conjuntosD.get(i).size();

    	  do { //Algoritmo de fechamento.
    		  tamAntigo = conjuntosD.get(i).size();
    		  for(Regra regra : g.regras) { //laco nas regras
    			  if (!conjuntosD.get(i).contains(regra.esquerda)) continue; //se o simbol oa esquerda nao estiver no conjunto, passa pula.
    			  conjuntosD.get(i).addAll(regra.direita); //adiciona tudo do lado direito, como eh um hashset, so adiciona os q ainda nao estao no conjunto.
    		  }
    	  }while(conjuntosD.get(i).size() - tamAntigo > 0); //para quando o conjunto nao aumentar de tamanho.
      }
   }
}
