package com.arx.pokerIA.model;

import com.arx.pokerIA.service.PokerHandEnum;

public class FourOfAKindPokerHand extends PokerHand {

	public FourOfAKindPokerHand(Integer highestValue, Integer secondHighestValue) {
		super(PokerHandEnum.FOUR_OF_A_KIND, highestValue, secondHighestValue, null, null, null);
	}

}
