package org.fbi.hmfsjz.enums;

import java.util.Hashtable;

/**
 * �˿��ѯ������
 */
public enum RefundQryStatus implements EnumApp {

    VALAID("0000", "�˿�֪ͨ����Ч"),
    NO_EXIST("0001", "û�д��˿�֪ͨ��"),
    HANDLING("0002", "�˿�֪ͨ����������״̬"),
    ALREADY_REFUND("0003", "�Ѿ�����˿�ȷ��"),
    CANCELED("0004", "�˿�֪ͨ���Ѿ�����");

    private String code = null;
    private String title = null;
    private static Hashtable<String, RefundQryStatus> aliasEnums;

    RefundQryStatus(String code, String title) {
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

    public static RefundQryStatus valueOfAlias(String alias) {
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
