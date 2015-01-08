package org.fbi.hmfsjz.gateway.domain.txn;

import org.fbi.hmfsjz.gateway.domain.base.Toa;
import org.fbi.hmfsjz.gateway.domain.base.xml.ToaHeader;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.io.xml.DomDriver;

import java.io.Serializable;

/**
 * ά���ʽ�-֧ȡȷ��
 */

@XStreamAlias("HMROOT")
public class Toa3004 extends Toa {

    public ToaHeader INFO = new ToaHeader();
    public Body BODY = new Body();

    public static class Body  implements Serializable {
        /*
          DRAW_BILLNO	֧ȡ����
          BILL_STS_CODE	�ɿ�������
          BILL_STS_TITLE	�ɿ���˵��
          RESERVE	������
         */
            public String DRAW_BILLNO = "";
            public String BILL_STS_CODE = "";
            public String BILL_STS_TITLE = "";
            public String RESERVE = "";
    }

    @Override
    public Toa toToa(String xml) {
        XStream xs = new XStream(new DomDriver());
        xs.processAnnotations(Toa3004.class);
        return (Toa3004) xs.fromXML(xml);
    }
}
