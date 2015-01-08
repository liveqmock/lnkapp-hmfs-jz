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
 * ά���ʽ�-�ɿ��ѯ 1001
 */

@XStreamAlias("HMROOT")
public class Toa1001 extends Toa {
    public ToaHeader INFO = new ToaHeader();
    public Body BODY = new Body();

    public static class Body implements Serializable {
        /*
        PAY_BILLNO     �ɿ��
        BILL_STS_CODE	�ɿ״̬����
        BILL_STS_TITLE	�ɿ״̬˵��
        HOUSE_ID	���ݱ��
        HOUSE_LOCATION	��������
        HOUSE_AREA	�������
        STANDARD	�ɴ��׼
        TXN_AMT	�ɴ���
        PAY_BANK	�ɿ�����
        AREA_ACCOUNT	ר���˺�
        HOUSE_ACCOUNT	�ֻ��˺�
        CARD_TYPE	֤������
        CARD_NO	֤������
        OWNER	ҵ������
        TEL	��ϵ�绰
        RESERVE	������
         */

        public String PAY_BILLNO = "";
        public String BILL_STS_CODE = "";
        public String BILL_STS_TITLE = "";
        public String HOUSE_ID = "";
        public String HOUSE_LOCATION = "";
        public String HOUSE_AREA = "";
        public String STANDARD = "";
        public String TXN_AMT = "";
        public String PAY_BANK = "";
        public String AREA_ACCOUNT = "";
        public String HOUSE_ACCOUNT = "";
        public String CARD_TYPE = "";
        public String CARD_NO = "";
        public String OWNER = "";
        public String TEL = "";
        public String RESERVE = "";
    }

    @Override
    public String toString() {
        this.INFO.TXN_CODE = "1001";
        XmlFriendlyNameCoder replacer = new XmlFriendlyNameCoder("$", "_");
        HierarchicalStreamDriver hierarchicalStreamDriver = new XppDriver(replacer);
        XStream xs = new XStream(hierarchicalStreamDriver);
        xs.processAnnotations(Toa1001.class);
        return "<?xml version=\"1.0\" encoding=\"GBK\"?>" + "\n" + xs.toXML(this);
    }

    @Override
    public Toa toToa(String xml) {
        XStream xs = new XStream(new DomDriver());
        xs.processAnnotations(Toa1001.class);
        return (Toa1001) xs.fromXML(xml);
    }
}
