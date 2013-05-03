package feaisil.raceforthegalaxy.card.power;

import feaisil.raceforthegalaxy.card.GoodType;
import feaisil.raceforthegalaxy.game.Phase;

public final class TradeBonus extends Power {

	private GoodType goodType;
	private int bonusValue;
	
	public TradeBonus(GoodType iGoodType, int iBonusValue) {
		super(Phase.consume);
		goodType = iGoodType;
		bonusValue = iBonusValue;
	}

	public GoodType getGoodType() {
		return goodType;
	}

	public int getBonusValue() {
		return bonusValue;
	}
}
