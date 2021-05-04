package com.w2m.anime;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

class ArchTest {

    @Test
    void servicesAndRepositoriesShouldNotDependOnWebLayer() {

        JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("com.w2m.anime");

        noClasses()
            .that()
                .resideInAnyPackage("com.w2m.anime.service..")
            .or()
                .resideInAnyPackage("com.w2m.anime.repository..")
            .should().dependOnClassesThat()
                .resideInAnyPackage("..com.w2m.anime.web..")
        .because("Services and repositories should not depend on web layer")
        .check(importedClasses);
    }
}
