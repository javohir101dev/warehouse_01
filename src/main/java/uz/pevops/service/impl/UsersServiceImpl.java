package uz.pevops.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.pevops.domain.Users;
import uz.pevops.repository.UsersRepository;
import uz.pevops.service.UsersService;
import uz.pevops.service.dto.UsersDTO;
import uz.pevops.service.mapper.UsersMapper;

/**
 * Service Implementation for managing {@link Users}.
 */
@Service
@Transactional
public class UsersServiceImpl implements UsersService {

    private final Logger log = LoggerFactory.getLogger(UsersServiceImpl.class);

    private final UsersRepository usersRepository;

    private final UsersMapper usersMapper;

    public UsersServiceImpl(UsersRepository usersRepository, UsersMapper usersMapper) {
        this.usersRepository = usersRepository;
        this.usersMapper = usersMapper;
    }

    @Override
    public UsersDTO save(UsersDTO usersDTO) {
        log.debug("Request to save Users : {}", usersDTO);
        Users users = usersMapper.toEntity(usersDTO);
        users = usersRepository.save(users);
        return usersMapper.toDto(users);
    }

    @Override
    public UsersDTO update(UsersDTO usersDTO) {
        log.debug("Request to save Users : {}", usersDTO);
        Users users = usersMapper.toEntity(usersDTO);
        users = usersRepository.save(users);
        return usersMapper.toDto(users);
    }

    @Override
    public Optional<UsersDTO> partialUpdate(UsersDTO usersDTO) {
        log.debug("Request to partially update Users : {}", usersDTO);

        return usersRepository
            .findById(usersDTO.getId())
            .map(existingUsers -> {
                usersMapper.partialUpdate(existingUsers, usersDTO);

                return existingUsers;
            })
            .map(usersRepository::save)
            .map(usersMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UsersDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Users");
        return usersRepository.findAll(pageable).map(usersMapper::toDto);
    }

    public Page<UsersDTO> findAllWithEagerRelationships(Pageable pageable) {
        return usersRepository.findAllWithEagerRelationships(pageable).map(usersMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UsersDTO> findOne(Long id) {
        log.debug("Request to get Users : {}", id);
        return usersRepository.findOneWithEagerRelationships(id).map(usersMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Users : {}", id);
        usersRepository.deleteById(id);
    }
}
