package faang.school.analytics.client.user;

import faang.school.analytics.dto.user.UpdateUsersRankDto;
import faang.school.analytics.dto.user.UserDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "${user-service.name}", url = "http://${user-service.host}:${user-service.port}", path = "/users")
public interface UserServiceClient {

    @GetMapping("{userId}")
    ResponseEntity<UserDto> getUser(@NotNull @PathVariable long userId);

    @GetMapping
    ResponseEntity<List<UserDto>> getUsersByIds(@RequestBody List<Long> userIds);

    @PutMapping("update-users-rank")
    ResponseEntity<Void> updateUsersRankByUserIds(@RequestBody @Valid UpdateUsersRankDto usersDto);
}
