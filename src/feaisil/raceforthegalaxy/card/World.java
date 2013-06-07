package feaisil.raceforthegalaxy.card;

import java.util.List;

import feaisil.raceforthegalaxy.card.power.Power;
import feaisil.raceforthegalaxy.card.victorypointbonus.EndGameBonus;

public class World extends Card{
	static public class WorldType
	{
		public WorldType(GoodType iGoodType, boolean iMilitary,
				boolean iProduction) {
			goodType = iGoodType;
			military = iMilitary;
			production = iProduction;
		}
		GoodType goodType;
		boolean military;
		boolean production;
          @Override
          public String toString()
          {
            return "WorldType [goodType=" + goodType + ", military=" + military
                + ", production=" + production + "]";
          }
	}
	private Card good;
	private WorldType worldType;
	
	public World(
			int id,
			String iName,
			String iGraphicId,
			int iCost, 
			int iVictoryPoints,
			boolean iPrestige,
			GoodType iGoodType,
			boolean iMilitary,
			boolean iProduction,
			List<Keyword> iKeywords,
			List<Power> iPowers,
			List<EndGameBonus> iEgb) {
		super(id, iName, iGraphicId, iCost, iVictoryPoints, iPrestige, iKeywords, iPowers, iEgb);
		worldType = new WorldType(iGoodType, iMilitary, iProduction);
		good = null;
	}

	public GoodType getGoodType() {
		return worldType.goodType;
	}
	public void consumeGood()
	{
		good = null;
	}
	public boolean hasGood()
	{
		return good != null;
	}
	public void setGood(Card iCard)
	{
		good = iCard;
	}
	public boolean isMilitary() {
		return worldType.military;
	}

	public boolean isProduction() {
		return worldType.production;
	}
	
	public WorldType getWorldType()
	{
		return worldType;
	}

  @Override
  public String toString()
  {
    return "World [good=" + good + ", worldType=" + worldType + "] " + super.toString();
  }
}
