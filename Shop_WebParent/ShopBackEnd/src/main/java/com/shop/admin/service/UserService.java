package com.shop.admin.service;

import com.shop.admin.exception.UserNotFoundException;
import com.shop.admin.repository.RoleRepository;
import com.shop.admin.repository.UserRepository;
import com.shop.model.Role;
import com.shop.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;

    public static final int PAGE_SIZE = 4;

    public List<User> findAllUsersSortedByFirstName() {
        return (List<User>) userRepository.findAll(Sort.by("firstName").ascending());
    }

    public List<User> findAllUsersSortedById() {
        return (List<User>) userRepository.findAll(Sort.by("id").ascending());
    }

    public Page<User> listByPage(int pageNum, String sortField, String sortDirection, String keyword) {

        Sort sort = Sort.by(sortField);
        if (sortField.equals("roles") && keyword != null) // ! when the condition is true, hibernate is throwing an
                                                          // exception
            sort = Sort.by("firstName");

        sort = sortDirection.equals("asc") ? sort.ascending() : sort.descending();

        PageRequest pageable = PageRequest.of(pageNum - 1, PAGE_SIZE, sort);

        if (keyword != null)
            return userRepository.findAll(keyword, pageable);
        else
            return userRepository.findAll(pageable);
    }

    public User findById(Long id) throws UserNotFoundException {
        Optional<User> found = userRepository.findById(id);
        if (found.isEmpty())
            throw new UserNotFoundException("Could not find the user with id " + id);
        return found.get();
    }

    public List<Role> findAllRoles() {
        return roleRepository.findAll();
    }

    public User getByEmail(String email) throws UserNotFoundException {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty())
            throw new UserNotFoundException("User doesn't exist");
        return user.get();
    }

    public boolean isEmailUnique(Long id, String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty())
            return true;
        else if (id == null)
            return false;
        else if (!Objects.equals(user.get().getId(), id))
            return false;
        return true;
    }

    @Transactional
    public User save(User user) {
        if (user.getId() != null) {
            Optional<User> optional = userRepository.findById(user.getId());
            if (optional.isPresent()) {
                User alrdyExist = optional.get();
                if (alrdyExist.getPassword().isEmpty())
                    user.setPassword(alrdyExist.getPassword());
                else
                    user.setPassword(encoder.encode(user.getPassword()));
            }
        } else
            user.setPassword(encoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Transactional
    public void delete(Long id) throws UserNotFoundException {
        Long counted = userRepository.countById(id);
        if (counted == null || counted == 0)
            throw new UserNotFoundException("Could not find user with this ID " + id);
        userRepository.deleteById(id);
    }

    @Transactional
    public void changeEnableState(Long id, boolean isEnable) {
        userRepository.updateEnabledStatus(id, isEnable);
    }
}
