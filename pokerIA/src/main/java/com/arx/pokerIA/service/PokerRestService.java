package com.arx.pokerIA.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.arx.pokerIA.model.GameDTO;
import com.arx.pokerIA.model.PlayerDTO;

@Component

/**
 * fait une interface entre l'API pokerOnline coté serveur et le client pokerIa.
 * Pour chaque web service dans l'API, une méthode sera créer ici. PokerIa
 * appelera une méthode de ce service des qu'il voudra appeller un web service
 * dur serveur PokerOnline. Pour cela on utilisera l'objet restTemplate, dont le
 * role est de faciliter les appels http. nottement, il fait la convertion objet
 * java vers json et vice versa une méthode get ne peux avoir que des path param
 * et des query param, pas de body un body contiens soit des forms data soit un
 * objet json
 * 
 * @author ozann
 *
 */
public class PokerRestService {

	private RestTemplate restTemplate = new RestTemplate();

	@Value("${pokerserver.adress}")
	private String serverAdress;

	/**
	 * en paramètre ; le nombre de joueur attendus en form data
	 * 
	 * @return
	 */
	public GameDTO createGame(int nbOfPlayer) {
		// passer les forms data
		MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
		map.add("nbOfPlayer", nbOfPlayer);
		return restTemplate.postForObject(serverAdress + "/games", map, GameDTO.class);
	}

	public PlayerDTO addPlayerToExistingGame(int gameId, String name) {
		PlayerDTO player = new PlayerDTO();
		player.setName(name);
		return restTemplate.postForObject(serverAdress + "/games/" + gameId + "/player", player, PlayerDTO.class);
	}

	public GameDTO getGame(int gameId, int playerId) {
		return restTemplate.getForObject(serverAdress + "/games/" + gameId + "?playerId=" + playerId, GameDTO.class);
	}

	public void play(int gameId, int playerId, ActionEnum action) {
		// passer les forms data
		MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
		map.add("playerId", playerId);
		map.add("playerAction", action.toString());
		restTemplate.postForObject(serverAdress + "/games/" + gameId + "/play", map, Void.class);
	}

	public List<GameDTO> listGames() {
		ResponseEntity<List<GameDTO>> result = restTemplate.exchange(serverAdress + "/games", HttpMethod.GET, null,
				new ParameterizedTypeReference<List<GameDTO>>() {
				});

		return result.getBody();
	}
}
