package org.fbi.hmfsjz.gateway.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.apache.commons.lang.StringUtils;
import org.fbi.hmfsjz.enums.TxnRtnCode;
import org.fbi.hmfsjz.gateway.domain.base.Tia;
import org.fbi.hmfsjz.gateway.domain.base.Toa;
import org.fbi.hmfsjz.gateway.domain.base.xml.TiaHeader;
import org.fbi.hmfsjz.gateway.domain.base.xml.ToaHeader;
import org.fbi.hmfsjz.helper.MD5Helper;
import org.fbi.hmfsjz.online.action.AbstractTxnAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MessageServerHandler extends SimpleChannelInboundHandler<String> {
    private static final Logger logger = LoggerFactory.getLogger(MessageServerHandler.class);

    @Override
    public void channelRead0(ChannelHandlerContext ctx, String requestBuffer) throws Exception {
        String responseBuffer = "";
        byte[] bytes = requestBuffer.getBytes();
        String reqsn = "";
        String wsysid = "";
        String txnCode = "";
        String txnDate = "";
        String rtnMsgHeader = "";
        Toa toa = null;
        try {
            // ��������ͷ
            byte[] headerBytes = new byte[64];
            System.arraycopy(bytes, 0, headerBytes, 0, headerBytes.length);
            String msgHeader = new String(headerBytes, "GBK");
            // ����������
            byte[] bodyBytes = new byte[bytes.length - 64];
            System.arraycopy(bytes, 64, bodyBytes, 0, bodyBytes.length);
            String msgData = new String(bodyBytes, "GBK");
            // ���ر��ĳ�ʼ��
            rtnMsgHeader = msgHeader.substring(0, 32);

            // ��Χϵͳ���롢�����롢�������ڡ�mac
            wsysid = msgHeader.substring(4, 14).trim().toUpperCase();
            txnCode = msgHeader.substring(14, 24).trim();
            txnDate = msgHeader.substring(24, 32).trim();
            String mac = msgHeader.substring(32);

            // MD5У��
            // Message Data���ּ���8λ�������ڼ����û�ID�������MD5ֵ
            String md5 = MD5Helper.getMD5String(msgData + txnDate + wsysid);
            // ��֤ʧ�� ������֤ʧ����Ϣ
            if (!md5.equals(mac)) {
                logger.info("MD5У�鲻һ�¡�[�����]:" + md5 + " [�ͻ���]:" + mac);
                throw new RuntimeException("MD5У�����");
            }

            Class tiaClazz = Class.forName("org.fbi.hmfsjz.gateway.domain.txn.Tia" + txnCode);
            Tia tia = (Tia) tiaClazz.newInstance();
            tia = tia.getTia(msgData);
            TiaHeader tiaHeader = (TiaHeader) tiaClazz.getField("INFO").get(tia);
            reqsn = tiaHeader.REQ_SN;

            Class txnClass = Class.forName("org.fbi.hmfsjz.online.action.Txn" + txnCode + "Action");
            AbstractTxnAction action = (AbstractTxnAction)txnClass.newInstance();
            toa = action.run(tia);

        } catch (Exception e) {
            // �����쳣��Ϣ
            String exmsg = e.getMessage();
            if (exmsg == null) exmsg = TxnRtnCode.OTHER_EXCEPTION.toRtnMsg();
            else if (!exmsg.contains("|")) exmsg = TxnRtnCode.OTHER_EXCEPTION.getCode() + "|" + exmsg;
            logger.error(exmsg, e);
            String[] arrExmsg = StringUtils.splitByWholeSeparatorPreserveAllTokens(exmsg, "|");
            Class toaClazz = Class.forName("org.fbi.hmfsjz.gateway.domain.txn.Toa" + txnCode);
            toa = (Toa) toaClazz.newInstance();
            ToaHeader toaHeader = new ToaHeader();
            toaHeader.REQ_SN = reqsn;
            toaHeader.RET_CODE = arrExmsg[0];
            toaHeader.ERR_MSG = arrExmsg[1];
            toaClazz.getField("INFO").set(toa, toaHeader);

        }
        String rtnMsgData = toa.toString();
        String txnMac = MD5Helper.getMD5String(rtnMsgData + txnDate + wsysid);
        rtnMsgHeader += txnMac;
        responseBuffer = rtnMsgHeader + rtnMsgData;
        ctx.writeAndFlush(responseBuffer);
        ctx.close();
    }


    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.info("ChannelInactived.");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("Unexpected exception from downstream.", cause);
        ctx.close();
    }

}
