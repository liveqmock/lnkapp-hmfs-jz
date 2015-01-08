package org.fbi.hmfsjz.enums;

import java.util.Hashtable;

/**
 * ֧ȡ����ѯ������
 */
public enum DrawQryStatus implements EnumApp {

    VALAID("0000", "֧ȡ����Ч"),
    NO_EXIST("0001", "û�д�֧ȡ��"),
    HANDLING("0002", "֧ȡ�����ڷ��ܾ����״̬"),
    ALREADY_DRAW("0003", "�Ѿ����֧ȡȷ��"),
    CANCELED("0004", "��֧ȡ���Ѿ�����");

    private String code = null;
    private String title = null;
    private static Hashtable<String, DrawQryStatus> aliasEnums;

    DrawQryStatus(String code, String title) {
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

    public static DrawQryStatus valueOfAlias(String alias) {
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
