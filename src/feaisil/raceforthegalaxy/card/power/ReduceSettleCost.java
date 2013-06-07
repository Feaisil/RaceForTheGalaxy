package feaisil.raceforthegalaxy.card.power;

import feaisil.raceforthegalaxy.card.GoodType;

public class ReduceSettleCost extends Power {
  private GoodType type;
  private int strength;

  public ReduceSettleCost(GoodType iType, int iStrength) {
    type = iType;
    strength = iStrength;
  }

  public GoodType getType() {
    return type;
  }

  public int getStrength() {
    return strength;
  }

  @Override
  public String toString()
  {
    return "ReduceSettleCost [type=" + type + ", strength=" + strength + "]";
  }
}
