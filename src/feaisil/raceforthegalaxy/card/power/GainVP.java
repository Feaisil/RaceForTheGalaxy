package feaisil.raceforthegalaxy.card.power;

import feaisil.raceforthegalaxy.game.Phase;

public final class GainVP extends Power {

	private int number;
	public GainVP() {
		super(Phase.consume);
		number = 1;
	}
	public GainVP(int iNumber) {
		super(Phase.consume);
		number = iNumber;
	}
	public GainVP(Phase iPhase, int iNumber) {
		super(iPhase);
		number = iNumber;
	}
	public int getNumber() {
		return number;
	}
}
