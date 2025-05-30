package br.com.alura.screenmatchnovo.repository;

import br.com.alura.screenmatchnovo.model.Categoria;
import br.com.alura.screenmatchnovo.model.Episodio;
import br.com.alura.screenmatchnovo.model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

//o pacote repository é um repositório que vai fazer pra gente essas operações básicas de CRUD

//Quando você estende a JpaRepository você tem que falar qual a entidade que estou persistindo aqui
//esse repositório está manipulando quem? Nesse caso estou trabalhando com a classe serie e o segundo
//parâmetro é qual é o tipo do ID. Com isso já consigo salvar e recuperar coisas do banco de dados
public interface SerieRepository extends JpaRepository<Serie, Long> {
    //Verifica se tem um titulo com o nome que está na variável nomeSerie
    Optional<Serie> findByTituloContainingIgnoreCase(String nomeSerie);
    //achar ator que consta na string, ignorando maiúsculas e a avaliação seja maior o igual ao fornecido
    List<Serie> findByAtoresContainingIgnoreCaseAndAvaliacaoGreaterThanEqual(String nomeAtor, Double Avaliacao);
    //achar os top5 e ordenar por avaliação de forma decrescente
    List<Serie> findTop5ByOrderByAvaliacaoDesc();
    //achar pelo genero que recebe pelo atributo categoria de série
    List<Serie> findByGenero(Categoria categoria);
    //achar temporadas menor ou igual a quantidade informada e avaliação maior ou igual a informada
    List<Serie> findByTotalTemporadasLessThanEqualAndAvaliacaoGreaterThanEqual(Integer TotalTemporadas, Double Avaliacao);

    //Essa é a JPQL é uma busca na linguagem SQL dentro do java
    @Query("select s from Serie s WHERE s.totalTemporadas <= :totalTemporadas AND s.avaliacao >= :avaliacao")
    List<Serie> seriesPorTemporadaEAvaliacao(Integer totalTemporadas, Double avaliacao);

    @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE e.titulo ILIKE %:trechoEpisodio%")
    List<Episodio> episodioPorTrecho(String trechoEpisodio);
}
