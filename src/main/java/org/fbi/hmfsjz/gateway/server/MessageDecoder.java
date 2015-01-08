package org.fbi.hmfsjz.gateway.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * ���Ľ���
 */
public class MessageDecoder  extends ByteToMessageDecoder {
    private static final Logger logger = LoggerFactory.getLogger(MessageDecoder.class);
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {

        if (in.readableBytes() < 8) {
            return;
        }

        byte[] lengthBuffer = new byte[8];
        in.getBytes(0,lengthBuffer);

        int dataLength = Integer.parseInt(new String(lengthBuffer).trim());
        if (in.readableBytes() < dataLength) {
            return;
        }

        byte[] decoded = new byte[dataLength - 8];
        in.skipBytes(8);
        in.readBytes(decoded);
        String msg = null;
        try {
            msg = new String(decoded, "GBK");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("���Ľ����ַ�������.", e);
        }
        logger.info("[LNKAPP-HMFS-JM ���ձ������ݣ�]" + msg);
        out.add(msg);
    }
}
