package com.FightLandlord.model;

import java.util.LinkedList;
import java.util.List;

import org.apache.mina.core.session.IoSession;

public class Player {
	private User user;
	private IoSession ioSession;
	private List<Integer> mCards;
	private int bet;// 叫分 0,1,2,3 未叫-1
	private boolean isReady;
	private int count;//统计出牌次数
	private int score;
	public Player(User user, IoSession session) {
		this.user = user;
		ioSession = session;
		mCards = new LinkedList<Integer>();
		bet = -1;
		isReady = false;
		count=0;
		score=0;
	}

	public boolean isReady() {
		return isReady;
	}

	public void setReady(boolean isReady) {
		this.isReady = isReady;
	}

	public int getBet() {
		return bet;
	}

	public void setBet(int bet) {
		this.bet = bet;
	}

	public List<Integer> getmCards() {
		return mCards;
	}

	public void setmCards(List<Integer> mCards) {
		this.mCards = mCards;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public IoSession getIoSession() {
		return ioSession;
	}

	public void setIoSession(IoSession ioSession) {
		this.ioSession = ioSession;
	}

	public void inCard(int card) {
		mCards.add(card);
	}

	
	
	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public void outCards(int cards[]) {
		for (int i = 0; i < cards.length; i++) {
		
			mCards.remove((Integer)cards[i]);
		}

	}

	public boolean isCardsNumZero()
	{
		if(0==mCards.size())
			return true;
		else return false;
	}
	public void cleanCards() {
		mCards.clear();
	}

	public void makeBet(int num) {
		bet = num;
	}

	public void notice(String mes) {
		ioSession.write(mes);
		
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public boolean hasCards(int cards[]) {
		for (int i = 0; i < cards.length; i++) {
			if (!mCards.contains(cards[i])) {
				return false;
			}
		}
		return true;
	}
	
	public void addCount()
	{
		count++;
	}
	public void addScore(int s)
	{
		score+=s;
	}
}
