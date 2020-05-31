package com.accounts.services.dataservice;

import com.accounts.dao.personRus.FemaleFirstNameDAO;
import com.accounts.dao.personRus.FemaleLastNameDAO;
import com.accounts.dao.personRus.MaleFirstNameDAO;
import com.accounts.dao.personRus.MaleLastNameDAO;
import com.accounts.models.*;
import com.ibm.icu.text.Transliterator;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Scope("prototype")
public class RandomDataImpl implements RandomData {
    @Autowired
    private FemaleFirstNameDAO femaleFirstNameDAO;
    @Autowired
    private FemaleLastNameDAO femaleLastNameDAO;
    @Autowired
    private MaleFirstNameDAO maleFirstNameDAO;
    @Autowired
    private MaleLastNameDAO maleLastNameDAO;

    @Override
    public String getFirstName(Sex sex) {
        String name = null;
        if (sex.equals(Sex.MALE)) {
            List<MaleFirstName> maleFirstNames = maleFirstNameDAO.findAll();
            int randomNumber = (int) (Math.random() * (maleFirstNames.size() - 1));
            name = maleFirstNames.get(randomNumber).getValue();
        } else if (sex.equals(Sex.FEMALE)) {
            List<FemaleFirstName> femaleFirstNames = femaleFirstNameDAO.findAll();
            int randomNumber = (int) (Math.random() * (femaleFirstNames.size() - 1));
            name = femaleFirstNames.get(randomNumber).getValue();
        }
        return name;
    }

    @Override
    public String getLastName(Sex sex) {
        String lastName = null;
        if (sex.equals(Sex.MALE)) {
            List<MaleLastName> maleLastNames = maleLastNameDAO.findAll();
            int randomNumber = (int) (Math.random() * (maleLastNames.size() - 1));
            lastName = maleLastNames.get(randomNumber).getValue();
        } else if (sex.equals(Sex.FEMALE)) {
            List<FemaleLastName> femaleLastNames = femaleLastNameDAO.findAll();
            int randomNumber = (int) (Math.random() * (femaleLastNames.size() - 1));
            lastName = femaleLastNames.get(randomNumber).getValue();
        }
        return lastName;
    }

    @Override
    public String getEmail(String firstName, String lastName) {
        Transliterator toLatinTrans = Transliterator.getInstance("Russian-Latin/BGN");

        String firstLetterOfNameEng = toLatinTrans
                .transliterate(firstName)
                .toLowerCase()
                .substring(0, 1);
        String lastNameEng = toLatinTrans
                .transliterate(lastName)
                .toLowerCase()
                .replace("\'", "")
                .replace("ʹ", "");

        int max = 1000;
        int min = 1;
        int range = max - min + 1;
        int randomNumber = (int) (Math.random() * range) + min;

        return firstLetterOfNameEng + "." + lastNameEng + randomNumber;
    }

    @Override
    public String getPassword() {
        return RandomStringUtils.random(10, true, true);
    }

    @Override
    public Sex getState() {
        int randomNumber = (int) (Math.random() * 2);
        return Sex.values()[randomNumber];
    }
}
