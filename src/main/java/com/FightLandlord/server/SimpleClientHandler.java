package com.FightLandlord.server;

import java.util.logging.Logger;

import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;



public class SimpleClientHandler implements IoHandler {
	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	@Override
	public void exceptionCaught(IoSession arg0, Throwable arg1) throws Exception {
		// TODO Auto-generated method stub
		logger.warning("客户端启动发生异常，have a exception : " + arg1.getMessage());

	}

	@Override
	public void inputClosed(IoSession arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		// TODO Auto-generated method stub
//		 if (message instanceof Person) {  
//	            Person person = (Person) message;  
//	            System.out.println("客户端收到信息："+person.getId()+" ####  "+person.getName());  	         
//	        } 
		String mes=(String )message;
		 System.out.println("客户端收到信息："+mes); 
		 session.write("客户端已经收到服务的的消息！！");
		 
	}

	@Override
	public void messageSent(IoSession arg0, Object message) throws Exception {
		// TODO Auto-generated method stub
		logger.info("客户端发了一个信息：" + message.toString());
	}

	@Override
	public void sessionClosed(IoSession arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sessionCreated(IoSession arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sessionIdle(IoSession arg0, IdleStatus arg1) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sessionOpened(IoSession arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
