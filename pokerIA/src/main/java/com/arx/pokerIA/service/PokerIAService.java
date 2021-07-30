package com.arx.pokerIA.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.arx.pokerIA.model.Card;
import com.arx.pokerIA.model.GameDTO;
import com.arx.pokerIA.model.PlayerDTO;
import com.arx.pokerIA.model.PokerHand;

@Component
public class PokerIAService {

	private static Logger LOG = LoggerFactory.getLogger(PokerIAService.class);

	@Autowired
	private PokerRestService restService;

	@Autowired
	private HandService handService;

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
				ActionEnum action = play(game, name);
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

	private ActionEnum play(GameDTO game, String myName) {
		Card smallCard;
		Card bigCard;
		boolean localPair = false;
		boolean potentialLocalStraight = false;
		int maxBet = getMaxBet(game.getPlayerInfo());
		PlayerDTO myInfo = game.getPlayerInfo().stream().filter(p -> myName.equals(p.getName())).findFirst().get();
		ActionEnum result = null;

		if (game.getPlayerHand().get(0).getNumber() > game.getPlayerHand().get(1).getNumber()) {
			smallCard = game.getPlayerHand().get(1);
			bigCard = game.getPlayerHand().get(0);
		} else {
			smallCard = game.getPlayerHand().get(0);
			bigCard = game.getPlayerHand().get(1);
		}

		if (smallCard.getNumber() == bigCard.getNumber()) {
			localPair = true;
		} else if (bigCard.getNumber() - smallCard.getNumber() < 4) {
			potentialLocalStraight = true;
		}

		if (PhaseEnum.PRE_FLOP.equals(game.getPhase())) {
			result =  playPreFlop(smallCard, bigCard, localPair, potentialLocalStraight, maxBet);
		} else {
			List<Card> deck = initDeck(game);
			double winProba = getProba(smallCard, bigCard, game.getCardsOnTable(), deck);

			if (winProba >= 80 && maxBet < 32) {
				result = ActionEnum.OVERBET;
			} else if (winProba >= 70 && maxBet < 16) {
				result = ActionEnum.OVERBET;
			} else if (winProba >= 70) {
				result = ActionEnum.FOLLOW;
			} else if (winProba > 60 && maxBet <= 32) {
				result = ActionEnum.FOLLOW;
			} else if (winProba > 30 && maxBet <= 16 && PhaseEnum.FLOP.equals(game.getPhase())) {
				result = ActionEnum.FOLLOW;
			} else if (winProba > 30 && maxBet <= 16 && PhaseEnum.TURN.equals(game.getPhase())) {
				result = ActionEnum.FOLLOW;
			} else if (maxBet <= 4) {
				result = ActionEnum.FOLLOW;
			} else if (maxBet <= 8 && bigCard.getNumber() >= 13) {
				result = ActionEnum.FOLLOW;
			} else {
				result = ActionEnum.FOLD;
			}
		}
		if(ActionEnum.FOLD.equals(result) && maxBet <= myInfo.getBet()) {
			result = ActionEnum.FOLLOW;
		}
		
		return result;
	}

	private double getProba(Card smallCard, Card bigCard, List<Card> table, List<Card> deck) {
		int better = 0;
		int worse = 0;
		List<Card> myCards = new ArrayList<>(table);
		myCards.add(smallCard);
		myCards.add(bigCard);
		PokerHand myHand = handService.findBestCombination(myCards);
		
		LocalDateTime end = LocalDateTime.now().plusSeconds(27);
		
		for (int i = 0; i < deck.size(); i++) {
			Card card1 = deck.get(i);
			for(int j = i + 1; j < deck.size(); j++) {
				Card card2 = deck.get(j);
				if(isBetterThanMe(myHand, card1, card2, table)) {
					better++;
				} else {
					worse++;
				}
			}
			if(LocalDateTime.now().isAfter(end)){
				break;
			}
		}
		
		LOG.info("Found, better: " + better + " , worse: " + worse);
		return (worse / (worse + better)) * 100;
	}

	private boolean isBetterThanMe(PokerHand myHand, Card card1, Card card2, List<Card> table) {
		List<Card> otherCards = new ArrayList<>(table);
		otherCards.add(card1);
		otherCards.add(card2);
		PokerHand otherHand = handService.findBestCombination(otherCards);

		return myHand.compareTo(otherHand) > 0;
	}

	public List<Card> initDeck(GameDTO game) {
		List<Card> deck = new ArrayList<>();
		deck.add(new Card(2, ColorEnum.SPADE));
		deck.add(new Card(3, ColorEnum.SPADE));
		deck.add(new Card(4, ColorEnum.SPADE));
		deck.add(new Card(5, ColorEnum.SPADE));
		deck.add(new Card(6, ColorEnum.SPADE));
		deck.add(new Card(7, ColorEnum.SPADE));
		deck.add(new Card(8, ColorEnum.SPADE));
		deck.add(new Card(9, ColorEnum.SPADE));
		deck.add(new Card(10, ColorEnum.SPADE));
		deck.add(new Card(11, ColorEnum.SPADE));
		deck.add(new Card(12, ColorEnum.SPADE));
		deck.add(new Card(13, ColorEnum.SPADE));
		deck.add(new Card(14, ColorEnum.SPADE));
		deck.add(new Card(2, ColorEnum.HEART));
		deck.add(new Card(3, ColorEnum.HEART));
		deck.add(new Card(4, ColorEnum.HEART));
		deck.add(new Card(5, ColorEnum.HEART));
		deck.add(new Card(6, ColorEnum.HEART));
		deck.add(new Card(7, ColorEnum.HEART));
		deck.add(new Card(8, ColorEnum.HEART));
		deck.add(new Card(9, ColorEnum.HEART));
		deck.add(new Card(10, ColorEnum.HEART));
		deck.add(new Card(11, ColorEnum.HEART));
		deck.add(new Card(12, ColorEnum.HEART));
		deck.add(new Card(13, ColorEnum.HEART));
		deck.add(new Card(14, ColorEnum.HEART));
		deck.add(new Card(2, ColorEnum.CLUB));
		deck.add(new Card(3, ColorEnum.CLUB));
		deck.add(new Card(4, ColorEnum.CLUB));
		deck.add(new Card(5, ColorEnum.CLUB));
		deck.add(new Card(6, ColorEnum.CLUB));
		deck.add(new Card(7, ColorEnum.CLUB));
		deck.add(new Card(8, ColorEnum.CLUB));
		deck.add(new Card(9, ColorEnum.CLUB));
		deck.add(new Card(10, ColorEnum.CLUB));
		deck.add(new Card(11, ColorEnum.CLUB));
		deck.add(new Card(12, ColorEnum.CLUB));
		deck.add(new Card(13, ColorEnum.CLUB));
		deck.add(new Card(14, ColorEnum.CLUB));
		deck.add(new Card(2, ColorEnum.DIAMOND));
		deck.add(new Card(3, ColorEnum.DIAMOND));
		deck.add(new Card(4, ColorEnum.DIAMOND));
		deck.add(new Card(5, ColorEnum.DIAMOND));
		deck.add(new Card(6, ColorEnum.DIAMOND));
		deck.add(new Card(7, ColorEnum.DIAMOND));
		deck.add(new Card(8, ColorEnum.DIAMOND));
		deck.add(new Card(9, ColorEnum.DIAMOND));
		deck.add(new Card(10, ColorEnum.DIAMOND));
		deck.add(new Card(11, ColorEnum.DIAMOND));
		deck.add(new Card(12, ColorEnum.DIAMOND));
		deck.add(new Card(13, ColorEnum.DIAMOND));
		deck.add(new Card(14, ColorEnum.DIAMOND));

		deck.removeAll(game.getPlayerHand());
		deck.removeAll(game.getCardsOnTable());

		Collections.shuffle(deck);
		return deck;
	}

	private ActionEnum playPreFlop(Card smallCard, Card bigCard, boolean localPair, boolean potentialLocalStraight,
			int maxBet) {
		if (maxBet < 8 && localPair) {
			return ActionEnum.OVERBET;
		} else if (maxBet < 16 && localPair && smallCard.getNumber() > 11) {
			return ActionEnum.OVERBET;
		} else if (potentialLocalStraight && maxBet <= 8) {
			return ActionEnum.FOLLOW;
		} else if (bigCard.getNumber() >= 13 && maxBet <= 8) {
			return ActionEnum.FOLLOW;
		} else {
			return ActionEnum.FOLD;
		}
	}

	private int getMaxBet(List<PlayerDTO> playerInfo) {
		return playerInfo.stream().map(p -> p.getBet()).max(Integer::compare).get();
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
