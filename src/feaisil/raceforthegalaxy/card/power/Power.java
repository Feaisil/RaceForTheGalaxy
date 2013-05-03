package feaisil.raceforthegalaxy.card.power;

import feaisil.raceforthegalaxy.game.Phase;

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
}
