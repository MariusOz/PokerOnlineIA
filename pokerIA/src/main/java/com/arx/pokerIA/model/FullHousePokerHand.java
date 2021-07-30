package com.arx.pokerIA.model;

import com.arx.pokerIA.service.PokerHandEnum;

public class FullHousePokerHand extends PokerHand {

	public FullHousePokerHand(Integer highestValue, Integer secondHighestValue) {
		super(PokerHandEnum.FULL_HOUSE, highestValue, secondHighestValue, null, null, null);
	}

}
