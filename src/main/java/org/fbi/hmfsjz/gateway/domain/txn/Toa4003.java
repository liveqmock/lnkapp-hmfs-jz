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
 * ά���ʽ�-���˻�����Ϣ��ѯ 4003
 */

@XStreamAlias("HMROOT")
public class Toa4003 extends Toa {
    public ToaHeader INFO = new ToaHeader();
    public Body BODY = new Body();

    public static class Body implements Serializable {
        /*
         STS_CODE �������
         STS_MSG ���˵��
         RESERVE	������
         HOUSE_SUM	��������
         AREA_SUM	�������
         MONEY_SUM	�ֻ��ʽ�����
         PAY_SUM	ҵ�������ʽ�����
         INTEREST_SUM	�ֻ���Ϣ����
         DRAW_SUM	�ʽ�֧ȡ����
         */

        public String STS_CODE = "";
        public String STS_MSG = "";
        public String HOUSE_SUM = "";
        public String AREA_SUM = "";
        public String MONEY_SUM = "";
        public String PAY_SUM = "";
        public String INTEREST_SUM = "";
        public String DRAW_SUM = "";
        public String RESERVE = "";
    }

    @Override
    public String toString() {
        this.INFO.TXN_CODE = "4003";
        XmlFriendlyNameCoder replacer = new XmlFriendlyNameCoder("$", "_");
        HierarchicalStreamDriver hierarchicalStreamDriver = new XppDriver(replacer);
        XStream xs = new XStream(hierarchicalStreamDriver);
        xs.processAnnotations(Toa4003.class);
        return "<?xml version=\"1.0\" encoding=\"GBK\"?>" + "\n" + xs.toXML(this);
    }

    @Override
    public Toa toToa(String xml) {
        XStream xs = new XStream(new DomDriver());
        xs.processAnnotations(Toa4003.class);
        return (Toa4003) xs.fromXML(xml);
    }
}
