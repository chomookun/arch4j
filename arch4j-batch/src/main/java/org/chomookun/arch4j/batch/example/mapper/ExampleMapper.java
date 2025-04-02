package org.chomookun.arch4j.batch.example.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.awt.*;

/**
 * Example Mapper
 */
@Mapper
public interface ExampleMapper {

    /**
     * Selects examples
     * @param type type
     * @return cursor of examples
     */
    Cursor selectExamples(@Param("type") String type);

}
