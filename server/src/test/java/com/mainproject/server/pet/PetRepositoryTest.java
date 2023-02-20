package com.mainproject.server.pet;

import com.mainproject.server.domain.member.entity.Member;
import com.mainproject.server.domain.pet.entity.Pet;
import com.mainproject.server.domain.pet.repository.PetRepository;
import com.mainproject.server.factory.MemberFactory;
import com.mainproject.server.factory.PetFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@DataJpaTest
public class PetRepositoryTest {
    @Autowired
    private PetRepository petRepository;

    @BeforeEach
    void init() {
        petRepository.deleteAll();
    }

    @Test
    @DisplayName("강아지 저장 테스트")
    void savePetTest() {
        //given
        Pet pet = PetFactory.createPet();
        //when
        Pet savePet = petRepository.save(pet);
        //then
        assertNotNull(savePet);
        assertTrue(pet.getName().equals(savePet.getName()));
        assertTrue(pet.getPetId().equals(savePet.getPetId()));
        assertTrue(pet.getMember().equals(savePet.getMember()));
        assertTrue(pet.getAge().equals(savePet.getAge()));
        assertTrue(pet.getGender().equals(savePet.getGender()));
        assertTrue(pet.getPetSize().equals(savePet.getPetSize()));
        assertTrue(pet.getAboutDog().equals(savePet.getAboutDog()));
        assertTrue(pet.getBreed().equals(savePet.getBreed()));
        assertThat(pet.isNeutered(), is(savePet.isNeutered()));
    }

    @Test
    @DisplayName("검색 기능 테스트")
    void searchPetTest() {
        //given
        List<Pet> pets = PetFactory.createPetList();
        Pageable pageable =
                PageRequest.of(0, 10, Sort.by("petId").descending());

        pets.stream().map(pet -> petRepository.save(pet));
        //when
        Page<Pet> petPage = petRepository.findByPetSizeLike(pageable, Pet.PetSize.DOG_S);
        List<Pet> actual = petPage.getContent();
        //then
        assertNotNull(actual);
        assertThat(pets.size(), is(actual.size()));
    }

    @Test
    @DisplayName("이름으로 찾기")
    void findByNameTest() {
        //given
        Pet pet = PetFactory.createPet();
        petRepository.save(pet);
        //when
        Optional<Pet> optionalActual = petRepository.findByName(pet.getName());
        Pet actual = optionalActual.get();
        //then
        assertNotNull(actual);
        assertTrue(pet.getName().equals(actual.getName()));
        assertTrue(pet.getPetId().equals(actual.getPetId()));
        assertTrue(pet.getMember().equals(actual.getMember()));
        assertTrue(pet.getAge().equals(actual.getAge()));
        assertTrue(pet.getGender().equals(actual.getGender()));
        assertTrue(pet.getPetSize().equals(actual.getPetSize()));
        assertTrue(pet.getAboutDog().equals(actual.getAboutDog()));
        assertTrue(pet.getBreed().equals(actual.getBreed()));
        assertThat(pet.isNeutered(), is(actual.isNeutered()));
    }

    @Test
    @DisplayName("멤버로 찾기")
    void findByMember() {
        //given
        List<Pet> expected = PetFactory.createPetList();
        expected.stream().map(pet -> petRepository.save(pet));

        Member member = MemberFactory.createMember();

        Pageable pageable =
                PageRequest.of(0, 10, Sort.by("petId").descending());
        //when
        Page<Pet> petPage = petRepository.findByMember(pageable, member);
        List<Pet> actual = petPage.getContent();
        //then
        assertNotNull(actual);
        assertThat(expected.size(), is(actual.size()));

    }
}
