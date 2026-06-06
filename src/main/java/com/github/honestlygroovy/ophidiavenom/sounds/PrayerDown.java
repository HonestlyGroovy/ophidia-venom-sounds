package com.github.honestlygroovy.ophidiavenom.sounds;

import com.github.honestlygroovy.ophidiavenom.OphidiavenomConfig;
import com.github.honestlygroovy.ophidiavenom.Sound;
import com.github.honestlygroovy.ophidiavenom.SoundEngine;
import com.github.honestlygroovy.ophidiavenom.SoundIds;
import com.github.honestlygroovy.ophidiavenom.overrides.SoundOverrideAction;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.Skill;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.SoundEffectPlayed;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.concurrent.ScheduledExecutorService;

@Singleton
@Slf4j
public class PrayerDown
{

	@Inject
	private Client client;

	@Inject
	private OphidiavenomConfig config;

	@Inject
	private SoundEngine soundEngine;

	@Inject
	private ScheduledExecutorService executor;


	private int previousPrayerValue = -1;
	private int lastLoginTick = -1;

	public void setLastLoginTick(int tick)
	{
    	lastLoginTick = tick;
	}

	public void onGameTick(GameTick event)
	{
		if (canReplacePrayerDownSound() && checkLowPrayer())
		{
			soundEngine.playClip(Sound.SMITED_NO_PRAYER, SoundOverrideAction.PRAYER_DOWN, executor);
		}
	}

	private boolean checkLowPrayer()
	{
		int currentPrayerValue = client.getBoostedSkillLevel(Skill.PRAYER);
		if (previousPrayerValue != currentPrayerValue)
		{
			previousPrayerValue = currentPrayerValue;
		}
		else
		{
			return false;
		}
		if (currentPrayerValue <= 0)
		{
			return true;
		}

		return false;
	}

	public void onSoundEffectPlayed(SoundEffectPlayed event)
	{
		if (event.getSoundId() == SoundIds.PRAYER_DOWN.Id && canReplacePrayerDownSound())
		{
			event.consume();
		}
	}

	private boolean canReplacePrayerDownSound()
	{
		if (!config.prayerMessage())
		{
			return false;
		}

		if (lastLoginTick == -1 || client.getTickCount() - lastLoginTick <= 2)
		{
			return false;
		}

		return true;
	}
}
