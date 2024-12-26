package com.jingdianjichi.auth.domain.convert;

import com.jingdianjichi.auth.domain.entity.AuthPermissionBO;
import com.jingdianjichi.auth.infra.basic.entity.AuthPermission;
import javax.annotation.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-26T15:49:28+0800",
    comments = "version: 1.4.2.Final, compiler: Eclipse JDT (IDE) 3.40.0.z20241112-1021, environment: Java 17.0.13 (Eclipse Adoptium)"
)
public class AuthPermissionBOConverterImpl implements AuthPermissionBOConverter {

    @Override
    public AuthPermission convertBOToEntity(AuthPermissionBO authPermissionBO) {
        if ( authPermissionBO == null ) {
            return null;
        }

        AuthPermission authPermission = new AuthPermission();

        authPermission.setIcon( authPermissionBO.getIcon() );
        authPermission.setId( authPermissionBO.getId() );
        authPermission.setMenuUrl( authPermissionBO.getMenuUrl() );
        authPermission.setName( authPermissionBO.getName() );
        authPermission.setParentId( authPermissionBO.getParentId() );
        authPermission.setPermissionKey( authPermissionBO.getPermissionKey() );
        authPermission.setShow( authPermissionBO.getShow() );
        authPermission.setStatus( authPermissionBO.getStatus() );
        authPermission.setType( authPermissionBO.getType() );

        return authPermission;
    }
}
