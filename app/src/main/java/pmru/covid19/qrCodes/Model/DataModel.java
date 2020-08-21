package pmru.covid19.qrCodes.Model;

public class DataModel {
    String id,name,used_qr_number;

    public DataModel() {
    }

    public DataModel(String id, String name,String used_qr_number) {
        this.id = id;
        this.name = name;
        this.used_qr_number = used_qr_number;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsed_qr_number() {
        return used_qr_number;
    }

    public void setUsed_qr_number(String used_qr_number) {
        this.used_qr_number = used_qr_number;
    }
}
