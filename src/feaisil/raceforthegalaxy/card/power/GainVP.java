package feaisil.raceforthegalaxy.card.power;

import feaisil.raceforthegalaxy.game.Phase;

public final class GainVP extends Power {

  private int number;
  private Phase phase;

  public GainVP() {
    phase = Phase.consume;
    number = 1;
  }
  public GainVP(int iNumber) {
    phase = Phase.consume;
    number = iNumber;
  }
  public GainVP(Phase iPhase, int iNumber) {
    phase = iPhase;
    number = iNumber;
  }
  public int getNumber() {
    return number;
  }
  public Phase getPhase()
  {
    return phase;
  }
  @Override
  public String toString()
  {
    return "GainVP [number=" + number + ", phase=" + phase + "]";
  }
}
