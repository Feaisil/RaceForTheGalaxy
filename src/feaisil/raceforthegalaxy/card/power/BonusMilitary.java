package feaisil.raceforthegalaxy.card.power;

import feaisil.raceforthegalaxy.card.GoodType;
import feaisil.raceforthegalaxy.game.Phase;

public final class BonusMilitary extends Power {
	private GoodType type;
	private int strength;
	
	public BonusMilitary(GoodType iType, int iStrength) {
		super(Phase.settle);
		
		type = iType;
		strength = iStrength;
	}
	public BonusMilitary( int iStrength) {
		super(Phase.settle);
		
		type = GoodType.Any;
		strength = iStrength;
	}


	public GoodType getType() {
		return type;
	}

	public int getStrength() {
		return strength;
	}
}
