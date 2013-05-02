package feaisil.raceforthegalaxy.card.power;

import java.util.ArrayList;
import java.util.List;

import feaisil.raceforthegalaxy.card.Card;
import feaisil.raceforthegalaxy.card.GoodType;
import feaisil.raceforthegalaxy.game.Game;
import feaisil.raceforthegalaxy.game.Phase;
import feaisil.raceforthegalaxy.game.Player;

public class Consume extends Power {
	private GoodType goodType;
	private int numberOfGood;
	private List<Power>	 targets; // VERIFY TODO
	
	public Consume(GoodType iGoodType, int iNumberOfGood) {
		super(Phase.consume);
		
		targets = new ArrayList<Power>();
		
		this.goodType = iGoodType;
		numberOfGood = iNumberOfGood;
	}
	public Consume(GoodType iGoodType) {
		super(Phase.consume);
		
		targets = new ArrayList<Power>();
		
		this.goodType = iGoodType;
		numberOfGood = 1;
	}

	public final void addTarget(Power iTarget)
	{
		targets.add(iTarget);
	}
	
	@Override
	public void trigger(Game iGame, Player iPlayer, Card iCard) {
		// TODO Auto-generated method stub

	}

}
