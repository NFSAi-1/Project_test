package com.example.talent_explorer.service;

import com.example.talent_explorer.model.AnswerRequest;
import com.example.talent_explorer.model.CareerPlan;
import com.example.talent_explorer.model.EducationPrediction;
import com.example.talent_explorer.model.EducationResult;
import com.example.talent_explorer.model.PlanningResult;
import com.example.talent_explorer.model.Report;
import com.example.talent_explorer.model.ReportResult;
import com.example.talent_explorer.model.StrengthAssessment;
import com.example.talent_explorer.model.StrengthResult;
import com.example.talent_explorer.repository.CareerPlanRepository;
import com.example.talent_explorer.repository.EducationPredictionRepository;
import com.example.talent_explorer.repository.ReportRepository;
import com.example.talent_explorer.repository.StrengthAssessmentRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDateTime;

@Service
public class AnalysisService {
    private static final Logger log = LoggerFactory.getLogger(AnalysisService.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private final PythonClient pythonClient;
    private final ReportRepository reportRepository;
    private final EducationPredictionRepository educationRepo;
    private final StrengthAssessmentRepository strengthRepo;
    private final CareerPlanRepository careerRepo;

    public AnalysisService(PythonClient pythonClient,
                           ReportRepository reportRepository,
                           EducationPredictionRepository educationRepo,
                           StrengthAssessmentRepository strengthRepo,
                           CareerPlanRepository careerRepo) {
        this.pythonClient = pythonClient;
        this.reportRepository = reportRepository;
        this.educationRepo = educationRepo;
        this.strengthRepo = strengthRepo;
        this.careerRepo = careerRepo;
    }

    public Mono<ReportResult> runFullAnalysis(String userId, String taskId, AnswerRequest request) {
        return pythonClient.compute(request)
                .publishOn(Schedulers.boundedElastic())
                .map(personality -> {
                    personality.setTaskId(taskId);

                    persistAll(userId, taskId, request, personality,
                            personality.getEducation(),
                            personality.getStrengths(),
                            personality.getPlanning());

                    return personality;
                });
    }

    private void persistAll(String userId, String taskId, AnswerRequest request,
                            ReportResult personality, EducationResult edu,
                            StrengthResult str, PlanningResult plan) {
        try {
            Report report = new Report();
            report.setTaskId(taskId);
            report.setUserId(userId);
            report.setRawAnswersJson(toJson(request.getAnswers()));
            if (personality.getDimensionScores() != null) {
                report.setOpenness(personality.getDimensionScores().get("openness"));
                report.setConscientiousness(personality.getDimensionScores().get("conscientiousness"));
                report.setExtraversion(personality.getDimensionScores().get("extraversion"));
                report.setStability(personality.getDimensionScores().get("stability"));
            }
            report.setTagsJson(toJson(personality.getTags()));
            report.setValidityScore(personality.getValidityScore());
            report.setCreatedAt(LocalDateTime.now());
            Report savedReport = reportRepository.save(report);
            Long reportId = savedReport.getId();
            log.info("Report saved: id={}, taskId={}", reportId, taskId);

            if (edu != null) {
                EducationPrediction ep = new EducationPrediction();
                ep.setReportId(reportId);
                ep.setTaskId(taskId);
                ep.setUserId(userId);
                ep.setPredictedDegree(edu.getPredictedDegree());
                ep.setDegreeConfidence(edu.getDegreeConfidence());
                ep.setSubjectDirectionsJson(toJson(edu.getSubjectDirections()));
                ep.setEducationAnalysis(edu.getEducationAnalysis());
                educationRepo.save(ep);
                log.info("EducationPrediction saved for reportId={}", reportId);
            }

            if (str != null) {
                StrengthAssessment sa = new StrengthAssessment();
                sa.setReportId(reportId);
                sa.setTaskId(taskId);
                sa.setUserId(userId);
                sa.setDimensionsJson(toJson(str.getDimensions()));
                sa.setStrengthsJson(toJson(str.getStrengths()));
                sa.setWeaknessesJson(toJson(str.getWeaknesses()));
                sa.setOverallSummary(str.getOverallSummary());
                strengthRepo.save(sa);
                log.info("StrengthAssessment saved for reportId={}", reportId);
            }

            if (plan != null) {
                CareerPlan cp = new CareerPlan();
                cp.setReportId(reportId);
                cp.setTaskId(taskId);
                cp.setUserId(userId);
                cp.setCareerPathsJson(toJson(plan.getCareerPaths()));
                cp.setShortTermPlanJson(toJson(plan.getShortTermPlan()));
                cp.setLongTermPlanJson(toJson(plan.getLongTermPlan()));
                cp.setPlanningSummary(plan.getPlanningSummary());
                careerRepo.save(cp);
                log.info("CareerPlan saved for reportId={}", reportId);
            }
        } catch (Exception e) {
            log.error("Failed to persist analysis results: {}", e.getMessage(), e);
        }
    }

    private String toJson(Object obj) {
        if (obj == null) return null;
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error("JSON serialization failed: {}", e.getMessage());
            return null;
        }
    }
}
