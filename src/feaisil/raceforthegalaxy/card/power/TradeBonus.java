package feaisil.raceforthegalaxy.card.power;

import feaisil.raceforthegalaxy.card.GoodType;

public final class TradeBonus extends Power {

  private GoodType goodType;
  private int bonusValue;

  public TradeBonus(GoodType iGoodType, int iBonusValue) {
    goodType = iGoodType;
    bonusValue = iBonusValue;
  }

  public GoodType getGoodType() {
    return goodType;
  }

  public int getBonusValue() {
    return bonusValue;
  }

  @Override
  public String toString()
  {
    return "TradeBonus [goodType=" + goodType + ", bonusValue=" + bonusValue
        + "]";
  }
}
