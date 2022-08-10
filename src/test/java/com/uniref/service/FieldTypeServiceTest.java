package com.uniref.service;

import com.uniref.bean.FieldType;
import com.uniref.bean.RefType;
import com.uniref.repo.FieldTypeRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тестирование сервиса типов полей
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
class FieldTypeServiceTest {
  @Autowired
  private FieldTypeService fieldTypeService;

  @MockBean
  private FieldTypeRepo fieldTypeRepo;

  /**
   * Создание типа поля
   */
  @Test
  void createFieldType() {
    String name = "Целое число";
    String pattern = "^[0-9]+$";

    FieldType fieldType = fieldTypeService.createFieldType(name, pattern);
    Assertions.assertNotNull(fieldType);
    Assertions.assertEquals(fieldType.getName(), name);
    Assertions.assertEquals(fieldType.getPattern(), pattern);

    Mockito.verify(fieldTypeRepo, Mockito.times(1)).findByName(name);
    Mockito.verify(fieldTypeRepo, Mockito.times(1)).findByPattern(pattern);
    Mockito.verify(fieldTypeRepo, Mockito.times(1)).save(fieldType);
  }

  /**
   * Создание типа поля, если с таким именем уже существует
   */
  @Test
  void createFieldTypeNameExists() {
    String name = "Целое число";
    String pattern = "^[0-9]+$";

    Mockito.doReturn(new FieldType())
        .when(fieldTypeRepo)
        .findByName(name);

    FieldType fieldType = fieldTypeService.createFieldType(name, pattern);

    Assertions.assertNull(fieldType);

    Mockito.verify(fieldTypeRepo, Mockito.times(1)).findByName(name);
    Mockito.verify(fieldTypeRepo, Mockito.never()).save(ArgumentMatchers.any(FieldType.class));
  }

  /**
   * Создание типа поля, если с таким шаблоном уже существует
   */
  @Test
  void createFieldTypePatternExists() {
    String name = "Целое число";
    String pattern = "^[0-9]+$";

    Mockito.doReturn(new FieldType())
        .when(fieldTypeRepo)
        .findByPattern(pattern);

    FieldType fieldType = fieldTypeService.createFieldType(name, pattern);

    Assertions.assertNull(fieldType);

    Mockito.verify(fieldTypeRepo, Mockito.times(1)).findByPattern(pattern);
    Mockito.verify(fieldTypeRepo, Mockito.never()).save(ArgumentMatchers.any(FieldType.class));
  }
}