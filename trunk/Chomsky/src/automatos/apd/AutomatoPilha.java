package automatos.apd;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class AutomatoPilha {
	public List<Estado> estados = new ArrayList<Estado>();
	public Estado estadoInicial;
	
	public void addEstado(Estado... estado) {
		for (Estado e : estado) {
			estados.add(e);
		}
	}
	
	public static AutomatoPilha carregarDeArquivo(String caminho) {
		AutomatoPilha automato = new AutomatoPilha();
		return automato;
	}
	
	public boolean derivaString(String string) {
		Stack<String> pilha = new Stack<String>();
		List<String> topoPilha = new ArrayList<String>();
		Estado estadoAtual = estadoInicial;
		int posicaoStr = 0;
		do {
			String simboloAtual = null;
			if (posicaoStr < string.length()) {
				simboloAtual = Character.toString(string.charAt(posicaoStr));
			}
			Transicao transicao = procurarTransicao(estadoAtual.transicoes, simboloAtual, topoPilha);
			if (transicao == null) {
				topoPilha.add(pilha.pop());
				continue;
			} else {
				topoPilha.clear();
				for (int i = transicao.adicionarTopoPilha.size() - 1; i >=0; i--) {
					pilha.push(transicao.adicionarTopoPilha.get(i));
				}
				if (simboloAtual.equals(transicao.simbolo)) {
					posicaoStr++;
				}
				estadoAtual = transicao.estadoDestino;
			}
			if (posicaoStr == string.length() && estadoAtual.isFinal) {
				return true;
			}
		} while (true);
	}
	
	public Transicao procurarTransicao(List<Transicao> transicoes, String simbolo, List<String> topoPilha) {
		//TODO RETIRAR VERIFICACAO DE UMBIGUIDADE!!!
		Transicao transicaoEncontrada = null;
		for (Transicao transicao : transicoes) {
			if (transicao.simbolo != null && !transicao.simbolo.equals(simbolo)) {
				continue;
			}
			if (transicao.topoPilhaEsperado.isEmpty() || transicao.topoPilhaEsperado.equals(topoPilha)) {
				if (transicaoEncontrada != null) {
					throw new RuntimeException("Ambiguidade!");
				}
				transicaoEncontrada = transicao;
			}
		}
		return transicaoEncontrada;
	}
}
