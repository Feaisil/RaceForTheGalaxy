package feaisil.raceforthegalaxy.card.power;

import feaisil.raceforthegalaxy.card.GoodType;
import feaisil.raceforthegalaxy.game.Phase;

public class ReduceSettleCost extends Power {
	private GoodType type;
	private int strength;
	
	public ReduceSettleCost(GoodType iType, int iStrength) {
		super(Phase.settle);
		type = iType;
		strength = iStrength;
	}

	public GoodType getType() {
		return type;
	}

	public int getStrength() {
		return strength;
	}
}
