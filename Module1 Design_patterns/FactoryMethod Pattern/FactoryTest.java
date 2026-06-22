public class FactoryTest {
    public static void main(String[] args) {
        DocumentFactory[] factories = {
            new WordDocumentFactory(),
            new PdfDocumentFactory(),
            new ExcelDocumentFactory()
        };

        for (DocumentFactory factory : factories) {
            Document doc = factory.createDocument();
            System.out.println("Created: " + doc.getType());
            doc.open();
            doc.save();
            System.out.println();
        }
    }
}