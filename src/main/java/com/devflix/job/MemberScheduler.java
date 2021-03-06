package com.devflix.job;

import com.devflix.entity.MemberConfirm;
import com.devflix.service.MemberConfirmService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MemberScheduler {

    private final MemberConfirmService memberConfirmService;
    private final Logger logger = LoggerFactory.getLogger(MemberScheduler.class);

    @Scheduled(cron = "0 0 */3 * * *", zone = "Asia/Seoul")
    public void deleteMemberConfirm() {
        final List<MemberConfirm> all = memberConfirmService.findAll();
        Calendar yesterday = Calendar.getInstance();

        yesterday.setTime(new Date());
        yesterday.add(Calendar.DATE, -1);

        logger.info("MemberConfirm delete start ...");

        for (MemberConfirm confirm : all) {
            if (yesterday.getTime().compareTo(confirm.getCreateAt()) > 0) {
                logger.info("MemberConfirm " + confirm.getEmail() + " delete !!");
                memberConfirmService.deleteMemberConfirm(confirm);
            }
        }

        logger.info("MemberConfirm delete end ...");
    }
}
