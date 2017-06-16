package com.FightLandlord.server;

import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.json.JSONObject;

import com.FightLandlord.control.Function;

import java.util.logging.Logger;

public class FightLandlordServerHandler implements IoHandler {
	private Logger logger = Logger.getLogger(this.getClass().getName());
	private Function fun = new Function();
	int a = 0;

	
	public void exceptionCaught(IoSession session, Throwable arg1) {
		logger.warning("服务器启动发生异常，have a exception : " + arg1.getMessage());

	}

	public void inputClosed(IoSession session) throws Exception {
		// TODO Auto-generated method stub
		logger.info("连接关闭");
		
		System.out.println("sessioninputclosed断断断了：");
		session.close(true);

	}

	public void messageReceived(IoSession session, Object message) throws Exception {
		// TODO Auto-generated method stub

		String messageStr = message.toString();

		System.out.println("服务器收到消息：" + messageStr);

		fun.run(session, messageStr);
		//session.write("收到了");

	}

	public void messageSent(IoSession session, Object arg1) throws Exception {
		// TODO Auto-generated method stub

		logger.info("服务器成功发送信息: " + arg1.toString());
	}

	public void sessionClosed(IoSession session) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("session断断断了：");
		fun.run(session, "{\"action\":100}");
		
	}

	public void sessionCreated(IoSession arg0) throws Exception {
		// TODO Auto-generated method stub

	}

	public void sessionIdle(IoSession arg0, IdleStatus arg1) throws Exception {
		// TODO Auto-generated method stub

	}

	public void sessionOpened(IoSession arg0) throws Exception {
		// TODO Auto-generated method stub

	}
}
