package com.uniref.service;

import com.uniref.bean.FieldType;
import com.uniref.bean.RefField;
import com.uniref.bean.RefType;
import com.uniref.repo.FieldTypeRepo;
import com.uniref.repo.RefFieldRepo;
import com.uniref.repo.RefTypeRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;

/**
 * Тестирование сервиса работы с типом справочника
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
class RefTypeServiceTest {
  @Autowired
  private RefTypeService refTypeService;

  @MockBean
  private RefTypeRepo refTypeRepo;

  @MockBean
  private FieldTypeRepo fieldTypeRepo;

  @MockBean
  private RefFieldRepo refFieldRepo;

  /**
   * Создание справочника
   */
  @Test
  void createRefType() {
    String refTypeName = "Справочник фамилий";
    RefType refType = refTypeService.createRefType(refTypeName);
    Assertions.assertEquals(refTypeName, refType.getName());
    Assertions.assertNotEquals(refType.getId(), 0L);

    Mockito.verify(refTypeRepo, Mockito.times(1)).findByName(refType.getName());
    Mockito.verify(refTypeRepo, Mockito.times(1)).save(refType);
  }

  /**
   * Создание справочника, если с таким именем уже существует
   */
  @Test
  void createRefTypeFail() {
    String refTypeName = "Справочник фамилий";

    //Имитируем ситуацию, что справочник фамилий уже есть.
    Mockito.doReturn(new RefType())
        .when(refTypeRepo)
        .findByName(refTypeName);

    RefType refType = refTypeService.createRefType(refTypeName);

    Mockito.verify(refTypeRepo, Mockito.times(1)).findByName(refTypeName);
    Mockito.verify(refTypeRepo, Mockito.never()).save(ArgumentMatchers.any(RefType.class));
  }


  /**
   * Добавление поля, если с таким порядковым номером уже существует.
   */
  @Test
  void addRefFieldOrderExists() {
    String refTypeName = "Справочник фамилий";
    String nameField = "Фамилия";
    Integer order = 1;
    String fieldTypeName = "Строка";

    RefType refType = new RefType();
    refType.setName(refTypeName);
    //Имитируем ситуацию, что справочник фамилий уже есть.
    Mockito.doReturn(refType)
        .when(refTypeRepo)
        .findByName(refTypeName);

    FieldType fieldType = new FieldType();
    fieldType.setName(fieldTypeName);
    //Имитируем ситуацию, что тип поля строка уже есть.
    Mockito.doReturn(fieldType)
        .when(fieldTypeRepo)
        .findByName(fieldTypeName);

    //Имитация, что поле с таким ордером уже есть
    RefField field = new RefField();
    field.setRefType(refType);
    field.setOrdr(1);
    Mockito.doReturn(Collections.singletonList(field))
        .when(refFieldRepo)
        .findAllByRefTypeAndOrdrGreaterThanEqual(refType, order);

    RefField refField = refTypeService.addRefField(refTypeName, nameField, fieldTypeName, order);

    Assertions.assertNotNull(refField);
    Assertions.assertNotNull(refField.getRefType());
    Assertions.assertEquals(refField.getRefType().getName(), refTypeName);
    Assertions.assertNotNull(refField.getFieldType());
    Assertions.assertEquals(refField.getFieldType().getName(), fieldTypeName);
    Assertions.assertEquals(refField.getName(), nameField);
    Assertions.assertEquals(refField.getOrdr(), order);

    Mockito.verify(refTypeRepo, Mockito.times(1)).findByName(refTypeName);
    Mockito.verify(fieldTypeRepo, Mockito.times(1)).findByName(fieldTypeName);
    //Был выполнен поиск элементов по ордеру, более чем указанный
    Mockito.verify(refFieldRepo, Mockito.times(1)).findAllByRefTypeAndOrdrGreaterThanEqual(ArgumentMatchers.any(RefType.class), ArgumentMatchers.eq(order));
    //Нашлось одно поле с таким же ордером, было сохранено + сохранение нового поля
    Mockito.verify(refFieldRepo, Mockito.times(2)).save(ArgumentMatchers.any(RefField.class));
    Mockito.verify(refFieldRepo, Mockito.times(1)).save(refField);


  }

  /**
   * Тест добавления поля, если порядковый номер поля не указан
   */
  @Test
  void addRefFieldOrderNotExists() {

    String refTypeName = "Справочник фамилий";
    String nameField = "Фамилия";
    Integer order = null;
    String fieldTypeName = "Строка";

    RefType refType = new RefType();
    refType.setName(refTypeName);
    //Имитируем ситуацию, что справочник фамилий уже есть.
    Mockito.doReturn(refType)
        .when(refTypeRepo)
        .findByName(refTypeName);

    FieldType fieldType = new FieldType();
    fieldType.setName(fieldTypeName);
    //Имитируем ситуацию, что тип поля строка уже есть.
    Mockito.doReturn(fieldType)
        .when(fieldTypeRepo)
        .findByName(fieldTypeName);

    //Имитация, что какое-то поле уже есть
    RefField field = new RefField();
    field.setRefType(refType);
    field.setOrdr(1);
    Mockito.doReturn(field)
        .when(refFieldRepo)
        .findRefFieldByRefTypeOrderByOrdrDesc(refType);

    RefField refField = refTypeService.addRefField(refTypeName, nameField, fieldTypeName, order);

    Assertions.assertNotNull(refField);
    Assertions.assertNotNull(refField.getRefType());
    Assertions.assertEquals(refField.getRefType().getName(), refTypeName);
    Assertions.assertNotNull(refField.getFieldType());
    Assertions.assertEquals(refField.getFieldType().getName(), fieldTypeName);
    Assertions.assertEquals(refField.getName(), nameField);
    Assertions.assertEquals(2, (int) refField.getOrdr());

    Mockito.verify(refTypeRepo, Mockito.times(1)).findByName(refTypeName);
    Mockito.verify(fieldTypeRepo, Mockito.times(1)).findByName(fieldTypeName);
    //Был выполнен поиск элемента с максимальным ордером
    Mockito.verify(refFieldRepo, Mockito.times(1)).findRefFieldByRefTypeOrderByOrdrDesc(ArgumentMatchers.any(RefType.class));
    Mockito.verify(refFieldRepo, Mockito.times(1)).save(refField);
  }


  /**
   * Тест добавления поля, если порядковый номер поля указан, но такого еще нет в БД
   */
  @Test
  void addRefFieldOrder() {

    String refTypeName = "Справочник фамилий";
    String nameField = "Фамилия";
    Integer order = 1;
    String fieldTypeName = "Строка";

    RefType refType = new RefType();
    refType.setName(refTypeName);
    //Имитируем, что справочник фамилий уже есть.
    Mockito.doReturn(refType)
        .when(refTypeRepo)
        .findByName(refTypeName);

    FieldType fieldType = new FieldType();
    fieldType.setName(fieldTypeName);
    //Имитируем, что тип поля строка уже есть.
    Mockito.doReturn(fieldType)
        .when(fieldTypeRepo)
        .findByName(fieldTypeName);

    RefField refField = refTypeService.addRefField(refTypeName, nameField, fieldTypeName, order);

    Assertions.assertNotNull(refField);
    Assertions.assertNotNull(refField.getRefType());
    Assertions.assertEquals(refField.getRefType().getName(), refTypeName);
    Assertions.assertNotNull(refField.getFieldType());
    Assertions.assertEquals(refField.getFieldType().getName(), fieldTypeName);
    Assertions.assertEquals(refField.getName(), nameField);
    Assertions.assertEquals(refField.getOrdr(), order);

    Mockito.verify(refTypeRepo, Mockito.times(1)).findByName(refTypeName);
    Mockito.verify(fieldTypeRepo, Mockito.times(1)).findByName(fieldTypeName);
    //Был выполнен поиск элементов по ордеру, более чем указанный
    Mockito.verify(refFieldRepo, Mockito.times(1)).findAllByRefTypeAndOrdrGreaterThanEqual(ArgumentMatchers.any(RefType.class), ArgumentMatchers.eq(order));
    Mockito.verify(refFieldRepo, Mockito.times(1)).save(refField);
  }

  /**
   * Тест  добавления поля, Справочника нет в БД
   */
  @Test
  void addRefFieldTypeNotExists() {

    String refTypeName = "Справочник фамилий";
    String nameField = "Фамилия";
    Integer order = 1;
    String fieldTypeName = "Строка";

    RefField refField = refTypeService.addRefField(refTypeName, nameField, fieldTypeName, order);
    Assertions.assertNull(refField);

    Mockito.verify(refTypeRepo, Mockito.times(1)).findByName(refTypeName);
    Mockito.verify(fieldTypeRepo, Mockito.never()).findByName(ArgumentMatchers.anyString());
    Mockito.verify(refFieldRepo, Mockito.never()).findRefFieldByRefTypeOrderByOrdrDesc(ArgumentMatchers.any(RefType.class));
    Mockito.verify(refFieldRepo, Mockito.never()).findAllByRefTypeAndOrdrGreaterThanEqual(ArgumentMatchers.any(RefType.class), ArgumentMatchers.anyInt());
    Mockito.verify(refFieldRepo, Mockito.never()).save(ArgumentMatchers.any(RefField.class));
  }

  /**
   * Тест добавления поля, если типа поля нет в БД
   */
  @Test
  void addRefFieldFieldTypeNotExists() {

    String refTypeName = "Справочник фамилий";
    String nameField = "Фамилия";
    Integer order = 1;
    String fieldTypeName = "Строка";

    RefType refType = new RefType();
    refType.setName(refTypeName);
    //Имитируем, что справочник фамилий уже есть.
    Mockito.doReturn(refType)
        .when(refTypeRepo)
        .findByName(refTypeName);

    RefField refField = refTypeService.addRefField(refTypeName, nameField, fieldTypeName, order);
    Assertions.assertNull(refField);

    Mockito.verify(refTypeRepo, Mockito.times(1)).findByName(refTypeName);
    Mockito.verify(fieldTypeRepo, Mockito.times(1)).findByName(fieldTypeName);
    Mockito.verify(refFieldRepo, Mockito.never()).findRefFieldByRefTypeOrderByOrdrDesc(ArgumentMatchers.any(RefType.class));
    Mockito.verify(refFieldRepo, Mockito.never()).findAllByRefTypeAndOrdrGreaterThanEqual(ArgumentMatchers.any(RefType.class), ArgumentMatchers.anyInt());
    Mockito.verify(refFieldRepo, Mockito.never()).save(ArgumentMatchers.any(RefField.class));
  }

  /**
   * Тест добавления поля, если с таким именем номером уже существует.
   */
  @Test
  void addRefFieldFieldExists() {
    String refTypeName = "Справочник фамилий";
    String nameField = "Фамилия";
    String fieldTypeName = "Строка";

    RefType refType = new RefType();
    refType.setName(refTypeName);
    //Имитируем ситуацию, что справочник фамилий уже есть.
    Mockito.doReturn(refType)
        .when(refTypeRepo)
        .findByName(refTypeName);

    FieldType fieldType = new FieldType();
    fieldType.setName(fieldTypeName);
    //Имитируем ситуацию, что тип поля строка уже есть.
    Mockito.doReturn(fieldType)
        .when(fieldTypeRepo)
        .findByName(fieldTypeName);

    //Имитация, что поле с таким именем уже есть
    RefField field = new RefField();
    field.setRefType(refType);
    field.setName(nameField);
    Mockito.doReturn(field)
        .when(refFieldRepo)
        .findByRefTypeAndName(refType, nameField);

    RefField refField = refTypeService.addRefField(refTypeName, nameField, fieldTypeName, null);

    Assertions.assertNotNull(refField);
    Assertions.assertNotNull(refField.getRefType());
    Assertions.assertEquals(refField.getRefType().getName(), refTypeName);
    Assertions.assertEquals(refField.getName(), nameField);


    Mockito.verify(refTypeRepo, Mockito.times(1)).findByName(refTypeName);
    Mockito.verify(fieldTypeRepo, Mockito.times(1)).findByName(fieldTypeName);
    Mockito.verify(refFieldRepo, Mockito.times(1)).findByRefTypeAndName(refType, nameField);
    //Больше ничего не вызывалось
    Mockito.verify(refFieldRepo, Mockito.never()).findRefFieldByRefTypeOrderByOrdrDesc(ArgumentMatchers.any(RefType.class));
    Mockito.verify(refFieldRepo, Mockito.never()).findAllByRefTypeAndOrdrGreaterThanEqual(ArgumentMatchers.any(RefType.class), ArgumentMatchers.anyInt());
    Mockito.verify(refFieldRepo, Mockito.never()).save(ArgumentMatchers.any(RefField.class));
  }
}