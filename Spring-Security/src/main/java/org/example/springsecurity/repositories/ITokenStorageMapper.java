package org.example.springsecurity.repositories;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.example.springsecurity.requests.TokenStorageReq;

@Mapper
public interface ITokenStorageMapper {
    @Insert("INSERT INTO token_storage (token_id, asset_token, refresh_token, username) VALUES (#{tokenId}, #{assetToken}, #{refreshToken}, #{username})")
    void insertToken(TokenStorageReq request);

    @Update("UPDATE token_storage SET active = 'INACTIVE', logout_date = CURRENT_TIMESTAMP WHERE asset_token = #{assetToken}")
    void killToken(String assetToken);

    @Select("SELECT active FROM token_storage WHERE asset_token = #{assetToken}")
    String findAssetToken(String assetToken);
}
