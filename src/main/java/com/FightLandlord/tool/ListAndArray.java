package com.FightLandlord.tool;

import java.lang.reflect.Array;
import java.util.List;

public class ListAndArray {
	public static int[] listToArray(List<Integer>list)
	{
		int size=list.size();
		int[] array=new int[size];
		for(int i=0;i<size;i++)
		{
			array[i]=list.get(i);
			
		}
		return array;
		
	}
}
