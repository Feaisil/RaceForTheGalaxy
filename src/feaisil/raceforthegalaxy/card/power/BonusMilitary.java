package feaisil.raceforthegalaxy.card.power;

import feaisil.raceforthegalaxy.card.GoodType;

public final class BonusMilitary extends Power {
  private GoodType type;
  private int strength;

  public BonusMilitary(GoodType iType, int iStrength) {

    type = iType;
    strength = iStrength;
  }
  public BonusMilitary( int iStrength) {

    type = GoodType.Any;
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
    return "BonusMilitary [type=" + type + ", strength=" + strength + "]";
  }
}
