package com.arx.pokerIA.model;

import com.arx.pokerIA.service.ColorEnum;

public class Card {

	private int number;
	private ColorEnum color;

	public Card(int number, ColorEnum color) {
		this.number = number;
		this.color = color;
	}

	public Card() {
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((color == null) ? 0 : color.hashCode());
		result = prime * result + number;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Card other = (Card) obj;
		if (color == null) {
			if (other.color != null)
				return false;
		} else if (!color.equals(other.color))
			return false;
		if (number != other.number)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return Integer.toString(number) + "_" + color.toString();
	}
	
	public String toShortString() {
		return Integer.toString(number) + color.toString().substring(0,1);
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public ColorEnum getColor() {
		return color;
	}

	public void setColor(ColorEnum color) {
		this.color = color;
	}
}
