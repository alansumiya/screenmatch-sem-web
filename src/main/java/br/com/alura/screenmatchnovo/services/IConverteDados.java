package br.com.alura.screenmatchnovo.services;

public interface IConverteDados {
    //precisa de um método de obter dados onde eu tenho como entrada um json e uma classe
    //<T> o método retorna um valor que não sabemos o que é, quando não sabemos
    //utilizamos ele
    <T> T obterDados(String json, Class<T> classe);
}
