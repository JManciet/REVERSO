package tests;

import entites.Client;
import exceptions.CustomException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class ClientTest extends Client {

    @ParameterizedTest
    @ValueSource(doubles = {200})
    void setChiffreAffairesNonValide(double invalidValues) {
        assertThrows(CustomException.class, () -> {
            this.setChiffreAffaires(invalidValues);
        });
    }

    @ParameterizedTest
    @ValueSource(ints = {0})
    void setNbrEmployesNonValide(int invalidValues) {
        assertThrows(CustomException.class, () -> {
            this.setNbrEmployes(invalidValues);
        });
    }

}