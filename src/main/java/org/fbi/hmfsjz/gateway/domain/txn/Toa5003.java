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
 * ά���ʽ�-�ֻ����� 5003
 */

@XStreamAlias("HMROOT")
public class Toa5003 extends Toa {
    public ToaHeader INFO = new ToaHeader();
    public Body BODY = new Body();

    public static class Body implements Serializable {
        /*
         STS_CODE �������
         STS_MSG ���˵��
         RESERVE	������
         */

        public String STS_CODE = "";
        public String STS_MSG = "";
        public String RESERVE = "";
    }

    @Override
    public String toString() {
        this.INFO.TXN_CODE = "5003";
        XmlFriendlyNameCoder replacer = new XmlFriendlyNameCoder("$", "_");
        HierarchicalStreamDriver hierarchicalStreamDriver = new XppDriver(replacer);
        XStream xs = new XStream(hierarchicalStreamDriver);
        xs.processAnnotations(Toa5003.class);
        return "<?xml version=\"1.0\" encoding=\"GBK\"?>" + "\n" + xs.toXML(this);
    }

    @Override
    public Toa toToa(String xml) {
        XStream xs = new XStream(new DomDriver());
        xs.processAnnotations(Toa5003.class);
        return (Toa5003) xs.fromXML(xml);
    }
}
