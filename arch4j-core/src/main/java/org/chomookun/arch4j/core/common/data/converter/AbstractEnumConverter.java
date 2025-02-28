package org.chomookun.arch4j.core.common.data.converter;

import jakarta.persistence.Converter;
import lombok.extern.slf4j.Slf4j;

import jakarta.persistence.AttributeConverter;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.type.descriptor.java.BasicJavaType;
import org.hibernate.type.descriptor.jdbc.JdbcType;
import org.hibernate.type.spi.TypeConfiguration;
import org.hibernate.type.spi.TypeConfigurationAware;
import org.hibernate.usertype.BaseUserTypeSupport;

import java.lang.reflect.ParameterizedType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Optional;
import java.util.function.BiConsumer;

@Converter
@Slf4j
public abstract class AbstractEnumConverter<E extends Enum<E>> extends BaseUserTypeSupport<E> implements AttributeConverter<E,String>, TypeConfigurationAware {

    private final Class<E> enumType;

    private final int sqlType = Types.VARCHAR;

    private TypeConfiguration typeConfiguration;

    public AbstractEnumConverter() {
        this.enumType = detectEnumType();
    }

    private Class<E> detectEnumType() {
        ParameterizedType type = (ParameterizedType) getClass().getGenericSuperclass();
        //noinspection unchecked
        return (Class<E>) type.getActualTypeArguments()[0];
    }

    @Override
    public String convertToDatabaseColumn(E attribute) {
        return Optional.ofNullable(attribute)
                .map(Enum::name)
                .orElse(null);
    }

    @Override
    public E convertToEntityAttribute(String dbData) {
        try {
            return Enum.valueOf(enumType, dbData);
        }catch(Throwable t){
            return null;
        }
    }

    protected void resolve(BiConsumer<BasicJavaType<E>, JdbcType> resolutionConsumer) {
        assert this.typeConfiguration != null;
        resolutionConsumer.accept((BasicJavaType)this.typeConfiguration.getJavaTypeRegistry().getDescriptor(this.enumType), this.typeConfiguration.getJdbcTypeRegistry().getDescriptor(sqlType));
    }

    @Override
    public TypeConfiguration getTypeConfiguration() {
        return this.typeConfiguration;
    }

    @Override
    public void setTypeConfiguration(TypeConfiguration typeConfiguration) {
        this.typeConfiguration = typeConfiguration;
    }

    @Override
    public int getSqlType() {
        return Types.VARCHAR; // ✅ convert as varchar instead of enum
    }

    @Override
    public Class<E> returnedClass() {
        return enumType;
    }

    @Override
    public E nullSafeGet(ResultSet rs, int position, SharedSessionContractImplementor session, Object owner) throws SQLException {
        String dbData = rs.getString(position);
        return convertToEntityAttribute(dbData);
    }

    @Override
    public void nullSafeSet(PreparedStatement st, E value, int index, SharedSessionContractImplementor session) throws SQLException {
        String columnValue = convertToDatabaseColumn(value);
        if (columnValue == null) {
            st.setNull(index, Types.VARCHAR);
        } else {
            st.setString(index, columnValue);
        }
    }

}
