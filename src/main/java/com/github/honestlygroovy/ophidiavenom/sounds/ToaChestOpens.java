package com.github.honestlygroovy.ophidiavenom.sounds;

import com.github.honestlygroovy.ophidiavenom.OphidiavenomConfig;
import com.github.honestlygroovy.ophidiavenom.Sound;
import com.github.honestlygroovy.ophidiavenom.SoundEngine;

import java.util.concurrent.ScheduledExecutorService;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.events.GameObjectSpawned;

@Singleton
@Slf4j
public class ToaChestOpens
{

	@Inject
	private Client client;

	@Inject
	private OphidiavenomConfig config;

	@Inject
	private SoundEngine soundEngine;

	@Inject
	private ScheduledExecutorService executor;

	// Thank you https://github.com/LlemonDuck/tombs-of-amascut
	private static final int SARCOPHAGUS_ID = 44934;

	public void onGameObjectSpawned(GameObjectSpawned e)
	{
		if (!config.toaPurpleChestOpens() || e.getGameObject().getId() != SARCOPHAGUS_ID)
		{
			return;
		}

		// The sarcophagus spawns as the player starts looting the chest
		soundEngine.playClip(Sound.TOA_CHEST_OPENS, executor);
	}
}
