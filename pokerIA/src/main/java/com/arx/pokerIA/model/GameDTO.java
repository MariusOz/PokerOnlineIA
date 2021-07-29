package com.arx.pokerIA.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.arx.pokerIA.service.GameStateStatusEnum;
import com.arx.pokerIA.service.PhaseEnum;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class GameDTO {
	
	private int gameId;
	private int currentRound;
	private int totalBets;
	private int currentPlayerId;
	// nom du joueur
	private String name;
	private boolean myTurn = false;
	// votre main
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private List<Card> playerHand;
	// cartes et mises sur la table
	private List<Card> cardsOnTable;
	// ce que chaque joueur à sur la table et en banque
	// si les joueurs son couchés ou actifs
	private List<PlayerDTO> playerInfo;
	private PhaseEnum phase = null;
	private GameStateStatusEnum status;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public int getGameId() {
		return gameId;
	}
	public void setGameId(int gameId) {
		this.gameId = gameId;
	}
	
	public List<Card> getPlayerHand() {
		return Collections.unmodifiableList(playerHand);
	}
	public void setPlayerHand(List<Card> playerHand) {
		this.playerHand = playerHand;
	}
	
	public List<Card> getCardsOnTable() {
		return Collections.unmodifiableList(cardsOnTable);
	}
	public void setCardsOnTable(List<Card> cardsOnTable) {
		this.cardsOnTable = cardsOnTable;
	}
	
	public int getTotalBets() {
		return totalBets;
	}
	public void setTotalBets(int totalBets) {
		this.totalBets = totalBets;
	}
	
	public List<PlayerDTO> getPlayerInfo() {
		return Collections.unmodifiableList(playerInfo);
	}
	public void setPlayerInfo(List<PlayerDTO> playerInfo) {
		this.playerInfo = playerInfo;
	}
	
	public PhaseEnum getPhase() {
		return phase;
	}
	public void setPhase(PhaseEnum phase) {
		this.phase = phase;
	}
	
	public GameStateStatusEnum getStatus() {
		return status;
	}
	public void setStatus(GameStateStatusEnum status) {
		this.status = status;
	}
	public int getCurrentRound() {
		return currentRound;
	}
	public void setCurrentRound(int currentRound) {
		this.currentRound = currentRound;
	}
	
	public int getCurrentPlayerId() {
		return currentPlayerId;
	}
	public boolean isMyTurn() {
		return myTurn;
	}
	public void setMyTurn(boolean myTurn) {
		this.myTurn = myTurn;
	}
}
