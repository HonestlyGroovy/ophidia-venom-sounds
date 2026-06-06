package com.github.honestlygroovy.ophidiavenom;

public enum OphidiavenomVarbitValues
{

	// Running varbit values, FALSE is never used, but is in here as a reference
	RUNNING_FALSE(OphidiavenomVarbits.RUNNING, 0),
	RUNNING_TRUE(OphidiavenomVarbits.RUNNING, 1),

	/*
		Used for DH Axe style determination

		FORMAT:
		Id; Value; VarpId

		CHOP:
		-1; 0; 43
		-1; 1; 46

		HACK:
		-1; 1; 43
		-1; 2; 46

		SMASH:
		-1; 2; 43;
		-1; 2; 46;

		BLOCK:
		-1; 3; 43;
		-1; 3; 46;
	 */
	COMBAT_STYLE_43_0(OphidiavenomVarbits.COMBAT_STYLE_43, 0),
	COMBAT_STYLE_43_1(OphidiavenomVarbits.COMBAT_STYLE_43, 1),
	COMBAT_STYLE_43_2(OphidiavenomVarbits.COMBAT_STYLE_43, 2),
	COMBAT_STYLE_43_3(OphidiavenomVarbits.COMBAT_STYLE_43, 3),

	COMBAT_STYLE_46_1(OphidiavenomVarbits.COMBAT_STYLE_46, 1),
	COMBAT_STYLE_46_2(OphidiavenomVarbits.COMBAT_STYLE_46, 2),
	COMBAT_STYLE_46_3(OphidiavenomVarbits.COMBAT_STYLE_46, 3);

	public final int Value;

	// Never actually used, only here to show and keep track of relations to varbits
	private final OphidiavenomVarbits Varbit;

	OphidiavenomVarbitValues(OphidiavenomVarbits varbit, int value)
	{
		Varbit = varbit;
		Value = value;
	}
}
