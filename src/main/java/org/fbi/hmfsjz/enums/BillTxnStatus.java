package org.fbi.hmfsjz.enums;

import java.util.Hashtable;

/**
 * �ɿ�ȷ�Ϸ�����
 */
public enum BillTxnStatus implements EnumApp {

    PAYED_SECCESS("0000", "�ɴ��տ�ȷ�ϳɹ�"),
    NO_EXIST("0001", "û�д˽ɴ�֪ͨ��"),
    UNCHECKED("0002", "�ɴ�֪ͨ����������״̬"),
    BANK_PAYED("0003", "�ɴ�֪ͨ�������Ѿ�����տ�"),
    CANCELED("0004", "�ɴ�֪ͨ���Ѿ�����"),
    UNCONNECT("0005", "��δ����������ִ���տ�ȷ��"),
    CONFIRMED("0006", "�ɴ�֪ͨ�����տ�ȷ��");

    private String code = null;
    private String title = null;
    private static Hashtable<String, BillTxnStatus> aliasEnums;

    BillTxnStatus(String code, String title) {
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

    public static BillTxnStatus valueOfAlias(String alias) {
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
