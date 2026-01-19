package com.example.Job_Service.Service;

import com.example.Job_Service.Dto.*;
import com.example.Job_Service.Model.*;
import com.example.Job_Service.Repository.JobRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;



@Slf4j
@Service
public class JobService {
    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    private static final String TOPIC = "job-events";
    @Autowired
    private JobRepository jobRepository;

    public Job createJob(JobRequestDTO dto, String customerId) {
        Job job = new Job();
        job.setCustomerId(customerId);
        job.setCustomerName(dto.getCustomerName());
        job.setWorkerId(dto.getWorkerId());
        job.setWorkerName(dto.getWorkerNmae());
        job.setServiceType(dto.getServiceName());
        job.setJobDescription(dto.getServiceDescription());
        // 1. Convert String to LocalDateTime
        LocalDateTime date = LocalDateTime.parse(dto.getScheduledAt());

// 2. Set it to the entity
        job.setScheduledAt(date);
        job.setStatus(JobStatus.PENDING);

        JobLocation loc = new JobLocation();
        loc.setCity(dto.getCity());
        loc.setState(dto.getState());
        loc.setZipCode(dto.getZipcode());
        loc.setLatitude(dto.getLat());
        loc.setLongitude(dto.getLongi());
        job.setLocation(loc);

        PaymentDetails payment = new PaymentDetails();
        payment.setAmount(dto.getPrice());
        payment.setPaymentStatus("pending");
        job.setPaymentDetails(payment);
        Job savedJob = jobRepository.save(job);

        // Produce event: Notify worker about new job
        JobEvent event = new JobEvent(savedJob.getJobId(), customerId, savedJob.getWorkerId(),
                savedJob.getServiceType(), "PENDING", "New job available!");
        kafkaTemplate.send(TOPIC, event);

        return savedJob;
    }

    // 2. Logic to update status (e.g., Worker accepts or completes job)
    public void updateStatus(String jobId, JobStatus newStatus,String workerId) {
        try{
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found with ID: " + jobId));

        // Business Rule: Validate transitions if necessary (e.g., can't complete a canceled job)
        job.setStatus(newStatus);
         jobRepository.save(job);
        String notificationMessage = "";
        switch (newStatus) {
            case ACCEPTED:
                notificationMessage = "Good news! Worker " + job.getWorkerName() + " has accepted your job.";
                break;
            case CANCELED:
                notificationMessage = "Worker " + job.getWorkerName() + " has declined your job request.";
                break;
            case COMPLETED:
                notificationMessage = "Job Completed! Please rate your experience with " + job.getWorkerName();
                break;
            default:
                notificationMessage = "Job status updated to " + newStatus;
        }

        // 5. Send Kafka Event
        // We send it to the USER (job.getUserId() is the recipient)
        JobEvent event = new JobEvent(
                jobId,
                job.getCustomerId(),
                job.getWorkerId(),
                job.getServiceType(),
                newStatus.toString(),
                notificationMessage
        );

        // Send to Kafka
        kafkaTemplate.send(TOPIC, event);
        System.out.println("Kafka Event Sent: " + notificationMessage);}
        catch (RuntimeException e) {
            throw new RuntimeException(e);
        }

    }

    // 3. Retrieve all bookings for a specific customer
    public List<BookingDTO> getJobsByCustomer(String customerId) {
        List<Job> jobs= jobRepository.findByCustomerId(customerId);
        return jobs.stream()
                .map(job -> new BookingDTO(
                        job.getJobId(),
                        job.getServiceType(),
                        job.getScheduledAt().toString(),
                        job.getStatus().toString(),
                        job.getPaymentDetails().getAmount()

                ))
                .collect(Collectors.toList());

    }

    public BookingDetail getJobById(String bookingId){
        Job job = jobRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Job not found with ID: " + bookingId));

        BookingDetail bookingDetail=new BookingDetail();
        bookingDetail.setId(job.getJobId());
        bookingDetail.setWorkerName(job.getWorkerName());
        bookingDetail.setCategoryName(job.getServiceType());
        bookingDetail.setServiceDescription(job.getJobDescription());
        bookingDetail.setScheduledAt(job.getScheduledAt().toString());
        bookingDetail.setCost(job.getPaymentDetails().getAmount());
        bookingDetail.setPaymentStatus(job.getPaymentDetails().getPaymentStatus());
        bookingDetail.setStatus(job.getStatus().toString());
        if(job.getCustomerRating()!=null&& job.getCustomerRating().getRating() != null){
        bookingDetail.setRating(job.getCustomerRating().getRating().floatValue());
        bookingDetail.setReview(job.getCustomerRating().getReview());
        }else {
            // Optional: Explicitly set default if you want
            bookingDetail.setRating(0.0f);
        }

        return bookingDetail;


    }
    // 4. Retrieve all tasks for a specific worker
    public List<Job> getJobsByWorker(String workerId) {
        return jobRepository.findByWorkerId(workerId);
    }

    //5. Accept the job by worker
//    public Job acceptJob(String jobId) {
//        Job job = jobRepository.findById(jobId)
//                .orElseThrow(() -> new RuntimeException("Job not found"));
//
//        // Logic: Only a PENDING job can be ACCEPTED
//        if (job.getStatus() == JobStatus.PENDING) {
//            job.setStatus(JobStatus.ACCEPTED);
//            //Job acceptjob=
//                    jobRepository.save(job);
//            JobEvent event = new JobEvent(job.getJobId(), job.getCustomerId(), job.getWorkerId(),
//                    job.getServiceType(), "ACCEPTED", "Worker has accepted your job!");
//            kafkaTemplate.send(TOPIC, event);
//
//            return job;
//        } else {
//            throw new IllegalStateException("Job cannot be accepted in its current state: " + job.getStatus());
//        }
//    }
    public void rateAndReviewJob(String jobId, ReviewDTO reviewDto) {
        // 1. Fetch the job
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found with id: " + jobId));

        // 2. Business Rule: Only completed jobs can be rated
        if (!"COMPLETED".equals(job.getStatus())) {
            throw new IllegalStateException("You can only rate a job that has been marked as COMPLETED.");
        }

        // 3. Update the fields
        CustomerRating customerRating=new CustomerRating();
        customerRating.setRating((double)reviewDto.getRating());
        customerRating.setReview(reviewDto.getReview());
        //customerRating.setCreatedAt();
        job.setCustomerRating(customerRating);

        // 4. Save changes
        jobRepository.save(job);
    }

    public Double getAverageRatingForWorker(String workerId) {
        return jobRepository.getAverageRatingByWorkerId(workerId);
    }
    public List<ReviewModel> getTop3Reviews(String workerId) {
        // Create a PageRequest to limit result to 3
        Pageable limitThree = PageRequest.of(0, 3);

        List<Job> jobs = jobRepository.findReviewsByWorkerId(workerId, limitThree);

        // Map Job entities to ReviewModel DTOs
        return jobs.stream().map(job -> {
            ReviewModel review = new ReviewModel();
            // Assuming Job has a 'Customer' object or customerName field
            review.setReviewerName(job.getCustomerId() != null ? job.getCustomerId(): "Anonymous");
            review.setComment(job.getCustomerRating().getReview());
            review.setRating(job.getCustomerRating().getRating() != null ? job.getCustomerRating().getRating().floatValue() : 0.0f);
            review.setDate(job.getScheduledAt().toString()); // Or generic formatting
            return review;
        }).collect(Collectors.toList());
    }
}
