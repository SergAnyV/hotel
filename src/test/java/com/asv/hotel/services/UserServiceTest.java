package com.asv.hotel.services;

import com.asv.hotel.dto.UserDTO;
import com.asv.hotel.dto.UserSimpleDTO;
import com.asv.hotel.dto.UserTypeDTO;
import com.asv.hotel.entities.User;
import com.asv.hotel.repositories.UserRepository;
import com.asv.hotel.repositories.UserTypeRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestConstructor;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class UserServiceTest {
    private UserService userService;
    private UserRepository userRepository;
    private UserTypeDTO testUserTypeDTO,testUserTypeDTO2;
    private UserTypeService userTypeService;
    private UserTypeRepository userTypeRepository;
    private UserDTO testUserDTO,testUserDTO2;
    private UserSimpleDTO testUserSimpleDTO;

    public UserServiceTest(UserService userService, UserRepository userRepository,
                           UserTypeService userTypeService, UserTypeRepository userTypeRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.userTypeService = userTypeService;
        this.userTypeRepository = userTypeRepository;
    }

    @BeforeEach
    void setUp() {
        testUserTypeDTO = UserTypeDTO.builder().role("Client").description("just a client").isActive(true).build();
        testUserTypeDTO2 = UserTypeDTO.builder().role("Admin").description("just a admin").isActive(true).build();
        testUserDTO = UserDTO.builder().role("Client").email("ghi@j.hj").firstName("Sergey")
                .fathersName("Vladimirivich").lastName("Numm").nickName("Best").phoneNumber("86866").password("1234").build();
        testUserSimpleDTO=UserSimpleDTO.builder().firstName("Sergey").fathersName("Vladimirivich").lastName("Numm")
                .phoneNumber("96").build();
        testUserDTO2 = UserDTO.builder().role("Client").email("sdgs@j.hj").firstName("Nikola")
                .fathersName("Vladimirivich").lastName("Ginn").nickName("YoloPuki").phoneNumber("86866").password("1234").build();

    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
        userTypeRepository.deleteAll();
    }

    @Test
    void saveShoulBeSavedNewUser() {
        userTypeService.save(testUserTypeDTO);
        UserDTO nudto = userService.save(testUserDTO);
        assertEquals(nudto.getFirstName(), testUserDTO.getFirstName());
    }

    @Test
    void findUserByLastNameAndFirstNameShoudBeFindUserDTO() {
        userTypeService.save(testUserTypeDTO);
        userService.save(testUserDTO);
        UserDTO nudto =  userService.findUserByLastNameAndFirstName(testUserDTO.getLastName(), testUserDTO.getFirstName());
        assertEquals(testUserDTO.getLastName(),nudto.getLastName());
        assertEquals(testUserDTO.getFirstName(),nudto.getFirstName());

    }

    @Test
    void findUserByLastNameAndFirstNameReturnUser() {
        userTypeService.save(testUserTypeDTO);
        userService.save(testUserDTO);
        User user=userService.findUserByLastNameAndFirstNameReturnUser(testUserSimpleDTO.getLastName()
                , testUserSimpleDTO.getFirstName());
        assertEquals(user.getFirstName(),testUserSimpleDTO.getFirstName());

    }

    @Test
    void updateUserByUserDTOShoildBeUpdated(){
        userTypeService.save(testUserTypeDTO);
        userTypeService.save(testUserTypeDTO2);
        userService.save(testUserDTO);
        testUserDTO.setRole("Admin");
        userService.updateUser(testUserDTO);
        var updatedUser=userService.findUserByLastNameAndFirstNameReturnUser(testUserDTO.getLastName(), testUserDTO.getFirstName());
        assertFalse(testUserDTO.getRole().equals("Client"));
        assertTrue(testUserDTO.getFirstName().equals(updatedUser.getFirstName()));


    }
}