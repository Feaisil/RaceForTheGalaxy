package feaisil.raceforthegalaxy.card.power;

import feaisil.raceforthegalaxy.card.Card;
import feaisil.raceforthegalaxy.exception.PlayerNotInGameException;
import feaisil.raceforthegalaxy.game.Game;
import feaisil.raceforthegalaxy.game.Phase;
import feaisil.raceforthegalaxy.game.Player;

public class ChooseActionLast extends Power {

	public ChooseActionLast() {
		super(Phase.selectAction);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void trigger(Game iGame, Player iPlayer, Card iCard)
	{
	}

}
