package ua.converter;

import ua.model.Service;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class ServiceTypeAttributeConverter implements AttributeConverter<Service.TYPE, String> {
    @Override
    public String convertToDatabaseColumn(Service.TYPE attribute) {
        return attribute == null ? null : attribute.value();
    }

    @Override
    public Service.TYPE convertToEntityAttribute(String dbData) {
        return dbData == null ? null : Service.TYPE.fromValue(dbData);
    }
}
