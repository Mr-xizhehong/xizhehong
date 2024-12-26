package com.jingdianjichi.auth.application.convert;

import com.jingdianjichi.auth.application.dto.AuthRoleDTO;
import com.jingdianjichi.auth.domain.entity.AuthRoleBO;
import javax.annotation.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-26T15:49:29+0800",
    comments = "version: 1.4.2.Final, compiler: Eclipse JDT (IDE) 3.40.0.z20241112-1021, environment: Java 17.0.13 (Eclipse Adoptium)"
)
public class AuthRoleDTOConverterImpl implements AuthRoleDTOConverter {

    @Override
    public AuthRoleBO convertDTOToBO(AuthRoleDTO authRoleDTO) {
        if ( authRoleDTO == null ) {
            return null;
        }

        AuthRoleBO authRoleBO = new AuthRoleBO();

        authRoleBO.setId( authRoleDTO.getId() );
        authRoleBO.setRoleKey( authRoleDTO.getRoleKey() );
        authRoleBO.setRoleName( authRoleDTO.getRoleName() );

        return authRoleBO;
    }
}
