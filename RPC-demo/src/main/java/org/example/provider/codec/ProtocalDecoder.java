package org.example.provider.codec;

import com.caucho.hessian.io.Hessian2Input;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.example.common.InvokeProtocal;

import java.io.ByteArrayInputStream;
import java.util.List;

public class ProtocalDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        System.out.println("Decoding: " + byteBuf);
        int i = byteBuf.readableBytes();
        byte[] bytes = new byte[i];
        byteBuf.readBytes(bytes);
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        Hessian2Input hi = new Hessian2Input(bais);
        Object object = hi.readObject();
        list.add(object);
    }
}
