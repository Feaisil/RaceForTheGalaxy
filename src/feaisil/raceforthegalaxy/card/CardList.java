package feaisil.raceforthegalaxy.card;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import feaisil.raceforthegalaxy.Expansion;
import feaisil.raceforthegalaxy.card.power.*;
import feaisil.raceforthegalaxy.card.victorypointbonus.EndGameBonus;
import feaisil.raceforthegalaxy.game.Action;
import feaisil.raceforthegalaxy.game.Phase;
import feaisil.raceforthegalaxy.game.PlayerColor;

public class CardList
{
  private boolean             initialized;
  private int                 id;
  private List<Card>          Deck;
  private List<Card>          StartingWorlds;
  private List<Card>          StartingBlueWorlds;
  private List<Card>          StartingRedWorlds;
  private List<Card>          actionCards;

  private Map<String, Action> actionName;

  public CardList()
  {
    initialized = false;
    id = 0;
    Deck = new ArrayList<Card>(300);
    StartingWorlds = new ArrayList<Card>(20);
    StartingBlueWorlds = new ArrayList<Card>(10);
    StartingRedWorlds = new ArrayList<Card>(10);
  }

  private void addCard(Card iCard)
  {
    Deck.add(iCard);
  }

  private void addStartingBlueWorld(Card iCard)
  {
    StartingWorlds.add(iCard);
    StartingBlueWorlds.add(iCard);
  }

  private void addStartingRedWorld(Card iCard)
  {
    StartingWorlds.add(iCard);
    StartingRedWorlds.add(iCard);
  }

  public List<Card> getDeck()
  {
    return Deck;
  }

  public List<Card> getStartingWorlds()
  {
    return StartingWorlds;
  }

  public List<Card> getStartingBlueWorlds()
  {
    return StartingBlueWorlds;
  }

  public List<Card> getStartingRedWorlds()
  {
    return StartingRedWorlds;
  }

  private void initActionName()
  {
    if (actionName == null)
    {
      actionName = new HashMap<String, Action>();
      actionName.put("Prestige/Search", Action.search);
      actionName.put("Draw explore", Action.exploreDraw);
      actionName.put("Keep explore", Action.exploreKeep);
      actionName.put("Develop", Action.develop);
      actionName.put("Settle", Action.settle);
      actionName.put("Consume trade", Action.trade);
      actionName.put("Consume 2x VPs", Action.consume);
      actionName.put("Produce", Action.produce);
    }
  }

  public List<Card> getActionCards(PlayerColor color)
  {
    // TODO return cards depending of color
    if (actionCards == null)
    {
      initActionName();
      TreeSet<Entry<String, Action>> actions = new TreeSet<Entry<String, Action>>(
          new Comparator<Entry<String, Action>>(){
            @Override
            public int compare(Entry<String, Action> arg0,
                Entry<String, Action> arg1)
            { 
              return arg0.getValue().ordinal() - arg1.getValue().ordinal();
            }
          });
      actions.addAll(actionName.entrySet());
      
      actionCards = new ArrayList<Card>(9);
      
      // action cards
      for (Entry<String, Action> act : actions)
        actionCards.add(new Card(id++, act.getKey(), "action" + id, 0, 0, false, null,
            null, null));
    }
    return actionCards;
  }

  public Action getAction(String name)
  {
    initActionName();
    return actionName.get(name);
  }

  public void initFromCsv(InputStream iIs, Set<Expansion> expansion)
      throws FileNotFoundException
  {
    if (initialized)
      return;
    Scanner scanner = new Scanner(iIs);
    try
    {
      while (scanner.hasNextLine())
      {
        try
        {
          processLineFromCsv(scanner.nextLine(), expansion);
        } catch (Exception e)
        {
          e.printStackTrace();
        }
      }
    } finally
    {
      scanner.close();
    }
    initialized = true;
  }

  private GoodType getGoodType(String str)
  {
    if (str.contains("Novelty"))
      return GoodType.Novelty;
    if (str.contains("Alien"))
      return GoodType.Alien;
    if (str.contains("Rare"))
      return GoodType.Rare;
    if (str.contains("Genes"))
      return GoodType.Gene;
    if (str.contains("(any)"))
      return GoodType.Any;
    return GoodType.None;
  }

  private void processLineFromCsv(String nextLine, Set<Expansion> expansion)
  {
    String[] values = nextLine.split(";");

    if (values[0].equals("Name "))
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

    switch (values.length)
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
       powers.addAll(parseEndGameAndOtherPowers(values[19]));
      case 19:
        powers.addAll(parseProducePowers(values[18]));
      case 18:
        powers.addAll(parseConsumePowers(values[17]));
      case 17:
        powers.addAll(parseTradePowers(values[16]));
      case 16:
        powers.addAll(parseSettlePowers(values[15]));
      case 15:
        powers.addAll(parseDevelopPowers(values[14]));
      case 14:
        powers.addAll(parseExplorePowers(values[13]));
      case 13:
        // is prestige
        prestige = (values[12].equals("©"));
      case 12:
        // get vp
        if (!values[11].equals("*") && !values[11].isEmpty())
          vps = Integer.parseInt(values[11]);
      case 11:
        // get cost
        if (!values[10].isEmpty())
          cost = Integer.parseInt(values[10]);
      case 10:
        goodType = getGoodType(values[9]);
      case 9:
        isProduction = (values[8].equals("Production"));
      case 8:
        isStartHand = !values[7].isEmpty();
      case 7:
        if (!values[6].isEmpty())
        {
          isStartBlue = (Integer.parseInt(values[6]) % 2 == 0);
          isStartRed = !isStartBlue;
        }
      case 6:
        for (String aKeyword : values[5].split(" "))
        {
          if (aKeyword.contains("Alien"))
          {
            keywords.add(Keyword.Alien);
            continue;
          }
          if (aKeyword.contains("Uplift"))
          {
            keywords.add(Keyword.Uplift);
            continue;
          }
          if (aKeyword.contains("?"))
          {
            keywords.add(Keyword.Gene);
            continue;
          }
          if (aKeyword.contains("Terraforming"))
          {
            keywords.add(Keyword.Terraforming);
            continue;
          }
          if (aKeyword.contains("Rebel"))
          {
            keywords.add(Keyword.Rebel);
            continue;
          }
        }
      case 5:
        if (values[4].equals("Development"))
          ; // Default
        if (values[4].equals("Military World"))
        {
          isWorld = true;
          isMilitary = true;
        }
        if (values[4].equals("Non-Military World"))
          isWorld = true;
      case 4:
        quantity = Integer.parseInt(values[3]);
      case 3:
        if (values[2].equals("Base"))
          exp = Expansion.BaseGame;
        if (values[2].equals("BoW"))
          exp = Expansion.TheBrinkOfWard;
        if (values[2].equals("GS"))
          exp = Expansion.TheGatheringStorm;
        if (values[2].equals("RvI"))
          exp = Expansion.RebelVsImperium;
        if (!expansion.contains(exp))
          return;
      case 2:
        graphicId = values[1];
        for (; graphicId.length() < 3;)
          graphicId = "0" + graphicId;
      case 1:
        name = values[0];
      default:
        break;
    }
    for (int i = 0; i < quantity; i++)
    {
      Card aCard;
      if (isWorld)
      {
        aCard = new World(id++, name, graphicId, cost, vps, prestige, goodType,
            isMilitary, isProduction, keywords, powers, endGameBonuses);
        if (isStartBlue)
          addStartingBlueWorld(aCard);
        else if (isStartRed)
          addStartingRedWorld(aCard);
        else
          addCard(aCard);
      } else
      {
        aCard = new Development(id++, name, graphicId, cost, vps, prestige,
            keywords, powers, endGameBonuses);
        addCard(aCard);
      }
    }
  }

  private Power parseCommon(String powerString, Phase phase )
  {
    Pattern drawP = Pattern.compile("Draw (\\d+) at start of phase", Pattern.CASE_INSENSITIVE);
    Matcher drawM = drawP.matcher(powerString);
    if (drawM.matches())
    {
      return new DrawCard(phase, Integer.parseInt(drawM.group(1)));
    }
    
    return null;
  }
  private List<Power> parseExplorePowers(String string)
  {
    Pattern drawP = Pattern.compile("Draw +(\\d+)", Pattern.CASE_INSENSITIVE);
    Pattern keepP = Pattern.compile("keep +(\\d+)", Pattern.CASE_INSENSITIVE);
    Pattern prestForCardP = Pattern.compile("(\\d+) Prestige for (\\d+) card", Pattern.CASE_INSENSITIVE);
    
    List<Power> powers = new ArrayList<Power>();
    for (String powerString : string.split("\\|"))
    {
      Power power = parseCommon(powerString, Phase.explore);
      if(power != null)
      {
        powers.add(power);
        continue;
      }

      Matcher drawM = drawP.matcher(powerString);
      if (drawM.matches())
      {
        powers.add(new ExploreDrawMoreCards(Integer.parseInt(drawM.group(1))));
        continue;
      }

      Matcher keepM = keepP.matcher(powerString);
      if (keepM.matches())
      {
        powers.add(new ExploreKeepMoreCards(Integer.parseInt(keepM.group(1))));
        continue;
      }
      
      if (powerString.equals("Combine draws with hand"))
      {
        powers.add(new CombineDrawsWithHand());
        continue;
      }
      
      Matcher prestForCardM = prestForCardP.matcher(powerString);
      if(prestForCardM.matches())
      {
        // TODO create power
      }
      
    }
    return powers;
  }

  private List<Power> parseDevelopPowers(String string)
  {
    Pattern reduceCostP = Pattern.compile("Cost -(\\d+)", Pattern.CASE_INSENSITIVE);
    
    List<Power> powers = new ArrayList<Power>();
    for (String powerString : string.split("\\|"))
    {
      Power power = parseCommon(powerString, Phase.develop);
      if(power != null)
      {
        powers.add(power);
        continue;
      }

      Matcher reduceCostM = reduceCostP.matcher(powerString);
      if (reduceCostM.matches())
      {
        powers.add(new ReduceDevelopCost(Integer.parseInt(reduceCostM.group(1))));
        continue;
      }
      // TODO Draw n then discard n at start of phase
      // TODO Discard GOOD for cost -n
      // TODO 1 Prestige after Developing 6 cost
      // TODO 1 PRestige after developing rebel
      // TODO 1 Prestige after developing
      // TODO discard (from hand) for cost -n
      // TODO save 1 card from payment under this card
    }
    return powers;
  }

  private List<Power> parseSettlePowers(String string)
  {
    List<Power> powers = new ArrayList<Power>();
    for (String substr : string.split("\\|"))
    {
      Power power = parseCommon(substr, Phase.settle);
      if(power != null)
      {
        powers.add(power);
        continue;
      }
      // TODO TYPE cost -n
      // TODO 1 prestige for military +n
      // TODO 1 Prestige for settling (type) world
      // TODO Alien cost = defense
      // TODO military +n
      // TODO TYPE military +n
      // TODO Discard for cost=0 (not alien)
      // TODO Draw n after settling
      // TODO save 1 card from payment
      // TODO Discard TYPE for cost -n
      // TODO Place good on production worlds zhen settled
      // TODO Military +n per military world
      // TODO Discard for defense = cost-2 (87)
    }
    return powers;
  }

  private List<Power> parseTradePowers(String string)
  {
    List<Power> powers = new ArrayList<Power>();
    for (String substr : string.split("\\|"))
    {
      // TODO
    }
    return powers;
  }

  private List<Power> parseConsumePowers(String string)
  {
    List<Power> powers = new ArrayList<Power>();
    for (String substr : string.split("\\|"))
    {
      Power power = parseCommon(substr, Phase.consume);
      if(power != null)
      {
        powers.add(power);
        continue;
      }
      // TODO
    }
    return powers;
  }

  private List<Power> parseProducePowers(String iString)
  {
    List<Power> powers = new ArrayList<Power>();
    for (String substr : iString.split("\\|"))
    {
      Power power = parseCommon(substr, Phase.produce);
      if(power != null)
      {
        powers.add(power);
        continue;
      }
      // TODO
      
    }
    return powers;
  }

  private List<Power>  parseEndGameAndOtherPowers(String string)
  {
    List<Power> powers = new ArrayList<Power>();
    for (String substr : string.split("\\|"))
    {
      if (substr.equals("[You end the game when you reach 14 cards in tableau]"))
      {
        powers.add(new IncreaseBoardLimit());
        continue;
      }

      if (substr.equals("[May keep up to 12 cards in hand at end of turn]"))
      {
        powers.add(new IncreaseHandLimit());
        continue;
      }

      if (substr.equals("[at round start, select your action after other players reveal theirs]"))
      {
        powers.add(new ChooseActionLast());
        continue;
      }
      
      // TODO
    }
    return powers;
  }
}