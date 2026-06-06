package com.github.honestlygroovy.ophidiavenom.sounds;


import com.github.honestlygroovy.ophidiavenom.OphidiavenomConfig;
import com.github.honestlygroovy.ophidiavenom.Sound;
import com.github.honestlygroovy.ophidiavenom.SoundEngine;
import java.util.concurrent.ScheduledExecutorService;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.api.events.WidgetLoaded;
import net.runelite.api.widgets.Widget;

public class OphidiavenomWarriors
{
	@Inject
	private Client client;

	@Inject
	private SoundEngine soundEngine;

	@Inject
	private ScheduledExecutorService executor;

	@Inject
	private OphidiavenomConfig config;

	private Widget warriorWidget = null;

	public void onWidgetLoaded(WidgetLoaded event)
	{
		if (!config.warriors())
		{
			return;
		}
		Widget musicWidget = client.getWidget(15663110);

		if (musicWidget == null)
		{
			return;
		}

		for (Widget w : musicWidget.getDynamicChildren())
		{
			if (w.getText().equals("7th Realm") || w.getText().equals("Ophidiavenom Warriors"))
			{
				w.setText("Ophidiavenom Warriors");
				w.setName("<col=ff9040>Ophidiavenom Warriors</col>");
				w.setTextColor(901389);
				w.revalidate();
				warriorWidget = w;
				return;
			}
		}
	}

	public void onMenuOptionClicked(MenuOptionClicked event)
	{
		if (!config.warriors())
		{
			return;
		}

		if (warriorWidget == null)
		{
			return;
		}
		if (event.getWidget() != warriorWidget)
		{
			return;
		}

		event.consume();

		soundEngine.playClip(Sound.WARRIOR, executor);
	}
}
