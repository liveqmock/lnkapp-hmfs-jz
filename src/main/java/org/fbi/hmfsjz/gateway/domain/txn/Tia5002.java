package org.fbi.hmfsjz.gateway.domain.txn;

import com.thoughtworks.xstream.io.HierarchicalStreamDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import com.thoughtworks.xstream.io.xml.XppDriver;
import org.fbi.hmfsjz.gateway.domain.base.Tia;
import org.fbi.hmfsjz.gateway.domain.base.xml.TiaHeader;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.io.xml.DomDriver;
import org.fbi.hmfsjz.helper.ProjectConfigManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * ά���ʽ�-�ύ�ֻ���Ϣ��ϸ
 */

@XStreamAlias("HMROOT")
public class Tia5002 extends Tia {
    public TiaHeader INFO = new TiaHeader();
    public Body BODY = new Body();

    public static class Body implements Serializable {
        /*
        BANK_ID	����ID		�ǿ�
        BANKUSER_ID	�����û�ID	�ǿ�	���ܶ�ϵͳ�������û���ID
        DETAIL_NUM	��ϸ��   �ǿ�
        RESERVE	������	�����ֶ�	�ɿ�
         */
        public String BANK_ID = "";
        public String BANKUSER_ID = "";
        public String RESERVE = "";
        public String DETAIL_NUM = "0";
        @XStreamImplicit
        public List<Detail> DETAILS = new ArrayList<Detail>();

    }

    @XStreamAlias("DETAIL")
    public static class Detail implements Serializable {
        public String HOUSE_ACCOUNT = "";
        public String INTEREST_NO = "";
        public String INTEREST_NAME = "";
        public String HOUSE_ID = "";
        public String HOUSE_LOCATION = "";
        public String OWNER = "";
        public String BEFORE_AMT = "";
        public String AFTER_AMT = "";
        public String BEGIN_DATE = "";
        public String END_DATE = "";
        public String CAPITAL = "";
        public String RATE = "";
        public String INTEREST = "";
    }

    @Override
    public Tia getTia(String xml) {
        XStream xs = new XStream(new DomDriver());
        xs.processAnnotations(Tia5002.class);
        return (Tia5002) xs.fromXML(xml);
    }

    @Override
    public String toString () {
        this.INFO.TXN_CODE = "5002";
        this.BODY.BANKUSER_ID = ProjectConfigManager.getInstance().getProperty("bank.userid");
        this.BODY.BANK_ID = ProjectConfigManager.getInstance().getProperty("bank.id");
        this.BODY.DETAIL_NUM = String.valueOf(this.BODY.DETAILS.size());
        XmlFriendlyNameCoder replacer = new XmlFriendlyNameCoder("$", "_");
        HierarchicalStreamDriver hierarchicalStreamDriver = new XppDriver(replacer);
        XStream xs = new XStream(hierarchicalStreamDriver);
        xs.processAnnotations(Tia5002.class);
        return "<?xml version=\"1.0\" encoding=\"GBK\"?>" + "\n" + xs.toXML(this);
    }
}
