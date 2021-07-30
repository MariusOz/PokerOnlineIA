package com.arx.pokerIA;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.web.client.RestTemplate;

import com.arx.pokerIA.model.GameDTO;
import com.arx.pokerIA.service.PokerIAService;
import com.arx.pokerIA.service.PokerRestService;

@SpringBootApplication
public class PokerIaApplication implements CommandLineRunner{

	@Autowired
	private PokerIAService pokerIa;
	@Autowired
	private PokerRestService restService;
	@Autowired
	private ApplicationContext context;
	
	private static Logger LOG = LoggerFactory.getLogger(PokerIaApplication.class);

	public static void main(String[] args) {
		LOG.info("STARTING THE APPLICATION");
		SpringApplication.run(PokerIaApplication.class, args);
		LOG.info("APPLICATION FINISHED");		
	}
	
	public void run(String... args) throws IOException {
		LOG.info("EXECUTING : command line runner");
		
		if(args.length == 2) {
			pokerIa.startIa(args[0], Integer.valueOf(args[1]));
			LOG.info("fin du programme");
		}else if(args.length == 1){
			pokerIa.startIa(args[0]);
		}else {
			LOG.info("le programme ne peut démarer qu'avec deux paramètres, l'identidfiant de la partie et le nom du joueur");
		}
	}
}
