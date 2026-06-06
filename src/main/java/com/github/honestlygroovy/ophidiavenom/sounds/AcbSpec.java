package com.github.honestlygroovy.ophidiavenom.sounds;

import com.github.honestlygroovy.ophidiavenom.OphidiavenomConfig;
import com.github.honestlygroovy.ophidiavenom.Sound;
import com.github.honestlygroovy.ophidiavenom.SoundEngine;
import com.github.honestlygroovy.ophidiavenom.SoundIds;
import com.github.honestlygroovy.ophidiavenom.overrides.SoundOverrideAction;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.Player;
import net.runelite.api.events.AreaSoundEffectPlayed;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.concurrent.ScheduledExecutorService;

import static com.github.honestlygroovy.ophidiavenom.OphidiavenomPlugin.OPHIDIAVENOM;
import net.runelite.api.events.SoundEffectPlayed;

@Singleton
@Slf4j
public class AcbSpec
{

	@Inject
	private Client client;

	@Inject
	private OphidiavenomConfig config;

	@Inject
	private SoundEngine soundEngine;

	@Inject
	private ScheduledExecutorService executor;
	private final String message = "ACB SPECCLE!";

	public void onAreaSoundEffectPlayed(AreaSoundEffectPlayed event)
	{
		int soundId = event.getSoundId();
		if (config.acbSpec())
		{
			if (soundId == SoundIds.ACB_SPEC.Id)
			{
				event.consume();
				soundEngine.playClip(Sound.ACB_SPEC, SoundOverrideAction.ACB_SPEC, executor);
				if (config.showChatMessages())
				{
					client.addChatMessage(ChatMessageType.PUBLICCHAT, OPHIDIAVENOM, message, null);
				}
				return;
			}
		}
	}

	public void onSoundEffectPlayed(SoundEffectPlayed event)
	{
		int soundId = event.getSoundId();
		final Player local = client.getLocalPlayer();

		if (config.acbSpec())
		{
			if (soundId == SoundIds.ACB_SPEC.Id)
			{
				event.consume();
				soundEngine.playClip(Sound.ACB_SPEC, SoundOverrideAction.ACB_SPEC, executor);
				if (config.showChatMessages())
				{
					client.addChatMessage(ChatMessageType.PUBLICCHAT, OPHIDIAVENOM, message, null);
				}
				return;
			}
		}
	}
}
