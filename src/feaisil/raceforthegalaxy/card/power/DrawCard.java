package feaisil.raceforthegalaxy.card.power;

import feaisil.raceforthegalaxy.game.Phase;

public final class DrawCard extends Power {

  private int number;
  private Phase phase;

  public DrawCard(Phase iPhase) {
    phase = iPhase;
    number = 1;
  }
  public DrawCard(Phase iPhase, int iNumber) {
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
    return "DrawCard [number=" + number + ", phase=" + phase + "]";
  }
}
