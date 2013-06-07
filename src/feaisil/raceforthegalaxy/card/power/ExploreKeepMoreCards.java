package feaisil.raceforthegalaxy.card.power;
public final class ExploreKeepMoreCards extends Power {
  private int strength;

  public ExploreKeepMoreCards(int iStrength) {

    strength = iStrength;
  }

  public int getStrength()
  {
    return strength;
  }

  @Override
  public String toString()
  {
    return "ExploreKeepMoreCards [strength=" + strength + "]";
  }
}
