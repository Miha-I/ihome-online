package ua.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.exception.FailedSaveModelException;
import ua.exception.FailedUpdateModelException;
import ua.exception.ModelNotFoundException;
import ua.exception.RemoveModelException;
import ua.model.Home;
import ua.model.LegalEntity;
import ua.repository.HomeRepository;
import ua.service.HomeService;
import ua.service.LegalEntityService;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class HomeServiceImpl implements HomeService {

    private final static Logger logger = LoggerFactory.getLogger(HomeServiceImpl.class);

    private LegalEntityService legalEntityService;

    private HomeRepository homeRepository;

    @Autowired
    public void setHomeRepository(HomeRepository homeRepository){
        this.homeRepository = homeRepository;
    }

    @Autowired
    public void setLegalEntityService(LegalEntityService legalEntityService){
        this.legalEntityService = legalEntityService;
    }

    @Override
    public List<Home> getAll(){
        LegalEntity legalEntity = legalEntityService.getCurrent();
        if(legalEntity != null){
            return homeRepository.findByLegalEntityId(legalEntityService.getCurrent().getId());
        }
        return null;
    }

    @Override
    public Home add(Home home){
        Home savedHome;
        LegalEntity legalEntity = legalEntityService.getCurrent();
        if(legalEntity == null)
            return null;
        home.setId(0);
        home.setLegalEntity(legalEntity);
        LocalDate currentPeriod = LocalDate.now().withDayOfMonth(1);
        home.setCurrentPeriod(currentPeriod);
        savedHome = homeRepository.save(home);
        if(savedHome.getId() == 0) {
            logger.error("Failed save Home model");
            throw new FailedSaveModelException("Failed add Home model");
        }
        legalEntity.setDefaultHome(savedHome);
        LegalEntity savedLegalEntity = legalEntityService.update(legalEntity);
        if(savedLegalEntity == null) {
            logger.error("Failed update Home model");
            throw new FailedUpdateModelException("Failed update LegalEntity model");
        }
        return savedHome;
    }

    @Override
    public Home get(int id){
        LegalEntity legalEntity = legalEntityService.getCurrent();
        if(legalEntity != null)
            return homeRepository.findByIdAndLegalEntityId(id, legalEntity.getId());
        return null;
    }

    @Override
    public Home update(int id, Home home){
        LegalEntity legalEntity = legalEntityService.getCurrent();
        if(legalEntity != null){
            Home oldHome = homeRepository.findByIdAndLegalEntityId(id, legalEntity.getId());
            if(oldHome != null) {
                home.setCurrentPeriod(oldHome.getCurrentPeriod());
                home.setLegalEntity(legalEntity);
                home.setId(oldHome.getId());
                return homeRepository.save(home);
            }
        }
        return null;
    }

    @Override
    public void delete(int id) {
        LegalEntity legalEntity = legalEntityService.getCurrent();
        if(legalEntity != null){
            Home home = homeRepository.findByIdAndLegalEntityId(id, legalEntity.getId());
            if(home == null)
                throw new ModelNotFoundException("Model Home not found with id - " + id);

            int countComes = getAll().size();
            if(home.getId() == legalEntity.getDefaultHome().getId() && countComes > 1)
                throw new RemoveModelException("Cannot delete current home");

            homeRepository.delete(home);
        }
    }
}
