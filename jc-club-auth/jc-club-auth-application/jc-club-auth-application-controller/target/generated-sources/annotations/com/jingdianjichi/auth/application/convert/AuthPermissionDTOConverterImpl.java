package com.jingdianjichi.auth.application.convert;

import com.jingdianjichi.auth.application.dto.AuthPermissionDTO;
import com.jingdianjichi.auth.domain.entity.AuthPermissionBO;
import javax.annotation.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-26T15:49:29+0800",
    comments = "version: 1.4.2.Final, compiler: Eclipse JDT (IDE) 3.40.0.z20241112-1021, environment: Java 17.0.13 (Eclipse Adoptium)"
)
public class AuthPermissionDTOConverterImpl implements AuthPermissionDTOConverter {

    @Override
    public AuthPermissionBO convertDTOToBO(AuthPermissionDTO authPermissionDTO) {
        if ( authPermissionDTO == null ) {
            return null;
        }

        AuthPermissionBO authPermissionBO = new AuthPermissionBO();

        authPermissionBO.setIcon( authPermissionDTO.getIcon() );
        authPermissionBO.setId( authPermissionDTO.getId() );
        authPermissionBO.setMenuUrl( authPermissionDTO.getMenuUrl() );
        authPermissionBO.setName( authPermissionDTO.getName() );
        authPermissionBO.setParentId( authPermissionDTO.getParentId() );
        authPermissionBO.setPermissionKey( authPermissionDTO.getPermissionKey() );
        authPermissionBO.setShow( authPermissionDTO.getShow() );
        authPermissionBO.setStatus( authPermissionDTO.getStatus() );
        authPermissionBO.setType( authPermissionDTO.getType() );

        return authPermissionBO;
    }
}
