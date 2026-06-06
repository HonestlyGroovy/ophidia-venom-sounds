package com.github.honestlygroovy.ophidiavenom.sounds;

import com.github.honestlygroovy.ophidiavenom.*;
import com.github.honestlygroovy.ophidiavenom.overrides.SoundOverrideAction;
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
public class RedemptionProc extends TimedSoundBase
{

	private boolean redemptionHasProcced = false;

	@Inject
	private Client client;

	@Inject
	private OphidiavenomConfig config;

	@Inject
	private SoundEngine soundEngine;

	@Inject
	private ScheduledExecutorService executor;

	RedemptionProc()
	{
		super(5);
	}

	public void onTick(int currentTick, Player local)
	{
		if (config.redemptionMessage() && !redemptionHasProcced)
		{

			boolean procced = local.hasSpotAnim(AnimationIds.REDEMPTION_PROC.Id);
			if (procced)
			{
				if (canPlaySound(currentTick))
				{
					redemptionHasProcced = true;
					setLastPlayedTickTick(currentTick);
					if (config.showChatMessages())
					{
						//TODO: Add different chat message
						client.addChatMessage(ChatMessageType.PUBLICCHAT, OPHIDIAVENOM, "Your redemption has procced.", null);
					}
					soundEngine.playClip(Sound.REDEMPTION_PROC, SoundOverrideAction.REDEMPTION_PROC, executor);
					return;
				}
			}
		}
	}

	@Override
	public void cleanupTicks(int currentTick)
	{
		int lastRedemptionProcTick = getLastPlayedTick();
		if (redemptionHasProcced &&
			lastRedemptionProcTick != currentTick &&
			currentTick - lastRedemptionProcTick > getTickDelay())
		{
			setLastPlayedTickTick(-1);
			redemptionHasProcced = false;
		}
	}

	public void onSoundPlayed(SoundEffectPlayed event)
	{
		Player local = client.getLocalPlayer();
		int soundId = event.getSoundId();

		if (config.redemptionMessage())
		{
			if (local == event.getSource())
			{
				if (soundId == SoundIds.REDEMPTION_PROC.Id)
				{
					event.consume();
					return;
				}
			}
		}
	}

	public boolean HasRedemptionProcced()
	{
		return redemptionHasProcced;
	}
}
