package org.fbi.hmfsjz.enums;

import java.util.Hashtable;

/**
 * �ɿ�������� 00-�տ�  10-�˿� 20-֧ȡ 99-����
 */
public enum BillBookType implements EnumApp {

    OPEN("99", "����"),
    DEPOSIT("00", "�տ�"),
    DEPOSIT_AGAIN("30", "����"),
    REFUND("10", "�˿�"),
    DRAW("20", "֧ȡ"),
    INTEREST_DRAW_CURRENT("80", "֧ȡʱ������Ϣ"),
    INTEREST_SCHE_CURRENT("90", "����������Ϣ"),
    INTEREST_SCHE_FIXED("91", "���������¶�����Ϣ");

    private String code = null;
    private String title = null;
    private static Hashtable<String, BillBookType> aliasEnums;

    BillBookType(String code, String title) {
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

    public static BillBookType valueOfAlias(String alias) {
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

    public String typeToQryDetail() {
        if ("00".equals(this.code)) return "0001";
        if ("30".equals(this.code)) return "0004";
        if ("20".equals(this.code)) return "0003";
        if ("10".equals(this.code)) return "0002";
        if ("80".equals(this.code)) return "0005";
        if ("90".equals(this.code)) return "0005";
        if ("91".equals(this.code)) return "0006";
        return "";
    }
}
