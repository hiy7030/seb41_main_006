package com.mainproject.server.pet;

import com.mainproject.server.domain.member.entity.Member;
import com.mainproject.server.domain.pet.entity.Pet;
import com.mainproject.server.domain.pet.repository.PetRepository;
import com.mainproject.server.domain.pet.service.PetService;
import com.mainproject.server.exception.BusinessLogicException;
import com.mainproject.server.factory.MemberFactory;
import com.mainproject.server.factory.PetFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class PetServiceTest {
    @Mock
    private PetRepository petRepository;

    @InjectMocks
    private PetService petService;

    @BeforeEach
    void init() {
        petRepository.deleteAll();
    }

    @Test
    @DisplayName("강쥐 불러오기")
    void findVerifiedPetTest() {
        //given
        Pet pet = PetFactory.createPet();

        given(petRepository.findById(Mockito.anyLong())).willReturn(Optional.of(pet));
        //when
        //then
        assertThrows(BusinessLogicException.class, ()->petService.findVerifiedPet(pet.getPetId()));
    }

}
