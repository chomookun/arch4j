package org.chomookun.arch4j.batch.common.item.file;

import lombok.Setter;
import org.chomookun.arch4j.batch.common.item.file.transform.DelimiterLineAggregator;
import org.chomookun.arch4j.batch.common.item.file.transform.FieldConversionService;
import org.chomookun.arch4j.batch.common.item.file.transform.ItemTypeDescriptor;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.LineAggregator;

@Deprecated
public class DelimiterFileItemWriter<T> extends GenericFileItemWriter<T> {

    @Setter
    public String delimiter = String.valueOf(',');

    @Override
    protected LineAggregator<T> createLineAggregator(Class<? extends T> itemType) {
        // item type descriptor
        ItemTypeDescriptor itemTypeDescriptor = new ItemTypeDescriptor(itemType);

        // conversion service
        FieldConversionService conversionService = FieldConversionService.builder()
                .dateTimeFormatter(dateTimeFormatter)
                .dateFormatter(dateFormatter)
                .timeFormatter(timeFormatter)
                .build();

        // field extractor
        BeanWrapperFieldExtractor<T> fieldExtractor = new BeanWrapperFieldExtractor<>();
        fieldExtractor.setNames(itemTypeDescriptor.getFieldNames().toArray(new String[0]));

        // line aggregator
        DelimiterLineAggregator<T> lineAggregator = new DelimiterLineAggregator<>(encoding, itemTypeDescriptor, fieldExtractor, conversionService);
        lineAggregator.setDelimiter(delimiter);

        // return
        return lineAggregator;
    }

}
