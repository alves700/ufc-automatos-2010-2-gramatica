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
      System.out.println("******* GRAMATICA APOS PASSO 1: *******");
      System.out.println(g.toString());
      eliminarRegraE(g);
      System.out.println("******* GRAMATICA APOS PASSO 2: *******");
      System.out.println(g.toString());
      
      eliminarRegrasCurtas(g);
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

    			  if(!outrasRegras.contains(nova))
    				  outrasRegras.add(nova); //Marca para posterior adicao a gramatica.
    			  
    			  //DUVIDA NESSA PARTE, NAO ENTENDI O LIVRO: CASO: A->BC onde B e C estao em NE
    			  //DEVE SER ADICIONADO TANTO A->B quanto A->C, OU BASTA UM DOS DOIS?
    			  if (set.contains(regra.direita.get(0)) && set.contains(regra.direita.get(1))) {
    				  nova = new Regra(regra.esquerda);
    				  s = regra.direita.get(0);
    				  nova.addSimboloDireita(s);
    				  if (!outrasRegras.contains(nova))
    					  outrasRegras.add(nova);
    			  }
    		  }
    	  }
      }
      for (Regra regra : outrasRegras) {
    	  g.addRegra(regra);
      }
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
    			 if(regra.direita.size() == 1) conjuntosD.get(i).addAll(regra.direita); //adiciona tudo do lado direito, como eh um hashset, so adiciona os q ainda nao estao no conjunto.
    		  }
    	  }while(conjuntosD.get(i).size() - tamAntigo > 0); //para quando o conjunto nao aumentar de tamanho.
      }
      System.out.println("LISTANDO CONJUNTOS:");
      for(int i = 0; i < conjuntosD.size(); i++)
    	  System.out.println(conjuntosD.get(i).toString());
      //Tirando as regras pequenas (tamanho == 1)
      List<Regra> temp = new ArrayList<Regra>();
      for(Regra r : g.regras) //marcando para remocao 
    	  if(r.direita.size() == 1) temp.add(r);
      for(Regra r : temp) //removendo regras marcadas da gramatica. 
    	  g.regras.remove(r);
      
      //ultima parte do passo 3....
      temp.clear();
      
      //A ignorancia abaixo foi feita por conta da forma de representacao dos simbolos terminais e n-terminais.
      //Doeu... mais passou.
      
      for(Regra r:g.regras) { //pra cada regra
    	  if(!r.direita.get(0).terminal) { //se o simbolo for um nao terminal
    		  Integer index = (Integer) r.direita.get(0).valor; //pega o valor pra ser usado como indice =xx gambss
    		  for(Simbolo<?> s : conjuntosD.get(index-1)) { //pra cada simbolo do seu conjunto D(S)
    			  if(!r.direita.get(1).terminal) { //se o segundo simbolo for um nao terminal
    				  Integer index2 = (Integer) r.direita.get(1).valor; //...
    				  for(Simbolo<?> s2 : conjuntosD.get(index2-1)) { //laco nos seu conjunto D(S)
    					  //cria regra e adiciona em temp.
    					  Regra nova = new Regra(r.esquerda);
    					  nova.addSimboloDireita(s);
    					  nova.addSimboloDireita(s2);
    					  if (!temp.contains(nova)) temp.add(nova);
    				  }
    			  }
    			  else { //se o simbolo 2 for um terminal:
					  Regra nova = new Regra(r.esquerda);
					  nova.addSimboloDireita(s);
					  nova.addSimboloDireita(r.direita.get(1));
					  if (!temp.contains(nova)) temp.add(nova);
    			  }
    		  }
    	  }
    	  else { //se o simbolo 1 for um terminal
   			  if(!r.direita.get(1).terminal) { //se o simbolo 2 nao for um terminal
 				  Integer index3 = (Integer) r.direita.get(1).valor;
				  //System.out.println(conjuntosD.get(index3).toString());
				  for(Simbolo<?> s3 : conjuntosD.get(index3-1)) {
					  Regra nova = new Regra(r.esquerda);
					  nova.addSimboloDireita(r.direita.get(0));
					  nova.addSimboloDireita(s3);
					  if (!temp.contains(nova))
						  temp.add(nova);
    	  }
      }
   			  else { //se simbolo 1 e simbolo 2 forem terminais...
   				Regra nova = new Regra(r.esquerda);
				  nova.addSimboloDireita(r.direita.get(0));
				  nova.addSimboloDireita(r.direita.get(1));
				  if (!temp.contains(nova))
					  temp.add(nova);
   			  }
    	  }
      }
      //adicionando regras
      for(Regra r : temp) if(!g.regras.contains(r)) g.addRegra(r);
   }
   
   public static boolean derivaPalavra(Gramatica g, ArrayList<Simbolo<?>> palavra) {
	   //HashSet<Simbolo<?>>[][] N = new HashSet<Simbolo<?>>[palavra.size()][palavra.size()];
	  // HashSet<Simbolo<?>>[][] M = new HashSet<Simbolo<?>>[10][10]();
	   //ArrayList<HashSet<Simbolo<?>>> N = new ArrayList<HashSet<Simbolo<?>>>();
	   ArrayList<ArrayList<HashSet<Simbolo<?>>>> N = new ArrayList<ArrayList<HashSet<Simbolo<?>>>>();
	   //HashSet<Simbolo<?>>[] l = new HashSet<Simbolo<?>>[10]();
	   //@SuppressWarnings("unchecked")
	   //HashSet<Simbolo<?>>[][] N = new HashSet[palavra.size()][palavra.size()];
	   for(int i = 0; i < palavra.size();i++) {
		   N.add(new ArrayList<HashSet<Simbolo<?>>>());
		   for(int j = 0; j < palavra.size(); j++) {
			   N.get(i).add(new HashSet<Simbolo<?>>());
		   }
	   }
	   
	   
	   for(int i = 0; i < palavra.size();i++){
		   N.get(i).get(i).add(palavra.get(i));
	   }

	   for(int s = 0; s < palavra.size();s++) {
		   for(int i = 0; i < palavra.size()-s;i++) {
			   for(int k = i; k < i+s;k++) {
				   for(Regra r : g.regras) {
					   if (N.get(i).get(k).contains(r.direita.get(0)) && N.get(k+1).get(i+s).contains(r.direita.get(1)))
						   N.get(i).get(i+s).add(r.esquerda);
				   }
			   }
		   }
	   }
	   
	   return N.get(0).get(palavra.size()-1).contains(g.simbInicial);
   }
}
