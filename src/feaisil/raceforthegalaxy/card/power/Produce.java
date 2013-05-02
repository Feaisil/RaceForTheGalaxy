package feaisil.raceforthegalaxy.card.power;

import feaisil.raceforthegalaxy.card.Card;
import feaisil.raceforthegalaxy.game.Game;
import feaisil.raceforthegalaxy.game.Phase;
import feaisil.raceforthegalaxy.game.Player;

public class Produce extends Power {

	public Produce() {
		super(Phase.produce);
	}

	@Override
	public void trigger(Game iGame, Player iPlayer, Card iCard) {
		// TODO Auto-generated method stub

	}

	@Override
	public String toString() {
		StringBuilder _builder = new StringBuilder();
		_builder.append("Produce [");
		if (super.toString() != null) {
			_builder.append("toString()=");
			_builder.append(super.toString());
		}
		_builder.append("]");
		return _builder.toString();
	}

}
