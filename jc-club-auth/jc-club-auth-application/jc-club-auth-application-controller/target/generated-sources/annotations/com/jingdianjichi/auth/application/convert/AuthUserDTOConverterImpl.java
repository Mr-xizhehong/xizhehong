package com.jingdianjichi.auth.application.convert;

import com.jingdianjichi.auth.domain.entity.AuthUserBO;
import com.jingdianjichi.auth.entity.AuthUserDTO;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-26T15:49:29+0800",
    comments = "version: 1.4.2.Final, compiler: Eclipse JDT (IDE) 3.40.0.z20241112-1021, environment: Java 17.0.13 (Eclipse Adoptium)"
)
public class AuthUserDTOConverterImpl implements AuthUserDTOConverter {

    @Override
    public AuthUserBO convertDTOToBO(AuthUserDTO authUserDTO) {
        if ( authUserDTO == null ) {
            return null;
        }

        AuthUserBO authUserBO = new AuthUserBO();

        authUserBO.setAvatar( authUserDTO.getAvatar() );
        authUserBO.setEmail( authUserDTO.getEmail() );
        authUserBO.setExtJson( authUserDTO.getExtJson() );
        authUserBO.setId( authUserDTO.getId() );
        authUserBO.setIntroduce( authUserDTO.getIntroduce() );
        authUserBO.setNickName( authUserDTO.getNickName() );
        authUserBO.setPassword( authUserDTO.getPassword() );
        authUserBO.setPhone( authUserDTO.getPhone() );
        authUserBO.setSex( authUserDTO.getSex() );
        authUserBO.setStatus( authUserDTO.getStatus() );
        authUserBO.setUserName( authUserDTO.getUserName() );

        return authUserBO;
    }

    @Override
    public AuthUserDTO convertBOToDTO(AuthUserBO authUserBO) {
        if ( authUserBO == null ) {
            return null;
        }

        AuthUserDTO authUserDTO = new AuthUserDTO();

        authUserDTO.setAvatar( authUserBO.getAvatar() );
        authUserDTO.setEmail( authUserBO.getEmail() );
        authUserDTO.setExtJson( authUserBO.getExtJson() );
        authUserDTO.setId( authUserBO.getId() );
        authUserDTO.setIntroduce( authUserBO.getIntroduce() );
        authUserDTO.setNickName( authUserBO.getNickName() );
        authUserDTO.setPassword( authUserBO.getPassword() );
        authUserDTO.setPhone( authUserBO.getPhone() );
        authUserDTO.setSex( authUserBO.getSex() );
        authUserDTO.setStatus( authUserBO.getStatus() );
        authUserDTO.setUserName( authUserBO.getUserName() );

        return authUserDTO;
    }

    @Override
    public List<AuthUserDTO> convertBOToDTO(List<AuthUserBO> authUserBO) {
        if ( authUserBO == null ) {
            return null;
        }

        List<AuthUserDTO> list = new ArrayList<AuthUserDTO>( authUserBO.size() );
        for ( AuthUserBO authUserBO1 : authUserBO ) {
            list.add( convertBOToDTO( authUserBO1 ) );
        }

        return list;
    }
}
