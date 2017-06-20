package com.FightLandlord.control;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.mina.core.session.IoSession;
import org.json.JSONException;
import org.json.JSONObject;

import com.FightLandlord.model.Player;
import com.FightLandlord.model.Room;
import com.FightLandlord.model.User;
import com.FightLandlord.tool.AITool;
import com.FightLandlord.tool.ByteArrayAndInt;
import com.FightLandlord.tool.ListAndArray;
import com.FightLandlord.tool.RandomRoomID;

public class GameHall {
	Map<String, Room> rooms;
	Map<String, Player> players;
	Map<IoSession, String[]> sessions;
	public GameHall() { 
		rooms = new HashMap<String, Room>();
		players = new HashMap<String, Player>();
		sessions=new HashMap<IoSession,String[]>();
	}

	

	public Map<IoSession, String[]> getSessions() {
		return sessions;
	}



	public void setSessions(Map<IoSession, String[]> sessions) {
		this.sessions = sessions;
	}



	public Map<String, Room> getRooms() {
		return rooms;
	}

	public void setRooms(Map<String, Room> rooms) {
		this.rooms = rooms;
	}

	public Map<String, Player> getPlayers() {
		return players;
	}

	public void setPlayers(Map<String, Player> players) {
		this.players = players;
	}

	public void addPlayer(String uid,IoSession session) throws JSONException {
		Player player=new Player(new User(uid,5),session);
		players.put(player.getUser().getUid(), player);
		String[] location=new String[2];
		location[0]=uid;
		sessions.put(session, location);
		JSONObject mjson=new JSONObject();
		mjson.put("action", 1);
		mjson.put("uid", player.getUser().getUid());
		mjson.put("roomCards", player.getUser().getRoomCard());
		session.write(mjson.toString());
	}

	
	public void createRoom(String uid,IoSession session) throws JSONException {
		Player player = players.get(uid);
		JSONObject mjson = null;
		if (0 == player.getUser().getRoomCard()) {
			mjson = new JSONObject();
			mjson.put("action", 21);
			mjson.put("warning", "noRoomCards");
			return;
		}
		String rid = RandomRoomID.IDBuilder();
		Room room = new Room(rid, player, 3);
		rooms.put(rid, room);
		players.remove(player.getUser().getUid());
		sessions.get(session)[1]=rid;
		mjson = new JSONObject();
		mjson.put("action", 2);
		mjson.put("uid", uid);
		mjson.put("rid", rid);
		player.notice(mjson.toString());
	}

	public void delRoom(String rid, String uid) throws JSONException {

		Room room = rooms.get(rid);
		JSONObject mjson = new JSONObject();
		mjson.put("action", 24);
		mjson.put("uid", uid);
		room.noticeOthers(uid, mjson.toString());
		// if (uid.equals(room.getHost())) {
		// JSONObject mjson = new JSONObject();
		// mjson.put("action", 6);
		// mjson.put("uid", room.getHost());
		//
		// room.noticeOthers(room.getHost(), mjson.toString());
		// Map<Integer, Player> p = room.getPlayers();
		// // List<Player> p = room.getPlayerList();
		// for (Integer key : p.keySet()) {
		// players.put(p.get(key).getUser().getUid(), p.get(key));
		// }
		//
		// // for (Player per : p) {
		// // players.put(per.getUser().getUid(), per);
		// // }
		// rooms.remove(rid);
		// RandomRoomID.IDDelete(rid);
		//
	}

	public void delRoom(String rid) {

		Room room = rooms.get(rid);
		// room.cleanRoom();
		Map<Integer, Player> p = room.getPlayers();
		for (Integer key : p.keySet()) {
			players.put(p.get(key).getUser().getUid(), p.get(key));
		}
		rooms.remove(rid);
		RandomRoomID.IDDelete(rid);

	}

	public String enterRoom(String rid, String uid,IoSession session) throws JSONException {
		Player player = players.get(uid);
		Room room = rooms.get(rid);
		// if(player==null)
		// System.out.println("烫烫烫");
		// else System.out.println(uid);

		// if(room==null)
		// System.out.println("room烫烫烫");
		// else System.out.println(rid);
		if (null == room)
			return "noRoom";
		if (room.enterRoom(player)) {
			{
				sessions.get(session)[1]=rid;
				players.remove(uid);
				
				return "true";

			}
		} else
			return "full";
	}

	public void afterEnterRoom(String rid, String uid) throws JSONException {
		Room room = rooms.get(rid);
		Player player = room.getPlayer(uid);
		JSONObject mjson = new JSONObject();
		mjson.put("action", 4);
		mjson.put("uid", uid);
		// mjson.put("score", player.getScore());

		room.noticeOthers(uid, mjson.toString());
		// room.noticeAll(mjson.toString());

		mjson = new JSONObject();
		mjson.put("action", 41);
		mjson.put("uid", player.getUser().getUid());
		mjson.put("seat", room.getPlayerSeat());
		// mjson.put("score", room.getPlayerScore());
		mjson.put("readyInfo", room.allPlayersReadyInfo());
		player.notice(mjson.toString());
	}

	public void outRoom(String rid, String uid) throws JSONException {
		Room room = rooms.get(rid);
		Player player = room.getPlayer(uid);
		if (room.outRoom(uid)) {
			JSONObject mjson = new JSONObject();
			mjson.put("action", 5);
			mjson.put("uid", uid);
			room.noticeOthers(uid, mjson.toString());
			players.put(uid, player);
		}
	}

	// 玩家准备
	public void playerReady(String rid, String uid) throws JSONException {
		Room room = rooms.get(rid);
		if (room.getPlayer(uid).isReady())
			return;// 防止玩家多次点击准备
		JSONObject mjson = null;
		if (room.makeReady(uid)) {
			// 所有玩家准备状态，开始游戏

			gameBegin(room);

		} else {
			// 告知其他玩家某人进入准备状态
			mjson = new JSONObject();
			mjson.put("action", 7);
			mjson.put("uid", uid);
			room.noticeOthers(uid, mjson.toString());
			// room.noticeAll(mjson.toString());
		}
	}

	// 玩家取消准备
	public void playerCancelReady(String rid, String uid) throws JSONException {
		Room room = rooms.get(rid);
		room.cancelReady(uid);
		JSONObject mjson = new JSONObject();
		mjson.put("action", 8);
		mjson.put("uid", uid);

		room.noticeOthers(uid, mjson.toString());
	}

	public void gameBegin(Room room) throws JSONException {
		JSONObject mjson = null;
		Map<Integer, Player> p = room.getPlayers();
		room.setGameBegin(true);
		room.addTimes();
		if (1 == room.getTimes())
			room.getPlayer(room.getHost()).getUser().useRoomCard();// 消耗房卡
		mjson = new JSONObject();
		mjson.put("action", 20);
		mjson.put("times", room.getTimes());
		room.noticeAll(mjson.toString());

		room.handOutCards();// 发牌
		// List<Player> p = room.getPlayerList();
		// 告知玩家手牌
		for (Integer key : p.keySet()) {
			mjson = new JSONObject();
			mjson.put("action", 9);
			mjson.put("uid", p.get(key).getUser().getUid());
			mjson.put("selfCards", p.get(key).getmCards());
			p.get(key).notice(mjson.toString());
		}
		// for (Player per : p) {
		// mjson = new JSONObject();
		// mjson.put("action", 9);
		// mjson.put("uid", per.getUser().getUid());
		// mjson.put("selfCards", per.getmCards());
		// per.notice(mjson.toString());
		// }

		// 功能重复
		// // 广播玩家座次
		// mjson = new JSONObject();
		// mjson.put("action", 10);
		// mjson.put("seat", room.getPlayerSeat());
		// room.noticeAll(mjson.toString());

		// 如果是开放后第一局，需要随机选出叫地主玩家
		if (0 == room.getTimes()) {
			Random random = new Random();
			int turn = random.nextInt(3);
			room.setFirstAsk(turn);
			room.setCurrentPlayer(turn);

		} else {
			// 否则，叫地主玩家由上一局叫地主玩家顺延
			int turn = (1 + room.getFirstAsk()) % 3;
			room.setFirstAsk(turn);
			room.setCurrentPlayer(turn);
		}
		// 通知房间内所有玩家，轮到uid玩家回合叫分
		mjson = new JSONObject();
		mjson.put("action", 11);
		mjson.put("firstAskID", room.getPlayer(room.getFirstAsk()).getUser().getUid());
		mjson.put("currentUid", room.getCurrentPlayerID());
		room.noticeAll(mjson.toString());
	}

	// 玩家叫分
	public void makeBet(String rid, String uid, int bet) throws JSONException {
		Room room = rooms.get(rid);
		Player player = room.getPlayer(uid);
		player.setBet(bet);
		if (bet > room.getCurrentBet())
			room.setCurrentBet(bet);
		JSONObject mjson = new JSONObject();
		mjson.put("action", 12);
		mjson.put("bet", bet);
		mjson.put("currentBet", room.getCurrentBet());
		room.noticeAll(mjson.toString());
		if (3 == bet) {
			bringLandlord(room);
			return;
		} else {
			if (room.isAllMakeBet()) {// 叫分阶段轮询完
				if (room.isAllZeroBet())// 如果都叫了0分，即都不叫
				{
					// 都不叫时
					room.cleanRoom();
					mjson = new JSONObject();
					mjson.put("action", 14);// 都不叫，通知重新发牌
					room.noticeAll(mjson.toString());
					// 通知房间内所有玩家重新发牌

					room.handOutCards();// 发牌
					// List<Player> p = room.getPlayerList();
					// 告知玩家手牌
					Map<Integer, Player> p = room.getPlayers();
					for (Integer key : p.keySet()) {
						mjson = new JSONObject();
						mjson.put("action", 9);
						mjson.put("uid", p.get(key).getUser().getUid());
						mjson.put("selfCards", p.get(key).getmCards());
						p.get(key).notice(mjson.toString());
					}
					// 恢复叫分顺序
					room.setCurrentPlayer(room.getFirstAsk());

					// 通知房间内所有玩家，轮到uid玩家回合叫分
					mjson = new JSONObject();
					mjson.put("action", 11);
					mjson.put("firstAskID", room.getPlayer(room.getFirstAsk()).getUser().getUid());
					mjson.put("currentUid", room.getCurrentPlayerID());
					room.noticeAll(mjson.toString());

				} else
					bringLandlord(room);
				return;
			}
		}
		// 通知房间内所有玩家，轮到uid玩家回合叫分
		room.currentPlayerTurn();
		// room.setCurrentPlayer((room.getCurrentPlayer()+1)%3);
		mjson = new JSONObject();
		mjson.put("action", 11);
		mjson.put("firstAskID", room.getPlayer(room.getFirstAsk()).getUser().getUid());
		mjson.put("currentUid", room.getCurrentPlayerID());
		room.noticeAll(mjson.toString());

	}

	// 产生地主，通知房间内所有玩家地主uid
	public void bringLandlord(Room room) throws JSONException {

		Map<Integer, Player> p = room.getPlayers();
		int seat = -1;
		for (Integer key : p.keySet()) {

			if (p.get(key).getBet() == room.getCurrentBet())
				// uid=p.get(key).getUser().getUid();
				seat = key;
		}
		// Player player = room.getPlayer(uid);
		// room.setLandlord(room.getKey(player));
		// room.setCurrentPlayer(room.getKey(player));
		room.setLandlord(seat);
		room.setCurrentPlayer(seat);
		room.addLandlordCards();
		JSONObject mjson = null;
		mjson = new JSONObject();
		mjson.put("action", 13);
		mjson.put("landlordUid", room.getPlayer(seat).getUser().getUid());
		mjson.put("point", room.getPoint());
		mjson.put("landlordCards", room.getLandlordCards());
		room.noticeAll(mjson.toString());
	}

	// 出牌阶段，玩家出牌
	// 验证出的牌是否在玩家手牌存在：TRUE，通知其他玩家玩家出的牌；FALSE，刷新该玩家手牌，重新出牌
	// 若该玩家出牌成功且手牌为零，这局游戏进入结算阶段
	public void outCards(String rid, String uid, List<Integer> cards) throws JSONException {
		Room room = rooms.get(rid);
		Player player = room.getPlayer(uid);
		JSONObject mjson = null;
		boolean gameover = false;
		if (player.hasCards(cards)) {

			player.outCards(cards);
			player.addCount();
			if (room.changeMultiple(cards))// 如果出的牌为炸弹，倍数翻倍，通知所有玩家
			{
				mjson = new JSONObject();
				mjson.put("action", 19);

				mjson.put("point", room.getPoint());

				room.noticeAll(mjson.toString());
			}
			gameover = player.isCardsNumZero();
			room.currentPlayerTurn();
			mjson = new JSONObject();
			mjson.put("action", 15);
			mjson.put("uid", uid);
			mjson.put("cards", cards);
			mjson.put("gameover", gameover);
			mjson.put("currentUid", room.getCurrentPlayerID());
			room.noticeOthers(uid, mjson.toString());
		} // else 非法牌处理 待写

		if (gameover) {
			room.setLandlordWin(room.getSeat(player) == room.getLandlord());
			tellWiner(room);
			Map<String, List<Integer>> playerRestCards = new HashMap<String, List<Integer>>();
			Map<Integer, Player> p = room.getPlayers();
			for (Integer key : p.keySet()) {
				if (!p.get(key).getUser().getUid().equals(uid))
					playerRestCards.put(p.get(key).getUser().getUid(), p.get(key).getmCards());
			}
			mjson = new JSONObject();

			mjson.put("action", 16);
			mjson.put("playerRestCards", playerRestCards);
			mjson.put("currentGameScore", room.getPlayerCurrentGameScore());
			mjson.put("playersScore", room.getPlayerScore());
			room.noticeAll(mjson.toString());
			room.cleanRoom();
		}
	}

	public void pass(String rid, String uid) throws JSONException {
		Room room = rooms.get(rid);
		Player player = room.getPlayer(uid);
		room.updateLastOutCards();
		boolean gameover = player.isCardsNumZero();
		room.currentPlayerTurn();
		JSONObject mjson = new JSONObject();
		mjson.put("action", 17);
		mjson.put("uid", uid);
		mjson.put("gameover", gameover);
		mjson.put("currentUid", room.getCurrentPlayerID());
		room.noticeOthers(uid, mjson.toString());
	}

	public void tellWiner(Room room) throws JSONException {
		Map<Integer, Player> p = room.getPlayers();
		JSONObject mjson = null;
		if (room.isLandlordWin()) {
			// 地主赢了
			for (Integer key : p.keySet()) {
				mjson = new JSONObject();
				mjson.put("action", 18);
				mjson.put("uid", p.get(key).getUser().getUid());
				mjson.put("win", key == room.getLandlord());// 座号与地主座号相等为TRUE，即是地主位为TRUE，是农民位为FALSE
				p.get(key).notice(mjson.toString());
			}

		} else {
			// 农民赢了
			for (Integer key : p.keySet()) {
				mjson = new JSONObject();
				mjson.put("action", 18);
				mjson.put("uid", p.get(key).getUser().getUid());
				mjson.put("win", !(key == room.getLandlord()));// 同上，取反后地主位为FALSE，农民位为TRUE
				p.get(key).notice(mjson.toString());
			}
		}
	}

	public void goBackToReady(String rid, String uid) throws JSONException {
		Room room = rooms.get(rid);
		Player player = room.getPlayer(uid);
		if (room.getHost().equals(uid) && room.getMaxTimes() == room.getTimes()
				&& 0 == player.getUser().getRoomCard()) {
			delRoomForNoRoomCard(rid);
		} else {
			JSONObject mjson = new JSONObject();
			mjson.put("action", 23);
			player.notice(mjson.toString());
		}

	}

	public void delRoomForNoRoomCard(String rid) throws JSONException {
		Room room = rooms.get(rid);
		JSONObject mjson = new JSONObject();
		mjson.put("action", 22);
		mjson.put("host", room.getHost());
		mjson.put("cause", "noRoomCard");
		delRoom(rid);
	}

	public void askAIForCards(String rid, String uid) throws JSONException {
		Room room = rooms.get(rid);
		Player player = room.getPlayer(uid);
		int seat = room.getCurrentPlayer();
		ArrayList<Integer> restCards = new ArrayList<Integer>(room.getRestCards());
		restCards.removeAll(player.getmCards());
		int[] cards;
		List<Integer> mCards=player.getmCards();
		
		if (room.isFreeOutCards(seat)) {
			cards = AITool.getCards(ListAndArray.listToArrayForAI(mCards),
					room.getPreviousPlayer(seat).getCardsNum(), room.getNextPlayer(seat).getCardsNum(),
					room.relationBetweenlandlordAndPlayer(seat), ListAndArray.listToArrayForAI(restCards));
		}else
		{
			int[] cardsNeedResponse=ListAndArray.listToArrayForAI(room.cardsNeedResponse(seat));
			cards = AITool.getCards(ListAndArray.listToArrayForAI(mCards),
					room.getPreviousPlayer(seat).getCardsNum(), room.getNextPlayer(seat).getCardsNum(),
					room.relationBetweenlandlordAndPlayer(seat), ListAndArray.listToArrayForAI(restCards),cardsNeedResponse);
		}
		
		
		ArrayList<Integer>cardsToResponse=new ArrayList<Integer>();
		Integer card;
		for(int i=0;i<cards.length;i++)
		{
			for(int j=1;j<cards[i];j++)
			{
				card=i+j*100;
				if(mCards.contains(card)){
					cardsToResponse.add(card);
					
				}
			}
		}
		
		JSONObject mjson = new JSONObject();
		mjson.put("action", 25);
		mjson.put("uid", uid);
		mjson.put("cards", cardsToResponse);
		
	}
	
	//简单的处理玩家掉线,掉线通知其他玩家
	public void disconnect(IoSession session) throws JSONException
	{
	
		String[] location=sessions.get(session);
		
		if(location!=null)
		{
			Room room=rooms.get(location[1]);
			//Player player=room.getPlayer(location[0]);
			JSONObject mjson=new JSONObject();
			mjson.put("action", 100);
			mjson.put("disconnectUid", location[0]);
			room.noticeOthers(location[0], mjson.toString());
		}
	}
}
