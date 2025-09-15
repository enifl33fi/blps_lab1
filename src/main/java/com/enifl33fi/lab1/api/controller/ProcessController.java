package com.enifl33fi.lab1.api.controller;

import com.enifl33fi.lab1.api.dto.request.AuthRequestDto;
import com.enifl33fi.lab1.api.dto.request.SubscribeRequestDto;
import com.enifl33fi.lab1.api.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/process")
@RequiredArgsConstructor
@Log4j2
public class ProcessController {

    private final RuntimeService runtimeService;
    private final RepositoryService repositoryService;
    private final UserService userService;
    private final TaskService taskService;

    @PostMapping("/start")
    public ResponseEntity<Map<String, Object>> startMainProcess(@RequestBody Map<String, Object> variables) {
        log.info("Starting main BPMN process with variables: {}", variables);
        
        // Start the main process
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(
                "Process_1os97ih", 
                variables
        );
        
        Map<String, Object> response = new HashMap<>();
        response.put("processInstanceId", processInstance.getId());
        response.put("processDefinitionId", processInstance.getProcessDefinitionId());
        response.put("message", "Process started successfully");
        
        log.info("Main process started with ID: {}", processInstance.getId());
        
        return ResponseEntity.ok(response);
    }

    @PostMapping("/auth")
    public ResponseEntity<Map<String, Object>> startAuthProcess(@RequestBody AuthRequestDto authRequest) {
        log.info("Starting authentication process for user: {}", authRequest.getEmail());
        
        Map<String, Object> variables = new HashMap<>();
        variables.put("email", authRequest.getEmail());
        variables.put("password", authRequest.getPassword());
        
        // Determine action based on whether user exists
        String action = "login"; // default
        try {
            // Try to load user - if user doesn't exist, it's registration
            userService.loadUserByUsername(authRequest.getEmail());
            log.info("User exists, setting action to login");
        } catch (Exception e) {
            action = "register";
            log.info("User doesn't exist, setting action to register");
        }
        
        variables.put("action", action);
        
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(
                "Process_1os97ih", 
                variables
        );
        
        Map<String, Object> response = new HashMap<>();
        response.put("processInstanceId", processInstance.getId());
        response.put("message", "Authentication process started");
        response.put("action", action);
        
        return ResponseEntity.ok(response);
    }

    @PostMapping("/subscribe")
    public ResponseEntity<Map<String, Object>> startSubscriptionProcess(
            @RequestParam Long offerId,
            @RequestBody SubscribeRequestDto subscribeRequest,
            @RequestParam String userEmail) {
        
        log.info("Starting subscription process for user: {} to offer: {}", userEmail, offerId);
        
        Map<String, Object> variables = new HashMap<>();
        variables.put("id", offerId.toString());
        variables.put("durationInMonths", String.valueOf(subscribeRequest.getDurationMonths()));
        variables.put("action", "subscribe");
        variables.put("userEmail", userEmail);
        
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(
                "Process_1os97ih", 
                variables
        );
        
        Map<String, Object> response = new HashMap<>();
        response.put("processInstanceId", processInstance.getId());
        response.put("message", "Subscription process started");
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/status/{processInstanceId}")
    public ResponseEntity<Map<String, Object>> getProcessStatus(@PathVariable String processInstanceId) {
        log.info("Getting status for process instance: {}", processInstanceId);
        
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .singleResult();
        
        Map<String, Object> response = new HashMap<>();
        
        if (processInstance != null) {
            response.put("processInstanceId", processInstance.getId());
            response.put("status", "RUNNING");
            response.put("processDefinitionId", processInstance.getProcessDefinitionId());
        } else {
            response.put("status", "COMPLETED");
            response.put("message", "Process instance not found or completed");
        }
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/deployed")
    public ResponseEntity<Map<String, Object>> getDeployedProcesses() {
        log.info("Getting deployed processes");
        List<ProcessDefinition> processDefinitions = repositoryService.createProcessDefinitionQuery().list();
        Map<String, Object> response = new HashMap<>();
        response.put("count", processDefinitions.size());
        List<Map<String, Object>> processes = new ArrayList<>();
        for (ProcessDefinition processDefinition : processDefinitions) {
            Map<String, Object> process = new HashMap<>();
            process.put("id", processDefinition.getId());
            process.put("key", processDefinition.getKey());
            process.put("name", processDefinition.getName());
            process.put("version", processDefinition.getVersion());
            processes.add(process);
        }
        response.put("processes", processes);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/send-email")
    public ResponseEntity<Map<String, Object>> startEmailProcess(@RequestBody Map<String, Object> request) {
        log.info("Starting email sending process for: {}", request.get("email"));
        
        Map<String, Object> variables = new HashMap<>();
        variables.put("email", request.get("email"));
        
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(
                "Process_1ja88do", 
                variables
        );
        
        Map<String, Object> response = new HashMap<>();
        response.put("processInstanceId", processInstance.getId());
        response.put("message", "Email sending process started");
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/active")
    public ResponseEntity<Map<String, Object>> getActiveProcesses() {
        log.info("Getting active processes");
        
        List<ProcessInstance> processInstances = runtimeService.createProcessInstanceQuery()
                .active()
                .list();
        
        Map<String, Object> response = new HashMap<>();
        response.put("count", processInstances.size());
        
        List<Map<String, Object>> instances = new ArrayList<>();
        for (ProcessInstance processInstance : processInstances) {
            Map<String, Object> instance = new HashMap<>();
            instance.put("id", processInstance.getId());
            instance.put("processDefinitionId", processInstance.getProcessDefinitionId());
            instance.put("businessKey", processInstance.getBusinessKey());
            instances.add(instance);
        }
        response.put("instances", instances);
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/tasks")
    public ResponseEntity<Map<String, Object>> getActiveTasks() {
        log.info("Getting active tasks");
        
        List<Task> tasks = taskService.createTaskQuery()
                .active()
                .list();
        
        Map<String, Object> response = new HashMap<>();
        response.put("count", tasks.size());
        
        List<Map<String, Object>> taskList = new ArrayList<>();
        for (Task task : tasks) {
            Map<String, Object> taskInfo = new HashMap<>();
            taskInfo.put("id", task.getId());
            taskInfo.put("name", task.getName());
            taskInfo.put("processInstanceId", task.getProcessInstanceId());
            taskInfo.put("taskDefinitionKey", task.getTaskDefinitionKey());
            taskInfo.put("created", task.getCreateTime());
            taskList.add(taskInfo);
        }
        response.put("tasks", taskList);
        
        return ResponseEntity.ok(response);
    }

    @PostMapping("/tasks/{taskId}/complete")
    public ResponseEntity<Map<String, Object>> completeTask(@PathVariable String taskId, @RequestBody Map<String, Object> variables) {
        log.info("Completing task: {}", taskId);
        
        try {
            taskService.complete(taskId, variables);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Task completed successfully");
            response.put("taskId", taskId);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error completing task: {}", e.getMessage());
            
            Map<String, Object> response = new HashMap<>();
            response.put("error", e.getMessage());
            
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/test-mail-send")
    public ResponseEntity<Map<String, Object>> testMailSend(@RequestBody Map<String, Object> request) {
        log.info("Testing mail-send process directly for: {}", request.get("email"));
        
        try {
            Map<String, Object> variables = new HashMap<>();
            variables.put("email", request.get("email"));
            
            ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(
                    "Process_1ja88do", 
                    variables
            );
            
            Map<String, Object> response = new HashMap<>();
            response.put("processInstanceId", processInstance.getId());
            response.put("message", "Mail-send process started directly");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error starting mail-send process: {}", e.getMessage());
            
            Map<String, Object> response = new HashMap<>();
            response.put("error", e.getMessage());
            
            return ResponseEntity.badRequest().body(response);
        }
    }
} 