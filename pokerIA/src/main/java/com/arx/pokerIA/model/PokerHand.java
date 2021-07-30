package com.arx.pokerIA.model;

import com.arx.pokerIA.service.PokerHandEnum;

public class PokerHand implements Comparable {

	private final PokerHandEnum pokerHandEnum;
	private final Integer highestValue;
	private final Integer secondHighestValue;

	private final Integer thirdHighestValue;
	private final Integer fourthHighestValue;
	private final Integer fifthHighestValue;

	protected PokerHand(PokerHandEnum pokerHandEnum, Integer highestValue, Integer secondHighestValue,
			Integer thirdHighestValue, Integer fourthHighestValue, Integer fifthHighestValue) {
		super();
		this.pokerHandEnum = pokerHandEnum;
		this.highestValue = highestValue;
		this.secondHighestValue = secondHighestValue;
		this.thirdHighestValue = thirdHighestValue;
		this.fourthHighestValue = fourthHighestValue;
		this.fifthHighestValue = fifthHighestValue;
	}

	@Override
	public int compareTo(Object o) {
		PokerHand other = (PokerHand) o;

		int comparedToOtherHand = pokerHandEnum.compareTo(other.pokerHandEnum);

		int firstHighValue;
		int secondHighValue;
		int thirdHighValue;
		int fourthHighValue;
		int fifthHighValue;

		if (comparedToOtherHand == 0) {
			switch (pokerHandEnum) {
			case STRAIGHT_FLUSH:
			case STRAIGHT:
				return Integer.compare(highestValue, other.highestValue);
			case FOUR_OF_A_KIND:
			case FULL_HOUSE:
				firstHighValue = Integer.compare(highestValue, other.highestValue);
				if (firstHighValue == 0) {
					return Integer.compare(secondHighestValue, other.secondHighestValue);
				} else {
					return firstHighValue;
				}
			case THREE_OF_A_KIND:
			case TWO_PAIR:
				firstHighValue = Integer.compare(highestValue, other.highestValue);
				if (firstHighValue == 0) {
					secondHighValue = Integer.compare(secondHighestValue, other.secondHighestValue);
					if (secondHighValue == 0) {
						return Integer.compare(thirdHighestValue, other.thirdHighestValue);
					} else {
						return secondHighValue;
					}
				} else {
					return firstHighValue;
				}
			case ONE_PAIR:
				firstHighValue = Integer.compare(highestValue, other.highestValue);
				if (firstHighValue == 0) {
					secondHighValue = Integer.compare(secondHighestValue, other.secondHighestValue);
					if (secondHighValue == 0) {
						thirdHighValue = Integer.compare(thirdHighestValue, other.thirdHighestValue);
						if (thirdHighValue == 0) {
							return Integer.compare(fourthHighestValue, other.fourthHighestValue);
						} else {
							return thirdHighValue;
						}
					} else {
						return secondHighValue;
					}
				} else {
					return firstHighValue;
				}

			case FLUSH:
			case HIGH_HAND:
				firstHighValue = Integer.compare(highestValue, other.highestValue);
				if (firstHighValue == 0) {
					secondHighValue = Integer.compare(secondHighestValue, other.secondHighestValue);
					if (secondHighValue == 0) {
						thirdHighValue = Integer.compare(thirdHighestValue, other.thirdHighestValue);
						if (thirdHighValue == 0) {
							fourthHighValue = Integer.compare(fourthHighestValue, other.fourthHighestValue);
							if (fourthHighValue == 0) {
								return Integer.compare(fifthHighestValue, other.fifthHighestValue);
							} else {
								return fourthHighValue;
							}
						} else {
							return thirdHighValue;
						}
					} else {
						return secondHighValue;
					}
				} else {
					return firstHighValue;
				}

			default:
				throw new IllegalStateException("Cas de main non géré, ne devrais pas se produire.");
			}
		} else {
			return comparedToOtherHand * -1;
		}
	}

	public PokerHandEnum getPokerHandEnum() {
		return pokerHandEnum;
	}

	public int getHighestValue() {
		return highestValue;
	}

	public int getSecondHighestValue() {
		return secondHighestValue;
	}

	public int getThirdHighestValue() {
		return thirdHighestValue;
	}

	public int getFourthHighestValue() {
		return fourthHighestValue;
	}

	public int getFifthHighestValue() {
		return fifthHighestValue;
	}
}
