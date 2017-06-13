package com.FightLandlord.server;

import java.net.InetSocketAddress;

import org.apache.mina.core.future.CloseFuture;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.filter.logging.LogLevel;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.SocketConnector;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

public class SimpleMinaClient {
	public SocketConnector connector = null;
	public ConnectFuture future;
	public IoSession session = null;

	SimpleMinaClient() {

	}

	boolean connect() {
		try {
			connector = new NioSocketConnector();
			connector.setConnectTimeoutMillis(3000);
		
			connector.getFilterChain().addLast("code", new ProtocolCodecFilter(new MyCodeFactory()));
			LoggingFilter log = new LoggingFilter();
			log.setMessageReceivedLogLevel(LogLevel.INFO);
			connector.getFilterChain().addLast("logger", log);
			connector.setHandler(new SimpleClientHandler());

			future = connector.connect(new InetSocketAddress("127.0.0.1", 9133));
			future.awaitUninterruptibly();
			session = future.getSession();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	public void setAttribute(Object key, Object value) {
		session.setAttribute(key, value);
	}

	void sentMsg(String message) {
		session.write(message);
	}

	boolean close() {
		CloseFuture future = session.getCloseFuture();
		future.awaitUninterruptibly(1000);
		connector.dispose();
		return true;
	}

	public SocketConnector getConnector() {
		return connector;
	}

	public IoSession getSession() {
		return session;
	}

	
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SimpleMinaClient client = new SimpleMinaClient();
		// client.connect();
		if (client.connect()) {
			client.sentMsg("hello");
			// client.close();
		}

	}
}
