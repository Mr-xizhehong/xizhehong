package com.jingdianjichi.auth.domain.convert;

import com.jingdianjichi.auth.domain.entity.AuthUserBO;
import com.jingdianjichi.auth.infra.basic.entity.AuthUser;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-26T15:49:28+0800",
    comments = "version: 1.4.2.Final, compiler: Eclipse JDT (IDE) 3.40.0.z20241112-1021, environment: Java 17.0.13 (Eclipse Adoptium)"
)
public class AuthUserBOConverterImpl implements AuthUserBOConverter {

    @Override
    public AuthUser convertBOToEntity(AuthUserBO authUserBO) {
        if ( authUserBO == null ) {
            return null;
        }

        AuthUser authUser = new AuthUser();

        authUser.setAvatar( authUserBO.getAvatar() );
        authUser.setEmail( authUserBO.getEmail() );
        authUser.setExtJson( authUserBO.getExtJson() );
        authUser.setId( authUserBO.getId() );
        authUser.setIntroduce( authUserBO.getIntroduce() );
        authUser.setNickName( authUserBO.getNickName() );
        authUser.setPassword( authUserBO.getPassword() );
        authUser.setPhone( authUserBO.getPhone() );
        authUser.setSex( authUserBO.getSex() );
        authUser.setStatus( authUserBO.getStatus() );
        authUser.setUserName( authUserBO.getUserName() );

        return authUser;
    }

    @Override
    public AuthUserBO convertEntityToBO(AuthUser authUser) {
        if ( authUser == null ) {
            return null;
        }

        AuthUserBO authUserBO = new AuthUserBO();

        authUserBO.setAvatar( authUser.getAvatar() );
        authUserBO.setEmail( authUser.getEmail() );
        authUserBO.setExtJson( authUser.getExtJson() );
        authUserBO.setId( authUser.getId() );
        authUserBO.setIntroduce( authUser.getIntroduce() );
        authUserBO.setNickName( authUser.getNickName() );
        authUserBO.setPassword( authUser.getPassword() );
        authUserBO.setPhone( authUser.getPhone() );
        authUserBO.setSex( authUser.getSex() );
        authUserBO.setStatus( authUser.getStatus() );
        authUserBO.setUserName( authUser.getUserName() );

        return authUserBO;
    }

    @Override
    public List<AuthUserBO> convertEntityToBO(List<AuthUser> authUserList) {
        if ( authUserList == null ) {
            return null;
        }

        List<AuthUserBO> list = new ArrayList<AuthUserBO>( authUserList.size() );
        for ( AuthUser authUser : authUserList ) {
            list.add( convertEntityToBO( authUser ) );
        }

        return list;
    }
}
