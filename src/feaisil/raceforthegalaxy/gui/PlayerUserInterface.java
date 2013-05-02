package feaisil.raceforthegalaxy.gui;

import java.util.List;

import feaisil.raceforthegalaxy.card.Card;
import feaisil.raceforthegalaxy.game.Player;

public interface PlayerUserInterface {
	public List<Card> query(Player p, QueryType query, List<Card> cards, int minCards,
			int maxCards);

	public void sendWarning(Player p, Warning warning);
}
