package com.FightLandlord.control;



import java.util.ArrayList;
import java.util.List;

import org.apache.mina.core.session.IoSession;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.FightLandlord.model.Player;
import com.FightLandlord.model.User;
import com.FightLandlord.tool.ListAndArray;

public class Function {
	private GameHall gameHall;
	public Function()
	{
		gameHall=new GameHall();
	}
	public void  run( IoSession session, String json) throws NumberFormatException, JSONException
	{
		JSONObject jstr= new JSONObject(json);
	
		
		int funCode=(Integer) jstr.get("action");
		JSONObject mjson=null;
		
		String rid=jstr.optString("rid");
		String uid=jstr.optString("uid");
		int bet=-1;
		switch(funCode)
		{
		case 1://登录，建立连接保存session
			Player player=new Player(new User(uid,5),session);
			gameHall.addPlayer(player);
			mjson=new JSONObject();
			mjson.put("action", 1);
			mjson.put("uid", player.getUser().getUid());
			mjson.put("roomCards", player.getUser().getRoomCard());
			session.write(mjson.toString());
			break;
		case 2://创建房间
			rid=gameHall.createRoom(uid);
			mjson=new JSONObject();
			mjson.put("action", 2);
			mjson.put("uid", jstr.getString("uid"));
			mjson.put("rid", rid);
			
			session.write(mjson.toString());
			break;
		case 3://加入房间
			String result=gameHall.enterRoom(rid, uid);
			mjson=new JSONObject();
			mjson.put("action", 3);
			mjson.put("uid", uid);
			mjson.put("result", result);			
			session.write(mjson.toString());
			
			gameHall.afterEnterRoom(rid, uid);
			break;
		case 4://退出房间	
			gameHall.outRoom(rid, uid);			
			break;
		case 5://房主解散房间
			gameHall.delRoom(rid, uid);
			break;
		case 6://玩家准备
			gameHall.playerReady(rid, uid);
			break;
		case 7://玩家取消准备
			gameHall.playerCancelReady(rid, uid);
			break;
		case 8://叫分
			bet=jstr.getInt("bet");
			gameHall.makeBet(rid, uid, bet);
			break;
		case 9://出牌
			List<Integer>cards=new ArrayList<Integer>();
			JSONArray array=(JSONArray) jstr.get("cards");
		//	cards=(List<Integer>) jstr.get("cards");
			for(int i=0;i<array.length();i++)
				cards.add((Integer)array.get(i));
			int[] arrayCards=ListAndArray.listToArray( cards);
			gameHall.outCards(rid, uid, arrayCards);
			break;
		case 10://不出
			gameHall.pass(rid, uid);
		}
		
	}
	
}
