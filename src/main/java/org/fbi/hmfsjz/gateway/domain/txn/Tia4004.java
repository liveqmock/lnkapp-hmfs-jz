package org.fbi.hmfsjz.gateway.domain.txn;

import org.fbi.hmfsjz.gateway.domain.base.Tia;
import org.fbi.hmfsjz.gateway.domain.base.xml.TiaHeader;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.io.xml.DomDriver;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * ά���ʽ�-�ֻ�������ˮ��ѯ-���ܷ������н���
 */

@XStreamAlias("HMROOT")
public class Tia4004 extends Tia {
    public TiaHeader INFO = new TiaHeader();
    public Body BODY = new Body();

    public static class Body implements Serializable {
        /*
        BANK_ID	����ID		�ǿ�
        RESERVE	������	�����ֶ�	�ɿ�
         */
        public String BANK_ID = "";
        public String RESERVE = "";
        public String ACCOUNT_NUM = "0";

        @XStreamImplicit(itemFieldName = "HOUSE_ACCOUNT")
        public List<String> ACCOUNTS = new ArrayList<String>();
    }

    @Override
    public Tia getTia(String xml) {
        XStream xs = new XStream(new DomDriver());
        xs.processAnnotations(Tia4004.class);
        return (Tia4004) xs.fromXML(xml);
    }

}
