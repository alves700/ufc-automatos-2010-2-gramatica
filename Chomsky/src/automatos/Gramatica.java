package automatos;

import java.util.ArrayList;
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
		Simbolo<Integer> simbolo = new Simbolo<Integer>(++maior, false);
		return simbolo;
	}
	
	@Override
	public String toString() {
		String str = "G = (N, E, R, S)\n";
		
		str += "N = {";
		for (int i = 0; i < simbNaoTerminais.size(); i++) {
			Simbolo<Integer> simbNaoTerminal = simbNaoTerminais.get(i);
			str += simbNaoTerminal;
			if (i < simbNaoTerminais.size() - 1) {
				str += ", ";
			}
		}
		str += "}\n";
		
		str += "E = {";
		for (int i = 0; i < alfabeto.size(); i++) {
			Simbolo<String> simbTerminal = alfabeto.get(i);
			str += simbTerminal;
			if (i < alfabeto.size() - 1) {
				str += ", ";
			}
		}
		str += "}\n";
		
		str += "R = {\n"; 
		for (int i = 0; i < regras.size(); i++) {
			Regra regra = regras.get(i);
			str += "\t" + regra;
			if (i < regras.size() - 1) {
				str += ", ";
			}
			str += "\n";
		}
		str += "}\n";
		
		str += "S = " + simbInicial;
		return str;
	}
}
