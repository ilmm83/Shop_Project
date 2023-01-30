package com.shop.admin.service.user;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shop.admin.exception.user.UserNotFoundException;
import com.shop.admin.repository.user.RoleRepository;
import com.shop.admin.repository.user.UserRepository;
import com.shop.model.Role;
import com.shop.model.User;

import lombok.RequiredArgsConstructor;

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
    public User save(User formUser) {
        if (formUser.getId() != null) {
            Optional<User> optional = userRepository.findById(formUser.getId());
            if (optional.isPresent()) {
                User dbUser = optional.get();
                if (formUser.getPassword().isEmpty())
                    formUser.setPassword(dbUser.getPassword());
                else
                    formUser.setPassword(encoder.encode(formUser.getPassword()));
            }
        } else
            formUser.setPassword(encoder.encode(formUser.getPassword()));
        return userRepository.save(formUser);
    }

    @Transactional
    public User updateUserAccount(User formUser) throws UserNotFoundException {
        Optional<User> optional = userRepository.findById(formUser.getId());
        if (optional.isEmpty())
            throw new UserNotFoundException("Could not find a user with ID : " + formUser.getId());

        User dbUser = optional.get();
        if (!formUser.getPassword().isEmpty())
            dbUser.setPassword(encoder.encode(formUser.getPassword()));
        if (formUser.getFirstName() != null)
            dbUser.setFirstName(formUser.getFirstName());
        if (formUser.getLastName() != null)
            dbUser.setLastName(formUser.getLastName());
        if (formUser.getEnabled() != null)
            dbUser.setEnabled(formUser.getEnabled());
        if (formUser.getPhotos() != null)
            dbUser.setPhotos(formUser.getPhotos());

        return userRepository.save(dbUser);
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
