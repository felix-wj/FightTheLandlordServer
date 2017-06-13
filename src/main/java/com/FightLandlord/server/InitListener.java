package com.FightLandlord.server;


import java.io.IOException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.springframework.context.support.ClassPathXmlApplicationContext;
public class InitListener implements ServletContextListener {
 public void contextDestroyed(ServletContextEvent arg0) {
  // TODO Auto-generated method stub
 }
 public void contextInitialized(ServletContextEvent sce) {
	 NioSocketAcceptor ioAcceptor= (NioSocketAcceptor) new ClassPathXmlApplicationContext("applicationContext.xml").getBean("ioAcceptor");
  //在tomcat的启动过程中,会看到控制台打印此语句.
//	 	try {
//			ioAcceptor.bind();
//			 System.out.println("********mina server 启动完毕*********");
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			 System.out.println("********mina server 启动失败*********");
//			e.printStackTrace();
//		}

 }
}