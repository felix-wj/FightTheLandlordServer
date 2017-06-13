package com.FightLandlord.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.logging.LogLevel;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.SocketAcceptor;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;




public class FightLandlordServer  {
	
	
	 
    SocketAcceptor acceptor = null;
    
    FightLandlordServer(){
        acceptor = new NioSocketAcceptor();
    }
    
    public boolean bind(){
		//acceptor.getFilterChain().addLast("code", new ProtocolCodecFilter(new ObjectSerializationCodecFactory()));
		acceptor.getFilterChain().addLast(
			     "codec",
			     new ProtocolCodecFilter(new MyCodeFactory()));

        LoggingFilter log = new LoggingFilter();
        log.setMessageReceivedLogLevel(LogLevel.INFO);
        acceptor.getFilterChain().addLast("logger", log);
		acceptor.setHandler(new FightLandlordServerHandler());;

        acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 30);
        
        try {
            acceptor.bind(new InetSocketAddress(9123));
            return true;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
        
    }
    
    public void main(String []args) {  
    	 
        FightLandlordServer server = new FightLandlordServer();
        if(!server.bind()){
            System.out.println("服务器启动失败");
        }else{
            System.out.println("服务器启动成功");
        }
    }
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
