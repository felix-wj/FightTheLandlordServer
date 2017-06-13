package com.FightLandlord.model;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table(name="gameinfo")
public class GameInfo implements Serializable {
	
	@Id       
	@GeneratedValue(strategy=GenerationType.IDENTITY) 
	@Column(name = "id", nullable = false)  
	private Integer id;
	private String firstSeat;
	private String secondSeat;
	private String thirdSeat;
	private String landlordId;
	private String host;
	private String firstAsk;
	private String content;
	private boolean winner;
	private Timestamp beginTime;
	private Timestamp endTime;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getFirstSeat() {
		return firstSeat;
	}
	public void setFirstSeat(String firstSeat) {
		this.firstSeat = firstSeat;
	}
	public String getSecondSeat() {
		return secondSeat;
	}
	public void setSecondSeat(String secondSeat) {
		this.secondSeat = secondSeat;
	}
	public String getThirdSeat() {
		return thirdSeat;
	}
	public void setThirdSeat(String thirdSeat) {
		this.thirdSeat = thirdSeat;
	}
	public String getLandlordId() {
		return landlordId;
	}
	public void setLandlordId(String landlordId) {
		this.landlordId = landlordId;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public String getFirstAsk() {
		return firstAsk;
	}
	public void setFirstAsk(String firstAsk) {
		this.firstAsk = firstAsk;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public boolean isWinner() {
		return winner;
	}
	public void setWinner(boolean winner) {
		this.winner = winner;
	}
	public Timestamp getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(Timestamp beginTime) {
		this.beginTime = beginTime;
	}
	public Timestamp getEndTime() {
		return endTime;
	}
	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}
	
	
}
