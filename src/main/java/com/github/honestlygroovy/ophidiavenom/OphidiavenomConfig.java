package com.github.honestlygroovy.ophidiavenom;

import java.awt.Color;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;
import net.runelite.client.config.Range;

@ConfigGroup(OphidiavenomConfig.CONFIG_GROUP)
public interface OphidiavenomConfig extends Config
{
	String CONFIG_GROUP = "ophidiavenomplugin";
	String SOUND_OVERRIDE_POOLS_KEY = "soundOverridePools";

	// =========================================================================
	// General Settings
	// =========================================================================

	@ConfigSection(
		name = "General",
		description = "Global plugin settings.",
		position = 10
	)
	String GENERAL_SECTION = "generalSection";

	@Range(min = 0, max = 200)
	@ConfigItem(
		keyName = "announcementVolume",
		name = "Announcement Volume",
		description = "Adjust how loud the audio announcements are played.",
		section = GENERAL_SECTION,
		position = 11
	)
	default int announcementVolume()
	{
		return 100;
	}

	@ConfigItem(
		keyName = "showChatMessages",
		name = "Show Fake Public Chat Message",
		description = "Should OphidiaVenom announce your achievements in game chat as well as audibly? Only you will see these messages.",
		section = GENERAL_SECTION,
		position = 12
	)
	default boolean showChatMessages()
	{
		return true;
	}

	@ConfigItem(
		keyName = "onlyForOwnPlayer",
		name = "Only Own Player",
		description = "Should OphidiaVenom sounds play for your player only?",
		section = GENERAL_SECTION,
		position = 13
	)
	default boolean ownPlayerOnly()
	{
		return true;
	}

	// =========================================================================
	// Specs & Combat
	// =========================================================================

	@ConfigSection(
		name = "Specs & Combat",
		description = "Special attack and combat-related sound replacements.",
		position = 20
	)
	String SPECS_SECTION = "specsSection";

	@ConfigItem(
			keyName = "crabCheck",
			name = "Crab Check",
			description = "Should OphidiaVenom let you know when the Gemstone Crab Moves?",
			section = SPECS_SECTION,
			position = 21
	)
	default boolean crabCheck()
	{
		return true;
	}

	@ConfigItem(
		keyName = "prayerMessage",
		name = "No Prayer Reminder",
		description = "Should OphidiaVenom let you know when you run out of prayer?",
		section = SPECS_SECTION,
		position = 28
	)
	default boolean prayerMessage()
	{
		return true;
	}

	@ConfigItem(
			keyName = "hunleffSwaps",
			name = "Hunleff Swaps",
			description = "Should OphidiaVenom let you know when to switch prayers at Hunleff? (Requires Sound Effects enabled)",
			section = SPECS_SECTION,
			position = 29
	)
	default boolean hunleffSwaps()
	{
		return true;
	}

	@ConfigItem(
			keyName = "yamaSwaps",
			name = "Yama Swaps",
			description = "Should OphidiaVenom let you know when to switch prayers at Yama? (Requires Area Sounds enabled)",
			section = SPECS_SECTION,
			position = 30
	)
	default boolean yamaSwaps()
	{
		return true;
	}



	// =========================================================================
	// PvP
	// =========================================================================

	@ConfigSection(
		name = "PvP",
		description = "PvP-related sounds.",
		position = 31
	)
	String PVP_SECTION = "pvpSection";

	@ConfigItem(
		keyName = "playerKilling",
		name = "Player Kill",
		description = "Should OphidiaVenom tell you something when you kill a player? This only works if you're still close to the player when they die.",
		section = PVP_SECTION,
		position = 32
	)
	default boolean playerKilling()
	{
		return true;
	}

	@ConfigItem(
		keyName = "pkChest",
		name = "PK Chest",
		description = "Should OphidiaVenom say something whenever you open the PK chest?",
		section = PVP_SECTION,
		position = 33
	)
	default boolean pkChest()
	{
		return true;
	}

	// =========================================================================
	// Achievements & Milestones
	// =========================================================================

	@ConfigSection(
		name = "Achievements & Milestones",
		description = "Sounds for level ups, quests, achievements, drops, and death.",
		position = 40
	)
	String ACHIEVEMENTS_SECTION = "achievementsSection";

	@ConfigItem(
		keyName = "announceLevelUp",
		name = "Level Ups",
		description = "Should OphidiaVenom announce when you gain a level in a skill?",
		section = ACHIEVEMENTS_SECTION,
		position = 41
	)
	default boolean announceLevelUp()
	{
		return true;
	}

	@ConfigItem(
		keyName = "announceLevelUpIncludesVirtual",
		name = "Include Virtual Level Ups",
		description = "Should OphidiaVenom announce when you gain a virtual level above 99 in a skill?",
		section = ACHIEVEMENTS_SECTION,
		position = 42
	)
	default boolean announceLevelUpIncludesVirtual()
	{
		return false;
	}

	@ConfigItem(
		keyName = "announceLevel99",
		name = "Level 99",
		description = "Should OphidiaVenom announce when you reach level 99 in a skill? This replaces the standard level-up sound for that milestone.",
		section = ACHIEVEMENTS_SECTION,
		position = 43
	)
	default boolean announceLevel99()
	{
		return true;
	}

	@ConfigItem(
		keyName = "announceQuestCompletion",
		name = "Quest Completions",
		description = "Should OphidiaVenom announce when you complete a quest?",
		section = ACHIEVEMENTS_SECTION,
		position = 44
	)
	default boolean announceQuestCompletion()
	{
		return true;
	}

	@ConfigItem(
		keyName = "announceCollectionLog",
		name = "New Collection Log Entry",
		description = "Should OphidiaVenom announce when you fill in a new slot in your collection log? This relies on you having chat messages enabled in the game settings, including the popup option.",
		section = ACHIEVEMENTS_SECTION,
		position = 45
	)
	default boolean announceCollectionLog()
	{
		return true;
	}

	@ConfigItem(
		keyName = "announceAchievementDiary",
		name = "Completed Achievement Diaries",
		description = "Should OphidiaVenom announce when you complete a new achievement diary?",
		section = ACHIEVEMENTS_SECTION,
		position = 46
	)
	default boolean announceAchievementDiary()
	{
		return true;
	}

	@ConfigItem(
		keyName = "announceCombatAchievement",
		name = "Completed Combat Achievement Tasks",
		description = "Should OphidiaVenom announce when you complete a new combat achievement task?",
		section = ACHIEVEMENTS_SECTION,
		position = 47
	)
	default boolean announceCombatAchievement()
	{
		return true;
	}

	@ConfigItem(
		keyName = "announceDeath",
		name = "When You Die",
		description = "Should OphidiaVenom say something when you die?",
		section = ACHIEVEMENTS_SECTION,
		position = 48
	)
	default boolean announceDeath()
	{
		return true;
	}

	@ConfigItem(
		keyName = "receivedPet",
		name = "Received Pet",
		description = "Should OphidiaVenom say something whenever you receive a pet?",
		section = ACHIEVEMENTS_SECTION,
		position = 49
	)
	default boolean receivedPet()
	{
		return true;
	}

	// =========================================================================
	// Trades & Interactions
	// =========================================================================

	@ConfigSection(
		name = "Trades & Interactions",
		description = "Sounds for trades, interfaces, and other interactions.",
		position = 50
	)
	String INTERACTIONS_SECTION = "interactionsSection";

	@ConfigItem(
		keyName = "acceptTrade",
		name = "Accept Trade",
		description = "Should OphidiaVenom say something when you accept a trade?",
		section = INTERACTIONS_SECTION,
		position = 51
	)
	default boolean acceptTrade()
	{
		return true;
	}

	@ConfigItem(
		keyName = "declineTrade",
		name = "Decline Trade",
		description = "Should OphidiaVenom say something when you decline a trade?",
		section = INTERACTIONS_SECTION,
		position = 52
	)
	default boolean declineTrade()
	{
		return true;
	}

	@ConfigItem(
		keyName = "dismissRandomEvent",
		name = "Dismiss Random Event",
		description = "Should OphidiaVenom say something when you dismiss a random event?",
		section = INTERACTIONS_SECTION,
		position = 53
	)
	default boolean dismissRandomEvent()
	{
		return true;
	}

	@ConfigItem(
		keyName = "petDog",
		name = "Pet the Dog",
		description = "Should OphidiaVenom say something when you pet a dog?",
		section = INTERACTIONS_SECTION,
		position = 54
	)
	default boolean petDog()
	{
		return true;
	}

	@ConfigItem(
		keyName = "bankPin",
		name = "Bank PIN",
		description = "Should OphidiaVenom make sounds when you type in your bank PIN?",
		section = INTERACTIONS_SECTION,
		position = 58
	)
	default boolean bankPin()
	{
		return true;
	}

	@ConfigItem(
			keyName = "hunterRumour",
			name = "Hunter Rumour",
			description = "Should OphidiaVenom make sounds complete a Hunter Rumour?",
			section = INTERACTIONS_SECTION,
			position = 59
	)
	default boolean hunterRumour()
	{
		return true;
	}

	// =========================================================================
	// Raids
	// =========================================================================

	@ConfigSection(
		name = "Raids",
		description = "All configurations regarding Raids",
		position = 100
	)
	String RAIDS = "raidsSection";

	@ConfigItem(
		keyName = "toaWhiteChest",
		name = "ToA White Chest",
		description = "When enabled, OphidiaVenom will say something if you receive a white light.",
		section = RAIDS,
		position = 101
	)
	default boolean toaWhiteChest()
	{
		return true;
	}

	@ConfigItem(
		keyName = "toaPurpleChest",
		name = "ToA Purple Chest",
		description = "When enabled, OphidiaVenom will say something if you receive a purple light.",
		section = RAIDS,
		position = 102
	)
	default boolean toaPurpleChest()
	{
		return true;
	}

	@ConfigItem(
		keyName = "enableToaPurpleChestOpens",
		name = "ToA Opening Chest",
		description = "When enabled, OphidiaVenom will say something whenever someone in your party opens the purple chest at TOA.",
		section = RAIDS,
		position = 103
	)
	default boolean toaPurpleChestOpens()
	{
		return true;
	}

	@ConfigItem(
		keyName = "tobWhiteChest",
		name = "ToB White Chest",
		description = "Should OphidiaVenom say something whenever you receive a white chest at TOB?",
		section = RAIDS,
		position = 201
	)
	default boolean tobWhiteChest()
	{
		return true;
	}

	@ConfigItem(
		keyName = "tobPurpleChest",
		name = "ToB Purple Chest",
		description = "Should OphidiaVenom say something whenever you receive a purple chest at TOB?",
		section = RAIDS,
		position = 202
	)
	default boolean tobPurpleChest()
	{
		return true;
	}

	@ConfigItem(
		keyName = "coxWhiteChest",
		name = "CoX White Chest",
		description = "Should OphidiaVenom say something whenever you get a white light at COX?",
		section = RAIDS,
		position = 301
	)
	default boolean coxWhiteChest()
	{
		return true;
	}

	@ConfigItem(
		keyName = "coxPurpleChest",
		name = "CoX Purple Chest",
		description = "Should OphidiaVenom say something whenever you get a purple light at COX?",
		section = RAIDS,
		position = 302
	)
	default boolean coxPurpleChest()
	{
		return true;
	}

	// =========================================================================
	// Livestream
	// =========================================================================

	@ConfigSection(
		name = "Livestream",
		description = "All livestream configurations.",
		position = 500
	)
	String LIVESTREAM_SECTION = "livestreamSection";

	@ConfigItem(
		keyName = "livestream",
		name = "Livestream Notification",
		description = "Should OphidiaVenom send a message whenever she is live?",
		section = LIVESTREAM_SECTION,
		position = 501
	)
	default boolean livestream()
	{
		return true;
	}

	@ConfigItem(
		keyName = "livestreamPlaySound",
		name = "Play Sound",
		description = "Should OphidiaVenom play a sound when she goes live?",
		section = LIVESTREAM_SECTION,
		position = 502
	)
	default boolean livestreamPlaySound()
	{
		return true;
	}

	@ConfigItem(
		keyName = "livestreamInterval",
		name = "Notification Interval",
		description = "Set the interval of the livestream notification message in minutes.",
		section = LIVESTREAM_SECTION,
		position = 503
	)
	default int livestreamInterval()
	{
		return 30;
	}

	@ConfigItem(
		keyName = "livestreamColor",
		name = "Notification Color",
		description = "Set the color of the livestream notification message.",
		section = LIVESTREAM_SECTION,
		position = 504
	)
	default Color livestreamColor()
	{
		return new Color(15,68,25);
	}

	// =========================================================================
	// Developer (always last)
	// =========================================================================
/*
	@ConfigSection(
		name = "Developer",
		description = "Developer mode configurations.",
		position = 900,
		closedByDefault = true
	)
	String DEVELOPER_SECTION = "developerSection";

	@ConfigItem(
		keyName = "developerLogging",
		name = "Developer Logging",
		description = "Enable developer logging when developer mode is active.",
		section = DEVELOPER_SECTION,
		position = 901
	)
	default boolean developerLogging()
	{
		return false;
	}
*/
	// =========================================================================
	// Internal (hidden)
	// =========================================================================

	@ConfigItem(
		keyName = SOUND_OVERRIDE_POOLS_KEY,
		name = "Sound Override Pools",
		description = "Internal storage for sound override selections.",
		position = 10000,
		hidden = true
	)
	default String soundOverridePools()
	{
		return "";
	}
}
