package com.shop.admin.user;

import com.shop.admin.paging.PagingAndSortingHelper;
import com.shop.admin.security.ShopUserDetails;
import com.shop.admin.utils.FileNotSavedException;
import com.shop.admin.utils.FileUploadUtil;
import com.shop.model.Role;
import com.shop.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

    public void listByPage(int pageNum, PagingAndSortingHelper helper) {
        helper.searchEntities(pageNum, PAGE_SIZE, userRepository);
    }

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Could not find the user with id " + id));
    }

    public List<Role> findAllRoles() {
        return roleRepository.findAll();
    }

    public User getByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Could not find the user with email " + email));
    }

    public boolean isEmailUnique(Long id, String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty())
            return true;
        else if (id == null)
            return false;
        else return Objects.equals(user.get().getId(), id);
    }

    public void updateUserAccount(MultipartFile multipart, User user, ShopUserDetails userDetails) {
        try {
            if (!multipart.isEmpty()) {
                var fileName = StringUtils.cleanPath(multipart.getOriginalFilename());
                user.setPhotos(fileName);
                var saved = updateUserAccount(user);
                var uploadDir = "./Shop_WebParent/ShopBackEnd/src/main/resources/static/images/user-images/" + saved.getId();
                FileUploadUtil.saveFile(uploadDir, fileName, multipart);
            } else {
                if (user.getPhotos().isEmpty()) {
                    user.setPhotos(null);
                }
                updateUserAccount(user);
            }
        } catch (IOException e) {
            throw new FileNotSavedException(e.getMessage(), e);
        }

        userDetails.setFirstName(user.getFirstName());
        userDetails.setLastName(user.getLastName());
    }

    public void createNewUser(MultipartFile multipart, User user) {
        try {
            if (!multipart.isEmpty()) {
                var fileName = StringUtils.cleanPath(multipart.getOriginalFilename());
                user.setPhotos(fileName);
                var saved = save(user);
                var uploadDir = "./Shop_WebParent/user-images/" + saved.getId();
                FileUploadUtil.saveFile(uploadDir, fileName, multipart);
            } else {
                if (user.getPhotos().isEmpty()) {
                    user.setPhotos(null);
                }
                save(user);
            }
        } catch (IOException e) {
            throw new FileNotSavedException(e.getMessage(), e);
        }
    }


    @Transactional
    private User save(User formUser) {
        if (formUser.getId() != null) {
            var optional = userRepository.findById(formUser.getId());
            if (optional.isPresent()) {
                var dbUser = optional.get();
                if (formUser.getPassword().isEmpty()) {
                    formUser.setPassword(dbUser.getPassword());
                } else {
                    formUser.setPassword(encoder.encode(formUser.getPassword()));
                }
            }
        } else {
            formUser.setPassword(encoder.encode(formUser.getPassword()));
        }
        return userRepository.save(formUser);
    }

    @Transactional
    private User updateUserAccount(User formUser) {
        var dbUser = userRepository.findById(formUser.getId()).get();

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
    public void delete(Long id) {
        userRepository.countById(id)
                .orElseThrow(() -> new UserNotFoundException("Could not find the user with id " + id));
        userRepository.deleteById(id);
    }

    @Transactional
    public void changeEnableState(Long id, boolean isEnable) {
        userRepository.updateEnabledStatus(id, isEnable);
    }
}
