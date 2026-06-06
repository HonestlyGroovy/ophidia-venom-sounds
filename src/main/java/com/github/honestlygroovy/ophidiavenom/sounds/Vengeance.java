package com.github.honestlygroovy.ophidiavenom.sounds;

import com.github.honestlygroovy.ophidiavenom.OphidiavenomConfig;
import com.github.honestlygroovy.ophidiavenom.Sound;
import com.github.honestlygroovy.ophidiavenom.SoundEngine;
import com.github.honestlygroovy.ophidiavenom.overrides.SoundOverrideAction;
import java.util.concurrent.ScheduledExecutorService;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.events.VarbitChanged;

@Singleton
@Slf4j
public class Vengeance
{

	@Inject
	private Client client;

	@Inject
	private OphidiavenomConfig config;

	@Inject
	private SoundEngine soundEngine;

	@Inject
	private ScheduledExecutorService executor;


	public void onVarbitChanged(VarbitChanged event)
	{
		final int varbitId = event.getVarbitId();
		final int varpId = event.getVarpId();
		final int value = event.getValue();

		if (config.vengeance() && varbitId == 2450 && varpId == 439 && value == 1)
		{
			soundEngine.playClip(Sound.VENGEANCE, SoundOverrideAction.VENGEANCE, executor);
		}
	}
}
