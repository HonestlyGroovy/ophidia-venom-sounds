package com.github.honestlygroovy.ophidiavenom.sounds;

import com.github.honestlygroovy.ophidiavenom.OphidiavenomConfig;
import com.github.honestlygroovy.ophidiavenom.Sound;
import com.github.honestlygroovy.ophidiavenom.SoundEngine;
import com.github.honestlygroovy.ophidiavenom.SoundIds;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.events.SoundEffectPlayed;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.concurrent.ScheduledExecutorService;

@Singleton
@Slf4j
public class EnteringBankPin
{

	@Inject
	private Client client;

	@Inject
	private OphidiavenomConfig config;

	@Inject
	private SoundEngine soundEngine;

	@Inject
	private ScheduledExecutorService executor;

	public boolean onSoundEffectPlayed(SoundEffectPlayed event)
	{
		int soundId = event.getSoundId();

		if (config.bankPin())
		{
			if (soundId == SoundIds.BANK_PIN.Id)
			{
				event.consume();
				soundEngine.playClip(Sound.TYPING_IN_BANKPIN, executor);
				return true;
			}
		}
		return false;
	}
}
