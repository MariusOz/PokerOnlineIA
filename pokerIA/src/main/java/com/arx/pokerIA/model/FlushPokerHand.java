package com.arx.pokerIA.model;

import com.arx.pokerIA.service.PokerHandEnum;

public class FlushPokerHand extends PokerHand {

	public FlushPokerHand(Integer highestValue, Integer secondHighestValue,
			Integer thirdHighestValue, Integer fourthHighestValue, Integer fifthHighestValue) {
		super(PokerHandEnum.FLUSH, highestValue, secondHighestValue, thirdHighestValue, fourthHighestValue, fifthHighestValue);
		// TODO Auto-generated constructor stub
	}

}
