package com.github.honestlygroovy.ophidiavenom.sounds;

import com.github.honestlygroovy.ophidiavenom.OphidiavenomConfig;
import com.github.honestlygroovy.ophidiavenom.Sound;
import com.github.honestlygroovy.ophidiavenom.SoundEngine;

import java.util.concurrent.ScheduledExecutorService;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.WallObject;
import net.runelite.api.events.WallObjectSpawned;

@Singleton
@Slf4j
public class ToaChestLight
{
	@Inject
	private Client client;

	@Inject
	private OphidiavenomConfig config;

	@Inject
	private SoundEngine soundEngine;

	@Inject
	private ScheduledExecutorService executor;

	private static final int VARBIT_VALUE_CHEST_KEY = 2;
	private static final int VARBIT_ID_SARCOPHAGUS = 14373;
	private static final int WALL_OBJECT_ID_SARCOPHAGUS = 46221;

	private static final int[] VARBIT_MULTILOC_IDS_CHEST = new int[]{
		14356, 14357, 14358, 14359, 14360, 14370, 14371, 14372
	};

	private boolean sarcophagusIsPurple;
	private boolean purpleIsMine = true;

	public void onWallObjectSpawned(final WallObjectSpawned event)
	{
		final WallObject wallObject = event.getWallObject();

		if ((!config.toaPurpleChest() && !config.toaWhiteChest()) || wallObject.getId() != WALL_OBJECT_ID_SARCOPHAGUS)
		{
			return;
		}

		parseVarbits();

		if (sarcophagusIsPurple)
		{
			if (config.toaPurpleChest())
			{
				if (purpleIsMine)
				{
					soundEngine.playClip(Sound.YOUR_PURPLE, executor);
				}
				else
				{
					soundEngine.playClip(Sound.NOT_YOUR_PURPLE, executor);
				}
			}
		}
		else if (config.toaWhiteChest())
		{
			soundEngine.playClip(Sound.WHITE_LIGHT_AFTER_RAID, executor);
		}
	}

	private void parseVarbits()
	{
		sarcophagusIsPurple = client.getVarbitValue(VARBIT_ID_SARCOPHAGUS) % 2 != 0;
		purpleIsMine = true;

		for (final int varbitId : VARBIT_MULTILOC_IDS_CHEST)
		{
			if (client.getVarbitValue(varbitId) == VARBIT_VALUE_CHEST_KEY)
			{
				purpleIsMine = false;
				break;
			}
		}
	}
}
