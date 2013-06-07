package feaisil.raceforthegalaxy.card.power;

import feaisil.raceforthegalaxy.card.GoodType;

public class Consume extends Power {
	private GoodType goodType;
	private int numberOfGood;
	
	public Consume(GoodType iGoodType, int iNumberOfGood) {
		
		this.goodType = iGoodType;
		numberOfGood = iNumberOfGood;
	}
	public Consume(GoodType iGoodType) {
		
		this.goodType = iGoodType;
		numberOfGood = 1;
	}
	
	public GoodType getGoodType() {
		return goodType;
	}
	public int getNumberOfGood() {
		return numberOfGood;
	}
  @Override
  public String toString()
  {
    return "Consume [goodType=" + goodType + ", numberOfGood=" + numberOfGood
        + "]";
  }
}
