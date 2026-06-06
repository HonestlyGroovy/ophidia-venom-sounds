package com.github.honestlygroovy.ophidiavenom.sounds;

import com.github.honestlygroovy.ophidiavenom.OphidiavenomConfig;
import static com.github.honestlygroovy.ophidiavenom.OphidiavenomPlugin.OPHIDIAVENOM;
import com.github.honestlygroovy.ophidiavenom.Sound;
import com.github.honestlygroovy.ophidiavenom.SoundEngine;
import com.github.honestlygroovy.ophidiavenom.overrides.SoundOverrideAction;
import java.util.concurrent.ScheduledExecutorService;
import java.util.regex.Pattern;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.events.ChatMessage;

@Singleton
@Slf4j
public class QuestCompleted
{

	@Inject
	private Client client;

	@Inject
	private OphidiavenomConfig config;

	@Inject
	private SoundEngine soundEngine;

	@Inject
	private ScheduledExecutorService executor;

	private static final Pattern QUEST_REGEX = Pattern.compile("Congratulations, you've completed a quest:.*");

	public boolean onChatMessage(ChatMessage chatMessage)
	{
		if (!config.announceQuestCompletion() || !QUEST_REGEX.matcher(chatMessage.getMessage()).matches())
		{
			return false;
		}
		if (config.showChatMessages())
		{
			client.addChatMessage(ChatMessageType.PUBLICCHAT, OPHIDIAVENOM, "Quest: completed.", null);
		}
		soundEngine.playClip(Sound.QUEST, SoundOverrideAction.QUEST_COMPLETED, executor);
		return true;
	}
}
