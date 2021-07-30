package com.arx.pokerIA.service;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.arx.pokerIA.model.GameDTO;
import com.arx.pokerIA.model.PlayerDTO;

@Component
public class PokerIAService {

	private static Logger LOG = LoggerFactory.getLogger(PokerIAService.class);

	@Autowired
	private PokerRestService restService;

	@Value("${numberofplayer}")
	private int nbOfPlayer;

	public void startIa(String name, int gameId) {
		PlayerDTO player = restService.addPlayerToExistingGame(gameId, name);
		LOG.info(name + " a bien été ajouté a la partie " + gameId + ", son identifiant est : " + player.getPlayerId());
		GameDTO game;

		do {
			game = restService.getGame(gameId, player.getPlayerId());
			LOG.info("le joueur récupère bien les infos de la partie");
			if (game.isMyTurn() && !GameStateStatusEnum.ENDED.equals(game.getStatus())) { // faire une méthode play()
																							// privé dans laquelle il y
																							// aura tout l'intelligence
																							// de l'IA
				ActionEnum action = play(game);
				try {
					restService.play(game.getGameId(), player.getPlayerId(), action);
					LOG.info("le joueur joue bien son action : " + action);
				} catch (Exception e) {
					LOG.error("can't play", e);
				}

			} else {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		} while (!isGameOver(game, name));
	}

	private ActionEnum play(GameDTO game) {
		return ActionEnum.values()[new Random().nextInt(ActionEnum.values().length)];
	}

	private boolean isGameOver(GameDTO game, String name) {
		// TODO on arrete la boucle lorsque gameStateEnum est terminé ou que je suis
		// éliminé
		boolean playerStillInGame = isPlayerStillInGame(game, name);
		boolean isGameOver = GameStateStatusEnum.ENDED.equals(game.getStatus()) || !playerStillInGame;
		if (isGameOver) {
			LOG.info("la partie est terminée, status : " + game.getStatus() + ", still in game : " + playerStillInGame);
			if (playerStillInGame) {
				LOG.info("félicitations, vous avez gagné");
			} else {
				LOG.info("Vous avez perdu");
			}
		}
		return isGameOver;
	}

	private boolean isPlayerStillInGame(GameDTO game, String name) {
		for (PlayerDTO player : game.getPlayerInfo()) {
			if (player.getName().equals(name)) {
				return true;
			}
		}
		return false;
	}

	public void startIa(String name) {
		Integer gameId = null;
		List<GameDTO> gameList = restService.listGames();
		if (gameList != null && !gameList.isEmpty()) {
			List<GameDTO> waitingsGames = gameList.stream()
					.filter(g -> GameStateStatusEnum.WAITING.equals(g.getStatus())).collect(Collectors.toList());
			if (waitingsGames.size() == 1) {
				gameId = waitingsGames.iterator().next().getGameId();
			}
		}

		if (gameId == null) {
			GameDTO game = restService.createGame(nbOfPlayer);
			gameId = game.getGameId();
			LOG.info("la partie numéro " + gameId + " a été créer");
		}

		startIa(name, gameId);
	}
}
