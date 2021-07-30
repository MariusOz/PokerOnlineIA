package com.arx.pokerIA.model;

import com.arx.pokerIA.service.PokerHandEnum;

public class TwoPairPokerHand extends PokerHand {

	public TwoPairPokerHand(Integer highestValue, Integer secondHighestValue, Integer thirdHighestValue) {
		super(PokerHandEnum.TWO_PAIR, highestValue, secondHighestValue, thirdHighestValue, null, null);
	}

}
