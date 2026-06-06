package com.github.honestlygroovy.ophidiavenom.sounds;

import com.github.honestlygroovy.ophidiavenom.*;
import com.github.honestlygroovy.ophidiavenom.overrides.SoundOverrideAction;
import java.util.Random;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.Player;
import net.runelite.api.events.SoundEffectPlayed;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.concurrent.ScheduledExecutorService;

import static com.github.honestlygroovy.ophidiavenom.OphidiavenomPlugin.OPHIDIAVENOM;

@Singleton
@Slf4j
public class DdsSpec extends TimedSoundBase
{

	@Inject
	private Client client;

	@Inject
	private OphidiavenomConfig config;

	@Inject
	private SoundEngine soundEngine;

	@Inject
	private ScheduledExecutorService executor;

	private Random random = new Random();

	private static final String message = "ZEW ZEW.";

	DdsSpec()
	{
		super(2);
	}

	public void onTick(int currentTick, Player local)
	{
		if (config.ddsSpec())
		{
			boolean ddsSpecced = local.hasSpotAnim(AnimationIds.DDS_SPEC.Id);
			if (ddsSpecced)
			{
				if (canPlaySound(currentTick))
				{
					if (config.showChatMessages())
					{
						//TODO: Add different chat message
						client.addChatMessage(ChatMessageType.PUBLICCHAT, OPHIDIAVENOM, message, null);
					}
					soundEngine.playClip(Sound.DDS_SPEC, SoundOverrideAction.DDS_SPEC, executor);
					setLastPlayedTickTick(currentTick);
				}
			}
		}
	}

	public void onSoundEffectPlayed(SoundEffectPlayed event)
	{
		int soundId = event.getSoundId();
		if (config.ddsSpec())
		{
			if (soundId == SoundIds.DDS_SPEC.Id)
			{
				event.consume();

				//TODO FIX for not you
				if (!config.ownPlayerOnly() && canPlaySound(client.getTickCount()))
				{
					soundEngine.playClip(Sound.DDS_SPEC, SoundOverrideAction.DDS_SPEC, executor);
				}
				return;
			}
		}
	}
}
