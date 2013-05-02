package feaisil.raceforthegalaxy.card.power;

import feaisil.raceforthegalaxy.card.Card;
import feaisil.raceforthegalaxy.card.GoodType;
import feaisil.raceforthegalaxy.exception.InactiveGameException;
import feaisil.raceforthegalaxy.game.Game;
import feaisil.raceforthegalaxy.game.Phase;
import feaisil.raceforthegalaxy.game.Player;

public final class TradeBonus extends Power {

	private GoodType goodType;
	private int bonusValue;
	
	public TradeBonus(GoodType iGoodType, int iBonusValue) {
		super(Phase.consume);
		goodType = iGoodType;
		bonusValue = iBonusValue;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void trigger(Game iGame, Player iPlayer, Card iCard)
	{
		for(int i=0; i<bonusValue; i++)
			iPlayer.addToHand(iGame.drawCard());
	}

}
