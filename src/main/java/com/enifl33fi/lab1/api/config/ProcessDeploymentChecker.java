package com.enifl33fi.lab1.api.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Log4j2
public class ProcessDeploymentChecker {
    
    private final RepositoryService repositoryService;
    
    @PostConstruct
    public void checkDeployedProcesses() {
        log.info("Checking deployed BPMN processes...");
        
        List<ProcessDefinition> processDefinitions = repositoryService.createProcessDefinitionQuery()
                .list();
        
        if (processDefinitions.isEmpty()) {
            log.warn("No BPMN processes found in repository!");
        } else {
            log.info("Found {} deployed processes:", processDefinitions.size());
            for (ProcessDefinition processDefinition : processDefinitions) {
                log.info("  - Process: {} (ID: {}, Key: {}, Version: {})", 
                    processDefinition.getName(),
                    processDefinition.getId(),
                    processDefinition.getKey(),
                    processDefinition.getVersion());
            }
        }
    }
} 