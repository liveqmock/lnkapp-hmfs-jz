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
 * ά���ʽ�-�ֻ���Ϣ��ѯ-���ܷ������н���
 */

@XStreamAlias("HMROOT")
public class Tia4001 extends Tia {
    public TiaHeader INFO = new TiaHeader();
    public Body BODY = new Body();

    public static class Body implements Serializable {
        /*
        BANK_ID	����ID		�ǿ�
        ACCOUNT_NUM �˻���   �ǿ�
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
        xs.processAnnotations(this.getClass());
        return (Tia4001) xs.fromXML(xml);
    }

}
