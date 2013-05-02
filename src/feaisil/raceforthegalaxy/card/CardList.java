package feaisil.raceforthegalaxy.card;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import feaisil.raceforthegalaxy.Expansion;
import feaisil.raceforthegalaxy.card.power.BonusMilitary;
import feaisil.raceforthegalaxy.card.power.IncreaseBoardLimit;
import feaisil.raceforthegalaxy.card.power.Power;
import feaisil.raceforthegalaxy.card.victorypointbonus.EndGameBonus;
import feaisil.raceforthegalaxy.game.Action;
import feaisil.raceforthegalaxy.game.PlayerColor;

public class CardList {
	private boolean initialized;
	private int id;
	private List<Card> Deck;
	private List<Card> StartingWorlds;
	private List<Card> StartingBlueWorlds;
	private List<Card> StartingRedWorlds;
	private List<Card> actionCards;
	
	private Map<String, Action> actionName;
	
	public CardList()
	{
		initialized = false;
		id = 0;
		Deck = new ArrayList<Card>(300);
		StartingWorlds = new ArrayList<Card>(300);
		StartingBlueWorlds = new ArrayList<Card>(10);
		StartingRedWorlds = new ArrayList<Card>(10);
	}
	
	private void addCard(Card iCard)
	{
		Deck.add(iCard);
	}
	private  void addStartingBlueWorld(Card iCard)
	{
		StartingWorlds.add(iCard);
		StartingBlueWorlds.add(iCard);
	}
	private void addStartingRedWorld(Card iCard)
	{
		StartingWorlds.add(iCard);
		StartingRedWorlds.add(iCard);
	}
	public List<Card> getDeck() {
		return Deck;
	}

	public List<Card> getStartingWorlds() {
		return StartingWorlds;
	}

	public List<Card> getStartingBlueWorlds() {
		return StartingBlueWorlds;
	}

	public List<Card> getStartingRedWorlds() {
		return StartingRedWorlds;
	}
	
	private void initActionName(){
		if(actionName == null){
			actionName = new HashMap<String, Action>();
			actionName.put("Draw explore",Action.exploreDraw);
			actionName.put("Keep explore", Action.exploreKeep);
			actionName.put("Develop", Action.develop);
			actionName.put("Settle", Action.settle);
			actionName.put("Consume trade", Action.trade);
			actionName.put("Consume 2x VPs", Action.consume);
			actionName.put("Produce", Action.produce);
			actionName.put("Prestige/Search", Action.search);
		}
	}
	public List<Card> getActionCards(PlayerColor color) {
		// TODO return cards depending of color
		if(actionCards == null){
			initActionName();
			actionCards = new ArrayList<Card>();

			// action cards
			for(String str: actionName.keySet())
				actionCards.add(
					new Card(
							id++,
							str,
							"action"+id,
							0,0,false,null,null,null));
		}
		return actionCards;
	}	
	public Action getAction(String name){
		initActionName();
		return actionName.get(name);
	}

	public void initFromCsv(InputStream iIs, Set<Expansion> expansion) throws FileNotFoundException
	{
		if(initialized) return;
		Scanner scanner = new Scanner(iIs);
	    try {
	      while ( scanner.hasNextLine() ){
	    	  try{
	    		  processLineFromCsv( scanner.nextLine() , expansion);
	    	  }
	    	  catch(Exception e)
	    	  {
	    		  e.printStackTrace();
	    	  }
	      }
	    }
	    finally {
	    	scanner.close();
	    }
	    initialized = true;
	}
	private GoodType getGoodType(String str)
	{
		if(str.contains("Novelty"))
			return GoodType.Novelty;
		if(str.contains("Alien"))
			return GoodType.Alien;
		if(str.contains("Rare"))
			return GoodType.Rare;
		if(str.contains("Genes"))
			return GoodType.Gene;
		if(str.contains("(any)"))
			return GoodType.Any;
		return GoodType.None;
	}
	private void processLineFromCsv(String nextLine, Set<Expansion> expansion) {
		String[] values = nextLine.split(";");
		
		if(values[0].equals("Name "))
			return;

		List<EndGameBonus> endGameBonuses = new ArrayList<EndGameBonus>();
		List<Power> powers = new ArrayList<Power>();
		List<Keyword> keywords = new ArrayList<Keyword>();
		
		int vps = 0;
		int cost = 0;
		int quantity = 0;
		
		GoodType goodType = GoodType.None;
		Expansion exp = Expansion.BaseGame;
		
		boolean prestige = false;
		boolean isStartRed = false;
		boolean isStartBlue = false;
		boolean isProduction = false;
		boolean isWorld = false;
		boolean isMilitary = false;
		boolean isStartHand = false;
		
		String name = "";
		String graphicId = "";
		
		switch(values.length)
		{
		case 39:
		case 38:
		case 37:
		case 36:
		case 35:
		case 34:
		case 33:
		case 32:
		case 31:
		case 30:
		case 29:
		case 28:
		case 27:
		case 26:
		case 25:
		case 24:
		case 23:
		case 22:
		case 21:
		case 20:
			for(String aStr: values[19].split(","))
			{
				// TODO game end bonus
			}
		case 19:
			for(String aStr: values[18].split(","))
			{
				powers.addAll(parseProducePowers(aStr));
			}
		case 18:
			for(String aStr: values[17].split(","))
			{
				// TODO consume powers
			}
		case 17:
			for(String aStr: values[16].split(","))
			{
				// TODO consume trade powers
			}
		case 16:
			for(String aStr: values[15].split(","))
			{
				// TODO settle powers
			}
		case 15:
			for(String aStr: values[14].split(","))
			{
				// TODO develop powers
			}
		case 14:
			for(String aStr: values[13].split(","))
			{
				// TODO explore powers
			}
		case 13:
			prestige = (values[12].equals("©"));
		case 12:
			if(!values[11].equals("*") && !values[11].isEmpty())
				vps = Integer.parseInt(values[11]);
		case 11:
			if(!values[10].isEmpty())
				cost = Integer.parseInt(values[10]);
		case 10:
			goodType = getGoodType(values[9]);
		case 9:
			isProduction = (values[8] == "Production");
		case 8:
			isStartHand = !values[7].isEmpty();
		case 7:
			if(!values[6].isEmpty())
			{
				isStartBlue = (Integer.parseInt(values[6])%2 == 0);
				isStartRed = !isStartBlue;
			}
		case 6:
			for(String aKeyword: values[5].split(" "))
			{
				if(aKeyword.contains("Alien"))
				{
					keywords.add(Keyword.Alien);
					continue;
				}
				if(aKeyword.contains("Uplift"))
				{
					keywords.add(Keyword.Uplift);
					continue;
				}
				if(aKeyword.contains("?"))
				{
					keywords.add(Keyword.Gene);
					continue;
				}
				if(aKeyword.contains("Terraforming"))
				{
					keywords.add(Keyword.Terraforming);
					continue;
				}
				if(aKeyword.contains("Rebel"))
				{
					keywords.add(Keyword.Rebel);
					continue;
				}
			}
		case 5:
			if(values[4].equals("Development"))
				; // Default
			if(values[4].equals("Military World"))
			{
				isWorld = true;
				isMilitary = true;
			}
			if(values[4].equals("Non-Military World"))
				isWorld = true;
		case 4:
			quantity = Integer.parseInt(values[3]);
		case 3:
			if(values[2].equals("Base"))
				exp = Expansion.BaseGame;
			if(values[2].equals("BoW"))
				exp = Expansion.TheBrinkOfWard;
			if(values[2].equals("GS"))
				exp = Expansion.TheGatheringStorm;
			if(values[2].equals("RvI"))
				exp = Expansion.RebelVsImperium;
			if(!expansion.contains(exp))
				return;
		case 2:
			graphicId = values[1];
			for(;graphicId.length()<3;)
				graphicId = "0"+graphicId;
		case 1:
			name = values[0];
		default:
			break;
		}
		for(int i=0; i<quantity; i++)
		{
			Card aCard;
			if(isWorld)
			{
				 aCard = new World(
						 id++,
						 name,
						 graphicId,
						 cost,
						 vps,
						 prestige,
						 goodType,
						 isMilitary,
						 isProduction,
						 keywords,
						 powers,
						 endGameBonuses);
				 if(isStartBlue)
					 addStartingBlueWorld(aCard);
				 else if (isStartRed)
					 addStartingRedWorld(aCard);
				 else
					 addCard(aCard);
			}
			else
			{
				aCard = new Development(
						id++,
						name,
						graphicId,
						cost,
						vps,
						prestige,
						keywords,
						powers,
						endGameBonuses);
				 addCard(aCard);
			}
		}
	}

	private List<Power> parseProducePowers(String iString) {
		List<Power> powers = new ArrayList<Power>();
		for(String substr: iString.split("|"))
		{
			if(substr.equals("[You end the game when you reach 14 cards in tableau]"))
				powers.add(new IncreaseBoardLimit());
		}
		return powers;
	}
}