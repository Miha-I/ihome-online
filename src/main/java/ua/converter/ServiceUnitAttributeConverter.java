package ua.converter;

import ua.model.Service;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class ServiceUnitAttributeConverter implements AttributeConverter<Service.UNIT, String> {
    @Override
    public String convertToDatabaseColumn(Service.UNIT attribute) {
        return attribute == null ? null : attribute.value();
    }

    @Override
    public Service.UNIT convertToEntityAttribute(String dbData) {
        return dbData == null ? null : Service.UNIT.fromValue(dbData).orElse(null);
    }
}
