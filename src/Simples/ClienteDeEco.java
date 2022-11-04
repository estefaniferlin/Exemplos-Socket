/*
 O exemplo abaixo está no livro: “Aprendendo Java 2”
 Mello, Chiara e Villela Novatec Editora Ltda. – www.novateceditora.com.br
 */
package simples;

import java.io.*;
import java.net.*;

public class ClienteDeEco {

    public static void main(String args[]) {
        try {
            // para se conectar ao servidor, cria-se objeto Socket.
            // O primeiro parâmetro é o IP ou endereço da máquina que
            // se quer conectar e o segundo é a porta da aplicação.
            // Neste caso, usa-se o IP da máquina local (127.0.0.1)
            // e a porta da aplicação ServidorDeEco (2000).
            Socket conexao = new Socket("127.0.0.1", 2000);  // 1º defino meu ip e porta para estabelecer comunicao com o servidor
            // uma vez estabelecida a comunicação, deve-se obter os
            // objetos que permitem controlar o fluxo de comunicação
            BufferedReader entrada = new BufferedReader(new InputStreamReader(conexao.getInputStream())); // crio um objeto que irá receber os valores
            PrintStream saida = new PrintStream(conexao.getOutputStream()); // crio um objeto para sair os valores
            String linha;
            // objetos que permitem a leitura do teclado
            BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in)); // no caso é informação do teclado do usuario
            // loop principal
            while (true) {
                // lê a linha do teclado
                System.out.print("> ");
                linha = teclado.readLine();
                // envia para o servidor
                saida.println(linha);  // quando eu mando, aguardo alguem mandar algo la no readLine
                // pega o que o servidor enviou
                linha = entrada.readLine();
                // Verifica se é linha válida, pois se for null a conexão
                // foi interrompida. Se ocorrer isso, termina a execução.
                if (linha == null) { // se nao recebe nada, encerra a conexao
                    System.out.println("Conexão encerrada!");
                    break;
                }
                // se a linha não for nula, deve-se imprimi-la no vídeo
                System.out.println(linha); // caso contrario, ele imprime a informação
            }
        } catch (IOException e) {
            // caso ocorra alguma excessão de E/S, mostre qual foi.
            System.out.println("IOException: " + e);
        }
    }
}
