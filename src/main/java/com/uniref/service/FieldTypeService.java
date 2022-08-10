package com.uniref.service;

import com.uniref.bean.FieldType;
import com.uniref.repo.FieldTypeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FieldTypeService {
  @Autowired
  private FieldTypeRepo fieldTypeRepo;

  public FieldType createFieldType(String name, String patternStr) {
    FieldType pattern = new FieldType();
    pattern.setName(name);
    pattern.setPattern(patternStr);

    if (fieldTypeRepo.findByName(name) != null) return null;
    if (fieldTypeRepo.findByPattern(patternStr) != null) return null;

    fieldTypeRepo.save(pattern);
    return pattern;
  }

  public FieldType updateFieldType(FieldType fieldType) {
    fieldTypeRepo.save(fieldType);
    return fieldType;
  }

  public void deleteFieldType(FieldType fieldType) {
    fieldTypeRepo.delete(fieldType);
  }

  public FieldType getFieldType(Long id) {
    Optional<FieldType> pattern = fieldTypeRepo.findById(id);
    return pattern.orElse(null);
  }

  
}
