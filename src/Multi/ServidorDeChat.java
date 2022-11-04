/*
 O exemplo abaixo está no livro: “Aprendendo Java 2”
 Mello, Chiara e Villela Novatec Editora Ltda. – www.novateceditora.com.br
Tipo de comunicação entre todos os clientes. Um Cliente envia a mensagem e os demais (todos)
recebem.
 */
package Multi;

import java.io.*;
import java.net.*;
import java.util.*;

public class ServidorDeChat extends Thread {

    // Parte que controla as conexões por meio de threads.
    // Note que a instanciação está no main.
    private static List<Cliente> clientes;

    private Cliente cliente;

    // socket deste cliente
    private Socket conexao;

    // nome deste cliente
    private String nomeCliente;
    
    // construtor que recebe o socket deste cliente
    public ServidorDeChat(Cliente c) {
        cliente = c;
    }

    // execução da thread
    public void run() {
        try {
            // objetos que permitem controlar fluxo de comunicação
            BufferedReader entrada = new BufferedReader(new InputStreamReader(cliente.getSocket().getInputStream()));
            PrintStream saida = new PrintStream(cliente.getSocket().getOutputStream());
            cliente.setSaida(saida);
            
            nomeCliente = entrada.readLine();
            // agora, verifica se string recebida é valida, pois
            // sem a conexão foi interrompida, a string é null.
            // Se isso ocorrer, deve-se terminar a execução.
            if (nomeCliente == null) {
                return;
            }
            cliente.setNome(nomeCliente);

            // clientes é objeto compartilhado por várias threads!
            // De acordo com o manual da API, os métodos são
            // sincronizados. Portanto, não há problemas de acessos
            // simultâneos.
            // Loop principal: esperando por alguma string do cliente.
            // Quando recebe, envia a todos os conectados até que o
            // cliente envie linha em branco.
            // Verificar se linha é null (conexão interrompida)
            // Se não for nula, pode-se compará-la com métodos string
            String linha = entrada.readLine();
            while (linha != null && !(linha.trim().equals(""))) {
                // reenvia a linha para todos os clientes conectados
                sendToAll(saida, " disse: ", linha);
                // espera por uma nova linha.
                linha = entrada.readLine();
            }
            // Uma vez que o cliente enviou linha em branco, retira-se
            // fluxo de saída do vetor de clientes e fecha-se conexão.
            sendToAll(saida, " saiu ", "do chat!");
            clientes.remove(saida);
            conexao.close();
        } catch (IOException e) {
            // Caso ocorra alguma excessão de E/S, mostre qual foi.
            System.out.println("IOException: " + e);
        }
    }

    // enviar uma mensagem para todos, menos para o próprio
    public void sendToAll(PrintStream saida, String acao,
            String linha) throws IOException {

        Iterator<Cliente> iter = clientes.iterator();
        while (iter.hasNext()) {
            Cliente outroCliente = iter.next();
            // obtém o fluxo de saída de um dos clientes
            PrintStream chat = (PrintStream) outroCliente.getSaida(); // toda vez que o cliente se conecta, é criado um fluxo de entrada e saida
            // envia para todos, menos para o próprio usuário
            if (chat != saida) {
                chat.println(cliente.getNome() + " com IP: " + cliente.getSocket().getRemoteSocketAddress() + acao + linha); // aqui é feita automaticamente a conversão de bites para string e conseguir mostrar os dados
            }
        }
    }

    public static void main(String args[]) {
        // instancia o vetor de clientes conectados
        clientes = new ArrayList<Cliente>();
        try {
            // criando um socket que fica escutando a porta 2222.
            ServerSocket s = new ServerSocket(2222);  // crio um servidor socket
            // Loop principal.
            while (true) {  // aguardo alguem se comunicar
                // aguarda algum cliente se conectar. A execução do
                // servidor fica bloqueada na chamada do método accept da
                // classe ServerSocket. Quando algum cliente se conectar
                // ao servidor, o método desbloqueia e retorna com um
                // objeto da classe Socket, que é porta da comunicação.
                System.out.print("Esperando alguem se conectar...");
                Socket conexao = s.accept();
                Cliente cliente = new Cliente(); // quando alguem se conecta, crio um objeto para ele, e seto para esse cliente um Id, Ip e o Socket (comunicação com auqele clietne) e depois adiicono em uma lista:
                cliente.setId(conexao.getRemoteSocketAddress().toString());
                cliente.setIp(conexao.getRemoteSocketAddress().toString());
                cliente.setSocket(conexao);

                clientes.add(cliente);

                System.out.println(" Conectou!: " + conexao.getRemoteSocketAddress());

                // cria uma nova thread para tratar essa conexão
                Thread t = new ServidorDeChat(cliente);  // rio uma porta secundaria para essecliente, assim liberando a porta principal
                t.start();
                // voltando ao loop, esperando mais alguém se conectar.
            }
        } catch (IOException e) {
            // caso ocorra alguma excessão de E/S, mostre qual foi.
            System.out.println("IOException: " + e);
        }
    }
}
