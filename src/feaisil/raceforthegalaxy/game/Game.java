package feaisil.raceforthegalaxy.game;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import feaisil.raceforthegalaxy.Expansion;
import feaisil.raceforthegalaxy.card.Card;
import feaisil.raceforthegalaxy.card.CardList;
import feaisil.raceforthegalaxy.card.power.ChooseActionLast;
import feaisil.raceforthegalaxy.card.power.IncreaseBoardLimit;
import feaisil.raceforthegalaxy.exception.*;
import feaisil.raceforthegalaxy.goal.Goal;

public final class Game implements Serializable{

	static public class Configuration {
		final public List<Expansion>  expansion;
		final public boolean 			advanced;
		final public boolean			goalsDisabled;
		final public boolean			takeOverDisabled;
		final public InputStream 		cardListFile;
		public Configuration(List<Expansion> expansion, boolean advanced,
				boolean goalsDisabled, boolean takeOverDisabled,
				InputStream cardListFile) {
			super();
			this.expansion = expansion;
			this.advanced = advanced;
			this.goalsDisabled = goalsDisabled;
			this.takeOverDisabled = takeOverDisabled;
			this.cardListFile = cardListFile;
		}
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	// game real properties
	private int 			turn;
	protected boolean 		advanced;
	private Set<Expansion>  expansion;
	protected boolean 		goalsDisabled;
	protected boolean 		takeOverDisabled;
	
	// game settings
	private long 			seed;
	protected Random		randGen;
	public InputStream 		cardListFile;
	
	// game entities
	private List<Player> 	players;
	protected CardList 		cardList;
	
	private List<Card> 		deck;
	protected List<Card>	discardPile;
	protected List<Card>	actions;
	
	private int				remainingVp;
	
	private Set<Goal>		goals;

	public Game(Configuration config)
	{
		// init random number generator
		seed 				= System.currentTimeMillis();
		randGen 			= new Random(seed);

		// set game properties
		advanced 			= config.advanced;
		expansion 			= new HashSet<Expansion>(config.expansion);
		expansion.add(Expansion.BaseGame);
		goalsDisabled 		= config.goalsDisabled;
		takeOverDisabled 	= config.takeOverDisabled;
		cardListFile 		= config.cardListFile;
		
		players = new ArrayList<Player>();
		cardList = new CardList();
		
		deck = new LinkedList<Card>();
		discardPile = new LinkedList<Card>();
		actions = new LinkedList<Card>();
		turn = 0;
		remainingVp = 0;

		goals = new HashSet<Goal>();
	}

	public void playGame() throws FileNotFoundException {
		executeStartingPhase();
		do{
			turn++;
			executeSelectAction();
			executeSearch();
			executeExplore();
			executeDevelop();
			executeSettle();
			executeConsume();
			executeProduce();
		} while (!isGameEnded());
	}
	
	// Methods accessed from the same package
	public Card drawCard() 
	{
		if(deck.size() == 0)
			discardPileToDeck();
		return deck.remove(randGen.nextInt(deck.size()));
	}

	public void drawVP(Player iPlayer, int iNumber)  
	{
		if(remainingVp < iNumber)
		{
			iPlayer.addVp(remainingVp);
			remainingVp = 0;
		}
		else
		{
			iPlayer.addVp(iNumber);
			remainingVp -= iNumber;
		}
	}

	// internal functions
	private void discardPileToDeck() { 
		deck.addAll(discardPile);
		discardPile.clear();
	}

	/*
	 * This function is used by isPhasePlayed 
	 * True if at least one players chose the iAction this turn.
	 */
	private boolean hasPlayerAction(final Action iAction)
	{
		for(Player aP: players)
		{
			if(aP.getActionChosen() == iAction)
				return true;
		}
		return false;
	}
	/*
	 * Check if a given phase has been chosen by a player,
	 * verifies that a player has selected a corresponding action.
	 */
	private boolean isPhasePlayed(Phase iPhase)
	{
		switch(iPhase)
		{
		case selectAction:
			return true;
		case search:
			if(hasPlayerAction(Action.search))
				return true;
			break;
		case explore:
			if(hasPlayerAction(Action.exploreDraw) || hasPlayerAction(Action.exploreKeep))
				return true;
			break;
		case develop:
			if(hasPlayerAction(Action.develop))
				return true;
			break;
		case settle:
			if(hasPlayerAction(Action.settle))
				return true;
			break;
		case consume:
			if(hasPlayerAction(Action.trade) || hasPlayerAction(Action.consume))
				return true;
			break;
		case produce:
			if(hasPlayerAction(Action.produce))
				return true;
			break;
		default:
			break;
		}
		return false;
	}
	
	private void executeStartingPhase() throws FileNotFoundException {
		cardList.initFromCsv(cardListFile, expansion);
				
		List<Card> blueworld = cardList.getStartingBlueWorlds();
		List<Card> redworld = cardList.getStartingBlueWorlds();
		List<Card> world = cardList.getStartingBlueWorlds();
		for(Player p : players){
			// Draw starting world cards according to parameters
			if(
					expansion.contains(Expansion.RebelVsImperium)||
					expansion.contains(Expansion.TheBrinkOfWard)||
					expansion.contains(Expansion.TheGatheringStorm))
			{
				p.board.add(blueworld.remove(
						randGen.nextInt(blueworld.size())));
				p.board.add(redworld.remove(
						randGen.nextInt(redworld.size())));
			} else {
				p.board.add(
					world.remove(
						randGen.nextInt(world.size())));
			}
			// Draw starting hand cards according to parameters
			deck.addAll(cardList.getDeck());
			for(int i=0; i < 6; i++)
				p.hand.add(drawCard());
			// Add starting worlds to deck
			if(
					expansion.contains(Expansion.RebelVsImperium)||
					expansion.contains(Expansion.TheBrinkOfWard)||
					expansion.contains(Expansion.TheGatheringStorm))
			{
				deck.addAll(redworld);
				deck.addAll(blueworld);
			}
			else
			{
				deck.addAll(world);
			}
		}
		for(Player p : players){
			// Discards...
			p.executeStartingPhase();
		}
		// Reorder player list according to there primary world	
		Collections.sort(players, new Comparator<Player>(){
			public int compare(Player p1, Player p2){
				return p1.board.get(0).getId() - p2.board.get(0).getId();
			}
		});
	}
	private void executeSelectAction(){
		Player playerChoosingActionLater = null;
		for(Player p : players){
			if(p.hasPower(ChooseActionLast.class))
				playerChoosingActionLater = p;
			else
				p.executeSelectAction();
		}
		if(playerChoosingActionLater != null)
			playerChoosingActionLater.executeSelectAction();
	}
	
	private void executeSearch() {
		// TODO Auto-generated method stub
		
	}
		
	private void executeExplore() {
		if(isPhasePlayed(Phase.explore))
		{
			for(Player p : players)
			{
				p.executeExplore();
			}
		}
	}
		
	private void executeDevelop() {
		// TODO Auto-generated method stub
		
	}
	
	private void executeSettle() {
		// TODO Auto-generated method stub
		
	}
	
	private void executeConsume() {
		// TODO Auto-generated method stub
		
	}
	
	private void executeProduce() {
		// TODO Auto-generated method stub
		
	}

	private boolean isGameEnded() {
		for(Player p: players)
		{
			if(p.board.size() >= 12)
			{
				if(p.hasPower(IncreaseBoardLimit.class))
				{
					if(p.board.size() >= 14)
					{
						return true;
					} 
					else
					{
						return false;
					}
				}
				else
				{
					return true;
				}
			}
		}
		return false;
	}

	protected void addPlayer(Player player) throws TooManyPlayersException {
		if(players.size() >= 6)
			throw new TooManyPlayersException();
		players.add(player);
	}

	public int getTurn() {
		return turn;
	}

	public boolean isAdvanced() {
		return advanced;
	}

	public Set<Expansion> getExpansion() {
		return expansion;
	}

	public boolean isTakeOverDisabled() {
		return takeOverDisabled;
	}

	public List<Player> getPlayers() {
		return players;
	}

	public int getRemainingVp() {
		return remainingVp;
	}

	public Set<Goal> getGoals() {
		return goals;
	}

	public int getDeck() {
		return deck.size();
	}

	public int getDiscardPile() {
		return discardPile.size();
	}

}
