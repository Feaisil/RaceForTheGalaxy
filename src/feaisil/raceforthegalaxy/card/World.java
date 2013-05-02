package feaisil.raceforthegalaxy.card;

import java.util.List;

import feaisil.raceforthegalaxy.card.power.Power;
import feaisil.raceforthegalaxy.card.victorypointbonus.EndGameBonus;

public class World extends Card{
	
	private Card good;
	private GoodType goodType;

	private boolean military;
	private boolean production;
	
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
		
		goodType = iGoodType;
		good = null;
		military = iMilitary;
		production = iProduction;
	}

	public GoodType getGoodType() {
		return goodType;
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
		return military;
	}

	public boolean isProduction() {
		return production;
	}
}
