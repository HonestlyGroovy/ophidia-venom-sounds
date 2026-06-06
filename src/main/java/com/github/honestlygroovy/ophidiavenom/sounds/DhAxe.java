package com.github.honestlygroovy.ophidiavenom.sounds;


import com.github.honestlygroovy.ophidiavenom.DhAxeStyles;
import com.github.honestlygroovy.ophidiavenom.OphidiavenomConfig;
import com.github.honestlygroovy.ophidiavenom.OphidiavenomVarbitValues;
import com.github.honestlygroovy.ophidiavenom.OphidiavenomVarbits;
import com.github.honestlygroovy.ophidiavenom.Sound;
import com.github.honestlygroovy.ophidiavenom.SoundEngine;
import com.github.honestlygroovy.ophidiavenom.overrides.SoundOverrideAction;
import java.util.Random;
import java.util.concurrent.ScheduledExecutorService;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.EquipmentInventorySlot;
import net.runelite.api.InventoryID;
import net.runelite.api.Item;
import net.runelite.api.ItemContainer;
import net.runelite.api.ItemID;
import net.runelite.api.events.VarbitChanged;
import net.runelite.client.callback.ClientThread;

@Singleton
@Slf4j
public class DhAxe extends TimedSoundBase
{

	@Inject
	private Client client;

	@Inject
	private OphidiavenomConfig config;

	@Inject
	private SoundEngine soundEngine;

	@Inject
	private ScheduledExecutorService executor;
	@Getter(AccessLevel.PACKAGE)
	@Inject
	private ClientThread clientThread;

	private static final Random random = new Random();

	private int current43 = -1;
	private int current46 = -1;
	private int current843 = 0;
	private DhAxeStyles dhAxeStyle;
	private final int[] dhAxeIds = new int[]{ItemID.DHAROKS_GREATAXE, ItemID.DHAROKS_GREATAXE_100, ItemID.DHAROKS_GREATAXE_75, ItemID.DHAROKS_GREATAXE_50, ItemID.DHAROKS_GREATAXE_25, ItemID.SOULREAPER_AXE, ItemID.SOULREAPER_AXE_28338, ItemID.DHAROKS_GREATAXE_25516};

	DhAxe()
	{
		super(5);
	}

	public void onVarbitChanged(VarbitChanged varbitChanged)
	{
		final int varpId = varbitChanged.getVarpId();
		final int value = varbitChanged.getValue();

		// Always set current values because if you switch weapons and the values stay the same, this method won't get triggered
		if (varpId == OphidiavenomVarbits.COMBAT_STYLE_43.VarpId)
		{
			current43 = value;
		}
		else if (varpId == OphidiavenomVarbits.COMBAT_STYLE_46.VarpId)
		{
			current46 = value;
		}
		else if (varpId == OphidiavenomVarbits.IS_WEARING_WEAPON.VarpId)
		{
			current843 = value;
		}
		else
		{
			return;
		}

		if (!config.dhAxe())
		{
			return;
		}
		clientThread.invokeLater(this::playSound);
	}

	public void playSound()
	{
		// Only necessary to do any checks if we're wearing the dh axe
		// And if we're not depositing worn items to the bank
		if (!isUsingDhAxe())
		{
			return;
		}

		Sound sound = getSoundForDhAxeStyle();
		if (sound == null)
		{
			return;
		}
		final int currentTick = client.getTickCount();
		if (canPlaySound(currentTick))
		{
			soundEngine.playClip(sound, toOverrideAction(sound), executor);

			setLastPlayedTickTick(currentTick);
		}
	}

	private boolean isUsingDhAxe()
	{
		if (current843 == 0)
		{
			return false;
		}

		final ItemContainer itemContainer = client.getItemContainer(InventoryID.EQUIPMENT);
		if (itemContainer == null)
		{
			return false;
		}
		final Item item = itemContainer.getItem(EquipmentInventorySlot.WEAPON.getSlotIdx());
		if (item == null)
		{
			return false;
		}
		final int itemId = item.getId();

		for (int id : dhAxeIds)
		{
			if (id == itemId)
			{
				return true;
			}
		}

		return false;
	}

	private Sound getSoundForDhAxeStyle()
	{
		Sound sound = null;
		if (current43 == OphidiavenomVarbitValues.COMBAT_STYLE_43_0.Value &&
			current46 == OphidiavenomVarbitValues.COMBAT_STYLE_46_1.Value)
		{
			sound = Sound.DH_AXE_CHOP;
		}
		else if (current43 == OphidiavenomVarbitValues.COMBAT_STYLE_43_1.Value &&
			current46 == OphidiavenomVarbitValues.COMBAT_STYLE_46_2.Value)
		{
			sound = Sound.DH_AXE_HACK;
		}
		else if (current43 == OphidiavenomVarbitValues.COMBAT_STYLE_43_2.Value &&
			current46 == OphidiavenomVarbitValues.COMBAT_STYLE_46_2.Value)
		{
			sound = Sound.DH_AXE_SMASH;
		}
		else if (current43 == OphidiavenomVarbitValues.COMBAT_STYLE_43_3.Value &&
			current46 == OphidiavenomVarbitValues.COMBAT_STYLE_46_3.Value)
		{
			sound = Sound.DH_AXE_BLOCK;
		}
		return sound;
	}

	private SoundOverrideAction toOverrideAction(Sound sound)
	{
		switch (sound)
		{
			case DH_AXE_CHOP:
				return SoundOverrideAction.DH_AXE_CHOP;
			case DH_AXE_HACK:
				return SoundOverrideAction.DH_AXE_HACK;
			case DH_AXE_SMASH:
				return SoundOverrideAction.DH_AXE_SMASH;
			case DH_AXE_BLOCK:
				return SoundOverrideAction.DH_AXE_BLOCK;
			default:
				return null;
		}
	}
}
