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
import net.runelite.api.events.SoundEffectPlayed;

@Singleton
@Slf4j
public class ZebakRoar extends TimedSoundBase
{
	ZebakRoar()
	{
		super(10);
	}

	@Inject
	private Client client;

	@Inject
	private OphidiavenomConfig config;

	@Inject
	private SoundEngine soundEngine;

	@Inject
	private ScheduledExecutorService executor;

	public void onSoundEffectPlayed(SoundEffectPlayed event)
	{
		int soundId = event.getSoundId();
		int currentTick = client.getTickCount();
		if (config.zebakRoar())
		{
			if (soundId == 5829)
			{
				event.consume();
				if (canPlaySound(currentTick))
				{
					setLastPlayedTickTick(currentTick);
					soundEngine.playClip(Sound.ZEBAK_ROAR, SoundOverrideAction.ZEBAK_ROAR, executor);
				}
			}
		}
	}
}
