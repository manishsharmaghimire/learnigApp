//package com.elearn.services.serviceImpl;
//
//import com.elearn.dto.UserDto;
//import com.elearn.entity.User;
//import com.elearn.repository.UserRepository;
//import com.elearn.services.UserService;
//import org.modelmapper.ModelMapper;
//
//import java.util.UUID;
//
//public class UserServiceImpl implements UserService {
//    private UserRepository userRepo;
//
//    private ModelMapper modelMapper;
//
//   // private RoleRepo roleRepo;
//
//   // private PasswordEncoder passwordEncoder;
//    /**
//     * @param dto
//     * @return
//     */
//    @Override
//    public UserDto create(UserDto dto) {
//
//        User user = modelMapper.map(dto, User.class);
//
//
//        user.setUserId(UUID.randomUUID().toString());
//    }
//
//    /**
//     * @param userId
//     * @return
//     */
//    @Override
//    public UserDto geById(String userId) {
//        return null;
//    }
//}
