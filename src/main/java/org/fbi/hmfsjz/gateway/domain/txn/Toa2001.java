package org.fbi.hmfsjz.gateway.domain.txn;

import org.fbi.hmfsjz.gateway.domain.base.Toa;
import org.fbi.hmfsjz.gateway.domain.base.xml.ToaHeader;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.io.xml.DomDriver;

import java.io.Serializable;

/**
 * 维修资金-缴款确认
 */

@XStreamAlias("HMROOT")
public class Toa2001 extends Toa {

    public ToaHeader INFO = new ToaHeader();
    public Body BODY = new Body();

    public static class Body  implements Serializable {
        /*
          PAY_BILLNO	缴存通知单号
          BILL_STS_CODE	缴款结果代码
          BILL_STS_TITLE	缴款结果说明
          RESERVE	保留域
         */
            public String PAY_BILLNO = "";
            public String BILL_STS_CODE = "";
            public String BILL_STS_TITLE = "";
            public String RESERVE = "";
    }



    @Override
    public Toa toToa(String xml) {
        XStream xs = new XStream(new DomDriver());
        xs.processAnnotations(Toa2001.class);
        return (Toa2001) xs.fromXML(xml);
    }
}
