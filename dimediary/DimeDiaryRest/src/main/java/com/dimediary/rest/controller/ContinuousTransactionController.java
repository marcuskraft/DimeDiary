package com.dimediary.rest.controller;

import com.dimediary.openapi.api.ContinuousTransactionApi;
import com.dimediary.openapi.model.ContinuousTransaction;
import com.dimediary.port.in.ContinuousTransactionProvider;
import com.dimediary.rest.controller.helper.ResponseFactory;
import com.dimediary.rest.converter.ContinuousTransactionRestConverter;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@RequestMapping("/api")
@Controller
@Api(tags = "ContinuousTransaction")
public class ContinuousTransactionController implements ContinuousTransactionApi {

  private final ContinuousTransactionProvider continuousTransactionProvider;
  private final ContinuousTransactionRestConverter continuousTransactionRestConverter;
  private final ResponseFactory responseFactory;

  public ContinuousTransactionController(
      final ContinuousTransactionProvider continuousTransactionProvider,
      final ContinuousTransactionRestConverter continuousTransactionRestConverter,
      final ResponseFactory responseFactory) {
    this.continuousTransactionProvider = continuousTransactionProvider;
    this.continuousTransactionRestConverter = continuousTransactionRestConverter;
    this.responseFactory = responseFactory;
  }


  @Override
  public ResponseEntity<ContinuousTransaction> createContinuousTransaction(
      final ContinuousTransaction continuousTransaction) {
    return this.responseFactory.created(this.continuousTransactionRestConverter.from(
        this.continuousTransactionProvider
            .persist(this.continuousTransactionRestConverter.to(continuousTransaction))));
  }

  @Override
  public ResponseEntity<Void> deleteContinuousTransaction(final Integer continuousTransactionId) {
    this.continuousTransactionProvider.deleteAllContinuousTransactions(continuousTransactionId);
    return this.responseFactory.okNoContent();
  }

  @Override
  public ResponseEntity<ContinuousTransaction> getContinuousTransaction(
      final Integer continuousTransactionId) {
    return this.responseFactory.ok(this.continuousTransactionRestConverter
        .from(
            this.continuousTransactionProvider.getContinuousTransactions(continuousTransactionId)));
  }

  @Override
  public ResponseEntity<Void> updateContinuousTransaction(final Integer continuousTransactionId,
      final ContinuousTransaction continuousTransaction) {
    if (!continuousTransactionId.equals(continuousTransaction.getId())) {
      return this.responseFactory.badRequest();
    }
    this.continuousTransactionProvider
        .persist(this.continuousTransactionRestConverter.to(continuousTransaction));
    return this.responseFactory.okNoContent();
  }
}
