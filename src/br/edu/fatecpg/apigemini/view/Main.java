package br.edu.fatecpg.apigemini.view;

import br.edu.fatecpg.apigemini.service.ConsomeApi;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("**** Olá! Bem-vindo ao Gemini! ****");

        Scanner scan = new Scanner(System.in);
        List<String> perguntas = new ArrayList<>();
        List<String> respostas = new ArrayList<>();

        while (true) {
            System.out.println("Faça sua pergunta (ou digite 'sair' para finalizar): ");
            String pergunta = scan.nextLine();

            if (pergunta.equalsIgnoreCase("sair")) {
                break;
            }

            perguntas.add(pergunta);
            try {
                String resposta = ConsomeApi.fazerPergunta(pergunta);
                respostas.add(resposta);
                System.out.println("Resposta recebida.");
            } catch (IOException e) {
                System.out.println("Erro ao acessar a API. Tente novamente mais tarde.");
                respostas.add("Erro ao acessar a API.");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("A operação foi interrompida.");
                respostas.add("Operação interrompida.");
            }

            System.out.println("***********");
            System.out.println("Deseja continuar perguntando? S/N:");
            String op = scan.nextLine();
            if (op.equalsIgnoreCase("n")) {
                break;
            }
        }


        System.out.println("Resumo das perguntas e respostas:");
        StringBuilder resumo = new StringBuilder();
        for (int i = 0; i < perguntas.size(); i++) {

            // Adiciona ao resumo
            resumo.append("Pergunta: ").append(perguntas.get(i)).append("\n");
            resumo.append("Resposta: ").append(respostas.get(i)).append("\n");
            resumo.append("***********\n");
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("resumo.txt"))) {
            writer.write(resumo.toString());
        } catch (IOException e) {
            System.out.println("Erro ao salvar o arquivo: " + e.getMessage());
        }

        // gerar uma string com o que foi lido no arquivo
        StringBuilder textoLido = new StringBuilder();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader("resumo.txt"))) {
            String linha;
            while ((linha = bufferedReader.readLine()) != null) {
                textoLido.append(linha).append("\n");
            }
        } catch (IOException e) {
            System.out.println("Erro ao ler o arquivo: " + e.getMessage());
        }

        String resposta_final = ConsomeApi.fazerResumo(textoLido.toString());

        // Salvar a resposta final em um arquivo
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("resumo_final.md"))) {
            writer.write(resposta_final);
            System.out.println("Resumo final salvo em 'resumo_final.txt'");
        } catch (IOException e) {
            System.out.println("Erro ao salvar o arquivo: " + e.getMessage());
        }

        System.out.println("Obrigado por usar o Gemini!");
        scan.close();
    }
}
