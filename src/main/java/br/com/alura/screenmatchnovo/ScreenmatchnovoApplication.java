package br.com.alura.screenmatchnovo;

import br.com.alura.screenmatchnovo.principal.Principal;
import br.com.alura.screenmatchnovo.principal.PrincipalComBanco;
import br.com.alura.screenmatchnovo.principal.PrincipalNovo;
import br.com.alura.screenmatchnovo.repository.SerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//a implementação permite que eu faça algumas chamadas de linha de comando, como é uma interface eu preciso
//cumprir o contrato e preciso implementar o método run
public class ScreenmatchnovoApplication implements CommandLineRunner {
	//é uma injeção de dependência onde os objetos necessário são fornecidos automaticamente pelo Spring,
	//em vez de você criá-los manualmente com o new
	@Autowired
	private SerieRepository repositorio;

	public static void main(String[] args) {
		//executar o método run
		SpringApplication.run(ScreenmatchnovoApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		PrincipalComBanco principalComBanco = new PrincipalComBanco(repositorio);
		principalComBanco.exibeMenu();


	}

}
