package org.fbi.hmfsjz.enums;

import java.util.Hashtable;

/**
 * �˻�״̬
 */
public enum ActStatus implements EnumApp {

    VALID("0000", "��Ч"),
    NOT_EXIST("0001", "������"),
    CANCEL("0002", "����");

    private String code = null;
    private String title = null;
    private static Hashtable<String, ActStatus> aliasEnums;

    ActStatus(String code, String title) {
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

    public static ActStatus valueOfAlias(String alias) {
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
