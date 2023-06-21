package dev.gether.getmetin.data;

public class ItemInvData {
    private String key;
    private String index;

    private RemoveType removeType;

    public ItemInvData(String key, String index, RemoveType removeType) {
        this.key = key;
        this.index = index;
        this.removeType = removeType;
    }

    public String getKey() {
        return key;
    }

    public String getIndex() {
        return index;
    }

    public RemoveType getRemoveType() {
        return removeType;
    }
}
