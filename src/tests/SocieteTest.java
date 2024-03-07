package tests;

import entites.Societe;
import exceptions.CustomException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SocieteTest extends Societe {

    @ParameterizedTest
    @NullSource
    @EmptySource
    @ValueSource(strings = {"\n", "\r",
            "123456789"})
    void setTelephoneNonValid(String invalidValues) {
        assertThrows(CustomException.class, () -> {
            this.setTelephone(invalidValues);
        });
    }

    @ParameterizedTest
    @EmptySource
    @ValueSource(strings = {"\n", "\r","aa",
            "a@","@a"})
    void setEMailNonValid(String invalidValues) {
        assertThrows(CustomException.class, () -> {
            this.setEMail(invalidValues);
        });
    }

    @ParameterizedTest
    @ValueSource(strings = {"a@a"})
    void setEMailValid(String validValues) throws CustomException {
        this.setEMail(validValues);
        assertEquals("a@a", getEMail());
    }
}