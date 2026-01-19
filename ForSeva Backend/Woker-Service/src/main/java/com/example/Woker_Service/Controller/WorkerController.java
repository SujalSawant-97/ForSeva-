package com.example.Woker_Service.Controller;

import com.example.Woker_Service.Dto.ApiResponse;
import com.example.Woker_Service.Dto.WorkerDetailSearch;
import com.example.Woker_Service.Dto.WorkerRegistrationDTO;
import com.example.Woker_Service.Dto.WorkerSearchDTO;
import com.example.Woker_Service.Model.Worker;
import com.example.Woker_Service.Service.WorkerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/workers")
public class WorkerController {
    @Autowired
    private WorkerService workerService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Worker>> register(
            @RequestHeader("X-User-Id") String userId,
            @RequestBody WorkerRegistrationDTO dto) {
        return ResponseEntity.ok(ApiResponse.success(workerService.registerWorker(userId, dto)));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<WorkerSearchDTO>>> search(
            @RequestParam String category,
            @RequestParam double lat,
            @RequestParam double lng
            //@RequestParam(defaultValue = "10") double radius
            ){
        System.out.println(category);
        System.out.println(lat );
        System.out.println(lng);

        return ResponseEntity.ok(ApiResponse.success(workerService.searchWorkers(category, lng, lat, 10)));
    }
    @GetMapping("/{id}/details")
    public ResponseEntity<ApiResponse<WorkerDetailSearch>> searchdetail(
            @PathVariable String id
    ) {
        return ResponseEntity.ok(ApiResponse.success(workerService.searchdetailservice(id)));
    }

}
