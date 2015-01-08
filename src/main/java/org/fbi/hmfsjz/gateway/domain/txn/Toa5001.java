package org.fbi.hmfsjz.gateway.domain.txn;

import org.fbi.hmfsjz.gateway.domain.base.Toa;
import org.fbi.hmfsjz.gateway.domain.base.xml.ToaHeader;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.io.HierarchicalStreamDriver;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import com.thoughtworks.xstream.io.xml.XppDriver;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * ά���ʽ�-���з���ֻ���Ϣ��ѯ-���ڶ���
 */

@XStreamAlias("HMROOT")
public class Toa5001 extends Toa {

    public ToaHeader INFO = new ToaHeader();
    public Body BODY = new Body();

    public static class Body implements Serializable {
        /*
          ACCOUNT_NUM	�˻�����
         */
        public String ACCOUNT_NUM = "0";
        public String RESERVE = "";
        @XStreamImplicit
        public List<Account> ACCOUNTS = new ArrayList<Account>();
    }

    @XStreamAlias("ACCOUNT")
    public static class Account implements Serializable {
        public String HOUSE_ACCOUNT = "";
        public String STATUS = "";
        public String HOUSE_ID = "";
        public String HOUSE_LOCATION = "";
        public String HOUSE_AREA = "";
        public String STANDARD = "";
        public String PAY_MONEY = "";
        public String BALAMT = "";
        public String ACCEPT_DATE = "";
    }


    @Override
    public String toString() {
        this.INFO.TXN_CODE = "5001";
        XmlFriendlyNameCoder replacer = new XmlFriendlyNameCoder("$", "_");
        HierarchicalStreamDriver hierarchicalStreamDriver = new XppDriver(replacer);
        XStream xs = new XStream(hierarchicalStreamDriver);
        xs.processAnnotations(Toa5001.class);
        return "<?xml version=\"1.0\" encoding=\"GBK\"?>" + "\n" + xs.toXML(this);
    }

    @Override
    public Toa toToa(String xml) {
        XStream xs = new XStream(new DomDriver());
        xs.processAnnotations(Toa5001.class);
        return (Toa5001) xs.fromXML(xml);
    }
}
