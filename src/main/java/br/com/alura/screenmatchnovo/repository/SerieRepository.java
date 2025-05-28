package br.com.alura.screenmatchnovo.repository;

import br.com.alura.screenmatchnovo.model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;

//o pacote repository é um repositório que vai fazer pra gente essas operações básicas de CRUD

//Quando você estende a JpaRepository você tem que falar qual a entidade que estou persistindo aqui
//esse repositório está manipulando quem? Nesse caso estou trabalhando com a classe serie e o segundo
//parâmetro é qual é o tipo do ID. Com isso já consigo salvar e recuperar coisas do banco de dados
public interface SerieRepository extends JpaRepository<Serie, Long> {
}
