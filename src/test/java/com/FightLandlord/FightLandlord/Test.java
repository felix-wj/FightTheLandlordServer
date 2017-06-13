package com.FightLandlord.FightLandlord;

import com.FightLandlord.tool.ByteArrayAndInt;

public class Test {
public static void main(String[] args) {
	String test="a";
	byte[] bytes=test.getBytes();
	byte[] size=new byte[4];
	size[0]=bytes[0];
	size[1]=bytes[1];
	size[2]=bytes[2];
	size[3]=bytes[3];
	int num=ByteArrayAndInt.byteArrayToInt(size); 
	System.out.println(num);
}
}
