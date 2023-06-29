package org.oopscraft.arch4j.core.sample.dao;

import org.oopscraft.arch4j.core.data.converter.AbstractEnumConverter;
import org.oopscraft.arch4j.core.sample.SampleType;

import javax.persistence.Converter;

@Converter(autoApply = true)
public class SampleTypeConverter extends AbstractEnumConverter<SampleType> {

}
