package faang.school.analytics.service.user;

import faang.school.analytics.entity.User;
import faang.school.analytics.repository.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void getSumOfUsersActionsByEventTypeTest_ReturnTen() {
        int res = userService.getSumOfUsersActionsByEventType(List.of(1,2,3,4));

        assertEquals(10, res);
    }

    @Test
    void getSumOfUsersActionsByEventTypeTest_ReturnZero() {
        int res = userService.getSumOfUsersActionsByEventType(List.of());

        assertEquals(0, res);
    }

    @Test
    void findByIdTest_ReturnUser() {
        when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(User.builder().build()));

        User user = userService.findById(Mockito.anyLong());

        assertNotNull(user);
    }

    @Test
    void findByIdTest_ThrowIllegalArgumentException() {
        when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () ->userService.findById(Mockito.anyLong()));
        verify(userRepository, times(1)).findById(Mockito.anyLong());
    }
}
