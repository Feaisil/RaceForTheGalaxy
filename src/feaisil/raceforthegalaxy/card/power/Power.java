package feaisil.raceforthegalaxy.card.power;

import feaisil.raceforthegalaxy.card.Card;
import feaisil.raceforthegalaxy.exception.InactiveGameException;
import feaisil.raceforthegalaxy.exception.PlayerNotInGameException;
import feaisil.raceforthegalaxy.game.Game;
import feaisil.raceforthegalaxy.game.Phase;
import feaisil.raceforthegalaxy.game.Player;

public abstract class Power {	
	private Phase phase;
	
	public Power(Phase iPhase)
	{
		phase = iPhase;
	}
	
	public Phase getPhase()
	{
		return phase;
	}
	
	public abstract void trigger(Game iGame, Player iPlayer, Card iCard);
	
	@Override
	public String toString() {
		StringBuilder _builder = new StringBuilder();
		_builder.append("Power [");
		if (phase != null) {
			_builder.append("phase=");
			_builder.append(phase);
		}
		_builder.append("]");
		return _builder.toString();
	}
	
}
