package br.com.alura.screenmatchnovo;

import br.com.alura.screenmatchnovo.principal.Principal;
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
		Principal principal = new Principal();
		principal.exibeMenu();


	}

}
