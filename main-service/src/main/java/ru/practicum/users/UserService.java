package ru.practicum.users;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exception.NotFoundException;
import ru.practicum.users.dto.UserDto;
import ru.practicum.users.model.User;

import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.util.PageCreator.getPage;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public List<UserDto> getUsers(List<Long> ids, Integer from, Integer size) {
        Pageable page = getPage(from, size);
        List<User> users;
        if (ids == null || ids.isEmpty()) {
            users = userRepository.findAll(page).getContent();
        } else {
            users = userRepository.findAllByIdIn(ids, page);
        }
        return users.stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    public UserDto addUser(UserDto userDto) {
        User user = UserMapper.toUser(userDto);
        return UserMapper.toUserDto(userRepository.save(user));
    }

    public void deleteUser(Long userId) {
        if (userRepository.findById(userId).isEmpty()) {
            throw new NotFoundException("User can't be deleted: User with id = " + userId + " doesn't exist");
        }
        userRepository.deleteById(userId);
    }
}
