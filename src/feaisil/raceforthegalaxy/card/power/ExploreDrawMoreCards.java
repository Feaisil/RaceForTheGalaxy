package feaisil.raceforthegalaxy.card.power;

import feaisil.raceforthegalaxy.game.Phase;

public final class ExploreDrawMoreCards extends Power {
	private int strength;
	
	public ExploreDrawMoreCards(int iStrength) {
		super(Phase.explore);
		
		strength = iStrength;
	}

	public int getStrength()
	{
		return strength;
	}
}
