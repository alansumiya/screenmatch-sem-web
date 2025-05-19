package br.com.alura.screenmatchnovo;

import br.com.alura.screenmatchnovo.model.DadosSerie;
import br.com.alura.screenmatchnovo.services.ConsumoApi;
import br.com.alura.screenmatchnovo.services.ConverteDados;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//a implementação permite que eu faça algumas chamadas de linha de comando, como é uma interface eu preciso
//cumprir o contrato e preciso implementar o método run
public class ScreenmatchnovoApplication implements CommandLineRunner {

	public static void main(String[] args) {
		//executar o método run
		SpringApplication.run(ScreenmatchnovoApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		var consumoApi = new ConsumoApi();
		var json = consumoApi.obterDados("https://www.omdbapi.com/?t=gilmore+girls&apikey=e10c440a");
		System.out.println(json);
		//instancio o conversor
		ConverteDados conversor = new ConverteDados();
		//transforma os dados em DadosSerie
		DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
		System.out.println(dados);
	}
}
