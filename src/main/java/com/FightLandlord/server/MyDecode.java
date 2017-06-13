package com.FightLandlord.server;



import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

import com.FightLandlord.tool.ByteArrayAndInt;




public class MyDecode extends CumulativeProtocolDecoder {   
    

    public boolean doDecode(IoSession session, IoBuffer in,   
            ProtocolDecoderOutput out) throws Exception {   
           
    	System.out.println(in.remaining());
    	if (in.remaining() < 4) // 是用来当拆包时候剩余长度小于4的时候的保护，不加容易出错
		{
			return false;
		}
        if(in.remaining() > 0){  
        
        	
            byte [] sizeBytes = new byte[4];   
            in.mark();//标记当前位置，以便reset   
            in.get(sizeBytes);  
                      
            int size = ByteArrayAndInt.byteArrayToInt(sizeBytes);   
            System.out.println("长度："+size);
            if(size > in.remaining()){ 
                in.reset();   
                return false; 
            } else{   
                byte[] bytes = new byte[size];    
                in.get(bytes, 0, size);   
                String mes = new String(bytes,"UTF-8");   
            	System.out.println("读完了没：："+in.remaining());    
                out.write(mes);                        
                }   
                if(in.remaining() > 0){
                    return true;   
                }   
            }   
           
        return false; 
    }   
  
  
}  
 
