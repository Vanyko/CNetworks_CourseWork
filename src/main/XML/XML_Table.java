package main.XML;

import java.util.ArrayList;

public class XML_Table {
    ArrayList<XML_Row> rows;

    public XML_Table() {
        this.rows = new ArrayList<>();
    }

    public ArrayList<XML_Row> getRows() {
        return rows;
    }

    public void setRows(ArrayList<XML_Row> rows) {
        this.rows.clear();
        this.rows.addAll(rows);
    }

    public void addRow(XML_Row row){
        rows.add(row);
    }

    public void removeRow(XML_Row row){
        rows.remove(row);
    }

    public int getMaxColumn(){
        int result = 0;

        for (XML_Row row : rows){
            if (result < row.getColumns()){
                result = row.getColumns();
            }
        }

        return result;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("<Table");

        s.append("ss:ExpandedColumnCount=\"");
        s.append(getMaxColumn());
        s.append("\" ss:ExpandedRowCount=\"");
        s.append(getRows().size());
        s.append("\" x:FullColumns=\"1");
        s.append("\" x:FullRows=\"1");
        s.append("\"");

        for (XML_Row row : rows){
            s.append(row.toString());
        }

        s.append("</Table>");

        return s.toString();
    }
}
