package org.example.springquartz.controller;

import org.example.springquartz.request.ScheduleCreateRequest;
import org.example.springquartz.request.ScheduleUpsertRequest;
import org.example.springquartz.service.IQuartzScheduleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/demo")
public class DemoController {
    private final IQuartzScheduleService service;

    public DemoController(IQuartzScheduleService service) {
        this.service = service;
    }

    // CREATE
    @PostMapping("/schedules")
    public ResponseEntity<?> create(@RequestBody ScheduleCreateRequest req) {
        service.schedule(req); // chỉ tạo mới, tồn tại thì 409
        return ResponseEntity.status(201).build();
    }

    // UPDATE (full replace)
    @PutMapping("/schedules/{triggerGroup}/{triggerName}")
    public ResponseEntity<?> update(
            @PathVariable String triggerGroup,
            @PathVariable String triggerName,
            @RequestBody ScheduleUpsertRequest req
    ) {
        service.updateSchedule(triggerGroup, triggerName, req);
        return ResponseEntity.ok().build();
    }

    // Command endpoints (giữ như bạn)
    @PostMapping("/triggers/{group}/{name}/pause")
    public ResponseEntity<?> pause(@PathVariable String group, @PathVariable String name){
        service.pause(name, group);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/triggers/{group}/{name}/resume")
    public ResponseEntity<?> resume(@PathVariable String group, @PathVariable String name){
        service.resume(name, group);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/jobs/{group}/{name}")
    public ResponseEntity<?> deleteJob(@PathVariable String group, @PathVariable String name){
        service.deleteJob(name, group);
        return ResponseEntity.noContent().build();
    }
}
