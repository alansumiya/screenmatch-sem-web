package br.com.alura.screenmatchnovo.principal;

import br.com.alura.screenmatchnovo.model.*;
import br.com.alura.screenmatchnovo.repository.SerieRepository;
import br.com.alura.screenmatchnovo.services.ConsumoApi;
import br.com.alura.screenmatchnovo.services.ConverteDados;

import java.util.*;
import java.util.stream.Collectors;

public class PrincipalComBanco {
    private Scanner leitura = new Scanner(System.in);
    private ConsumoApi consumo = new ConsumoApi();
    private ConverteDados conversor = new ConverteDados();
    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=6585022c";
    private List<DadosSerie> dadosSeries = new ArrayList<>();
    private SerieRepository repositorio;

    private List<Serie> series = new ArrayList<>();

    private Optional<Serie> serieBusca;

    public PrincipalComBanco(SerieRepository repositorio) {
        this.repositorio = repositorio;
    }


    public void exibeMenu() {
        var opcao = -1;
        while (opcao != 0) {

            var menu = """
                   1 - Buscar séries
                   2 - Buscar episódios
                   3 - Listar séries buscadas
                   4 - Buscar série por título
                   5 - Buscar série por ator
                   6 - Top 5 séries
                   7 - Buscar séries por categoria
                   8 - Buscar séries por quantidade de temporadas
                   9 - Buscar episódio por trecho
                   10 - Top 5 episódios por série
                   11 - Buscar episódios a partir de uma data
                   
                   0 - Sair                                 
                   """;

            System.out.println(menu);
            opcao = leitura.nextInt();
            leitura.nextLine();

            switch (opcao) {
                case 1:
                    buscarSerieWeb();
                    break;
                case 2:
                    buscarEpisodioPorSerie();
                    break;
                case 3:
                    listarSeriesBuscadas();
                    break;
                case 4:
                    buscarSeriePorTitulo();
                    break;
                case 5:
                    buscarSeriePorAtor();
                    break;
                case 6:
                    buscarTop5Series();
                    break;
                case 7:
                    buscarSeriesPorCategoria();
                    break;
                case 8:
                    buscarSeriesPorQntTemporada();
                    break;
                case 9:
                    buscarEpisodioPorTrecho();
                    break;
                case 10:
                    topEpisodiosPorSerie();
                    break;
                case 11:
                    buscarEpisodioPorData();
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida");
            }
        }
    }

    private void buscarEpisodioPorData() {
        buscarSeriePorTitulo();
        if (serieBusca.isPresent()){
            Serie serie = serieBusca.get();
            System.out.println("Digite o ano limite de lançamento");
            var anoLancamento = leitura.nextInt();
            leitura.nextLine();
            List<Episodio> episodiosAno = repositorio.episodiosPorSerieEAno(serie, anoLancamento);
            episodiosAno.forEach(System.out::println);
        }

    }

    private void topEpisodiosPorSerie() {
        buscarSeriePorTitulo();
        if (serieBusca.isPresent()){
            Serie serie = serieBusca.get();
            List<Episodio> topEpisodios = repositorio.topEpisodiosPorSerie(serie);
            topEpisodios.forEach(episodio ->
                    System.out.printf("Série: %s Temporada %s - Episódio %s - %s Avalicação %s\n",
                            episodio.getSerie().getTitulo(), episodio.getTemporada(),
                            episodio.getNumeroEpisodio(), episodio.getTitulo(), episodio.getAvaliacao()));
        }
    }

    private void buscarEpisodioPorTrecho() {
        System.out.print("Qual o nome do episódio para busca?:  ");
        var trechoEpisodio = leitura.nextLine();
        List<Episodio> episodiosEncontrados = repositorio.episodioPorTrecho(trechoEpisodio);
        episodiosEncontrados.forEach(episodio ->
                System.out.printf("Série: %s Temporada %s - Episódio %s - %s\n",
                        episodio.getSerie().getTitulo(), episodio.getTemporada(),
                        episodio.getNumeroEpisodio(), episodio.getTitulo()));
    }

    private void buscarSeriesPorQntTemporada() {
        System.out.print("Digite a quantidade máxima de temporadas que você deseja: ");
        var totalTemporadas = leitura.nextInt();
        leitura.nextLine();
        System.out.print("Digite a avaliação mínima desejada: ");
        var avaliacao = leitura.nextDouble();
        leitura.nextLine();
        List<Serie> filtroSeries = repositorio.seriesPorTemporadaEAvaliacao(totalTemporadas, avaliacao);
        System.out.println("Séries filtradas");
        filtroSeries.forEach(s ->
                System.out.println(s.getTitulo() + " - avaliação "+ s.getAvaliacao()));

    }

    private void buscarSeriesPorCategoria() {
        System.out.println("Deseja buscar séries de que categoria/gênero? ");
        var nomeGenero = leitura.nextLine();
        //estou pegando o que o usuário digitou e associando atráves do método fromPortugues
        //a uma categoria que tem cadastrado no enum
        Categoria categoria = Categoria.fromPortugues(nomeGenero);
        List<Serie> seriesPorCategoria = repositorio.findByGenero(categoria);
        System.out.println("Séries da categoria "+ nomeGenero);
        seriesPorCategoria.forEach(System.out::println);
    }

    private void buscarTop5Series() {
        List<Serie> serieTop = repositorio.findTop5ByOrderByAvaliacaoDesc();
        serieTop.forEach(s ->
                System.out.println(s.getTitulo() + " avaliação: "+ s.getAvaliacao()));
    }

    private void buscarSeriePorAtor() {
        System.out.print("Digite o nome do ator: ");
        var nomeAtor = leitura.nextLine();
        System.out.print("Avaliações a partir de que valor? ");
        var avaliacao = leitura.nextDouble();
        List<Serie> seriesEncontradas = repositorio.findByAtoresContainingIgnoreCaseAndAvaliacaoGreaterThanEqual(nomeAtor, avaliacao);
        System.out.println("Séries em que " + nomeAtor + " trabalhou: ");
        seriesEncontradas.forEach(s ->
                System.out.println(s.getTitulo() + " avaliação: " + s.getAvaliacao()));
    }

    private void buscarSeriePorTitulo() {
        System.out.print("Escolha uma série pelo nome: ");
        var nomeSerie = leitura.nextLine();
        serieBusca = repositorio.findByTituloContainingIgnoreCase(nomeSerie);

        if(serieBusca.isPresent()){
            System.out.println("Dados da série: "+ serieBusca.get());
        }else{
            System.out.println("Série não encontrada!");
        }
    }

    private void listarSeriesBuscadas() {
        //pega do repositório e salva na lista
        series = repositorio.findAll();
        series.stream()
                .sorted(Comparator.comparing(Serie::getGenero))
                .forEach(System.out::println);

    }

    private void buscarSerieWeb() {
        DadosSerie dados = getDadosSerie();
        Serie serie = new Serie(dados);
        //pego os dados que foram trazidos da série e salvo no banco de dados
        repositorio.save(serie);
        System.out.println(dados);
    }

    private DadosSerie getDadosSerie() {
        System.out.println("Digite o nome da série para busca");
        var nomeSerie = leitura.nextLine();
        var json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);
        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
        return dados;
    }

    private void buscarEpisodioPorSerie(){
        listarSeriesBuscadas();
        System.out.print("Escolha uma série pelo nome: ");
        var nomeSerie = leitura.nextLine();

        Optional<Serie> serie = repositorio.findByTituloContainingIgnoreCase(nomeSerie);
        if (serie.isPresent()) {
            var serieEncontrada = serie.get();
            List<DadosTemporada> temporadas = new ArrayList<>();

            for (int i = 1; i <= serieEncontrada.getTotalTemporadas(); i++) {
                var json = consumo.obterDados(ENDERECO + serieEncontrada.getTitulo().replace(" ", "+") + "&season=" + i + API_KEY);
                DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
                temporadas.add(dadosTemporada);
            }
            temporadas.forEach(System.out::println);
            List<Episodio> episodios = temporadas.stream()
                    .flatMap(d -> d.episodios().stream()
                    .map(e ->new Episodio(d.numero(), e)))
                    .collect(Collectors.toList());
            serieEncontrada.setEpisodios(episodios);
            repositorio.save(serieEncontrada);
        } else {
            System.out.println("Série não encontrada!");
        }
    }
}
