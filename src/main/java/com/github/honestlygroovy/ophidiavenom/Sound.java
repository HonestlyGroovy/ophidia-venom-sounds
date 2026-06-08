package com.github.honestlygroovy.ophidiavenom;

public enum Sound
{

	DEATH("death", "DyingHCIMCompleted_r1.wav"),//#2
	COLLECTION_LOG_SLOT("collectionlog", "ColLogSlotCompleted_r1.wav"),//#3
	NO_PRAYER("noprayer", "NoPrayer.wav"),//#4
	NEW_PET("newpet", "NewPet.wav"),//#5
	PETTING_DOG("pettingdog", "PettingDog.wav"),//#6
	DECLINE_TRADE("declinetrade", "DeclineTrade.wav"),//#7
	CRAB_CHECK("crabcheck", "CrabCheck.wav"),//#8
	HUNTER_RUMOUR("hunterrumour", "HunterRumourCompleted.wav"),//#8
	HUNTER_RUMOUR_NOT_COMPLETED("hunterrumour", "HunterRumourNotCompleted.wav"),//#8

	KILLING_SOMEONE_1("playerkilling", "KillingSomeone_r1.wav"),//#9

	COMBAT_TASK("combattask", "CombatTaskCompleted_r1.wav"),//#15

	ACCEPTED_TRADE("accepttrade", "AcceptTrade.wav"),//#17

	GETTING_PURPLE_1("gettingpurple", "GettingPurple_r1.wav"),//#18
	GETTING_PURPLE_2("gettingpurple", "GettingPurple_r2.wav"),//#18

	DISMISSING_RANDOM_EVENT("dismissrandomevent", "DismissingRandomEvent.wav"),//#20
	TYPING_IN_BANKPIN("typingbankpin", "TypingInBankpin.wav"),//#21
	TOA_CHEST_OPENS("toachestopens", "ToaChestOpens.wav"),//#23
	WHITE_LIGHT_AFTER_RAID("whitelight", "WhiteLightAfterRaid.wav"),//#24
	TURNING_ON_RUN("turningonrun", "TurningOnRun.wav"),//#25
	CLICKING_PK_LOOT_CHEST("pkchest", "ClickingPkLootChest.wav"),//#26

	LEVEL_UP("levelup", "LevelUpCompleted_r1.wav"),
	QUEST("quest", "QuestCompleted_r1.wav"),
	ACHIEVEMENT_DIARY("achievementdiary", "AchievementDiary_r1.wav"),
	EASTER_EGG_STRAYDOG_BONE("givebone", "GiveBone.wav"),
	GO_LIVE("golive", "go_live.wav");

	private final String resourceName;
	private final String directory;

	Sound(String directory, String resourceName)
	{
		this.directory = directory;
		this.resourceName = resourceName;
	}


	public String getResourceName()
	{
		return resourceName;
	}

	public String getDirectory()
	{
		return directory;
	}
}
