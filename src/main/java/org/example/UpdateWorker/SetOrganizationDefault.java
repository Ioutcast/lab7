package org.example.UpdateWorker;

import org.example.UpdateWorker.UpdateOrganizationField.setOrganizationField;
import org.example.Worker.Address;
import org.example.Worker.Location;
import org.example.Worker.Organization;
import org.example.Worker.OrganizationType;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class SetOrganizationDefault implements Serializable {
    public static Organization setOrganizationDefault(List<String> arguments, boolean isAuto1, BufferedReader reader) throws IOException {
        System.out.println("Ввод организации. Организация null? Если да  введите -> \"\", нет -> любое слово ");
        Organization organization = null;
        boolean key = true;
        if (isAuto1) {
            if (arguments.remove(0).equals(""))
                key = false;
        } else if (reader.readLine().equals(""))
            key = false;
        if (key) {
            Long employeesCount = null;
            System.out.println("Введите employeesCount ");
            employeesCount = setOrganizationField.setEmployeesCount(arguments, isAuto1, reader, employeesCount);


            OrganizationType type = null;
            System.out.println("Введите OrganizationType (COMMERCIAL/PUBLIC/PRIVATE_LIMITED_COMPANY/OPEN_JOINT_STOCK_COMPANY)");
            type = setOrganizationField.setOrganizationType(arguments, isAuto1, reader, type);

            String street = "";
            System.out.println("Введите street");
            street = setOrganizationField.setStreet(arguments, isAuto1, reader, street);

            String zipCode = "";
            System.out.println("Введите zipCode");
            zipCode = setOrganizationField.setZipCode(arguments, isAuto1, reader, zipCode);

            Double locationX = null;
            System.out.println("Введите locationX");
            locationX = setOrganizationField.setLocationX(arguments, isAuto1, reader, locationX);

            Integer locationY = null;
            System.out.println("Введите locationY");
            locationY = setOrganizationField.setLocationY(arguments, isAuto1, reader, locationY);

            String locationName = "";
            System.out.println("Введите locationName");
            locationName = setOrganizationField.setLocationName(arguments, isAuto1, reader, locationName);

            organization = new Organization(employeesCount, type, new Address(street, zipCode, new Location(locationX, locationY, locationName)));
        }
        return organization;
    }

    public static Organization createOrganization(List<String> arguments, boolean isAuto1) throws IOException {
        BufferedInputStream bf = new BufferedInputStream(System.in);
        BufferedReader reader = new BufferedReader(new InputStreamReader(bf, StandardCharsets.UTF_8));
        Organization organization = setOrganizationDefault(arguments, isAuto1, reader);
        return organization;
    }
}
