package org.fbi.hmfsjz.enums;

import java.util.Hashtable;

/**
 * �ɿ��ѯ������
 */
public enum BillQryStatus implements EnumApp {

    VALAID("0000", "�ɴ�֪ͨ����Ч"),
    NO_EXIST("0001", "û�д˽ɴ�֪ͨ��"),
    HANDLING("0002", "�ɴ�֪ͨ����������״̬"),
    ALREADY_PAYED("0003", "�Ѿ�����տ�ȷ��"),
    CANCELED("0004", "�ɴ�֪ͨ���Ѿ�����");

    private String code = null;
    private String title = null;
    private static Hashtable<String, BillQryStatus> aliasEnums;

    BillQryStatus(String code, String title) {
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

    public static BillQryStatus valueOfAlias(String alias) {
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
