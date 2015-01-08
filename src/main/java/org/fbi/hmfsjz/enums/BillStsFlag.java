package org.fbi.hmfsjz.enums;

import java.util.Hashtable;

/**
 * ���ݼ���״̬��־ 000-δ���� 100-��ȷ�ϼ���
 */
public enum BillStsFlag implements EnumApp {

    UNBOOK("000", "δ����"),
    BOOKED("100", "��ȷ��"),
    INPUT("900", "��¼");

    private String code = null;
    private String title = null;
    private static Hashtable<String, BillStsFlag> aliasEnums;

    BillStsFlag(String code, String title) {
        this.init(code, title);
    }

    @SuppressWarnings("unchecked")
    private void init(String code, String title) {
        this.code = code;
        this.title = title;
        synchronized (this.getClass()) {
            if (aliasEnums == null) {
                aliasEnums = new Hashtable();
            }
        }
        aliasEnums.put(code, this);
        aliasEnums.put(title, this);
    }

    public static BillStsFlag valueOfAlias(String alias) {
        return aliasEnums.get(alias);
    }

    public String getCode() {
        return this.code;
    }

    public String getTitle() {
        return this.title;
    }

    public String toRtnMsg() {
        return this.code + "|" + this.title;
    }
}
