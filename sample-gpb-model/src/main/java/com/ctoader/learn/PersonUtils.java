package com.ctoader.learn;

public class PersonUtils {

    public static PersonWrapper.Person makePerson(Integer index) {
        return PersonWrapper.Person.newBuilder()
                .setId(index)
                .setName("Pearson " + index)
                .setDescription("This is some description we are sending.")
                .build();
    }
}
