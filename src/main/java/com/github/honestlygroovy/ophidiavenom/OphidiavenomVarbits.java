package com.github.honestlygroovy.ophidiavenom;

public enum OphidiavenomVarbits
{
	RUNNING(-1, 173),

	// Used for DH Axe style determination
	COMBAT_STYLE_43(-1, 43),
	COMBAT_STYLE_46(-1, 46),

	IS_WEARING_WEAPON(-1, 843);

	public final int Id;
	public final int VarpId;

	OphidiavenomVarbits(int id, int varpId)
	{
		Id = id;
		VarpId = varpId;
	}
}
