package feaisil.raceforthegalaxy.card.power;

import feaisil.raceforthegalaxy.card.Card;
import feaisil.raceforthegalaxy.exception.InactiveGameException;
import feaisil.raceforthegalaxy.exception.PlayerNotInGameException;
import feaisil.raceforthegalaxy.game.Game;
import feaisil.raceforthegalaxy.game.Phase;
import feaisil.raceforthegalaxy.game.Player;

public final class GainVP extends Power {

	private int number;
	public GainVP() {
		super(Phase.consume);
		number = 1;
	}
	public GainVP(int iNumber) {
		super(Phase.consume);
		number = iNumber;
	}
	public GainVP(Phase iPhase, int iNumber) {
		super(iPhase);
		number = iNumber;
	}

	@Override
	public void trigger(Game iGame, Player iPlayer, Card iCard)
	{
		iGame.drawVP(iPlayer, number);
	}

}
