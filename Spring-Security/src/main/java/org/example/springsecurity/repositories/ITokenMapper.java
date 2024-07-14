//package org.example.springsecurity.repositories;
//
//import org.apache.ibatis.annotations.Delete;
//import org.apache.ibatis.annotations.Mapper;
//import org.apache.ibatis.annotations.Select;
//import org.example.springsecurity.configurations.security.UserDetailsImpl;
//import org.example.springsecurity.requests.LoginRequest;
//import org.example.springsecurity.requests.RegisterUserRequest;
//import org.example.springsecurity.responses.ResetTokenRequest;
//import org.example.springsecurity.responses.ResetTokenResponse;
//import org.example.springsecurity.responses.TokenResetResponse;
//
//import java.util.Optional;
//
//@Mapper
//public interface ITokenMapper {
//    @Select("SELECT * FROM TOKEN_RESET WHERE TOKEN = #{token}")
//    ResetTokenResponse findByToken(String token);
//    int saveToken(ResetTokenRequest request);
//    @Delete("DELETE FROM TOKEN_RESET WHERE TOKEN = #{token}")
//    void deleteToken(String token);
//}
