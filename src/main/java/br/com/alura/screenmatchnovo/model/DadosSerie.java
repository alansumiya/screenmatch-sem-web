package br.com.alura.screenmatchnovo.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

//Essa anotação JsonAlias é para vincular o nome exato que está no banco de dados
//com o nome que eu defini na minha classe de atributos

//Essa anotação ignora as informações que você não encontrar
@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosSerie(@JsonAlias("Title") String titulo,
                         @JsonAlias("totalSeasons") Integer totalTemporadas,
                         @JsonAlias("imdbRating") String avaliacao) {
}
