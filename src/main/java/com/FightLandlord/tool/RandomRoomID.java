package com.FightLandlord.tool;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class RandomRoomID {
	private static Set<String> ridSet=new HashSet<String>();
	private static Random random=new Random();
	public static String IDBuilder()
	{
		
		StringBuffer strbuf=new StringBuffer();
		
		for(int i=0;i<5;i++)
		{
			strbuf.append(random.nextInt(10));
		}
		
		while(ridSet.contains(strbuf.toString()))
		{
			strbuf.setLength(0);
			for(int i=0;i<5;i++)
			{
				strbuf.append(random.nextInt(10));
			}
			
		}
		ridSet.add(strbuf.toString());
		return strbuf.toString();
	}
	
	public static void IDDelete(String rid)
	{
		ridSet.remove(rid);
	}
}
