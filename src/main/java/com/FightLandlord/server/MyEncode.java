package com.FightLandlord.server;

import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

import com.FightLandlord.tool.ByteArrayAndInt;



public class MyEncode extends ProtocolEncoderAdapter {   

       
    public void encode(IoSession session, Object message,   
        ProtocolEncoderOutput out) throws Exception {   
    
        IoBuffer buffer = IoBuffer.allocate(100).setAutoExpand(true);   
           
        String mes = (String) message;   
           
        
        byte[] bytes = mes.getBytes();   
        byte[] sizeBytes = ByteArrayAndInt.intToByteArray(bytes.length);   
           
        buffer.put(sizeBytes);//将前4位设置成数据体的字节长度   
        buffer.put(bytes);//消息内容   
        buffer.flip();   
        out.write(buffer);   
    }

	
  
}  