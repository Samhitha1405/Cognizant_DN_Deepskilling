class WordDocument implements Document {
    public void open()      { System.out.println("Opening Word document (.docx)"); }
    public void save()      { System.out.println("Saving Word document (.docx)"); }
    public String getType() { return "Word"; }
}

class PdfDocument implements Document {
    public void open()      { System.out.println("Opening PDF document (.pdf)"); }
    public void save()      { System.out.println("Saving PDF document (.pdf)"); }
    public String getType() { return "PDF"; }
}

class ExcelDocument implements Document {
    public void open()      { System.out.println("Opening Excel document (.xlsx)"); }
    public void save()      { System.out.println("Saving Excel document (.xlsx)"); }
    public String getType() { return "Excel"; }
}