package ua.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.model.Home;
import ua.util.RequestUtil;
import ua.model.LegalEntity;
import ua.repository.LegalEntityRepository;
import ua.service.LegalEntityService;

import javax.servlet.http.HttpSession;

import java.util.Objects;

import static ua.config.AppConfig.SESSION_ATTRIBUTE_LEGAL_ENTITY;

@Service
@Transactional
public class LegalEntityServiceImpl implements LegalEntityService {

    private LegalEntityRepository legalEntityRepository;

    private HttpSession httpSession;

    @Autowired
    public void setLegalEntityRepository(LegalEntityRepository legalEntityService){
        this.legalEntityRepository = legalEntityService;
    }

    @Autowired
    public void setHttpSession(HttpSession httpSession){
        this.httpSession = httpSession;
    }

    @Override
    public LegalEntity getCurrent(){
        Object attributeLegalEntity = httpSession.getAttribute(SESSION_ATTRIBUTE_LEGAL_ENTITY);
        if(Objects.isNull(attributeLegalEntity)){
            String subDomain = RequestUtil.getSubDomain();
            if(!subDomain.isBlank()){
                LegalEntity legalEntity = getBySubDomain(subDomain);
                httpSession.setAttribute(SESSION_ATTRIBUTE_LEGAL_ENTITY, legalEntity);
                return legalEntity;
            }
            return null;
        }
        return (LegalEntity) attributeLegalEntity;
    }

    @Override
    public LegalEntity getBySubDomain(String subDomain){
        return legalEntityRepository.findBySubdomain(subDomain);
    }

    @Override
    public LegalEntity update(LegalEntity legalEntity){
        return legalEntityRepository.save(legalEntity);
    }

    @Override
    public Home setDefaultHome(Home home) {
        LegalEntity legalEntity = getCurrent();
        if (legalEntity == null)
            return null;

        legalEntity.setDefaultHome(home);
        LegalEntity savedLegalEntity = update(legalEntity);
        return savedLegalEntity.getDefaultHome();
    }
}
