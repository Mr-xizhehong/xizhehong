package com.jingdianjichi.auth.application.convert;

import com.jingdianjichi.auth.application.dto.AuthRoleDTO;
import com.jingdianjichi.auth.domain.entity.AuthRoleBO;
import javax.annotation.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-17T16:25:59+0800",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 1.8.0_421 (Oracle Corporation)"
)
public class AuthRoleDTOConverterImpl implements AuthRoleDTOConverter {

    @Override
    public AuthRoleBO convertDTOToBO(AuthRoleDTO authRoleDTO) {
        if ( authRoleDTO == null ) {
            return null;
        }

        AuthRoleBO authRoleBO = new AuthRoleBO();

        return authRoleBO;
    }
}
