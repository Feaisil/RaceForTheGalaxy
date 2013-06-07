package feaisil.raceforthegalaxy.card.power;

public final class ExploreDrawMoreCards extends Power {
  private int strength;

  public ExploreDrawMoreCards(int iStrength) {

    strength = iStrength;
  }

  public int getStrength()
  {
    return strength;
  }

  @Override
  public String toString()
  {
    return "ExploreDrawMoreCards [strength=" + strength + "]";
  }
}
