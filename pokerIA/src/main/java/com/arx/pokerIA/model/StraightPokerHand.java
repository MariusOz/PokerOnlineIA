package com.arx.pokerIA.model;

import com.arx.pokerIA.service.PokerHandEnum;

public class StraightPokerHand extends PokerHand {

	public StraightPokerHand(Integer highestValue) {
		super(PokerHandEnum.STRAIGHT, highestValue, null, null, null, null);
	}

}
