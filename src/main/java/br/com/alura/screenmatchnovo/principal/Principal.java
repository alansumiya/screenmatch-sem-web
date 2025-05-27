package br.com.alura.screenmatchnovo.principal;

import br.com.alura.screenmatchnovo.model.DadosEpisodio;
import br.com.alura.screenmatchnovo.model.DadosSerie;
import br.com.alura.screenmatchnovo.model.DadosTemporada;
import br.com.alura.screenmatchnovo.model.Episodio;
import br.com.alura.screenmatchnovo.services.ConsumoApi;
import br.com.alura.screenmatchnovo.services.ConverteDados;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Principal {
    private Scanner ler = new Scanner(System.in);
    private ConsumoApi consumo = new ConsumoApi();
    private ConverteDados conversor = new ConverteDados();

    //para variáveis constantes coloco a palavra final, ou seja, ela nunca vai mudar
    //a nomenclatura de variáveis constantes é o nome todo em maiúsculo.
    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=e10c440a";

    public void exibeMenu(){

        System.out.print("Digite o nome da série para busca: ");
        var nomeSerie = ler.nextLine();
        var json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);
        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
        System.out.println(dados);

        List<DadosTemporada> temporadas = new ArrayList<>();

		for (int i = 1; i <= dados.totalTemporadas(); i++){
			json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + "&season=" + i + API_KEY);
			DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
			temporadas.add(dadosTemporada);
		}
		temporadas.forEach(System.out::println);

//        for (int i = 0; i < dados.totalTemporadas(); i++) {
//            List<DadosEpisodio> episodiosTemporada = temporadas.get(i).episodios();
//            for (int j = 0; j < episodiosTemporada.size(); j++) {
//                System.out.println(episodiosTemporada.get(j).titulo());
//            }
//        }
        //uma forma simplificada de fazer a mesma coisa que o for acima
        temporadas.forEach(t -> t.episodios().forEach(e -> System.out.println(e.titulo())));


        //estou criando uma lista de episódios e fazendo alguns filtros
        List<DadosEpisodio> dadosEpisodios = temporadas.stream()
                //para cada temporada eu gero um fluxo de dados de todos os episódios de todas as temporadas
                .flatMap(t -> t.episodios().stream())
                //crio a lista e possui incluir novos elementos nela
                .collect(Collectors.toList());

        System.out.println("\nTop 10 episódios");
        //pega a lista dos episódios criado acima
        dadosEpisodios.stream()
                //ignora quem não tem avaliação
                .filter(e -> !e.avaliacao().equalsIgnoreCase("N/A"))
                .peek(e -> System.out.println("Primeiro filtro (N/A) "+ e))
                //vai ordernar de acordo com a avaliação em ordem decrescente
                .sorted(Comparator.comparing(DadosEpisodio::avaliacao).reversed())
                .peek(e -> System.out.println("Ordenação "+ e))
                //limitando em 5 episódios
                .limit(10)
                .peek(e -> System.out.println("Limite "+ e))
                //Coloca os títulos em maiúsculo
                .map(e -> e.titulo().toUpperCase())
                .peek(e -> System.out.println("Mapeamento "+ e))
                //imprime esses episódios
                .forEach(System.out::println);

        List<Episodio> episodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream()
                        //transformo os dadosEpisodio em um novo episodio, onde tem o número do episódio
                        //mais as informações dos DadosEpisodio
                        .map(d -> new Episodio(t.numero(), d))
                ).collect(Collectors.toList());
        episodios.forEach(System.out::println);
/*
        System.out.print("Digite um trecho do título do epsisódio: ");
        var trechoTitulo = ler.nextLine();
        Optional<Episodio> episodioBuscado = episodios.stream()
                .filter(e -> e.getTitulo().toUpperCase().contains(trechoTitulo.toUpperCase()))
                .findFirst();
        if (episodioBuscado.isPresent()) {
            System.out.println("Episódio encontrado!");
            System.out.println("Temporada: "+ episodioBuscado.get().getTemporada());
        } else {
            System.out.println("Episódio não encontrado!");
        }

        System.out.println("A partir de que ano você deseja ver os episódios? ");
        var ano = ler.nextInt();
        ler.nextLine();

        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        //dataBusca vai ter o ano que o usuário digitou começando do dia 1 de janeiro
        LocalDate dataBusca = LocalDate.of(ano, 1, 1);

        episodios.stream()
                .filter(e -> e.getDataLancamento() != null && e.getDataLancamento().isAfter(dataBusca))
                .forEach(e -> System.out.println(
                        "Temporada: " + e.getTemporada() +
                        " Episódio: " + e.getNumeroEpisodio() +
                        " Data lançamento: "+ e.getDataLancamento().format(formatador)
                ));*/


        //faz a média das avaliações por temporada
        Map<Integer, Double> avaliacoesPorTemporada = episodios.stream()
                .filter(e -> e.getAvaliacao() > 0.00)
                .collect(Collectors.groupingBy(Episodio::getTemporada,
                        Collectors.averagingDouble(Episodio::getAvaliacao)));
        System.out.println(avaliacoesPorTemporada);

        //imprime uma lista de estatística de todos os episódios
        DoubleSummaryStatistics est = episodios.stream()
                .filter(e -> e.getAvaliacao() > 0.00)
                .collect(Collectors.summarizingDouble(Episodio::getAvaliacao));
        System.out.println("Média: "+ est.getAverage());
        System.out.println("Melhor episódio: "+ est.getMax());
        System.out.println("Pior episódio: "+ est.getMin());
        System.out.println("Quantidade: "+ est.getCount());

    }

}
