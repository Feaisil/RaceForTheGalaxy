package feaisil.raceforthegalaxy.card.power;

public class ReduceDevelopCost extends Power {
  private int strength;

  public ReduceDevelopCost(int iStrength) {
    strength = iStrength;
  }

  public int getStrength() {
    return strength;
  }

  @Override
  public String toString()
  {
    return "ReduceDevelopCost [strength=" + strength + "]";
  }
}
