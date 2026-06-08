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
import java.time.Duration;

@Singleton
@Slf4j
public class HunleffSwaps
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

        if (config.hunleffSwaps())
        {
            if (soundId == SoundIds.HUNLEFF_RANGE.Id)
            {
                event.consume();
                soundEngine.playClip(Sound.RANGE, executor, Duration.ofMillis(2400));
                return true;
            }
            else if (soundId == SoundIds.HUNLEFF_MAGE.Id)
            {
                event.consume();
                soundEngine.playClip(Sound.MAGE, executor, Duration.ofMillis(2400));
                return true;
            }
        }
        return false;
    }
}