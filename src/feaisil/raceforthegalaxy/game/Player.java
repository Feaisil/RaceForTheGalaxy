package feaisil.raceforthegalaxy.game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import feaisil.raceforthegalaxy.card.*;
import feaisil.raceforthegalaxy.card.power.*;
import feaisil.raceforthegalaxy.exception.*;
import feaisil.raceforthegalaxy.gui.PlayerUserInterface;
import feaisil.raceforthegalaxy.gui.QueryType;
import feaisil.raceforthegalaxy.gui.Warning;

public class Player implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 0L;

  protected String clientId;
  private Game game;
  private boolean isPhaseExecuted;

  protected int victoryPoints;
  protected int prestigePoints;
  protected List<Card> hand;
  protected List<Card> board;
  protected List<Card> startingWorlds;

  protected Action actionChosen;
  protected Action secondActionChosen; //< for advanced games

  protected PlayerColor color;
  protected boolean prestigeActionUsed;
  protected boolean isPrestige;

  public PlayerUserInterface ui;

  public Player(Game iGame, String clientId, PlayerUserInterface iUi) throws TooManyPlayersException, ActiveGameException
  {
    game = iGame;
    this.clientId = clientId;
    ui = iUi;
    iGame.addPlayer(this);
    switch(iGame.getPlayers().size())
    {
      case 1: color = PlayerColor.Blue; break;
      case 2: color = PlayerColor.Green; break;
      case 3: color = PlayerColor.LightBlue; break;
      case 4: color = PlayerColor.Pink; break;
      case 5: color = PlayerColor.Red; break;
      case 6: 
      default: color = PlayerColor.Yellow; break;
    }

    hand = new ArrayList<Card>(20);
    board = new ArrayList<Card>(14);
    startingWorlds = new ArrayList<Card>(2);


  }

  //  STARTING
  protected void executeStartingPhase() {
    List<Card> reply = new ArrayList<Card>(1);
    // Choose one world
    if(startingWorlds.size() > 1)
      reply = queryUi(
          QueryType.startingPhaseChooseWorld,
          startingWorlds,
          1,1);
    else
      reply = startingWorlds;

    addToBoard(reply.get(0));

    //Remove the world not chosen
    startingWorlds.removeAll(reply);
    game.discardPile.addAll(startingWorlds);

    // TODO for some starting worlds, discard one more card... 
    reply = queryUi(
        QueryType.startingPhaseDiscardHand,
        hand,
        2,2);

    hand.removeAll(reply);
    game.discardPile.addAll(reply);
  }

  //  SELECT 
  protected void executeSelectAction() {
    List<Card> reply = queryUi(
        QueryType.chooseAction,
        game.cardList.getActionCards(getColor()),
        1,2);

    //Search case
    if(		game.cardList.getAction(reply.get(0).getName()) == Action.search)
    {
      if(!prestigeActionUsed){
        isPrestige = true;
        prestigeActionUsed = true;
      } else {
        // Not good...
        ui.sendWarning(this, Warning.PrestigeActionAlreadyUsed);
        executeSelectAction();
        return;
      }
    }

    //Prestige case
    if(reply.size() == 2)
    {
      if(		game.cardList.getAction(reply.get(0).getName()).equals(Action.search) ||
          game.cardList.getAction(reply.get(1).getName()).equals(Action.search))
      {
        if(!prestigeActionUsed)
        {
          if( prestigePoints > 0)
          {
            prestigePoints --;
          }
          else
          {
            // Not good...
            ui.sendWarning(this, Warning.NoPrestigePointsAvailable);
            executeSelectAction();
            return;
          }

          isPrestige = true;
          prestigeActionUsed = true;
        } else 
        {
          // Not good...
          ui.sendWarning(this, Warning.PrestigeActionAlreadyUsed);
          executeSelectAction();
          return;
        }
      }
      else
      {
        // Not good...
        ui.sendWarning(this, Warning.TwoActionSelectedButNotPrestige);
        executeSelectAction();
        return;
      }
    }

    // Obtain real action
    if(reply.size() == 2)
    {
      if(game.cardList.getAction(reply.get(0).getName()) == Action.search)
      {
        actionChosen = game.cardList.getAction(reply.get(1).getName());
      }
      else
      {
        actionChosen = game.cardList.getAction(reply.get(0).getName());
      }
    }
    else 
    {
      actionChosen = game.cardList.getAction(reply.get(0).getName());
    }
  }

  //	SEARCH
  protected void executeSearch() {
    // TODO Auto-generated method stub

  }

  //	EXPLORE
  protected void executeExplore() {
    processDrawCardPowers(Phase.explore);

    int numberOfCardToDraw = 2;
    int numberOfCardToKeep = 1;
    List<Card> drawnCards = new ArrayList<Card>();

    if(actionChosen == Action.exploreKeep)
    {
      numberOfCardToDraw += 1;
      numberOfCardToKeep += 1;
    }
    else if(actionChosen == Action.exploreDraw)
    {
      numberOfCardToDraw += 5;
    }
    for(ExploreDrawMoreCards power: getPowers(ExploreDrawMoreCards.class))
    {
      numberOfCardToDraw += (power).getStrength();
    }
    for(ExploreKeepMoreCards power: getPowers(ExploreKeepMoreCards.class))
    {
      numberOfCardToKeep += (power).getStrength();
    }
    for(int i=0; i<numberOfCardToDraw; i++)
      drawnCards.add(game.drawCard());

    drawnCards.removeAll(discardCards(drawnCards, numberOfCardToDraw - numberOfCardToKeep, QueryType.exploreDiscard));

    hand.addAll(drawnCards);
  }

  //	DEVELOP
  protected void executeDevelop()
  {
    processDrawCardPowers(Phase.develop);

    List<Card> developments = new ArrayList<Card>(12);
    int maxReducedCost = 0, fixedReducedCost = 0;

    if(getActionChosen() == Action.develop)
    {
      fixedReducedCost += 1;
      if(isPrestige())
      {
        fixedReducedCost += 2;
      }
    }		

    for(ReduceDevelopCost power: getPowers(ReduceDevelopCost.class))
    {
      fixedReducedCost += power.getStrength();
    }
    maxReducedCost += fixedReducedCost;
    // TODO search for powers with cost

    for(Card card: hand)
    {
      if(card instanceof Development)
      {
        if(((Development)(card)).getCost() - maxReducedCost < hand.size())
        {
          if(!board.contains(card))
            developments.add(card);
        }
      }
    }

    List<Card> reply = queryUi(
        QueryType.developChoose,
        developments,
        0,1);

    if(reply.size() == 1)
    {
      Development toDevelop = (Development)(reply.get(0));
      hand.remove(toDevelop);
      int remainingCost = (toDevelop.getCost() - fixedReducedCost);

      // TODO Select board cards with the powers and propose to user

      if(remainingCost > 0)
      {
        hand.removeAll(discardCards(hand, remainingCost, QueryType.developDiscard));
      }

      addToBoard(toDevelop);
      
      // Draw card if specific powers TODO
    }
  }

  //	SETTLE
  protected void executeSettle()
  {
    processDrawCardPowers(Phase.settle);
    //		
    //		getFixedReducedSettleCost();
    //		getVariableReducedSettleCost();
    //		
    //		getSettleableWorlds();
    //		doSettle();
    //		
    // TODO search for powers with cost
  }

  // CONSUME
  protected void executeConsume() {
    for(DrawCard power: getPowers(DrawCard.class))
    {
      if(power.getPhase() == Phase.consume)
      {
        for(int i=0; i < power.getNumber(); i++)
          addToHand(game.drawCard());
      }
    }
  }

  // PRODUCE
  protected void executeProduce() {
    // TODO Auto-generated method stub

  }

  // FINALIZE
  protected void executeFinalizeTurn() {
    int handSizeLimit = 10;
    if(hasPower(IncreaseHandLimit.class))
      handSizeLimit = 12;
    if( hand.size() > handSizeLimit)
    {
      hand.removeAll(discardCards(hand, hand.size() - handSizeLimit, QueryType.finalizeDiscardHand));			
    }

    isPrestige = false;
  }


  // internal methods
  private List<Card> queryUi(QueryType query, List<Card> cards, int minCards, int maxCards){
    List<Card> reply = ui.query(this, query, cards, minCards, maxCards);
    assert(reply.size() >= minCards && reply.size() <= maxCards); 
    return reply;
  }

  private void processDrawCardPowers(Phase iPhase) {
    for(DrawCard power: getPowers(DrawCard.class))
    {
      if(power.getPhase() == iPhase)
      {
        for(int i=0; i < power.getNumber(); i++)
          addToHand(game.drawCard());
      }
    }
  }

  protected void addVp(int iNumber) {
    victoryPoints += iNumber;
  }

  public PlayerColor getColor() {
    return color;
  }

  public void setColor(PlayerColor color) {
    this.color = color;
  }

  public void addToHand(Card iCard) {
    hand.add(iCard);
  }
  protected void removeFromHand(Card iCard) {
    hand.remove(iCard);
    game.discardPile.add(iCard);
  }

  protected void addToBoard(Card iCard) {
    board.add(iCard);

    if(iCard instanceof World)
    {
      World world = (World)(iCard);
      if(! world.isProduction())
      {
        if(world.getGoodType() != GoodType.None)
        {
          world.setGood(game.drawCard());
        }
      }
    }
  }

  protected Action getActionChosen() {
    return actionChosen;
  }

  protected void setActionChosen(Action actionChosen) {
    this.actionChosen = actionChosen;
  }

  protected boolean isPrestige() {
    return isPrestige;
  }

  protected void setPrestige(boolean isPrestige) {
    this.isPrestige = isPrestige && !prestigeActionUsed;
  }

  protected int getPrestigePoints() {
    return prestigePoints;
  }

  protected void setPrestigePoints(int prestigePoints) {
    this.prestigePoints = prestigePoints;
  }

  protected boolean isActionExecuted() {
    return isPhaseExecuted;
  }

  protected boolean hasPower(Class<? extends Power> iPower) {
    for(Card aC: board)
    {
      return aC.hasPower(iPower);
    }
    return false;
  }


  private <PowerType extends Power> List<PowerType> getPowers(Class<PowerType> iPower) {
    List<PowerType> result = new ArrayList<PowerType>();
    for(Card aC: board)
    {
      result.addAll(aC.getPowers(iPower));
    }
    return result;
  }

  private List<Card> discardCards(List<Card> drawnCards, int numberOfCardsToDiscard,
      QueryType explorediscard) {
    List<Card> result = queryUi(explorediscard, drawnCards, numberOfCardsToDiscard, numberOfCardsToDiscard);

    game.discardPile.addAll(result);

    return result;
  }

  public int getVictoryPoints() {
    return victoryPoints;
  }

  public List<Card> getHand() {
    return hand;
  }

  public List<Card> getBoard() {
    return board;
  }

  public boolean isPrestigeActionUsed() {
    return prestigeActionUsed;
  }

  public String getClientId() {
    return clientId;
  }

}
