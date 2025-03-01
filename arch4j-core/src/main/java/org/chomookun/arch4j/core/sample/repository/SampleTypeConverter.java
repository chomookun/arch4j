package org.chomookun.arch4j.core.sample.repository;

import org.chomookun.arch4j.core.common.data.converter.GenericEnumConverter;
import org.chomookun.arch4j.core.sample.model.SampleType;

import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class SampleTypeConverter extends GenericEnumConverter<SampleType> {

}
