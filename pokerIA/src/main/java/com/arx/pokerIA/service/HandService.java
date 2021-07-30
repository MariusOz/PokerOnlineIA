package com.arx.pokerIA.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.arx.pokerIA.model.Card;
import com.arx.pokerIA.model.FlushPokerHand;
import com.arx.pokerIA.model.FourOfAKindPokerHand;
import com.arx.pokerIA.model.FullHousePokerHand;
import com.arx.pokerIA.model.HighHandPokerHand;
import com.arx.pokerIA.model.OnePairPokerHand;
import com.arx.pokerIA.model.PokerHand;
import com.arx.pokerIA.model.StraightFlushPokerHand;
import com.arx.pokerIA.model.StraightPokerHand;
import com.arx.pokerIA.model.ThreeOfAKindPokerHand;
import com.arx.pokerIA.model.TwoPairPokerHand;

@Component
public class HandService {

	public PokerHand findBestCombination(List<Card> cards) {

		PokerHand result = hasStraightFlush(cards);
		if (result != null) {
			return result;
		}
		result = hasFourOfAKind(cards);
		if (result != null) {
			return result;
		}
		result = hasFullHouse(cards);
		if (result != null) {
			return result;
		}
		result = hasflush(cards);
		if (result != null) {
			return result;
		}
		result = hasStraight(cards);
		if (result != null) {
			return result;
		}

		result = hasThreeOfAKind(cards);
		if (result != null) {
			return result;
		}
		result = hasTwoPairs(cards);
		if (result != null) {
			return result;
		}
		result = hasOnePair(cards);
		if (result != null) {
			return result;
		}
		return hasHighHand(cards);
	}

	private PokerHand hasStraightFlush(List<Card> cards) {
		Card[] cardArray = sortFromMaxToMinValue(cards);
		Card[] finalHand = new Card[5];

		// solution

		Card highCard = null;
		Card currentCard = null;
		int nextCardFound = 0;
		List<Card> filteredCards = null;
		Card[] sortedFilteredCardsArray = null;

		// pour chaque couleur, y a t il une quinte ?
		for (ColorEnum c : ColorEnum.values()) {
			filteredCards = filterByColor(cards, c);
			sortedFilteredCardsArray = sortFromMaxToMinValue(filteredCards);

			for (int i = 0; i < sortedFilteredCardsArray.length - 4; i++) {
				// set la première carte a vérifier
				currentCard = sortedFilteredCardsArray[i];
				for (int j = 0; j < sortedFilteredCardsArray.length; j++) {
					if (!currentCard.equals(sortedFilteredCardsArray[j])
							&& sortedFilteredCardsArray[j].getNumber() == currentCard.getNumber() - 1) {
						// verifie si le nombre de toute les cartes de l'array se suivre
						currentCard = sortedFilteredCardsArray[j];
						nextCardFound++;
						// si il y a une succession de 5 cartes
						if (nextCardFound == 4) {
							highCard = sortedFilteredCardsArray[i];
							break;
						}
					}
				}
				if (highCard != null) {
					break;
				}
			}
		}
		if (highCard == null) {
			Card[] sortedFilteredCardArrayForAceCaseStraights = new Card[cards.size()];
			int counter = 0;

			// verifier si la sorted list contiens une carte de valeur 14
			if (contains14(cards.toArray(new Card[cards.size()]))) {
				for (int j = 0; j < cards.size(); j++) {
					Card aceAsAOne = null;

					if (cards.get(j).getNumber() == 14) {
						// ajouter la carte a l'array de nouvelles cartes
						aceAsAOne = new Card(1, cards.get(j).getColor());
						sortedFilteredCardArrayForAceCaseStraights[j] = aceAsAOne;
						counter += 1;
					} else {
						sortedFilteredCardArrayForAceCaseStraights[j] = cards.get(j);
						counter += 1;
					}

				}
				sortedFilteredCardArrayForAceCaseStraights = sortFromMaxToMinValue(
						Arrays.asList(sortedFilteredCardArrayForAceCaseStraights));

				return hasStraightFlush(Arrays.asList(sortedFilteredCardArrayForAceCaseStraights));
				// sort le nouvel array de carte avec les nouvelles valeurs

			} else {
				return null;
			}
		} else {
			return new StraightFlushPokerHand(highCard.getNumber());
		}

	}

	private PokerHand hasFourOfAKind(List<Card> cards) {
		// sort la main de 7 cartes du player
		Card[] currentHand = sortFromMaxToMinValue(cards);

		Card[] finalHand = new Card[5];
		int value = 0;
		// recherche le première triple dans le tableau trier puis les ajoute dans le
		// tableau en 0 et 1 si il existe
		for (int i = 0; i < currentHand.length - 3; i++) {
			if (currentHand[i].getNumber() == currentHand[i + 1].getNumber()
					&& currentHand[i + 1].getNumber() == currentHand[i + 2].getNumber()
					&& currentHand[i + 2].getNumber() == currentHand[i + 3].getNumber()) {
				finalHand[0] = currentHand[i];
				finalHand[1] = currentHand[i + 1];
				finalHand[2] = currentHand[i + 2];
				finalHand[3] = currentHand[i + 3];
				currentHand[i] = null;
				currentHand[i + 1] = null;
				currentHand[i + 2] = null;
				currentHand[i + 3] = null;
				break;
			}
		}
		// si le tableau est vide, on a pas trouvé de triple
		if (finalHand[0] == null && finalHand[1] == null && finalHand[2] == null && finalHand[3] == null) {
			return null;
		} else {
			// continuer la recherche pour ajouter les 3 plus hautes valeurs dans le tableau
			for (int i = 4; i < 5; i++) {
				// chercher la plus haute valeur dans current hand
				for (int j = 0; j < currentHand.length; j++) {
					if (currentHand[j] != null) {
						finalHand[i] = currentHand[j];
						currentHand[j] = null;
						break;
					}
				}
			}
			return new FourOfAKindPokerHand(finalHand[0].getNumber(), finalHand[4].getNumber());
		}
	}

	private PokerHand hasFullHouse(List<Card> cards) {
		// sort la main de 7 cartes du player
		Card[] currentHand = sortFromMaxToMinValue(cards);

		Card[] finalHand = new Card[5];
		int value = 0;
		// recherche le première triple dans le tableau trier puis les ajoute dans le
		// tableau en 0, 1 et 2 si il existe
		for (int i = 0; i < currentHand.length - 2; i++) {
			if (currentHand[i].getNumber() == currentHand[i + 1].getNumber()
					&& currentHand[i + 1].getNumber() == currentHand[i + 2].getNumber()) {
				finalHand[0] = currentHand[i];
				finalHand[1] = currentHand[i + 1];
				finalHand[2] = currentHand[i + 2];
				currentHand[i] = null;
				currentHand[i + 1] = null;
				currentHand[i + 2] = null;
				break;
			}
		}
		// si le tableau est vide, on a pas trouvé de triple
		if (finalHand[0] == null && finalHand[1] == null && finalHand[2] == null) {
			return null;
		} else {
			// on cherche une seconde paire dans les cartes restantes dans currentHand
			for (int i = 0; i < currentHand.length - 1; i++) {
				if (currentHand[i] != null && currentHand[i + 1] != null) {
					if (currentHand[i].getNumber() == currentHand[i + 1].getNumber()) {
						finalHand[2] = currentHand[i];
						finalHand[3] = currentHand[i + 1];
						currentHand[i] = null;
						currentHand[i + 1] = null;
						break;
					}
				}
			}
			if (finalHand[3] == null && finalHand[4] == null) {
				return null;
			} else {
				return new FullHousePokerHand(finalHand[0].getNumber(), finalHand[3].getNumber());
			}
		}

	}

	private PokerHand hasflush(List<Card> cards) {
		Map<ColorEnum, Integer> colorMap = new HashMap<>();
		colorMap.put(ColorEnum.CLUB, 0);
		colorMap.put(ColorEnum.DIAMOND, 0);
		colorMap.put(ColorEnum.HEART, 0);
		colorMap.put(ColorEnum.SPADE, 0);

		for (Card card : cards) {
			// récupérer la key de la couleur de la carte
			int counter = colorMap.get(card.getColor());
			// ajouter la carte dans le map et incrémenter le compteur
			colorMap.put(card.getColor(), counter + 1);
		}

		ColorEnum flushColor = null;
		for (Entry<ColorEnum, Integer> e : colorMap.entrySet()) {
			// pour chaque colorEnum dans la map, vérifier le compteur
			if (e.getValue() >= 5) {
				flushColor = e.getKey();
			}
		}

		if (flushColor != null) {
			// filtrer les carte
			List<Card> flushedCards = filterByColor(cards, flushColor);

			// sort les cartes
			Card[] finalHand = sortFromMaxToMinValue(flushedCards);
			return new FlushPokerHand(finalHand[0].getNumber(), finalHand[1].getNumber(), finalHand[2].getNumber(),
					finalHand[3].getNumber(), finalHand[4].getNumber());
		} else {
			return null;
		}

	}

	private PokerHand hasStraight(List<Card> cards) {
		Card[] cardArray = sortFromMaxToMinValue(cards);
		Card[] finalHand = new Card[5];

		// solution

		Card highCard = null;
		Card currentCard = null;
		int nextCardFound = 0;

		for (int i = 0; i < cardArray.length - 4; i++) {
			// set la première carte a vérifier
			currentCard = cardArray[i];
			for (int j = 0; j < cardArray.length; j++) {
				if (!currentCard.equals(cardArray[j]) && cardArray[j].getNumber() == currentCard.getNumber() - 1) {
					// verifie si le nombre de toute les cartes de l'array se suivre
					currentCard = cardArray[j];
					nextCardFound++;
					// si il y a une succession de 5 cartes
					if (nextCardFound == 4) {
						highCard = cardArray[i];
						break;
					}
				}
			}
			if (highCard != null) {
				break;
			}
		}
		if (highCard == null) {
			return null;
		} else {
			return new StraightPokerHand(highCard.getNumber());
		}

		// fin

//		for (int i = 0; i < cardArray.length; i++) {
//			
//			if (cardArray[i + 1].getNumber() == cardArray[i].getNumber() - 1) {
//				if (finalHand[finalHand.length - 1] == null) {
//					for (int j = 0; j < finalHand.length; j++) {
//						if (finalHand[j] == null) {
//							finalHand[j] = cardArray[i];
//							cardArray[i] = null;
//							break;
//						}
//					}
//				} else {
//					return new StraightPokerHand(finalHand[0].getNumber());
//				}
//
//			} else {
//				// vider l'array
//				for (int j = 0; j < finalHand.length; j++) {
//					finalHand[j] = null;
//				}
//				if (i >= 3) {
//					return null;
//				}
//
//				// retourner null
//
//			}
//		}
//		return null;
	}

	private PokerHand hasThreeOfAKind(List<Card> cards) {
		// sort la main de 7 cartes du player
		Card[] currentHand = sortFromMaxToMinValue(cards);

		Card[] finalHand = new Card[5];
		int value = 0;
		// recherche le première triple dans le tableau trier puis les ajoute dans le
		// tableau en 0, 1 et 2 si il existe
		for (int i = 0; i < currentHand.length - 2; i++) {
			if (currentHand[i].getNumber() == currentHand[i + 1].getNumber()
					&& currentHand[i + 1].getNumber() == currentHand[i + 2].getNumber()) {
				finalHand[0] = currentHand[i];
				finalHand[1] = currentHand[i + 1];
				finalHand[2] = currentHand[i + 2];
				currentHand[i] = null;
				currentHand[i + 1] = null;
				currentHand[i + 2] = null;
				break;
			}
		}
		// si le tableau est vide, on a pas trouvé de triple
		if (finalHand[0] == null && finalHand[1] == null && finalHand[2] == null) {
			return null;
		} else {
			// continuer la recherche pour ajouter les 2 plus hautes valeurs dans le tableau
			for (int i = 3; i < 5; i++) {
				// chercher la plus haute valeur dans current hand
				for (int j = 0; j < currentHand.length; j++) {
					if (currentHand[j] != null) {
						finalHand[i] = currentHand[j];
						currentHand[j] = null;
						break;
					}
				}
			}
			return new ThreeOfAKindPokerHand(finalHand[0].getNumber(), finalHand[3].getNumber(),
					finalHand[4].getNumber());
		}
	}

	private PokerHand hasTwoPairs(List<Card> cards) {
		// TODO Auto-generated method stub
		// sort la main de 7 cartes du player
		Card[] currentHand = sortFromMaxToMinValue(cards);

		Card[] finalHand = new Card[5];
		int value = 0;
		// recherche la première paire dans le tableau trier puis les ajoute dans le
		// tableau en 0 et 1 si elle existe
		for (int i = 0; i < currentHand.length - 1; i++) {
			if (currentHand[i].getNumber() == currentHand[i + 1].getNumber()) {
				finalHand[0] = currentHand[i];
				finalHand[1] = currentHand[i + 1];
				currentHand[i] = null;
				currentHand[i + 1] = null;
				break;
			}
		}
		// si le tableau est vide, on a pas trouvé de paire
		if (finalHand[0] == null && finalHand[1] == null) {
			return null;
		} else {
			// on cherche une seconde paire dans les cartes restantes dans currentHand
			for (int i = 0; i < currentHand.length - 1; i++) {
				if (currentHand[i] != null && currentHand[i + 1] != null) {
					if (currentHand[i].getNumber() == currentHand[i + 1].getNumber()) {
						finalHand[2] = currentHand[i];
						finalHand[3] = currentHand[i + 1];
						currentHand[i] = null;
						currentHand[i + 1] = null;
						break;
					}
				}
			}
		}

		if (finalHand[2] == null && finalHand[3] == null) {
			return null;
		} else {
			// continuer la recherche pour ajouter la plus hautes valeurs dans le tableau
			for (int i = 4; i < 5; i++) {
				// chercher la plus haute valeur dans current hand
				for (int j = 0; j < currentHand.length; j++) {
					if (currentHand[j] != null) {
						finalHand[i] = currentHand[j];
						currentHand[j] = null;
						break;
					}
				}
			}
			return new TwoPairPokerHand(finalHand[0].getNumber(), finalHand[2].getNumber(), finalHand[4].getNumber());
		}
	}

	private PokerHand hasOnePair(List<Card> cards) {
		// sort la main de 7 cartes du player
		Card[] currentHand = sortFromMaxToMinValue(cards);

		Card[] finalHand = new Card[5];
		int value = 0;
		// recherche la première paire dans le tableau trier puis les ajoute dans le
		// tableau en 0 et 1 si elle existe
		for (int i = 0; i < currentHand.length - 1; i++) {
			if (currentHand[i].getNumber() == currentHand[i + 1].getNumber()) {
				finalHand[0] = currentHand[i];
				finalHand[1] = currentHand[i + 1];
				currentHand[i] = null;
				currentHand[i + 1] = null;
				break;
			}
		}
		// si le tableau est vide, on a pas trouvé de paire
		if (finalHand[0] == null && finalHand[1] == null) {
			return null;
		} else {
			// continuer la recherche pour ajouter les 3 plus hautes valeurs dans le tableau
			for (int i = 2; i < 5; i++) {
				// chercher la plus haute valeur dans current hand
				for (int j = 0; j < currentHand.length; j++) {
					if (currentHand[j] != null) {
						finalHand[i] = currentHand[j];
						currentHand[j] = null;
						break;
					}
				}
			}
			return new OnePairPokerHand(finalHand[0].getNumber(), finalHand[2].getNumber(), finalHand[3].getNumber(),
					finalHand[4].getNumber());
		}
	}

	private PokerHand hasHighHand(List<Card> cards) {

		Card[] cardArray = sortFromMaxToMinValue(cards);
		Card[] finalHand = new Card[5];

		for (int i = 0; i < finalHand.length; i++) {
			finalHand[i] = cardArray[i];
			cardArray[i] = null;
		}
		if (finalHand[0] == null) {
			return null;
		} else {
			return new HighHandPokerHand(finalHand[0].getNumber(), finalHand[1].getNumber(), finalHand[2].getNumber(),
					finalHand[3].getNumber(), finalHand[4].getNumber());
		}

	}

	private boolean contains14(Card[] sortedFilteredCardsArray) {
		for (Card card : sortedFilteredCardsArray) {
			if (card.getNumber() == 14) {
				return true;
			}
		}
		return false;
	}

	private Card[] sortFromMaxToMinValue(List<Card> cards) {
		Card[] finalHand = new Card[cards.size()];
		Card[] cardArray = cards.toArray(new Card[cards.size()]);

		for (int i = 0; i < cardArray.length; i++) {
			int maxValue = 0;
			int index = 0;
			Card highestCard = null;
			for (int j = 0; j < cardArray.length; j++) {
				Card card = cardArray[j];
				if (card != null && card.getNumber() > maxValue) {
					maxValue = card.getNumber();
					highestCard = card;
					index = j;
				}
			}
			finalHand[i] = highestCard;
			cardArray[index] = null;
		}
		return finalHand;
	}

	private List<Card> filterByColor(List<Card> cards, ColorEnum flushColor) {
		List<Card> flushedCards = new ArrayList<>();
		for (Card card : cards) {
			if (flushColor.equals(card.getColor())) {
				flushedCards.add(card);
			}

		}
		return flushedCards;
	}

	public boolean isAnyoneStrongerThanMe(List<Card> myCards, List<Card> communityCards,
			List<List<Card>> otherCardsList) {

		List<Card> myHand = new ArrayList<>();
		myHand.addAll(myCards);
		myHand.addAll(communityCards);

		List<List<Card>> otherHands = new ArrayList<>();
		for (List<Card> otherCards : otherCardsList) {
			List<Card> list = new ArrayList<>();
			list.addAll(otherCards);
			list.addAll(communityCards);
			otherHands.add(list);
		}

		PokerHand myBestHand = findBestCombination(myHand);

		List<PokerHand> otherBestHandList = otherHands.stream().map(lc -> findBestCombination(lc))
				.collect(Collectors.toList());

		for (PokerHand otherPokerHand : otherBestHandList) {
			if (otherPokerHand.compareTo(myBestHand) > 0) {
				return true;
			}
		}

		return false;
	}

}
