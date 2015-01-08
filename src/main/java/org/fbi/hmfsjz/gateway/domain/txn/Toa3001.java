package org.fbi.hmfsjz.gateway.domain.txn;

import org.fbi.hmfsjz.gateway.domain.base.Toa;
import org.fbi.hmfsjz.gateway.domain.base.xml.ToaHeader;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.io.HierarchicalStreamDriver;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import com.thoughtworks.xstream.io.xml.XppDriver;

import java.io.Serializable;

/**
 * ά���ʽ�-�˿��ѯ 3001
 */

@XStreamAlias("HMROOT")
public class Toa3001 extends Toa {
    public ToaHeader INFO = new ToaHeader();
    public Body BODY = new Body();

    public static class Body implements Serializable {
        /*
        REFUND_BILLNO �˿��
        BILL_STS_CODE	�˿״̬����
        BILL_STS_TITLE	����״̬˵��
        PAY_BILL_NO	�ɴ�֪ͨ����
        PAY_BANK	�տ�����
        BANK_USER	�����տ���
        BANK_CFM_DATE	�����տ�����
        PAY_MONEY	�����տ���
        HOUSE_ID	���ݱ��
        HOUSE_LOCATION	��������
        HOUSE_AREA	�������
        STANDARD	�ɴ��׼
        AREA_ACCOUNT	ר���˺�
        HOUSE_ACCOUNT	�ֻ��˺�
        CARD_TYPE	֤������
        CARD_NO	֤������
        OWNER	ҵ������
        TEL	��ϵ�绰
        ACCEPT_DATE	�ɴ���������
        RESERVE	������
         RP_TYPE	�˿����
        RP_MEMO	�˿�����
        RP_MONEY	�˿���
         */
        public String REFUND_BILLNO = "";
        public String BILL_STS_CODE = "";
        public String BILL_STS_TITLE = "";
        public String RP_TYPE = "";
        public String RP_MEMO = "";
        public String RP_MONEY = "";
        public String PAY_BILL_NO = "";
        public String PAY_BANK = "";
        public String BANK_USER = "";
        public String BANK_CFM_DATE = "";
        public String PAY_MONEY = "";
        public String HOUSE_ID = "";
        public String HOUSE_LOCATION = "";
        public String HOUSE_AREA = "";
        public String STANDARD = "";
        public String AREA_ACCOUNT = "";
        public String HOUSE_ACCOUNT = "";
        public String CARD_TYPE = "";
        public String CARD_NO = "";
        public String OWNER = "";
        public String TEL = "";
        public String ACCEPT_DATE = "";
        public String RESERVE = "";
    }

    @Override
    public String toString() {
        this.INFO.TXN_CODE = "3001";
        XmlFriendlyNameCoder replacer = new XmlFriendlyNameCoder("$", "_");
        HierarchicalStreamDriver hierarchicalStreamDriver = new XppDriver(replacer);
        XStream xs = new XStream(hierarchicalStreamDriver);
        xs.processAnnotations(Toa3001.class);
        return "<?xml version=\"1.0\" encoding=\"GBK\"?>" + "\n" + xs.toXML(this);
    }

    @Override
    public Toa toToa(String xml) {
        XStream xs = new XStream(new DomDriver());
        xs.processAnnotations(Toa3001.class);
        return (Toa3001) xs.fromXML(xml);
    }
}
