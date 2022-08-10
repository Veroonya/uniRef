package com.uniref.service;

import com.uniref.bean.Pattern;
import com.uniref.repo.PatternRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PatternService {
  @Autowired
  private PatternRepo patternRepo;

  public Pattern createPattern(String name, String patternStr) {
    Pattern pattern = new Pattern();
    pattern.setName(name);
    pattern.setPattern(patternStr);
    patternRepo.save(pattern);
    return pattern;
  }

  public Pattern updatePattern(Pattern pattern) {
    patternRepo.save(pattern);
    return pattern;
  }

  public void deletePattern(Pattern pattern) {
    patternRepo.delete(pattern);
  }

  public Pattern getPattern(Long id) {
    Optional<Pattern> pattern = patternRepo.findById(id);
    return pattern.orElse(null);
  }

  
}
