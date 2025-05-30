package br.com.alura.screenmatchnovo.model;

public enum Categoria {
    ACAO("Action", "Ação"),
    ROMANCE("Romance", "Romance"),
    DRAMA("Drama", "Drama"),
    COMEDIA("Comedy", "Comédia"),
    CRIME("Crime", "Crime");
    private String categoriaOmdb;
    private String categoriaPortugues;

    Categoria(String categoriaOmdb, String categoriaPortugues){
        this.categoriaOmdb = categoriaOmdb;
        this.categoriaPortugues = categoriaPortugues;
    }

    //ele recebe a categoria da API, caso ele ache a correspondência no ENUM, ele atribui a categoria
    //caso ele não achei ele lança uma exceção
    public static Categoria fromString(String text) {
        for (Categoria categoria : Categoria.values()) {
            if (categoria.categoriaOmdb.equalsIgnoreCase(text)) {
                return categoria;
            }
        }
        throw new IllegalArgumentException("Nenhuma categoria encontrada para a string fornecida: " + text);
    }
    //Associa o que o usuário digitou a uma categoria que existe no Enum
    public static Categoria fromPortugues(String text) {
        for (Categoria categoria : Categoria.values()) {
            if (categoria.categoriaPortugues.equalsIgnoreCase(text)) {
                return categoria;
            }
        }
        throw new IllegalArgumentException("Nenhuma categoria encontrada para a string fornecida: " + text);
    }
}
