package br.com.alura.screenmatchnovo.model;

import br.com.alura.screenmatchnovo.services.ConsultaMyMemory;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;
@Entity
@Table(name = "series")//associa o nome que está na tabela do banco de dados com a classe Serie
public class Serie {
    //Indica que o atributo id é a chave primária da classe
    @Id
    //a geração do id é incremental
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //indica que o dados inserido nesse atributo é único da tabela, ou seja, um título não pode se
    //repetir mais de uma vez
    @Column(unique = true)
    private String titulo;
    private Integer totalTemporadas;
    private Double avaliacao;
    //Estou dizendo que o atributo Categoria é do tipo enum contendo String
    @Enumerated(EnumType.STRING)
    private Categoria genero;
    private String atores;
    private String poster;
    private String sinopse;
    // é uma anotação que diz para a aplicação não representar os episódios no banco de dados
   // @Transient
    //Relação 1 para muitos, eu tenho uma série que contém vários episódios
    //O mapeamento é feito indicando para episódio que está sendo feita pelo atributo serie
    @OneToMany(mappedBy = "serie", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Episodio> episodios = new ArrayList<>();

    //para que a JPA recupere os dados que foram buscados no banco de dados, ele exige que
    //tenha um construtor padrão para representar um objeto do tipo serie
    public Serie(){}


    //vai encaixando na classe as informações que vem da API
    public Serie(DadosSerie dadosSerie){
        this.titulo = dadosSerie.titulo();
        this.totalTemporadas = dadosSerie.totalTemporadas();
        //optional é ou ele recebe o valor da avaliação ou ele recebe 0 caso não tenha avaliação
        this.avaliacao = OptionalDouble.of(Double.valueOf(dadosSerie.avaliacao())).orElse(0);
        //split ele fraciona o texto em partes e você define que as frações são divididas pela vírgula
        //nesse caso estou pegando a primeira fração, no caso o zero define isso. O trim ele ignora
        //espaço que estiver dentro dessa fração
        this.genero = Categoria.fromString(dadosSerie.genero().split(",")[0].trim());
        this.atores = dadosSerie.atores();
        this.poster = dadosSerie.poster();
        this.sinopse = ConsultaMyMemory.obterTraducao(dadosSerie.sinopse().trim());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Integer getTotalTemporadas() {
        return totalTemporadas;
    }

    public void setTotalTemporadas(Integer totalTemporadas) {
        this.totalTemporadas = totalTemporadas;
    }

    public Double getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(Double avaliacao) {
        this.avaliacao = avaliacao;
    }

    public Categoria getGenero() {
        return genero;
    }

    public void setGenero(Categoria genero) {
        this.genero = genero;
    }

    public String getAtores() {
        return atores;
    }

    public void setAtores(String atores) {
        this.atores = atores;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getSinopse() {
        return sinopse;
    }

    public void setSinopse(String sinopse) {
        this.sinopse = sinopse;
    }

    public List<Episodio> getEpisodios() {
        return episodios;
    }

    public void setEpisodios(List<Episodio> episodios) {
        //preenche a chave estrangeira na tabela dos episódios
        episodios.forEach(e -> e.setSerie(this));
        this.episodios = episodios;
    }

    @Override
    public String toString() {
        return  "genero=" + genero +
                ", titulo='" + titulo + '\'' +
                ", totalTemporadas=" + totalTemporadas +
                ", avaliacao=" + avaliacao +
                ", atores='" + atores + '\'' +
                ", poster='" + poster + '\'' +
                ", sinopse='" + sinopse + '\'' +
                ", episodios='" + episodios + '\''
                ;
    }
}
