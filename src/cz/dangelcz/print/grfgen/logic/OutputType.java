package cz.dangelcz.print.grfgen.logic;

public enum OutputType
{
	ZPL,
	GRF;

	public String getType()
	{
		return name().toLowerCase();
	}
}
