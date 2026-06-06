package com.github.honestlygroovy.ophidiavenom.sounds;

import com.github.honestlygroovy.ophidiavenom.OphidiavenomConfig;
import com.github.honestlygroovy.ophidiavenom.Sound;
import com.github.honestlygroovy.ophidiavenom.SoundEngine;
import com.github.honestlygroovy.ophidiavenom.SoundIds;
import com.github.honestlygroovy.ophidiavenom.overrides.SoundOverrideAction;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.Player;
import net.runelite.api.events.SoundEffectPlayed;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.concurrent.ScheduledExecutorService;

@Singleton
@Slf4j
public class RubyBoltProc
{

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
		final Player local = client.getLocalPlayer();

		if (config.rubyBoltProc())
		{
			if (soundId == SoundIds.RUBY_BOLT_PROC.Id)
			{
				event.consume();
				soundEngine.playClip(Sound.RUBY_PROC, SoundOverrideAction.RUBY_BOLT_PROC, executor);
				return;
			}
		}
	}
}
