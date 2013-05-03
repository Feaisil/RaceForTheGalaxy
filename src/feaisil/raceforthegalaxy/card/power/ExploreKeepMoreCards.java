package feaisil.raceforthegalaxy.card.power;

import feaisil.raceforthegalaxy.game.Phase;

public final class ExploreKeepMoreCards extends Power {
	private int strength;
	
	public ExploreKeepMoreCards(int iStrength) {
		super(Phase.explore);
		
		strength = iStrength;
	}

	public int getStrength()
	{
		return strength;
	}
}
