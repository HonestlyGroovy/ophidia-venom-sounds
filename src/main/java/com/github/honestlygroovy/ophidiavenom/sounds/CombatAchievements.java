package com.github.honestlygroovy.ophidiavenom.sounds;

import com.github.honestlygroovy.ophidiavenom.OphidiavenomConfig;
import static com.github.honestlygroovy.ophidiavenom.OphidiavenomPlugin.OPHIDIAVENOM;
import com.github.honestlygroovy.ophidiavenom.Sound;
import com.github.honestlygroovy.ophidiavenom.SoundEngine;

import java.util.concurrent.ScheduledExecutorService;
import java.util.regex.Pattern;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.events.ChatMessage;
import net.runelite.client.events.ConfigChanged;

@Singleton
@Slf4j
public class CombatAchievements
{

	@Inject
	private Client client;

	@Inject
	private OphidiavenomConfig config;

	@Inject
	private SoundEngine soundEngine;

	@Inject
	private ScheduledExecutorService executor;

	private static final Pattern COMBAT_TASK_REGEX = Pattern.compile("CA_ID:\\d+\\|Congratulations, you've completed an? \\w+ combat task:.*");

	public boolean onChatMessage(ChatMessage chatMessage)
	{
		if (!config.announceCombatAchievement() || !COMBAT_TASK_REGEX.matcher(chatMessage.getMessage()).matches())
		{
			return false;
		}
		if (config.showChatMessages())
		{
			client.addChatMessage(ChatMessageType.PUBLICCHAT, OPHIDIAVENOM, "Combat task: completed.", null);
		}
		soundEngine.playClip(Sound.COMBAT_TASK, executor);
		return true;
	}

	public boolean onConfigChanged (ConfigChanged configChanged)
	{
		if (config.announceCombatAchievement() && configChanged.getKey().equals("announceCombatAchievement")) {
			soundEngine.playClip(Sound.COMBAT_TASK, executor);
			return true;
		}
		return false;
	}
}
