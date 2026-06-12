package com.github.honestlygroovy.ophidiavenom.sounds;

import com.github.honestlygroovy.ophidiavenom.OphidiavenomConfig;
import com.github.honestlygroovy.ophidiavenom.Sound;
import com.github.honestlygroovy.ophidiavenom.SoundEngine;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.events.ChatMessage;

import javax.inject.Inject;
import java.util.concurrent.ScheduledExecutorService;
import java.util.regex.Pattern;

import static com.github.honestlygroovy.ophidiavenom.OphidiavenomPlugin.OPHIDIAVENOM;

public class SlayerCompleted {

    @Inject
    private Client client;

    @Inject
    private OphidiavenomConfig config;

    @Inject
    private SoundEngine soundEngine;

    @Inject
    private ScheduledExecutorService executor;


    private static final Pattern SLAYER_TASK_REGEX = Pattern.compile("You have completed your task! You killed .*. You gained .* xp.");

    public boolean onChatMessage(ChatMessage chatMessage) {
        if (!config.announceSlayerCompletion() || !SLAYER_TASK_REGEX.matcher(chatMessage.getMessage()).matches()) {
            return false;
        }
        if (config.showChatMessages()) {
            client.addChatMessage(ChatMessageType.PUBLICCHAT, OPHIDIAVENOM, "Slayer Task: completed.", null);
        }
        soundEngine.playClip(Sound.SLAYER_TASK, executor);
        return true;
    }
}
