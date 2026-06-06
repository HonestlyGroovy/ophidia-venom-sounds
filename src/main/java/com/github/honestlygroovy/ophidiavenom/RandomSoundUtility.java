package com.github.honestlygroovy.ophidiavenom;

import java.util.Random;

public class RandomSoundUtility
{
	private static Random random = new Random();

	public static String getRandomSound(String[] sounds)
	{
		return sounds[random.nextInt(sounds.length)];
	}
}
