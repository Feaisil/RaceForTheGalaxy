package feaisil.raceforthegalaxy.card;

import java.util.List;

import feaisil.raceforthegalaxy.card.power.Power;
import feaisil.raceforthegalaxy.card.victorypointbonus.EndGameBonus;

public class Development extends Card {

	public Development(
			int id, 
			String iName,
			String iGraphicId,
			int iCost, 
			int iVictoryPoints, 
			boolean iPrestige,
			List<Keyword> iKeywords,
			List<Power> iPowers,
			List<EndGameBonus> iEgb) {
		super(id, iName, iGraphicId, iCost, iVictoryPoints, iPrestige, iKeywords, iPowers, iEgb);
	}

}
