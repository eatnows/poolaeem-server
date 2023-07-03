package com.poolaeem.poolaeem.common.encrypto;

import jakarta.persistence.AttributeConverter;
import org.springframework.stereotype.Component;

@Component
public class TextEncryptConverter implements AttributeConverter<String, String> {

    private final AESEncryptor encryptor;

    public TextEncryptConverter(AESEncryptor encryptor) {
        this.encryptor = encryptor;
    }

    @Override
    public String convertToDatabaseColumn(String attribute) {
        if (attribute == null) {
            return null;
        }
        return encryptor.encrypt(attribute);
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        return encryptor.decrypt(dbData);
    }
}
