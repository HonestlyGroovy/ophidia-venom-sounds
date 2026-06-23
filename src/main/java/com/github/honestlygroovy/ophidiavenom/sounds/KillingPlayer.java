package com.github.honestlygroovy.ophidiavenom.sounds;


import com.github.honestlygroovy.ophidiavenom.OphidiavenomConfig;
import com.github.honestlygroovy.ophidiavenom.PlayerKillLineManager;
import com.github.honestlygroovy.ophidiavenom.Sound;
import com.github.honestlygroovy.ophidiavenom.SoundEngine;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.Player;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.VarbitChanged;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.util.Text;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.concurrent.ScheduledExecutorService;
import java.util.regex.Pattern;

@Singleton
@Slf4j
public class KillingPlayer
{

	@Inject
	private Client client;

	@Inject
	private OphidiavenomConfig config;

	@Inject
	private SoundEngine soundEngine;

	@Inject
	private ScheduledExecutorService executor;

	private int lmsKills = 0;

	public boolean onChatMessage(ChatMessage chatMessage)
	{
		Player local = client.getLocalPlayer();
		String standardized = Text.standardize(chatMessage.getMessage());
		// Player Kill message checks
		if (config.playerKilling() &&
				chatMessage.getName().equals(""))
		{
			Pattern[] patterns = PlayerKillLineManager.getPatterns();
			for (Pattern pattern : patterns)
			{
				if (pattern.matcher(standardized).matches())
				{
					playSound(true);
					return true;
				}
			}

			if (standardized.equalsIgnoreCase(local.getName() + " has won!"))
			{
				playSound(false);
				return true;
			}
		}
		return false;
	}

	public void onVarbitChanged(VarbitChanged event)
	{
		final int id = event.getVarbitId();
		final int val = event.getValue();
		final int varpId = event.getVarpId();

		if (id == 5315 && varpId == 1377 && val != lmsKills)
		{
			lmsKills = val;

			if (lmsKills > 0)
			{
				playSound(true);
			}
		}
	}

	private void playSound(boolean sendMessage)
	{
		soundEngine.playClip(Sound.KILLING_SOMEONE_1, executor);
	}

	public boolean onConfigChanged (ConfigChanged configChanged)
	{
		if (config.playerKilling() && configChanged.getKey().equals("playerKilling")) {
			soundEngine.playClip(Sound.KILLING_SOMEONE_1, executor);
			return true;
		}
		return false;
	}
}