package br.com.alura.screenmatchnovo.services;

import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.service.OpenAiService;

public class ConsultaChatGPT {
    public static String obterTraducao(String texto) {
        OpenAiService service = new OpenAiService(System.getenv("OPENAI_APIKEY"));

        CompletionRequest requisicao = CompletionRequest.builder()
                .model("gpt-3.5-turbo-instruct") //modelo de compatibilidade de dependência que a gente utiliza
                .prompt("traduza para o português o texto: " + texto)//onde fazemos a pergunta
                .maxTokens(1000)//quanto mais tokens mais resposta será a resposta dele
                .temperature(0.7)//modificação que vai ter de uma solicitação a outra, se deixar nulo ele sempre trará a mesma resposta
                .build();

        var resposta = service.createCompletion(requisicao);
        return resposta.getChoices().get(0).getText();
    }

}
