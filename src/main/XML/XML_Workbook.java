package main.XML;

import java.util.ArrayList;

public class XML_Workbook {
    ArrayList<XML_Worksheet> worksheets;

    public XML_Workbook() {
        this.worksheets = new ArrayList<>();
    }

    public ArrayList<XML_Worksheet> getWorksheets() {
        return worksheets;
    }

    public void setWorksheets(ArrayList<XML_Worksheet> worksheets) {
        this.worksheets.clear();
        this.worksheets.addAll(worksheets);
    }

    public void addWorksheet(XML_Worksheet worksheet){
        worksheets.add(worksheet);
    }

    public void removeWorksheet(XML_Worksheet worksheet){
        worksheets.remove(worksheet);
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("<Workbook xmlns=\"urn:schemas-microsoft-com:office:spreadsheet\"\n" +
                " xmlns:o=\"urn:schemas-microsoft-com:office:office\"\n" +
                " xmlns:x=\"urn:schemas-microsoft-com:office:excel\"\n" +
                " xmlns:ss=\"urn:schemas-microsoft-com:office:spreadsheet\"\n" +
                " xmlns:html=\"http://www.w3.org/TR/REC-html40\">");

        for (XML_Worksheet worksheet : worksheets){
            s.append(worksheet.toString());
        }

        s.append("</Workbook>");

        return s.toString();
    }
}
