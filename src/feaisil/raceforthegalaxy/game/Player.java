package feaisil.raceforthegalaxy.game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import feaisil.raceforthegalaxy.card.Card;
import feaisil.raceforthegalaxy.card.CardList;
import feaisil.raceforthegalaxy.exception.ActiveGameException;
import feaisil.raceforthegalaxy.exception.TooManyPlayersException;
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
		case 0: color = PlayerColor.Blue; break;
		case 1: color = PlayerColor.Green; break;
		case 2: color = PlayerColor.LightBlue; break;
		case 3: color = PlayerColor.Pink; break;
		case 4: color = PlayerColor.Red; break;
		case 5: color = PlayerColor.Yellow; break;
		default:
		}
		
		hand = new ArrayList<Card>(20);
		board = new ArrayList<Card>(14);
		startingWorlds = new ArrayList<Card>(2);
	}
	
// execute starting phase
	protected void executeStartingPhase() {
		List<Card> reply;
		// Choose one world
		if(startingWorlds.size() > 1)
			reply = queryUi(
					QueryType.startingPhaseChooseWorld,
					startingWorlds,
					1,1);
		else
			reply = startingWorlds;
		
		//Remove the world not chosen
		board.addAll(reply);
		startingWorlds.removeAll(reply);
		game.discardPile.addAll(startingWorlds);
		
		reply = queryUi(
				QueryType.startingPhaseDiscardHand,
				hand,
				2,2);
		
		hand.removeAll(reply);
		game.discardPile.addAll(reply);
	}

// execute select action
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
			else // TODO add check for advanced game case
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

//	
	protected void executeSearch() {
		// TODO Auto-generated method stub
		
	}
		
//	
	protected void executeExplore() {
		//Card aCard = playerInterface.chooseCardToDiscard(hand);
		// TODO Auto-generated method stub
		
	}
		
//	
	protected void executeDevelop() {
		// TODO Auto-generated method stub
		
	}
	
//	
	protected void executeSettle() {
		// TODO Auto-generated method stub
		
	}
	
//
	protected void executeConsume() {
		// TODO Auto-generated method stub
		
	}
	
//
	protected void executeProduce() {
		// TODO Auto-generated method stub
		
	}



	
// internal methods
	private List<Card> queryUi(QueryType query, List<Card> cards, int minCards, int maxCards){
		List<Card> reply = ui.query(this, query, cards, minCards, maxCards);
		assert(reply.size() >= minCards && reply.size() <= maxCards); 
		return reply;
	}
	
	// getters / setters
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

	protected boolean hasPower(String iPower) {
		for(Card aC: board)
		{
			return aC.hasPower(iPower);
		}
		return false;
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

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
}
