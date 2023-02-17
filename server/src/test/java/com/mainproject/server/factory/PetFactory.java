package com.mainproject.server.factory;

import com.mainproject.server.awsS3.entity.S3UpFile;
import com.mainproject.server.domain.member.entity.Member;
import com.mainproject.server.domain.pet.dto.PetDto;
import com.mainproject.server.domain.pet.entity.Pet;

import java.util.List;

public class PetFactory {
    public static Pet createPet() {

        Pet pet = new Pet();
        pet.setPetId(1L);
        pet.setName("마루");
        pet.setAge("5");
        pet.setPetSize(Pet.PetSize.DOG_S);
        pet.setNeutered(true);
        pet.setGender("F");
        pet.setAboutDog("귀여움");
        pet.setBreed("푸들");

        Member member = MemberFactory.createMember();
        S3UpFile s3UpFile = S3UpFileFactory.createS3UpFile(member, pet);

        pet.setMember(member);
        pet.setS3UpFile(s3UpFile);

        return pet;
    }

    public static List<Pet> createPetList() {
        Pet pet = new Pet();
        pet.setPetId(1L);
        pet.setName("마루");
        pet.setAge("5");
        pet.setPetSize(Pet.PetSize.DOG_S);
        pet.setNeutered(true);
        pet.setGender("F");
        pet.setAboutDog("귀여움");
        pet.setBreed("푸들");

        Member member = MemberFactory.createMember();
        S3UpFile s3UpFile = S3UpFileFactory.createS3UpFile(member, pet);

        pet.setMember(member);
        pet.setS3UpFile(s3UpFile);

        Pet pet2 = new Pet();
        pet2.setPetId(2L);
        pet2.setName("보리");
        pet2.setAge("5");
        pet2.setPetSize(Pet.PetSize.DOG_S);
        pet2.setNeutered(true);
        pet2.setGender("F");
        pet2.setAboutDog("앙칼짐");
        pet2.setBreed("비숑");

        S3UpFile s3UpFile2 = S3UpFileFactory.createS3UpFile(member, pet2);

        pet.setMember(member);
        pet.setS3UpFile(s3UpFile2);

        return List.of(pet, pet2);
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
