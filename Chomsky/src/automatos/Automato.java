package automatos;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


public class Automato {
	public Gramatica g;
	public Automato(Gramatica g) {
		this.g = g;
	}
	private void Heuristica1() {
		ArrayList<ArrayList<Regra>> regrasNTerminais = new ArrayList<ArrayList<Regra>>();
		List<Regra> regrasTemp = g.regras;
		int tamanhoAntigo = 0;
		ArrayList<Regra> regrasParaMudar;
		
		do { //Separando o joio do trigo!
			regrasParaMudar = new ArrayList<Regra>();
			tamanhoAntigo = regrasTemp.size();
			for(int j = 1; j < regrasTemp.size(); j++) {
				if (regrasTemp.get(j).esquerda != regrasTemp.get(0).esquerda) continue;
				if (regrasTemp.get(j).direita.get(0) != regrasTemp.get(0).direita.get(0)) continue;
				regrasParaMudar.add(regrasTemp.get(j));
			}
			if (regrasParaMudar.size() > 0) {
				regrasParaMudar.add(regrasTemp.get(0)); //adiciona a primera
				for(Regra r : regrasParaMudar) regrasTemp.remove(r); //Remove regras ja encontradas
				regrasNTerminais.add(regrasParaMudar); //adiciona para aplicar heuristica.
			}
			regrasTemp.remove(0);
		}while(tamanhoAntigo - regrasTemp.size() > 0);
		
		
		//Quebrando as regras
		for(int i = 0; i < regrasNTerminais.size();i++) { //para cada conjunto de regras q precisa mudar:
			Regra nova = new Regra(regrasNTerminais.get(i).get(0).esquerda);// A->aA'
			nova.addSimboloDireita(regrasNTerminais.get(i).get(0).direita.get(0)); //A->a
			Simbolo<Integer> novoSimbolo = g.gerarProximoSimboloNaoTerminal();//Cria A'
			g.addSimbNaoTerminal(novoSimbolo); //adiciona novo simbolo A' a gramatica.
			nova.addSimboloDireita(novoSimbolo);//A->aA'
			
			g.addRegra(nova);
			
			for(int j = 0; j < regrasNTerminais.get(i).size();j++) {
				nova = new Regra(novoSimbolo); //A' ->
				if (regrasNTerminais.get(i).get(j).direita.size() < 2) { nova.addSimboloDireita(new Simbolo<String>("e",true)); continue; } //A'-> e, se for o caso.
				for(int k = 1; k < regrasNTerminais.get(i).get(j).direita.size(); k++) //Construindo: A' -> Bi 
					nova.addSimboloDireita(regrasNTerminais.get(i).get(j).direita.get(k));
				g.addRegra(nova); //A' -> Bi
			}
		}
		
		//Removendo as regras antigas.
		for(int i = 0; i < regrasNTerminais.size(); i++)
			for(int j = 0; j < regrasNTerminais.size();j++)
				g.regras.remove(regrasNTerminais.get(i).get(j));
		
		
	}
}
