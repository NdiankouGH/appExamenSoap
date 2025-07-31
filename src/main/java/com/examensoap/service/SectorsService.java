package com.examensaop.service;

import com.examensoap.generated.Sectors;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class SectorsService {
    private final Map<Long, Sectors> students = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);
}
