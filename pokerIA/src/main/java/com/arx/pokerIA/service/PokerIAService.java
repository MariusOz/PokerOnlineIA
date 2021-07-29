package com.arx.pokerIA.service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.arx.pokerIA.PokerIaApplication;
import com.arx.pokerIA.model.GameDTO;
import com.arx.pokerIA.model.PlayerDTO;

@Component
public class PokerIAService {

	private static Logger LOG = LoggerFactory.getLogger(PokerIAService.class);

	@Autowired
	private PokerRestService restService;

	public void startIa(int gameId, String name) {
		PlayerDTO player = restService.addPlayerToExistingGame(gameId, name);
		GameDTO game;

		do {
			game = restService.getGame(gameId, player.getPlayerId());
			if (game.isMyTurn() == true) { // faire une méthode play() privé dans laquelle il y aura tout l'intelligence
											// de l'IA
				ActionEnum action = play(game);
				restService.play(game.getGameId(), player.getPlayerId(), action);
			}

		} while (!isGameOver(game, name));
	}

	private ActionEnum play(GameDTO game) {

		return ActionEnum.values()[new Random().nextInt(ActionEnum.values().length)];
	}

	private boolean isGameOver(GameDTO game, String name) {
		// TODO on arrete la boucle lorsque gameStateEnum est terminé ou que je suis
		// éliminé
		return GameStateStatusEnum.ENDED.equals(game.getStatus()) || isPlayerStillInGame(game, name);
	}

	private boolean isPlayerStillInGame(GameDTO game, String name) {
		for (PlayerDTO player : game.getPlayerInfo()) {
			if (player.getName().equals(name)) {
				return true;
			}
		}
		return false;
	}
}
