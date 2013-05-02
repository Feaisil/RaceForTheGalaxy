package feaisil.raceforthegalaxy.card.power;

import feaisil.raceforthegalaxy.card.Card;
import feaisil.raceforthegalaxy.card.GoodType;
import feaisil.raceforthegalaxy.game.Game;
import feaisil.raceforthegalaxy.game.Phase;
import feaisil.raceforthegalaxy.game.Player;

public class ReduceSettleCost extends Power {
	private GoodType type;
	private int strength;
	
	public ReduceSettleCost(GoodType iType, int iStrength) {
		super(Phase.settle);
		type = iType;
		strength = iStrength;
	}
	
	@Override
	public void trigger(Game iGame, Player iPlayer, Card iCard) {
		// TODO Auto-generated method stub

	}

	@Override
	public String toString() {
		StringBuilder _builder = new StringBuilder();
		_builder.append("ReduceSettleCost [");
		if (type != null) {
			_builder.append("type=");
			_builder.append(type);
			_builder.append(", ");
		}
		_builder.append("strength=");
		_builder.append(strength);
		_builder.append(", ");
		if (super.toString() != null) {
			_builder.append("toString()=");
			_builder.append(super.toString());
		}
		_builder.append("]");
		return _builder.toString();
	}

}
