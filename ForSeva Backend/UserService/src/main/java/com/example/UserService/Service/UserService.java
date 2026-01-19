package com.example.UserService.Service;

import com.example.UserService.Dto.LocationUpdateDto;
import com.example.UserService.Dto.UserDto;
import com.example.UserService.Dto.UserRequestDto;
import com.example.UserService.Model.Address;
import com.example.UserService.Model.User;
import com.example.UserService.Repository.UserRepository;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    private final GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);

    public User registerUser( UserDto dto) {
        User user=new User();
        user.setUserId(dto.getUserId());
        user.setEmail(dto.getEmail());
        return userRepository.save(user);

//        User user = new User();
//        user.setUserId(userId);
//        user.setName(dto.getFullName());
//        user.setEmail(dto.getEmail());
//        user.setPhone(dto.getPhone());
//        //user.setProfilePicture(dto.getProfilePicture());
//
//        Address address = new Address();
//        address.setCity(dto.getAddress().getCity());
//        address.setState(dto.getAddress().getState());
//        address.setZipCode(dto.getAddress().getZipcode());
//        address.setLatitude(dto.getAddress().getLatitude());
//        address.setLongitude(dto.getAddress().getLongitude());
//
//        user.setAddress(address);

        // Convert lat/lon to JTS Point (SRID 4326 for WGS84)
        //GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
        //user.setLocation(geometryFactory.createPoint(new Coordinate(dto.getAddress().getLatitude(), dto.getAddress().getLongitude())));

        //return userRepository.save(user);
    }

    public UserRequestDto getUser(String id) {
        User user= userRepository.findById(id).orElseThrow();
        UserRequestDto userRequestDto=new UserRequestDto();
        userRequestDto.setFullName(user.getName());
        userRequestDto.setEmail(user.getEmail());
        userRequestDto.setPhone(user.getPhone());
        if (user.getAddress() != null) {
            LocationUpdateDto locationUpdateDto = new LocationUpdateDto();
            locationUpdateDto.setCity(user.getAddress().getCity());
            locationUpdateDto.setState(user.getAddress().getState());
            locationUpdateDto.setZipcode(user.getAddress().getZipCode());
            locationUpdateDto.setLatitude(user.getAddress().getLongitude());
            locationUpdateDto.setLongitude(user.getAddress().getLongitude());
            userRequestDto.setAddress(locationUpdateDto);
        }else {
            // If no address exists, we explicitly set it to null
            // (or you could set a new empty LocationUpdateDto() if you prefer)
            userRequestDto.setAddress(null);
        }

        return userRequestDto;
    }

    public User upDateUser(String userId, UserRequestDto dto) {
        User user=userRepository.findById(userId).orElseThrow();


        //user.setUserId(userId);
        user.setName(dto.getFullName());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        //user.setProfilePicture(dto.getProfilePicture());

        Address address = new Address();
        address.setCity(dto.getAddress().getCity());
        address.setState(dto.getAddress().getState());
        address.setZipCode(dto.getAddress().getZipcode());
        address.setLatitude(dto.getAddress().getLatitude());
        address.setLongitude(dto.getAddress().getLongitude());

        user.setAddress(address);

        // Convert lat/lon to JTS Point (SRID 4326 for WGS84)
        //GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
        //user.setLocation(geometryFactory.createPoint(new Coordinate(dto.getAddress().getLatitude(), dto.getAddress().getLongitude())));

        return userRepository.save(user);
    }

//    public User updateLocation(String userId, LocationUpdateDto dto) {
//        // 1. Fetch existing user [cite: 52]
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        // 2. Create new spatial Point (Longitude, Latitude) [cite: 47]
//        Point newLocation = geometryFactory.createPoint(new Coordinate(dto.getLongitude(), dto.getLatitude()));
//
//        // 3. Update and save
//        user.setLocation(newLocation);
//        return userRepository.save(user);
//    }
}
