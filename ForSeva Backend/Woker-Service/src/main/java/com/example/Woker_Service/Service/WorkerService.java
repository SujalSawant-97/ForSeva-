package com.example.Woker_Service.Service;

import com.example.Woker_Service.Client.JobClient;
//import com.example.Woker_Service.Client.ReviewClient;
import com.example.Woker_Service.Dto.*;
import com.example.Woker_Service.Model.Address;
import com.example.Woker_Service.Model.Worker;
import com.example.Woker_Service.Model.WorkerDetails;
import com.example.Woker_Service.Repository.WorkerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class WorkerService {
    @Autowired
    private WorkerRepository workerRepository;
    @Autowired
    private JobClient jobClient;

    public Worker registerWorker(String userId, WorkerRegistrationDTO dto) {
        Worker worker = new Worker();
        worker.setUserId(userId);
        worker.setName(dto.getName());
        worker.setEmail(dto.getEmail());
        worker.setPhone(dto.getPhone());
        worker.setUserType("worker");

        // Map Address with GeoJsonPoint
        Address addr = new Address();
        addr.setCity(dto.getAddress().getCity());
        addr.setLongitude(dto.getAddress().getLongitude());
        addr.setLatitude(dto.getAddress().getLatitude());
        // MongoDB GeoJSON order: Longitude, Latitude
        addr.setLocation(new GeoJsonPoint(dto.getAddress().getLongitude(), dto.getAddress().getLatitude()));
        worker.setAddress(addr);

        // Map Worker Details
        WorkerDetails details = new WorkerDetails();
        details.setSkills(dto.getWorkerDetails().getSkills());
        details.setHourlyRate(dto.getWorkerDetails().getHourlyRate());
        details.setAvailability(true);
        details.setRating(0.0);
        details.setReviews(new ArrayList<>());
        worker.setWorkerDetails(details);

        return workerRepository.save(worker);
    }

    public List<WorkerSearchDTO> searchWorkers(String skill, double lng, double lat, double km) {
        GeoJsonPoint userLocation = new GeoJsonPoint(lng, lat);
        System.out.println("Query Point: " + userLocation);

        Distance searchRange = new Distance(km, Metrics.KILOMETERS);
        System.out.println(searchRange);

        // 1. Existing logic: Find workers nearby with the right skill
        List<Worker> workers = workerRepository.searchWorkers(
                skill, lng, lat,10000
        );
        System.out.println(workers.getFirst());

        // 2. Transformation logic: Convert Entity to DTO with extra info
        return workers.stream().map(worker -> {
            // Fetch average rating from Review-Service using Feign
            Double avgRating = jobClient.getAverageRating(worker.getUserId()).getData();
            System.out.println(avgRating);
            double workerLat = worker.getAddress().getLocation().getY();
            double workerLng = worker.getAddress().getLocation().getX();

            double distanceKm = calculateDistance(lat, lng, workerLat, workerLng);
            String formattedDistance = String.format("%.1f km", distanceKm);
            System.out.println(formattedDistance);

            return new WorkerSearchDTO(
                    worker.getUserId(),
                    worker.getName(),
                    worker.getProfilePicture(),// Name from Worker/User profile
                    avgRating != null ? avgRating : 0.0 ,// Rating from Review service
                    worker.getWorkerDetails().getHourlyRate(),
                    formattedDistance
            );
        }).collect(Collectors.toList());
    }
    //Future Optimization
    //Pro Optimization (Recommended for later): Create an endpoint in Job Service
    // that accepts a List of IDs (List<String> workerIds) and returns a Map<String,
    // Double> of ratings in one single call. Then you only make 1 network request
    // instead of 50.
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Radius of the earth in km
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        // Returns distance in KM (e.g., 2.5)
        return R * c;
    }

    public WorkerDetailSearch searchdetailservice(String workerId) {
        // 1. Fetch Local Data (Profile, Bio, Experience)
        Worker worker = workerRepository.findById(workerId)
                .orElseThrow(() -> new RuntimeException("Worker not found"));

        // 2. Fetch Remote Data (Rating, Job Count, Reviews) via Feign
        // Using CompletableFuture for parallel fetching (Optional but faster)
        Double avgRating = jobClient.getAverageRating(workerId).getData();
        Long jobCount = jobClient.getWorkerJobCount(workerId);
        List<ReviewModel> reviews = jobClient.getWorkerReviews(workerId);

        // 3. Map to DTO
        WorkerDetailSearch dto = new WorkerDetailSearch();
        dto.setId(worker.getUserId());
        dto.setExperience(worker.getExperienceYears());
        dto.setName(worker.getName());
        dto.setProfileImageUrl(worker.getProfilePicture());

        // From Local Entity
        dto.setBio(worker.getWorkerDetails().getBio());
        dto.setExperience(worker.getWorkerDetails().getExperienceYears()); // Local Data
        dto.setHourlyRate(worker.getWorkerDetails().getHourlyRate());
        dto.setDistance("Calculating..."); // You can calc distance here if you have user lat/lng

        // From Feign (Job Service)
        dto.setRating(avgRating != null ? avgRating : 0.0);
        dto.setJobsDone(jobCount != null ? jobCount.intValue() : 0);       // Remote Data
        dto.setReviews(reviews != null ? reviews : new ArrayList<>());

        return dto;
    }
}
