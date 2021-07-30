package com.arx.pokerIA.model;

import com.arx.pokerIA.service.PokerHandEnum;

public class ThreeOfAKindPokerHand extends PokerHand {

	public ThreeOfAKindPokerHand(Integer highestValue, Integer secondHighestValue, Integer thirdHighestValue) {
		super(PokerHandEnum.THREE_OF_A_KIND, highestValue, secondHighestValue, thirdHighestValue, null, null);
	}

}
