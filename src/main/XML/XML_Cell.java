package main.XML;

public class XML_Cell {
    private XML_Cell data;

    public XML_Cell(XML_Cell data) {
        this.data = data;
    }

    public XML_Cell getData() {
        return data;
    }

    public void setData(XML_Cell data) {
        this.data = data;
    }

    @Override
    public String toString() {
        String s = "<Cell>";
        s += data.toString();
        s+= "</Cell>";

        return s;
    }
}
