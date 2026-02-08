package com.krowdless.scheduler;

import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.krowdless.entity.Destination;
import com.krowdless.entity.DestinationCrowd;
import com.krowdless.repository.DestinationCrowdRepository;
import com.krowdless.repository.DestinationRepository;
import com.krowdless.service.CrowdCalculationService;

@Component
@EnableScheduling
public class CrowdUpdateScheduler {

    private static final int BATCH_SIZE = 500;
    private int currentPage = 0;

    private final DestinationRepository destinationRepository;
    private final DestinationCrowdRepository destinationCrowdRepository;
    private final CrowdCalculationService crowdService;

    public CrowdUpdateScheduler(
            DestinationRepository destinationRepository,
            DestinationCrowdRepository destinationCrowdRepository,
            CrowdCalculationService crowdService) {
        this.destinationRepository = destinationRepository;
        this.destinationCrowdRepository = destinationCrowdRepository;
        this.crowdService = crowdService;
    }

    @Scheduled(cron = "0 */30 * * * *")
    public void updateCrowdLevels() {

        Pageable pageable = PageRequest.of(
                currentPage,
                BATCH_SIZE,
                Sort.by(Sort.Direction.ASC, "id")
        );

        Page<Destination> page =
                destinationRepository.findByActiveTrue(pageable);

        if (!page.hasContent()) {
            currentPage = 0;
            return;
        }

        for (Destination d : page.getContent()) {

            if (d.getLatitude() == null || d.getLongitude() == null) {
                continue;
            }

            try {
                String crowd =
                        crowdService.calculateCrowdLevel(
                                d.getLatitude(),
                                d.getLongitude());

                DestinationCrowd dc = new DestinationCrowd();
                dc.setDestinationId(d.getId());
                dc.setCrowdLevel(crowd);
                dc.setUpdatedAt(LocalDateTime.now());

                destinationCrowdRepository.save(dc);

            } catch (Exception e) {
                System.err.println(
                        "Crowd update failed for id=" + d.getId());
            }
        }

        currentPage++;
    }
}
