package com.arx.pokerIA.model;

import com.arx.pokerIA.service.PokerHandEnum;

public class StraightFlushPokerHand extends PokerHand {

	public StraightFlushPokerHand(Integer highestValue) {
		super(PokerHandEnum.STRAIGHT_FLUSH, highestValue, null, null, null, null);
	}

}
