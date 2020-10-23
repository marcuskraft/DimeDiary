package com.dimediary.persistence.repositories;

import com.dimediary.domain.BankAccountCategory;
import com.dimediary.persistence.converter.BankAccountCategoryTransformer;
import com.dimediary.persistence.entities.BankAccountCategoryEntity;
import com.dimediary.persistence.repositories.cruds.BankAccountCategoryCrudRepository;
import com.dimediary.port.out.BankAccountCategoryRepo;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
class BankAccountCategoryRepoImpl implements BankAccountCategoryRepo {


  private final BankAccountCategoryTransformer bankaccountCategoryTransformer;
  private final BankAccountCategoryCrudRepository bankAccountCategoryCrudRepository;

  @Autowired
  public BankAccountCategoryRepoImpl(
      final BankAccountCategoryTransformer bankaccountCategoryTransformer,
      final BankAccountCategoryCrudRepository bankAccountCategoryCrudRepository) {
    this.bankaccountCategoryTransformer = bankaccountCategoryTransformer;
    this.bankAccountCategoryCrudRepository = bankAccountCategoryCrudRepository;
  }


  @Override
  public List<BankAccountCategory> getBankAccountCategories() {
    final List<BankAccountCategory> bankAccountCategories = new ArrayList<>();

    this.bankAccountCategoryCrudRepository.findAll().forEach(
        bankAccountCategoryEntity -> bankAccountCategories.add(this.bankaccountCategoryTransformer
            .bankAccountCategoryEntityToBankAccountCategory(bankAccountCategoryEntity)));

    return bankAccountCategories;
  }

  @Override
  public BankAccountCategory persist(final BankAccountCategory bankAccountCategory) {
    if (bankAccountCategory == null) {
      return null;
    }
    BankAccountCategoryRepoImpl.log
        .info("persist BankAccountCategory: {}", bankAccountCategory.getName());

    if (bankAccountCategory.getId() == null) {
      bankAccountCategory.setId(UUID.randomUUID());
    }

    BankAccountCategoryEntity bankAccountCategoryEntity = this.bankaccountCategoryTransformer
        .bankAccountCategoryToBankAccountCategoryEntity(bankAccountCategory);

    bankAccountCategoryEntity = this.bankAccountCategoryCrudRepository
        .save(bankAccountCategoryEntity);

    return this.bankaccountCategoryTransformer
        .bankAccountCategoryEntityToBankAccountCategory(bankAccountCategoryEntity);
  }

  @Override
  public void delete(final UUID bankAccountCategoryId) {
    this.bankAccountCategoryCrudRepository.deleteById(bankAccountCategoryId.toString());
  }

}
