package org.fbi.hmfsjz.enums;

import java.util.Hashtable;

/**
 * ���ͱ�־ 0-δ���� 1-�ѷ���
 */
public enum SendFlag implements EnumApp {

    UNSEND("0", "δ����"),
    SENT("1", "�ѷ���"),
    SENT_ERROR("2", "����ʧ��");

    private String code = null;
    private String title = null;
    private static Hashtable<String, SendFlag> aliasEnums;

    SendFlag(String code, String title) {
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

    public static SendFlag valueOfAlias(String alias) {
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
