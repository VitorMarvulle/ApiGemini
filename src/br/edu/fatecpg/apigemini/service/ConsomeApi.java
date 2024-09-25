package br.edu.fatecpg.apigemini.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConsomeApi {
    private static final String API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash-latest:generateContent?key=AIzaSyDRDGCqCW_3zurR2NP5-wkJEkhwTqJwlbY";
    private static final Pattern RESP_PATTERN = Pattern.compile("\"text\"\\s*:\\s*\"([^\"]+)\"");

    //fazerPergunta()
    public static String fazerPergunta(String pergunta) throws IOException, InterruptedException {
        String jsonRequest = gerarJsonRequest(pergunta);
        String respostaJson = enviarRequisicao(jsonRequest);
        return extrairResposta(respostaJson);
    }

    public static String fazerResumo(String resumo) throws IOException, InterruptedException {
        String jsonRequest = gerarResumo(resumo);
        String respostaJson = enviarRequisicao(jsonRequest);
        return extrairResposta(respostaJson);
    }


    //gerarJsonRequest()
    private static String gerarJsonRequest(String pergunta) {
        String promptFormatado = "O resultado gerado não deve possuir formatacção ou caracteres especiais. Pergunta: " + pergunta;
        return "{\"contents\":[{\"parts\":[{\"text\":\""+ promptFormatado+"\"}]}]}";
    }

    //gerarResumo
    public static String gerarResumo(String resumo) {
        String promptFormatadoResumo = "Realizar um resumo conciso e pequeno sobre as últimas perguntas e respostas " + resumo;
        return "{\"contents\":[{\"parts\":[{\"text\":\""+ promptFormatadoResumo+"\"}]}]}";
    }

    //enviarRequisicao()

    private static String enviarRequisicao(String jsonRequest) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonRequest))
                .build();
        return client.send(request, HttpResponse.BodyHandlers.ofString()).body();
    }

    //extrairResposta()
    private static String extrairResposta(String respostaJson) {
        StringBuilder resposta = new StringBuilder();
        for(String linha: respostaJson.lines().toList()){
            Matcher matcher = RESP_PATTERN.matcher(linha);
            if(matcher.find()){
                resposta.append(matcher.group(1)).append(" ");
            }
        }
        return resposta.toString().trim();
    }
}
