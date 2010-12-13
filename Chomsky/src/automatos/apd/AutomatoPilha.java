package automatos.apd;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AutomatoPilha {
	public List<String> simbNaoTerminais = new ArrayList<String>();
	public List<String> simbTerminais = new ArrayList<String>();
	public List<Estado> estados = new ArrayList<Estado>();
	public Estado estadoInicial;
	int profundidade;
	
	public void addEstados(Estado... estados) {
		for (Estado e : estados) {
			this.estados.add(e);
		}
	}
	
	public void addSimbNaoTerminais(String... simbNaoTerminais) {
		for (String s : simbNaoTerminais) {
			this.simbNaoTerminais.add(s);
		}
	}
	
	public void addSimbTerminais(String... simbTerminais) {
		for (String s : simbTerminais) {
			this.simbTerminais.add(s);
		}
	}
	
	public static AutomatoPilha carregarDeArquivo(String caminho) throws Exception {
		AutomatoPilha automato = new AutomatoPilha();
		BufferedReader reader = null;
		Pattern transitionPattern = Pattern.compile("\\(([^?]+)\\?([^?]*)\\?([^?]*)\\)\\(([^?]+)\\?([^?]*)\\)");
		
		try {
			reader = new BufferedReader(new FileReader(caminho));
			SecaoLeitura secaoLeitura = SecaoLeitura.SECAO_ESTADO_INICIAL;
			String linha = null;
			while ((linha = reader.readLine()) != null) {
				linha = linha.trim();
				SecaoLeitura proxSecaoLeitura = SecaoLeitura.getByIdentificador(linha);
				if (proxSecaoLeitura != null) {
					secaoLeitura = proxSecaoLeitura;
				} else {
					switch (secaoLeitura) {
					case SECAO_SIMBOLOS_NAO_TERMINAIS:
						automato.addSimbNaoTerminais(linha.split(" "));
						break;
					case SECAO_SIMBOLOS_TERMINAIS:
						automato.addSimbTerminais(linha.split(" "));
						break;
					case SECAO_ESTADO_INICIAL:
						Estado estadoInicial = new Estado(linha.trim(), false);
						automato.estadoInicial = estadoInicial;
						automato.estados.add(estadoInicial);
						break;
					case SECAO_ESTADOS_FINAIS:
						Estado estadoFinal = obterOuAdicionarEstado(automato, linha);
						estadoFinal.isFinal = true;
						break;
					case SECAO_TRANSICOES:
						if (linha.isEmpty() || linha.startsWith("//") || linha.indexOf("(") == -1) {
							continue;
						}
						linha = linha.substring(linha.indexOf("("));
						linha = linha.replaceAll("  ", " ").replaceAll("\\([ ]+", "(").replaceAll("[ ]*\\?[ ]*", "?").replaceAll("[ ]+\\)", ")");
						Matcher linhaMatcher = transitionPattern.matcher(linha);
						if (!linhaMatcher.matches()) {
							throw new Exception();
						}
						Estado estadoAnterior = obterOuAdicionarEstado(automato, linhaMatcher.group(1));
						String simbolo = linhaMatcher.group(2);
						simbolo = simbolo.trim().isEmpty() ? null : simbolo;
						String[] topoPilhaEsperado = linhaMatcher.group(3).split(" ");
						if (topoPilhaEsperado.length == 1 && topoPilhaEsperado[0].isEmpty()) {
							topoPilhaEsperado = new String[0];
						}
						Estado estadoDestino = obterOuAdicionarEstado(automato, linhaMatcher.group(4));
						String[] adicionarTopoPilha = linhaMatcher.group(5).split(" ");
						if (adicionarTopoPilha.length == 1 && adicionarTopoPilha[0].isEmpty()) {
							adicionarTopoPilha = new String[0];
						}
						estadoAnterior.addTransicao(new Transicao(simbolo, estadoDestino, topoPilhaEsperado, adicionarTopoPilha));
						break;
					}
				}
			}
		} finally {
			reader.close();
		}
		return automato;
	}
	
	private static Estado obterOuAdicionarEstado(AutomatoPilha automato, String label) {
		int indiceEstadoExistente = automato.estados.indexOf(new Estado(label, false));
		if (indiceEstadoExistente == -1) {
			Estado estado = new Estado(label, false);
			automato.addEstados(estado);
			return estado;
		} else {
			return automato.estados.get(indiceEstadoExistente);
		}
	}
	
	public boolean derivaString(String string) {
		String[] simbolosString = tokenizePorSimbTerminais(string.replaceAll(" ", ""));
		Stack<String> pilha = new Stack<String>();
		List<String> topoPilha = new ArrayList<String>();
		Estado estadoAtual = estadoInicial;
		int indiceSimb = 0;
		do {
			String simboloAtual = null;
			if (indiceSimb < simbolosString.length) {
				simboloAtual = simbolosString[indiceSimb];
			}
			if (simboloAtual == null && estadoAtual.isFinal) {
				return true;
			}
			//Realiza desempilhamentos ate encontrar uma transicao:
			Transicao transicao = null;
			do {
				String proxSimbolo = null;
				if (indiceSimb < simbolosString.length - 1) {
					proxSimbolo = simbolosString[indiceSimb + 1];
				}
				transicao = procurarTransicao(estadoAtual.transicoes, simboloAtual, topoPilha, proxSimbolo);
				if (transicao == null) {
					if (pilha.isEmpty()) {
						return false;
					} else {
						topoPilha.add(pilha.pop());
					}
				}
			} while (transicao == null);
			topoPilha.clear();
			
			for (int i = transicao.adicionarTopoPilha.size() - 1; i >=0; i--) {
				pilha.push(transicao.adicionarTopoPilha.get(i));
			}			
			estadoAtual = transicao.estadoDestino;
			if (transicao.simbolo != null) {
				indiceSimb++;
			}
		} while (true);
	}
	
	private String[] tokenizePorSimbTerminais(String string) {
		List<String> simbolos = new ArrayList<String>();
		StringBuilder simbAtual = new StringBuilder();
		for (int i = 0; i < string.length(); i++) {
			simbAtual.append(string.charAt(i));
			for (String simbTerminal : simbTerminais) {
				if (simbAtual.toString().matches(simbTerminal)) {
					if (i == string.length() - 1 ||	!(simbAtual.toString() + string.charAt(i+1)).matches(simbTerminal)) {
						simbolos.add(simbAtual.toString());
						simbAtual = new StringBuilder();
						break;
					}
				}
			}
		}
		return simbolos.toArray(new String[0]);
	}
	
	public Transicao procurarTransicao(List<Transicao> transicoes, String simbolo, List<String> topoPilha, String proxSimbolo) {
		//TODO FAZER O LOOOOOOOOOOKAHEAD(1) VERIFICANDO O PRIMEIRO NAO TERMINAL QUE A NOVA TRANSICAO ADICIONA NA PILHA!!!!!!
		Transicao transicaoEncontrada = null;
		int counter = 0; //adicao do counter pra poder ver se realmente ha uma ambiguidade.
		for (Transicao transicao : transicoes) {
			if (transicao.simbolo != null && !simbolo.matches(transicao.simbolo)) {
				continue;
			}
			if (transicao.topoPilhaEsperado.isEmpty() || transicao.topoPilhaEsperado.equals(topoPilha)) {
				transicaoEncontrada = transicao;
				counter++;
			}
		}
		if (counter > 1) { 
			Stack<String> pilha = new Stack<String>();
			for(String s: topoPilha) pilha.push(s);
			return Lookahead(transicoes,simbolo,pilha,proxSimbolo);
		}
		return transicaoEncontrada;
	}

	public Transicao Lookahead(List<Transicao> transicoes, String simbolo, Stack<String> pilha, String proxSimbolo) {
		Transicao r = null;
		profundidade = simbNaoTerminais.size();
		for (Transicao t : transicoes) {
			if (lookAheadRecursao(t, simbolo, pilha, proxSimbolo, 0)) {
				r = t;
				break;
			} // manda desse jeito pra dar erro no primero teste e comecar a busca...
		}
		return r;
	}

	public boolean lookAheadRecursao(Transicao t, String simbolo, Stack<String> pilha, String proxSimbolo, int prof) {
		boolean axou = false;
		Stack<String> p = new Stack<String>();
		if ((t.simbolo != null && simbolo.matches(t.simbolo)) && t.topoPilhaEsperado.equals(pilha.peek()) && prof > 0)
			return true;
		if (prof > profundidade)
			return false;
		for (Transicao tt : t.estadoDestino.transicoes) { // para cada transicao do estado alvo...
			if (tt.simbolo != null && !proxSimbolo.matches(tt.simbolo))
				continue; // se o simbolo da transacao nao for o proximoSimbolo nao chama recursao.
			if (!tt.topoPilhaEsperado.equals(pilha.peek()))
				continue; // se a pilha nao for o esperado, elimina transicao.
			p = pilha;
			p.pop();
			for (int i = t.adicionarTopoPilha.size(); i > 0; --i)
				p.push(t.adicionarTopoPilha.get(i));

			if (lookAheadRecursao(tt, proxSimbolo, p, proxSimbolo, prof + 1)) {
				axou = true;
				break;
			} // se encontro um para o loop q chama a recursao.
		}
		return axou;
	}
	
	@Override
	public String toString() {
		String strEstados = "Estados: ";
		String strEstadoInicial = "Estado inicial: " + estadoInicial.label + "\n";
		String strEstadosFinais = "Estados finais: ";
		
		Collections.sort(estados);
		Iterator<Estado> estadosIt = estados.iterator();
		while (estadosIt.hasNext()) {
			Estado estado = (Estado) estadosIt.next();
			if (estado.isFinal) {
				strEstadosFinais += estado.label + " ";
			} else {
				strEstados += estado.label + " ";
			}
		}
		strEstados += "\n";
		strEstadosFinais += "\n";
		
		String strTransicoes = "Transicoes:\n";
		estadosIt = estados.iterator();
		while (estadosIt.hasNext()) {
			Estado estado = (Estado) estadosIt.next();
			for (Transicao transicao : estado.transicoes) {
				strTransicoes += "\t" + transicao.toString(estado) + "\n";
			}
		}
		return strEstados + strEstadoInicial + strEstadosFinais + strTransicoes;
	}

	private static enum SecaoLeitura {
		SECAO_SIMBOLOS_NAO_TERMINAIS("#simbolos_nao_terminais"),
		SECAO_SIMBOLOS_TERMINAIS("#simbolos_terminais"),
		SECAO_ESTADO_INICIAL("#estado_inicial"),
		SECAO_ESTADOS_FINAIS("#estados_finais"),
		SECAO_TRANSICOES("#transicoes");
		
		public final String identificador;
		SecaoLeitura(String identificador) {
			this.identificador = identificador;
		}
		
		public static SecaoLeitura getByIdentificador(String identificador) {
			for (SecaoLeitura estado : values()) {
				if (identificador.equals(estado.identificador)) {
					return estado;
				}
			}
			return null;
		}
	}
}
