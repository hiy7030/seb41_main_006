package com.mainproject.server.factory;

import com.mainproject.server.awsS3.entity.S3UpFile;
import com.mainproject.server.domain.member.entity.Member;
import com.mainproject.server.domain.pet.dto.PetDto;
import com.mainproject.server.domain.pet.entity.Pet;

import java.util.List;

public class PetFactory {
    public static Pet createPet() {

        Pet pet = Pet.builder()
                .petId(1L)
                .name("마루")
                .age("5")
                .petSize(Pet.PetSize.DOG_S)
                .neutered(true)
                .gender("F")
                .aboutDog("귀여움")
                .breed("푸들")
                .build();

        Member member = MemberFactory.createMember();
        S3UpFile s3UpFile = S3UpFileFactory.createS3UpFile(member, pet);

        pet.setMember(member);
        pet.setS3UpFile(s3UpFile);

        return pet;
    }

    public static List<Pet> createPetList() {
        Pet pet1 = Pet.builder()
                .petId(1L)
                .name("마루")
                .age("5")
                .petSize(Pet.PetSize.DOG_S)
                .neutered(true)
                .gender("F")
                .aboutDog("귀여움")
                .breed("푸들")
                .build();

        Member member = MemberFactory.createMember();
        S3UpFile s3UpFile = S3UpFileFactory.createS3UpFile(member, pet1);

        pet1.setMember(member);
        pet1.setS3UpFile(s3UpFile);


        Pet pet2 = Pet.builder()
                .petId(1L)
                .name("보리")
                .age("5")
                .petSize(Pet.PetSize.DOG_S)
                .neutered(true)
                .gender("F")
                .aboutDog("앙칼짐")
                .breed("비숑")
                .build();

        S3UpFile s3UpFile2 = S3UpFileFactory.createS3UpFile(member, pet2);

        pet2.setMember(member);
        pet2.setS3UpFile(s3UpFile2);

        return List.of(pet1, pet2);
    }

    public static PetDto.Post createPetPostDto() {
        PetDto.Post post = PetDto.Post.builder()
                .name("마루")
                .age("5")
                .gender("F")
                .petSize(Pet.PetSize.DOG_S)
                .neutered(true)
                .aboutDog("귀여움")
                .breed("푸들")
                .profileImageId(1L)
                .build();

        return post;
    }

    public static PetDto.Patch createPetPatchDto() {
        PetDto.Patch patch = PetDto.Patch.builder()
                .name("마루")
                .age("5")
                .gender("F")
                .petSize(Pet.PetSize.DOG_S)
                .neutered(true)
                .aboutDog("귀여움")
                .breed("푸들")
                .profileImageId(1L)
                .build();

        return patch;
    }
    public static PetDto.Response createPetResponseDto(Pet pet) {
        PetDto.Response response = PetDto.Response.builder()
                .petId(pet.getPetId())
                .name(pet.getName())
                .age(pet.getAge())
                .gender(pet.getGender())
                .petSize(pet.getPetSize())
                .neutered(pet.isNeutered())
                .aboutDog(pet.getAboutDog())
                .breed(pet.getBreed())
                .member(MemberFactory.createMemberResponseDto())
                .profileImage(S3UpFileFactory.createPetS3Response())
                .build();

        return response;
    }
    public static PetDto.SimpleResponse createPetSimpleResponseDto(Pet pet) {
        PetDto.SimpleResponse response = PetDto.SimpleResponse.builder()
                .petId(pet.getPetId())
                .name(pet.getName())
                .age(pet.getAge())
                .gender(pet.getGender())
                .petSize(pet.getPetSize())
                .neutered(pet.isNeutered())
                .aboutDog(pet.getAboutDog())
                .breed(pet.getBreed())
                .profileImage(S3UpFileFactory.createPetS3Response())
                .build();

        return response;
    }

}
