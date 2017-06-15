package com.FightLandlord.tool;

public class AITool {
	// 自己出牌 mCards[]手牌，before 上家手牌数，after 下家手牌数，
	// landlord 地主：地主是上家值为1，地主是自己值为2，地主是下家值为3
	public static native int[] getCards(int[] mCards, int before, int after, int landlord, int rest[]);

	// 接牌 cards[] 某一玩家的出牌
	public static native int[] getCards(int[] mCards, int before, int after, int landlord, int rest[], int[] cards);

	// 叫地主 currentMaxBet目前场上最大叫分 num还剩几人没叫分
	public static native int getBet(int[] mCards, int currentMaxBet, int num);

	static {
		System.loadLibrary("AITool");
	}
}