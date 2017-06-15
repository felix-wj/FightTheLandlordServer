package com.FightLandlord.tool;

import java.util.List;

public class ListAndArray {
	public static int[] listToArray(List<Integer> list) {
		int size = list.size();
		int[] array = new int[size];
		for (int i = 0; i < size; i++) {
			array[i] = list.get(i);

		}
		return array;

	}
	public static int[] listToArrayForAI(List<Integer> list) {
		int size = list.size();
		int[] array = new int[20];
		for (int i = 0; i < 20; i++) {
			array[i] = 0;

		}
		for (int i = 0; i < size; i++) {
			array[list.get(i)%100]++;
		}
		return array;
	}
}
