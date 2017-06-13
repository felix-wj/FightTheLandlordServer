package com.FightLandlord.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Room {
	private String rid;
	private String host;// 房主的uid
	private int maxPlayerNum;// 房间最多人数
	private int currentPlayerNum;// 当前房间人数
	private int readyNum;// 准备状态人数
	// private List<Player> playerList;
	private Map<Integer, Player> players;// 按照key值排座位顺序 0,1,2
	private int landlord;// 地主玩家座号
	private int firstAsk;// 叫地主的玩家座号
	private int currentPlayer;// 当前回合所属玩家的座号
	private int landlordCards[];// 地主牌
	private boolean gameBegin;
	private int currentBet;// 当前叫分
	private int multiple;// 倍数
	private int times;// 累计局数
	private boolean landlordWin;// 地主是否获胜
	private int passCount;// 统计轮询时不出次数，防止出现三个玩家一直不出情况

	public Room(String rid, Player player, int max) {
		this.rid = rid;
		// playerList = new ArrayList<Player>();
		players = new HashMap<Integer, Player>();
		// playerList.add(player);
		players.put(0, player);
		host = player.getUser().getUid();
		currentPlayerNum = 1;
		maxPlayerNum = max;
		readyNum = 0;
		currentPlayer = -1;
		landlord = -1;
		firstAsk = -1;
		landlordCards = new int[3];
		gameBegin = false;
		times = 0;
		currentBet = 0;
		multiple = 1;
		landlordWin = false;
		passCount = 0;
	}

	public int getPassCount() {
		return passCount;
	}

	public void setPassCount(int passCount) {
		this.passCount = passCount;
	}

	public int getMultiple() {
		return multiple;
	}

	public void setMultiple(int multiple) {
		this.multiple = multiple;
	}

	public boolean isLandlordWin() {
		return landlordWin;
	}

	public void setLandlordWin(boolean landlordWin) {
		this.landlordWin = landlordWin;
	}

	public Map<Integer, Player> getPlayers() {
		return players;
	}

	public void setPlayers(Map<Integer, Player> players) {
		this.players = players;
	}

	public int getTimes() {
		return times;
	}

	public void setTimes(int times) {
		this.times = times;
	}

	public boolean isGameBegin() {
		return gameBegin;
	}

	public void setGameBegin(boolean gameBegin) {
		this.gameBegin = gameBegin;
	}

	public int getCurrentBet() {
		return currentBet;
	}

	public void setCurrentBet(int currentBet) {
		this.currentBet = currentBet;
	}

	public String getRid() {
		return rid;
	}

	public void setRid(String rid) {
		this.rid = rid;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getMaxPlayerNum() {
		return maxPlayerNum;
	}

	public void setMaxPlayerNum(int maxPlayerNum) {
		this.maxPlayerNum = maxPlayerNum;
	}

	public int getCurrentPlayerNum() {
		return currentPlayerNum;
	}

	public void setCurrentPlayerNum(int currentPlayerNum) {
		this.currentPlayerNum = currentPlayerNum;
	}

	public int getReadyNum() {
		return readyNum;
	}

	public void setReadyNum(int readyNum) {
		this.readyNum = readyNum;
	}

	// public List<Player> getPlayerList() {
	// return playerList;
	// }
	//
	// public void setPlayerList(List<Player> playerList) {
	// this.playerList = playerList;
	// }

	public int getLandlord() {
		return landlord;
	}

	public void setLandlord(int landlord) {
		this.landlord = landlord;
	}

	public int getCurrentPlayer() {
		return currentPlayer;
	}

	public void setCurrentPlayer(int currentPlayer) {
		this.currentPlayer = currentPlayer;
	}

	public int getFirstAsk() {
		return firstAsk;
	}

	public void setFirstAsk(int firstAsk) {
		this.firstAsk = firstAsk;
	}

	public int[] getLandlordCards() {
		return landlordCards;
	}

	public void setLandlordCards(int[] landlordCards) {
		this.landlordCards = landlordCards;
	}

	public String getCurrentPlayerID() {
		return players.get(currentPlayer).getUser().getUid();
	}

	public void handOutCards() {
		int cards[] = new int[54];
		int num = 0;
		for (int i = 3; i < 16; i++) {
			for (int j = 1; j < 5; j++) {
				cards[num] = i + j * 100;
				num++;
			}
		}
		cards[52] = 116;
		cards[53] = 117;
		Random random = new Random();
		for (int l = 0; l < 54; l++) {
			int des = random.nextInt(54);
			int temp = cards[l];
			cards[l] = cards[des];
			cards[des] = temp;
		}
		// //定手牌 测试
		// int j,m;
		// m=0;
		// for(j=103;j<117;j++)
		// {
		// cards[m]=j;
		// m++;
		// }
		// cards[14]=215;
		// cards[15]=315;
		// cards[16]=415;//玩家1 手牌 103~116 215 315 415
		// m+=3;
		// for(j=203;j<215;j++)
		// {
		// cards[m]=j;
		// m++;
		// }
		// cards[29]=303;
		// cards[30]=403;
		// cards[31]=304;
		// cards[32]=404;
		// cards[33]=117;
		//
		// m+=5;
		// for(j=305;j<315;j++)
		// {
		// cards[m]=j;
		// m++;
		// }
		// for(j=405;j<412;j++)
		// {
		// cards[m]=j;
		// m++;
		// }
		// cards[51]=412;
		// cards[52]=413;
		// cards[53]=414;

		int t = 0;
		for (int k = 0; k < maxPlayerNum; k++) {
			do {
				players.get(k).inCard(cards[t]);
				t++;
			} while (0 != (t % 17));
		}
		landlordCards[0] = cards[51];
		landlordCards[1] = cards[52];
		landlordCards[2] = cards[53];

		// //测试：
		// System.out.println("测试：玩家手牌");
		// System.out.println(players.get(0).getmCards().size());
		// List<Integer>list=players.get(0).getmCards();
		// for(Integer i:list)
		// System.out.print(i+" ");
	}

	public void addLandlordCards() {
		for (int i = 0; i < 3; i++)
			players.get(landlord).inCard(landlordCards[i]);
	}

	public void putOutCards(int cards[]) {

		players.get(currentPlayer).outCards(cards);
	}

	public boolean enterRoom(Player player) {

		if (currentPlayerNum == maxPlayerNum)
			return false;
		// playerList.add(player);
		// 如果是房主,直接加入，当前人数不做变化
		if (!player.getUser().getUid().equals(host))
			currentPlayerNum++;

		for (int i = 0; i < maxPlayerNum; i++) {
			if (players.containsKey(i)) {
				for (int j = 1; j < maxPlayerNum; j++) {
					if (!players.containsKey((j + i) % maxPlayerNum)) {
						players.put((j + i) % maxPlayerNum, player);
						return true;
					}
				}
			}
		}
		return false;
	}

	public void outRoom(String uid) {
		Player player = getPlayer(uid);
		if (player != null) {
			// playerList.remove(player);

			for (Integer key : players.keySet()) {
				if (players.get(key) == player) {
					players.remove(key);
					System.out.println("成功退出");
					break;
				}
			}

			// 如果是房主，退出时当前房间人数不做变化
			if (!player.getUser().getUid().equals(host))
				currentPlayerNum--;
		}

	}

	public Player getPlayer(String uid) {
		for (Integer key : players.keySet()) {
			if (players.get(key).getUser().getUid().equals(uid)) {
				return players.get(key);
			}
		}
		return null;
	}

	public Player getPlayer(Integer key) {
		return players.get(key);
	}

	public boolean makeReady(String uid) {
		for (Integer key : players.keySet()) {
			if (players.get(key).getUser().getUid().equals(uid)) {
				{
					if (!players.get(key).isReady()) {
						readyNum++;
						players.get(key).setReady(true);
					}
					break;
				}
			}
		}

		// for (Player p : playerList) {
		// if (p.getUser().getUid().equals(uid)) {
		// if (!p.isReady())
		// readyNum++;
		// }
		// }
		if (readyNum == maxPlayerNum) {
			// gameBegin = true;
			// handOutCards();

			// 发牌、确定叫地主玩家和当前回合玩家在GameHall的beginGame中执行
			// if (-1 == firstAsk) {
			// Random random = new Random();
			// firstAsk = random.nextInt(maxPlayerNum);
			// }

			return true;
		}
		return false;
	}

	public Map<String, Boolean> allPlayersReadyInfo() {
		Map<String, Boolean> readyInfo = new HashMap<String, Boolean>();
		for (Integer key : players.keySet()) {
			readyInfo.put(players.get(key).getUser().getUid(), players.get(key).isReady());
		}
		return readyInfo;
	}

	public void cancelReady(String uid) {
		for (Integer key : players.keySet()) {
			if (players.get(key).getUser().getUid().equals(uid)) {
				{
					readyNum--;
					break;
				}
			}
		}

		// for (Player p : playerList) {
		// if (p.getUser().getUid().equals(uid)) {
		// readyNum--;
		// }
		// }
	}

	// 准备，叫分，出牌，退出房间等信息广播给其他玩家
	public void noticeOthers(String uid, String mes) {

		for (Integer key : players.keySet()) {
			if (!players.get(key).getUser().getUid().equals(uid)) {
				{
					players.get(key).notice(mes);
					// break;
				}
			}
		}

		// for (Player p : playerList) {
		// if (!p.getUser().getUid().equals(uid)) {
		// p.notice(mes);
		// }
		// }
	}

	// 玩家座次，当前回合所属玩家ID，广播给房间内所有玩家
	public void noticeAll(String mes) {

		for (Integer key : players.keySet()) {
			players.get(key).notice(mes);
		}
	}

	public Map<Integer, String> getPlayerSeat() {

		Map<Integer, String> seat = new HashMap<Integer, String>();
		for (Integer key : players.keySet()) {
			seat.put(key, players.get(key).getUser().getUid());
		}

		return seat;
	}

	public Integer getKey(Player player) {
		Integer key = null;
		// Map,HashMap并没有实现Iteratable接口.不能用于增强for循环.
		for (Integer getKey : players.keySet()) {
			if (players.get(getKey).equals(player)) {
				key = getKey;
			}
		}
		return key;
	}

	// 检查是否都叫分了
	public boolean isAllMakeBet() {
		for (Integer key : players.keySet()) {
			if (-1 == players.get(key).getBet())
				return false;
		}
		return true;
	}

	// 检查是否都叫了0分，即都不叫
	public boolean isAllZeroBet() {
		for (Integer key : players.keySet()) {
			if (0 != players.get(key).getBet()) {
				return false;
			}
		}
		return true;
	}

	// currentPlayer轮转
	public void currentPlayerTurn() {
		currentPlayer = (1 + currentPlayer) % 3;
	}

	// 先叫地主标记firstAsk轮转
	public void firstAskTurn() {
		firstAsk = (1 + firstAsk) % 3;
	}

	public void addPassCount() {
		passCount++;
	}

	// 清理房间数据，准备下一局游戏
	public void cleanRoom() {
		readyNum = 0;
		for (Integer key : players.keySet()) {
			players.get(key).setReady(false);
			players.get(key).getmCards().clear();
			players.get(key).setBet(-1);
			players.get(key).setCount(0);
			
		}
		currentPlayer = -1;
		landlord = -1;
		gameBegin = false;
		currentBet = 0;
		times++;

		readyNum = 0;

	}
	// 更改倍数，判断牌型，是否有炸弹，有则倍数×2，返回TRUE

	public boolean changeMultiple(int cards[]) {
		if (2 == cards.length) {
			if (233 == (cards[0] + cards[1])) {
				multiple*=2;
				return true;
			}
			return false;
		}
		if(4==cards.length)
		{
			int a=cards[0]%100;
			for(int i=1;i<4;i++)
				if(cards[i]%100!=a)
					return false;
			multiple*=2;
			return true;
		}
		return false;
	}
	
	
	public int getPoint()//游戏当前底分
	{
		return multiple*currentBet;
	}
	
	
	//获得玩家累计得分
	public Map<String,Integer>getPlayerScore()
	{
		Map<String,Integer> scores=new HashMap<String,Integer>();
		for (Integer key : players.keySet()) {
			scores.put(players.get(key).getUser().getUid(), players.get(key).getScore());
		}

		return scores;
	}
	
	
	//获得玩家当前一局得分
	public Map<String,Integer>getPlayerCurrentGameScore()
	{
		Map<String,Integer> scores=new HashMap<String,Integer>();
		
		int landlordScore;
		int farmScorce;
		
			if (landlordWin)
			{
				
				landlordScore=getPoint()*2;
				farmScorce=-getPoint();
				if(0==players.get((landlord+1)%3).getCount()&&0==players.get((landlord+2)%3).getCount())
				{
					
					landlordScore*=2;
					farmScorce*=2;			
				}									
		}else{
			landlordScore=-getPoint()*2;
			farmScorce=getPoint();
			if(1==players.get(landlord).getCount())
			{
				landlordScore*=2;
				farmScorce*=2;	
			}
		}
			scores.put(players.get(landlord).getUser().getUid(), landlordScore);
			
			players.get(landlord).addScore(landlordScore);
			
			scores.put(players.get((landlord+1)%3).getUser().getUid(), farmScorce);
			
			players.get((landlord+1)%3).addScore(farmScorce);
			
			scores.put(players.get((landlord+2)%3).getUser().getUid(), farmScorce);
			
			players.get((landlord+2)%3).addScore(farmScorce);
		return scores;
	}
}