package automatos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class Gramatica {

	public List<Simbolo<Integer>> simbNaoTerminais = new ArrayList<Simbolo<Integer>>();
	public List<Simbolo<String>> alfabeto = new ArrayList<Simbolo<String>>();
	public List<Regra> regras = new ArrayList<Regra>();
	public Simbolo<Integer> simbInicial;

	public Simbolo<String> addSimboloAlfabeto(String valor) {
		Simbolo<String> simbolo = new Simbolo<String>(valor, true);
		alfabeto.add(simbolo);
		return simbolo;
	}
	
	public Simbolo<Integer> addSimbNaoTerminal(Integer valor){
		Simbolo<Integer> simbolo = new Simbolo<Integer>(valor, false);
		return addSimbNaoTerminal(simbolo);	
	}
	
	public Simbolo<Integer> addSimbNaoTerminal(Simbolo<Integer> simbolo){
		simbNaoTerminais.add(simbolo);
		return simbolo;
	}
	
	public void addRegra(Regra... regras) {
		for (Regra regra : regras) {
			this.regras.add(regra);
		}
	}
	
	public Simbolo<Integer> gerarProximoSimboloNaoTerminal() {
		Integer maior;
		maior = 0;
		if (!simbNaoTerminais.isEmpty()) {
			for (Simbolo<Integer> simbolo : simbNaoTerminais) {
				if (simbolo.valor > maior) {
					maior = simbolo.valor;
				}
			}
		}
		Simbolo<Integer> simbolo = new Simbolo<Integer>(++maior, false);
		return simbolo;
	}
	
	@Override
	public String toString() {
		Collections.sort(simbNaoTerminais);
		Collections.sort(alfabeto);
		Collections.sort(regras);
		
		String str = "Gramática G = (N, E, R, S). N: Simb. Não terminais; E: Alfabeto; R: Regras; S: Simb. Inicial\n";
		
		//Simbolos não terminais:
		str += "N = {";
		Iterator<Simbolo<Integer>> simbNaoTerminaisIt = simbNaoTerminais.iterator();
		while (simbNaoTerminaisIt.hasNext()) {
			Simbolo<Integer> simbNaoTerminal  = (Simbolo<java.lang.Integer>) simbNaoTerminaisIt.next();
			str += simbNaoTerminal;
			if (simbNaoTerminaisIt.hasNext()) {
				str += ", ";
			}
		}
		str += "}\n";
		
		//Simbolo inicial:
		str += "S = " + simbInicial + "\n";
		
		//Alfabeto:
		str += "E = {";
		Iterator<Simbolo<String>> alfabetoIt = alfabeto.iterator();
		while (alfabetoIt.hasNext()) {
			Simbolo<String> simbTerminal = alfabetoIt.next();
			str += simbTerminal;
			if (alfabetoIt.hasNext()) {
				str += ", ";
			}
		}
		str += "}\n";
		
		//Regras
		str += "R = {\n"; 
		Iterator<Regra> regrasIt = regras.iterator();
		while (regrasIt.hasNext()) {
			Regra regra = regrasIt.next();
			str += "\t" + regra;
//			if (regrasIt.hasNext()) {
//				str += ", ";
//			}
			str += "\n";
		}
		str += "}";
		
		return str;
	}
}
