package com.github.honestlygroovy.ophidiavenom.sounds;

import com.github.honestlygroovy.ophidiavenom.Sound;
import com.github.honestlygroovy.ophidiavenom.SoundEngine;
import com.github.honestlygroovy.ophidiavenom.overrides.SoundOverrideAction;
import java.util.concurrent.ScheduledExecutorService;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class LivestreamLiveSound
{
	@Inject
	private SoundEngine soundEngine;

	@Inject
	private ScheduledExecutorService executor;

	public void playSound()
	{
		soundEngine.playClip(Sound.GAMON_GO_LIVE, SoundOverrideAction.LIVESTREAM_GO_LIVE, executor);
	}
}
