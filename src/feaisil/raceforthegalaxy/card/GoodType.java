package feaisil.raceforthegalaxy.card;

public enum GoodType {
	None(0),
	Any(1),
	Novelty(2),
	Rare(3),
	Gene(4),
	Alien(5);
	
	private int _value;
	private GoodType(int iValue)
	{
		_value = iValue;
	}
	public int getValue()
	{
		return _value;
	}
}
