package com.inm.tracewise.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inm.tracewise.model.dto.Trace;
public interface TraceRepository extends JpaRepository<Trace, Long> {
   List<Trace> findByTraceId(String traceId);
}
