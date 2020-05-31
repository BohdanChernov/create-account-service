package com.accounts.services.dataservice;

import com.accounts.dao.personRus.FemaleFirstNameDAO;
import com.accounts.dao.personRus.FemaleLastNameDAO;
import com.accounts.dao.personRus.MaleFirstNameDAO;
import com.accounts.dao.personRus.MaleLastNameDAO;
import com.accounts.models.FemaleFirstName;
import com.accounts.models.FemaleLastName;
import com.accounts.models.MaleFirstName;
import com.accounts.models.MaleLastName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

@Component
@Scope("prototype")
public class PutDataServiceImpl implements PutDataService {
    @Autowired
    private FemaleFirstNameDAO femaleFirstNameDAO;
    @Autowired
    private FemaleLastNameDAO femaleLastNameDAO;
    @Autowired
    private MaleFirstNameDAO maleFirstNameDAO;
    @Autowired
    private MaleLastNameDAO maleLastNameDAO;

    @Override
    public void putFemaleFirstNames(String path) {
        try {
            FileReader fileReader = new FileReader(path);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            while (bufferedReader.ready()) {
                String line = bufferedReader.readLine();

                try {
                    femaleFirstNameDAO.save(new FemaleFirstName(line));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void putFemaleLastNames(String path) {
        try {
            FileReader fileReader = new FileReader(path);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            while (bufferedReader.ready()) {
                String line = bufferedReader.readLine();

                try {
                    femaleLastNameDAO.save(new FemaleLastName(line));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void putMaleFirstNames(String path) {
        try {
            FileReader fileReader = new FileReader(path);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            while (bufferedReader.ready()) {
                String line = bufferedReader.readLine();

                try {
                    maleFirstNameDAO.save(new MaleFirstName(line));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void putMaleLastNames(String path) {
        try {
            FileReader fileReader = new FileReader(path);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            while (bufferedReader.ready()) {
                String line = bufferedReader.readLine();

                try {
                    maleLastNameDAO.save(new MaleLastName(line));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
