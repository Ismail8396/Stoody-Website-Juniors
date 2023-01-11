package com.loam.stoody.service.communication.report;

import com.loam.stoody.model.communication.report.ReportedUser;
import com.loam.stoody.repository.communication.report.ReportedUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ReportService {
    private final ReportedUserRepository reportedUserRepository;

    public List<ReportedUser> getAllReportedUser(){
        return reportedUserRepository.findAll();
    }

    public void saveReportedUser(ReportedUser reportedUser){
        reportedUserRepository.save(reportedUser);
    }

    public void removeReportedUserById(long id){
        reportedUserRepository.deleteById(id);
    }

    public Boolean doesReportedUserExistByUsername(String reportedBy, String reportedUser){
        return reportedUserRepository.findAll()
                .stream().anyMatch(
                        e-> e.getReportedBy().getUsername().equals(reportedBy)
                                && e.getReportedUser().getUsername().equals(reportedUser));
    }

    public Boolean doesReportedUserExist(ReportedUser reportedUser){
        return reportedUserRepository.findAll()
                .stream().anyMatch(
                        e-> e.getReportedBy().getUsername().equals(reportedUser.getReportedBy().getUsername())
                        && e.getReportedUser().getUsername().equals(reportedUser.getReportedUser().getUsername())
                        && e.getReason().equals(reportedUser.getReason()));
    }
}
