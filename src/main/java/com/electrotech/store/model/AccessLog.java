package com.electrotech.store.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "access_logs")
@Data
public class AccessLog {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userEmail;
    private String action;
    private String pageUrl;
    @Column(name = "ip_address")
    private String ipAddress;
    private LocalDateTime timestamp = LocalDateTime.now();
}