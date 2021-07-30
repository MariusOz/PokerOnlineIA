package com.arx.pokerIA.model;

import com.arx.pokerIA.service.PokerHandEnum;

public class HighHandPokerHand extends PokerHand {

	public HighHandPokerHand(Integer highestValue, Integer secondHighestValue,
			Integer thirdHighestValue, Integer fourthHighestValue, Integer fifthHighestValue) {
		super(PokerHandEnum.HIGH_HAND, highestValue, secondHighestValue, thirdHighestValue, fourthHighestValue, fifthHighestValue);
		// TODO Auto-generated constructor stub
	}

}
