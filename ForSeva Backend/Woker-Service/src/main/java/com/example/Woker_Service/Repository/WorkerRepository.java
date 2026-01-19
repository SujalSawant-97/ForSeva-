package com.example.Woker_Service.Repository;

import com.example.Woker_Service.Model.Worker;
import org.springframework.data.geo.Distance;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkerRepository extends MongoRepository<Worker, String> {

    @Query("{ 'workerDetails.availability': true, 'workerDetails.skills': ?0, 'address.location': { $near: { $geometry: { type: 'Point', coordinates: [ ?1, ?2 ] }, $maxDistance: ?3 } } }")
    List<Worker> searchWorkers(
            String skill,
            double lng,
            double lat,
            double maxDistanceMeters
    );

}
