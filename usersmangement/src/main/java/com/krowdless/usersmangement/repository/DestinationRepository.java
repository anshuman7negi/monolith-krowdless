package com.krowdless.usersmangement.repository;

import com.krowdless.usersmangement.entity.Destination;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface DestinationRepository
        extends JpaRepository<Destination, Long>,
                JpaSpecificationExecutor<Destination> {  
                        
        Page<Destination> findByActiveTrue(Pageable pageable);

}
