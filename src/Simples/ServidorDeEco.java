/*
 O exemplo abaixo está no livro: “Aprendendo Java 2”
 Mello, Chiara e Villela Novatec Editora Ltda. – www.novateceditora.com.br
 */
package Simples;

import java.io.*;
import java.net.*;

public class ServidorDeEco {
    public static void main(String args[]) {
        try {
            // criando um socket que fica escutando a porta 2000.
            ServerSocket s = new ServerSocket(2000); //  1º criamos um servidor socket na porta 2000
            // loop principal.
            while (true) {  // 2º irá executar ate alguem se conectar com o servidor
                // Aguarda alguém se conectar. A execução do servidor
                // fica bloqueada na chamada do método accept da classe
                // ServerSocket. Quando alguém se conectar ao servidor, o
                // método desbloqueia e retorna com um objeto da classe
                // Socket, que é uma porta da comunicação.
                System.out.print("Esperando alguém se conectar...");
                Socket conexao = s.accept(); // 3º conexão aceita acontece no momento que o srvidor cria aquela porta secundaria que sera direcionado o cliente para liberar sua porta principal - a conexão está establecida
                System.out.println(" Conectou!");
                // obtendo os objetos de controle do fluxo de comunicação
                BufferedReader entrada = new BufferedReader(new InputStreamReader(conexao.getInputStream())); // criação do objeto para poder ler uma entrada
                PrintStream saida = new PrintStream(conexao.getOutputStream()); // criação do objeto para mostrar uma saída
                // esperando por alguma string do cliente até que ele
                // envie uma linha em branco.
                // Verificar se linha recebida não é nula.
                // Isso ocorre quando conexão é interrompida pelo cliente
                // Se a linha não for null(o objeto existe), podemos usar
                // métodos de comparação de string(caso contrário,estaria
                // tentando chamar um método de um objeto que não existe)
                String linha = entrada.readLine(); // le textos, entao le as informações que estao chegando (nesse caso esta tratando como string, entao n sei se é int, double, ...)
                while (linha != null && !(linha.trim().equals(""))) { // mando de volta para todas as comunicações (clientes) que existem com esse servidor - manda de volta o proprio dado (mando um broadcast para todo mundo do que eu recebi). ex: um cliente mandou um ola, eu dou um broadcast com ola para todos (mas como nao estou usando thread, apenas o primeiro cliente que conseguiu se conectar é o que irá receber)
                    // envia a linha de volta.
                    saida.println("Eco: " + linha);
                    // espera por uma nova linha.
                    linha = entrada.readLine();
                }
                // se o cliente enviou linha em branco, fecha-se conexão.
                conexao.close();
                // e volta-se ao loop, esperando mais alguém se conectar
            }
        } catch (IOException e) {
            // caso ocorra alguma excessão de E/S, mostre qual foi
            System.out.println("IOException: " + e);
        }
    }
}
