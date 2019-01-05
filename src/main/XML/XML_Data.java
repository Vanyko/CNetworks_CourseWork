package main.XML;

public class XML_Data {
    private Integer m_int;
    private String m_str;

    public XML_Data(int i) {
        this.m_int = i;
        this.m_str = null;
    }

    public XML_Data(String s) {
        this.m_int = null;
        this.m_str = s;
    }

    public String createData(String data){
        String s = "<XML_Data ss:Type=\"String\">";
        s += data;
        s+= "</XML_Data>";

        return s;
    }

    public String createData(int data){
        String s = "<XML_Data ss:Type=\"String\">";
        s += data;
        s+= "</XML_Data>";

        return s;
    }

    public Integer getI() {
        return m_int;
    }

    public String getS() {
        return m_str;
    }

    public void setI(Integer i) {
        this.m_int = i;
    }

    public void setS(String s) {
        this.m_str = s;
    }

    @Override
    public String toString() {
        String s = "<XML_Data ss:Type=\"";
        if (m_int == null){
            s += "String\">";
            s += m_str;
        } else {
            s += "Number\">";
            s += m_int;
        }
        s+= "</XML_Data>";

        return s;
    }
}
