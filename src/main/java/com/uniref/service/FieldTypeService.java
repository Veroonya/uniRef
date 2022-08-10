package com.uniref.service;

import com.uniref.bean.FieldType;
import com.uniref.repo.FieldTypeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Сервис для работы с типами полей
 */
@Service
public class FieldTypeService {
  /**
   * Репозиторий для работы с типами полей
   */
  @Autowired
  private FieldTypeRepo fieldTypeRepo;

  /**
   * Создает тип поля
   * @param name Наименование типа
   * @param patternStr - регулярное выражения для анализа введенных данных
   * @return созданный тип поля
   */
  public FieldType createFieldType(String name, String patternStr) {
    FieldType pattern = new FieldType();
    pattern.setName(name);
    pattern.setPattern(patternStr);

    if (fieldTypeRepo.findByName(name) != null) return null;
    if (fieldTypeRepo.findByPattern(patternStr) != null) return null;

    fieldTypeRepo.save(pattern);
    return pattern;
  }

  /**
   * Обновляет тип поля
   * @param fieldType Тип поля для сохранения
   * @return сохраненный тип поля
   */
  public FieldType updateFieldType(FieldType fieldType) {
    //TODO: Полезнее был бы метод, чтобы менять наименование, заменять одно на другое, или pattern
    fieldTypeRepo.save(fieldType);
    return fieldType;
  }

  /**
   * Удаляет тип поля
   * @param fieldType Тип поля для удаления
   */
  public void deleteFieldType(FieldType fieldType) {
    fieldTypeRepo.delete(fieldType);
  }

  
}
