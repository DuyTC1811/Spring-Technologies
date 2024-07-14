package org.example.springsecurity.repositories;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.example.springsecurity.models.TokenStorageInfo;
import org.example.springsecurity.requests.TokenStorageReq;

@Mapper
public interface ITokenStorageMapper {
    @Insert("INSERT INTO token_storage (token_id, asset_token, refresh_token, username) VALUES (#{tokenId}, #{assetToken}, #{refreshToken}, #{username})")
    void insertToken(TokenStorageReq request);

    @Update("UPDATE token_storage SET status = 'INACTIVE', logout_date = CURRENT_TIMESTAMP WHERE asset_token = #{assetToken}")
    void killToken(String assetToken);

    @Select("SELECT status FROM token_storage WHERE asset_token = #{assetToken}")
    String findAssetToken(String assetToken);

    @Select("SELECT u.username, u.email, u.mobile, ts.status, ts.cout_refresh FROM token_storage ts LEFT JOIN users u ON ts.username = u.username WHERE ts.refresh_token = #{refreshToken}")
    TokenStorageInfo findReFreshToken(String refreshToken);

    @Update("UPDATE token_storage SET asset_token = #{accessToken}, updated_date = CURRENT_TIMESTAMP, cout_refresh = #{coutRefresh} WHERE refresh_token = #{refreshToken}")
    void updateReFreshToken(String accessToken, String refreshToken, int coutRefresh);

}
