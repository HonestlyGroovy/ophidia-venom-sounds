package com.github.honestlygroovy.ophidiavenom.sounds;

import com.github.honestlygroovy.ophidiavenom.OphidiavenomConfig;
import com.github.honestlygroovy.ophidiavenom.Sound;
import com.github.honestlygroovy.ophidiavenom.SoundEngine;
import com.github.honestlygroovy.ophidiavenom.overrides.SoundOverrideAction;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.events.MenuOptionClicked;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.concurrent.ScheduledExecutorService;
import net.runelite.api.widgets.Widget;

@Singleton
@Slf4j
public class DismissRandomEvent
{

	@Inject
	private Client client;

	@Inject
	private OphidiavenomConfig config;

	@Inject
	private SoundEngine soundEngine;

	@Inject
	private ScheduledExecutorService executor;

	private static final String optionText = "Dismiss";
	private static final int runePouchWidgetId = 983062;
	private static final int lootingBagWidgetId = 983048;

	public void onMenuOptionClicked(MenuOptionClicked menuOptionClicked)
	{
		Widget widget = menuOptionClicked.getWidget();
		int widgetId = widget == null ? -1 : widget.getId();
		String option = menuOptionClicked.getMenuOption();
		// Dismiss random event
		if (config.dismissRandomEvent() && option.equals(optionText) && widgetId != runePouchWidgetId && widgetId != lootingBagWidgetId)
		{
			soundEngine.playClip(Sound.DISMISSING_RANDOM_EVENT, SoundOverrideAction.DISMISS_RANDOM_EVENT, executor);
		}
	}
}
