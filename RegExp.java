import java.util.*;
import java.io.*;

/*
    OBSERVAÇÕES
        coringa (.): aceita QUALQUER coisa.
        conjunto ([ ]): deve conter apenas letras e números, e deve ser conjunto não-vazio.
        intervalo ([ - ]): deve conter apenas letras e números.
        complemento ([^...]): caracteres (letras e números em alfabeto[]) que não estão no conjunto.
*/

public class RegExp {

    static ArrayList<Boolean> eh_caracter;
    static char[] alfabeto = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
                        'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
                        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};


    public static String alteraExpReg(String exp_reg) {
        eh_caracter = new ArrayList<Boolean>();
        StringBuilder nova_exp = new StringBuilder();
        for (int i = 0; i < exp_reg.length(); i++) {
            char caracter = exp_reg.charAt(i);
            if (caracter == '\\') { // caracter = '\'
                eh_caracter.add(false);
                eh_caracter.add(true); // o próximo é, necessariamente, um caracter
                nova_exp.append(exp_reg.charAt(i));
                nova_exp.append(exp_reg.charAt(i + 1));
                i += 1;
            }
            else if (caracter == '[' && exp_reg.charAt(i + 2) == '-') { // intervalo
                int ascii_1 = exp_reg.charAt(i + 1); // pegar ASCII do primeiro elemento
                int ascii_2 = exp_reg.charAt(i + 3); // pegar ASCII do segundo elemento
                if (ascii_2 >= ascii_1) {
                    StringBuilder parcial = new StringBuilder();
                    ArrayList<Boolean> caracteres_1 = new ArrayList<Boolean>();
                    ArrayList<Boolean> caracteres_2 = new ArrayList<Boolean>();

                    parcial.append(exp_reg.charAt(i + 1)); // adiciona o primeiro elemento
                    caracteres_2.add(true);
                    int ascii = ascii_1 + 1; // ascii do elemento seguinte ao primeiro
                    while (ascii <= ascii_2) {
                        parcial.append('|');
                        caracteres_2.add(false);
                        parcial.append(Character.toString ((char) ascii)); // converte ASCII para character
                        caracteres_2.add(true);
                        parcial.append(')');
                        caracteres_2.add(false);
                        parcial.insert(0, '(');
                        caracteres_1.add(false);
                        ascii++;
                    }
                    eh_caracter.addAll(caracteres_1);
                    eh_caracter.addAll(caracteres_2);
                    nova_exp.append(parcial);
                    i += 4; // posição do ']'
                }
            }
            else if (caracter == '[' && exp_reg.charAt(i + 1) == '^') { // complemento
                StringBuilder parcial = new StringBuilder();
                ArrayList<Boolean> caracteres_1 = new ArrayList<Boolean>();
                ArrayList<Boolean> caracteres_2 = new ArrayList<Boolean>();
                int contador = 2;
                boolean primeiro = true;
                for (int j = 0; j < alfabeto.length; j++) { // iterando sobre o alfabeto
                    char letra_alfabeto = alfabeto[j];
                    caracter = exp_reg.charAt(i + 2);
                    contador = 2;
                    boolean encontrou = false;
                    while (caracter != ']') { // iterando sobre os caracteres após '^'
                        if (letra_alfabeto == caracter) {
                            encontrou = true;
                            break;
                        }
                        contador++;
                        caracter = exp_reg.charAt(i + contador);
                    }
                    if (encontrou) // não deve ser adicionado na expressão regular
                        continue;
                    else { // deve ser adicionado na expressão regular
                        if (primeiro) {
                            parcial.append(letra_alfabeto);
                            caracteres_2.add(true);
                            primeiro = false;
                        }
                        else {
                            parcial.append('|');
                            caracteres_2.add(false);
                            parcial.append(letra_alfabeto);
                            caracteres_2.add(true);
                            parcial.append(')');
                            caracteres_2.add(false);
                            parcial.insert(0, '(');
                            caracteres_1.add(false);
                        }
                    }
                }
                eh_caracter.addAll(caracteres_1);
                eh_caracter.addAll(caracteres_2);
                nova_exp.append(parcial);
                while (exp_reg.charAt(i) != ']')
                    i += 1;
            }
            else if (caracter == '[') { // conjunto
                StringBuilder parcial = new StringBuilder();
                ArrayList<Boolean> caracteres_1 = new ArrayList<Boolean>();
                ArrayList<Boolean> caracteres_2 = new ArrayList<Boolean>();
                parcial.append(exp_reg.charAt(i + 1));
                caracteres_2.add(true);
                caracter = exp_reg.charAt(i + 2);
                int contador = 2;
                while (caracter != ']') {
                    parcial.append('|');
                    caracteres_2.add(false);
                    parcial.append(caracter);
                    caracteres_2.add(true);
                    parcial.append(')');
                    caracteres_2.add(false);
                    parcial.insert(0, '(');
                    caracteres_1.add(false);
                    contador++;
                    caracter = exp_reg.charAt(i + contador);
                }
                eh_caracter.addAll(caracteres_1);
                eh_caracter.addAll(caracteres_2);
                nova_exp.append(parcial);
                i += contador; // posição do ']'
            }
            else if (caracter == '(' || caracter == ')' || caracter == '|' || caracter == '*' || caracter == '.' || caracter == '+') {
                nova_exp.append(exp_reg.charAt(i));
                eh_caracter.add(false);
            }
            else { // caracteres comuns
                nova_exp.append(exp_reg.charAt(i));
                eh_caracter.add(true);
            }
        }
        return nova_exp.toString();
    }

    public static boolean reconhece(Grafo G, char[] letras, String palavra) {
        int V = G.V;
        boolean[] atingidos = new boolean[V];
        G.dfsR(0, atingidos);
        for (int i = 0; i < palavra.length(); i++) { // percorre as letras da palavra
            boolean[] prox = new boolean[V];
            for (int j = 0; j < V; j++)
                if (atingidos[j] && (letras[j] == palavra.charAt(i) || letras[j] == Character.toString((char) 7).charAt(0))) // segundo caso é o coringa
                    prox[j + 1] = true;
            boolean[] marked = new boolean[V];
            for (int j = 0; j < V; j++)
                atingidos[j] = false;
            for (int j = 0; j < V; j++) {
                if (prox[j]) {
                    for (int k = 0; k < V; k++)
                        marked[k] = false;
                    G.dfsR(j, marked);
                    for (int k = 0; k < V; k++)
                        if (marked[k])
                            atingidos[k] = true;
                }
            }
        }
        return atingidos[V - 1];
    }

    public static Grafo constroiGrafo(String exp_reg, char[] letras) {
        int tamanho = exp_reg.length() + 1; // considerando "ponto" ao final, a ser adicionado no grafo
        Grafo G = new Grafo(tamanho);
        Pilha<Integer> pilha = new Pilha<Integer>();

        for (int i = 0; i < exp_reg.length(); i++) {
            int ant = i;
            if (exp_reg.charAt(i) == '\\') { // '\'
                G.incluiArco(i, i + 1);
            }
            else if ((i == 0 || (i > 0 && exp_reg.charAt(i - 1) != '\\')) && (exp_reg.charAt(i) == '(' || exp_reg.charAt(i) == '|')) { // '(' ou '|'
                pilha.push(i);
            }
            else { // caractere, ou ')', ou '*', ou '+', ou '.'
                if (eh_caracter.get(i)) { // caractere
                    letras[i] = exp_reg.charAt(i);
                }
                else if (exp_reg.charAt(i) == ')') {
                    int topo = pilha.pop();
                    if (exp_reg.charAt(topo) == '|') {
                        ant = pilha.pop();
                        G.incluiArco(ant, topo + 1);
                        G.incluiArco(topo, i);
                    }
                    else { // era ‘(‘
                        ant = topo;
                    }
                }
                else if (exp_reg.charAt(i) == '.')
                    letras[i] = Character.toString((char) 7).charAt(0); // VAI DAR PAU AQUI
                if (i + 1 < exp_reg.length() && exp_reg.charAt(i + 1) == '*') {
                    G.incluiArco(ant, i + 1);
                    G.incluiArco(i + 1, ant);
                }
                if (i + 1 < exp_reg.length() && exp_reg.charAt(i + 1) == '+')
                    G.incluiArco(i + 1, ant);
            }
            if (i + 1 <= exp_reg.length() && (exp_reg.charAt(i) == '(' || exp_reg.charAt(i) == '*' || exp_reg.charAt(i) == ')' || exp_reg.charAt(i) == '+'))
                G.incluiArco(i, i + 1);
        }
        return G;
    }


    public static void main (String[] args) throws IOException {
        System.out.print("Qual o arquivo de input? ");
        Scanner input = new Scanner(System.in);
        String endereco = input.nextLine();
        input.close();

        input = new Scanner(new File(endereco));
        String exp_reg = input.nextLine();
        exp_reg = alteraExpReg(exp_reg);
        char[] letras = new char[exp_reg.length() + 1]; // + 1 para o ponto final
        for (int i = 0; i <= exp_reg.length(); i++)
            letras[i] = '¢';
        Grafo G = constroiGrafo(exp_reg, letras);

        int n_palavras = input.nextInt();
        input.nextLine();

        for (int i = 0; i < n_palavras; i++) {
            String palavra = input.nextLine();
            boolean reconheceu = reconhece(G, letras, palavra);
            if (reconheceu)
                System.out.println("S");
            else
                System.out.println("N");
        }

        input.close();
    }
}