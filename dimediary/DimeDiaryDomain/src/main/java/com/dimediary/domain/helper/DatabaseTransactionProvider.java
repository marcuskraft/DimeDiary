package com.dimediary.domain.helper;

public interface DatabaseTransactionProvider {

  boolean beginTransaction();

  boolean commitTransaction();

  boolean rollbackTransaction();

  void close();

}