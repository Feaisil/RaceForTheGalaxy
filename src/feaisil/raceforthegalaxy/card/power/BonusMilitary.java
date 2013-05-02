package feaisil.raceforthegalaxy.card.power;

import feaisil.raceforthegalaxy.card.Card;
import feaisil.raceforthegalaxy.card.GoodType;
import feaisil.raceforthegalaxy.game.Game;
import feaisil.raceforthegalaxy.game.Phase;
import feaisil.raceforthegalaxy.game.Player;

public final class BonusMilitary extends Power {
	private GoodType type;
	private int strength;
	
	public BonusMilitary(GoodType iType, int iStrength) {
		super(Phase.settle);
		
		type = iType;
		strength = iStrength;
	}

	@Override
	public void trigger(Game iGame, Player iPlayer, Card iCard) {
		// TODO Auto-generated method stub

	}

}
