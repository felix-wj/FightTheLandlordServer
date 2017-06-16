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

	public Function() {
		gameHall = new GameHall();
	}

	public void run(IoSession session, String json) throws NumberFormatException, JSONException {
		JSONObject jstr = new JSONObject(json);

		int funCode = (Integer) jstr.get("action");
		JSONObject mjson = null;

		String rid = jstr.optString("rid");
		String uid = jstr.optString("uid");
		int bet = -1;
		switch (funCode) {
		case 1:// 登录，建立连接保存session

			gameHall.addPlayer(uid, session);

			break;
		case 2:// 创建房间
			gameHall.createRoom(uid,session);
			break;
		case 3:// 加入房间
			String result = gameHall.enterRoom(rid, uid,session);
			mjson = new JSONObject();
			mjson.put("action", 3);
			mjson.put("uid", uid);
			mjson.put("result", result);
			session.write(mjson.toString());
			if(result.equals("true"))
				gameHall.afterEnterRoom(rid, uid);
			break;
		case 4:// 刚进入房间，游戏开始前退出房间
			gameHall.outRoom(rid, uid);
			break;
		case 5:// 解散房间，申请退出，投票解散房间
			gameHall.delRoom(rid, uid);
			break;
		case 6:// 玩家准备
			gameHall.playerReady(rid, uid);
			break;
		case 7:// 玩家取消准备
			gameHall.playerCancelReady(rid, uid);
			break;
		case 8:// 叫分
			bet = jstr.getInt("bet");
			gameHall.makeBet(rid, uid, bet);
			break;
		case 9:// 出牌
			List<Integer> cards = new ArrayList<Integer>();
			JSONArray array = (JSONArray) jstr.get("cards");
			for (int i = 0; i < array.length(); i++)
				cards.add((Integer) array.get(i));
			gameHall.outCards(rid, uid, cards);
			break;
		case 10:// 不出
			gameHall.pass(rid, uid);
			break;
		case 11:// 结算后返回游戏
			gameHall.goBackToReady(rid, uid);
			break;
		case 12:// 投票是否同意解散房间,有一人同意即可解散房间
			boolean vote = jstr.getBoolean("vote");
			if (vote)
				gameHall.delRoom(rid);
			break;
		case 13:// 点击提示，求助AI出牌
			gameHall.askAIForCards(rid, uid);
			break;

		case 100:// 断线
			System.out.println("case100");
			gameHall.disconnect(session);
		}

	}

}
