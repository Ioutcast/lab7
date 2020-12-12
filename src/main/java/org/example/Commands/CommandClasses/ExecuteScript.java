package org.example.Commands.CommandClasses;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class ExecuteScript extends AbstractCommand {
    private static int ScriptCounter = 0;
    private static ArrayList<String> arrayList = new ArrayList<>();

    public ExecuteScript() {
        command = "execute_script";
        helpText = "считать и исполнить скрипт из указанного файла";
        NeedAnStr = true;
    }

    public ArrayList<String> ScriptCommands() {
        ScriptCounter = 0;
        return arrayList;
    }

    public void GetExecute(String string) throws ParserConfigurationException, TransformerException, IOException {
        ScriptCounter++; //Счётчик вложений в файл
        if (ScriptCounter < 10) {
            try {
                FileReader fileReader = new FileReader(string);
                Scanner scanner1 = new Scanner(fileReader);
                readAndExecute(scanner1);
            } catch (FileNotFoundException e) {
                System.out.println("Файл " + string + " не найден");

            }
        }
    }

    private void readAndExecute(Scanner scanner) throws IOException, TransformerException, ParserConfigurationException, NoSuchElementException {
        String str;

        if (arrayList.size() > 100) {
            System.out.println("Превышение лимита " + arrayList.size());
        } else {

            try {
                while ((str = scanner.nextLine()) != null) {
                    if (!str.contains("execute_script")) {
                        arrayList.add(str);
                    }
                    if (str.contains("execute_script") && str.split(" ").length == 2) {

                        String[] s = str.split(" ");
                        GetExecute(s[1]);
                    }
                }
            } catch (NoSuchElementException e) {
            }
        }
    }

}
