package feaisil.raceforthegalaxy.card.power;

import feaisil.raceforthegalaxy.card.GoodType;
import feaisil.raceforthegalaxy.game.Phase;

public class Consume extends Power {
	private GoodType goodType;
	private int numberOfGood;
	
	public Consume(GoodType iGoodType, int iNumberOfGood) {
		super(Phase.consume);
		
		this.goodType = iGoodType;
		numberOfGood = iNumberOfGood;
	}
	public Consume(GoodType iGoodType) {
		super(Phase.consume);
		
		this.goodType = iGoodType;
		numberOfGood = 1;
	}
	
	public GoodType getGoodType() {
		return goodType;
	}
	public int getNumberOfGood() {
		return numberOfGood;
	}
}
