package org.example.Commands.CommandClasses;


public class Save extends AbstractCommand {
    public Save() {
        command = "save";
        helpText = "сохранить коллекцию в файл";
        NeedAnStr = false;
    }
}

//    @Override
//    public String execute(DataBaseManager dataBaseManager, String arg) throws IOException {
//        try {
//            XmlWriteToFile.xmlWriterToFile(getMapCollection(), getNewXml());
//        } catch (ParserConfigurationException | TransformerException e) {
//            e.printStackTrace();
//        }
//        stop();
//        System.out.println("Данные сохранены.");
//        return "Данные сохранены.";
//    }
//}
