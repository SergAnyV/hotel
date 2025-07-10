package com.asv.hotel.services;

import com.asv.hotel.dto.usertypedto.UserTypeDTO;
import com.asv.hotel.exceptions.mistakes.DataAlreadyExistsException;
import com.asv.hotel.repositories.UserTypeRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestConstructor;

import javax.sql.DataSource;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class UserTypeServiceTest {
    private DataSource dataSource;
    private UserTypeDTO testUserTypeDTO, testUserTypeDTO2;
    private UserTypeService userTypeService;
    private UserTypeRepository userTypeRepository;

    public UserTypeServiceTest(DataSource dataSource, UserTypeService userTypeService,
                               UserTypeRepository userTypeRepository) {
        this.dataSource = dataSource;
        this.userTypeService = userTypeService;
        this.userTypeRepository = userTypeRepository;
    }

    @BeforeEach
    public void setUp() {
        testUserTypeDTO = UserTypeDTO.builder()
                .role("Администратора")
                .description("просто админ")
                .isActive(true)
                .build();
        testUserTypeDTO2 = UserTypeDTO.builder()
                .role("Работник")
                .description("просто работник")
                .isActive(true)
                .build();
    }

    @AfterEach
    public void tearDown() {
        try {
            userTypeRepository.deleteAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void testDataSourceIsNotNull() {
        assertThat(dataSource).isNotNull();
    }

    //сохранение новой роли в репозитории CREATE
    @Test
    void saveUserTypeShouldBeSaveUserType() {
        UserTypeDTO savedUserType = userTypeService.save(testUserTypeDTO);
        assertEquals(savedUserType.getRole(), testUserTypeDTO.getRole());
        assertEquals(savedUserType.getDescription(), testUserTypeDTO.getDescription());
        assertEquals(savedUserType.getIsActive(), testUserTypeDTO.getIsActive());

    }

    @Test
    void saveUserTypeShouldThrowIfUserTypeExist() {
        userTypeService.save(testUserTypeDTO);

        assertThatThrownBy(() -> userTypeService.save(testUserTypeDTO))
                .isInstanceOf(DataAlreadyExistsException.class)
                .hasMessageContaining(testUserTypeDTO.getRole());
    }

    //поиск всех возможных ролей
    @Test
    void findAllShouldBeFindAllUserTypes() {
        userTypeService.save(testUserTypeDTO);
        userTypeService.save(testUserTypeDTO2);
        List<UserTypeDTO> userTypeDTOList = userTypeService.findAll();
        assertEquals(userTypeDTOList.size(), 2);

    }

    @Test
    void deleteShouldDeleteUserType(){
        userTypeService.save(testUserTypeDTO);
        userTypeService.save(testUserTypeDTO2);
        userTypeService.delete(testUserTypeDTO.getRole());
        assertThat(userTypeRepository.findUserTypeByRoleLikeIgnoreCase(testUserTypeDTO.getRole())).isEmpty();
    }

    @Test
    void deleteAllShouldDeleteAll(){
        userTypeService.save(testUserTypeDTO);
        userTypeService.save(testUserTypeDTO2);
        userTypeService.deleteAll();
        assertTrue(userTypeRepository.findAll().isEmpty(),"не удалились все данные из списка user_types");
    }


}