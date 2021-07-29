package com.arx.pokerIA.model;
/**
 * 
 * Informations publiques sur les joueurs
 * @author ozann
 *
 */
public class PlayerDTO {

	// argent mis en jeux
	private int bet;
	// argent restant
	private int funds;
	private int playerId;
	// nom du joueur
	private String name;
	// participe a la manche
	private boolean isStillInRound;
	private boolean hasPlayed;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public int getBet() {
		return bet;
	}
	public void setBet(int bet) {
		this.bet = bet;
	}

	public int getFunds() {
		return funds;
	}
	public void setFunds(int funds) {
		this.funds = funds;
	}

	public boolean isStillInRound() {
		return isStillInRound;
	}
	public void setStillInRound(boolean isStillInRound) {
		this.isStillInRound = isStillInRound;
	}
	
	public boolean isHasPlayed() {
		return hasPlayed;
	}
	public void setHasPlayed(boolean hasPlayed) {
		this.hasPlayed = hasPlayed;
	}

	public int getPlayerId() {
		return playerId;
	}
	public void setPlayerId(int id) {
		this.playerId = id;
	}
}